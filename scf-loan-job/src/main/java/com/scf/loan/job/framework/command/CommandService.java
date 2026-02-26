package com.scf.loan.job.framework.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandService {
    private final CommandRegistry commandRegistry;

    @Autowired
    public CommandService(CommandRegistry commandRegistry, List<ScfCoreCommand<?>> commands) {
        this.commandRegistry = commandRegistry;
        if (commands != null) {
            for (ScfCoreCommand<?> command : commands) {
                commandRegistry.register(command);
            }
        }
    }

    public ScfCoreCommand<?> getCommand(String commandType) {
        return commandRegistry.getCommand(commandType);
    }

    public void register(ScfCoreCommand<?> command) {
        commandRegistry.register(command);
    }
}
