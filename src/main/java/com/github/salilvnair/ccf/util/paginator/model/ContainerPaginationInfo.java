package com.github.salilvnair.ccf.util.paginator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContainerPaginationInfo {
    private Boolean paginate;
    private PaginationInfo paginationInfo;

    public boolean isPaginate() {
        return paginate != null && paginate;
    }
}
