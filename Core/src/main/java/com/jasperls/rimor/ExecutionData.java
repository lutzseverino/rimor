package com.jasperls.rimor;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ExecutionData {
    public List<String> parameters = new ArrayList<>();

    public ExecutionData() {
    }
    
    public void setParameters(List<String> params) {
        this.parameters = params;
    }
}
