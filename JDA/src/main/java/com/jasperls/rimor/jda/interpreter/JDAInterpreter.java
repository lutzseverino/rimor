package com.jasperls.rimor.jda.interpreter;

import com.jasperls.rimor.Rimor;
import com.jasperls.rimor.data.Data;
import com.jasperls.rimor.interpreter.CoreInterpreter;
import com.jasperls.rimor.interpreter.RimorInterpreter;
import com.jasperls.rimor.jda.data.JDACommandData;
import com.jasperls.rimor.jda.data.OptionExecutionData;
import com.jasperls.rimor.jda.method.JDACommandMethod;
import com.jasperls.rimor.jda.method.OptionMethod;
import com.jasperls.rimor.jda.type.JDACommand;
import com.jasperls.rimor.method.RimorMethod;
import com.jasperls.rimor.type.Command;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class JDAInterpreter implements RimorInterpreter {

    private JDACommand instance;

    @Override public void execute(String[] path, Data data) {
        this.findMethod(path, data).ifPresentOrElse(method -> {

            if (method instanceof JDACommandMethod jdaMethod && data instanceof JDACommandData jdaData) {
                jdaData.getEvent().getOptions().forEach(option -> {
                    OptionMethod optionMethod = jdaMethod.getOptionMethod(option.getName());
                    optionMethod.invoke(this.instance, new OptionExecutionData(option));
                });
            }

            method.invoke(this.instance, data);

        }, () -> {
            throw new IllegalArgumentException("Couldn't find method to execute with path " + Arrays.toString(path));
        });
    }

    @Override public Optional<? extends RimorMethod> findMethod(String[] path, Data data) {
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
     * @param data    a data source that is, or extends {@link Data}
     * @return the final {@link RimorMethod}
     * @throws IllegalArgumentException if no command method, subcommand method or subcommand group is found
     * @see CoreInterpreter#findMethod(String[], Data)
     */
    public Optional<? extends RimorMethod> findMethod(Command command, Data data) {
        List<String> path = data.getParameters();
        RimorMethod method = null;

        if (command instanceof JDACommand jdaCommand) {
            this.instance = jdaCommand;

            if (path.isEmpty()) {
                method = command.getCommandMethod();

            } else if (jdaCommand.getSubcommandMethod(path.get(0)) != null) {
                data.setParameters(path.subList(1, path.size()));
                method = jdaCommand.getSubcommandMethod(path.get(0));

            } else if (jdaCommand.getOptionSubcommand(path.get(0)) != null) {
                this.instance = jdaCommand.getOptionSubcommand(path.get(0));

                data.setParameters(path.subList(1, path.size()));
                method = jdaCommand.getJdaCommandMethod();

            } else if (jdaCommand.getSubcommandGroup(path.get(0)) != null) {
                data.setParameters(path.subList(1, path.size()));
                return findMethod(jdaCommand.getSubcommandGroup(path.get(0)), data);

            } else if (jdaCommand.getCommandMethod() != null) {
                data.setParameters(path.subList(1, path.size()));
                method = jdaCommand.getCommandMethod();

            }
        }

        return Optional.ofNullable(method);
    }
}
