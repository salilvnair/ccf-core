package com.github.salilvnair.ccf.core.data.query.decorator.main;

import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.query.decorator.base.AbstractQueryString;
import com.github.salilvnair.ccf.core.data.query.decorator.base.QueryString;
import com.github.salilvnair.ccf.core.data.query.type.PlaceHolderType;
import com.github.salilvnair.ccf.core.data.type.FieldIdType;
import com.github.salilvnair.ccf.core.entity.ContainerFieldInfo;
import com.github.salilvnair.ccf.core.repository.ContainerFieldInfoRepo;

public class FilterDistinctQueryString extends AbstractQueryString {

    public FilterDistinctQueryString() {
        super();
    }

    public FilterDistinctQueryString(QueryString queryString) {
        super(queryString);
    }

    @Override
    public String build(DataContext dataContext) {
        String queryString = this.queryString != null ? super.build(dataContext) : queryContext(dataContext).getQueryString();
        queryString = queryString.replace(placeHolder(dataContext).value(), replaceWith(dataContext));
        return queryString;
    }

    @Override
    public String replaceWith(DataContext dataContext) {
        return replaceMappedColumn(dataContext, migrationPageContainerFieldInfoRepo(dataContext));
    }

    @Override
    public PlaceHolderType placeHolder(DataContext dataContext) {
        return PlaceHolderType.FILTER_BY_FIELD_PLACEHOLDER;
    }

    private String replaceMappedColumn(DataContext dataContext, ContainerFieldInfoRepo containerFieldInfoRepo) {
        ContainerFieldInfo fieldInfo = containerFieldInfoRepo.findById(dataContext.getFilterFieldId()).orElse(null);
        if(fieldInfo==null) {
            return "";
        }
        String mappedVal = fieldInfo.getMappedColumnName();
        if(fieldInfo.getFieldFormat() == null) {
            return mappedVal;
        }
        if(FieldIdType.DATE.id() == fieldInfo.getFieldTypeId()) {
            mappedVal = "TO_CHAR("+mappedVal+",'"+fieldInfo.getFieldFormat()+"') "+mappedVal;
        }
        return mappedVal;
    }
}
