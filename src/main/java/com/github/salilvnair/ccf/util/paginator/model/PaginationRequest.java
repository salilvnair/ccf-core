package com.github.salilvnair.ccf.util.paginator.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Salil V Nair
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class PaginationRequest<E> {
    private Integer currentPage = -1;
    private Integer fromRow = -1;
    private Integer toRow = -1;
    private Integer pageSize = -1;
    private Integer totalRows = -1;

    public PaginationRequest(PaginationInfo paginationInfo) {
        this.currentPage = paginationInfo.getCurrentPage();
        this.pageSize = paginationInfo.getPageSize();
        this.fromRow = paginationInfo.getFromRow();
        this.toRow = paginationInfo.getToRow();
        this.totalRows = paginationInfo.getTotalRows();
    }
}
