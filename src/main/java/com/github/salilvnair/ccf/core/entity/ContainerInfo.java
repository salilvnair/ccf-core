package com.github.salilvnair.ccf.core.entity;

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
@Table(name = "CCF_CONTAINER_INFO")
public class ContainerInfo {
    @Id
    @Column(name = "CONTAINER_ID")
    private Integer id;

    @Column(name = "PARENT_CONTAINER_ID")
    private Integer parentContainerId;

    @Column(name = "SECTION_ID")
    private Integer sectionId;

    @Column(name = "ACTIVE")
    private String active;

    @Column(name = "PRODUCT")
    private String productCommaSeperatedString;

    @Column(name = "LOAD_BY_DEFAULT")
    private String loadByDefault;

    @Column(name = "CONTAINER_TYPE")
    private String containerType;

    @Column(name = "CONTAINER_DISPLAY_NAME")
    private String containerDisplayName;

    @Column(name = "BEAN_NAME")
    private String beanName;

    @Column(name = "BEAN_METHOD_NAME")
    private String beanMethodName;

    @Column(name = "TBL_ROW_BEAN_NAME")
    private String tableRowBeanName;

    @Column(name = "TBL_ROW_BEAN_METHOD_NAME")
    private String tableRowBeanMethodName;

    @Column(name = "MAPPED_ROLES")
    private String commaSeperatedMappedRoleString;


    public List<Integer> productIds() {
        List<Integer> productIds = new ArrayList<>();
        if(productCommaSeperatedString != null) {
            productIds = Arrays.stream(productCommaSeperatedString.split(","))
                                                                    .map(String::trim)
                                                                    .map(Integer::parseInt)
                                                                    .collect(Collectors.toList());
        }
        return productIds;
    }

    public List<Integer> mappedRoles() {
        List<Integer> mappedRoles = new ArrayList<>();
        if(commaSeperatedMappedRoleString != null) {
            mappedRoles = Arrays.stream(commaSeperatedMappedRoleString.split(","))
                    .map(String::trim)
                    .filter(roleString -> !roleString.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }
        return mappedRoles;
    }
}

