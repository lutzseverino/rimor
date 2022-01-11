package com.jasperls.rimor.jda.option;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RimorChoice {
    private final String name;
    private final String description;
    private final List<String> stringValues = new ArrayList<>();
    private final List<Long> longValues  = new ArrayList<>();
    private final List<Double> doublesValues = new ArrayList<>();

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
