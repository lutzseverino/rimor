package com.jasperls.rimor.jda.event;

import com.jasperls.rimor.jda.data.JDAExecutionData;
import com.jasperls.rimor.jda.interpreter.JDAInterpreter;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SlashCommandListener extends ListenerAdapter {

    private final JDAInterpreter interpreter = new JDAInterpreter();

    @Override public void onSlashCommand(@NotNull SlashCommandEvent event) {
        JDAExecutionData jdaExecutionData = new JDAExecutionData();

        jdaExecutionData.setEvent(event);
        this.interpreter.execute(event.getCommandPath().split("/"), jdaExecutionData);
    }
}
