package com.jasperls.rimor;

import com.jasperls.rimor.annotation.CommandNames;
import com.jasperls.rimor.method.RimorMethod;
import com.jasperls.rimor.type.Command;

import java.util.*;

public class Rimor {
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Registers {@link Command} instances by linking them their
     * {@link String}.
     *
     * @param commands an array of {@link Command} to be registered
     * @see CommandNames
     */
    public void registerCommands(Command... commands) {
        for (Command command : commands) {
            List<String> aliases = new ArrayList<>();

            CommandNames commandNames = command.getClass().getAnnotation(CommandNames.class);

            if (commandNames != null && commandNames.value() != null)
                Collections.addAll(aliases, commandNames.value());

            aliases.add(command.getClass().getSimpleName().toLowerCase());

            for (String alias : aliases) {
                this.commands.put(alias.toLowerCase(), command);
            }
        }
    }

    public Optional<Command> getCommand(String name) {
        return Optional.ofNullable(this.commands.get(name));
    }

    public Collection<Command> getAllCommands() {
        return commands.values();
    }

    /**
     * @param path a path with each individual command steps
     * @param data a data source that is, or extends {@link ExecutionData}
     * @throws IllegalArgumentException if no command is found
     */
    public void execute(String[] path, ExecutionData data) {
        data.setParameters(List.of(Arrays.copyOfRange(path, 1, path.length)));

        this.getCommand(path[0]).ifPresentOrElse(
                command -> this.execute(command, data),
                () -> {
                    throw new IllegalArgumentException("No command named \"" + path[0] + "\" was found.");
                }
        );
    }

    /**
     * Recursively looks for a {@link RimorMethod} and invokes it.
     *
     * @param command the {@link Command} for which to look recursively in for
     * @param data    a data source that is, or extends {@link ExecutionData}
     * @throws IllegalArgumentException if no command method, subcommand method or subcommand group is found
     * @see Rimor#execute(String[], ExecutionData)
     */
    void execute(Command command, ExecutionData data) {
        List<String> path = data.getParameters();

        if (path.isEmpty()) {
            command.getCommandMethod().ifPresentOrElse(
                    commandMethod -> commandMethod.invoke(command, data),
                    () -> {
                        throw new IllegalArgumentException("No command method was found.");
                    }
            );
            return;
        }

        command.getSubcommandMethod(path.get(0)).ifPresentOrElse(
                subcommandMethod -> {
                    data.setParameters(path.subList(1, path.size()));
                    subcommandMethod.invoke(command, data);
                },
                () -> command.getSubcommandGroup(path.get(0)).ifPresentOrElse(
                        subcommandGroup -> {
                            data.setParameters(path.subList(1, path.size()));
                            execute(subcommandGroup, data);
                        },
                        () -> command.getCommandMethod().ifPresentOrElse(
                                commandMethod -> {
                                    data.setParameters(path.subList(1, path.size()));
                                    commandMethod.invoke(command, data);
                                },
                                () -> {
                                    throw new IllegalArgumentException("No subcommand method, subcommand group or command method for \"" + path.get(0) + "\" was found.");
                                }
                        )
                )
        );
    }
}
