package com.jasperls.rimor.jda.method;

import com.jasperls.rimor.method.RimorMethod;
import com.jasperls.rimor.type.Command;
import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class OptionMethod extends RimorMethod {
    private final Command parent;

    public OptionMethod(Method method, Command parent) {
        super(method);
        this.parent = parent;
    }
}
