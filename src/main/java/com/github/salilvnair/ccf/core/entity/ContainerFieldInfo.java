package com.github.salilvnair.ccf.core.entity;

import com.github.salilvnair.ccf.core.data.context.DataContext;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CCF_CONTAINER_FIELD_INFO")
public class ContainerFieldInfo {
    @Id
    @Column(name = "CONTAINER_FIELD_ID")
    private Long id;

    @Column(name = "GROUP_ID")
    private Integer groupId;

    @Column(name = "CONTAINER_ID")
    private Integer containerId;

    @Column(name = "ACTIVE")
    private String active;

    @Column(name = "SUB_CONTAINER_ID")
    private Integer subContainerId;

    @Column(name = "FIELD_TYPE_ID")
    private Integer fieldTypeId;

    @Column(name = "FIELD_LENGTH")
    private Integer fieldLength;

    @Column(name = "FIELD_MIN_LENGTH")
    private Integer fieldMinLength;

    @Column(name = "FIELD_MAX_LENGTH")
    private Integer fieldMaxLength;

    @Column(name = "FIELD_TYPE")
    private String fieldType;

    @Column(name = "FIELD_FORMAT")
    private String fieldFormat;

    @Column(name = "FIELD_DISPLAY_NAME")
    private String fieldDisplayName;

    @Column(name = "MAPPED_COLUMN_NAME")
    private String mappedColumnName;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder;

    @Column(name = "FILTERABLE")
    private String filterable;

    @Column(name = "SORTABLE")
    private String sortable;

    @Column(name = "ENABLED")
    private String enabled;

    @Column(name = "VISIBLE")
    private String visible;

    @Column(name = "EDITABLE")
    private String editable;

    @Column(name = "REQUIRED")
    private String required;

    @Column(name = "BEAN_NAME")
    private String beanName;

    @Column(name = "BEAN_METHOD_NAME")
    private String beanMethodName;

    @Column(name = "PRODUCT")
    private String productCommaSeparatedString;

    @Column(name = "MAPPED_ROLES")
    private String commaSeperatedMappedRoleString;


    public List<String> products() {
        List<String> products = new ArrayList<>();
        if(productCommaSeparatedString != null) {
            products = Arrays.asList(productCommaSeparatedString.split(","));
        }
        return products;
    }

    public List<Long> mappedRoles() {
        List<Long> mappedRoles = new ArrayList<>();
        if(commaSeperatedMappedRoleString != null) {
            mappedRoles = Arrays.stream(commaSeperatedMappedRoleString.split(","))
                    .map(String::trim)
                    .filter(roleString -> !roleString.isEmpty())
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }
        return mappedRoles;
    }


    public String mappedColumnAsLower() {
        return "LOWER("+mappedColumnName+")";
    }

    public String mappedColumnAsUpper() {
        return "UPPER("+mappedColumnName+")";
    }

    public boolean eligible(DataContext dataContext) {
        return true;
    }
}

