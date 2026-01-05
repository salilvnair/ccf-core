package com.github.salilvnair.ccf.core.component.exception;

import com.github.salilvnair.ccf.core.component.model.core.ComponentDataError;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentDataGeneratorException  extends Exception  {
    private ComponentDataError componentDataError;
    public ComponentDataGeneratorException(String message) {
        super(message);
    }

    public ComponentDataGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComponentDataGeneratorException(ComponentDataError componentDataError) {
        super(componentDataError.errorDetailedDescription());
        this.componentDataError = componentDataError;
    }

    public ComponentDataGeneratorException(ComponentDataError componentDataError, Throwable cause) {
        super(componentDataError.errorDetailedDescription(), cause);
        this.componentDataError = componentDataError;
    }

}
