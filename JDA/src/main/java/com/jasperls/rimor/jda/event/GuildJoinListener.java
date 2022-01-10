package com.jasperls.rimor.jda.event;

import com.jasperls.rimor.Rimor;
import com.jasperls.rimor.jda.manager.JDARimorManager;
import com.jasperls.rimor.jda.type.JDACommand;
import com.jasperls.rimor.type.Command;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildJoinListener extends ListenerAdapter {
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        JDARimorManager jdaRimorManager = new JDARimorManager(event.getJDA());

        for (Command command : Rimor.INSTANCE.getAllCommands())
            if (command instanceof JDACommand jdaCommand && jdaCommand.isGuildOnlySetting())
                jdaRimorManager.registerCommands(event.getGuild());
    }
}
