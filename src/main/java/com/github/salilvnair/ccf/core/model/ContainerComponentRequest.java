package com.github.salilvnair.ccf.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.salilvnair.ccf.core.component.model.core.ComponentDataRequest;
import com.github.salilvnair.ccf.core.model.type.RequestType;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerComponentRequest {
    private List<PageInfoRequest> pageInfo;
    private List<RequestType> requestTypes;
    private ComponentDataRequest componentDataRequest;
}
