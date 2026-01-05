package com.github.salilvnair.ccf.core.data.handler.provider.transformer;

import com.github.salilvnair.ccf.core.constant.StringConstant;
import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.handler.provider.container.AbstractTransformedContainerGenerator;
import com.github.salilvnair.ccf.core.entity.ContainerInfo;
import com.github.salilvnair.ccf.core.entity.ContainerQueryInfo;
import com.github.salilvnair.ccf.core.repository.ContainerInfoRepo;
import com.github.salilvnair.ccf.core.repository.ContainerQueryInfoRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

@Component(CommonTransformedContainerDataGenerator.BEAN_NAME)
public class CommonTransformedContainerDataGenerator extends AbstractTransformedContainerGenerator {
    public static final String BEAN_NAME = "commonTransformedContainerDataGenerator";
    private static final Logger logger = LoggerFactory.getLogger(CommonTransformedContainerDataGenerator.class);

    private final ContainerQueryInfoRepo containerQueryInfoRepo;
    private final ContainerInfoRepo containerInfoRepo;

    public CommonTransformedContainerDataGenerator(ContainerQueryInfoRepo containerQueryInfoRepo, ContainerInfoRepo containerInfoRepo) {
        this.containerQueryInfoRepo = containerQueryInfoRepo;
        this.containerInfoRepo = containerInfoRepo;
    }

    @Override
    public <T> List<T> generate(DataContext dataContext, Class<T> clazz) {
        List<ContainerInfo> containerInfoList = containerInfoRepo.findByIdAndActive(dataContext.getContainerId(), StringConstant.Y);
        ContainerQueryInfo containerQueryInfo = containerQueryInfoRepo.findByContainerId(dataContext.getContainerId()).orElse(null);
        dataContext.setContainerInfo(containerInfoList.get(0));
        dataContext.setContainerQueryInfo(containerQueryInfo);
        return fetchTransformedResultList(dataContext, clazz);
    }
}
