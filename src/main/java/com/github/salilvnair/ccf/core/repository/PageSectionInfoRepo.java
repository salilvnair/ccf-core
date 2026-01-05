package com.github.salilvnair.ccf.core.repository;

import com.github.salilvnair.ccf.core.entity.SectionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PageSectionInfoRepo extends JpaRepository<SectionInfo, Integer> {
    Optional<SectionInfo> findByPageIdAndSectionIdAndActive(int pageId, int sectionId, String active);

    @Query("SELECT t FROM #{#entityName} t WHERE t.sectionId IN :sectionIds and t.pageId IN :pageId and active='Y'")
    List<SectionInfo> findActiveSectionsByPageIdAndSectionIdsIn(@Param("pageId") Integer pageId, @Param("sectionIds") List<Integer> sectionIds);

    @Query("SELECT t FROM #{#entityName} t WHERE t.sectionId=:sectionId and t.pageId IN :pageId and active='Y'")
    List<SectionInfo> findActiveSectionByPageIdAndSectionId(@Param("pageId") Integer pageId, @Param("sectionId") Integer sectionId);

    List<SectionInfo> findByPageIdAndActive(Integer pageId, String active);
}
