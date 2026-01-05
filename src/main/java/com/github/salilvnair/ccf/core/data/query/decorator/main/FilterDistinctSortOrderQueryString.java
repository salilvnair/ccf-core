package com.github.salilvnair.ccf.core.data.query.decorator.main;

import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.query.decorator.base.AbstractQueryString;
import com.github.salilvnair.ccf.core.data.query.decorator.base.QueryString;
import com.github.salilvnair.ccf.core.data.query.type.PlaceHolderType;
import com.github.salilvnair.ccf.core.entity.ContainerFieldInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterDistinctSortOrderQueryString extends AbstractQueryString {

    private Logger logger = LoggerFactory.getLogger(FilterDistinctSortOrderQueryString.class);

    public FilterDistinctSortOrderQueryString() {
        super();
    }

    public FilterDistinctSortOrderQueryString(QueryString queryString) {
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
        String sortReplaceString = "";
        if(dataContext.getFilterFieldId() == null) {
            logger.info("FilterDistinctSortOrderQueryString>> replaceWith >> filterFieldId is null");
            return sortReplaceString;
        }
        ContainerFieldInfo fieldInfo = migrationPageContainerFieldInfoRepo(dataContext).findById(dataContext.getFilterFieldId()).orElse(null);
        if(fieldInfo==null) {
            return sortReplaceString;
        }
        sortReplaceString = resolveOrderFormatsIfApplicable(fieldInfo.getMappedColumnName(), fieldInfo);
        sortReplaceString = "order by "+sortReplaceString;
        return sortReplaceString;
    }

    @Override
    public PlaceHolderType placeHolder(DataContext dataContext) {
        return PlaceHolderType.ORDER_BY_PLACE_HOLDER;
    }
}
