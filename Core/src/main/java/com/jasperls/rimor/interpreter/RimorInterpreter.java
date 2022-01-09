package com.jasperls.rimor.interpreter;

import com.jasperls.rimor.ExecutionData;
import com.jasperls.rimor.Rimor;
import com.jasperls.rimor.method.RimorMethod;

import java.util.Optional;

public interface RimorInterpreter {
    void execute(Rimor rimorInstance, String[] path, ExecutionData data);

    /**
     * @param path a path with each individual command steps
     * @param data a data source that is, or extends {@link ExecutionData}
     * @return the final {@link RimorMethod}
     * @throws IllegalArgumentException if no command is found
     */
    Optional<? extends RimorMethod> findMethod(Rimor rimorInstance, String[] path, ExecutionData data);
}
