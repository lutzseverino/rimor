package com.jasperls.rimor.method;

import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class SubcommandMethod extends RimorMethod {

    public SubcommandMethod(Method method) {
        super(method);
    }
}
