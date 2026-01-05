package com.github.salilvnair.ccf.core.component.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

/**
 * @author Salil V Nair
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComponentDataResponse implements IComponentDataResponse {
    private Map<String, ComponentData> componentData;

    //service methods
    @Override
    public Map<String, ComponentData> componentData() {
        return componentData;
    }
}
