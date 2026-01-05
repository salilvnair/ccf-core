package com.github.salilvnair.ccf.core.data.handler.core;

import com.github.salilvnair.ccf.core.data.context.DataContext;

import java.util.Collections;
import java.util.List;

public interface TransformedContainerDataGenerator extends DataGenerator<List<?>> {
    default List<?> generate(DataContext dataContext) {
        return Collections.emptyList();
    }

   <T> List<T> generate(DataContext dataContext, Class<T> clazz);
}
