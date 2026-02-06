// Complete implementation of condition-based waiting utilities
// Translated from TypeScript to Java
// Context: Fixed flaky tests by replacing arbitrary timeouts with condition-based waiting

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class for condition-based waiting in tests.
 * Assumes a ThreadManager interface and LaceEvent class exist in the project.
 */
public class ConditionBasedWaiting {

    private static final long DEFAULT_POLL_INTERVAL_MS = 10;
    private static final long DEFAULT_TIMEOUT_MS = 5000;

    /**
     * Wait for a specific event type to appear in thread
     *
     * @param threadManager The thread manager to query
     * @param threadId Thread to check for events
     * @param eventType Type of event to wait for
     * @param timeoutMs Maximum time to wait
     * @return Future resolving to the first matching event
     */
    public static CompletableFuture<LaceEvent> waitForEvent(
            ThreadManager threadManager,
            String threadId,
            LaceEventType eventType,
            long timeoutMs) {
        
        return waitForEventMatch(
                threadManager,
                threadId,
                event -> event.getType() == eventType,
                "event type " + eventType,
                timeoutMs
        );
    }

    /**
     * Wait for a specific number of events of a given type
     *
     * @param threadManager The thread manager to query
     * @param threadId Thread to check for events
     * @param eventType Type of event to wait for
     * @param count Number of events to wait for
     * @param timeoutMs Maximum time to wait
     * @return Future resolving to all matching events once count is reached
     */
    public static CompletableFuture<List<LaceEvent>> waitForEventCount(
            ThreadManager threadManager,
            String threadId,
            LaceEventType eventType,
            int count,
            long timeoutMs) {

        CompletableFuture<List<LaceEvent>> future = new CompletableFuture<>();
        long startTime = System.currentTimeMillis();

        Runnable check = new Runnable() {
            @Override
            public void run() {
                List<LaceEvent> events = threadManager.getEvents(threadId);
                List<LaceEvent> matchingEvents = events.stream()
                        .filter(e -> e.getType() == eventType)
                        .collect(Collectors.toList());

                if (matchingEvents.size() >= count) {
                    future.complete(matchingEvents);
                } else if (System.currentTimeMillis() - startTime > timeoutMs) {
                    future.completeExceptionally(new TimeoutException(
                            String.format("Timeout waiting for %d %s events after %dms (got %d)",
                                    count, eventType, timeoutMs, matchingEvents.size())
                    ));
                } else {
                    // In a real async environment, use a scheduled executor
                    try {
                        Thread.sleep(DEFAULT_POLL_INTERVAL_MS);
                        this.run(); // Simple recursion for demo; use loop or scheduler in prod
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        future.completeExceptionally(e);
                    }
                }
            }
        };

        // Run in a separate thread to avoid blocking
        new Thread(check).start();
        return future;
    }

    /**
     * Wait for an event matching a custom predicate
     * Useful when you need to check event data, not just type
     *
     * @param threadManager The thread manager to query
     * @param threadId Thread to check for events
     * @param predicate Function that returns true when event matches
     * @param description Human-readable description for error messages
     * @param timeoutMs Maximum time to wait
     * @return Future resolving to the first matching event
     */
    public static CompletableFuture<LaceEvent> waitForEventMatch(
            ThreadManager threadManager,
            String threadId,
            Predicate<LaceEvent> predicate,
            String description,
            long timeoutMs) {

        CompletableFuture<LaceEvent> future = new CompletableFuture<>();
        long startTime = System.currentTimeMillis();

        Runnable check = new Runnable() {
            @Override
            public void run() {
                List<LaceEvent> events = threadManager.getEvents(threadId);
                LaceEvent event = events.stream()
                        .filter(predicate)
                        .findFirst()
                        .orElse(null);

                if (event != null) {
                    future.complete(event);
                } else if (System.currentTimeMillis() - startTime > timeoutMs) {
                    future.completeExceptionally(new TimeoutException(
                            String.format("Timeout waiting for %s after %dms", description, timeoutMs)
                    ));
                } else {
                    try {
                        Thread.sleep(DEFAULT_POLL_INTERVAL_MS);
                        this.run();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        future.completeExceptionally(e);
                    }
                }
            }
        };

        new Thread(check).start();
        return future;
    }

    // Mock interfaces for compilation (Replace with actual project classes)
    public interface ThreadManager {
        List<LaceEvent> getEvents(String threadId);
    }

    public interface LaceEvent {
        LaceEventType getType();
    }

    public enum LaceEventType {
        TOOL_RESULT, AGENT_MESSAGE, TOOL_CALL
    }
}
