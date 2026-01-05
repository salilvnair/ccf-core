package com.github.salilvnair.ccf.core.component.generator.factory;

import com.github.salilvnair.ccf.core.component.generator.core.ComponentDataGenerator;
import com.github.salilvnair.ccf.core.component.generator.type.ComponentGeneratorType;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Salil V Nair
 */
@Component
public class ComponentDataGeneratorFactory {

    private final ApplicationContext applicationContext;
    public ComponentDataGeneratorFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    public ComponentDataGenerator generate(ComponentGeneratorType component, Map<String, Object> inputParams ) {
        return  applicationContext.containsBean(component.value()) ? (ComponentDataGenerator) applicationContext.getBean(component.value()) : null;
    }
}
