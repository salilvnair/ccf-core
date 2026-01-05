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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CCF_CONTAINER_QUERY_INFO")
public class ContainerQueryInfo {
    @Id
    @Column(name = "CONTAINER_QUERY_ID")
    private Integer id;

    @Column(name = "CONTAINER_ID")
    private Integer containerId;

    @Column(name = "QUERY_STRING")
    private String queryString;

    @Column(name = "PAGINATION_QUERY_STRING")
    private String paginationQueryString;

    @Column(name = "COUNT_QUERY_STRING")
    private String countQueryString;

    @Column(name = "QUERY_PARAMS")
    private String queryParamsCommaSeparatedString;


    public List<String> queryParamKeys() {
        List<String> queryParamKeys = new ArrayList<>();
        if(queryParamsCommaSeparatedString != null) {
            queryParamKeys = Arrays.asList(queryParamsCommaSeparatedString.split(","));
        }
        return queryParamKeys;
    }
}

