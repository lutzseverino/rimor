package com.jasperls.rimor.interpreter;

import com.jasperls.rimor.ExecutionData;
import com.jasperls.rimor.Rimor;
import com.jasperls.rimor.method.RimorMethod;

import java.util.Optional;

public interface RimorInterpreter {
    void execute(Rimor rimorInstance, String[] path, ExecutionData data);

    Optional<? extends RimorMethod> findMethod(Rimor rimorInstance, String[] path, ExecutionData data);
}
