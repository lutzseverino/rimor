package com.jasperls.rimor.type;

import com.jasperls.rimor.annotation.CommandNames;
import com.jasperls.rimor.annotation.MethodCommand;
import com.jasperls.rimor.annotation.MethodSubcommand;
import com.jasperls.rimor.method.CommandMethod;
import com.jasperls.rimor.method.SubcommandMethod;
import lombok.Getter;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;

public abstract class Command {
    private final Map<String, SubcommandMethod> subcommandMethodMap = new HashMap<>();
    private final Map<String, SubcommandGroup> subcommandGroupMap = new HashMap<>();
    @Getter
    private CommandMethod commandMethod;
    @Getter
    private final List<SubcommandMethod> subcommandMethods = new ArrayList<>();
    @Getter
    private final List<SubcommandGroup> subcommandGroups = new ArrayList<>();

    public Command() {
        Class<? extends Command> clazz = this.getClass();

        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(MethodCommand.class)) {
                this.setCommandMethod(new CommandMethod(method));

                if (!subcommandMethodMap.isEmpty())
                    this.subcommandMethodMap.clear();

                continue;
            }

            if (method.isAnnotationPresent(MethodSubcommand.class)) {
                this.addSubcommandMethod(this.findAliases(method), method);
            }
        }
    }

    public SubcommandMethod getSubcommandMethod(String name) {
        return this.subcommandMethodMap.get(name);
    }

    public SubcommandGroup getSubcommandGroup(String name) {
        return this.subcommandGroupMap.get(name);
    }

    public void addSubcommandGroups(SubcommandGroup... subcommandGroup) {
        for (SubcommandGroup group : subcommandGroup) {
            this.findAliases(group.getClass()).forEach(name -> this.subcommandGroupMap.put(name, group));
        }
    }

    protected void setCommandMethod(CommandMethod method) {
        this.commandMethod = method;
    }

    protected void addSubcommandMethod(Set<String> names, Method method) {
        names.forEach(name -> this.subcommandMethodMap.put(name, new SubcommandMethod(method)));
    }

    protected Set<String> findAliases(AnnotatedElement element) {
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
