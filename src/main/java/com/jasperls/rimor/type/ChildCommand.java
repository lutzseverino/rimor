package com.jasperls.rimor.type;

import com.jasperls.rimor.annotation.CommandNames;
import com.jasperls.rimor.annotation.LonelyCommand;
import com.jasperls.rimor.annotation.Subcommand;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class ChildCommand extends Command {

    private final Map<String, MethodSubcommand> subcommandMap = new HashMap<>();

    public ChildCommand() {
        for (Method method : this.getClass().getMethods()) {
            CommandNames names = method.getAnnotation(CommandNames.class);

            if (method.isAnnotationPresent(LonelyCommand.class)) {
                throw new IllegalArgumentException("A child command cannot contain a lonely command");
            }

            if (method.isAnnotationPresent(Subcommand.class)) {
                if (names != null) {
                    for (String name : names.value()) {
                        this.subcommandMap.put(name.toLowerCase(), new MethodSubcommand(method, this));
                    }
                }
                this.subcommandMap.put(method.getName().toLowerCase(), new MethodSubcommand(method, this));
            }
        }
    }
}

