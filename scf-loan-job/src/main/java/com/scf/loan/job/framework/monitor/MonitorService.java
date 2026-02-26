package com.scf.loan.job.framework.monitor;

import com.scf.loan.job.framework.model.RouteResult;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MonitorService {
    private final MeterRegistry meterRegistry;

    @Autowired
    public MonitorService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void recordRoute(RouteResult routeResult, long durationMillis) {
        if (routeResult != null && routeResult.isHit()) {
            meterRegistry.counter("scf.job.route.hit", "strategy", value(routeResult.getStrategyName())).increment();
        } else {
            meterRegistry.counter("scf.job.route.miss").increment();
        }
        meterRegistry.timer("scf.job.route.time").record(durationMillis, TimeUnit.MILLISECONDS);
    }

    public void recordCommand(String commandType, boolean success, long durationMillis) {
        meterRegistry.counter("scf.job.command.execute", "command", value(commandType)).increment();
        if (success) {
            meterRegistry.counter("scf.job.command.success", "command", value(commandType)).increment();
        } else {
            meterRegistry.counter("scf.job.command.failed", "command", value(commandType)).increment();
        }
        meterRegistry.timer("scf.job.command.time", "command", value(commandType)).record(durationMillis, TimeUnit.MILLISECONDS);
    }

    public void recordExecute(String executeType) {
        meterRegistry.counter("scf.job.execute." + value(executeType)).increment();
    }

    public void recordDispatcherError(String errorType) {
        meterRegistry.counter("scf.job.system.error", "error_type", value(errorType)).increment();
    }

    private String value(String value) {
        return value == null ? "unknown" : value;
    }
}
