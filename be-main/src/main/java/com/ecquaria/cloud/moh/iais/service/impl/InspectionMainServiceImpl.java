package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppInGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.BelicationViewMainService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.BeInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.BePremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskMainClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Shicheng
 * @date 2019/11/19 14:45
 **/
@Service
@Slf4j
public class InspectionMainServiceImpl implements InspectionMainService {
    @Autowired
    private InspectionTaskMainClient inspectionTaskClient;

    @Autowired
    private HcsaConfigMainClient hcsaConfigClient;

    @Autowired
    private OrganizationMainClient organizationClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private BelicationViewMainService belicationViewService;

    @Autowired
    private BePremisesRoutingHistoryClient bePremisesRoutingHistoryClient;

    @Autowired
    private InspectionMainAssignTaskService inspectionAssignTaskService;

    @Autowired
    private BeInspectionStatusClient beInspectionStatusClient;

    @Override
    public List<SelectOption> getAppTypeOption() {
        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION, ApplicationConsts.APPLICATION_TYPE_RENEWAL, ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE});
        return appTypeOption;
    }

    @Override
    public List<SelectOption> getAppStatusOption(String role) {
        String[] statusStrs;
        switch (role){
            case RoleConsts.USER_ROLE_PSO_LEAD:
            case RoleConsts.USER_ROLE_PSO:
                statusStrs = new String[]{
                        ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST,
                        ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ENQUIRE,
                };
                break;
            case RoleConsts.USER_ROLE_ASO_LEAD:
            case RoleConsts.USER_ROLE_ASO:
                statusStrs = new String[]{
                        ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_DMS_APPROVAL,
                        ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ENQUIRE,
                        ApplicationConsts.APPLICATION_STATUS_PROFESSIONAL_SCREENING_OFFICER_ENQUIRE,
                };
                break;
            case RoleConsts.USER_ROLE_AO1_LEAD:
            case RoleConsts.USER_ROLE_AO1:
                statusStrs = new String[]{
                        ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_AO1_SUPPORT,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW,
                };
                break;
            case RoleConsts.USER_ROLE_AO2_LEAD:
            case RoleConsts.USER_ROLE_AO2:
                statusStrs = new String[]{
                        ApplicationConsts.APPLICATION_STATUS_PENDING_AO2_SUPPORT,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST,
                };
                break;
            case RoleConsts.USER_ROLE_AO3_LEAD:
            case RoleConsts.USER_ROLE_AO3:
                statusStrs = new String[]{
                        ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03,
                };
                break;
            case RoleConsts.USER_ROLE_BROADCAST:
                statusStrs = new String[]{
                        ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST,
                };
                break;
            case RoleConsts.USER_ROLE_INSPECTION_LEAD:
                statusStrs = new String[]{
                        ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_DRAFT_LETTER,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_REVIEW,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_DMS_APPROVAL,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING,
                };
                break;
            case RoleConsts.USER_ROLE_INSPECTIOR:
                statusStrs = new String[]{
                        ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_DRAFT_LETTER,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_REVIEW,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION,
                        ApplicationConsts.APPLICATION_STATUS_PENDING_DMS_APPROVAL,
                };
                break;
            default:
                statusStrs = new String[]{
                        ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING,
                };
                break;
        }

        List<SelectOption> appStatusOption = MasterCodeUtil.retrieveOptionsByCodes(statusStrs);
        return appStatusOption;
    }

    @Override
    public List<TaskDto> getTasksByUserIdAndRole(String userId,String curRole) {
        return organizationClient.getTasksByUserIdAndRole(userId, curRole).getEntity();
    }

    @Override
    public String[] getApplicationNoListByPool(List<TaskDto> commPools) {
        if(commPools == null || commPools.size() <= 0){
            return null;
        }
        Set<String> applicationNoSet = IaisCommonUtils.genNewHashSet();
        for(TaskDto tDto:commPools){
            applicationNoSet.add(tDto.getRefNo());
        }
        List<String> applicationNoList = new ArrayList<>(applicationNoSet);
        String[] applicationStrs = new String[applicationNoList.size()];
        for(int i = 0; i < applicationStrs.length; i++){
            applicationStrs[i] = applicationNoList.get(i);
        }
        return applicationStrs;
    }

    @Override
    public HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId){
        return hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
    }

    @Override
    public List<String> getWorkGroupIdsByLogin(LoginContext loginContext) {
        List<String> workGroupIdList = IaisCommonUtils.genNewArrayList();
        List<UserGroupCorrelationDto> userGroupCorrelationDtos = organizationClient.getUserGroupCorreByUserId(loginContext.getUserId()).getEntity();
        for(UserGroupCorrelationDto ugcDto:userGroupCorrelationDtos){
//            if(ugcDto.getIsLeadForGroup() == 1){
                workGroupIdList.add(ugcDto.getGroupId());
//            }
        }
        return workGroupIdList;
    }

    @Override
    public void assignTaskForInspectors(InspectionTaskPoolListDto inspectionTaskPoolListDto, List<TaskDto> commPools,
                                        String internalRemarks, ApplicationDto applicationDto, TaskDto taskDto, ApplicationViewDto applicationViewDto) {
        try {
            List<SelectOption> inspectorCheckList = inspectionTaskPoolListDto.getInspectorCheck();
            for(TaskDto td:commPools) {
                if (td.getId().equals(inspectionTaskPoolListDto.getTaskId())) {
                    if(StringUtil.isEmpty(td.getUserId())){
                        td.setUserId(inspectorCheckList.get(0).getValue());
                        td.setDateAssigned(new Date());
                        td.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                        updateTask(td);
                        inspectorCheckList.remove(0);
                        createAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),taskDto.getTaskKey(),internalRemarks);
                        ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING);
                        applicationViewDto.setApplicationDto(applicationDto1);
                        createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(),applicationDto1.getStatus(), HcsaConsts.ROUTING_STAGE_INS,null);
                        beInspectionStatusClient.createAppInspectionStatusByAppDto(applicationDto);
                        if(inspectorCheckList != null && inspectorCheckList.size() > 0){
                            inspectionTaskPoolListDto(inspectorCheckList, commPools, inspectionTaskPoolListDto);
                            for(int i = 0; i < inspectorCheckList.size(); i++){
                                createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(),applicationDto1.getStatus(), HcsaConsts.ROUTING_STAGE_INS,null);
                            }
                        }
                    } else {
                        td.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
                        td.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                        updateTask(td);
                        createAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),taskDto.getTaskKey(),internalRemarks);
                        if(inspectorCheckList != null && inspectorCheckList.size() > 0){
                            inspectionTaskPoolListDto(inspectorCheckList, commPools, inspectionTaskPoolListDto);
                            for(int i = 0; i < inspectorCheckList.size(); i++){
                                createAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),taskDto.getTaskKey(),internalRemarks);
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            log.error(StringUtil.changeForLog("Error when Submit Assign Task Project: "), e);
            throw e;
        }
    }

    private TaskDto getTaskDtoByPool(List<TaskDto> commPools, InspectionTaskPoolListDto inspectionTaskPoolListDto) {
        TaskDto taskDto = new TaskDto();
        for(TaskDto tDto:commPools){
            if(tDto.getId().equals(inspectionTaskPoolListDto.getTaskId())){
                taskDto = tDto;
            }
        }
        return taskDto;
    }

    private ApplicationDto  updateApplication(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return belicationViewService.updateApplicaiton(applicationDto);
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String appStatus,
                                                                         String stageId, String internalRemarks){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto = bePremisesRoutingHistoryClient.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto).getEntity();
        return appPremisesRoutingHistoryDto;
    }

    private void inspectionTaskPoolListDto(List<SelectOption> inspectorCheckList, List<TaskDto> commPools, InspectionTaskPoolListDto inspectionTaskPoolListDto) {
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
        for(SelectOption so : inspectorCheckList) {
            for (TaskDto td : commPools) {
                if(td.getId().equals(inspectionTaskPoolListDto.getTaskId())){
                    td.setId("");
                    td.setUserId(so.getValue());
                    td.setDateAssigned(new Date());
                    td.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    taskDtoList.add(td);
                }
            }
        }
        createTask(taskDtoList);
    }

    private void createTask(List<TaskDto> taskDtoList){
        taskService.createTasks(taskDtoList);
    }

    private void updateTask(TaskDto td) {
        taskService.updateTask(td);
    }

    /**
     * @author: shicheng
     * @Date 2019/11/23
     * @Param: appGroupId
     * @return: AppGrpPremisesDto
     * @Descripation: get Application Group Premises By Application Id
     */
    public AppGrpPremisesDto getAppGrpPremisesDtoByAppGroId(String applicationId){
        return inspectionTaskClient.getAppGrpPremisesDtoByAppGroId(applicationId).getEntity();
    }


    @Override
    @SearchTrack(catalog = "inspectionQuery",key = "AppGroup")
    public SearchResult<InspectionAppGroupQueryDto> searchInspectionBeAppGroup(SearchParam searchParam) {
        return inspectionTaskClient.searchInspectionBeAppGroup(searchParam).getEntity();
    }

    @Override
    @SearchTrack(catalog = "inspectionQuery",key = "AppByGroupAjax")
    public SearchResult<InspectionAppInGroupQueryDto> searchInspectionBeAppGroupAjax(SearchParam searchParam){
        return inspectionTaskClient.searchInspectionBeAppGroupAjax(searchParam).getEntity();
    }

}
