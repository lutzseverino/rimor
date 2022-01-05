package com.jasperls.rimor.type;

import com.jasperls.rimor.annotation.CommandNames;
import com.jasperls.rimor.annotation.MethodCommand;
import com.jasperls.rimor.annotation.MethodSubcommand;
import com.jasperls.rimor.method.impl.CommandMethod;
import com.jasperls.rimor.method.impl.SubcommandMethod;
import lombok.Getter;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;

public abstract class Command {
    private final Map<String, SubcommandMethod> subcommandMethodsMap = new HashMap<>();
    private final Map<String, SubcommandGroup> subcommandGroupsMap = new HashMap<>();
    public CommandMethod commandMethod;
    @Getter
    public List<SubcommandMethod> subcommandMethods;
    @Getter
    public List<SubcommandGroup> subcommandGroups;

    public Command() {
        Class<? extends Command> clazz = this.getClass();

        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(MethodCommand.class)) {
                this.addCommandMethod(this.findAliases(method), method);

                if (!subcommandMethodsMap.isEmpty())
                    this.subcommandMethodsMap.clear();

                continue;
            }

            if (method.isAnnotationPresent(MethodSubcommand.class)) {
                this.addSubcommandMethod(this.findAliases(method), method);
            }
        }
    }

    public Optional<CommandMethod> getCommandMethod() {
        return Optional.ofNullable(this.commandMethod);
    }

    public Optional<SubcommandMethod> getSubcommandMethod(String name) {
        return Optional.ofNullable(this.subcommandMethodsMap.get(name));
    }

    public Optional<SubcommandGroup> getSubcommandGroup(String name) {
        return Optional.ofNullable(this.subcommandGroupsMap.get(name));
    }

    public void addSubcommandGroups(SubcommandGroup... subcommandGroup) {
        for (SubcommandGroup group : subcommandGroup) {
            this.findAliases(group.getClass()).forEach(name -> this.subcommandGroupsMap.put(name, group));
        }
    }

    void addCommandMethod(Set<String> names, Method method) {
        this.commandMethod = new CommandMethod(names, method);
    }

    void addSubcommandMethod(Set<String> names, Method method) {
        names.forEach(name -> this.subcommandMethodsMap.put(name, new SubcommandMethod(method, this)));
    }

    Set<String> findAliases(AnnotatedElement element) {
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
