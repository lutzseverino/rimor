package com.jasperls.rimor.jda.data;

import com.jasperls.rimor.data.Data;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@Getter
public class JDACommandData extends Data {

    private final SlashCommandEvent event;


    public JDACommandData(SlashCommandEvent event) {
        this.event = event;
    }
}
