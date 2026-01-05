package com.github.salilvnair.ccf.core.component.service.core;

import com.github.salilvnair.ccf.core.component.collector.core.ComponentDataCollector;
import com.github.salilvnair.ccf.core.component.collector.factory.ComponentDataCollectorFactory;
import com.github.salilvnair.ccf.core.component.collector.type.ComponentCollectorType;
import com.github.salilvnair.ccf.core.component.context.ComponentContext;
import com.github.salilvnair.ccf.core.component.generator.core.ComponentDataGenerator;
import com.github.salilvnair.ccf.core.component.generator.factory.ComponentDataGeneratorFactory;
import com.github.salilvnair.ccf.core.component.generator.type.ComponentGeneratorType;
import com.github.salilvnair.ccf.core.component.model.core.*;
import com.github.salilvnair.ccf.core.component.type.ComponentRequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Salil V Nair
 */
@Service
public class ComponentServiceImpl extends AbstractComponentService implements ComponentService {
    @Autowired
    private ComponentDataGeneratorFactory componentDataGeneratorFactory;

    @Autowired
    private ComponentDataCollectorFactory componentDataCollectorFactory;


    @Override
    public IComponentDataResponse execute(ComponentDataRequest request, ComponentContext context) {
        ComponentRequestType requestType = request.getRequestType();
        if(requestType == null) {
            requestType = ComponentRequestType.GENERATE;
        }
        return ComponentRequestType.GENERATE.equals(requestType) ? generate(request, context) : collect(request, context);
    }

    @Override
    public IComponentDataResponse generate(ComponentDataRequest request, ComponentContext context) {
        Map<String, ComponentData> componentDataMap = new HashMap<>(1);
        if(request.getInputParams() == null) {
            request.setInputParams(new HashMap<>());
        }
        context.setRequestInputParams(request.getInputParams());
        request.getComponents().forEach(componentInfo -> {
            ComponentGeneratorType componentGeneratorType = ComponentGeneratorType.type(componentInfo.getType());
            if(componentGeneratorType !=null) {
                Map<String, Object> componentInputParams = componentInfo.getInputParams() != null ? componentInfo.getInputParams() :  new HashMap<>();
                ComponentDataGenerator generator = componentDataGeneratorFactory.generate(componentGeneratorType, componentInputParams);
                if(generator!=null) {
                    context.setComponentGeneratorType(componentGeneratorType);
                    initComponentContext(request, context, componentInfo, componentInputParams);
                    generator.generate(context, componentGeneratorType, request, componentInputParams, componentDataMap);
                }
            }
        });
        return prepareResponseFromDataMap(context, componentDataMap);
    }

    @Override
    public IComponentDataResponse collect(ComponentDataRequest request, ComponentContext context) {
        Map<String, ComponentData> componentDataMap = new HashMap<>(1);
        if(request.getInputParams() == null) {
            request.setInputParams(new HashMap<>());
        }
        context.setRequestInputParams(request.getInputParams());
        request.getComponents().forEach(componentInfo -> {
            ComponentCollectorType componentCollectorType = ComponentCollectorType.type(componentInfo.getType());
            if(componentCollectorType !=null) {
                Map<String, Object> componentInputParams = componentInfo.getInputParams() != null ? componentInfo.getInputParams() :  new HashMap<>();
                ComponentDataCollector collector = componentDataCollectorFactory.generate(componentCollectorType, componentInputParams);
                if(collector!=null) {
                    context.setComponentCollectorType(componentCollectorType);
                    initComponentContext(request, context, componentInfo, componentInputParams);
                    collector.collect(context, componentCollectorType, request, componentInputParams, componentDataMap);
                }
            }
        });
        return prepareResponseFromDataMap(context, componentDataMap);
    }

    private void initComponentContext(ComponentDataRequest request, ComponentContext context, ComponentInfo componentInfo, Map<String, Object> componentInputParams) {
        context.setComponentInfo(componentInfo);
        Map<String, Object> mergedInputParams = mergeAllInputParams(request, componentInputParams);
        componentInputParams.putAll(mergedInputParams);
        context.setComponentInputParams(componentInputParams);
        context.dataContext().setInputParams(mergedInputParams);
    }

    private Map<String, Object> mergeAllInputParams(ComponentDataRequest request, Map<String, Object> componentInputParams) {
        Map<String, Object> requestInputParams = request.getInputParams();
        if(CollectionUtils.isEmpty(requestInputParams) && CollectionUtils.isEmpty(componentInputParams)) {
            return new HashMap<>();
        }
        else if(CollectionUtils.isEmpty(requestInputParams)) {
            return componentInputParams;
        }
        else if(CollectionUtils.isEmpty(componentInputParams)) {
            return requestInputParams;
        }
        else {
            Map<String, Object> mergedMap = new HashMap<>();
            mergedMap.putAll(requestInputParams);
            mergedMap.putAll(componentInputParams);
            return mergedMap;
        }
    }

    private ComponentDataResponse prepareResponseFromDataMap(ComponentContext context, Map<String, ComponentData> componentDataMap) {
        ComponentDataResponse response = new ComponentDataResponse();
        response.setComponentData(componentDataMap);
        return response;
    }
}
