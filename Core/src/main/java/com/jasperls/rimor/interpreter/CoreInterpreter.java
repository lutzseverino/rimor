package com.jasperls.rimor.interpreter;

import com.jasperls.rimor.ExecutionData;
import com.jasperls.rimor.Rimor;
import com.jasperls.rimor.method.RimorMethod;
import com.jasperls.rimor.type.Command;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CoreInterpreter implements RimorInterpreter {
    private Command commandInstance;

    @Override
    public void execute(Rimor rimorInstance, String[] path, ExecutionData data) {
        this.findMethod(rimorInstance, path, data).ifPresentOrElse(method -> method.invoke(commandInstance, data),
                () -> {
                    throw new IllegalArgumentException("");
                });
    }

    /**
     * @param path a path with each individual command steps
     * @param data a data source that is, or extends {@link ExecutionData}
     * @return the final {@link RimorMethod}
     * @throws IllegalArgumentException if no command is found
     */
    @Override
    public Optional<? extends RimorMethod> findMethod(Rimor rimorInstance, String[] path, ExecutionData data) {
        data.setParameters(List.of(Arrays.copyOfRange(path, 1, path.length)));

        Optional<Command> command = rimorInstance.getCommand(path[0]);

        if (command.isPresent())
            return this.findMethod(command.get(), data);

        return Optional.empty();
    }

    /**
     * Recursively looks for a {@link RimorMethod}.
     *
     * @param command the {@link Command} for which to look recursively in for
     * @param data    a data source that is, or extends {@link ExecutionData}
     * @return the final {@link RimorMethod}
     * @throws IllegalArgumentException if no command method, subcommand method or subcommand group is found
     * @see CoreInterpreter#findMethod(Rimor, String[], ExecutionData)
     */
    public Optional<? extends RimorMethod> findMethod(Command command, ExecutionData data) {
        List<String> path = data.getParameters();

        if (path.isEmpty())
            return Optional.of(command.getCommandMethod());

        if (command.getSubcommandMethod(path.get(0)) != null) {
            data.setParameters(path.subList(1, path.size()));
            return Optional.of(command.getSubcommandMethod(path.get(0)));

        } else if (command.getSubcommandGroup(path.get(0)) != null) {
            data.setParameters(path.subList(1, path.size()));
            findMethod(command.getSubcommandGroup(path.get(0)), data);

        } else if (command.getCommandMethod() != null) {
            data.setParameters(path.subList(1, path.size()));
            return Optional.of(command.getCommandMethod());
        }

        return Optional.empty();
    }
}
