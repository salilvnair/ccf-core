package com.github.salilvnair.ccf.core.data.context;

import com.github.salilvnair.ccf.core.model.ContainerData;
import com.github.salilvnair.ccf.core.model.FilterParam;
import com.github.salilvnair.ccf.core.model.MultiConditionFilterParams;
import com.github.salilvnair.ccf.core.model.SortParam;
import com.github.salilvnair.ccf.core.constant.StringConstant;
import com.github.salilvnair.ccf.core.data.handler.factory.DataGeneratorFactory;
import com.github.salilvnair.ccf.core.data.handler.task.base.DataTaskExecutor;
import com.github.salilvnair.ccf.core.data.query.context.QueryContext;
import com.github.salilvnair.ccf.core.data.type.ContainerType;
import com.github.salilvnair.ccf.core.data.type.SectionType;
import com.github.salilvnair.ccf.core.entity.*;
import com.github.salilvnair.ccf.core.repository.ContainerInfoRepo;
import com.github.salilvnair.ccf.core.repository.ContainerQueryInfoRepo;
import com.github.salilvnair.ccf.util.paginator.model.ContainerPaginationInfo;
import com.github.salilvnair.ccf.util.paginator.model.PaginationInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class DataContext {
    public static final String DATA_CONTEXT = "DATA_CONTEXT";
    private DataTaskExecutor dataTaskExecutor;
    private DataGeneratorFactory dataGeneratorFactory;
    private ContainerInfoRepo containerInfoRepo;
    private ContainerQueryInfoRepo containerQueryInfoRepo;
    private boolean paginate;
    private boolean filter;
    private boolean sort;
    private SectionType sectionType;
    private Map<String, Object> inputParams;
    private Map<String, Object> commonPageData;
    private List<Map<String, Object>> commonPageDataList;
    private List<FilterParam> filterParams;
    private Integer filterFieldId;
    private List<String> userAccessibleFeatureIds;
    private List<SortParam> sortParams;
    private Map<Integer, List<FilterParam>> containerFilterParams;
    private Map<Integer, MultiConditionFilterParams> containerMultiConditionFilterParams;
    private MultiConditionFilterParams multiConditionFilterParams;
    private Map<Integer, List<SortParam>> containerSortParams;
    private PaginationInfo paginationInfo;
    private Map<Integer, ContainerPaginationInfo> containerPaginationInfo;
    private ContainerType containerType;
    private SectionInfo sectionInfo;
    private ContainerInfo containerInfo;
    private ContainerQueryInfo containerQueryInfo;
    private List<PageCommonQueryInfo> pageCommonQueryInfos;
    private String userId;
    private String loggedInUserId;
    private Integer pageId;
    private Integer sectionId;
    private Integer containerId;
    private List<Integer> sectionIds;
    private List<Integer> containerIds;
    private List<Integer> productIds;
    private List<Integer> mappedRoles;
    private List<ContainerFieldInfo> currentContainerFieldInfoList;
    private QueryContext queryContext;
    private Set<Integer> commonQueriesExecuted;
    private List<Map<String,Object>> containerQueryData;
    private String loadPageContainerByDefault = StringConstant.Y;
    private boolean forceLoadAllActiveContainersBySectionId = false;
    private List<Map<String, Object>> dbData;
    private ContainerData o1cdRequestTransDataContainer;

    public static void inject(Map<String, Object> inputParams, DataContext dataContext) {
        inputParams.put(DATA_CONTEXT, dataContext);
    }

    public static DataContext eject(Map<String, Object> inputParams) {
        return (DataContext) inputParams.get(DATA_CONTEXT);
    }

    public static Map<String, Object> commonPageData(DataContext dataContext) {
        return dataContext.getCommonPageData() == null ? new HashMap<>() : dataContext.commonPageData;
    }

    public static List<Map<String, Object>> commonPageDataList(DataContext dataContext) {
        return dataContext.getCommonPageDataList() == null ? new ArrayList<>() : dataContext.commonPageDataList;
    }

    public void resetContainerIds() {
        this.containerId = null;
        this.containerIds = null;
    }

    public Map<String, Object> inputParams() {
        if(inputParams == null) {
            inputParams = new HashMap<>();
        }
        return inputParams;
    }

    public List<Map<String, Object>> dbData() {
        return dbData == null ? new ArrayList<>() : dbData;
    }

    public boolean paginate(Integer containerId) {
        if (containerPaginationInfo != null) {
            return containerPaginationInfo.get(containerId) != null && containerPaginationInfo.get(containerId).isPaginate();
        }
        return false;
    }

    public void setPaginate(boolean paginate, Integer containerId) {
        this.setPaginate(paginate);
        if (containerPaginationInfo != null && containerPaginationInfo.get(containerId) != null) {
            containerPaginationInfo.get(containerId).setPaginate(paginate);
        }
    }

    public boolean paginate(DataContext dataContext) {
        Integer containerId = dataContext.getContainerQueryInfo() !=null ? dataContext.getContainerQueryInfo().getContainerId() : null;
        if(containerId == null) {
            return dataContext.isPaginate();
        }
        if (containerPaginationInfo != null && containerPaginationInfo.containsKey(containerId)) {
            return containerPaginationInfo.get(containerId) != null && containerPaginationInfo.get(containerId).isPaginate();
        }
        return dataContext.isPaginate();
    }

    public PaginationInfo paginationInfo(DataContext dataContext) {
        Integer containerId = dataContext.getContainerQueryInfo() !=null ? dataContext.getContainerQueryInfo().getContainerId() : null;
        if(containerId == null) {
            return paginationInfo;
        }
        if (containerPaginationInfo != null && containerPaginationInfo.get(containerId) != null) {
            return containerPaginationInfo.get(containerId).getPaginationInfo();
        }
        return paginationInfo;
    }

    public void setPaginationInfo(DataContext dataContext, PaginationInfo paginationInfo) {
        this.setPaginationInfo(paginationInfo);
        if (containerPaginationInfo != null && dataContext.getContainerQueryInfo()!= null && dataContext.getContainerQueryInfo().getContainerId() != null) {
            containerPaginationInfo.put(dataContext.getContainerQueryInfo().getContainerId(), new ContainerPaginationInfo(dataContext.isPaginate(), paginationInfo));
        }
    }
}
