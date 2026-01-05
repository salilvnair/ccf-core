package com.github.salilvnair.ccf.core.data.query.decorator.main;

import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.query.decorator.base.AbstractQueryString;
import com.github.salilvnair.ccf.core.data.query.decorator.base.QueryString;
import com.github.salilvnair.ccf.core.data.query.type.PlaceHolderType;
import com.github.salilvnair.ccf.core.model.SortParam;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class RowNumberOverOrderByQueryString extends AbstractQueryString {

    public RowNumberOverOrderByQueryString() {
        super();
    }

    public RowNumberOverOrderByQueryString(QueryString queryString) {
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
        String sortReplaceString = "row_number() over (order by (SELECT 1))";
        List<SortParam> sortParams = extractApplicableSortParams(dataContext);
        if(dataContext.isSort() && !CollectionUtils.isEmpty(sortParams)) {
            sortReplaceString = sortReplaceString(dataContext, migrationPageContainerFieldInfoRepo(dataContext));
            sortReplaceString = "row_number() over (order by "+sortReplaceString+")";
        }
        return sortReplaceString;
    }

    @Override
    public PlaceHolderType placeHolder(DataContext dataContext) {
        return PlaceHolderType.ROW_NUMBER_ORDER_BY_PLACE_HOLDER;
    }
}
