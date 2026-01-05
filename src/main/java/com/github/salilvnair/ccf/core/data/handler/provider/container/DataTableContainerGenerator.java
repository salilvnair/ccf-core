package com.github.salilvnair.ccf.core.data.handler.provider.container;

import com.github.salilvnair.ccf.core.model.SectionField;
import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.exception.DataException;
import com.github.salilvnair.ccf.core.data.handler.core.ContainerTableDataGenerator;
import com.github.salilvnair.ccf.core.data.query.decorator.main.OrderByQueryString;
import com.github.salilvnair.ccf.core.data.query.decorator.main.RowNumberOverOrderByQueryString;
import com.github.salilvnair.ccf.core.data.query.decorator.main.WhereQueryString;
import com.github.salilvnair.ccf.core.data.type.ContainerType;
import com.github.salilvnair.ccf.core.entity.ContainerQueryInfo;
import com.github.salilvnair.ccf.util.paginator.model.PaginatedResponse;
import com.github.salilvnair.ccf.util.paginator.model.PaginationInfo;
import com.github.salilvnair.ccf.util.paginator.model.PaginationRequest;
import com.github.salilvnair.ccf.util.paginator.service.Paginator;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component(value = ContainerType.Value.DATA_TABLE)
public class DataTableContainerGenerator extends AbstractTableContainerGenerator implements ContainerTableDataGenerator, Paginator<List<SectionField>> {

    @Override
    public List<List<SectionField>> generate(DataContext dataContext) throws DataException {
        return dataContext.paginate(dataContext) ? paginatedResults(dataContext) : results(dataContext);
    }


    @Override
    public int totalRows(PaginationRequest<List<SectionField>> paginationRequest, Map<String, Object> inputParams) {
        DataContext dataContext = DataContext.eject(inputParams);
        return totalRows(dataContext);
    }

    @Override
    public List<List<SectionField>> results(PaginationRequest<List<SectionField>> paginationRequest, PaginationInfo paginationInfo, Map<String, Object> inputParams) {
        DataContext dataContext = DataContext.eject(inputParams);
        dataContext.setPaginationInfo(dataContext, paginationInfo);
        ContainerQueryInfo containerQueryInfo = dataContext.getContainerQueryInfo();
        if(containerQueryInfo == null) {
            return Collections.emptyList();
        }
        String paginationQueryString = containerQueryInfo.getPaginationQueryString();
        paginationQueryString = resolvePlaceHolders(paginationQueryString, dataContext, new WhereQueryString(new RowNumberOverOrderByQueryString(new OrderByQueryString())));
        List<Map<String, Object>> dbData = fetchColumKeyedResultList(dataContext, paginationQueryString, true);
        return processTableData(dbData, dataContext);
    }

    private List<List<SectionField>> paginatedResults(DataContext dataContext) {
        Map<String, Object> inputParams = dataContext.getInputParams();
        DataContext.inject(inputParams, dataContext);
        PaginationRequest<List<SectionField>> paginationRequest = new PaginationRequest<>(dataContext.paginationInfo(dataContext));
        PaginatedResponse<List<SectionField>> paginatedResponse = paginate(paginationRequest, inputParams);
        dataContext.setPaginationInfo(dataContext, paginatedResponse.getPaginationInfo());
        return paginatedResponse.getResults();
    }
}
