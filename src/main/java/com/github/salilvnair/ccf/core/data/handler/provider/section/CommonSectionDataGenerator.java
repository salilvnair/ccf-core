package com.github.salilvnair.ccf.core.data.handler.provider.section;

import com.github.salilvnair.ccf.core.constant.InputParamsKey;
import com.github.salilvnair.ccf.core.constant.StringConstant;
import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.context.DataTaskContext;
import com.github.salilvnair.ccf.core.data.exception.DataException;
import com.github.salilvnair.ccf.core.data.handler.core.ContainerDataGenerator;
import com.github.salilvnair.ccf.core.data.handler.core.ContainerTableDataGenerator;
import com.github.salilvnair.ccf.core.data.handler.core.ContainerTableRawDataGenerator;
import com.github.salilvnair.ccf.core.data.handler.core.SectionDataGenerator;
import com.github.salilvnair.ccf.core.data.handler.factory.DataGeneratorFactory;
import com.github.salilvnair.ccf.core.data.type.ContainerType;
import com.github.salilvnair.ccf.core.data.type.SectionType;
import com.github.salilvnair.ccf.core.entity.ContainerInfo;
import com.github.salilvnair.ccf.core.entity.ContainerQueryInfo;
import com.github.salilvnair.ccf.core.entity.SectionInfo;
import com.github.salilvnair.ccf.core.model.ContainerData;
import com.github.salilvnair.ccf.core.model.ContainerMetaData;
import com.github.salilvnair.ccf.core.model.SectionField;
import com.github.salilvnair.ccf.core.repository.ContainerInfoRepo;
import com.github.salilvnair.ccf.core.repository.ContainerQueryInfoRepo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(value = SectionType.Value.COMMON)
public class CommonSectionDataGenerator implements SectionDataGenerator {

    private final DataGeneratorFactory dataGeneratorFactory;

    private final ContainerInfoRepo containerInfoRepo;

    private final ContainerQueryInfoRepo containerQueryInfoRepo;


    public CommonSectionDataGenerator(DataGeneratorFactory dataGeneratorFactory, ContainerInfoRepo containerInfoRepo,
                                      ContainerQueryInfoRepo containerQueryInfoRepo) {
        this.dataGeneratorFactory = dataGeneratorFactory;
        this.containerInfoRepo = containerInfoRepo;
        this.containerQueryInfoRepo = containerQueryInfoRepo;
    }

    @Override
    public List<ContainerData> generate(DataContext dataContext) throws DataException {
        List<ContainerData> containerDataList = new ArrayList<>();
        SectionInfo pageSectionFieldInfo = dataContext.getSectionInfo();
        List<Integer> containerIds = dataContext.getContainerIds();
        List<ContainerInfo> containerInfoList = CollectionUtils.isEmpty(containerIds) ?
                                                    dataContext.getContainerId() != null ? containerInfoRepo.findByIdAndActive(dataContext.getContainerId(), StringConstant.Y)
                                                            : dataContext.isForceLoadAllActiveContainersBySectionId() ? containerInfoRepo.findBySectionIdAndActive(pageSectionFieldInfo.getSectionId(), StringConstant.Y) : containerInfoRepo.findBySectionIdAndActiveAndLoadByDefault(pageSectionFieldInfo.getSectionId(), StringConstant.Y, dataContext.getLoadPageContainerByDefault()): containerInfoRepo.findByIdInAndActive(containerIds, StringConstant.Y);
        dataContext.setDataGeneratorFactory(dataGeneratorFactory);
        dataContext.setContainerInfoRepo(containerInfoRepo);
        dataContext.setContainerQueryInfoRepo(containerQueryInfoRepo);
        if(!CollectionUtils.isEmpty(containerInfoList)) {
            containerInfoList = filterSectionInfoIfApplicable(containerInfoList, dataContext);
            if(!CollectionUtils.isEmpty(containerInfoList)) {
                executeSectionDataTasks(dataContext);
            }
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
                        if(containerType.isRawData()) {
                            resolveRawTableSectionData(dataContext, containerData, containerInfo);
                        }
                        else {
                            ContainerTableDataGenerator containerTableDataGenerator = dataGeneratorFactory.containerTableDataGenerator(dataContext);
                            List<List<SectionField>> tableData = containerTableDataGenerator.generate(dataContext);
                            List<SectionField> headers = containerTableDataGenerator.generateHeaders(dataContext, containerInfo);
                            containerData.setTableData(tableData);
                            containerData.setPaginationInfo(dataContext.paginationInfo(dataContext));
                            containerData.setTableHeaders(headers);
                            containerData.setFilterValues(containerTableDataGenerator.generateFilterValues(tableData));
                            containerData.setDtFilterValues(containerTableDataGenerator.generateDtFilterValues(tableData));
                        }
                    }
                    else {
                        ContainerDataGenerator containerDataGenerator = dataGeneratorFactory.containerDataGenerator(dataContext);
                        List<SectionField> data = containerDataGenerator.generate(dataContext);
                        containerData.setData(data);
                    }
                    containerData.setMetaData(generateContainerMetaData(dataContext, containerData, containerInfo));
                    containerDataList.add(containerData);
                }
            }
        }
        return containerDataList;
    }

    private void resolveRawTableSectionData(DataContext dataContext, ContainerData containerData, ContainerInfo containerInfo) throws DataException {
        ContainerTableRawDataGenerator containerTableDataGenerator = dataGeneratorFactory.containerTableRawDataGenerator(dataContext);
        List<Map<String, Object>> tableData = containerTableDataGenerator.generate(dataContext);
        containerData.setRawTableData(tableData);
    }

    private void executeSectionDataTasks(DataContext dataContext) {
        SectionInfo sectionInfo = dataContext.getSectionInfo();
        DataTaskContext dataTaskContext = DataTaskContext
                                            .builder()
                                            .dataContext(dataContext)
                                            .build();
        dataContext.getDataTaskExecutor().execute(sectionInfo.getBeanName(), sectionInfo.getBeanMethodName(), dataTaskContext);
    }

    private List<ContainerInfo> filterSectionInfoIfApplicable(List<ContainerInfo> containerInfoList, DataContext dataContext) {
        containerInfoList = filterSectionInfoByProductIdsIfApplicable(containerInfoList, dataContext);
        containerInfoList = filterSectionInfoByMappedRolesIfApplicable(containerInfoList, dataContext);
        return containerInfoList;
    }

    private List<ContainerInfo> filterSectionInfoByProductIdsIfApplicable(List<ContainerInfo> containerInfoList, DataContext dataContext) {
        if(!CollectionUtils.isEmpty(dataContext.getProductIds())) {
            containerInfoList = containerInfoList.stream().filter(containerInfo -> containerInfo.productIds().stream().anyMatch(dataContext.getProductIds()::contains)).collect(Collectors.toList());
        }
        return containerInfoList;
    }

    private List<ContainerInfo> filterSectionInfoByMappedRolesIfApplicable(List<ContainerInfo> containerInfoList, DataContext dataContext) {
        if(!CollectionUtils.isEmpty(dataContext.getMappedRoles())) {
            containerInfoList = containerInfoList.stream().filter(containerInfo -> containerInfo.mappedRoles() == null || containerInfo.mappedRoles().stream().anyMatch(dataContext.getMappedRoles()::contains)).collect(Collectors.toList());
        }
        return containerInfoList;
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
