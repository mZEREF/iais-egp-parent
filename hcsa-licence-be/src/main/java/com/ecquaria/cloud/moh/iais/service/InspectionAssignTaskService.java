package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/11/22 10:19
 **/
public interface InspectionAssignTaskService {
    /**
     * @author: shicheng
     * @Date 2019/11/21
     * @Param: loginContext
     * @return: List<TaskDto>
     * @Descripation: According to the group Id, get the work pool
     */
    List<TaskDto> getCommPoolByGroupWordId(LoginContext loginContext);

    /**
      * @author: shicheng
      * @Date 2019/11/23
      * @Param: appCorrelationId, commPools, loginContext
      * @return: InspecTaskCreAndAssQueryDto
      * @Descripation: Gets a single Common Pool of information for allocation
      */
    InspecTaskCreAndAssDto getInspecTaskCreAndAssDto(String appCorrelationId, List<TaskDto> commPools, LoginContext loginContext);

    /**
      * @author: shicheng
      * @Date 2019/11/27
      * @Param: searchParam
      * @return: SearchResult
      * @Descripation: Search results based on search criteria
      */
    SearchResult<InspectionCommonPoolQueryDto> getSearchResultByParam(SearchParam searchParam);

    /**
      * @author: shicheng
      * @Date 2019/11/27
      * @Param: commPools
      * @return: List<String>
      * @Descripation: get Application No By commPools
      */
    String[] getAppCorrIdListByPool(List<TaskDto> commPools);

    /**
      * @author: shicheng
      * @Date 2019/11/27
      * @Param: null
      * @return: List<SelectOption>
      * @Descripation: getAppTypeOption
      */
    List<SelectOption> getAppTypeOption();

    /**
      * @author: shicheng
      * @Date 2019/11/29
      * @Param: commPools inspecTaskCreAndAssDto applicationViewDto internalRemarks taskDto
      * @return: void
      * @Descripation: update Common Pool and create Inspector Task
      */
    void assignTaskForInspectors(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto, ApplicationViewDto applicationViewDto, String internalRemarks, TaskDto taskDto);

    /**
      * @author: shicheng
      * @Date 2019/12/2
      * @Param: searchResult
      * @return: SearchResult
      * @Descripation: get Address By Result
      */
    SearchResult<InspectionCommonPoolQueryDto> getAddressByResult(SearchResult<InspectionCommonPoolQueryDto> searchResult);

    /**
      * @author: shicheng
      * @Date 2019/12/5
      * @Param: applicationNo
      * @return: ApplicationViewDto
      * @Descripation: search ApplicationViewDto By app premises correlation Id
      */
    ApplicationViewDto searchByAppCorrId(String correlationId);

    /**
      * @author: shicheng
      * @Date 2019/12/10
      * @Param: internalRemarks, taskKey, status, appPremisesCorrelationId, processDec, RoleId
      * @return: AppPremisesRoutingHistoryDto
      * @Descripation: createAppPremisesRoutingHistory
      */
    AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String status, String taskKey, String internalRemarks, String processDec, String RoleId);

    /**
      * @author: shicheng
      * @Date 2019/12/10
      * @Param: commPools, inspecTaskCreAndAssDto, internalRemarks
      * @return: void
      * @Descripation: routing Task By CommonPool
      */
    void routingTaskByCommonPool(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto, String internalRemarks);
}
