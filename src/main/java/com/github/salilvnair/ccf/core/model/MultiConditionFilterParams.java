package com.github.salilvnair.ccf.core.model;

import com.github.salilvnair.ccf.core.model.FilterParam;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MultiConditionFilterParams {
    private String condition;
    private List<FilterParam> filterParams;
}
