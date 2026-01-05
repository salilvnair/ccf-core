package com.github.salilvnair.ccf.core.component.model.core;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComponentDataError {
    private String errorId;
    private String errorInfo;
    private String errorDetailedDescription;


    public String errorDetailedDescription() {
        return errorDetailedDescription == null ? "" : errorDetailedDescription;
    }

    public String errorInfo() {
        return errorInfo == null ? "" : errorInfo;
    }
}
