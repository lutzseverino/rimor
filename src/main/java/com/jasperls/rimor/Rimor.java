package com.jasperls.rimor;

import com.jasperls.rimor.annotation.CommandNames;
import com.jasperls.rimor.type.Command;

import java.util.*;

public class Rimor {
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Adds your desired {@link Command} object/s to a {@link Map<String, Command>} for access and evaluation.
     *
     * @param commands a single or multiple {@link Command} to register
     */
    public void registerCommands(Command... commands) {
        for (Command command : commands) {
            List<String> aliases = new ArrayList<>();

            if (command.getClass().isAnnotationPresent(CommandNames.class) && command.getClass().getAnnotation(CommandNames.class).value() != null)
                aliases = Arrays.asList(command.getClass().getAnnotation(CommandNames.class).value());
            else aliases.add(command.getClass().getSimpleName().toLowerCase());

            for (String alias : aliases) {
                this.commands.put(alias.toLowerCase(), command);
            }
        }
    }

    /**
     * Gets a single {@link Command} using a linked name.
     *
     * @param name a command name
     * @return an {@link Optional<Command>} that matches the provided name
     */
    public Optional<Command> getCommand(String name) {
        return Optional.ofNullable(this.commands.get(name));
    }

    /**
     * Returns all saved {@link Command} objects regardless their name or properties.
     *
     * @return a {@link Collection} of all available {@link Command}
     */
    public Collection<Command> getAllCommands() {
        Collection<Command> commands = new HashSet<>();

        for (Command command : this.commands.values()) {
            if (!commands.contains(command)) {
                commands.add(command);
            }
        }
        return commands;
    }
}
