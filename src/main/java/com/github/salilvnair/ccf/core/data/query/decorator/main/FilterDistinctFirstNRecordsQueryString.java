package com.github.salilvnair.ccf.core.data.query.decorator.main;

import com.github.salilvnair.ccf.core.model.FilterParam;
import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.query.decorator.base.AbstractQueryString;
import com.github.salilvnair.ccf.core.data.query.decorator.base.QueryString;
import com.github.salilvnair.ccf.core.data.query.type.PlaceHolderType;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class FilterDistinctFirstNRecordsQueryString extends AbstractQueryString {

    private int n = -1;


    public FilterDistinctFirstNRecordsQueryString(int n) {
        super();
        this.n = n;
    }

    public FilterDistinctFirstNRecordsQueryString(QueryString queryString, int n) {
        super(queryString);
        this.n = n;
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
        if(n > 0) {
            replaceString = "FETCH FIRST "+n+" ROWS ONLY";
        }
        if(dataContext.isFilter() && !CollectionUtils.isEmpty(filterParams)
                && !filterParams.stream().allMatch(filterParam -> CollectionUtils.isEmpty(filterParam.getFieldValues()) && filterParam.getFieldValue() == null)) {
            boolean anyLikeFilter = filterParams.stream().anyMatch(FilterParam::isLikeMatch);
            if(anyLikeFilter) {
                replaceString = "";
            }
        }
        return replaceString;
    }

    @Override
    public PlaceHolderType placeHolder(DataContext dataContext) {
        return PlaceHolderType.FETCH_FIRST_N_ROWS_PLACE_HOLDER;
    }
}
