package com.github.salilvnair.ccf.core.component.model.core;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComponentDataInfo {
    private Integer statusCode;
    private String statusText;
}
