package com.jasperls.rimor;

import com.jasperls.rimor.type.Command;

import java.util.Collection;
import java.util.Optional;

public interface Rimor {
    void registerCommands(Command... commands);

    Optional<Command> getCommand(String name);

    Collection<Command> getAllCommands();

    void execute(Command command, ExecutionData data);
}
