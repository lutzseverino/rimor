package com.jasperls.rimor;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExecutionData {
    public List<String> parameters = new ArrayList<>();
}
