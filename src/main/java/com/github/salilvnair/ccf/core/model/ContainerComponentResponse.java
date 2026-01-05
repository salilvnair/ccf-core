package com.github.salilvnair.ccf.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.salilvnair.ccf.core.component.model.core.ComponentDataResponse;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContainerComponentResponse {
    public Map<Integer, PageDataResponse> pages;
    private ComponentDataResponse componentDataResponse;
}
