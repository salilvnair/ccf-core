package com.github.salilvnair.ccf.core.repository;

import com.github.salilvnair.ccf.core.entity.ContainerFieldInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("containerFieldInfoRepo")
public interface ContainerFieldInfoRepo extends JpaRepository<ContainerFieldInfo, Integer> {
    List<ContainerFieldInfo> findByContainerIdAndActiveOrderByDisplayOrder(Integer containerId, String active);

    List<ContainerFieldInfo> findByIdAndActive(Integer fieldId, String active);

    List<ContainerFieldInfo> findByIdInAndActive(List<Long> fieldIds, String active);
    List<ContainerFieldInfo> findByIdInAndContainerIdInAndActive(List<Integer> fieldIds, List<Integer> containerIds, String active);
}
