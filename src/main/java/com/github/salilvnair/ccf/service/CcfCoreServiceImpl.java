package com.github.salilvnair.ccf.service;

import com.github.salilvnair.ccf.core.component.constant.ComponentInputParamsKey;
import com.github.salilvnair.ccf.core.component.context.ComponentContext;
import com.github.salilvnair.ccf.core.component.helper.ComponentHelper;
import com.github.salilvnair.ccf.core.component.model.core.ComponentDataRequest;
import com.github.salilvnair.ccf.core.component.model.core.ComponentDataResponse;
import com.github.salilvnair.ccf.core.component.service.core.ComponentService;
import com.github.salilvnair.ccf.core.constant.StringConstant;
import com.github.salilvnair.ccf.core.dao.QueryService;
import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.exception.DataException;
import com.github.salilvnair.ccf.core.data.handler.core.SectionDataGenerator;
import com.github.salilvnair.ccf.core.data.handler.factory.DataGeneratorFactory;
import com.github.salilvnair.ccf.core.data.handler.task.base.DataTaskExecutor;
import com.github.salilvnair.ccf.core.data.type.SectionType;
import com.github.salilvnair.ccf.core.entity.PageCommonQueryInfo;
import com.github.salilvnair.ccf.core.entity.SectionInfo;
import com.github.salilvnair.ccf.core.model.*;
import com.github.salilvnair.ccf.core.model.type.RequestType;
import com.github.salilvnair.ccf.core.repository.PageCommonQueryRepo;
import com.github.salilvnair.ccf.core.repository.PageSectionInfoRepo;
import com.github.salilvnair.ccf.util.log.StackTraceFrame;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CcfCoreServiceImpl extends QueryService implements CcfCoreService {

    @Autowired
    private PageCommonQueryRepo pageCommonQueryRepo;

    @Autowired
    private PageSectionInfoRepo pageSectionInfoRepo;

    @Autowired
    private DataGeneratorFactory dataGeneratorFactory;

    @Autowired
    private DataTaskExecutor dataTaskExecutor;

    @Autowired
    private ComponentService componentService;


    protected Logger logger = StackTraceFrame.initLogger(CcfCoreServiceImpl.class);

    @Override
    public ContainerComponentResponse execute(ContainerComponentRequest request) {
        ContainerComponentResponse containerComponentResponse = new ContainerComponentResponse();
        if(CollectionUtils.isEmpty(request.getRequestTypes())) {
            return containerComponentResponse;
        }
        if(request.getRequestTypes().contains(RequestType.COMPONENT)) {
            containerComponentResponse = executeComponentRequest(request, containerComponentResponse);
        }
        if(request.getRequestTypes().contains(RequestType.CONTAINER)) {
            containerComponentResponse = executeContainerRequest(request, containerComponentResponse);
        }
        return containerComponentResponse;
    }


    public ContainerComponentResponse executeContainerRequest(ContainerComponentRequest request, ContainerComponentResponse irdResponse) {
        Map<Integer, PageDataResponse> pages = new HashMap<>();
        List<PageInfoRequest> pageInfos = request.getPageInfo();
        try {
            for(PageInfoRequest pageInfoRequest: pageInfos) {
                PageDataResponse page = new PageDataResponse();
                Integer pageId = pageInfoRequest.getPageId();
                page.setPageId(pageId);
                DataContext dataContext = initDataContext(request, pageInfoRequest);
                Map<Integer, SectionData> sectionIdKeyedSectionData = new HashMap<>();
                List<Integer> sectionIds = pageInfoRequest.getSectionIds();
                List<SectionInfo> sectionInfos = pageInfoRequest.getSectionId()!=null ?
                        pageSectionInfoRepo.findActiveSectionByPageIdAndSectionId(pageId, pageInfoRequest.getSectionId()) : CollectionUtils.isEmpty(sectionIds)
                        ? pageSectionInfoRepo.findByPageIdAndActive(pageId, StringConstant.Y) : pageSectionInfoRepo.findActiveSectionsByPageIdAndSectionIdsIn(pageId, sectionIds);
                for(SectionInfo sectionInfo : sectionInfos) {
                    SectionData sectionData = new SectionData();
                    List<ContainerData> containerData = generate(dataContext, pageInfoRequest, sectionInfo);
                    SectionMetaData sectionMetaData = generateSectionMetaData(dataContext, pageInfoRequest, sectionInfo);
                    sectionData.setData(containerData);
                    sectionData.setMetaData(sectionMetaData);
                    sectionIdKeyedSectionData.put(sectionInfo.getSectionId(), sectionData);
                }
                page.setSections(sectionIdKeyedSectionData);
                pages.put(pageId, page);
            }
        }
        catch (Exception ex) {
            StackTraceFrame stackTraceFrame = new StackTraceFrame(ex, this.getClass());
            stackTraceFrame.printStackTrace();
        }
        irdResponse.setPages(pages);
        return irdResponse;
    }

    public ContainerComponentResponse executeComponentRequest(ContainerComponentRequest request, ContainerComponentResponse irdResponse) {
        ComponentContext componentContext = initComponentContext(request.getComponentDataRequest(), request);
        ComponentDataResponse componentDataResponse = (ComponentDataResponse) componentService.execute(request.getComponentDataRequest(), componentContext);
        irdResponse.setComponentDataResponse(componentDataResponse);
        return irdResponse;
    }

    private ComponentContext initComponentContext(ComponentDataRequest componentDataRequest, ContainerComponentRequest request) {
        ComponentContext componentContext = new ComponentContext();
      //  PageInfoRequest pageInfoRequest = request.getPageInfo().g;
        DataContext dataContext = initDataContext(request);
        componentContext.setDataContext(dataContext);
        return componentContext;
    }

    private DataContext initDataContext(ContainerComponentRequest request) {
        DataContext dataContext = new DataContext();
        if (!CollectionUtils.isEmpty(request.getPageInfo())) {
            List<PageInfoRequest> pageInfos = request.getPageInfo();

                for(PageInfoRequest pageInfoRequest: pageInfos) {
                    dataContext =  initDataContextFromPageInfoRequest(request,pageInfoRequest);
                }

        }
        ComponentDataRequest componentDataRequest = request.getComponentDataRequest();
        if(componentDataRequest != null) {
            Map<String, Object> requestInputParams = componentDataRequest.getInputParams();
            dataContext.setUserId(ComponentHelper.extract(ComponentInputParamsKey.USER_ID, requestInputParams));
            dataContext.setLoggedInUserId(ComponentHelper.extract(ComponentInputParamsKey.LOGGED_IN_USER_ID, requestInputParams));
        }
        dataContext.setDataTaskExecutor(dataTaskExecutor);
        return dataContext;
    }



    private DataContext initDataContext(ContainerComponentRequest request, PageInfoRequest pageInfoRequest) {
        DataContext dataContext = initDataContextFromPageInfoRequest(request, pageInfoRequest);
        dataContext.setDataTaskExecutor(dataTaskExecutor);
        return dataContext;
    }

    private SectionMetaData generateSectionMetaData(DataContext dataContext, PageInfoRequest pageInfoRequest, SectionInfo sectionInfo) {
        return SectionMetaData
                .builder()
                .sectionId(sectionInfo.getSectionId())
                .build();
    }

    private DataContext initDataContextFromPageInfoRequest(ContainerComponentRequest irdRequest, PageInfoRequest pageInfoRequest) {
        DataContext dataContext = new DataContext();
        if(pageInfoRequest!=null) {
            dataContext.setUserId(pageInfoRequest.getUserId());
            dataContext.setLoggedInUserId(pageInfoRequest.getLoggedInUserId());
            dataContext.setPageId(pageInfoRequest.getPageId());
            dataContext.setSectionIds(pageInfoRequest.getSectionIds());
            dataContext.setContainerIds(pageInfoRequest.getContainerIds());
            dataContext.setProductIds(pageInfoRequest.getProductIds());
            dataContext.setSectionId(pageInfoRequest.getSectionId());
            dataContext.setContainerId(pageInfoRequest.getContainerId());
            dataContext.setInputParams(pageInfoRequest.getInputParams());
            dataContext.setPaginate(pageInfoRequest.getPaginate() != null ? pageInfoRequest.getPaginate() : false);
            dataContext.setFilter(pageInfoRequest.getFilter() != null ? pageInfoRequest.getFilter() : false);
            dataContext.setSort(pageInfoRequest.getSort() != null ? pageInfoRequest.getSort() : false);
            dataContext.setPaginationInfo(pageInfoRequest.getPaginationInfo());
            dataContext.setContainerPaginationInfo(pageInfoRequest.getContainerPaginationInfo());
            dataContext.setFilterParams(pageInfoRequest.getFilterParams());
            dataContext.setFilterFieldId(pageInfoRequest.getFilterFieldId());
            dataContext.setSortParams(pageInfoRequest.getSortParams());
            dataContext.setContainerFilterParams(pageInfoRequest.getContainerFilterParams());
            dataContext.setContainerSortParams(pageInfoRequest.getContainerSortParams());
        }
        return dataContext;
    }



    private List<ContainerData> generate(DataContext dataContext, PageInfoRequest pageInfoRequest, SectionInfo sectionInfo) throws DataException {
        List<ContainerData> containerData = new ArrayList<>();
        if(sectionInfo != null) {
            dataContext.setSectionType(SectionType.COMMON);
            dataContext.setSectionId(sectionInfo.getSectionId());
            dataContext.setSectionInfo(sectionInfo);
            List<PageCommonQueryInfo> pageCommonQueryInfos = pageCommonQueryRepo.findByPageId(dataContext.getPageId());
            dataContext.setPageCommonQueryInfos(pageCommonQueryInfos);
            SectionDataGenerator sectionDataGenerator = dataGeneratorFactory.sectionDataGenerator(dataContext);
            containerData = sectionDataGenerator.generate(dataContext);
        }
        return containerData;
    }
}
