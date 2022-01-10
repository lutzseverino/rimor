package com.jasperls.rimor.jda.event;

import com.jasperls.rimor.Rimor;
import com.jasperls.rimor.jda.manager.JDAManager;
import com.jasperls.rimor.jda.type.JDACommand;
import com.jasperls.rimor.type.Command;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GuildJoinListener extends ListenerAdapter {
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        JDAManager jdaManager = new JDAManager(event.getJDA());

        List<JDACommand> jdaCommandList = new ArrayList<>();

        for (Command command : Rimor.INSTANCE.getAllCommands())
            if (command instanceof JDACommand jdaCommand && jdaCommand.isGuildOnlySetting())
                jdaCommandList.add(jdaCommand);

        jdaManager.uploadCommands(event.getGuild(), jdaCommandList);

    }
}
