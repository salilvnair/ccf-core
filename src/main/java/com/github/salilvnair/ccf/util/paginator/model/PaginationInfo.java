package com.github.salilvnair.ccf.util.paginator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Salil V Nair
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationInfo {
    private Integer previousPage = -1;
    private Integer currentPage = -1;
    private Integer nextPage = -1;
    private Integer fromRow = -1;
    private Integer toRow = -1;
    private Integer pageSize = -1;
    private Integer totalPages = -1;
    private Integer totalRows = -1;
    private Boolean hasMoreResults;

    public boolean isHasMoreResults() {
        return hasMoreResults != null && hasMoreResults;
    }
}
