package com.jasperls.rimor.jda.option;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RimorOption {
    @Getter
    private final OptionType type;
    private final Map<String, RimorChoice> rimorChoiceMap = new HashMap<>();

    public RimorOption(OptionType type) {
        this.type = type;
    }

    public void addChoice(RimorChoice... choices) {
        for (RimorChoice choice : choices) {
            this.rimorChoiceMap.put(choice.getName(), choice);
        }
    }

    public List<RimorChoice> getChoices() {
        return new ArrayList<>(this.rimorChoiceMap.values());
    }
}
