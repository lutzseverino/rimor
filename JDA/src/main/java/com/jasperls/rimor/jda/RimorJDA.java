package com.jasperls.rimor.jda;

import com.jasperls.rimor.Rimor;
import com.jasperls.rimor.jda.annotation.CommandDescription;
import com.jasperls.rimor.jda.event.SlashCommandEventListener;
import com.jasperls.rimor.jda.method.OptionMethod;
import com.jasperls.rimor.jda.type.JDACommand;
import com.jasperls.rimor.jda.type.OptionSubcommand;
import com.jasperls.rimor.method.SubcommandMethod;
import com.jasperls.rimor.type.Command;
import com.jasperls.rimor.type.SubcommandGroup;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RimorJDA extends Rimor {
    public void service(JDA jdaInstance, boolean updateCommands) {
        jdaInstance.addEventListener(new SlashCommandEventListener());
    }

    // TODO Options & Choices
    private void updateCommands(JDA jdaInstance) {
        List<CommandData> commandDataList = new ArrayList<>();
        List<SubcommandGroupData> subcommandGroupDataList = new ArrayList<>();

        for (Command command : this.getAllCommands()) {
            if (command instanceof JDACommand jdaCommand) {
                CommandData commandData = new CommandData(jdaCommand.getClass().getSimpleName(), jdaCommand.getClass().getAnnotation(CommandDescription.class).value());

                List<OptionMethod> optionMethodList = new ArrayList<>(jdaCommand.getJdaCommandMethod().getOptionMethods());

                if (!optionMethodList.isEmpty()) {
                    Collections.sort(optionMethodList);

                    for (OptionMethod optionMethod : optionMethodList) {
                        commandData.addOption(optionMethod.getOption().getType(), optionMethod.getMethod().getName().toLowerCase(), optionMethod.getOption().getDescription());
                    }
                }

                SubcommandGroupData subcommandGroupData;
                for (SubcommandGroup subcommandGroup : jdaCommand.getSubcommandGroups()) {
                    subcommandGroupData = new SubcommandGroupData(subcommandGroup.getClass().getSimpleName(),
                            subcommandGroup.getClass().getAnnotation(CommandDescription.class).value());

                    for (SubcommandMethod subcommandMethod : subcommandGroup.getSubcommandMethods()) {
                        subcommandGroupData.addSubcommands(new SubcommandData(subcommandMethod.getMethod().getName(),
                                subcommandMethod.getMethod().getAnnotation(CommandDescription.class).value()));
                    }

                    subcommandGroupDataList.add(subcommandGroupData);
                }

                for (OptionSubcommand optionSubcommand : jdaCommand.getOptionSubcommands()) {
                    // TODO finish
                }

                if (!subcommandGroupDataList.isEmpty()) {
                    subcommandGroupDataList.forEach(commandData::addSubcommandGroups);
                }

                commandDataList.add(commandData);
            }
        }
    }
}
