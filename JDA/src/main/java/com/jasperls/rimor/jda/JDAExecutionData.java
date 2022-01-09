package com.jasperls.rimor.jda;

import com.jasperls.rimor.ExecutionData;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@Getter
@Setter
public class JDAExecutionData extends ExecutionData {
    private SlashCommandEvent event;
}
