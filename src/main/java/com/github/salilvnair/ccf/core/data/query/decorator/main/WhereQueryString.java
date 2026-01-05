package com.github.salilvnair.ccf.core.data.query.decorator.main;

import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.query.decorator.base.AbstractQueryString;
import com.github.salilvnair.ccf.core.data.query.decorator.base.QueryString;
import com.github.salilvnair.ccf.core.data.query.type.PlaceHolderType;
import com.github.salilvnair.ccf.core.model.FilterParam;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class WhereQueryString extends AbstractQueryString {

    public WhereQueryString() {
        super();
    }

    public WhereQueryString(QueryString queryString) {
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
        String replaceString = "";
        List<FilterParam> filterParams = extractApplicableFilterParams(dataContext);
        if(dataContext.isFilter() && !CollectionUtils.isEmpty(filterParams)
                && !filterParams.stream().allMatch(filterParam -> CollectionUtils.isEmpty(filterParam.getFieldValues()) && filterParam.getFieldValue() == null)) {
            dataContext.setFilterParams(filterParams);
            replaceString = filterReplaceString(dataContext, migrationPageContainerFieldInfoRepo(dataContext));
            replaceString = "where  "+replaceString;
        }
        return replaceString;
    }

    @Override
    public PlaceHolderType placeHolder(DataContext dataContext) {
        return PlaceHolderType.WHERE_FILTER_BY_PLACE_HOLDER;
    }
}
