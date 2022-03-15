package com.jasperls.rimor.jda.type;

import com.jasperls.rimor.annotation.MethodCommand;
import com.jasperls.rimor.jda.annotation.MethodOption;
import com.jasperls.rimor.jda.method.JDACommandMethod;
import com.jasperls.rimor.jda.option.RimorOption;
import com.jasperls.rimor.type.Command;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.*;

public abstract class JDACommand extends Command {

    private final Map<String, OptionSubcommand> optionSubcommandMap = new HashMap<>();
    @Getter private JDACommandMethod jdaCommandMethod;

    public JDACommand() {
        super();
        Map<String, com.jasperls.rimor.jda.method.OptionMethod> optionMethods = new HashMap<>();

        for (Method method : this.getClass().getMethods()) {
            MethodOption methodOption = method.getAnnotation(MethodOption.class);

            if (methodOption != null) {
                RimorOption rimorOption = new RimorOption();

                Set<String> optionNames = this.findAliases(method);
                optionNames.forEach(name -> optionMethods.put(name, new com.jasperls.rimor.jda.method.OptionMethod(method, rimorOption)));
            }
        }

        for (Method method : this.getClass().getMethods()) {
            if (method.isAnnotationPresent(MethodCommand.class)) {
                JDACommandMethod jdaCommandMethod = new JDACommandMethod(method);

                optionMethods.forEach(jdaCommandMethod::addOptionMethod);
                this.setJdaCommandMethod(jdaCommandMethod);
            }
        }
    }

    public void addOptionSubcommand(OptionSubcommand... optionSubcommands) {
        for (OptionSubcommand optionSubcommand : optionSubcommands) {
            this.findAliases(optionSubcommand.getClass()).forEach(name -> this.optionSubcommandMap.put(name, optionSubcommand));
        }
    }

    public OptionSubcommand getOptionSubcommand(String name) {
        return this.optionSubcommandMap.get(name);
    }

    public List<OptionSubcommand> getOptionSubcommands() {
        return new ArrayList<>(this.optionSubcommandMap.values());
    }

    void setJdaCommandMethod(JDACommandMethod method) {
        this.jdaCommandMethod = method;
    }
}
