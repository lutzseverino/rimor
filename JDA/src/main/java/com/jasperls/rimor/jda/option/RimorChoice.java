package com.jasperls.rimor.jda.option;

import lombok.Getter;

@Getter
public class RimorChoice {
    private final String name;
    private final String value;

    public RimorChoice(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
