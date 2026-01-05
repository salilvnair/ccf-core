package com.github.salilvnair.ccf.util.paginator.service;


import com.github.salilvnair.ccf.util.paginator.helper.PaginationHelper;
import com.github.salilvnair.ccf.util.paginator.model.PaginatedResponse;
import com.github.salilvnair.ccf.util.paginator.model.PaginationInfo;
import com.github.salilvnair.ccf.util.paginator.model.PaginationRequest;

import java.util.List;
import java.util.Map;

/**
 * @author Salil V Nair
 */
public interface Paginator<E> {
    int totalRows(PaginationRequest<E> paginationRequest, Map<String, Object> inputParams);
    List<E> results(PaginationRequest<E> paginationRequest, PaginationInfo paginationInfo, Map<String, Object> inputParams);
    default PaginatedResponse<E> paginate(PaginationRequest<E> paginationRequest, Map<String, Object> inputParams) {
        if(paginationRequest == null) {
            return null;
        }
        int totalRows = totalRows(paginationRequest, inputParams);
        //TODO: Change below such a way request should accept paginationInfo container wise as of now its page wise
//        int totalRows = paginationRequest.getTotalRows()!=-1 ? paginationRequest.getTotalRows() : totalRows(paginationRequest, inputParams);
        PaginationInfo paginationInfo = PaginationHelper.initPaginator(paginationRequest, totalRows);
        List<E> results = null;
        results = results(paginationRequest, paginationInfo, inputParams);
        return PaginationHelper.buildPaginatedResponse(paginationRequest, paginationInfo, results);
    }
}
