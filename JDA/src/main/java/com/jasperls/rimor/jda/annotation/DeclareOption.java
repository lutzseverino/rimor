package com.jasperls.rimor.jda.annotation;

import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DeclareOption {
    OptionType type();

    String name();

    String description();

    boolean required();
}
