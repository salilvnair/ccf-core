package com.github.salilvnair.ccf.core.component.collector.core;


import com.github.salilvnair.ccf.core.component.collector.type.ComponentCollectorType;
import com.github.salilvnair.ccf.core.component.helper.ComponentHelper;
import com.github.salilvnair.ccf.core.component.model.core.ComponentData;
import com.github.salilvnair.ccf.core.component.model.core.ComponentDataError;
import com.github.salilvnair.ccf.core.component.model.core.IComponentDataRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Salil V Nair
 */
public abstract class AbstractComponentDataCollector implements ComponentDataCollector {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    protected  <T> T extract(IComponentDataRequest request) {
        return ComponentHelper.extract(request);
    }

    protected <T> T extract(String key, Map<String, Object> inputParams) {
        return ComponentHelper.extract(key, inputParams);
    }

    protected void addParam(String key, Object value, Map<String, Object> inputParams) {
        ComponentHelper.addParam(key, value, inputParams);
    }

    protected <T> T extractObject(String key, Map<String, Object> inputParams, Class<T> clazz ) {
        return ComponentHelper.extractObject(key, inputParams, clazz);
    }

    protected <T> List<T> extractList(String key, Map<String, Object> inputParams, Class<T> clazz ) {
        return ComponentHelper.extractList(key, inputParams, clazz);
    }

    protected boolean hasParam(String key, Map<String, Object> inputParams) {
        return ComponentHelper.hasParam(key, inputParams);
    }


    protected void addMetaData(Object metaData, ComponentCollectorType componentCollectorType, Map<String, ComponentData> componentDataMap) {
        ComponentData componentData = new ComponentData();
        componentData.setMetadata(metaData);
        componentDataMap.put(componentCollectorType.value(), componentData);
    }

    protected void addComponentData(Object content, ComponentCollectorType componentCollectorType, Map<String, ComponentData> componentDataMap) {
        ComponentData componentData = new ComponentData();
        List<Object> results = new ArrayList<>();
        if(content!=null) {
            results.add(content);
            componentData.setContent(Collections.unmodifiableList(results));
        }
        componentDataMap.put(componentCollectorType.value(), componentData);
    }

    protected void addComponentErrors(List<?> errors, ComponentCollectorType componentCollectorType, Map<String, ComponentData> componentDataMap) {
        addComponentErrors(null, errors, componentCollectorType, componentDataMap);
    }

    protected void addComponentError(ComponentDataError error, ComponentCollectorType componentCollectorType, Map<String, ComponentData> componentDataMap) {
        addComponentError(null, error, componentCollectorType, componentDataMap);
    }

    protected void addComponentErrors(Object metaData, List<?> errors, ComponentCollectorType componentCollectorType, Map<String, ComponentData> componentDataMap) {
        ComponentData componentData = new ComponentData();
        componentData.setMetadata(metaData);
        if(!CollectionUtils.isEmpty(errors)) {
            componentData.setErrors(Collections.unmodifiableList(errors));
        }
        componentDataMap.put(componentCollectorType.value(), componentData);
    }


    protected void addComponentError(Object metaData, ComponentDataError error, ComponentCollectorType componentCollectorType, Map<String, ComponentData> componentDataMap) {
        ComponentData componentData = new ComponentData();
        componentData.setMetadata(metaData);
        List<Object> errors = new ArrayList<>();
        if(error!=null) {
            errors.add(error);
            componentData.setErrors(Collections.unmodifiableList(errors));
        }
        componentDataMap.put(componentCollectorType.value(), componentData);
    }
}
