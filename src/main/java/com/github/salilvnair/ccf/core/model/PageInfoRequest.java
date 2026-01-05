package com.github.salilvnair.ccf.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.github.salilvnair.ccf.util.paginator.model.ContainerPaginationInfo;
import com.github.salilvnair.ccf.util.paginator.model.PaginationInfo;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageInfoRequest {
	private String userId;
    private String loggedInUserId;
    private Integer pageId;
    private Integer sectionId;
    private Integer containerId;
    private List<Integer> sectionIds;
    private List<Integer> containerIds;
    private List<Integer> productIds;
    private Map<String, Object> inputParams;
    private Boolean filter;
    private Boolean sort;
    private Boolean paginate;
    private List<FilterParam> filterParams;
    private List<SortParam> sortParams;
    private Map<Integer, List<FilterParam>> containerFilterParams;
    private Map<Integer, List<SortParam>> containerSortParams;
    private Map<Integer, ContainerPaginationInfo> containerPaginationInfo;
    private PaginationInfo paginationInfo;
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean loadFilterValues = false;
    private Integer filterFieldId;
}
