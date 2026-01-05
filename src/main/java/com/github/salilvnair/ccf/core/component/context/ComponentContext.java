package com.github.salilvnair.ccf.core.component.context;

import com.github.salilvnair.ccf.core.component.collector.type.ComponentCollectorType;
import com.github.salilvnair.ccf.core.component.generator.type.ComponentGeneratorType;
import com.github.salilvnair.ccf.core.component.model.core.ComponentDataError;
import com.github.salilvnair.ccf.core.component.model.core.ComponentDataRequest;
import com.github.salilvnair.ccf.core.component.model.core.ComponentInfo;
import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentContext {
    public static final String INSTANCE = "ComponentContext_INSTANCE";
    private ComponentDataRequest componentRequest;
    private Map<String, Object> requestInputParams;
    private Map<String, Object> componentInputParams;
    private ComponentInfo componentInfo;
    private String userId;
    private DataContext dataContext;
    private ComponentCollectorType componentCollectorType;
    private ComponentGeneratorType componentGeneratorType;
    @Getter(AccessLevel.NONE)
    private ComponentDataError componentDataError;
    private Long requestId;

    public DataContext dataContext() {
        if(dataContext == null) {
            dataContext = new DataContext();
        }
        return dataContext;
    }
    public ComponentDataError componentDataError() {
        ComponentDataError dataError = null;
        try {
            String dataErrorString = new ObjectMapper().writeValueAsString(this.componentDataError);
            dataError = new ObjectMapper().readValue(dataErrorString, ComponentDataError.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.componentDataError = null;
        return dataError;
    }

    public boolean hasComponentDataError() {
        return this.componentDataError != null;
    }


}
