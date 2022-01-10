package com.jasperls.rimor.jda.manager;

import com.jasperls.rimor.Rimor;
import com.jasperls.rimor.jda.annotation.CommandDescription;
import com.jasperls.rimor.jda.event.GuildJoinListener;
import com.jasperls.rimor.jda.event.SlashCommandListener;
import com.jasperls.rimor.jda.method.OptionMethod;
import com.jasperls.rimor.jda.option.RimorChoice;
import com.jasperls.rimor.jda.type.JDACommand;
import com.jasperls.rimor.jda.type.OptionSubcommand;
import com.jasperls.rimor.method.SubcommandMethod;
import com.jasperls.rimor.type.Command;
import com.jasperls.rimor.type.SubcommandGroup;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDARimorManager {
    private final JDA jdaInstance;

    public JDARimorManager(JDA jdaInstance) {
        this.jdaInstance = jdaInstance;
    }

    public void service() {
        this.addEventListeners();
    }

    public void service(Guild guild) {
        this.addEventListeners();
        this.registerCommands(guild);
    }

    public void registerCommands(Guild guild) {
        for (CommandData commandData : this.registerCommands())
            guild.upsertCommand(commandData);
    }

    public void registerCommands(JDA jdaInstance) {
        for (CommandData commandData : this.registerCommands())
            jdaInstance.upsertCommand(commandData);
    }

    void addEventListeners() {
        this.jdaInstance.addEventListener(
                new SlashCommandListener(),
                new GuildJoinListener()
        );
    }

    List<CommandData> registerCommands() {
        List<CommandData> commandDataList = new ArrayList<>();

        for (Command command : Rimor.INSTANCE.getAllCommands()) {
            if (command instanceof JDACommand jdaCommand) {
                // Adds initial command
                CommandData commandData = new CommandData(jdaCommand.getClass().getSimpleName(),
                        jdaCommand.getClass().getAnnotation(CommandDescription.class).value());

                List<OptionMethod> optionMethodList = new ArrayList<>(jdaCommand.getJdaCommandMethod().getOptionMethods());
                this.findOptionsAndChoices(commandData, optionMethodList);

                // Adds subcommands with no options
                if (!jdaCommand.getSubcommandMethods().isEmpty()) {
                    for (SubcommandMethod subcommandMethod : jdaCommand.getSubcommandMethods()) {
                        SubcommandData subcommandData = new SubcommandData(subcommandMethod.getMethod().getName(),
                                subcommandMethod.getMethod().getAnnotation(CommandDescription.class).value());

                        commandData.addSubcommands(subcommandData);
                    }
                }

                // Adds option subcommands
                if (!jdaCommand.getOptionSubcommands().isEmpty()) {
                    for (OptionSubcommand optionSubcommand : jdaCommand.getOptionSubcommands()) {
                        SubcommandData subcommandData = new SubcommandData(optionSubcommand.getJdaCommandMethod().getMethod().getName(),
                                optionSubcommand.getJdaCommandMethod().getMethod().getAnnotation(CommandDescription.class).value());

                        commandData.addSubcommands(subcommandData);

                        optionMethodList = new ArrayList<>(optionSubcommand.getJdaCommandMethod().getOptionMethods());
                        this.findOptionsAndChoices(subcommandData, optionMethodList);
                    }
                }

                // Adds subcommand groups
                if (!jdaCommand.getSubcommandGroups().isEmpty()) {
                    for (SubcommandGroup subcommandGroup : jdaCommand.getSubcommandGroups()) {
                        SubcommandGroupData subcommandGroupData = new SubcommandGroupData(subcommandGroup.getClass().getSimpleName(),
                                subcommandGroup.getClass().getAnnotation(CommandDescription.class).value());

                        SubcommandData subcommandData = null;
                        for (SubcommandMethod subcommandMethod : subcommandGroup.getSubcommandMethods()) {
                            subcommandData = new SubcommandData(subcommandMethod.getMethod().getName(),
                                    subcommandMethod.getMethod().getAnnotation(CommandDescription.class).value());

                            subcommandGroupData.addSubcommands(subcommandData);

                            optionMethodList = new ArrayList<>(jdaCommand.getJdaCommandMethod().getOptionMethods());
                            this.findOptionsAndChoices(subcommandData, optionMethodList);
                        }

                        if (subcommandData != null) {
                            subcommandGroupData.addSubcommands(subcommandData);
                        }

                        commandData.addSubcommandGroups(subcommandGroupData);
                    }
                }
                commandDataList.add(commandData);
            }
        }
        return commandDataList;
    }

    void findOptionsAndChoices(CommandData command, List<OptionMethod> optionMethodList) {
        if (!optionMethodList.isEmpty()) {
            Collections.sort(optionMethodList);

            OptionData optionData;

            for (OptionMethod optionMethod : optionMethodList) {
                optionData = new OptionData(
                        optionMethod.getOption().getType(),
                        optionMethod.getMethod().getName().toLowerCase(),
                        optionMethod.getOption().getDescription()
                );
                command.addOptions(optionData);

                this.findChoices(optionData, optionMethod);
            }
        }
    }

    void findOptionsAndChoices(SubcommandData command, List<OptionMethod> optionMethodList) {
        if (!optionMethodList.isEmpty()) {
            Collections.sort(optionMethodList);

            OptionData optionData;

            for (OptionMethod optionMethod : optionMethodList) {
                optionData = new OptionData(
                        optionMethod.getOption().getType(),
                        optionMethod.getMethod().getName().toLowerCase(),
                        optionMethod.getOption().getDescription()
                );
                command.addOptions(optionData);

                this.findChoices(optionData, optionMethod);
            }
        }
    }

    void findChoices(OptionData optionData, OptionMethod optionMethod) {
        for (RimorChoice choice : optionMethod.getOption().getChoices()) {
            List<net.dv8tion.jda.api.interactions.commands.Command.Choice> choiceList = new ArrayList<>();

            if (!choice.getStringValues().isEmpty())
                for (String stringValue : choice.getStringValues())
                    choiceList.add(new net.dv8tion.jda.api.interactions.commands.Command.Choice(
                            choice.getName(),
                            stringValue)
                    );

            else if (!choice.getLongValues().isEmpty())
                for (long longValue : choice.getLongValues())
                    choiceList.add(new net.dv8tion.jda.api.interactions.commands.Command.Choice(
                            choice.getName(),
                            longValue)
                    );

            else if (!choice.getDoublesValues().isEmpty())
                for (double doubleValue : choice.getDoublesValues())
                    choiceList.add(new net.dv8tion.jda.api.interactions.commands.Command.Choice(
                            choice.getName(),
                            doubleValue)
                    );

            optionData.addChoices(choiceList);
        }
    }
}
