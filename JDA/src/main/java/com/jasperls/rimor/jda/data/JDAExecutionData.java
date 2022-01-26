package com.jasperls.rimor.jda.data;

import com.jasperls.rimor.data.ExecutionData;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@Getter
@Setter
public class JDAExecutionData extends ExecutionData {
    private SlashCommandEvent event;
}
