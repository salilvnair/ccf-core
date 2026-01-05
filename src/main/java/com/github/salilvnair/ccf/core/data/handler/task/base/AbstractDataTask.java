package com.github.salilvnair.ccf.core.data.handler.task.base;

import com.github.salilvnair.ccf.core.model.ContainerData;
import com.github.salilvnair.ccf.core.component.generator.core.CommonComponentDataGenerator;
import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.context.DataTaskContext;
import com.github.salilvnair.ccf.core.data.exception.DataException;
import com.github.salilvnair.ccf.core.data.type.SectionType;
import com.github.salilvnair.ccf.core.entity.SectionInfo;
import com.github.salilvnair.ccf.core.repository.PageSectionInfoRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public abstract class AbstractDataTask implements DataTask {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected List<ContainerData> containerData;

    @Autowired
    private CommonComponentDataGenerator commonComponentDataGenerator;

    @Autowired
    private PageSectionInfoRepo sectionInfoRepo;


    protected DataContext generateContainerData(DataTaskContext dataTaskContext) throws Exception {
        DataContext dataContext =  new DataContext();
        dataContext.setSectionId(dataTaskContext.getDataContext().getSectionId());
        dataContext.setInputParams(dataTaskContext.getDataContext().getInputParams());
        Integer sectionId = dataTaskContext.getSectionId();
        dataContext.resetContainerIds();
        dataContext.setContainerId(dataTaskContext.getContainerId());
        dataContext.setContainerIds(dataTaskContext.getContainerIds());
        if(sectionId == null) {
            return dataContext;
        }
        SectionInfo sectionInfo = sectionInfoRepo.findById(sectionId).orElse(null);
        if(sectionInfo == null) {
            throw new DataException("Section with id "+ sectionId+ " not found!");
        }
        containerData = commonComponentDataGenerator.generateContainerData(SectionType.COMMON, dataContext, sectionInfo);
        return dataContext;
    }


}
