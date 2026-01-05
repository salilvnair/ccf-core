package com.github.salilvnair.ccf.core.data.handler.factory;

import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.handler.core.ContainerDataGenerator;
import com.github.salilvnair.ccf.core.data.handler.core.ContainerTableDataGenerator;
import com.github.salilvnair.ccf.core.data.handler.core.SectionDataGenerator;
import com.github.salilvnair.ccf.core.data.handler.core.TransformedContainerDataGenerator;
import com.github.salilvnair.ccf.core.data.handler.provider.transformer.CommonTransformedContainerDataGenerator;
import com.github.salilvnair.ccf.core.data.type.ContainerType;
import com.github.salilvnair.ccf.core.data.type.SectionType;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DataGeneratorFactory {
    private final ApplicationContext applicationContext;
    public DataGeneratorFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public SectionDataGenerator sectionDataGenerator(DataContext dataContext) {
        SectionType sectionType = dataContext.getSectionType();
        return (SectionDataGenerator) applicationContext.getBean(sectionType.value());
    }

    public ContainerDataGenerator containerDataGenerator(DataContext dataContext) {
        ContainerType containerType = dataContext.getContainerType();
        return (ContainerDataGenerator) applicationContext.getBean(containerType.value());
    }

    public ContainerDataGenerator containerDataGenerator(ContainerType containerType) {
        return (ContainerDataGenerator) applicationContext.getBean(containerType.value());
    }

    public ContainerTableDataGenerator containerTableDataGenerator(DataContext dataContext) {
        ContainerType containerType = dataContext.getContainerType();
        return containerTableDataGenerator(containerType);
    }

    public ContainerTableDataGenerator containerTableDataGenerator(ContainerType containerType) {
        return (ContainerTableDataGenerator) applicationContext.getBean(containerType.value());
    }

    public TransformedContainerDataGenerator transformedContainerDataGenerator(DataContext dataContext) {
        return (TransformedContainerDataGenerator)applicationContext.getBean(CommonTransformedContainerDataGenerator.BEAN_NAME);
    }


}
