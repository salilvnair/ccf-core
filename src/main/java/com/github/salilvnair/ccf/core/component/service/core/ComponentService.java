package com.github.salilvnair.ccf.core.component.service.core;


import com.github.salilvnair.ccf.core.component.context.ComponentContext;
import com.github.salilvnair.ccf.core.component.model.core.ComponentDataRequest;
import com.github.salilvnair.ccf.core.component.model.core.IComponentDataResponse;

/**
 * @author Salil V Nair
 */
public interface ComponentService {

    default IComponentDataResponse execute(ComponentDataRequest request, ComponentContext context){return null;};

    default IComponentDataResponse generate(ComponentDataRequest request, ComponentContext context){return null;};

    default IComponentDataResponse collect(ComponentDataRequest request, ComponentContext context){return null;};
}
