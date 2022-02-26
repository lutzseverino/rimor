package com.jasperls.rimor.method;

import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Getter
public abstract class RimorMethod {

    protected Method method;

    public RimorMethod(Method method) {
        this.method = method;
    }

    public void invoke(Object instance, Object... args) {
        try {
            this.method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
