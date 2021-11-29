package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewMainService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryMainClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationMainClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayMainClient;
import com.ecquaria.cloud.moh.iais.service.client.EgpUserCommonClient;
import com.ecquaria.cloud.moh.iais.service.client.EicClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskMainClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import com.ecquaria.cloud.role.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2019/11/22 10:19
 **/
@Service
@Slf4j
public class InspectionMainAssignTaskServiceImpl implements InspectionMainAssignTaskService {

    @Value("${spring.application.name}")
    private String currentApp;
    @Value("${iais.current.domain}")
    private String currentDomain;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Autowired
    private InspectionTaskMainClient inspectionTaskMainClient;

    @Autowired
    private HcsaConfigMainClient hcsaConfigClient;

    @Autowired
    private ApplicationMainClient applicationMainClient;

    @Autowired
    private OrganizationMainClient organizationClient;

    @Autowired
    private AppPremisesRoutingHistoryMainClient appPremisesRoutingHistoryMainClient;

    @Autowired
    private BeEicGatewayMainClient beEicGatewayMainClient;

    @Autowired
    private EicClient eicClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationViewMainService applicationViewMainService;

    @Autowired
    private EgpUserCommonClient egpUserCommonClient;

    @Override
    public HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId) {
        return hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
    }

    @Override
    public String taskRead(String taskId){
        return organizationClient.taskRead(taskId).getEntity();
    }

    @Override
    public AppGrpPremisesDto getAppGrpPremisesDtoByAppCorrId(String appCorrId) {
        AppGrpPremisesDto appGrpPremisesDto = inspectionTaskMainClient.getAppGrpPremisesDtoByAppGroId(appCorrId).getEntity();
        if (StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
            appGrpPremisesDto.setHciName("");
        }
        if (StringUtil.isEmpty(appGrpPremisesDto.getHciCode())) {
            appGrpPremisesDto.setHciCode(HcsaConsts.HCSA_PREMISES_HCI_NULL);
        }
        setAddressByGroupPremises(appGrpPremisesDto);
        if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())) {
            appGrpPremisesDto.setConveyanceBlockNo(appGrpPremisesDto.getBlkNo());
            appGrpPremisesDto.setConveyanceStreetName(appGrpPremisesDto.getStreetName());
            appGrpPremisesDto.setConveyanceBuildingName(appGrpPremisesDto.getBuildingName());
            appGrpPremisesDto.setConveyanceFloorNo(appGrpPremisesDto.getFloorNo());
            appGrpPremisesDto.setConveyanceUnitNo(appGrpPremisesDto.getUnitNo());
            appGrpPremisesDto.setConveyancePostalCode(appGrpPremisesDto.getPostalCode());
        } else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(appGrpPremisesDto.getPremisesType())) {
            appGrpPremisesDto.setOffSiteBlockNo(appGrpPremisesDto.getBlkNo());
            appGrpPremisesDto.setOffSiteStreetName(appGrpPremisesDto.getStreetName());
            appGrpPremisesDto.setOffSiteBuildingName(appGrpPremisesDto.getBuildingName());
            appGrpPremisesDto.setOffSiteFloorNo(appGrpPremisesDto.getFloorNo());
            appGrpPremisesDto.setOffSiteUnitNo(appGrpPremisesDto.getUnitNo());
            appGrpPremisesDto.setOffSitePostalCode(appGrpPremisesDto.getPostalCode());
        }
        return appGrpPremisesDto;
    }

    private void setAddressByGroupPremises(AppGrpPremisesDto appGrpPremisesDto) {
        if (StringUtil.isEmpty(appGrpPremisesDto.getBlkNo())) {
            appGrpPremisesDto.setBlkNo("");
        }
        if (StringUtil.isEmpty(appGrpPremisesDto.getStreetName())) {
            appGrpPremisesDto.setStreetName("");
        }
        if (StringUtil.isEmpty(appGrpPremisesDto.getBuildingName())) {
            appGrpPremisesDto.setBuildingName("");
        }
        if (StringUtil.isEmpty(appGrpPremisesDto.getFloorNo())) {
            appGrpPremisesDto.setFloorNo("");
        }
        if (StringUtil.isEmpty(appGrpPremisesDto.getUnitNo())) {
            appGrpPremisesDto.setUnitNo("");
        }
        if (StringUtil.isEmpty(appGrpPremisesDto.getPostalCode())) {
            appGrpPremisesDto.setPostalCode("");
        }
    }

    @Override
    public String getAddress(AppGrpPremisesDto appGrpPremisesDto, HcsaTaskAssignDto hcsaTaskAssignDto) {
        String result = "";
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType()) ||
                ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType()) ||
                ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(appGrpPremisesDto.getPremisesType()) ||
                ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())) {
            if(hcsaTaskAssignDto != null && hcsaTaskAssignDto.getAppPremisesAllUnitNoStrMap() != null) {
                Map<String, String> appPremisesAllUnitNoStrMap = hcsaTaskAssignDto.getAppPremisesAllUnitNoStrMap();
                result = appPremisesAllUnitNoStrMap.get(appGrpPremisesDto.getId());
            } else {
                result = MiscUtil.getAddressForApp(appGrpPremisesDto.getBlkNo(), appGrpPremisesDto.getStreetName(), appGrpPremisesDto.getBuildingName(),
                        appGrpPremisesDto.getFloorNo(), appGrpPremisesDto.getUnitNo(), appGrpPremisesDto.getPostalCode(), null);
            }
        }
        return result;
    }

    @Override
    public InspecTaskCreAndAssDto setFastTrackFlag(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, ApplicationDto applicationDto) {
        boolean fastTrackFlag = false;
        String appStatus = applicationDto.getStatus();
        if(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(appStatus)){
            fastTrackFlag = true;
        }
        if(applicationDto.isFastTracking()){
            inspecTaskCreAndAssDto.setFastTrackCheckFlag(true);
        } else {
            inspecTaskCreAndAssDto.setFastTrackCheckFlag(false);
        }
        inspecTaskCreAndAssDto.setFastTrackFlag(fastTrackFlag);
        return inspecTaskCreAndAssDto;
    }

    @Override
    public InspecTaskCreAndAssDto getInspecTaskCreAndAssDto(ApplicationDto applicationDto, LoginContext loginContext, InspecTaskCreAndAssDto inspecTaskCreAndAssDto,
                                                            HcsaTaskAssignDto hcsaTaskAssignDto) {
        List<OrgUserDto> orgUserDtos = IaisCommonUtils.genNewArrayList();
        TaskDto taskDto = inspecTaskCreAndAssDto.getTaskDto();
        String workGroupId = "";
        String appCorrelationId = "";
        if (taskDto != null) {
            orgUserDtos = organizationClient.getUsersByWorkGroupName(taskDto.getWkGrpId(), AppConsts.COMMON_STATUS_ACTIVE).getEntity();
            workGroupId = taskDto.getWkGrpId();
            appCorrelationId = taskDto.getRefNo();
        }

        AppGrpPremisesDto appGrpPremisesDto = getAppGrpPremisesDtoByAppCorrId(appCorrelationId);
        String address = getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
        HcsaServiceDto hcsaServiceDto = getHcsaServiceDtoByServiceId(applicationDto.getServiceId());
        ApplicationGroupDto applicationGroupDto = getApplicationGroupDtoByAppGroId(applicationDto.getAppGrpId());

        inspecTaskCreAndAssDto.setApplicationId(applicationDto.getId());
        inspecTaskCreAndAssDto.setApplicationNo(applicationDto.getApplicationNo());
        inspecTaskCreAndAssDto.setAppCorrelationId(appCorrelationId);
        inspecTaskCreAndAssDto.setApplicationType(applicationDto.getApplicationType());
        inspecTaskCreAndAssDto.setApplicationStatus(applicationDto.getStatus());
        if (!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
            inspecTaskCreAndAssDto.setHciName(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
        } else {
            inspecTaskCreAndAssDto.setHciName(StringUtil.viewHtml(address));
        }
        inspecTaskCreAndAssDto.setHciCode(appGrpPremisesDto.getHciCode());
        inspecTaskCreAndAssDto.setServiceName(hcsaServiceDto.getSvcName());
        //todo:inspection type
        inspecTaskCreAndAssDto.setInspectionTypeName(InspectionConstants.INSPECTION_TYPE_ONSITE);
        inspecTaskCreAndAssDto.setInspectionType(applicationGroupDto.getIsPreInspection());
        inspecTaskCreAndAssDto.setSubmitDt(applicationGroupDto.getSubmitDt());
        //set inspector checkbox list
        setInspectorByOrgUserDto(inspecTaskCreAndAssDto, orgUserDtos, loginContext);
        setInspectorLeadName(inspecTaskCreAndAssDto, orgUserDtos, workGroupId);
        //set recommendation leads
        setInspectorLeadRecom(inspecTaskCreAndAssDto, appCorrelationId, workGroupId);
        return inspecTaskCreAndAssDto;
    }

    private void setInspectorLeadRecom(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, String appCorrelationId, String workGroupId) {
        if(!StringUtil.isEmpty(workGroupId)) {
            WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(workGroupId).getEntity();
            String workGroupName = workingGroupDto.getGroupName();
            if (!StringUtil.isEmpty(workGroupName) && workGroupName.contains("Inspection") && !workGroupName.contains("Approval")) {
                AppPremisesRecommendationDto appPremisesRecommendationDto = inspectionTaskMainClient.getAppPremRecordByIdAndType(appCorrelationId, InspectionConstants.RECOM_TYPE_INSPECTION_LEAD).getEntity();
                if (appPremisesRecommendationDto == null) {
                    List<String> leadNames = inspecTaskCreAndAssDto.getInspectionLeads();
                    if (!IaisCommonUtils.isEmpty(leadNames)) {
                        String nameStr = "";
                        for (String name : leadNames) {
                            if (StringUtil.isEmpty(nameStr)) {
                                nameStr = name;
                            } else {
                                nameStr = nameStr + "," + name;
                            }
                        }
                        appPremisesRecommendationDto = new AppPremisesRecommendationDto();
                        appPremisesRecommendationDto.setAppPremCorreId(appCorrelationId);
                        appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                        appPremisesRecommendationDto.setVersion(1);
                        appPremisesRecommendationDto.setRecomInDate(null);
                        appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPECTION_LEAD);
                        appPremisesRecommendationDto.setRecomDecision(nameStr);
                        appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                        inspectionTaskMainClient.saveAppRecom(appPremisesRecommendationDto);
                    }
                }
            }
        }
    }

    public void setInspectorLeadName(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, List<OrgUserDto> orgUserDtos, String workGroupId) {
        if (StringUtil.isEmpty(workGroupId)) {
            return;
        }
        List<String> leadNames = IaisCommonUtils.genNewArrayList();
        List<String> leadIds = organizationClient.getInspectionLead(workGroupId).getEntity();
        StringBuilder leadStrBu = new StringBuilder();
        Collections.sort(leadIds);
        for (String id : leadIds) {
            for (OrgUserDto oDto : orgUserDtos) {
                if (id.equals(oDto.getId())) {
                    leadNames.add(oDto.getDisplayName());
                    if(StringUtil.isEmpty(leadStrBu.toString())) {
                        leadStrBu.append(oDto.getDisplayName());
                    } else {
                        leadStrBu.append(',');
                        leadStrBu.append(' ');
                        leadStrBu.append(oDto.getDisplayName());
                    }
                }
            }
        }
        inspecTaskCreAndAssDto.setInspectionLeads(leadNames);
        inspecTaskCreAndAssDto.setGroupLeadersShow(leadStrBu.toString());
    }

    private void setInspectorByOrgUserDto(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, List<OrgUserDto> orgUserDtos, LoginContext loginContext) {
        if (orgUserDtos == null || orgUserDtos.size() <= 0) {
            inspecTaskCreAndAssDto.setInspector(null);
            return;
        }
        List<SelectOption> inspectorList = IaisCommonUtils.genNewArrayList();
        List<String> roles = loginContext.getRoleIds();
        List<String> roleList = new ArrayList<>(roles);
        //get current lead role
        String curRole = loginContext.getCurRoleId();
        //get member role
        String leadRole;
        if (curRole.contains(RoleConsts.USER_LEAD)) {
            leadRole = curRole;
        } else {
            leadRole = curRole + RoleConsts.USER_LEAD;
        }
        if (roleList.contains(leadRole)) {
            addInspector(inspectorList, orgUserDtos, loginContext, roleList);
        } else {
            for (OrgUserDto oDto : orgUserDtos) {
                if (oDto.getId().equals(loginContext.getUserId())) {
                    SelectOption so = new SelectOption(oDto.getId(), oDto.getDisplayName());
                    inspectorList.add(so);
                }
            }
        }
        inspecTaskCreAndAssDto.setInspector(inspectorList);
    }

    private void addInspector(List<SelectOption> inspectorList, List<OrgUserDto> orgUserDtos, LoginContext loginContext, List<String> roleList) {
        String flag = AppConsts.FALSE;
        String curRole = loginContext.getCurRoleId();
        //get member role
        String memberRole;
        if (curRole.contains(RoleConsts.USER_LEAD)) {
            memberRole = curRole.replaceFirst(RoleConsts.USER_LEAD, "");
        } else {
            memberRole = curRole;
        }
        if (roleList.contains(memberRole)) {
            flag = AppConsts.TRUE;
        }
        for (OrgUserDto oDto : orgUserDtos) {
            if (!(oDto.getId().equals(loginContext.getUserId()))) {
                SelectOption so = new SelectOption(oDto.getId(), oDto.getDisplayName());
                inspectorList.add(so);
            } else {
                if (AppConsts.TRUE.equals(flag)) {
                    SelectOption so = new SelectOption(oDto.getId(), oDto.getDisplayName());
                    inspectorList.add(so);
                }
            }
        }
    }

    public ApplicationGroupDto getApplicationGroupDtoByAppGroId(String appGroupId) {
        return inspectionTaskMainClient.getApplicationGroupDtoByAppGroId(appGroupId).getEntity();
    }

    @Override
    public InspecTaskCreAndAssDto setEditHoursFlagByAppAndUser(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, ApplicationDto applicationDto) {
        List<String> appHoursStatusList = IaisCommonUtils.genNewArrayList();
        appHoursStatusList.add(ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL);
        appHoursStatusList.add(ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT);
        appHoursStatusList.add(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT);
        String appStatus = "";
        if(applicationDto != null) {
            appStatus = applicationDto.getStatus();
        }
        if(appHoursStatusList.contains(appStatus)) {
            inspecTaskCreAndAssDto.setEditHoursFlag(AppConsts.COMMON_POOL);
        }
        return inspecTaskCreAndAssDto;
    }

    @Override
    public GroupRoleFieldDto getGroupRoleField(LoginContext loginContext) {
        GroupRoleFieldDto groupRoleFieldDto = new GroupRoleFieldDto();
        String curRole = loginContext.getCurRoleId();
        String otherRole;
        String leadRole;
        String groupLeadName = "";
        String groupMemBerName = "";
        if (curRole.contains(RoleConsts.USER_LEAD)) {
            leadRole = curRole;
            otherRole = curRole.replaceFirst(RoleConsts.USER_LEAD, "");
        } else {
            leadRole = curRole + RoleConsts.USER_LEAD;
            otherRole = curRole;
        }
        if (!StringUtil.isEmpty(leadRole)) {
            if(RoleConsts.USER_ROLE_BROADCAST.equals(otherRole)){
                groupLeadName = "Leader";
            } else {
                Role role = getRoleByDomainRoleId(AppConsts.HALP_EGP_DOMAIN, leadRole);
                if(role != null) {
                    groupLeadName = role.getName();
                }
            }
        }
        if (!StringUtil.isEmpty(otherRole)) {
            Role role = getRoleByDomainRoleId(AppConsts.HALP_EGP_DOMAIN, otherRole);
            if(role != null) {
                groupMemBerName = role.getName();
            }
        }
        groupRoleFieldDto.setGroupLeadName(groupLeadName);
        groupRoleFieldDto.setGroupMemBerName(groupMemBerName);
        return groupRoleFieldDto;
    }

    private Role getRoleByDomainRoleId(String domain, String roleId) {
        if(StringUtil.isEmpty(domain) || StringUtil.isEmpty(roleId)){
            return null;
        }
        List<Role> roles = getRolesByDomain(domain);
        if(!IaisCommonUtils.isEmpty(roles)) {
            for (Role roleDto : roles) {
                if(roleDto != null && roleId.equals(roleDto.getId())) {
                    return roleDto;
                }
            }
        }
        return null;
    }

    private List<Role> getRolesByDomain(String domain) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        map.put("userDomains", domain);
        return egpUserCommonClient.search(map).getEntity();
    }

    @Override
    public String routingTaskByCommonPool(ApplicationViewDto applicationViewDto, InspecTaskCreAndAssDto inspecTaskCreAndAssDto, String internalRemarks, LoginContext loginContext) {
        TaskDto taskDto = inspecTaskCreAndAssDto.getTaskDto();
        String saveFlag = assignTaskForInspectors(inspecTaskCreAndAssDto, applicationViewDto, internalRemarks, taskDto, loginContext);
        if(!StringUtil.isEmpty(inspecTaskCreAndAssDto.getInspManHours()) && AppConsts.SUCCESS.equals(saveFlag)){
            //create inspManHours recommendation or update
            AppPremisesRecommendationDto appPremisesRecommendationDto = inspectionTaskMainClient.getAppPremRecordByIdAndType(taskDto.getRefNo(), InspectionConstants.RECOM_TYPE_INSP_MAN_HOUR).getEntity();
            if(appPremisesRecommendationDto != null){
                appPremisesRecommendationDto.setRecomDecision(inspecTaskCreAndAssDto.getInspManHours());
                appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            } else {
                appPremisesRecommendationDto = new AppPremisesRecommendationDto();
                appPremisesRecommendationDto.setAppPremCorreId(taskDto.getRefNo());
                appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPremisesRecommendationDto.setVersion(1);
                appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSP_MAN_HOUR);
                appPremisesRecommendationDto.setRecomDecision(inspecTaskCreAndAssDto.getInspManHours());
                appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            }
            inspectionTaskMainClient.saveAppRecom(appPremisesRecommendationDto);
        }
        return saveFlag;
    }

    public String assignTaskForInspectors(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, ApplicationViewDto applicationViewDto, String internalRemarks, TaskDto taskDto, LoginContext loginContext) {
        List<SelectOption> inspectorCheckList = inspecTaskCreAndAssDto.getInspectorCheck();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        if(RoleConsts.USER_ROLE_BROADCAST.equals(taskDto.getRoleId())){
            //broadcast task assign
            String saveFlag = assignBroadcastTask(taskDto, applicationDtos, auditTrailDto, loginContext);
            return saveFlag;
        } else {
            //get score
            List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, taskDto.getTaskKey());
            hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
            //other tasks assign
            List<TaskDto> taskDtos = IaisCommonUtils.genNewArrayList();
            taskDtos.add(taskDto);
            taskDto.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
            taskDto.setAuditTrailDto(auditTrailDto);
            inspecTaskCreAndAssDto.setTaskDto(taskDto);
            inspecTaskCreAndAssDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            inspecTaskCreAndAssDto.setTaskDtos(taskDtos);
            inspecTaskCreAndAssDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
            inspecTaskCreAndAssDto = organizationClient.assignCommonPool(inspecTaskCreAndAssDto).getEntity();
            if(inspecTaskCreAndAssDto != null) {
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryMainClient.getAppPremisesRoutingHistorySubStage(taskDto.getRefNo(), taskDto.getTaskKey()).getEntity();
                createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), internalRemarks,
                        InspectionConstants.PROCESS_DECI_COMMON_POOL_ASSIGN, taskDto.getRoleId(), appPremisesRoutingHistoryDto.getSubStage(), taskDto.getWkGrpId());
                if (inspectorCheckList != null && inspectorCheckList.size() > 0) {
                    for (int i = 0; i < inspectorCheckList.size(); i++) {
                        if (ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(applicationDto.getStatus())) {
                            if(applicationDto.isFastTracking()) {
                                applicationDto.setFastTracking(true);
                            } else {
                                String fastTrack = inspecTaskCreAndAssDto.getFastTrackCheck();
                                if (!StringUtil.isEmpty(fastTrack)) {
                                    applicationDto.setFastTracking(true);
                                } else {
                                    applicationDto.setFastTracking(false);
                                }
                            }
                            ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING);
                            updateFEApplication(applicationDto1);
                            inspecTaskCreAndAssDto.setApplicationStatus(applicationDto1.getStatus());
                            createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(), applicationDto1.getStatus(), taskDto.getTaskKey(), null, null, taskDto.getRoleId(), null, taskDto.getWkGrpId());
                        } else {
                            createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), null, null, taskDto.getRoleId(), appPremisesRoutingHistoryDto.getSubStage(), taskDto.getWkGrpId());
                        }
                    }
                }
            } else {
                return AppConsts.FAIL;
            }
        }
        return AppConsts.SUCCESS;
    }

    private String assignBroadcastTask(TaskDto td, List<ApplicationDto> applicationDtos, AuditTrailDto auditTrailDto, LoginContext loginContext) {
        //get role and stage
        List<String> roleIdSet = loginContext.getRoleIds();
        List<String> roleIds = new ArrayList<>(roleIdSet);
        Map<String, String> stageRoleMap = MiscUtil.getStageRoleByBroadcast(roleIds);
        if(stageRoleMap != null){
            td.setSlaDateCompleted(new Date());
            td.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
            td.setAuditTrailDto(auditTrailDto);
            TaskDto updateTask = organizationClient.updateTaskForAssign(td).getEntity();
            if(updateTask != null) {
                List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
                for (Map.Entry<String, String> map : stageRoleMap.entrySet()) {
                    ApplicationDto applicationDto = applicationDtos.get(0);
                    createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), td.getTaskKey(), null,
                            InspectionConstants.PROCESS_DECI_COMMON_POOL_ASSIGN, td.getRoleId(), null, td.getWkGrpId());
                    String subStage = null;
                    String stageId;
                    String role = map.getValue();
                    if (RoleConsts.USER_ROLE_AO1.equals(role)) {
                        stageId = getAoOneStage(applicationDto.getApplicationNo());
                        if (HcsaConsts.ROUTING_STAGE_INS.equals(stageId)) {
                            subStage = HcsaConsts.ROUTING_STAGE_POT;
                        }
                    } else {
                        stageId = map.getKey();
                    }
                    List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, stageId);
                    hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
                    int score = hcsaSvcStageWorkingGroupDtos.get(0).getCount();
                    String processUrl = getProcessUrlByRoleAndStageId(role, stageId);
                    TaskDto taskDto = new TaskDto();
                    taskDto.setId(null);
                    taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
                    taskDto.setPriority(td.getPriority());
                    taskDto.setRefNo(td.getRefNo());
                    taskDto.setSlaAlertInDays(td.getSlaAlertInDays());
                    taskDto.setSlaDateCompleted(null);
                    taskDto.setSlaInDays(td.getSlaInDays());
                    taskDto.setSlaRemainInDays(null);
                    taskDto.setTaskKey(stageId);
                    taskDto.setTaskType(td.getTaskType());
                    taskDto.setWkGrpId(td.getWkGrpId());
                    taskDto.setUserId(loginContext.getUserId());
                    taskDto.setDateAssigned(new Date());
                    taskDto.setRoleId(td.getRoleId());
                    taskDto.setAuditTrailDto(auditTrailDto);
                    taskDto.setProcessUrl(processUrl);
                    taskDto.setScore(score);
                    taskDto.setApplicationNo(td.getApplicationNo());
                    taskDtoList.add(taskDto);
                    createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), stageId, null, null, td.getRoleId(), subStage, td.getWkGrpId());
                }
                taskService.createTasks(taskDtoList);
            } else {
                return AppConsts.FAIL;
            }
        }
        return AppConsts.SUCCESS;
    }

    private String getProcessUrlByRoleAndStageId(String role, String stageId) {
        String processUrl = TaskConsts.TASK_PROCESS_URL_MAIN_FLOW;
        if(RoleConsts.USER_ROLE_AO1.equals(role)){
            if(HcsaConsts.ROUTING_STAGE_INS.equals(stageId)){
                processUrl = TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT_REVIEW_AO1;
            }
        } else if(RoleConsts.USER_ROLE_INSPECTIOR.equals(role)){
            processUrl = TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT;
        }
        return processUrl;
    }

    private String getAoOneStage(String applicationNo) {
        String stageId = HcsaConsts.ROUTING_STAGE_AO1;
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = appPremisesRoutingHistoryMainClient.getAppPremisesRoutingHistorysByAppNo(applicationNo).getEntity();
        if(!IaisCommonUtils.isEmpty(appPremisesRoutingHistoryDtos)){
            for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto : appPremisesRoutingHistoryDtos){
                if(HcsaConsts.ROUTING_STAGE_INS.equals(appPremisesRoutingHistoryDto.getStageId())){
                    stageId = HcsaConsts.ROUTING_STAGE_INS;
                    return stageId;
                }
            }
        }
        return stageId;
    }

    public List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId){
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos start ...."));
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = IaisCommonUtils.genNewArrayList();
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos stageId -->:"+stageId));
        for(ApplicationDto applicationDto : applicationDtos){
            AppGrpPremisesEntityDto appGrpPremisesEntityDto = applicationMainClient.getPremisesByAppNo(applicationDto.getApplicationNo()).getEntity();
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

    private ApplicationDto updateApplication(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationViewMainService.updateApplicaiton(applicationDto);
    }

    public ApplicationDto updateFEApplication(ApplicationDto applicationDto) {
        log.info(StringUtil.changeForLog("The updateFEApplicaiton start ..."));
        String moduleName = currentApp + "-" + currentDomain;
        EicRequestTrackingDto dto = new EicRequestTrackingDto();
        dto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        dto.setActionClsName(this.getClass().getName());
        dto.setActionMethod("callEicInterApplication");
        dto.setDtoClsName(applicationDto.getClass().getName());
        dto.setDtoObject(JsonUtil.parseToJson(applicationDto));
        String refNo = String.valueOf(System.currentTimeMillis());
        log.info(StringUtil.changeForLog("The updateFEApplicaiton refNo is  -- >:"+refNo));
        dto.setRefNo(refNo);
        dto.setModuleName(moduleName);
        eicClient.saveEicTrack(dto);
        callEicInterApplication(applicationDto);
        dto = eicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
        Date now = new Date();
        dto.setProcessNum(1);
        dto.setFirstActionAt(now);
        dto.setLastActionAt(now);
        dto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        List<EicRequestTrackingDto> list = IaisCommonUtils.genNewArrayList(1);
        list.add(dto);
        eicClient.updateStatus(list);
        log.info(StringUtil.changeForLog("The updateFEApplicaiton end ..."));
        return applicationDto;
    }

    public ApplicationDto callEicInterApplication(ApplicationDto applicationDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return beEicGatewayMainClient.updateApplication(applicationDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
    }

    public AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String status, String stageId, String internalRemarks,
                                                                        String processDec, String roleId, String subStage, String workGroupId) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(status);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setProcessDecision(processDec);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setSubStage(subStage);
        appPremisesRoutingHistoryDto.setWrkGrpId(workGroupId);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryMainClient.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto).getEntity();
        return appPremisesRoutingHistoryDto;
    }
}