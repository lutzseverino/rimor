package com.jasperls.rimor.jda.method;

import com.jasperls.rimor.jda.option.RimorOption;
import com.jasperls.rimor.method.RimorMethod;
import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class OptionMethod extends RimorMethod {

    private final RimorOption option;

    public OptionMethod(Method method, RimorOption option) {
        super(method);
        this.option = option;
    }
}
