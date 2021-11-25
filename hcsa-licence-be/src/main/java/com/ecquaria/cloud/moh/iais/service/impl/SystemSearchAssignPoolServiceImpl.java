package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.SystemAssignTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.SuperPoolTaskQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.SystemAssignSearchQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.SystemSearchAssignPoolService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2020/8/26 14:31
 **/
@Service
@Slf4j
public class SystemSearchAssignPoolServiceImpl implements SystemSearchAssignPoolService {

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Override
    public GroupRoleFieldDto getSystemSearchStage() {
        GroupRoleFieldDto groupRoleFieldDto = new GroupRoleFieldDto();
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = hcsaConfigClient.stagelist().getEntity();
        if(IaisCommonUtils.isEmpty(hcsaSvcRoutingStageDtos)){
            return null;
        } else {
            List<SelectOption> stageOptions = IaisCommonUtils.genNewArrayList();
            Map<String, HcsaSvcRoutingStageDto> stageMap = IaisCommonUtils.genNewHashMap();
            int index = 0;
            for(HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto : hcsaSvcRoutingStageDtos){
                SelectOption selectOption = new SelectOption(index + "", hcsaSvcRoutingStageDto.getStageName());
                stageMap.put(index + "", hcsaSvcRoutingStageDto);
                stageOptions.add(selectOption);
                index++;
            }
            SelectOption selectOption = stageOptions.get(0);
            if(selectOption != null){
                groupRoleFieldDto.setCurStage(selectOption.getValue());
            }
            groupRoleFieldDto.setStageOption(stageOptions);
            groupRoleFieldDto.setStageMap(stageMap);
        }
        return groupRoleFieldDto;
    }

    @Override
    public List<TaskDto> getSystemTaskPool(String userId) {
        if(!StringUtil.isEmpty(userId)) {
            List<TaskDto> systemTasks = organizationClient.getTasksByUserId(userId).getEntity();
            return systemTasks;
        }
        return null;
    }

    @Override
    @SearchTrack(catalog = "inspectionQuery",key = "systemGroupPoolSearch")
    public SearchResult<SystemAssignSearchQueryDto> getSystemGroupPoolByParam(SearchParam searchParam) {
        SearchResult<SystemAssignSearchQueryDto> searchResult = inspectionTaskClient.searchSystemPoolTaskByGroup(searchParam).getEntity();
        return searchResult;
    }

    @Override
    public List<SelectOption> getAppStatusOption(GroupRoleFieldDto groupRoleFieldDto) {
        List<SelectOption> appStatusOption = IaisCommonUtils.genNewArrayList();
        String curStage = groupRoleFieldDto.getCurStage();
        Map<String, HcsaSvcRoutingStageDto> stageMap = groupRoleFieldDto.getStageMap();
        log.debug(StringUtil.changeForLog("curStage = " + curStage));
        if(!StringUtil.isEmpty(curStage) && stageMap != null) {
            HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto = stageMap.get(curStage);
            if(hcsaSvcRoutingStageDto != null) {
                String stage = hcsaSvcRoutingStageDto.getId();
                String roleId;
                switch (stage) {
                    case HcsaConsts.ROUTING_STAGE_ASO:
                        roleId = RoleConsts.USER_ROLE_ASO;
                        break;
                    case HcsaConsts.ROUTING_STAGE_PSO:
                        roleId = RoleConsts.USER_ROLE_PSO;
                        break;
                    case HcsaConsts.ROUTING_STAGE_INS:
                        roleId = RoleConsts.USER_ROLE_INSPECTIOR;
                        break;
                    case HcsaConsts.ROUTING_STAGE_AO1:
                        roleId = RoleConsts.USER_ROLE_AO1;
                        break;
                    case HcsaConsts.ROUTING_STAGE_AO2:
                        roleId = RoleConsts.USER_ROLE_AO2;
                        break;
                    case HcsaConsts.ROUTING_STAGE_AO3:
                        roleId = RoleConsts.USER_ROLE_AO3;
                        break;
                    default:
                        roleId = "";
                        break;
                }
                appStatusOption = IaisEGPHelper.getAppStatusByRoleId(roleId);
                if (RoleConsts.USER_ROLE_INSPECTIOR.equals(roleId)) {
                    String appValue = MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING);
                    SelectOption selectOption = new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING, appValue);
                    appStatusOption.add(selectOption);
                }
            }
        }
        return appStatusOption;
    }

    @Override
    @SearchTrack(catalog = "inspectionQuery", key = "systemPoolDropdown")
    public SearchResult<SuperPoolTaskQueryDto> getSystemPoolSecondByParam(SearchParam searchParam) {
        return organizationClient.supervisorSecondSearch(searchParam).getEntity();
    }

    @Override
    public GroupRoleFieldDto setGroupMemberName(GroupRoleFieldDto groupRoleFieldDto) {
        String curStage = groupRoleFieldDto.getCurStage();
        Map<String, HcsaSvcRoutingStageDto> stageMap = groupRoleFieldDto.getStageMap();
        HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto = stageMap.get(curStage);
        String stage = hcsaSvcRoutingStageDto.getId();
        String memberName = "MOH Officer";
        String roleId = "";
        switch (stage){
            case HcsaConsts.ROUTING_STAGE_ASO:
                roleId = RoleConsts.USER_ROLE_ASO;
                break;
            case HcsaConsts.ROUTING_STAGE_PSO:
                roleId = RoleConsts.USER_ROLE_PSO;
                break;
            case HcsaConsts.ROUTING_STAGE_INS:
                roleId = RoleConsts.USER_ROLE_INSPECTIOR;
                break;
            case HcsaConsts.ROUTING_STAGE_AO1:
                roleId = RoleConsts.USER_ROLE_AO1;
                break;
            case HcsaConsts.ROUTING_STAGE_AO2:
                roleId = RoleConsts.USER_ROLE_AO2;
                break;
            case HcsaConsts.ROUTING_STAGE_AO3:
                roleId = RoleConsts.USER_ROLE_AO3;
                break;
            default:
                memberName = "MOH Officer";
                break;
        }
        if(!StringUtil.isEmpty(roleId)){
            memberName = MasterCodeUtil.getCodeDesc(roleId);
        }
        groupRoleFieldDto.setGroupMemBerName(memberName);
        return groupRoleFieldDto;
    }

    @Override
    public SystemAssignTaskDto setWorkGroupAndOfficer(GroupRoleFieldDto groupRoleFieldDto, SystemAssignTaskDto systemAssignTaskDto) {
        String curStage = groupRoleFieldDto.getCurStage();
        TaskDto taskDto = systemAssignTaskDto.getTaskDto();
        Map<String, HcsaSvcRoutingStageDto> stageMap = groupRoleFieldDto.getStageMap();
        HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto = stageMap.get(curStage);
        String stage = hcsaSvcRoutingStageDto.getId();
        //get work group
        List<String> workGroupIds = hcsaConfigClient.getWorkGroupIdsByStageId(stage).getEntity();
        if(!IaisCommonUtils.isEmpty(workGroupIds) && taskDto != null){
            Map<String, String> workGroupIdMap = IaisCommonUtils.genNewHashMap();
            Map<String, Map<String, String>> groupCheckUserIdMap = IaisCommonUtils.genNewHashMap();
            Map<String, List<SelectOption>> inspectorByGroup = IaisCommonUtils.genNewHashMap();
            List<String> workGroupNos = IaisCommonUtils.genNewArrayList();
            int workGroupNo = 0;
            List<SelectOption> workGroupOptions = IaisCommonUtils.genNewArrayList();
            for(String workGroupId : workGroupIds){
                WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(workGroupId).getEntity();
                SelectOption workGroupOption = new SelectOption(workGroupNo + "", workingGroupDto.getGroupName());
                workGroupOptions.add(workGroupOption);
                workGroupIdMap.put(workGroupNo + "", workGroupId);
                List<OrgUserDto> orgUserDtoList = organizationClient.activeUsersByWorkGroupAndRole(workGroupId, taskDto.getRoleId()).getEntity();
                if(!IaisCommonUtils.isEmpty(orgUserDtoList)){
                    Map<String, String> userIdMap = IaisCommonUtils.genNewHashMap();
                    List<SelectOption> officerOption = IaisCommonUtils.genNewArrayList();
                    int officerNo = 0;
                    for(OrgUserDto orgUserDto : orgUserDtoList){
                        if(orgUserDto.getAvailable()) {
                            userIdMap.put(officerNo + "", orgUserDto.getId());
                            SelectOption so = new SelectOption(officerNo + "", orgUserDto.getDisplayName());
                            officerOption.add(so);
                            officerNo++;
                        }
                    }
                    groupCheckUserIdMap.put(workGroupNo + "", userIdMap);
                    inspectorByGroup.put(workGroupNo + "", officerOption);
                } else {
                    groupCheckUserIdMap.put(workGroupNo + "", null);
                    inspectorByGroup.put(workGroupNo + "", null);
                }
                workGroupNos.add(workGroupNo + "");
                workGroupNo++;
            }
            systemAssignTaskDto.setWorkGroupIdMap(workGroupIdMap);
            systemAssignTaskDto.setGroupCheckUserIdMap(groupCheckUserIdMap);
            systemAssignTaskDto.setInspectorByGroup(inspectorByGroup);
            systemAssignTaskDto.setWorkGroupNos(workGroupNos);
            systemAssignTaskDto.setWorkGroupOptions(workGroupOptions);
            if(StringUtil.isEmpty(systemAssignTaskDto.getCheckWorkGroup())){
                systemAssignTaskDto.setCheckWorkGroup(workGroupNos.get(0));
            }
        }
        return systemAssignTaskDto;
    }

    @Override
    public SystemAssignTaskDto getDataForSystemAssignTask(Map<String, SuperPoolTaskQueryDto> systemAssignMap, SystemAssignTaskDto systemAssignTaskDto,
                                                          TaskDto taskDto, ApplicationViewDto applicationViewDto) {
        SuperPoolTaskQueryDto superPoolTaskQueryDto = systemAssignMap.get(taskDto.getId());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        HcsaServiceDto hcsaServiceDto = getHcsaServiceDtoByServiceId(applicationDto.getServiceId());
        ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();
        //set
        systemAssignTaskDto.setTaskDto(taskDto);
        systemAssignTaskDto.setApplicationDto(applicationDto);
        systemAssignTaskDto.setSubmitDt(applicationGroupDto.getSubmitDt());
        systemAssignTaskDto.setServiceName(hcsaServiceDto.getSvcName());
        systemAssignTaskDto.setHciCode(superPoolTaskQueryDto.getHciCode());
        systemAssignTaskDto.setHciName(superPoolTaskQueryDto.getHciAddress());
        return systemAssignTaskDto;
    }

    @Override
    public SystemAssignTaskDto getCheckGroupNameAndUserName(SystemAssignTaskDto systemAssignTaskDto) {
        String checkGroup = systemAssignTaskDto.getCheckWorkGroup();
        String checkUser = systemAssignTaskDto.getCheckUser();
        Map<String, List<SelectOption>> inspectorByGroup = systemAssignTaskDto.getInspectorByGroup();
        Map<String, String> workGroupIdMap = systemAssignTaskDto.getWorkGroupIdMap();
        if(inspectorByGroup != null && !StringUtil.isEmpty(checkGroup)){
            if(workGroupIdMap != null){
                String workGroupId = workGroupIdMap.get(checkGroup);
                if(!StringUtil.isEmpty(workGroupId)) {
                    WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(workGroupId).getEntity();
                    systemAssignTaskDto.setCheckGroupName(workingGroupDto.getGroupName());
                } else {
                    systemAssignTaskDto.setCheckGroupName("-");
                }
            }
            List<SelectOption> systemOfficerOption = inspectorByGroup.get(checkGroup);
            if(systemOfficerOption != null){
                for(SelectOption so : systemOfficerOption){
                    String value = so.getValue();
                    if(!StringUtil.isEmpty(value) && value.equals(checkUser)){
                        systemAssignTaskDto.setCheckUserName(so.getText());
                    }
                }
            }
        } else {
            systemAssignTaskDto.setCheckUserName("-");
        }
        return systemAssignTaskDto;
    }

    @Override
    public void systemAssignTask(SystemAssignTaskDto systemAssignTaskDto) {
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        ApplicationDto applicationDto = systemAssignTaskDto.getApplicationDto();
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        TaskDto taskDto = systemAssignTaskDto.getTaskDto();
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, taskDto.getTaskKey());
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        int score = hcsaSvcStageWorkingGroupDtos.get(0).getCount();
        //task
        String checkGroup = systemAssignTaskDto.getCheckWorkGroup();
        String checkUser = systemAssignTaskDto.getCheckUser();
        Map<String, String> workGroupIdMap = systemAssignTaskDto.getWorkGroupIdMap();
        Map<String, Map<String, String>> groupCheckUserIdMap = systemAssignTaskDto.getGroupCheckUserIdMap();
        //set new task user Id and work group Id
        TaskDto createTask = new TaskDto();
        if(groupCheckUserIdMap != null && !StringUtil.isEmpty(checkGroup)){
            if(workGroupIdMap != null){
                String workGroupId = workGroupIdMap.get(checkGroup);
                createTask.setWkGrpId(workGroupId);
                if (ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING.equals(applicationDto.getStatus()) && !StringUtil.isEmpty(workGroupId)) {
                    //set inspector leads
                    setInspLeadsInRecommendation(taskDto, workGroupId, auditTrailDto);
                }
            }
            Map<String, String> groupUserMap = groupCheckUserIdMap.get(checkGroup);
            if(groupUserMap != null){
                String userId = groupUserMap.get(checkUser);
                createTask.setUserId(userId);
            }
        } else {
            systemAssignTaskDto.setCheckUserName("-");
        }
        createTask.setScore(score);
        createTask.setAuditTrailDto(auditTrailDto);
        createTask = setOtherDataByOldTask(createTask, taskDto);
        //update task
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
        taskDto.setAuditTrailDto(auditTrailDto);
        taskService.updateTask(taskDto);
        List<TaskDto> taskDtos = IaisCommonUtils.genNewArrayList();
        taskDtos.add(createTask);
        taskService.createTasks(taskDtos);
        //create history
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorySubStage(taskDto.getRefNo(), taskDto.getTaskKey()).getEntity();
        createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), null, InspectionConstants.PROCESS_DECI_SYSTEM_ADMIN_RE_ASSIGN, taskDto.getRoleId(), appPremisesRoutingHistoryDto.getSubStage(), taskDto.getWkGrpId());
        createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), null, null, taskDto.getRoleId(), appPremisesRoutingHistoryDto.getSubStage(), taskDto.getWkGrpId());
    }

    @Override
    public String getSysCurStageId(GroupRoleFieldDto groupRoleFieldDto) {
        String stageId = "";
        if(groupRoleFieldDto != null) {
            String curStage = groupRoleFieldDto.getCurStage();
            if (!StringUtil.isEmpty(curStage)) {
                Map<String, HcsaSvcRoutingStageDto> stageMap = groupRoleFieldDto.getStageMap();
                if (stageMap != null) {
                    HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto = stageMap.get(curStage);
                    if (hcsaSvcRoutingStageDto != null) {
                        stageId = hcsaSvcRoutingStageDto.getId();
                    }
                }
            }
        }
        return stageId;
    }

    @Override
    public List<String> getSystemPoolAppGrpIdByResult(SearchResult<SystemAssignSearchQueryDto> searchResult) {
        if(searchResult != null && !IaisCommonUtils.isEmpty(searchResult.getRows())) {
            List<String> appGrpIds = IaisCommonUtils.genNewArrayList();
            List<SystemAssignSearchQueryDto> systemAssignSearchQueryDtos = searchResult.getRows();
            for(SystemAssignSearchQueryDto systemAssignSearchQueryDto : systemAssignSearchQueryDtos) {
                if(systemAssignSearchQueryDto != null) {
                    appGrpIds.add(systemAssignSearchQueryDto.getId());
                }
            }
            return appGrpIds;
        }
        return null;
    }

    private TaskDto setOtherDataByOldTask(TaskDto createTask, TaskDto taskDto) {
        createTask.setId(null);
        createTask.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        createTask.setApplicationNo(taskDto.getApplicationNo());
        createTask.setProcessUrl(taskDto.getProcessUrl());
        createTask.setTaskKey(taskDto.getTaskKey());
        createTask.setRoleId(taskDto.getRoleId());
        createTask.setDateAssigned(new Date());
        createTask.setTaskType(taskDto.getTaskType());
        createTask.setSlaAlertInDays(taskDto.getSlaAlertInDays());
        createTask.setSlaDateCompleted(null);
        createTask.setSlaInDays(taskDto.getSlaInDays());
        createTask.setSlaRemainInDays(null);
        createTask.setPriority(taskDto.getPriority());
        createTask.setRefNo(taskDto.getRefNo());
        return createTask;
    }

    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId){
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos start ...."));
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = IaisCommonUtils.genNewArrayList();
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos stageId -->:"+stageId));
        for(ApplicationDto applicationDto : applicationDtos){
            AppGrpPremisesEntityDto appGrpPremisesEntityDto = applicationClient.getPremisesByAppNo(applicationDto.getApplicationNo()).getEntity();
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            if(appGrpPremisesEntityDto != null){
                hcsaSvcStageWorkingGroupDto.setPremiseType(appGrpPremisesEntityDto.getPremisesType());
            }else{
                log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos this APP do not have the premise :"+applicationDto.getApplicationNo()));
            }
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos end ...."));
        return hcsaSvcStageWorkingGroupDtos;
    }

    public HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId){
        return hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String appStatus, String stageId, String internalRemarks,
                                                                         String processDec, String roleId, String subStage, String workGroupId){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setProcessDecision(processDec);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setSubStage(subStage);
        appPremisesRoutingHistoryDto.setWrkGrpId(workGroupId);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto).getEntity();
        return appPremisesRoutingHistoryDto;
    }

    private void setInspLeadsInRecommendation(TaskDto taskDto, String workGroupId, AuditTrailDto auditTrailDto) {
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(taskDto.getRefNo(), InspectionConstants.RECOM_TYPE_INSPECTION_LEAD).getEntity();
        List<String> leadIds = organizationClient.getInspectionLead(workGroupId).getEntity();
        List<OrgUserDto> orgUserDtos = organizationClient.getUsersByWorkGroupName(workGroupId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
        String nameStr = "";
        for (String id : leadIds) {
            for (OrgUserDto oDto : orgUserDtos) {
                if (id.equals(oDto.getId())) {
                    if(StringUtil.isEmpty(nameStr)){
                        nameStr = oDto.getDisplayName();
                    } else {
                        nameStr = nameStr + "," + oDto.getDisplayName();
                    }
                }
            }
        }
        if(appPremisesRecommendationDto == null){
            appPremisesRecommendationDto = new AppPremisesRecommendationDto();
            appPremisesRecommendationDto.setAppPremCorreId(taskDto.getRefNo());
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremisesRecommendationDto.setVersion(1);
            appPremisesRecommendationDto.setRecomInDate(null);
            appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPECTION_LEAD);
            appPremisesRecommendationDto.setRecomDecision(nameStr);
            appPremisesRecommendationDto.setAuditTrailDto(auditTrailDto);
            fillUpCheckListGetAppClient.saveAppRecom(appPremisesRecommendationDto);
        } else {
            appPremisesRecommendationDto.setAppPremCorreId(taskDto.getRefNo());
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPECTION_LEAD);
            appPremisesRecommendationDto.setRecomInDate(null);
            appPremisesRecommendationDto.setRecomDecision(nameStr);
            appPremisesRecommendationDto.setVersion(1);
            appPremisesRecommendationDto.setAuditTrailDto(auditTrailDto);
            fillUpCheckListGetAppClient.updateAppRecom(appPremisesRecommendationDto);
        }
    }
}
