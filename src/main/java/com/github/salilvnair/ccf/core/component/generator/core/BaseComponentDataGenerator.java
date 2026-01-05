package com.github.salilvnair.ccf.core.component.generator.core;

import com.github.salilvnair.ccf.core.component.context.ComponentContext;
import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.exception.DataException;
import com.github.salilvnair.ccf.core.data.handler.core.SectionDataGenerator;
import com.github.salilvnair.ccf.core.data.handler.factory.DataGeneratorFactory;
import com.github.salilvnair.ccf.core.data.type.SectionType;
import com.github.salilvnair.ccf.core.entity.PageCommonQueryInfo;
import com.github.salilvnair.ccf.core.entity.SectionInfo;
import com.github.salilvnair.ccf.core.model.ContainerData;
import com.github.salilvnair.ccf.core.repository.PageCommonQueryRepo;
import com.github.salilvnair.ccf.core.repository.PageSectionInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public abstract class BaseComponentDataGenerator extends AbstractComponentDataGenerator {

    @Autowired
    protected PageSectionInfoRepo sectionInfoRepo;

    @Autowired
    protected PageCommonQueryRepo pageCommonQueryRepo;

    @Autowired
    protected DataGeneratorFactory dataGeneratorFactory;

    protected List<ContainerData> generateContainerData(Integer sectionId, ComponentContext componentContext, Map<String, Object> inputParams) throws DataException {
        SectionInfo sectionInfo = sectionInfoRepo.findById(sectionId).orElse(null);
        if(sectionInfo == null) {
            throw new DataException("Section with id "+ sectionId+ " not found!");
        }
        return generateContainerData(SectionType.COMMON, componentContext.getDataContext(), sectionInfo);
    }


    public List<ContainerData> generateContainerData(SectionType sectionType, DataContext dataContext, SectionInfo sectionInfo) throws DataException {
        List<ContainerData> containerData = new ArrayList<>();
        if(sectionInfo != null) {
            dataContext.setSectionType(sectionType);
            dataContext.setSectionId(sectionInfo.getSectionId());
            dataContext.setSectionInfo(sectionInfo);
            List<PageCommonQueryInfo> pageCommonQueryInfos = pageCommonQueryRepo.findByPageId(sectionInfo.getPageId());
            dataContext.setPageCommonQueryInfos(pageCommonQueryInfos);
            SectionDataGenerator sectionDataGenerator = dataGeneratorFactory.sectionDataGenerator(dataContext);
            containerData = sectionDataGenerator.generate(dataContext);
        }
        return containerData;
    }
}
