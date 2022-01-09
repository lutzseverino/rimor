package com.jasperls.rimor.jda.option;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Map;

@Getter
public class RimorOption {
    private final OptionType type;
    private final String description;
    private final boolean required;
    private Map<String, RimorChoice> rimorChoiceMap;

    public RimorOption(OptionType type, String description, boolean required) {
        this.type = type;
        this.description = description;
        this.required = required;
    }

    public void addChoice(RimorChoice... choices) {
        for (RimorChoice choice : choices) {
            this.rimorChoiceMap.put(choice.getName(), choice);
        }
    }
}
