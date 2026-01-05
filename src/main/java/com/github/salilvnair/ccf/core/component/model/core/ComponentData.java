package com.github.salilvnair.ccf.core.component.model.core;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ComponentData {

    private Object metadata;
    private List<Object> content;
    private List<Object> errors;
}
