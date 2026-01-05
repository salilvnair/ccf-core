package com.github.salilvnair.ccf.core.data.query.decorator.base;

import com.github.salilvnair.ccf.core.model.MultiConditionFilterParams;
import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.query.context.QueryContext;
import com.github.salilvnair.ccf.core.data.type.FieldIdType;
import com.github.salilvnair.ccf.core.entity.ContainerFieldInfo;
import com.github.salilvnair.ccf.core.entity.ContainerInfo;
import com.github.salilvnair.ccf.core.model.FilterParam;
import com.github.salilvnair.ccf.core.model.SortParam;
import com.github.salilvnair.ccf.core.repository.ContainerFieldInfoRepo;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractQueryString implements QueryString {
    protected QueryString queryString;
    public AbstractQueryString() {

    }
    public AbstractQueryString(QueryString queryString) {
        this.queryString = queryString;
    }

    @Override
    public String build(DataContext dataContext) {
        return queryString.build(dataContext);
    }

    public String sortReplaceString(DataContext dataContext, ContainerFieldInfoRepo containerFieldInfoRepo) {
        List<SortParam> sortParams = extractApplicableSortParams(dataContext);
        String replaceString = null;
        if(!CollectionUtils.isEmpty(sortParams)) {
             replaceString = sortParams.stream().map(param -> {
                ContainerFieldInfo fieldInfo = containerFieldInfoRepo.findById(param.getFieldId()).orElse(null);
                return fieldInfo!=null ? resolveOrderFormatsIfApplicable(fieldInfo.getMappedColumnName(), fieldInfo) + " " + (param.isDesc() ? "DESC" : "ASC"): "";
            }).collect(Collectors.joining(","));
        }
        return replaceString;
    }

    public List<SortParam> extractApplicableSortParams(DataContext dataContext) {
        ContainerInfo containerInfo = dataContext.getContainerInfo();
        List<SortParam> sortParams = dataContext.getSortParams();
        Map<Integer, List<SortParam>> containerSortParamsMap = dataContext.getContainerSortParams();
        if(containerSortParamsMap!=null && !containerSortParamsMap.isEmpty() && containerSortParamsMap.containsKey(containerInfo.getId())) {
            sortParams = containerSortParamsMap.get(containerInfo.getId());
        }
        return sortParams;
    }

    public String filterReplaceString(DataContext dataContext, ContainerFieldInfoRepo containerFieldInfoRepo) {
        List<FilterParam> filterParams = dataContext.getFilterParams();
        String replaceString = null;
        if(!CollectionUtils.isEmpty(filterParams)) {
            replaceString = filterParams.stream().map(param -> {
                ContainerFieldInfo fieldInfo = containerFieldInfoRepo.findById(param.getFieldId()).orElse(null);
                if(fieldInfo !=null && param.isFieldValueNull()) {
                    return fieldInfo.getMappedColumnName()+ " is null";
                }
                else  if (fieldInfo !=null && CollectionUtils.isEmpty(param.getFieldValues()) && param.getFieldValue() == null ) {
                    return "";
                }
                if(param.isLikeMatch() && (!CollectionUtils.isEmpty(param.getFieldValues()) || param.getFieldValue() != null) ) {
                    String fieldValue = param.getFieldValue();
                    if(fieldValue == null) {
                        fieldValue = param.getFieldValues().get(0);
                    }
                    return fieldInfo!=null ? fieldInfo.mappedColumnAsLower() + " LIKE " + resolveWhereLikeAsLower(fieldValue, fieldInfo) : "";
                }
                return fieldInfo!=null ? !CollectionUtils.isEmpty(param.getFieldValues()) ? fieldInfo.getMappedColumnName()+ " in ("+ param.getFieldValues().stream().map(f -> resolveWhereFormatsIfApplicable(f, fieldInfo)).collect(Collectors.joining(", ")) +")" :
                        fieldInfo.getMappedColumnName() + "=" + resolveWhereFormatsIfApplicable(param.getFieldValue(), fieldInfo) : "";
            }).collect(Collectors.joining(" and "));
        }
        replaceString = replaceString == null ? null : replaceString.replaceAll(" and $", "");
        return replaceString;
    }

    public String multiConditionFilterReplaceString(DataContext dataContext, ContainerFieldInfoRepo containerFieldInfoRepo) {
        List<FilterParam> filterParams = dataContext.getFilterParams();
        String replaceString = null;
        if (!CollectionUtils.isEmpty(filterParams)) {
            Map<Integer, String> fieldIdToConditionMap = new HashMap<>();
            filterParams.forEach(param -> {
                ContainerFieldInfo fieldInfo = containerFieldInfoRepo.findById(param.getFieldId()).orElse(null);
                if (fieldInfo != null) {
                    String condition = "";
                    if (param.isFieldValueNull()) {
                        condition = fieldInfo.getMappedColumnName() + " is null";
                    }
                    else if (CollectionUtils.isEmpty(param.getFieldValues()) && param.getFieldValue() == null) {
                        condition = "";
                    }
                    else if (param.isLikeMatch()) {
                        String fieldValue = param.getFieldValue();
                        if (fieldValue == null) {
                            fieldValue = param.getFieldValues().get(0);
                        }
                        condition = fieldInfo.mappedColumnAsLower() + " LIKE " + resolveWhereLikeAsLower(fieldValue, fieldInfo);
                    }
                    else {
                        condition = !CollectionUtils.isEmpty(param.getFieldValues()) ? fieldInfo.getMappedColumnName() + " in (" + param.getFieldValues().stream().map(f -> resolveWhereFormatsIfApplicable(f, fieldInfo)).collect(Collectors.joining(", ")) + ")" : fieldInfo.getMappedColumnName() + "=" + resolveWhereFormatsIfApplicable(param.getFieldValue(), fieldInfo);
                    }
                    fieldIdToConditionMap.put(param.getFieldId(), condition);
                }
            });
            String condition = dataContext.getMultiConditionFilterParams().getCondition();
            for (Map.Entry<Integer, String> fieldIdEntry : fieldIdToConditionMap.entrySet()) {
                condition = condition.replace(fieldIdEntry.getKey()+"", fieldIdEntry.getValue());
            }
            replaceString = condition;
        }
        return replaceString;
    }

    private String resolveWhereLikeAsLower(String fieldValue, ContainerFieldInfo fieldInfo) {
        return "LOWER('%" + fieldValue + "%')";
    }

    public List<FilterParam> extractApplicableFilterParams(DataContext dataContext) {
        ContainerInfo containerInfo = dataContext.getContainerInfo();
        List<FilterParam> filterParams = dataContext.getFilterParams();
        Map<Integer, List<FilterParam>> containerFilterParams = dataContext.getContainerFilterParams();
        Integer parentContainerId = containerInfo.getParentContainerId();
        if(containerFilterParams!=null && !containerFilterParams.isEmpty()) {
            Integer containerId = containerInfo.getId();
            if(containerFilterParams.containsKey(containerId)) {
                filterParams = containerFilterParams.get(containerId);
            }
            else if (parentContainerId != null && containerFilterParams.containsKey(parentContainerId)) {
                filterParams = containerFilterParams.get(parentContainerId);
            }
        }
        return filterParams;
    }

    public List<FilterParam> extractApplicableMultiConditionFilterParams(DataContext dataContext) {
        ContainerInfo containerInfo = dataContext.getContainerInfo();
        List<FilterParam> filterParams = new ArrayList<>();
        Map<Integer, MultiConditionFilterParams> containerMultiConditionFilterParams = dataContext.getContainerMultiConditionFilterParams();
        if(containerMultiConditionFilterParams!=null && !containerMultiConditionFilterParams.isEmpty()) {
            Integer containerId = containerInfo.getId();
            if(containerMultiConditionFilterParams.containsKey(containerId)) {
                MultiConditionFilterParams multiConditionFilterParams = containerMultiConditionFilterParams.get(containerId);
                dataContext.setMultiConditionFilterParams(multiConditionFilterParams);
                filterParams = multiConditionFilterParams.getFilterParams();
            }
        }
        return filterParams;
    }

    private String resolveWhereFormatsIfApplicable(String fieldValue, ContainerFieldInfo fieldInfo) {
        String formattedString = "'" + fieldValue + "'";
        if(fieldInfo.getFieldFormat() == null) {
            return formattedString;
        }
        if(FieldIdType.DATE.id() == fieldInfo.getFieldTypeId()) {
            formattedString =  "TO_DATE('"+fieldValue+"','"+fieldInfo.getFieldFormat()+"')";
        }
        return formattedString;
    }

    public String resolveOrderFormatsIfApplicable(String mappedColumn, ContainerFieldInfo fieldInfo) {
        String formattedString = mappedColumn;
        if(fieldInfo.getFieldFormat() == null) {
            return formattedString;
        }
        if(FieldIdType.DATE_TEXT.id() == fieldInfo.getFieldTypeId()) {
            formattedString =  "TO_DATE("+mappedColumn+",'"+fieldInfo.getFieldFormat()+"')";
        }
        return formattedString;
    }

    public QueryContext queryContext(DataContext dataContext) {
        return dataContext.getQueryContext();
    }

    public ContainerFieldInfoRepo migrationPageContainerFieldInfoRepo(DataContext dataContext) {
        return queryContext(dataContext).getContainerFieldInfoRepo();
    }

}
