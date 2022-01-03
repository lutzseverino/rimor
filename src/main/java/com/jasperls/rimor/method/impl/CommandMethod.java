package com.jasperls.rimor.method.impl;

import com.jasperls.rimor.method.RimorMethod;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Set;

@Getter
public class CommandMethod extends RimorMethod {
    public final Set<String> names;

    public CommandMethod(Set<String> names, Method method) {
        this.method = method;
        this.names = names;
    }
}
