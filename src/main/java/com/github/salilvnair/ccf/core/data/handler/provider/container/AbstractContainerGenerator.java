package com.github.salilvnair.ccf.core.data.handler.provider.container;

import com.github.salilvnair.ccf.core.constant.StringConstant;
import com.github.salilvnair.ccf.core.dao.AbstractQueryDao;
import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.context.DataTaskContext;
import com.github.salilvnair.ccf.core.data.handler.core.ContainerDataGenerator;
import com.github.salilvnair.ccf.core.data.handler.core.ContainerTableDataGenerator;
import com.github.salilvnair.ccf.core.data.query.constant.QueryParamsKey;
import com.github.salilvnair.ccf.core.data.query.context.QueryContext;
import com.github.salilvnair.ccf.core.data.query.decorator.base.QueryString;
import com.github.salilvnair.ccf.core.data.query.decorator.main.OrderByQueryString;
import com.github.salilvnair.ccf.core.data.query.decorator.main.WhereQueryString;
import com.github.salilvnair.ccf.core.data.query.serializer.JsonStringQueryParams;
import com.github.salilvnair.ccf.core.data.type.ContainerType;
import com.github.salilvnair.ccf.core.data.type.FieldIdType;
import com.github.salilvnair.ccf.core.entity.ContainerFieldInfo;
import com.github.salilvnair.ccf.core.entity.ContainerInfo;
import com.github.salilvnair.ccf.core.entity.ContainerQueryInfo;
import com.github.salilvnair.ccf.core.entity.PageCommonQueryInfo;
import com.github.salilvnair.ccf.core.model.SectionField;
import com.github.salilvnair.ccf.core.repository.ContainerFieldInfoRepo;
import com.github.salilvnair.ccf.util.common.ObjectCopier;
import com.github.salilvnair.ccf.util.common.TranslateUtils;
import com.github.salilvnair.ccf.util.paginator.helper.PaginationHelper;
import com.github.salilvnair.ccf.util.paginator.model.PaginationInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public abstract class AbstractContainerGenerator extends AbstractQueryDao {

    @Autowired
    private ContainerFieldInfoRepo containerFieldInfoRepo;

    protected String resolvePlaceHolders(String paginatedFilterSortQueryString, DataContext dataContext, QueryString queryString) {
        QueryContext queryContext = QueryContext
                                    .builder()
                                    .queryString(paginatedFilterSortQueryString)
                                    .containerFieldInfoRepo(containerFieldInfoRepo)
                                    .build();
        dataContext.setQueryContext(queryContext);

        return queryString.build(dataContext);
    }

    protected List<SectionField> processData(List<Map<String,Object>> dbData, DataContext dataContext) {
        dataContext.setContainerQueryData(dbData);
        ContainerInfo containerInfo = dataContext.getContainerInfo();
        if(containerInfo.getBeanName() != null ) {
            executeContainerTask(containerInfo, dbData, dataContext);
        }
        return prepareData(containerInfo, dbData, dataContext);
    }

    protected Object executeContainerTask(ContainerInfo containerInfo, List<Map<String,Object>> dbData, DataContext dataContext) {
        DataTaskContext dataTaskContext = DataTaskContext
                                            .builder()
                                            .dataContext(dataContext)
                                            .dbData(dbData)
                                            .build();
        return dataContext.getDataTaskExecutor().execute(containerInfo.getBeanName(), containerInfo.getBeanMethodName(), dataTaskContext);
    }

    protected Object executeTableRowTask(ContainerInfo containerInfo, Map<String,Object> rowData, List<Map<String,Object>> dbData, DataContext dataContext) {
        DataTaskContext dataTaskContext = DataTaskContext
                                            .builder()
                                            .dataContext(dataContext)
                                            .dbData(dbData)
                                            .rowData(rowData)
                                            .build();
        return dataContext.getDataTaskExecutor().execute(containerInfo.getTableRowBeanName(), containerInfo.getTableRowBeanMethodName(), dataTaskContext);
    }

    protected Object executeFieldTask(ContainerFieldInfo containerFieldInfo, Map<String,Object> rowData, List<Map<String,Object>> dbData, DataContext dataContext) {
        DataTaskContext dataTaskContext = DataTaskContext
                                            .builder()
                                            .dataContext(dataContext)
                                            .dbData(dbData)
                                            .containerFieldInfo(containerFieldInfo)
                                            .rowData(rowData)
                                            .build();
        return dataContext.getDataTaskExecutor().execute(containerFieldInfo.getBeanName(), containerFieldInfo.getBeanMethodName(), dataTaskContext);
    }

    protected List<SectionField> prepareData(ContainerInfo containerInfo, List<Map<String,Object>> dbData, DataContext dataContext) {
        dataContext.setDbData(dbData);
        List<SectionField> data = new ArrayList<>();
        ContainerQueryInfo containerQueryInfo = dataContext.getContainerQueryInfo();
        List<ContainerFieldInfo> containerFieldInfoList;
        if(ContainerType.FILTER_VALUE_MULTI_ROW.equals(dataContext.getContainerType())) {
            containerFieldInfoList = containerFieldInfoRepo.findByIdAndActive(dataContext.getFilterFieldId(), StringConstant.Y);
        }
        else {
            containerFieldInfoList = containerFieldInfoRepo.findByContainerIdAndActiveOrderByDisplayOrder(containerQueryInfo.getContainerId(), StringConstant.Y);
        }

        if(!CollectionUtils.isEmpty(containerFieldInfoList)) {
            dataContext.setCurrentContainerFieldInfoList(containerFieldInfoList);
            if(!CollectionUtils.isEmpty(dbData)) {
                data = ContainerType.MULTI_ROW.equals(dataContext.getContainerType())
                        || ContainerType.FILTER_VALUE_MULTI_ROW.equals(dataContext.getContainerType()) ? prepareMultiRowSectionFields(containerInfo, dbData, containerFieldInfoList, dataContext) : prepareSectionFields(containerInfo, dbData, containerFieldInfoList, dataContext);
            }
        }
        return data;
    }

    protected List<SectionField> prepareSectionFields(ContainerInfo containerInfo, List<Map<String,Object>> dbData, List<ContainerFieldInfo> containerFieldInfoList, DataContext dataContext) {
        List<SectionField> data = new ArrayList<>();
        for (Map<String,Object> rowData : dbData) {
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
                    addContainerFieldDataIntoSection(sectionField, dataContext, containerFieldInfo);
                }
                else {
                    sectionField.setFieldValue(rowData.get(column.toUpperCase()));
                }
                data.add(sectionField);
            }
        }
        return data;
    }

    protected List<SectionField> prepareMultiRowSectionFields(ContainerInfo containerInfo, List<Map<String,Object>> dbData, List<ContainerFieldInfo> containerFieldInfoList, DataContext dataContext) {
        List<SectionField> data = new ArrayList<>();
        Map<String, ContainerFieldInfo> columnNameKeyedContainerFieldInfo = containerFieldInfoList.stream().collect(Collectors.toMap(info -> info.getMappedColumnName().toUpperCase(), info -> info, (o, n) -> n));
        Set<String> fieldColumns = columnNameKeyedContainerFieldInfo.keySet();
        if(!CollectionUtils.isEmpty(fieldColumns)) {
            String multiRowColumn = new ArrayList<>(fieldColumns).get(0);
            List<Object> fieldValues = new ArrayList<>();
            ContainerFieldInfo containerFieldInfo = columnNameKeyedContainerFieldInfo.get(multiRowColumn.toUpperCase());
            SectionField sectionField = prepareSectionFieldsUsingContainerFieldInfo(dataContext, containerInfo, containerFieldInfo);
            for (Map<String,Object> rowData : dbData) {
                if(containerFieldInfo.getBeanName() != null ) {
                    executeFieldTask(containerFieldInfo, rowData, dbData, dataContext);
                }
                Object fieldValue = rowData.get(multiRowColumn.toUpperCase());
                if(fieldValue != null) {
                    fieldValues.add(fieldValue);
                }
            }
            sectionField.setFieldValues(fieldValues);
            data.add(sectionField);
        }
        return data;
    }

    protected static SectionField prepareSectionFieldsUsingContainerFieldInfo(DataContext dataContext, ContainerInfo containerInfo, ContainerFieldInfo containerFieldInfo) {
        SectionField sectionField = new SectionField();
        sectionField.setContainerId(containerFieldInfo.getContainerId());
        sectionField.setSectionId(dataContext.getSectionId());
        sectionField.setFieldId(containerFieldInfo.getId());
        sectionField.setGroupId(containerFieldInfo.getGroupId());
        sectionField.setFieldDisplayName(containerFieldInfo.getFieldDisplayName());
        sectionField.setFieldType(containerFieldInfo.getFieldType());
        sectionField.setFieldLength(containerFieldInfo.getFieldLength());
        sectionField.setFieldMinLength(containerFieldInfo.getFieldMinLength());
        sectionField.setFieldMaxLength(containerFieldInfo.getFieldMaxLength());
        sectionField.setFieldTypeId(containerFieldInfo.getFieldTypeId());
        sectionField.setDisplayOrder(containerFieldInfo.getDisplayOrder());
        sectionField.setSortable(TranslateUtils.ynToBool(containerFieldInfo.getSortable()));
        sectionField.setFilterable(TranslateUtils.ynToBool(containerFieldInfo.getFilterable()));
        sectionField.setEnabled(TranslateUtils.ynToBool(containerFieldInfo.getEnabled()));
        sectionField.setVisible(TranslateUtils.ynToBool(containerFieldInfo.getVisible()));
        sectionField.setEditable(TranslateUtils.ynToBool(containerFieldInfo.getEditable()));
        sectionField.setRequired(TranslateUtils.ynToBool(containerFieldInfo.getRequired()));
        return sectionField;
    }

    protected List<?>  fetchResultList(DataContext dataContext, String queryString) {
        Map<String, Object> queryParams = buildQueryParamsUsingQueryParamKeysFromDB(dataContext);
        return execute(queryString, queryParams);
    }

    protected <T> List<T>  fetchTransformedResultList(DataContext dataContext, String queryString, Class<T> clazz) {
        Map<String, Object> queryParams = buildQueryParamsUsingQueryParamKeysFromDB(dataContext);
        return execute(queryString, queryParams, clazz);
    }

    protected List<Map<String, Object>> fetchColumKeyedResultList(DataContext dataContext, String queryString, boolean hasFromToRow) {
        Map<String, Object> queryParams = buildQueryParamsUsingQueryParamKeysFromDB(dataContext, hasFromToRow);
        return execute(queryString, queryParams, true);
    }


    protected List<Map<String, Object>> fetchColumKeyedResultList(DataContext dataContext, String queryString, List<String> queryParamKeys) {
        Map<String, Object> queryParams = buildQueryParams(dataContext, queryParamKeys);
        return execute(queryString, queryParams, true);
    }

    protected Map<String, Object> buildQueryParamsUsingQueryParamKeysFromDB(DataContext dataContext) {
        ContainerQueryInfo containerQueryInfo = dataContext.getContainerQueryInfo();
        return buildQueryParams(dataContext, containerQueryInfo.queryParamKeys());
    }

    protected Map<String, Object> buildQueryParams(DataContext dataContext, List<String> queryParamKeys ) {
        Map<String, Object> queryParams = new HashMap<>();
        Map<String, Object> inputParams = dataContext.getInputParams();
        if(!CollectionUtils.isEmpty(queryParamKeys)) {
            queryParamKeys.forEach(queryParamKey -> {
                if(QueryParamsKey.JSON_STRING.equals(queryParamKey)) {
                    String jsonString = "{}";
                    try {
                        JsonStringQueryParams<String, Object> jsonStringQueryParams = new JsonStringQueryParams<>();
                        jsonStringQueryParams.putAll(inputParams);
                        jsonString = new ObjectMapper().writeValueAsString(jsonStringQueryParams);
                    }
                    catch (Exception ignore) {

                    }
                    queryParams.put(queryParamKey, jsonString);
                }
                else if(inputParams.containsKey(queryParamKey)) {
                    queryParams.put(queryParamKey, inputParams.get(queryParamKey));
                }
            });
        }
        return queryParams;
    }

    protected Map<String, Object> buildQueryParamsUsingQueryParamKeysFromDB(DataContext dataContext, boolean hasFromToRow) {
        Map<String, Object> queryParams = buildQueryParamsUsingQueryParamKeysFromDB(dataContext);
        if(hasFromToRow) {
            addFromToRowQueryParams( queryParams, dataContext);
        }
        return queryParams;
    }

    protected void addFromToRowQueryParams(Map<String, Object> queryParams, DataContext dataContext) {
        PaginationInfo paginationInfo = dataContext.paginationInfo(dataContext);
        queryParams.put(PaginationHelper.FROM_ROW_QUERY_PARAM_KEY, paginationInfo.getFromRow());
        queryParams.put(PaginationHelper.TO_ROW_QUERY_PARAM_KEY, paginationInfo.getToRow());
    }

    protected List<Map<String, Object>> fetchCommonPageDataList(DataContext dataContext) {
        List<PageCommonQueryInfo> pageCommonQueryInfos = dataContext.getPageCommonQueryInfos();
        List<Map<String, Object>> commonData = DataContext.commonPageDataList(dataContext);
        for(PageCommonQueryInfo pageCommonQueryInfo: pageCommonQueryInfos) {
            String queryString = pageCommonQueryInfo.getQueryString();
            List<String> queryParamKeys = pageCommonQueryInfo.queryParamKeys();
            if(CollectionUtils.isEmpty(dataContext.getCommonQueriesExecuted()) || !dataContext.getCommonQueriesExecuted().contains(pageCommonQueryInfo.getId())) {
                queryString = resolvePlaceHolders(queryString, dataContext, new WhereQueryString(new OrderByQueryString()));
                List<Map<String, Object>> dbData = fetchColumKeyedResultList(dataContext, queryString, queryParamKeys);
                if(!CollectionUtils.isEmpty(dbData)) {
                    commonData.addAll(dbData);
                    Set<Integer> executedQueryIds = new HashSet<>();
                    executedQueryIds.add(pageCommonQueryInfo.getId());
                    dataContext.setCommonQueriesExecuted(executedQueryIds);
                }
            }
        }
        dataContext.setCommonPageDataList(commonData);
        return DataContext.commonPageDataList(dataContext);
    }

    protected List<Map<String, Object>> fetchCommonPageData(DataContext dataContext) {
        List<PageCommonQueryInfo> pageCommonQueryInfos = dataContext.getPageCommonQueryInfos();
        Map<String, Object> commonData = DataContext.commonPageData(dataContext);
        for(PageCommonQueryInfo pageCommonQueryInfo: pageCommonQueryInfos) {
            String queryString = pageCommonQueryInfo.getQueryString();
            List<String> queryParamKeys = pageCommonQueryInfo.queryParamKeys();
            if(CollectionUtils.isEmpty(dataContext.getCommonQueriesExecuted()) || !dataContext.getCommonQueriesExecuted().contains(pageCommonQueryInfo.getId())) {
                queryString = resolvePlaceHolders(queryString, dataContext, new WhereQueryString(new OrderByQueryString()));
                List<Map<String, Object>> dbData = fetchColumKeyedResultList(dataContext, queryString, queryParamKeys);
                if(!CollectionUtils.isEmpty(dbData)) {
                    commonData.putAll(dbData.get(0));
                    Set<Integer> executedQueryIds = new HashSet<>();
                    executedQueryIds.add(pageCommonQueryInfo.getId());
                    dataContext.setCommonQueriesExecuted(executedQueryIds);
                }
            }
        }
        dataContext.setCommonPageData(commonData);
        Map<String, Object> commonPageData = DataContext.commonPageData(dataContext);
        List<Map<String, Object>> dbData = new ArrayList<>();
        dbData.add(commonPageData);
        return dbData;
    }

    protected void addContainerFieldDataIntoSection(SectionField sectionField, DataContext dataContext, ContainerFieldInfo containerFieldInfo) {
        try {
            ContainerInfo subContainerInfo = dataContext.getContainerInfoRepo().findById(containerFieldInfo.getSubContainerId()).orElse(null);
            if(subContainerInfo != null) {
                ContainerInfo parentContainerInfo = ObjectCopier.clone(dataContext.getContainerInfo(), ContainerInfo.class);
                dataContext.setContainerInfo(subContainerInfo);
                boolean parentPagination = dataContext.paginate(parentContainerInfo.getId());
                ContainerQueryInfo parentContainerQueryInfo = ObjectCopier.clone(dataContext.getContainerQueryInfo(), ContainerQueryInfo.class);
                List<ContainerFieldInfo> parentContainerFieldInfoList = ObjectCopier.clone(dataContext.getCurrentContainerFieldInfoList(), ContainerFieldInfo.class) ;
                ContainerQueryInfo subContainerQueryInfo = dataContext.getContainerQueryInfoRepo().findByContainerId(containerFieldInfo.getSubContainerId()).orElse(null);
                dataContext.setPaginate(subContainerQueryInfo!=null && subContainerQueryInfo.getPaginationQueryString()!=null);
                List<ContainerFieldInfo> subContainerFieldInfoList = containerFieldInfoRepo.findByContainerIdAndActiveOrderByDisplayOrder(containerFieldInfo.getSubContainerId(), StringConstant.Y);
                dataContext.setContainerQueryInfo(subContainerQueryInfo);
                dataContext.setCurrentContainerFieldInfoList(subContainerFieldInfoList);
                ContainerType containerType = ContainerType.type(subContainerInfo.getContainerType());
                if(containerType.isTable()) {
                    ContainerTableDataGenerator containerTableDataGenerator = dataContext.getDataGeneratorFactory().containerTableDataGenerator(containerType);
                    List<List<SectionField>> tableData = containerTableDataGenerator.generate(dataContext);
                    List<SectionField> headers = containerTableDataGenerator.generateHeaders(dataContext, subContainerInfo);
                    sectionField.setTableHeaders(headers);
                    sectionField.setTableData(tableData);
                }
                else {
                    ContainerDataGenerator containerDataGenerator = dataContext.getDataGeneratorFactory().containerDataGenerator(containerType);
                    List<SectionField> data = containerDataGenerator.generate(dataContext);
                    sectionField.setData(data);
                }
                sectionField.setContainerId(subContainerInfo.getId());
                sectionField.setContainerDisplayName(subContainerInfo.getContainerDisplayName());
                sectionField.setContainerType(containerType.value());
                dataContext.setContainerQueryInfo(parentContainerQueryInfo);
                dataContext.setCurrentContainerFieldInfoList(parentContainerFieldInfoList);
                dataContext.setContainerInfo(parentContainerInfo);
                dataContext.setPaginate(parentPagination, parentContainerInfo.getId());
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
