package com.github.salilvnair.ccf.core.data.handler.provider.container;

import com.github.salilvnair.ccf.core.model.MultiConditionFilterParams;
import com.github.salilvnair.ccf.core.component.constant.ComponentInputParamsKey;
import com.github.salilvnair.ccf.core.component.helper.ComponentHelper;
import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.handler.core.TransformedContainerDataGenerator;
import com.github.salilvnair.ccf.core.data.query.decorator.main.MultiConditionWhereQueryString;
import com.github.salilvnair.ccf.core.entity.ContainerQueryInfo;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public abstract class AbstractTransformedContainerGenerator extends AbstractContainerGenerator implements TransformedContainerDataGenerator {

    protected <T> List<T> fetchTransformedResultList(DataContext dataContext, Class<T> clazz) {
        ContainerQueryInfo containerQueryInfo = dataContext.getContainerQueryInfo();
        if(containerQueryInfo == null) {
            return Collections.emptyList();
        }
        String queryString = containerQueryInfo.getQueryString();
        if (queryString == null) {
            return Collections.emptyList();
        }
        Map<String, MultiConditionFilterParams> containerMultiConditionFilterParams = ComponentHelper.extractObjectValueMap(ComponentInputParamsKey.CONTAINER_MULTI_CONDITION_FILTER_PARAMS, dataContext.inputParams(), String.class, MultiConditionFilterParams.class);
        Map<Integer, MultiConditionFilterParams> containerMultiConditionFilterNumberParams = new HashMap<>();
        if(containerMultiConditionFilterParams != null) {
            containerMultiConditionFilterParams.forEach((key, value) -> containerMultiConditionFilterNumberParams.put(Integer.parseInt(key), value));
        }
        dataContext.setContainerMultiConditionFilterParams(containerMultiConditionFilterNumberParams);
        dataContext.setFilter(!containerMultiConditionFilterNumberParams.isEmpty());
        queryString = resolvePlaceHolders(queryString, dataContext, new MultiConditionWhereQueryString());
        return fetchTransformedResultList(dataContext, queryString, clazz);
    }
}
