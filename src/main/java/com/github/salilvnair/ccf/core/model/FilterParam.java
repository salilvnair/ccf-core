package com.github.salilvnair.ccf.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilterParam {
    private Integer fieldId;
    private String fieldValue;
    private List<String> fieldValues;
    private Boolean fieldValueNull;
    private Boolean likeMatch;

    public boolean isFieldValueNull() {
        return fieldValueNull != null && fieldValueNull;
    }

    public boolean isLikeMatch() {
        return likeMatch != null && likeMatch;
    }
}
