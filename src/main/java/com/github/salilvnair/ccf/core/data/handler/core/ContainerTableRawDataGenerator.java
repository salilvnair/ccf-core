package com.github.salilvnair.ccf.core.data.handler.core;

import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.exception.DataException;
import com.github.salilvnair.ccf.core.entity.ContainerInfo;
import com.github.salilvnair.ccf.core.model.FilterValue;
import com.github.salilvnair.ccf.core.model.SectionField;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ContainerTableRawDataGenerator extends  DataGenerator<List<Map<String, Object>>> {
    List<SectionField> generateHeaders(DataContext dataContext, ContainerInfo containerInfo) throws DataException;
    default int totalRows(DataContext dataContext) throws DataException {return 0;}
}
