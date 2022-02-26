package com.jasperls.rimor.jda.data;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

@Getter
public class OptionExecutionData {

    OptionMapping optionMapping;

    public OptionExecutionData(OptionMapping optionMapping) {
        this.optionMapping = optionMapping;
    }
}
