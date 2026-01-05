package com.github.salilvnair.ccf.core.data.query.context;

import com.github.salilvnair.ccf.core.repository.ContainerFieldInfoRepo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QueryContext {
    private String queryString;
    private ContainerFieldInfoRepo containerFieldInfoRepo;
}
