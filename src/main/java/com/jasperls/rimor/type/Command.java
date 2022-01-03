package com.jasperls.rimor.type;

import com.jasperls.rimor.annotation.CommandNames;
import com.jasperls.rimor.annotation.MethodCommand;
import com.jasperls.rimor.annotation.MethodSubcommand;
import com.jasperls.rimor.method.impl.CommandMethod;
import com.jasperls.rimor.method.impl.SubcommandMethod;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;

public abstract class Command {
    private final Map<String, SubcommandMethod> subcommandMethods = new HashMap<>();
    private final Map<String, SubcommandGroup> subcommandGroups = new HashMap<>();
    private CommandMethod commandMethod;

    public Command() {
        Class<? extends Command> clazz = this.getClass();

        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(MethodCommand.class)) {
                this.addCommandMethod(this.getAliases(method), method);

                if (!subcommandMethods.isEmpty())
                    this.subcommandMethods.clear();

                continue;
            }

            if (method.isAnnotationPresent(MethodSubcommand.class)) {
                this.addSubcommandMethod(this.getAliases(method), method);
            }
        }
    }

    public void addSubcommandGroups(SubcommandGroup... subcommandGroup) {
        for (SubcommandGroup group : subcommandGroup) {
            this.getAliases(group.getClass()).forEach(name -> this.subcommandGroups.put(name, group));
        }
    }

    public Optional<CommandMethod> getCommandMethod() {
        return Optional.ofNullable(this.commandMethod);
    }

    public Optional<SubcommandMethod> getSubcommandMethod(String name) {
        return Optional.ofNullable(this.subcommandMethods.get(name));
    }

    public Optional<SubcommandGroup> getSubcommandGroup(String name) {
        return Optional.ofNullable(this.subcommandGroups.get(name));
    }

    void addCommandMethod(Set<String> names, Method method) {
        this.commandMethod = new CommandMethod(names, method);
    }

    void addSubcommandMethod(Set<String> names, Method method) {
        names.forEach(name -> this.subcommandMethods.put(name, new SubcommandMethod(method, this)));
    }

    Set<String> getAliases(AnnotatedElement element) {
        Set<String> names = new HashSet<>();

        if (element.isAnnotationPresent(CommandNames.class))
            names.addAll(Set.of(element.getAnnotation(CommandNames.class).value()));

        if (element instanceof Method method)
            names.add(method.getName().toLowerCase());
        else if (element instanceof Class<?> clazz)
            names.add(clazz.getSimpleName().toLowerCase());

        return names;
    }
}
