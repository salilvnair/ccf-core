package com.github.salilvnair.ccf.util.paginator.helper;


import com.github.salilvnair.ccf.util.paginator.model.PaginatedResponse;
import com.github.salilvnair.ccf.util.paginator.model.PaginationInfo;
import com.github.salilvnair.ccf.util.paginator.model.PaginationRequest;

import java.util.Collections;
import java.util.List;
/**
 * @author Salil V Nair
 */
public class PaginationHelper {

    private PaginationHelper() {}

    public static final String FROM_ROW_QUERY_PARAM_KEY = "fromRow";
    public static final String TO_ROW_QUERY_PARAM_KEY = "toRow";

    public static <E> PaginationInfo initPaginator(PaginationRequest<E> paginationRequest, int totalRows) {
        if(paginationRequest == null) {
            return null;
        }
        int currentPage = paginationRequest.getCurrentPage();
        int pageSize = paginationRequest.getPageSize();
        return initPaginator(currentPage, pageSize, totalRows);
    }

    public static PaginationInfo initPaginator(int currentPage, int pageSize, int totalRows) {
        PaginationInfo paginationInfo = new PaginationInfo();

        int to = Math.min(currentPage * pageSize, totalRows);
        int from = ((currentPage * pageSize) - pageSize) + 1;
        //TODO: Change below such a way request should accept paginationInfo container wise as of now its page wise
//        int to = paginationRequest.getToRow() != -1 ? paginationRequest.getToRow() : Math.min(currentPage * pageSize, totalRows);
//        int from = paginationRequest.getFromRow() != -1 ? paginationRequest.getFromRow() : ((currentPage * pageSize) - pageSize) + 1;
        paginationInfo.setHasMoreResults(to != totalRows);
        paginationInfo.setFromRow(from);
        paginationInfo.setToRow(to);
        paginationInfo.setCurrentPage(currentPage);
        paginationInfo.setPageSize(pageSize);
        paginationInfo.setTotalRows(totalRows);
        int totalPages = Math.max(totalRows / pageSize, 0);
        if (totalRows % pageSize > 0) totalPages++;
        paginationInfo.setTotalPages(totalPages);
        paginationInfo.setPreviousPage((currentPage-1) <= 0 ? -1 : (currentPage-1));
        paginationInfo.setNextPage((currentPage+1) > totalPages ? -1 : (currentPage+1));
        return paginationInfo;
    }

    public static <E> PaginatedResponse<E> initPaginatedResponse(PaginationRequest<E> paginationRequest, PaginationInfo paginationInfo) {
        if(paginationRequest == null) {
            return null;
        }
        PaginatedResponse<E> paginatedResponse = new PaginatedResponse<E>();
        paginatedResponse.setFromRow(paginationInfo.getFromRow());
        paginatedResponse.setToRow(paginationInfo.getToRow());
        paginatedResponse.setTotalRows(paginationInfo.getTotalRows());
        paginatedResponse.setPreviousPage(paginationInfo.getPreviousPage());
        paginatedResponse.setCurrentPage(paginationRequest.getCurrentPage());
        paginatedResponse.setNextPage(paginationInfo.getNextPage());
        paginatedResponse.setPageSize(paginationRequest.getPageSize());
        paginatedResponse.setTotalPages(paginationInfo.getTotalPages());
        return paginatedResponse;
    }

    public static <E> PaginatedResponse<E> buildPaginatedResponse(PaginationRequest<E> paginationRequest, PaginationInfo paginationInfo, List<E> results) {
        PaginatedResponse<E> paginatedResponse = initPaginatedResponse(paginationRequest, paginationInfo);
        if(!results.isEmpty()) {
            paginatedResponse.setResults(Collections.unmodifiableList(results));
            paginatedResponse.setHasMoreResults(paginationInfo.isHasMoreResults());
        }
        paginatedResponse.setPaginationInfo(paginationInfo);
        return paginatedResponse;
    }

}
