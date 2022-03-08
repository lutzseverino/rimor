package com.jasperls.rimor.jda.data;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

@Getter
@Setter
public class JDAOptionData {

    OptionMapping optionMapping;

    public JDAOptionData(OptionMapping optionMapping) {
        this.optionMapping = optionMapping;
    }
}
