package com.github.salilvnair.ccf.core.data.context;

import com.github.salilvnair.ccf.core.entity.ContainerFieldInfo;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTaskContext {
    private DataContext dataContext;
    private List<Map<String,Object>> dbData;
    private ContainerFieldInfo containerFieldInfo;
    private Map<String, Object> rowData;
    private Integer sectionId;
    private Integer containerId;
    private List<Integer> containerIds;

    public ContainerFieldInfo containerFieldInfo() {
        return containerFieldInfo;
    }

    public DataContext dataContext() {
        if(dataContext == null) {
            dataContext = new DataContext();
        }
        return dataContext;
    }
}
