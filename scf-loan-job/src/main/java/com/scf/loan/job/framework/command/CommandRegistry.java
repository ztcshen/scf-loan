package com.scf.loan.job.framework.command;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CommandRegistry {
    private final Map<String, ScfCoreCommand<?>> commandMap = new ConcurrentHashMap<>();

    public void register(ScfCoreCommand<?> command) {
        if (command == null || command.getCommandType() == null) {
            return;
        }
        commandMap.put(command.getCommandType(), command);
    }

    public void unregister(String commandType) {
        if (commandType == null) {
            return;
        }
        commandMap.remove(commandType);
    }

    public ScfCoreCommand<?> getCommand(String commandType) {
        if (commandType == null) {
            return null;
        }
        return commandMap.get(commandType);
    }

    public Collection<ScfCoreCommand<?>> getAllCommands() {
        return commandMap.values();
    }
}
