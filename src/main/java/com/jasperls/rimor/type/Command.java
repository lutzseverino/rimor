package com.jasperls.rimor.type;

import com.jasperls.rimor.annotation.CommandNames;
import com.jasperls.rimor.annotation.LonelyCommand;
import com.jasperls.rimor.annotation.Subcommand;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.*;

@Getter
public abstract class Command {

    private final Map<String, MethodSubcommand> subcommandMap = new HashMap<>();
    private final Map<String, ChildCommand> childCommandMap = new HashMap<>();
    private Method lonelyMethod;

    public Command() {
        for (Method method : this.getClass().getMethods()) {
            List<String> names = new ArrayList<>();

            if (method.isAnnotationPresent(CommandNames.class) && method.getAnnotation(CommandNames.class).value() != null)
                names = Arrays.asList(method.getAnnotation(CommandNames.class).value());
            else names.add(method.getName().toLowerCase());

            if (method.isAnnotationPresent(LonelyCommand.class)) {
                this.lonelyMethod = method;
            }

            if (method.isAnnotationPresent(Subcommand.class)) {
                for (String name : names) {
                    this.subcommandMap.put(name.toLowerCase(), new MethodSubcommand(method, this));
                }
                this.subcommandMap.put(method.getName().toLowerCase(), new MethodSubcommand(method, this));
            }
        }
    }

    /**
     * <p>
     * Child commands will mimic Discord's subcommand groups hierarchy, this applies for legacy commands.
     * </p>
     *
     * @param childCommand a {@link ChildCommand} object
     */
    public void addChild(ChildCommand childCommand) {
        CommandNames names = childCommand.getClass().getAnnotation(CommandNames.class);

        if (names != null) {
            for (String name : names.value()) {
                this.childCommandMap.put(name.toLowerCase(), childCommand);
            }
        }

        this.childCommandMap.put(childCommand.getClass().getSimpleName().toLowerCase(), childCommand);
    }
}
