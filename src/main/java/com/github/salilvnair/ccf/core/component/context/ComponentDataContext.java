package com.github.salilvnair.ccf.core.component.context;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ComponentDataContext<T> {
    private List<T> content;
    public void addContent(T contentObj) {
        if(content == null) {
            content = new ArrayList<>();
        }
        content.add(contentObj);
    }
}
