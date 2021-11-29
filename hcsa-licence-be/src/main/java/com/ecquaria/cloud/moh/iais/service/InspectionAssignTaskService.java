package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppStageSlaTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.PoolRoleCheckDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ComPoolAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
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
      * @Date 2020/11/18
      * @Param: applicationDtos, stageId
      * @return:  List<HcsaSvcStageWorkingGroupDto>
      * @Descripation: generateHcsaSvcStageWorkingGroupDtos
      */
    List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId);

    /**
     * @author: shicheng
     * @Date 2019/11/23
     * @Param: appCorrelationId, commPools, loginContext, hcsaTaskAssignDto
     * @return: InspecTaskCreAndAssQueryDto
     * @Descripation: Gets a single Common Pool of information for allocation
     */
    InspecTaskCreAndAssDto getInspecTaskCreAndAssDto(String appCorrelationId, List<TaskDto> commPools, LoginContext loginContext, InspecTaskCreAndAssDto inspecTaskCreAndAssDto, HcsaTaskAssignDto hcsaTaskAssignDto);

    /**
     * @author: shicheng
     * @Date 2019/11/23
     * @Param: appGroupId
     * @return: AppGrpPremisesDto
     * @Descripation: get Application Group Premises By Application Id
     */
    AppGrpPremisesDto getAppGrpPremisesDtoByAppGroId(String applicationId);

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
    List<String> getAppCorrIdListByPool(List<TaskDto> commPools);

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
     * @Param: commPools inspecTaskCreAndAssDto applicationViewDto internalRemarks taskDto loginContext
     * @return: void
     * @Descripation: update Common Pool and create Inspector Task
     */
    String assignTaskForInspectors(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto, ApplicationViewDto applicationViewDto, String internalRemarks, TaskDto taskDto, LoginContext loginContext);

    /**
      * @author: shicheng
      * @Date 2020/6/23
      * @Param: td, taskUserIds, applicationDtos, auditTrailDto, applicationGroupDto, loginContext
      * @return: void
      * @Descripation: 
      */
    String assignReschedulingTask(TaskDto td, List<String> taskUserIds, List<ApplicationDto> applicationDtos, AuditTrailDto auditTrailDto,
                                ApplicationGroupDto applicationGroupDto, String inspManHours, LoginContext loginContext);

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
     * @Param: internalRemarks, taskKey, status, appPremisesCorrelationId, processDec, RoleId, subStage, workGroupId
     * @return: AppPremisesRoutingHistoryDto
     * @Descripation: createAppPremisesRoutingHistory
     */
    AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String status, String taskKey, String internalRemarks, String processDec, String RoleId, String subStage, String workGroupId);

    /**
     * @author: shicheng
     * @Date 2019/12/10
     * @Param: commPools, inspecTaskCreAndAssDto, internalRemarks
     * @return: void
     * @Descripation: routing Task By CommonPool
     */
    String routingTaskByCommonPool(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto, String internalRemarks, LoginContext loginContext);

    /**
     * @author: shicheng
     * @Date 2020/3/19
     * @Param: appGrpPremisesDto, appPremisesOperationalUnitDtos
     * @return: String
     * @Descripation: getAddress
     */
    String getAddress(AppGrpPremisesDto appGrpPremisesDto, HcsaTaskAssignDto hcsaTaskAssignDto);

    /**
     * @author: shicheng
     * @Date 2020/3/19
     * @Param: searchParam
     * @return: SearchResult<InspectionAppInGroupQueryDto>
     * @Descripation: getAjaxResultByParam
     */
    SearchResult<ComPoolAjaxQueryDto> getAjaxResultByParam(SearchParam searchParam);

    /**
     * @author: shicheng
     * @Date 2020/3/20
     * @Param: loginContext
     * @return: GroupRoleFieldDto
     * @Descripation: get Group Role Field
     */
    GroupRoleFieldDto getGroupRoleField(LoginContext loginContext);

    /**
     * @author: shicheng
     * @Date 2020/3/28
     * @Param: workGroupId, inspecTaskCreAndAssDto, orgUserDtos
     * @return:
     * @Descripation:
     */
    void setInspectorLeadName(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, List<OrgUserDto> orgUserDtos, String workGroupId);

    /**
      * @author: shicheng
      * @Date 2020/5/11
      * @Param: loginContext, commonPool
      * @return: PoolRoleCheckDto
      * @Descripation: get roles option by pool kind name
      */
    PoolRoleCheckDto getRoleOptionByKindPool(LoginContext loginContext, String poolName, PoolRoleCheckDto poolRoleCheckDto);

    AppStageSlaTrackingDto searchSlaTrackById(String appNo,String stageId);

    /**
      * @author: shicheng
      * @Date 2020/7/7
      * @Param: refNo
      * @return: boolean
      * @Descripation: applicantIsSubmit
      */
    boolean applicantIsSubmit(String refNo);

    /**
      * @author: shicheng
      * @Date 2021/6/22
      * @Param:
      * @return:
      * @Descripation: assign Multiple Task By AppNos
      */
    String assignMultTaskByAppNos(String[] appNoChecks, LoginContext loginContext, List<TaskDto> commPools);

    /**
      * @author: shicheng
      * @Date 2021/11/22
      * @Param: SearchResult<InspectionCommonPoolQueryDto> searchResult
      * @return: List<String>
      * @Descripation: getComPoolAppGrpIdByResult
      */
    List<String> getComPoolAppGrpIdByResult(SearchResult<InspectionCommonPoolQueryDto> searchResult);

    /**
      * @author: shicheng
      * @Date 2021/11/23
      * @Param: searchParam, hci_address, hcsaTaskAssignDto, fieldName, filterName
      * @return: SearchParam
      * @Descripation: setAppGrpIdsByUnitNos
      */
    SearchParam setAppGrpIdsByUnitNos(SearchParam searchParam, String hci_address, HcsaTaskAssignDto hcsaTaskAssignDto, String fieldName, String filterName);

    SearchParam setAppPremisesIdsByUnitNos(SearchParam searchParam, String hci_address, HcsaTaskAssignDto hcsaTaskAssignDto, String fieldName, String filterName);
}
