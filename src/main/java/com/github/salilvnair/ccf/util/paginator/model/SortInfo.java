package com.github.salilvnair.ccf.util.paginator.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Salil V Nair
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SortInfo {
    private String by;
    private boolean desc;

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public boolean isDesc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }
}
