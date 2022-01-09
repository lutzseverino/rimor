package com.jasperls.rimor.jda.method;

import com.jasperls.rimor.method.CommandMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class JDACommandMethod extends CommandMethod {
    public final Map<String, OptionMethod> optionMethodMap = new HashMap<>();

    public JDACommandMethod(Method method) {
        super(method);
    }

    public void addOptionMethod(String name, OptionMethod option) {
        this.optionMethodMap.put(name, option);
    }

    public OptionMethod getOptionMethod(String name) {
        return this.optionMethodMap.get(name);
    }
}
