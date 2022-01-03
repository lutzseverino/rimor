package com.jasperls.rimor;

import com.jasperls.rimor.annotation.CommandNames;
import com.jasperls.rimor.method.RimorMethod;
import com.jasperls.rimor.method.impl.CommandMethod;
import com.jasperls.rimor.method.impl.SubcommandMethod;
import com.jasperls.rimor.type.Command;
import com.jasperls.rimor.type.SubcommandGroup;

import java.util.*;

public class RimorImpl implements Rimor {
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Registers {@link Command} instances by linking them their
     * {@link String} aliases and putting them into {@link RimorImpl#commands}
     *
     * @param commands an array of {@link Command} to be registered
     * @see CommandNames
     */
    @Override
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

    @Override
    public Optional<Command> getCommand(String name) {
        return Optional.ofNullable(this.commands.get(name));
    }

    @Override
    public Collection<Command> getAllCommands() {
        return commands.values();
    }

    /**
     * Recursively looks for a {@link RimorMethod} and invokes it.
     * <p>
     * Starts by checking if the path provided by {@param data} is empty,
     * if so, it looks for a {@link CommandMethod} and invokes it.
     * <p>
     * If the list is not empty, it looks for any {@link SubcommandMethod},
     * invokes it with all remaining path entries as parameters it if successful,
     * looks for a {@link SubcommandGroup} otherwise.
     * <p>
     * If a {@link SubcommandGroup} is available, it re-calls this method with
     * this new {@link Command}, otherwise it looks for a {@link CommandMethod}
     * and passes all remaining path entries as parameters.
     *
     * @param command the {@link Command} for which to look recursively in for
     * @param data    a data source that is, or extends {@link ExecutionData}
     * @throws NullPointerException if no command method, subcommand method or subcommand group is found
     * @see RimorImpl#execute(String[], ExecutionData)
     */
    @Override
    public void execute(Command command, ExecutionData data) {
        List<String> path = data.getParameters();

        if (path.isEmpty()) {
            command.getCommandMethod().ifPresentOrElse(
                    commandMethod -> commandMethod.invoke(command, data),
                    () -> {
                        throw new NullPointerException("No command method was found.");
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
                                    throw new NullPointerException("No subcommand method, subcommand group or command method for \"" + path.get(0) + "\" was found.");
                                }
                        )
                )
        );
    }

    /**
     * Sets the path as parameters to {@param data} excluding the first entry,
     * then looks for a {@link Command} that matches the first {@param path} entry.
     * Executes {@link RimorImpl#execute(Command, ExecutionData)} afterwards.
     *
     * @param path a path with each individual command steps
     * @param data a data source that is, or extends {@link ExecutionData}
     */
    public void execute(String[] path, ExecutionData data) {
        data.setParameters(List.of(Arrays.copyOfRange(path, 1, path.length)));

        this.getCommand(path[0]).ifPresentOrElse(
                command -> this.execute(command, data),
                () -> {
                    throw new NullPointerException("No command named \"" + path[0] + "\" was found.");
                }
        );
    }
}
