package com.github.salilvnair.ccf.core.model;

import com.github.salilvnair.ccf.core.model.FilterOption;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilterValue {
    private Long fieldId;
    private String fieldDisplayName;
    private List<FilterOption> filterOptions;
}
