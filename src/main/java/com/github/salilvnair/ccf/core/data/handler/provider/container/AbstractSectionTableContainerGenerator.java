package com.github.salilvnair.ccf.core.data.handler.provider.container;

import com.github.salilvnair.ccf.core.constant.StringConstant;
import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.exception.DataException;
import com.github.salilvnair.ccf.core.data.handler.core.ContainerTableDataGenerator;
import com.github.salilvnair.ccf.core.data.query.decorator.main.WhereQueryString;
import com.github.salilvnair.ccf.core.data.type.FieldIdType;
import com.github.salilvnair.ccf.core.entity.ContainerFieldInfo;
import com.github.salilvnair.ccf.core.entity.ContainerInfo;
import com.github.salilvnair.ccf.core.entity.ContainerQueryInfo;
import com.github.salilvnair.ccf.core.model.FilterOption;
import com.github.salilvnair.ccf.core.model.FilterValue;
import com.github.salilvnair.ccf.core.model.SectionField;
import com.github.salilvnair.ccf.core.repository.ContainerFieldInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public abstract class AbstractSectionTableContainerGenerator extends AbstractContainerGenerator implements ContainerTableDataGenerator {

    @Autowired
    private ContainerFieldInfoRepo containerFieldInfoRepo;


    public List<SectionField> generateHeaders(DataContext dataContext, ContainerInfo containerInfo) throws DataException {
        List<ContainerFieldInfo> containerFieldInfoList = dataContext.getCurrentContainerFieldInfoList();
        if(!CollectionUtils.isEmpty(containerFieldInfoList)) {
            return containerFieldInfoList.stream().filter(containerFieldInfo -> containerFieldInfo.eligible(dataContext)).map(containerFieldInfo -> prepareSectionFieldsUsingContainerFieldInfo(dataContext, containerInfo, containerFieldInfo)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    protected List<List<SectionField>> results(DataContext dataContext) {
        List<Map<String, Object>> dbData = fetchNonPaginatedResults(dataContext);
        return processTableData(dbData, dataContext);
    }

    protected List<List<SectionField>> processTableData(List<Map<String,Object>> dbData, DataContext dataContext) {
        dataContext.setDbData(dbData);
        dataContext.setContainerQueryData(dbData);
        ContainerInfo containerInfo = dataContext.getContainerInfo();
        if(containerInfo.getBeanName() != null ) {
            executeContainerTask(containerInfo, dbData, dataContext);
        }
        return prepareTableData(dbData, dataContext);
    }

    protected List<List<SectionField>> prepareTableData(List<Map<String,Object>> dbData, DataContext dataContext) {
        List<List<SectionField>> tableData = new ArrayList<>();
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
                    List<SectionField> sectionFields = new ArrayList<>();
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
                        SectionField sectionField = prepareSectionFieldsUsingContainerFieldInfo(dataContext, containerInfo, containerFieldInfo);
                        if(FieldIdType.CONTAINER.id() == containerFieldInfo.getFieldTypeId()) {
                            addContainerFieldRawDataIntoSection(sectionField, dataContext, containerFieldInfo);
                        }
                        else {
                            sectionField.setFieldValue(rowData.get(column.toUpperCase()));
                        }
                        sectionFields.add(sectionField);
                    }
                    tableData.add(sectionFields);
                }
            }
        }
        return tableData;
    }

    @Override
    public Map<Long, Set<String>> generateFilterValues(List<List<SectionField>> tableData) {
        Map<Long, Set<String>> filterValues = new HashMap<>();
        if(!CollectionUtils.isEmpty(tableData)) {
            filterValues = tableData.stream()
                                .flatMap(Collection::stream)
                                .filter(SectionField::isFilterable)
                                .collect(
                                        Collectors.toMap(
                                                SectionField::getFieldId,
                                                row -> {
                                                    Set<String> values = new HashSet<>();
                                                    values.add(row.getFieldValue() + "");
                                                    return values;
                                                },
                                                (o, n) -> {
                                                    o.addAll(n);
                                                    return o;
                                                }
                                        )
                                );
        }
        return filterValues;
    }

    public Map<Long, FilterValue> generateDtFilterValues(List<List<SectionField>> tableData) {
        Map<Long, FilterValue> filterValues = new HashMap<>();
        Map<Long, Set<String>> dataMap = generateFieldIdKeyedFieldValuesMap(tableData);
        if(!CollectionUtils.isEmpty(dataMap)) {
            dataMap.forEach((k,v) -> {
                FilterValue filterValue = new FilterValue();
                List<FilterOption> filterOptions = new ArrayList<>();
                for (String s : v) {
                    FilterOption filterOption = new FilterOption();
                    filterOption.setFieldValue(s);
                    filterOptions.add(filterOption);
                }
                filterValue.setFilterOptions(filterOptions);
                filterValue.setFieldId(k);
                filterValues.put(k, filterValue);
            });
        }

        return filterValues;
    }

    private Map<Long, Set<String>> generateFieldIdKeyedFieldValuesMap(List<List<SectionField>> tableData) {
        Map<Long, Set<String>> filterValues = new HashMap<>();
        if(!CollectionUtils.isEmpty(tableData)) {
            filterValues = tableData.stream()
                    .flatMap(Collection::stream)
                    .filter(SectionField::isFilterable)
                    .collect(
                            Collectors.toMap(
                                    SectionField::getFieldId,
                                    row -> {
                                        Set<String> values = new HashSet<>();
                                        values.add(row.getFieldValue() + "");
                                        return values;
                                    },
                                    (o, n) -> {
                                        o.addAll(n);
                                        return o;
                                    }
                            )
                    );
        }
        return filterValues;
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
            String classType = resultList.get(0).getClass().getName();
            if(classType.equalsIgnoreCase("java.math.BigDecimal")) {
                return ((BigDecimal)resultList.get(0)).intValue();
            } else if(classType.equalsIgnoreCase("java.lang.Long")) {
                return ((Long)resultList.get(0)).intValue();
            }
        }
        return 0;
    }
}
