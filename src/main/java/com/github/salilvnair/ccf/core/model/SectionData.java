package com.github.salilvnair.ccf.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.salilvnair.ccf.core.model.ContainerData;
import com.github.salilvnair.ccf.core.model.SectionMetaData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SectionData {
    private List<ContainerData> data;
    private SectionMetaData metaData;
}
