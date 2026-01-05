package com.github.salilvnair.ccf.core.component.collector.factory;

import com.github.salilvnair.ccf.core.component.collector.core.ComponentDataCollector;
import com.github.salilvnair.ccf.core.component.collector.type.ComponentCollectorType;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Salil V Nair
 */
@Component
public class ComponentDataCollectorFactory {

    private final ApplicationContext applicationContext;
    public ComponentDataCollectorFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    public ComponentDataCollector generate(ComponentCollectorType component, Map<String, Object> inputParams ) {
        return  applicationContext.containsBean(component.value()) ? (ComponentDataCollector) applicationContext.getBean(component.value()) : null;
    }
}
