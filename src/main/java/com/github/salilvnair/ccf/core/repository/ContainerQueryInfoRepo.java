package com.github.salilvnair.ccf.core.repository;

import com.github.salilvnair.ccf.core.entity.ContainerQueryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContainerQueryInfoRepo extends JpaRepository<ContainerQueryInfo, Integer> {
    Optional<ContainerQueryInfo> findByContainerId(int containerId);
}
