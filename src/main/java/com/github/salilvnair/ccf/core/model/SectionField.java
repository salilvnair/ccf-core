package com.github.salilvnair.ccf.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SectionField {
    private Integer containerId;
    private String containerDisplayName;
    private String containerType;
    private Integer sectionId;
    private Long fieldId;
    private Integer groupId;
    private Integer fieldTypeId;
    private Integer fieldLength;
    private Integer fieldMinLength;
    private Integer fieldMaxLength;
    private String fieldType;
    private String fieldDisplayName;
    private Object fieldValue;
    private List<List<SectionField>> tableData;
    private List<SectionField> data;
    private List<SectionField> tableHeaders;
    private List<Object> fieldValues;
    private Integer displayOrder;
    private boolean sortable;
    private boolean filterable;
    private boolean enabled;
    private boolean visible;
    private boolean editable;
    private boolean required;
    private boolean fieldValueChanged;
}
