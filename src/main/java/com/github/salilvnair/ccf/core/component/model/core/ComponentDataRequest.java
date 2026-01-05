package com.github.salilvnair.ccf.core.component.model.core;

import com.github.salilvnair.ccf.core.component.type.ComponentRequestType;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Salil V Nair
 */
@Data
public class ComponentDataRequest implements IComponentDataRequest {
    private List<ComponentInfo> components;
    private Map<String, Object> inputParams;
    private ComponentRequestType requestType;
}
