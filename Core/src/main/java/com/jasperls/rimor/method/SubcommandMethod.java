package com.jasperls.rimor.method;

import com.jasperls.rimor.type.Command;
import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class SubcommandMethod extends RimorMethod {
    private final Command parent;

    public SubcommandMethod(Method method, Command parent) {
        super(method);
        this.parent = parent;
    }
}
