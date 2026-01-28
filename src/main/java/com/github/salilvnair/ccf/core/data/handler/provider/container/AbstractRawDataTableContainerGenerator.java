package com.github.salilvnair.ccf.core.data.handler.provider.container;

import com.github.salilvnair.ccf.core.constant.StringConstant;
import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.exception.DataException;
import com.github.salilvnair.ccf.core.data.handler.core.ContainerTableRawDataGenerator;
import com.github.salilvnair.ccf.core.data.query.decorator.main.WhereQueryString;
import com.github.salilvnair.ccf.core.data.type.FieldIdType;
import com.github.salilvnair.ccf.core.entity.ContainerFieldInfo;
import com.github.salilvnair.ccf.core.entity.ContainerInfo;
import com.github.salilvnair.ccf.core.entity.ContainerQueryInfo;
import com.github.salilvnair.ccf.core.model.SectionField;
import com.github.salilvnair.ccf.core.repository.ContainerFieldInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public abstract class AbstractRawDataTableContainerGenerator extends AbstractContainerGenerator implements ContainerTableRawDataGenerator {

    @Autowired
    private ContainerFieldInfoRepo containerFieldInfoRepo;


    public List<SectionField> generateHeaders(DataContext dataContext, ContainerInfo containerInfo) throws DataException {
        List<ContainerFieldInfo> containerFieldInfoList = dataContext.getCurrentContainerFieldInfoList();
        if(!CollectionUtils.isEmpty(containerFieldInfoList)) {
            return containerFieldInfoList.stream().filter(containerFieldInfo -> containerFieldInfo.eligible(dataContext)).map(containerFieldInfo -> prepareSectionFieldsUsingContainerFieldInfo(dataContext, containerInfo, containerFieldInfo)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    protected List<Map<String, Object>> results(DataContext dataContext) {
        List<Map<String, Object>> dbData = fetchNonPaginatedResults(dataContext);
        return results(dbData, dataContext);
    }

    protected List<Map<String, Object>> results(List<Map<String,Object>> dbData, DataContext dataContext) {
        List<Map<String, Object>> tableData = new ArrayList<>();
        ContainerInfo containerInfo = dataContext.getContainerInfo();
        ContainerQueryInfo containerQueryInfo = dataContext.getContainerQueryInfo();
        if(containerQueryInfo == null) {
            return Collections.emptyList();
        }
        List<ContainerFieldInfo> containerFieldInfoList = containerFieldInfoRepo.findByContainerIdAndActiveOrderByDisplayOrder(containerQueryInfo.getContainerId(), StringConstant.Y);
        if(!CollectionUtils.isEmpty(containerFieldInfoList)) {
            dataContext.setCurrentContainerFieldInfoList(containerFieldInfoList);
            if(!CollectionUtils.isEmpty(dbData)) {
                for(Map<String, Object> rowData: dbData) {
                    Map<String, Object> rawRowData  = new HashMap<>();
                    if(containerInfo.getTableRowBeanName() != null ) {
                        executeTableRowTask(containerInfo, rowData, dbData, dataContext);
                    }
                    for(ContainerFieldInfo containerFieldInfo: containerFieldInfoList) {
                        String column = containerFieldInfo.getMappedColumnName();
                        if(containerFieldInfo.getBeanName() != null ) {
                            executeFieldTask(containerFieldInfo, rowData, dbData, dataContext);
                        }
                        if(!containerFieldInfo.eligible(dataContext)) {
                            continue;
                        }
                        if(FieldIdType.CONTAINER.id() == containerFieldInfo.getFieldTypeId()) {
                            addContainerFieldRawDataIntoSection(rowData, dataContext, containerFieldInfo);
                        }
                        else {
                            rawRowData.put(column.toUpperCase(), rowData.get(column.toUpperCase()));
                        }
                    }
                    tableData.add(rawRowData);
                }
            }
        }
        return tableData;
    }

    @Override
    public int totalRows(DataContext dataContext) {
        ContainerQueryInfo containerQueryInfo = dataContext.getContainerQueryInfo();
        if(containerQueryInfo == null) {
            return 0;
        }
        String countQueryString = containerQueryInfo.getCountQueryString();
        if(countQueryString == null) {
            return 0;
        }
        countQueryString = resolvePlaceHolders(countQueryString, dataContext, new WhereQueryString());
        List<?> resultList = fetchResultList(dataContext, countQueryString);
        if(!CollectionUtils.isEmpty(resultList)) {
            String classType = resultList.getFirst().getClass().getName();
            if(classType.equalsIgnoreCase("java.math.BigDecimal")) {
                return ((BigDecimal)resultList.getFirst()).intValue();
            } else if(classType.equalsIgnoreCase("java.lang.Long")) {
                return ((Long)resultList.getFirst()).intValue();
            }
        }
        return 0;
    }
}
