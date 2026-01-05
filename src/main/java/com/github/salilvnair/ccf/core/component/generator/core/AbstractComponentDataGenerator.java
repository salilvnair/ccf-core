package com.github.salilvnair.ccf.core.component.generator.core;

import com.github.salilvnair.ccf.core.component.generator.type.ComponentGeneratorType;
import com.github.salilvnair.ccf.core.component.helper.ComponentHelper;
import com.github.salilvnair.ccf.core.component.model.core.ComponentData;
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
public abstract class AbstractComponentDataGenerator implements ComponentDataGenerator {

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

    protected void addMetaData(Object metaData, ComponentGeneratorType componentGeneratorType, Map<String, ComponentData> componentDataMap) {
        ComponentData componentData = new ComponentData();
        componentData.setMetadata(metaData);
        componentDataMap.put(componentGeneratorType.value(), componentData);
    }


    protected void addComponentData(Object content, ComponentGeneratorType componentGeneratorType, Map<String, ComponentData> componentDataMap) {
        ComponentData componentData = new ComponentData();
        List<Object> results = new ArrayList<>();
        if(content!=null) {
            results.add(content);
        }
        componentData.setContent(!CollectionUtils.isEmpty(results) ? Collections.unmodifiableList(results) : Collections.emptyList());
        componentDataMap.put(componentGeneratorType.value(), componentData);
    }

    protected void addListComponentData(List<?> content, ComponentGeneratorType componentGeneratorType, Map<String, ComponentData> componentDataMap) {
        ComponentData componentData = new ComponentData();
        componentData.setContent(!CollectionUtils.isEmpty(content) ? Collections.unmodifiableList(content) : Collections.emptyList());
        componentDataMap.put(componentGeneratorType.value(), componentData);
    }

    protected void addComponentErrors(List<?> errors, ComponentGeneratorType componentGeneratorType, Map<String, ComponentData> componentDataMap) {
        addComponentErrors(null, errors, componentGeneratorType, componentDataMap);
    }

    protected void addComponentError(Object error, ComponentGeneratorType componentGeneratorType, Map<String, ComponentData> componentDataMap) {
        addComponentError(null, error, componentGeneratorType, componentDataMap);
    }

    protected void addComponentErrors(Object metaData, List<?> errors, ComponentGeneratorType componentGeneratorType, Map<String, ComponentData> componentDataMap) {
        ComponentData componentData = new ComponentData();
        componentData.setMetadata(metaData);
        if(!CollectionUtils.isEmpty(errors)) {
            componentData.setErrors(Collections.unmodifiableList(errors));
        }
        componentDataMap.put(componentGeneratorType.value(), componentData);
    }


    protected void addComponentError(Object metaData, Object error, ComponentGeneratorType componentGeneratorType, Map<String, ComponentData> componentDataMap) {
        ComponentData componentData = new ComponentData();
        componentData.setMetadata(metaData);
        List<Object> errors = new ArrayList<>();
        if(error!=null) {
            errors.add(error);
            componentData.setErrors(Collections.unmodifiableList(errors));
        }
        componentDataMap.put(componentGeneratorType.value(), componentData);
    }
}
