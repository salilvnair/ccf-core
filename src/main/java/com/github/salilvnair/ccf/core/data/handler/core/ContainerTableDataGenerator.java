package com.github.salilvnair.ccf.core.data.handler.core;

import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.exception.DataException;
import com.github.salilvnair.ccf.core.entity.ContainerInfo;
import com.github.salilvnair.ccf.core.model.FilterValue;
import com.github.salilvnair.ccf.core.model.SectionField;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ContainerTableDataGenerator extends  DataGenerator<List<List<SectionField>>> {
    List<SectionField> generateHeaders(DataContext dataContext, ContainerInfo containerInfo) throws DataException;
    default Map<Long, Set<String>> generateFilterValues(List<List<SectionField>> tableData) { return null;}
    default Map<Long, FilterValue> generateDtFilterValues(List<List<SectionField>> tableData) { return null;}

    default int totalRows(DataContext dataContext) throws DataException {return 0;}
}
