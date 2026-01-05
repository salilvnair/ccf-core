package com.github.salilvnair.ccf.core.data.handler.provider.container;

import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.exception.DataException;
import com.github.salilvnair.ccf.core.data.handler.core.ContainerDataGenerator;
import com.github.salilvnair.ccf.core.data.query.decorator.main.OrderByQueryString;
import com.github.salilvnair.ccf.core.data.query.decorator.main.WhereQueryString;
import com.github.salilvnair.ccf.core.data.query.type.PlaceHolderType;
import com.github.salilvnair.ccf.core.data.type.ContainerType;
import com.github.salilvnair.ccf.core.entity.ContainerQueryInfo;
import com.github.salilvnair.ccf.core.model.SectionField;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component(value = ContainerType.Value.MULTI_ROW)
public class MultiRowContainerGenerator extends AbstractContainerGenerator implements ContainerDataGenerator {

    @Override
    public List<SectionField> generate(DataContext dataContext) throws DataException {
        List<Map<String, Object>> dbData;
        ContainerQueryInfo containerQueryInfo = dataContext.getContainerQueryInfo();
        if(containerQueryInfo == null) {
            return Collections.emptyList();
        }
        String queryString = containerQueryInfo.getQueryString();
        if(PlaceHolderType.PICK_FROM_COMMON_QUERY.value().equals(queryString)) {
            dbData = fetchCommonPageDataList(dataContext);
        }
        else {
            dbData = fetchSectionData(dataContext);
        }
        return processData(dbData, dataContext);
    }


    private List<Map<String, Object>> fetchSectionData(DataContext dataContext) {
        ContainerQueryInfo containerQueryInfo = dataContext.getContainerQueryInfo();
        String queryString = containerQueryInfo.getQueryString();
        List<String> queryParamKeys = containerQueryInfo.queryParamKeys();
        queryString = resolvePlaceHolders(queryString, dataContext, new WhereQueryString(new OrderByQueryString()));
        return fetchColumKeyedResultList(dataContext, queryString, queryParamKeys);
    }

}
