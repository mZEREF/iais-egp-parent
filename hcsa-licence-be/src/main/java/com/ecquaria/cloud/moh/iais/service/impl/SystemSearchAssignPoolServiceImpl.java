package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.SystemAssignTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.SuperPoolTaskQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.SystemAssignSearchQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.SystemSearchAssignPoolService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private ApplicationViewService applicationViewService;

    @Autowired
    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;

    @Autowired
    private InspectionAssignTaskService inspectionAssignTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;

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
        Map<String, HcsaSvcRoutingStageDto> stageMap = groupRoleFieldDto.getStageMap();
        HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto = stageMap.get(curStage);
        String stage = hcsaSvcRoutingStageDto.getId();
        //get work group
        List<String> workGroupIds = hcsaConfigClient.getWorkGroupIdsByStageId(stage).getEntity();
        if(!IaisCommonUtils.isEmpty(workGroupIds)){
            Map<String, String> workGroupIdMap = IaisCommonUtils.genNewHashMap();
            Map<String, Map<String, String>> groupCheckUserIdMap = IaisCommonUtils.genNewHashMap();
            Map<String, List<SelectOption>> inspectorByGroup = IaisCommonUtils.genNewHashMap();
            List<String> workGroupNos = IaisCommonUtils.genNewArrayList();
            int workGroupNo = 0;
            for(String workGroupId : workGroupIds){
                workGroupIdMap.put(workGroupNo + "", workGroupId);
                List<OrgUserDto> orgUserDtoList = organizationClient.getUsersByWorkGroupName(workGroupId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
                if(!IaisCommonUtils.isEmpty(orgUserDtoList)){
                    Map<String, String> userIdMap = IaisCommonUtils.genNewHashMap();
                    List<SelectOption> officerOption = IaisCommonUtils.genNewArrayList();
                    int officerNo = 0;
                    for(OrgUserDto orgUserDto : orgUserDtoList){
                        userIdMap.put(officerNo + "", orgUserDto.getId());
                        SelectOption so = new SelectOption(officerNo + "", orgUserDto.getDisplayName());
                        officerOption.add(so);
                        officerNo++;
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

    public HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId){
        return hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
    }
}
