package com.jasperls.rimor.jda;

import com.jasperls.rimor.jda.annotation.CommandDescription;
import com.jasperls.rimor.jda.event.GuildJoinListener;
import com.jasperls.rimor.jda.event.SlashCommandListener;
import com.jasperls.rimor.jda.method.OptionMethod;
import com.jasperls.rimor.jda.option.RimorChoice;
import com.jasperls.rimor.jda.type.JDACommand;
import com.jasperls.rimor.jda.type.OptionSubcommand;
import com.jasperls.rimor.method.SubcommandMethod;
import com.jasperls.rimor.type.Command;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO Rework manager, make methods more implementable and fix
public class JDAManager {
    private final JDA jdaInstance;

    public JDAManager(JDA jdaInstance) {
        this.jdaInstance = jdaInstance;
    }

    public void startService() {
        this.jdaInstance.addEventListener(
                new SlashCommandListener(),
                new GuildJoinListener()
        );
    }

    public void uploadCommands(Guild guild, List<JDACommand> commands) {
        for (CommandData commandData : this.uploadCommands(commands))
            guild.upsertCommand(commandData).queue();
    }

    public void uploadCommands(JDA jdaInstance, List<JDACommand> commands) {
        for (CommandData commandData : this.uploadCommands(commands))
            jdaInstance.upsertCommand(commandData).queue();
    }

    List<CommandData> uploadCommands(List<JDACommand> commands) {
        List<CommandData> commandDataList = new ArrayList<>();

        for (JDACommand command : commands) {
            // Adds initial command
            CommandData commandData = new CommandData(command.getClass().getSimpleName().toLowerCase(),
                    command.getClass().getAnnotation(CommandDescription.class).value());

            List<OptionMethod> optionMethodList;

            if (command.getJdaCommandMethod() != null) {
                optionMethodList = new ArrayList<>(command.getJdaCommandMethod().getOptionMethods());
                this.findOptionsAndChoices(commandData, optionMethodList);
            }

            // Adds subcommands with no options
            if (!command.getSubcommandMethods().isEmpty()) {
                for (SubcommandMethod subcommandMethod : command.getSubcommandMethods()) {
                    SubcommandData subcommandData = new SubcommandData(subcommandMethod.getMethod().getName(),
                            subcommandMethod.getMethod().getAnnotation(CommandDescription.class).value());

                    commandData.addSubcommands(subcommandData);
                }
            }

            // Adds option subcommands
            if (!command.getOptionSubcommands().isEmpty()) {
                for (OptionSubcommand optionSubcommand : command.getOptionSubcommands()) {
                    SubcommandData subcommandData = new SubcommandData(optionSubcommand.getJdaCommandMethod().getMethod().getName(),
                            optionSubcommand.getClass().getAnnotation(CommandDescription.class).value());

                    commandData.addSubcommands(subcommandData);

                    optionMethodList = new ArrayList<>(optionSubcommand.getJdaCommandMethod().getOptionMethods());
                    this.findOptionsAndChoices(subcommandData, optionMethodList);
                }
            }

            // Adds subcommand groups
            if (!command.getSubcommandGroups().isEmpty()) {
                for (Command subcommandGroup : command.getSubcommandGroups()) {
                    SubcommandGroupData subcommandGroupData = new SubcommandGroupData(subcommandGroup.getClass().getSimpleName().toLowerCase(),
                            subcommandGroup.getClass().getAnnotation(CommandDescription.class).value());

                    SubcommandData subcommandData = null;
                    for (SubcommandMethod subcommandMethod : subcommandGroup.getSubcommandMethods()) {
                        subcommandData = new SubcommandData(subcommandMethod.getMethod().getName(),
                                subcommandMethod.getMethod().getAnnotation(CommandDescription.class).value());

                        subcommandGroupData.addSubcommands(subcommandData);
                    }

                    if (subcommandData != null) {
                        subcommandGroupData.addSubcommands(subcommandData);
                    }

                    commandData.addSubcommandGroups(subcommandGroupData);
                }
            }
            commandDataList.add(commandData);
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

                if (!optionMethod.getOption().getChoices().isEmpty()) {
                    this.findChoices(optionData, optionMethod);
                }
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

                if (!optionMethod.getOption().getChoices().isEmpty()) {
                    this.findChoices(optionData, optionMethod);
                }
            }
        }
    }

    void findChoices(OptionData optionData, OptionMethod optionMethod) {
        if (!optionMethod.getOption().getChoices().isEmpty()) {
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
}
