package com.github.salilvnair.ccf.core.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortParam {
    private Integer fieldId;
    private Boolean desc;

    public boolean isDesc() {
        return desc != null && desc;
    }
}
