package com.github.salilvnair.ccf.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.salilvnair.ccf.util.paginator.model.PaginationInfo;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContainerData {
    private Integer containerId;
    private Integer sectionId;
    private String containerDisplayName;
    private String containerType;
    private PaginationInfo paginationInfo;
    private List<SectionField> data;
    private Map<String, Object> rawData;
    private List<SectionField> tableHeaders;
    private Map<Long, Set<String>> filterValues;
    private Map<Long, FilterValue> dtFilterValues;
    private List<List<SectionField>> tableData;
    private List<Map<String, Object>> rawTableData;
    private Integer tableRowCount;
    private ContainerMetaData metaData;
}
