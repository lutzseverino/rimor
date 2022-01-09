package com.jasperls.rimor.jda.option;

import lombok.Getter;

import java.util.List;

@Getter
public class RimorChoice {
    private final String name;
    private final String description;
    private List<String> stringValues;
    private List<Long> longValues;
    private List<Double> doublesValues;

    public RimorChoice(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void addValue(String value) {
        this.stringValues.add(value);
    }

    public void addValue(long value) {
        this.longValues.add(value);
    }

    public void addValue(double value) {
        this.doublesValues.add(value);
    }
}
