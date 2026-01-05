package com.github.salilvnair.ccf.core.component.exception;

import com.github.salilvnair.ccf.core.component.model.core.ComponentDataError;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ComponentDataCollectorException extends Exception {
    private ComponentDataError componentDataError;
    public ComponentDataCollectorException(String message) {
        super(message);
    }

    public ComponentDataCollectorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComponentDataCollectorException(ComponentDataError componentDataError) {
        super(componentDataError.errorDetailedDescription());
        this.componentDataError = componentDataError;
    }

    public ComponentDataCollectorException(ComponentDataError componentDataError, Throwable cause) {
        super(componentDataError.errorDetailedDescription(), cause);
        this.componentDataError = componentDataError;
    }
}
