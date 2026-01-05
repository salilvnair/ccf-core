package com.github.salilvnair.ccf.core.data.handler.provider.container;

import com.github.salilvnair.ccf.core.model.SectionField;
import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.exception.DataException;
import com.github.salilvnair.ccf.core.data.handler.core.ContainerDataGenerator;
import com.github.salilvnair.ccf.core.data.query.decorator.main.FilterDistinctQueryString;
import com.github.salilvnair.ccf.core.data.query.decorator.main.FilterDistinctSortOrderQueryString;
import com.github.salilvnair.ccf.core.data.query.decorator.main.FilterDistinctWhereQueryString;
import com.github.salilvnair.ccf.core.data.type.ContainerType;
import com.github.salilvnair.ccf.core.entity.ContainerQueryInfo;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component(value = ContainerType.Value.FILTER_VALUE_MULTI_ROW)
public class FilterValueMultiRowContainerGenerator extends AbstractContainerGenerator implements ContainerDataGenerator {
    @Override
    public List<SectionField> generate(DataContext dataContext) throws DataException {
        List<Map<String, Object>> dbData = fetchFilterValues(dataContext);
        return processData(dbData, dataContext);
    }


    private List<Map<String, Object>> fetchFilterValues(DataContext dataContext) {
        ContainerQueryInfo containerQueryInfo = dataContext.getContainerQueryInfo();
        if(containerQueryInfo == null) {
            return Collections.emptyList();
        }
        String queryString = containerQueryInfo.getQueryString();
        if(queryString == null) {
            return Collections.emptyList();
        }
        List<String> queryParamKeys = containerQueryInfo.queryParamKeys();
        queryString = resolvePlaceHolders(queryString, dataContext, new FilterDistinctWhereQueryString(new FilterDistinctQueryString(new FilterDistinctSortOrderQueryString())));
        return fetchColumKeyedResultList(dataContext, queryString, queryParamKeys);
    }

}
