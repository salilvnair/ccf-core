package com.github.salilvnair.ccf.core.repository;

import com.github.salilvnair.ccf.core.entity.ContainerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContainerInfoRepo extends JpaRepository<ContainerInfo, Integer> {
    List<ContainerInfo> findBySectionIdAndActiveAndLoadByDefault(int sectionId, String active, String loadByDefault);

    List<ContainerInfo> findBySectionIdAndActive(int sectionId, String active);

    List<ContainerInfo> findByIdAndActive(int containerId, String active);

    List<ContainerInfo> findByIdInAndActive(List<Integer> containerIds, String active);
}
