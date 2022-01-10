package com.jasperls.rimor.jda.type;

import com.jasperls.rimor.annotation.MethodCommand;
import com.jasperls.rimor.jda.annotation.OptionChoice;
import com.jasperls.rimor.jda.annotation.OptionChoices;
import com.jasperls.rimor.jda.annotation.OptionDetails;
import com.jasperls.rimor.jda.method.JDACommandMethod;
import com.jasperls.rimor.jda.method.OptionMethod;
import com.jasperls.rimor.jda.option.RimorChoice;
import com.jasperls.rimor.jda.option.RimorOption;
import com.jasperls.rimor.type.Command;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.*;

public abstract class JDACommand extends Command {
    private final Map<String, OptionSubcommand> optionSubcommandMap = new HashMap<>();
    @Getter
    private JDACommandMethod jdaCommandMethod;

    public JDACommand() {
        super();
        Map<String, OptionMethod> optionMethods = new HashMap<>();

        for (Method method : this.getClass().getMethods()) {
            OptionDetails optionDetails = method.getAnnotation(OptionDetails.class);
            OptionChoices optionChoices = method.getAnnotation(OptionChoices.class);

            if (optionDetails != null) {
                RimorOption rimorOption = new RimorOption(optionDetails.type(), optionDetails.description(), optionDetails.required());

                if (optionChoices != null) {
                    for (OptionChoice optionChoice : optionChoices.value()) {
                        RimorChoice rimorChoice = new RimorChoice(optionChoice.name(), optionChoice.description());

                        if (optionChoice.stringValues().length != 0)
                            for (String s : optionChoice.stringValues())
                                rimorChoice.addValue(s);

                        else if (optionChoice.longValues().length != 0)
                            for (long l : optionChoice.longValues())
                                rimorChoice.addValue(l);

                        else if (optionChoice.doubleValues().length != 0)
                            for (double d : optionChoice.doubleValues())
                                rimorChoice.addValue(d);

                        else
                            throw new IllegalArgumentException("Declared choice doesn't contain choices of any type");

                        rimorOption.addChoice(rimorChoice);
                    }
                }

                Set<String> optionNames = this.findAliases(method);
                optionNames.forEach(names -> optionMethods.put(names, new OptionMethod(method, optionDetails.position(), rimorOption)));

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
