package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Shicheng
 * @date 2019/11/22 10:19
 **/
@Service
@Slf4j
public class InspectionAssignTaskServiceImpl implements InspectionAssignTaskService {
    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;

    @Autowired
    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;

    @Override
    public List<TaskDto> getCommPoolByGroupWordId(LoginContext loginContext) {
        List<TaskDto> taskDtoList = new ArrayList<>();
        Set<String> workGrpIds = loginContext.getWrkGrpIds();
        if(workGrpIds == null || workGrpIds.size() <= 0){
            return null;
        }
        List<String> workGrpIdList = new ArrayList<>(workGrpIds);
        for(String workGrpId:workGrpIdList){
            for(TaskDto tDto:taskService.getCommPoolByGroupWordId(workGrpId)){
                taskDtoList.add(tDto);
            }
        }
        return taskDtoList;
    }

    @Override
    public InspecTaskCreAndAssDto getInspecTaskCreAndAssDto(String appCorrelationId, List<TaskDto> commPools, LoginContext loginContext,
                                                            InspecTaskCreAndAssDto inspecTaskCreAndAssDto) {
        List<OrgUserDto> orgUserDtos = new ArrayList<>();
        String workGroupId = "";
        for(TaskDto tDto:commPools){
            if(appCorrelationId.equals(tDto.getRefNo())){
                orgUserDtos =  organizationClient.getUsersByWorkGroupName(tDto.getWkGrpId(), AppConsts.COMMON_STATUS_ACTIVE).getEntity();
            }
            if(inspecTaskCreAndAssDto.getTaskId().equals(tDto.getId())){
                workGroupId = tDto.getWkGrpId();
            }
        }

        ApplicationDto applicationDto = searchByAppCorrId(appCorrelationId).getApplicationDto();
        AppGrpPremisesDto appGrpPremisesDto = getAppGrpPremisesDtoByAppGroId(appCorrelationId);
        HcsaServiceDto hcsaServiceDto = getHcsaServiceDtoByServiceId(applicationDto.getServiceId());
        ApplicationGroupDto applicationGroupDto = getApplicationGroupDtoByAppGroId(applicationDto.getAppGrpId());

        inspecTaskCreAndAssDto.setApplicationNo(applicationDto.getApplicationNo());
        inspecTaskCreAndAssDto.setAppCorrelationId(appCorrelationId);
        inspecTaskCreAndAssDto.setApplicationType(applicationDto.getApplicationType());
        inspecTaskCreAndAssDto.setApplicationStatus(applicationDto.getStatus());
        inspecTaskCreAndAssDto.setHciName(appGrpPremisesDto.getHciName() + " / " + appGrpPremisesDto.getAddress());
        inspecTaskCreAndAssDto.setHciCode(appGrpPremisesDto.getHciCode());
        inspecTaskCreAndAssDto.setServiceName(hcsaServiceDto.getSvcName());
        inspecTaskCreAndAssDto.setInspectionTypeName(applicationGroupDto.getIsPreInspection() == 0? "Post":"Pre");
        inspecTaskCreAndAssDto.setInspectionType(applicationGroupDto.getIsPreInspection());
        inspecTaskCreAndAssDto.setSubmitDt(applicationGroupDto.getSubmitDt());
        //set inspector checkbox list
        setInspectorByOrgUserDto(inspecTaskCreAndAssDto, orgUserDtos, loginContext);
        setInspectorLeadName(inspecTaskCreAndAssDto, orgUserDtos, workGroupId);
        return inspecTaskCreAndAssDto;
    }



    private void setInspectorLeadName(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, List<OrgUserDto> orgUserDtos, String workGroupId) {
        if(StringUtil.isEmpty(workGroupId)){
            return;
        }
        List<String> leadNames = new ArrayList<>();
        List<String> leadIds = organizationClient.getInspectionLead(workGroupId).getEntity();
        for(String id : leadIds){
            for(OrgUserDto oDto : orgUserDtos){
                if(id.equals(oDto.getId())){
                    leadNames.add(oDto.getDisplayName());
                }
            }
        }
        inspecTaskCreAndAssDto.setInspectionLeads(leadNames);
    }

    private void setInspectorByOrgUserDto(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, List<OrgUserDto> orgUserDtos, LoginContext loginContext) {
        if(orgUserDtos == null || orgUserDtos.size() <= 0){
            inspecTaskCreAndAssDto.setInspector(null);
            return;
        }
        List<SelectOption> inspectorList = new ArrayList<>();
        Set<String> roles = loginContext.getRoleIds();
        List<String> roleList = new ArrayList<>(roles);
        if(roleList.contains(RoleConsts.USER_ROLE_INSPECTION_LEAD)){
            addInspector(inspectorList, orgUserDtos, loginContext, roleList);
        } else {
            for(OrgUserDto oDto:orgUserDtos){
                if(oDto.getId().equals(loginContext.getUserId())){
                    SelectOption so = new SelectOption(oDto.getId(), oDto.getDisplayName());
                    inspectorList.add(so);
                }
            }
        }
        inspecTaskCreAndAssDto.setInspector(inspectorList);
    }

    private void addInspector(List<SelectOption> inspectorList, List<OrgUserDto> orgUserDtos, LoginContext loginContext, List<String> roleList) {
        String flag = AppConsts.FALSE;
        if(roleList.contains(RoleConsts.USER_ROLE_INSPECTIOR)){
            flag = AppConsts.TRUE;
        }
        for(OrgUserDto oDto:orgUserDtos){
            if(!(oDto.getId().equals(loginContext.getUserId()))){
                SelectOption so = new SelectOption(oDto.getId(), oDto.getDisplayName());
                inspectorList.add(so);
            } else {
                if(AppConsts.TRUE.equals(flag)){
                    SelectOption so = new SelectOption(oDto.getId(), oDto.getDisplayName());
                    inspectorList.add(so);
                }
            }
        }
    }

    @Override
    @SearchTrack(catalog = "inspectionQuery",key = "assignInspector")
    public SearchResult<InspectionCommonPoolQueryDto> getSearchResultByParam(SearchParam searchParam) {
        return inspectionTaskClient.searchInspectionPool(searchParam).getEntity();
    }

    @Override
    public List<String> getAppCorrIdListByPool(List<TaskDto> commPools) {
        if(IaisCommonUtils.isEmpty(commPools)){
            List<String> appCorrIdList = new ArrayList<>();
            appCorrIdList.add(AppConsts.NO);
            return appCorrIdList;
        }
        Set<String> appCorrIdSet = new HashSet<>();
        for(TaskDto tDto:commPools){
            appCorrIdSet.add(tDto.getRefNo());
        }
        List<String> appCorrIdList = new ArrayList<>(appCorrIdSet);

        return appCorrIdList;
    }

    @Override
    public List<SelectOption> getAppTypeOption() {
        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION, ApplicationConsts.APPLICATION_TYPE_RENEWAL, ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE});
        return appTypeOption;
    }

    @Override
    public void assignTaskForInspectors(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto,
                                        ApplicationViewDto applicationViewDto, String internalRemarks, TaskDto taskDto) {
        try {
            List<SelectOption> inspectorCheckList = inspecTaskCreAndAssDto.getInspectorCheck();
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            List<ApplicationDto> applicationDtos = new ArrayList<>();
            applicationDtos.add(applicationDto);
            List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos,HcsaConsts.ROUTING_STAGE_INS);
            hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
            for(TaskDto td:commPools) {
                if (td.getId().equals(inspecTaskCreAndAssDto.getTaskId())) {
                    td.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
                    td.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    inspecTaskCreAndAssDto.setTaskDto(td);
                    inspecTaskCreAndAssDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    inspecTaskCreAndAssDto.setTaskDtos(commPools);
                    inspecTaskCreAndAssDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
                    organizationClient.assignCommonPool(inspecTaskCreAndAssDto);
                    AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorySubStage(td.getRefNo(), td.getTaskKey()).getEntity();
                    createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto.getStatus(),taskDto.getTaskKey(),internalRemarks,
                                                    InspectionConstants.PROCESS_DECI_COMMON_POOL_ASSIGN, td.getRoleId(), appPremisesRoutingHistoryDto.getSubStage(), td.getWkGrpId());
                    if(inspectorCheckList != null && inspectorCheckList.size() > 0){
                        for(int i = 0; i < inspectorCheckList.size(); i++){
                            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto.getStatus(), taskDto.getTaskKey(),null, null, td.getRoleId(), appPremisesRoutingHistoryDto.getSubStage(), td.getWkGrpId());
                        }
                    }
                }
            }
        } catch (Exception e){
            log.error(StringUtil.changeForLog("Error when Submit Assign Task Project: "), e);
            throw e;
        }
    }

    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId){
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = new ArrayList();
        for(ApplicationDto applicationDto : applicationDtos){
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        return hcsaSvcStageWorkingGroupDtos;
    }

    @Override
    public SearchResult<InspectionCommonPoolQueryDto> getAddressByResult(SearchResult<InspectionCommonPoolQueryDto> searchResult) {
        for(InspectionCommonPoolQueryDto icpqDto: searchResult.getRows()){
            AppGrpPremisesDto appGrpPremisesDto = getAppGrpPremisesDtoByAppGroId(icpqDto.getId());
            icpqDto.setHciName(icpqDto.getHciName() + " / " + appGrpPremisesDto.getAddress());
        }
        return searchResult;
    }

    @Override
    public ApplicationViewDto searchByAppCorrId(String correlationId) {
        return applicationClient.getAppViewByCorrelationId(correlationId).getEntity();
    }

    @Override
    public AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String status, String stageId, String internalRemarks,
                                                                        String processDec, String roleId, String subStage, String workGroupId) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(status);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setProcessDecision(processDec);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setSubStage(subStage);
        appPremisesRoutingHistoryDto.setWrkGrpId(workGroupId);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto).getEntity();
        return appPremisesRoutingHistoryDto;
    }

    @Override
    public void routingTaskByCommonPool(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto, String internalRemarks) {
        TaskDto taskDto = getTaskDtoByPool(commPools, inspecTaskCreAndAssDto);
        ApplicationViewDto applicationViewDto = searchByAppCorrId(inspecTaskCreAndAssDto.getAppCorrelationId());
        assignTaskForInspectors(commPools, inspecTaskCreAndAssDto, applicationViewDto, internalRemarks, taskDto);
    }

    private TaskDto getTaskDtoByPool(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto) {
        TaskDto taskDto = new TaskDto();
        for(TaskDto tDto:commPools){
            if(tDto.getId().equals(inspecTaskCreAndAssDto.getTaskId())){
                taskDto = tDto;
            }
        }
        return taskDto;
    }

    /**
      * @author: shicheng
      * @Date 2019/11/22
      * @Param: serviceId
      * @return: HcsaServiceDto
      * @Descripation: get HcsaServiceDto By Service Id
      */
    public HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId){
        return hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
    }

    @Override
    public AppGrpPremisesDto getAppGrpPremisesDtoByAppGroId(String appCorrId){
        AppGrpPremisesDto appGrpPremisesDto = inspectionTaskClient.getAppGrpPremisesDtoByAppGroId(appCorrId).getEntity();
        if(StringUtil.isEmpty(appGrpPremisesDto.getHciName())){
            appGrpPremisesDto.setHciName(HcsaConsts.HCSA_PREMISES_HCI_NULL);
        }if(StringUtil.isEmpty(appGrpPremisesDto.getHciCode())){
            appGrpPremisesDto.setHciCode(HcsaConsts.HCSA_PREMISES_HCI_NULL);
        }
        if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
            appGrpPremisesDto.setConveyanceBlockNo(appGrpPremisesDto.getBlkNo());
            appGrpPremisesDto.setConveyanceStreetName(appGrpPremisesDto.getStreetName());
            appGrpPremisesDto.setConveyanceBuildingName(appGrpPremisesDto.getBuildingName());
            appGrpPremisesDto.setConveyanceFloorNo(appGrpPremisesDto.getFloorNo());
            appGrpPremisesDto.setConveyanceUnitNo(appGrpPremisesDto.getUnitNo());
            appGrpPremisesDto.setConveyancePostalCode(appGrpPremisesDto.getPostalCode());
        }
        return appGrpPremisesDto;
    }

    /**
      * @author: shicheng
      * @Date 2019/11/23
      * @Param: appGroupId
      * @return: ApplicationGroupDto
      * @Descripation: get ApplicationGroup By Application Group Id
      */
    public ApplicationGroupDto getApplicationGroupDtoByAppGroId(String appGroupId){
        return inspectionTaskClient.getApplicationGroupDtoByAppGroId(appGroupId).getEntity();
    }
}
