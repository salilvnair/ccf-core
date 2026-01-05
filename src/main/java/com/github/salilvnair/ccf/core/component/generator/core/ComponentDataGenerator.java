package com.github.salilvnair.ccf.core.component.generator.core;

import com.github.salilvnair.ccf.core.component.context.ComponentContext;
import com.github.salilvnair.ccf.core.component.generator.type.ComponentGeneratorType;
import com.github.salilvnair.ccf.core.component.model.core.ComponentData;
import com.github.salilvnair.ccf.core.component.model.core.IComponentDataRequest;

import java.util.Map;

/**
 * @author Salil V Nair
 */
public interface ComponentDataGenerator {
    void generate(ComponentContext componentContext, ComponentGeneratorType componentGeneratorType, IComponentDataRequest requestWrapper, Map<String, Object> componentInputParams, Map<String, ComponentData> componentDataMap);
}
