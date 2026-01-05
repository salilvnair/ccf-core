package com.github.salilvnair.ccf.core.data.handler.provider.section;

import com.github.salilvnair.ccf.core.constant.InputParamsKey;
import com.github.salilvnair.ccf.core.constant.StringConstant;
import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.exception.DataException;
import com.github.salilvnair.ccf.core.data.handler.core.ContainerTableDataGenerator;
import com.github.salilvnair.ccf.core.data.handler.core.SectionDataGenerator;
import com.github.salilvnair.ccf.core.data.handler.factory.DataGeneratorFactory;
import com.github.salilvnair.ccf.core.data.type.ContainerType;
import com.github.salilvnair.ccf.core.data.type.SectionType;
import com.github.salilvnair.ccf.core.entity.ContainerInfo;
import com.github.salilvnair.ccf.core.entity.ContainerQueryInfo;
import com.github.salilvnair.ccf.core.entity.SectionInfo;
import com.github.salilvnair.ccf.core.model.ContainerData;
import com.github.salilvnair.ccf.core.model.ContainerMetaData;
import com.github.salilvnair.ccf.core.repository.ContainerInfoRepo;
import com.github.salilvnair.ccf.core.repository.ContainerQueryInfoRepo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service(value = SectionType.Value.DATA_TABLE_ROW_COUNT)
public class TableRowCountSectionDataGenerator implements SectionDataGenerator {

    private final DataGeneratorFactory dataGeneratorFactory;

    private final ContainerInfoRepo containerInfoRepo;

    private final ContainerQueryInfoRepo containerQueryInfoRepo;


    public TableRowCountSectionDataGenerator(DataGeneratorFactory dataGeneratorFactory, ContainerInfoRepo containerInfoRepo,
                                             ContainerQueryInfoRepo containerQueryInfoRepo) {
        this.dataGeneratorFactory = dataGeneratorFactory;
        this.containerInfoRepo = containerInfoRepo;
        this.containerQueryInfoRepo = containerQueryInfoRepo;
    }

    @Override
    public List<ContainerData> generate(DataContext dataContext) throws DataException {
        List<ContainerData> containerDataList = new ArrayList<>();
        SectionInfo pageSectionFieldInfo = dataContext.getSectionInfo();
        List<ContainerInfo> containerInfoList = dataContext.getContainerId() != null ? containerInfoRepo.findByIdAndActive(dataContext.getContainerId(), StringConstant.Y) : containerInfoRepo.findBySectionIdAndActive(pageSectionFieldInfo.getSectionId(), StringConstant.Y);
        dataContext.setDataGeneratorFactory(dataGeneratorFactory);
        dataContext.setContainerInfoRepo(containerInfoRepo);
        dataContext.setContainerQueryInfoRepo(containerQueryInfoRepo);
        if(!CollectionUtils.isEmpty(containerInfoList)) {
            for (ContainerInfo containerInfo : containerInfoList) {
                ContainerData containerData = new ContainerData();
                if(containerInfo != null) {
                    Integer containerId = containerInfo.getId();
                    ContainerQueryInfo containerQueryInfo = containerQueryInfoRepo.findByContainerId(containerId).orElse(null);
                    containerData.setContainerId(containerId);
                    containerData.setSectionId(containerInfo.getSectionId());
                    ContainerType containerType = ContainerType.type(containerInfo.getContainerType());
                    containerData.setContainerType(containerType.value());
                    containerData.setContainerDisplayName(containerInfo.getContainerDisplayName());
                    dataContext.setContainerType(containerType);
                    dataContext.setContainerInfo(containerInfo);
                    dataContext.setContainerQueryInfo(containerQueryInfo);

                    if(containerType.isTable()) {
                        ContainerTableDataGenerator containerTableDataGenerator = dataGeneratorFactory.containerTableDataGenerator(dataContext);
                        containerData.setTableRowCount(containerTableDataGenerator.totalRows(dataContext));
                    }
                    containerData.setMetaData(generateContainerMetaData(dataContext, containerData, containerInfo));
                    containerDataList.add(containerData);
                }
            }
        }
        return containerDataList;
    }

    private ContainerMetaData generateContainerMetaData(DataContext dataContext, ContainerData containerData, ContainerInfo containerInfo) {
        Map<String, Object> inputParams = dataContext.getInputParams();
        ContainerMetaData.ContainerMetaDataBuilder containerMetaDataBuilder = ContainerMetaData.builder().containerId(containerInfo.getId());
        if(!CollectionUtils.isEmpty(inputParams) && inputParams.containsKey(InputParamsKey.SHOW_CONTAINER_QUERY_DATA)) {
            containerMetaDataBuilder.containerQueryData(dataContext.getContainerQueryData());
        }
        return containerMetaDataBuilder.build();
    }
}
