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
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CCF_SECTION_INFO")
public class SectionInfo {
    @Id
    @Column(name = "SECTION_ID")
    private Integer sectionId;

    @Column(name = "PAGE_ID")
    private Integer pageId;

    @Column(name = "ACTIVE")
    private String active;

    @Column(name = "PRODUCT")
    private String productCommaSeperatedString;

    public List<Integer> productIds() {
        List<Integer> productIds = new ArrayList<>();
        if(productCommaSeperatedString != null) {
            productIds = Arrays.stream(productCommaSeperatedString.split(","))
                    .map(String::trim)
                    .filter(str -> !str.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }
        return productIds;
    }

}

