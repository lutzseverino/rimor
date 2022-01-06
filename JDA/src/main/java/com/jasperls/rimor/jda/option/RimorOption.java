package com.jasperls.rimor.jda.option;

import com.jasperls.rimor.jda.annotation.OptionChoice;
import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@Getter
public class RimorOption {
    private final OptionType type;
    private final String name;
    private final String description;
    private final boolean required;
    private final OptionChoice[] choices;
    private RimorChoice choice;

    public RimorOption(OptionType type, String name, String description, boolean required, OptionChoice[] choices) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.required = required;
        this.choices = choices;
    }

    public RimorOption(OptionType type, String name, String description, boolean required, OptionChoice[] choices, RimorChoice choice) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.required = required;
        this.choices = choices;
        this.choice = choice;
    }
}
