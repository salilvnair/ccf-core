package com.github.salilvnair.ccf.util.paginator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Salil V Nair
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class PaginatedResponse<E> {
    private Integer fromRow;
    private Integer toRow;
    private Integer totalRows;
    private Integer previousPage;
    private Integer currentPage;
    private Integer nextPage;
    private Integer totalPages;
    private Integer pageSize;
    private List<E> results;
    private boolean hasMoreResults;
    private PaginationInfo paginationInfo;

}
