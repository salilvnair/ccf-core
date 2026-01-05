package com.github.salilvnair.ccf.core.component.collector.core;

import com.github.salilvnair.ccf.core.component.collector.type.ComponentCollectorType;
import com.github.salilvnair.ccf.core.component.context.ComponentContext;
import com.github.salilvnair.ccf.core.component.model.core.ComponentData;
import com.github.salilvnair.ccf.core.component.model.core.IComponentDataRequest;

import java.util.Map;

/**
 * @author Salil V Nair
 */
public interface ComponentDataCollector {
    void collect(ComponentContext componentContext, ComponentCollectorType componentCollectorType, IComponentDataRequest requestWrapper, Map<String, Object> componentInputParams, Map<String, ComponentData> componentDataMap);
}
