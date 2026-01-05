package com.github.salilvnair.ccf.core.data.handler.core;

import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.exception.DataException;

public interface DataGenerator<E> {
    E generate(DataContext dataContext) throws DataException;
}
