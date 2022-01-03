package com.jasperls.rimor.method.impl;

import com.jasperls.rimor.method.RimorMethod;
import com.jasperls.rimor.type.Command;
import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class SubcommandMethod extends RimorMethod {
    private final Command parent;

    public SubcommandMethod(Method method, Command parent) {
        this.method = method;
        this.parent = parent;
    }
}
