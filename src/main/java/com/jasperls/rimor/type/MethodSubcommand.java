package com.jasperls.rimor.type;

import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class MethodSubcommand {
    private final Method method;
    private Command parent;
    private ChildCommand child;

    public MethodSubcommand(Method method, Command parent) {
        this.method = method;
        this.parent = parent;
    }

    public MethodSubcommand(Method method, ChildCommand child) {
        this.method = method;
        this.child = child;
    }
}
