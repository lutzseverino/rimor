package com.jasperls.rimor;

import com.jasperls.rimor.type.Command;
import com.jasperls.rimor.type.MethodSubcommand;
import lombok.Getter;

import java.lang.reflect.Method;

public class CommandMethodFinder {

    @Getter
    Object finalObject;

    /**
     * @param path         the command hierarchy path
     * @param firstCommand the first {@link Command} object
     * @return the evaluated final {@link Method}
     */
    public Method evaluate(String[] path, Command firstCommand) {
        if (firstCommand.getLonelyMethod() != null) {
            finalObject = firstCommand;
            return firstCommand.getLonelyMethod();
        }

        MethodSubcommand methodSubcommand;
        boolean isNested = false;
        String nestedStep = null;

        for (String step : path) {
            methodSubcommand = firstCommand.getSubcommandMap().get(step);

            if (methodSubcommand != null) {
                finalObject = methodSubcommand.getParent();
                return methodSubcommand.getMethod();
            }

            if (firstCommand.getChildCommandMap().get(step) != null) {
                isNested = true;
                nestedStep = step;
                continue;
            }

            methodSubcommand = firstCommand.getChildCommandMap().get(nestedStep).getSubcommandMap().get(step);

            if (isNested && methodSubcommand != null) {
                finalObject = methodSubcommand.getChild();
                return methodSubcommand.getMethod();
            }
        }
        return null;
    }
}
