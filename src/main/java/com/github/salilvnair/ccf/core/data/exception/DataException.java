package com.github.salilvnair.ccf.core.data.exception;

import com.github.salilvnair.ccf.core.component.context.ComponentContext;
import com.github.salilvnair.ccf.core.component.model.core.ComponentDataError;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataException extends Exception {
    private ComponentDataError componentDataError;
    private boolean hasApiErrorMessage = false;

    public DataException(){}
    public DataException(String message) {
        super(message);
    }

    public DataException(Throwable ex) {
        super(ex);
    }
    public DataException(String message, Throwable ex) {
        super(message, ex);
    }
    public DataException(ComponentDataError componentDataError) {
        super(componentDataError.getErrorInfo() + " " + componentDataError.errorDetailedDescription());
        this.componentDataError = componentDataError;
    }

    public DataException(ComponentDataError componentDataError, ComponentContext componentContext) {
        super(componentDataError.getErrorInfo() + " " + componentDataError.errorDetailedDescription());
        this.componentDataError = componentDataError;
        componentContext.setComponentDataError(componentDataError);
    }

    public DataException(ComponentDataError componentDataError, ComponentContext componentContext, Throwable ex) {
        super(componentDataError.getErrorInfo() + " " + componentDataError.errorDetailedDescription(), ex);
        this.componentDataError = componentDataError;
        componentContext.setComponentDataError(componentDataError);
    }

    public DataException(boolean hasApiErrorMessage, ComponentDataError componentDataError, ComponentContext componentContext, Throwable ex) {
        super(componentDataError.getErrorInfo() + " " + componentDataError.errorDetailedDescription(), ex);
        this.hasApiErrorMessage = hasApiErrorMessage;
        this.componentDataError = componentDataError;
        componentContext.setComponentDataError(componentDataError);
    }

    public DataException(ComponentDataError componentDataError, Throwable ex) {
        super(componentDataError.getErrorInfo() + " " + componentDataError.errorDetailedDescription(), ex);
        this.componentDataError = componentDataError;
    }

    public boolean hasApiErrorMessage() {
        return hasApiErrorMessage;
    }
}
