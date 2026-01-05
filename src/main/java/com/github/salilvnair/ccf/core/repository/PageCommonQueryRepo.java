package com.github.salilvnair.ccf.core.repository;

import com.github.salilvnair.ccf.core.entity.PageCommonQueryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageCommonQueryRepo extends JpaRepository<PageCommonQueryInfo, Integer> {
    List<PageCommonQueryInfo> findByPageId(int pageId);
}
