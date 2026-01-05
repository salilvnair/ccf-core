package com.github.salilvnair.ccf.core.component.service.core;

import com.github.salilvnair.ccf.core.component.helper.ComponentHelper;

import java.util.Map;

/**
 * @author Salil V Nair
 */
public abstract class AbstractComponentService implements ComponentService {

    protected <T> T extract(String key, Map<String, Object> inputParams) {
        return ComponentHelper.extract(key, inputParams);
    }

}
