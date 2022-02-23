package com.jasperls.rimor.jda.method;

import com.jasperls.rimor.jda.option.RimorOption;
import com.jasperls.rimor.method.RimorMethod;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

@Getter
// TODO check use of OptionMethod#position, library has gone a different path and this variable might not be required
public class OptionMethod extends RimorMethod implements Comparable<OptionMethod> {
    private final int position;
    private final RimorOption option;

    public OptionMethod(Method method, int position, RimorOption option) {
        super(method);
        this.position = position;
        this.option = option;
    }

    @Override
    public int compareTo(@NotNull OptionMethod o) {
        return Integer.compare(getPosition(), o.getPosition());
    }
}
