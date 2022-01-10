package com.jasperls.rimor.jda.method;

import com.jasperls.rimor.method.CommandMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDACommandMethod extends CommandMethod {
    private final Map<String, OptionMethod> optionMethodMap = new HashMap<>();

    public JDACommandMethod(Method method) {
        super(method);
    }

    public void addOptionMethod(String name, OptionMethod option) {
        this.optionMethodMap.put(name, option);
    }

    public OptionMethod getOptionMethod(String name) {
        return this.optionMethodMap.get(name);
    }

    public List<OptionMethod> getOptionMethods() {
        return new ArrayList<>(this.optionMethodMap.values());
    }
}
