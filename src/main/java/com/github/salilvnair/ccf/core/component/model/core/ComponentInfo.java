package com.github.salilvnair.ccf.core.component.model.core;

import lombok.Data;

import java.util.Map;

@Data
public class ComponentInfo {
    private String type;
    private Map<String, Object> inputParams;
}
