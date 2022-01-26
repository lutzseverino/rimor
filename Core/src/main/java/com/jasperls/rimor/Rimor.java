package com.jasperls.rimor;

import com.jasperls.rimor.annotation.CommandNames;
import com.jasperls.rimor.type.Command;

import java.util.*;

public enum Rimor {
    INSTANCE;

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
}
