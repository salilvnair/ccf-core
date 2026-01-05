package com.github.salilvnair.ccf.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.salilvnair.ccf.core.model.SectionData;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageDataResponse {
    private Integer pageId;
    private Map<Integer, SectionData> sections;
}
