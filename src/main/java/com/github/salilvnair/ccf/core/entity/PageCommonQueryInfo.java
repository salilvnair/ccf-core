package com.github.salilvnair.ccf.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CCF_PAGE_COMMON_QUERY")
public class PageCommonQueryInfo {
    @Id
    @Column(name = "PAGE_COMMON_QUERY_ID")
    private Integer id;

    @Column(name = "PAGE_ID")
    private Integer pageId;

    @Column(name = "QUERY_STRING")
    private String queryString;

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

