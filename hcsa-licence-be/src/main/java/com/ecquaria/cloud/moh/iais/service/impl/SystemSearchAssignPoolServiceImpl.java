package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
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
    public List<SelectOption> getAppStatusOption(String curStage) {
        String roleId;
        switch (curStage){
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
        List<SelectOption> appStatusOption = IaisEGPHelper.getAppStatusByRoleId(roleId);
        if(RoleConsts.USER_ROLE_INSPECTIOR.equals(roleId)) {
            String appValue = MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING);
            SelectOption selectOption = new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING, appValue);
            appStatusOption.add(selectOption);
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
        String memberName = "MOH Officer";
        String roleId = "";
        switch (curStage){
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
}
