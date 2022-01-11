package com.jasperls.rimor.jda.interpreter;

import com.jasperls.rimor.ExecutionData;
import com.jasperls.rimor.Rimor;
import com.jasperls.rimor.interpreter.CoreInterpreter;
import com.jasperls.rimor.interpreter.RimorInterpreter;
import com.jasperls.rimor.jda.JDAExecutionData;
import com.jasperls.rimor.jda.OptionExecutionData;
import com.jasperls.rimor.jda.method.JDACommandMethod;
import com.jasperls.rimor.jda.method.OptionMethod;
import com.jasperls.rimor.jda.type.JDACommand;
import com.jasperls.rimor.method.RimorMethod;
import com.jasperls.rimor.type.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class JDAInterpreter implements RimorInterpreter {
    private JDACommand commandInstance;

    @Override
    public void execute(String[] path, ExecutionData data) {
        if (data instanceof JDAExecutionData jdaData) {
            this.findMethod(path, data).ifPresentOrElse(method -> {

                if (method instanceof JDACommandMethod jdaMethod) {
                    for (OptionMapping option : jdaData.getEvent().getOptions()) {
                        OptionMethod optionMethod = jdaMethod.getOptionMethod(option.getName());

                        optionMethod.invoke(commandInstance, new OptionExecutionData(option));
                    }
                }
                method.invoke(commandInstance, jdaData);

            }, () -> {
                throw new IllegalArgumentException("Couldn't find method to execute with path " + Arrays.toString(path));
            });
        } else
            throw new IllegalArgumentException("Data is missing properties due to wrong instancing");
    }

    @Override
    public Optional<? extends RimorMethod> findMethod(String[] path, ExecutionData data) {
        data.setParameters(List.of(Arrays.copyOfRange(path, 1, path.length)));

        Optional<Command> command = Rimor.INSTANCE.getCommand(path[0]);

        if (command.isPresent() && command.get() instanceof JDACommand jdaCommand)
            return this.findMethod(jdaCommand, data);

        return Optional.empty();
    }

    /**
     * Recursively looks for a {@link RimorMethod}.
     *
     * @param command the {@link Command} for which to look recursively in for
     * @param data    a data source that is, or extends {@link ExecutionData}
     * @return the final {@link RimorMethod}
     * @throws IllegalArgumentException if no command method, subcommand method or subcommand group is found
     * @see CoreInterpreter#findMethod(String[], ExecutionData)
     */
    public Optional<? extends RimorMethod> findMethod(Command command, ExecutionData data) {
        List<String> path = data.getParameters();

        if (command instanceof JDACommand jdaCommand) {
            this.commandInstance = jdaCommand;

            if (path.isEmpty())
                return Optional.ofNullable(command.getCommandMethod());

            if (jdaCommand.getSubcommandMethod(path.get(0)) != null) {
                data.setParameters(path.subList(1, path.size()));
                return Optional.ofNullable(jdaCommand.getSubcommandMethod(path.get(0)));

            } else if (jdaCommand.getOptionSubcommand(path.get(0)) != null) {
                this.commandInstance = jdaCommand.getOptionSubcommand(path.get(0));

                data.setParameters(path.subList(1, path.size()));
                return Optional.ofNullable(this.commandInstance.getJdaCommandMethod());

            } else if (jdaCommand.getSubcommandGroup(path.get(0)) != null) {
                data.setParameters(path.subList(1, path.size()));
                findMethod(jdaCommand.getSubcommandGroup(path.get(0)), data);

            } else if (jdaCommand.getCommandMethod() != null) {
                data.setParameters(path.subList(1, path.size()));
                return Optional.ofNullable(jdaCommand.getCommandMethod());
            }
        }

        return Optional.empty();
    }
}
