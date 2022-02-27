package com.jasperls.rimor.interpreter;

import com.jasperls.rimor.data.Data;
import com.jasperls.rimor.method.RimorMethod;

import java.util.Optional;

public interface RimorInterpreter {

    void execute(String[] path, Data data);

    /**
     * @param path a path with each individual command steps
     * @param data a data source that is, or extends {@link Data}
     * @return the final {@link RimorMethod}
     * @throws IllegalArgumentException if no command is found
     */
    Optional<? extends RimorMethod> findMethod(String[] path, Data data);
}
