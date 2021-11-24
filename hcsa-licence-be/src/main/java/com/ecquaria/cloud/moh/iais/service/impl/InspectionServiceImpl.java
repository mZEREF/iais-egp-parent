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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.SuperPoolTaskQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Shicheng
 * @date 2019/11/19 14:45
 **/
@Service
@Slf4j
public class InspectionServiceImpl implements InspectionService {
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

    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;

    @Override
    public List<SelectOption> getAppTypeOption() {
        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{
                ApplicationConsts.APPLICATION_TYPE_APPEAL,
                ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK,
                ApplicationConsts.APPLICATION_TYPE_CESSATION,
                ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,
                ApplicationConsts.APPLICATION_TYPE_RENEWAL,
                ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,
                ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL,
                ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION
        });
        return appTypeOption;
    }

    @Override
    public List<SelectOption> getAppStatusOption(LoginContext loginContext, String poolType) {
        String roleId = loginContext.getCurRoleId();
        List<SelectOption> appStatusOption = IaisEGPHelper.getAppStatusByRoleId(roleId);
        if(AppConsts.SUPERVISOR_POOL.equals(poolType)){
            String pendingTaskAssignStatus = MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT);
            SelectOption so = new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT, pendingTaskAssignStatus);
            appStatusOption.add(so);
        }
        return appStatusOption;
    }

    @Override
    public List<TaskDto> getSupervisorPoolByGroupWordId(String workGroupId) {
        return organizationClient.getSupervisorPoolByGroupWordId(workGroupId).getEntity();
    }

    @Override
    public List<String> getApplicationNoListByPool(List<TaskDto> commPools) {
        if(IaisCommonUtils.isEmpty(commPools)){
            List<String> appCorrIdList = IaisCommonUtils.genNewArrayList();
            return appCorrIdList;
        }
        Set<String> appCorrIdSet = IaisCommonUtils.genNewHashSet();
        for(TaskDto tDto:commPools){
            appCorrIdSet.add(tDto.getRefNo());
        }
        List<String> appCorrIdList = new ArrayList<>(appCorrIdSet);
        return appCorrIdList;
    }

    @Override
    public HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId){
        return hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
    }

    @Override
    public String routingTaskByPool(InspectionTaskPoolListDto inspectionTaskPoolListDto, List<TaskDto> commPools, String internalRemarks) {
        if(!StringUtil.isEmpty(inspectionTaskPoolListDto.getTaskId())) {
            TaskDto taskDto = taskService.getTaskById(inspectionTaskPoolListDto.getTaskId());
            ApplicationViewDto applicationViewDto = inspectionAssignTaskService.searchByAppCorrId(taskDto.getRefNo());
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            //create history, update application, update/create inspection status
            String saveFlag = assignTaskForInspectors(inspectionTaskPoolListDto, commPools, internalRemarks, applicationDto, taskDto, applicationViewDto);
            if (!StringUtil.isEmpty(inspectionTaskPoolListDto.getInspManHours()) && AppConsts.SUCCESS.equals(saveFlag)) {
                //create inspManHours recommendation or update
                AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(taskDto.getRefNo(), InspectionConstants.RECOM_TYPE_INSP_MAN_HOUR).getEntity();
                if (appPremisesRecommendationDto != null) {
                    appPremisesRecommendationDto.setRecomDecision(inspectionTaskPoolListDto.getInspManHours());
                    appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                } else {
                    appPremisesRecommendationDto = new AppPremisesRecommendationDto();
                    appPremisesRecommendationDto.setAppPremCorreId(taskDto.getRefNo());
                    appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    appPremisesRecommendationDto.setVersion(1);
                    appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSP_MAN_HOUR);
                    appPremisesRecommendationDto.setRecomDecision(inspectionTaskPoolListDto.getInspManHours());
                    appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    fillUpCheckListGetAppClient.saveAppRecom(appPremisesRecommendationDto);
                }
            }
            return saveFlag;
        } else {
            return null;
        }
    }

    @Override
    public List<String> getWorkGroupIdsByLogin(LoginContext loginContext) {
        List<String> workGroupIdList = IaisCommonUtils.genNewArrayList();
        List<UserGroupCorrelationDto> userGroupCorrelationDtos = organizationClient.getUserGroupLeadByUserId(loginContext.getUserId()).getEntity();
        String roleId = loginContext.getCurRoleId();
        if(!IaisCommonUtils.isEmpty(userGroupCorrelationDtos)) {
            for (UserGroupCorrelationDto ugcDto : userGroupCorrelationDtos) {
                if (ugcDto != null) {
                    if (AppConsts.COMMON_STATUS_ACTIVE.equals(ugcDto.getStatus())) {
                        WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(ugcDto.getGroupId()).getEntity();
                        String groupName = workingGroupDto.getGroupName();
                        if (RoleConsts.USER_ROLE_INSPECTION_LEAD.equals(roleId) && (groupName.contains("Inspection") && !groupName.contains("Approval"))) {
                            workGroupIdList.add(ugcDto.getGroupId());
                            continue;
                        }
                        if (RoleConsts.USER_ROLE_ASO_LEAD.equals(roleId) && (groupName.contains("Admin Screening officer"))) {
                            workGroupIdList.add(ugcDto.getGroupId());
                            continue;
                        }
                        if (RoleConsts.USER_ROLE_PSO_LEAD.equals(roleId) && (groupName.contains("Professional"))) {
                            workGroupIdList.add(ugcDto.getGroupId());
                            continue;
                        }
                        if (RoleConsts.USER_ROLE_AO1_LEAD.equals(roleId) && (groupName.contains("Level 1 Approval"))) {
                            workGroupIdList.add(ugcDto.getGroupId());
                            continue;
                        }
                        if (RoleConsts.USER_ROLE_AO2_LEAD.equals(roleId) && (groupName.contains("Level 2 Approval"))) {
                            workGroupIdList.add(ugcDto.getGroupId());
                            continue;
                        }
                        if (RoleConsts.USER_ROLE_AO3_LEAD.equals(roleId) && (groupName.contains("Level 3 Approval"))) {
                            workGroupIdList.add(ugcDto.getGroupId());
                            continue;
                        }
                    }
                }
            }
        }
        return workGroupIdList;
    }

    @Override
    public InspectionTaskPoolListDto inputInspectorOption(InspectionTaskPoolListDto inspectionTaskPoolListDto, LoginContext loginContext) {
        List<String> roles = loginContext.getRoleIds();
        List<String> roleList = new ArrayList<>(roles);
        inspectionTaskPoolListDto.setCurRole(loginContext.getCurRoleId());
        inspectionTaskPoolListDto.setRoles(roleList);
        inspectionTaskPoolListDto.setLoginContextId(loginContext.getUserId());
        inspectionTaskPoolListDto = organizationClient.filterInspectorOption(inspectionTaskPoolListDto).getEntity();
        return inspectionTaskPoolListDto;
    }

    @Override
    public List<TaskDto> getReassignPoolByGroupWordId(String workGroupId) {
        List<TaskDto> reassignTasks = IaisCommonUtils.genNewArrayList();
        if(workGroupId!=null){
            reassignTasks = organizationClient.getReassignTaskByWkId(workGroupId).getEntity();
        }
        return reassignTasks;
    }

    @Override
    public InspectionTaskPoolListDto reassignInspectorOption(InspectionTaskPoolListDto inspectionTaskPoolListDto, LoginContext loginContext,String taskId){
        List<String> roles = loginContext.getRoleIds();
        List<String> roleList = new ArrayList<>(roles);
        inspectionTaskPoolListDto.setCurRole(loginContext.getCurRoleId());
        inspectionTaskPoolListDto.setRoles(roleList);
        inspectionTaskPoolListDto.setLoginContextId(loginContext.getUserId());
        List<SelectOption> inspectorOption = IaisCommonUtils.genNewArrayList();
        TaskDto taskDto = organizationClient.getTaskById(taskId).getEntity();
        List<OrgUserDto> orgUserDtoList = organizationClient.activeUsersByWorkGroupAndRole(inspectionTaskPoolListDto.getWorkGroupId(), taskDto.getRoleId()).getEntity();
        String userId = taskDto.getUserId();
        for(OrgUserDto oDto:orgUserDtoList){
            if(!(oDto.getId().equals(userId))){
                Boolean available = oDto.getAvailable();
                if(available){
                    SelectOption so = new SelectOption(oDto.getId(), oDto.getDisplayName());
                    inspectorOption.add(so);
                }
            }
        }
        inspectionTaskPoolListDto.setInspectorOption(inspectorOption);
        return inspectionTaskPoolListDto;
    }

    @Override
    public String getMemberValueByWorkGroupUserId(String userId) {
        String memberValue = "";
        if(!StringUtil.isEmpty(userId)) {
            List<TaskDto> taskDtoList = organizationClient.getTasksByUserId(userId).getEntity();
            if (!IaisCommonUtils.isEmpty(taskDtoList)) {
                memberValue = getOptionValue(taskDtoList);
            }
        }
        return memberValue;
    }

    @Override
    public SearchResult<InspectionSubPoolQueryDto> getGroupLeadName(SearchResult<InspectionSubPoolQueryDto> searchResult, LoginContext loginContext) {
        if(!IaisCommonUtils.isEmpty(searchResult.getRows())){
            for(InspectionSubPoolQueryDto iDto : searchResult.getRows()){
                if(iDto != null) {
                    String workingGroupId = iDto.getWorkGroupId();
                    if (!StringUtil.isEmpty(workingGroupId)) {
                        List<OrgUserDto> orgUserDtoList = organizationClient.getUsersByWorkGroupName(workingGroupId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
                        List<String> leadName = getWorkGroupLeadsByGroupId(workingGroupId, orgUserDtoList);
                        iDto.setGroupLead(leadName);
                    }
                }
            }
        }
        return searchResult;
    }

    @Override
    @SearchTrack(catalog = "inspectionQuery", key = "supervisorPoolDropdown")
    public SearchResult<SuperPoolTaskQueryDto> getSupPoolSecondByParam(SearchParam searchParam) {
        return organizationClient.supervisorSecondSearch(searchParam).getEntity();
    }

    @Override
    public SearchResult<SuperPoolTaskQueryDto> getSecondSearchOtherData(SearchResult<SuperPoolTaskQueryDto> searchResult, HcsaTaskAssignDto hcsaTaskAssignDto) {
        List<SuperPoolTaskQueryDto> superPoolTaskQueryDtos = searchResult.getRows();
        if(!IaisCommonUtils.isEmpty(superPoolTaskQueryDtos)) {
            for (SuperPoolTaskQueryDto superPoolTaskQueryDto : superPoolTaskQueryDtos) {
                //set maskId
                superPoolTaskQueryDto.setMaskId(MaskUtil.maskValue("taskId", superPoolTaskQueryDto.getId()));
                //get memberName
                String userId = superPoolTaskQueryDto.getUserId();
                String memberName = getMemberNameByUserId(userId);
                superPoolTaskQueryDto.setMemberName(memberName);
                //get Application data
                ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(superPoolTaskQueryDto.getTaskRefNo()).getEntity();
                superPoolTaskQueryDto.setAppNo(applicationDto.getApplicationNo());
                if(StringUtil.isEmpty(memberName) || HcsaConsts.HCSA_PREMISES_HCI_NULL.equals(memberName)) {
                    superPoolTaskQueryDto.setAppStatus(MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT));
                } else {
                    superPoolTaskQueryDto.setAppStatus(MasterCodeUtil.getCodeDesc(applicationDto.getStatus()));
                }
                //get HCI data
                AppGrpPremisesDto appGrpPremisesDto = inspectionAssignTaskService.getAppGrpPremisesDtoByAppGroId(superPoolTaskQueryDto.getTaskRefNo());
                String address = inspectionAssignTaskService.getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
                superPoolTaskQueryDto.setHciCode(appGrpPremisesDto.getHciCode());
                if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                    superPoolTaskQueryDto.setHciAddress(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
                } else {
                    superPoolTaskQueryDto.setHciAddress(StringUtil.viewHtml(address));
                }
                //get inspection date
                AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(superPoolTaskQueryDto.getTaskRefNo(), InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
                if(appPremisesRecommendationDto != null){
                    superPoolTaskQueryDto.setInspectionDate(appPremisesRecommendationDto.getRecomInDate());
                    String inspDateStr = Formatter.formatDateTime(appPremisesRecommendationDto.getRecomInDate(), AppConsts.DEFAULT_DATE_FORMAT);
                    superPoolTaskQueryDto.setInspectionDateStr(inspDateStr);
                } else {
                    superPoolTaskQueryDto.setInspectionDate(null);
                    superPoolTaskQueryDto.setInspectionDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                }
                //get license date
                if(StringUtil.isEmpty(applicationDto.getOriginLicenceId())){
                    superPoolTaskQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                } else {
                    LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(applicationDto.getOriginLicenceId()).getEntity();
                    Date licExpiryDate = licenceDto.getExpiryDate();
                    if(licExpiryDate != null) {
                        superPoolTaskQueryDto.setLicenceExpiryDate(licExpiryDate);
                        String licExpiryDateStr = Formatter.formatDateTime(licExpiryDate, AppConsts.DEFAULT_DATE_FORMAT);
                        superPoolTaskQueryDto.setLicenceExpiryDateStr(licExpiryDateStr);
                    } else {
                        superPoolTaskQueryDto.setLicenceExpiryDate(null);
                        superPoolTaskQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                    }
                }
            }
        }
        return searchResult;
    }

    @Override
    public InspectionTaskPoolListDto getDataForAssignTask(Map<String, SuperPoolTaskQueryDto> assignMap, InspectionTaskPoolListDto inspectionTaskPoolListDto, String taskId) {
        SuperPoolTaskQueryDto superPoolTaskQueryDto = assignMap.get(taskId);
        ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(superPoolTaskQueryDto.getTaskRefNo()).getEntity();
        HcsaServiceDto hcsaServiceDto = getHcsaServiceDtoByServiceId(applicationDto.getServiceId());
        ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();
        TaskDto taskDto = taskService.getTaskById(taskId);
        //set leaders' name
        List<String> leadName =IaisCommonUtils.genNewArrayList();
        if(taskDto != null && !StringUtil.isEmpty(taskDto.getWkGrpId())){
            List<OrgUserDto> orgUserDtos = organizationClient.getUsersByWorkGroupName(taskDto.getWkGrpId(), AppConsts.COMMON_STATUS_ACTIVE).getEntity();
            leadName = getWorkGroupLeadsByGroupId(taskDto.getWkGrpId(), orgUserDtos);
            Set<String> leadNameSet = new HashSet<>(leadName);
            leadName = new ArrayList<>(leadNameSet);
            inspectionTaskPoolListDto.setInspectorLeads(leadName);
            String leadersStr = setLeadersStrShow(leadName);
            inspectionTaskPoolListDto.setGroupLeadersShow(leadersStr);
        }
        //set task data
        inspectionTaskPoolListDto.setTaskId(taskId);
        inspectionTaskPoolListDto.setTaskDto(taskDto);
        inspectionTaskPoolListDto.setWorkGroupId(superPoolTaskQueryDto.getWorkGroupId());
        //set application data
        if(taskDto != null && !StringUtil.isEmpty(taskDto.getUserId())) {
            inspectionTaskPoolListDto.setApplicationStatus(applicationDto.getStatus());
        } else {
            inspectionTaskPoolListDto.setApplicationStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT);
        }
        inspectionTaskPoolListDto.setApplicationNo(applicationDto.getApplicationNo());
        inspectionTaskPoolListDto.setApplicationType(applicationDto.getApplicationType());
        inspectionTaskPoolListDto.setSubmitDt(applicationGroupDto.getSubmitDt());
        inspectionTaskPoolListDto.setServiceName(hcsaServiceDto.getSvcName());
        inspectionTaskPoolListDto.setHciCode(superPoolTaskQueryDto.getHciCode());
        inspectionTaskPoolListDto.setHciName(superPoolTaskQueryDto.getHciAddress());
        //todo: get authentic Inspection Type
        inspectionTaskPoolListDto.setInspectionTypeName(InspectionConstants.INSPECTION_TYPE_ONSITE);

        //save leaders in recommendation
        if(taskDto!= null && StringUtil.isEmpty(taskDto.getUserId()) && ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(applicationDto.getStatus())) {
            String appPremCorrId = taskDto.getRefNo();
            AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrId, InspectionConstants.RECOM_TYPE_INSPECTION_LEAD).getEntity();
            if (appPremisesRecommendationDto == null) {
                if (!IaisCommonUtils.isEmpty(leadName)) {
                    String nameStr = "";
                    for (String name : leadName) {
                        if (StringUtil.isEmpty(nameStr)) {
                            nameStr = name;
                        } else {
                            nameStr = nameStr + "," + name;
                        }
                    }
                    appPremisesRecommendationDto = new AppPremisesRecommendationDto();
                    appPremisesRecommendationDto.setAppPremCorreId(appPremCorrId);
                    appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    appPremisesRecommendationDto.setVersion(1);
                    appPremisesRecommendationDto.setRecomInDate(null);
                    appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPECTION_LEAD);
                    appPremisesRecommendationDto.setRecomDecision(nameStr);
                    appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    fillUpCheckListGetAppClient.saveAppRecom(appPremisesRecommendationDto);
                }
            }
        }
        return inspectionTaskPoolListDto;
    }

    private String setLeadersStrShow(List<String> leadName) {
        if(leadName != null){
            StringBuilder leadStrBu = new StringBuilder();
            Collections.sort(leadName);
            for(String lead : leadName){
                if(StringUtil.isEmpty(leadStrBu.toString())) {
                    leadStrBu.append(lead);
                } else {
                    leadStrBu.append(',');
                    leadStrBu.append(' ');
                    leadStrBu.append(lead);
                }
            }
            return leadStrBu.toString();
        }
        return "";
    }

    @Override
    public List<String> getUserIdByWorkGrpId(String workGrpId) {
        List<String> userIds = IaisCommonUtils.genNewArrayList();
        List<OrgUserDto> orgUserDtos = organizationClient.getUsersByWorkGroupName(workGrpId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
        if(!IaisCommonUtils.isEmpty(orgUserDtos)) {
            for (OrgUserDto orgUserDto : orgUserDtos) {
                if(orgUserDto != null){
                    if(!StringUtil.isEmpty(orgUserDto.getId())){
                        userIds.add(orgUserDto.getId());
                    }
                }
            }
        }
        return userIds;
    }

    @Override
    public List<String> getWorkIdsByLogin(LoginContext loginContext) {
        List<String> roleIds = loginContext.getRoleIds();
        List<String> workGroupIdList = IaisCommonUtils.genNewArrayList();
        List<UserGroupCorrelationDto> userGroupCorrelationDtos = organizationClient.getUserGroupCorreByUserId(loginContext.getUserId()).getEntity();
        for(UserGroupCorrelationDto ugcDto:userGroupCorrelationDtos){
            WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(ugcDto.getGroupId()).getEntity();
            String groupName = workingGroupDto.getGroupName();
            if((roleIds.contains(RoleConsts.USER_ROLE_INSPECTION_LEAD)||roleIds.contains(RoleConsts.USER_ROLE_INSPECTIOR))&&(groupName.contains("Inspection") && !groupName.contains("Approval"))){
                workGroupIdList.add(ugcDto.getGroupId());
                continue;
            }
            if((roleIds.contains(RoleConsts.USER_ROLE_ASO_LEAD)||roleIds.contains(RoleConsts.USER_ROLE_ASO))&&(groupName.contains("Admin Screening officer"))){
                workGroupIdList.add(ugcDto.getGroupId());
                continue;
            }
            if((roleIds.contains(RoleConsts.USER_ROLE_PSO_LEAD)||roleIds.contains(RoleConsts.USER_ROLE_PSO))&&(groupName.contains("Professional"))){
                workGroupIdList.add(ugcDto.getGroupId());
                continue;
            }
            if((roleIds.contains(RoleConsts.USER_ROLE_AO1_LEAD)||roleIds.contains(RoleConsts.USER_ROLE_AO1))&&(groupName.contains("Level 1 Approval"))){
                workGroupIdList.add(ugcDto.getGroupId());
                continue;
            }
            if((roleIds.contains(RoleConsts.USER_ROLE_AO2_LEAD)||roleIds.contains(RoleConsts.USER_ROLE_AO2))&&(groupName.contains("Level 2 Approval"))){
                workGroupIdList.add(ugcDto.getGroupId());
                continue;
            }
            if((roleIds.contains(RoleConsts.USER_ROLE_AO3_LEAD)||roleIds.contains(RoleConsts.USER_ROLE_AO3))&&(groupName.contains("Level 3 Approval"))){
                workGroupIdList.add(ugcDto.getGroupId());
                continue;
            }
        }
        if(!IaisCommonUtils.isEmpty(workGroupIdList)) {
            Set<String> workGroupIdSet = new HashSet<>(workGroupIdList);
            workGroupIdList = new ArrayList<>(workGroupIdSet);
        }
        return workGroupIdList;
    }

    @Override
    public List<TaskDto> filterCommonPoolTask(List<TaskDto> superPool) {
        if(IaisCommonUtils.isEmpty(superPool)){
            return superPool;
        } else {
            List<TaskDto> commonPool = IaisCommonUtils.genNewArrayList();
            for(TaskDto taskDto : superPool){
                if(taskDto != null){
                    if(StringUtil.isEmpty(taskDto.getUserId())){
                        commonPool.add(taskDto);
                    }
                }
            }
            return commonPool;
        }
    }

    @Override
    public HcsaTaskAssignDto getHcsaTaskAssignDtoByAppGrp(List<String> appGroupIds) {
        if(!IaisCommonUtils.isEmpty(appGroupIds)) {
            HcsaTaskAssignDto hcsaTaskAssignDto = appPremisesCorrClient.getUnitNoAndAddressByAppGrpIds(appGroupIds).getEntity();
            return hcsaTaskAssignDto;
        }
        return null;
    }

    @Override
    public List<String> getSuperPoolAppGrpIdByResult(SearchResult<InspectionSubPoolQueryDto> searchResult) {
        if(searchResult != null && !IaisCommonUtils.isEmpty(searchResult.getRows())) {
            List<String> appGrpIds = IaisCommonUtils.genNewArrayList();
            List<InspectionSubPoolQueryDto> inspectionSubPoolQueryDtos = searchResult.getRows();
            for(InspectionSubPoolQueryDto inspectionSubPoolQueryDto : inspectionSubPoolQueryDtos) {
                if(inspectionSubPoolQueryDto != null) {
                    appGrpIds.add(inspectionSubPoolQueryDto.getId());
                }
            }
            return appGrpIds;
        }
        return null;
    }

    private String getMemberNameByUserId(String userId) {
        String memberName = HcsaConsts.HCSA_PREMISES_HCI_NULL;
        if(!StringUtil.isEmpty(userId)) {
            OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
            memberName = orgUserDto.getDisplayName();
        }
        return memberName;
    }

    private InspectionSubPoolQueryDto getLeadByAppPremCorrId(TaskDto taskDto, List<AppPremisesCorrelationDto> appPremisesCorrelationDtos, InspectionSubPoolQueryDto iDto) {
        if(!IaisCommonUtils.isEmpty(appPremisesCorrelationDtos)) {
            String refNo = taskDto.getRefNo();
            for(AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos) {
                log.debug(StringUtil.changeForLog("==============="+refNo));
                if(appPremisesCorrelationDto != null) {
                    if (appPremisesCorrelationDto.getId().equals(taskDto.getRefNo())) {
                        List<OrgUserDto> orgUserDtos = organizationClient.getUsersByWorkGroupName(taskDto.getWkGrpId(), AppConsts.COMMON_STATUS_ACTIVE).getEntity();
                        List<String> leadName = getWorkGroupLeadsByGroupId(taskDto.getWkGrpId(), orgUserDtos);
                        Set<String> leadNameSet = new HashSet<>(leadName);
                        leadName = new ArrayList<>(leadNameSet);
                        iDto.setGroupLead(leadName);
                    }
                }
            }
        }
        return iDto;
    }

    @Override
    public GroupRoleFieldDto getInspectorOptionByLogin(LoginContext loginContext, List<String> workGroupIds, GroupRoleFieldDto groupRoleFieldDto) {
        List<SelectOption> inspectorOption = IaisCommonUtils.genNewArrayList();
        Map<String, String> userIdMap = IaisCommonUtils.genNewHashMap();
        if(IaisCommonUtils.isEmpty(workGroupIds)){
            return null;
        }
        List<String> userIdList = IaisCommonUtils.genNewArrayList();
        for(String workId:workGroupIds){
            List<OrgUserDto> orgUserDtoList = organizationClient.getUsersByWorkGroupName(workId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
            userIdList = getUserIdList(orgUserDtoList, userIdList);
        }
        List<OrgUserDto> orgUserDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(userIdList)){
            orgUserDtos = organizationClient.retrieveOrgUserAccount(userIdList).getEntity();
        }
        if(orgUserDtos != null && !(orgUserDtos.isEmpty())){
            for(int i = 0; i < orgUserDtos.size(); i++){
                userIdMap.put(i + "", orgUserDtos.get(i).getId());
                SelectOption so = new SelectOption(i + "", orgUserDtos.get(i).getDisplayName());
                inspectorOption.add(so);
            }
        }
        groupRoleFieldDto.setUserIdMap(userIdMap);
        groupRoleFieldDto.setMemberOption(inspectorOption);
        return groupRoleFieldDto;
    }

    private String getOptionValue(List<TaskDto> taskDtoList) {
        StringBuilder value = new StringBuilder();
        value.append(taskDtoList.get(0).getRefNo());
        taskDtoList.remove(0);
        if(!IaisCommonUtils.isEmpty(taskDtoList)) {
            for (TaskDto tDto : taskDtoList) {
                value.append(',').append(tDto.getRefNo());
            }
        }
        return value.toString();
    }

    private List<String> getUserIdList(List<OrgUserDto> orgUserDtoList, List<String> userIdList) {
        for(OrgUserDto oDto:orgUserDtoList){
            userIdList.add(oDto.getId());
        }
        return userIdList;
    }

    @Override
    public String assignTaskForInspectors(InspectionTaskPoolListDto inspectionTaskPoolListDto, List<TaskDto> commPools, String internalRemarks,
                                        ApplicationDto applicationDto, TaskDto taskDto, ApplicationViewDto applicationViewDto) {
        List<SelectOption> inspectorCheckList = inspectionTaskPoolListDto.getInspectorCheck();
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, taskDto.getTaskKey());
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        inspectionTaskPoolListDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
        String appStatus = applicationDto.getStatus();
        for(TaskDto td:commPools) {
            if (td.getId().equals(inspectionTaskPoolListDto.getTaskId())) {
                if(StringUtil.isEmpty(td.getUserId())){
                    if(ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL.equals(appStatus) ||
                            ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT.equals(appStatus)){
                        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
                        List<String> taskUserIds = IaisCommonUtils.genNewArrayList();
                        for (SelectOption so : inspectionTaskPoolListDto.getInspectorCheck()) {
                            taskUserIds.add(so.getValue());
                        }
                        ApplicationGroupDto applicationGroupDto = applicationViewDto.getApplicationGroupDto();
                        String saveFlag = inspectionAssignTaskService.assignReschedulingTask(td, taskUserIds, applicationDtos, auditTrailDto, applicationGroupDto, null, null);
                        return saveFlag;
                    } else {
                        td.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
                        td.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                        inspectionTaskPoolListDto.setTaskDto(td);
                        inspectionTaskPoolListDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                        inspectionTaskPoolListDto.setTaskDtos(commPools);
                        inspectionTaskPoolListDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
                        inspectionTaskPoolListDto = organizationClient.assignSupTasks(inspectionTaskPoolListDto).getEntity();
                        if(inspectionTaskPoolListDto == null) {
                            return AppConsts.FAIL;
                        } else {
                            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorySubStage(td.getRefNo(), td.getTaskKey()).getEntity();
                            createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), internalRemarks, InspectionConstants.PROCESS_DECI_SUPER_USER_POOL_ASSIGN, td.getRoleId(), appPremisesRoutingHistoryDto.getSubStage(), td.getWkGrpId());
                            if (inspectorCheckList != null && inspectorCheckList.size() > 0) {
                                if (ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(applicationDto.getStatus())) {
                                    ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING);
                                    applicationService.updateFEApplicaiton(applicationDto1);
                                    inspectionTaskPoolListDto.setApplicationStatus(applicationDto1.getStatus());
                                    createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(), applicationDto1.getStatus(), taskDto.getTaskKey(), null, null, td.getRoleId(), null, td.getWkGrpId());
                                } else {
                                    createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), null, null, td.getRoleId(), appPremisesRoutingHistoryDto.getSubStage(), td.getWkGrpId());
                                }
                            }
                        }
                    }
                } else {
                    td.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
                    td.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    inspectionTaskPoolListDto.setTaskDto(td);
                    inspectionTaskPoolListDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    inspectionTaskPoolListDto.setTaskDtos(commPools);
                    inspectionTaskPoolListDto = organizationClient.assignSupTasks(inspectionTaskPoolListDto).getEntity();
                    if(inspectionTaskPoolListDto == null) {
                        return AppConsts.FAIL;
                    } else {
                        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorySubStage(td.getRefNo(), td.getTaskKey()).getEntity();
                        createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), internalRemarks, InspectionConstants.PROCESS_DECI_SUPER_USER_POOL_ASSIGN, td.getRoleId(), appPremisesRoutingHistoryDto.getSubStage(), td.getWkGrpId());
                        if (inspectorCheckList != null && inspectorCheckList.size() > 0) {
                            createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), null, null, td.getRoleId(), appPremisesRoutingHistoryDto.getSubStage(), td.getWkGrpId());
                        }
                    }
                }
            }
        }
        return AppConsts.SUCCESS;
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

    private TaskDto getTaskDtoByPool(List<TaskDto> commPools, InspectionTaskPoolListDto inspectionTaskPoolListDto) {
        TaskDto taskDto = new TaskDto();
        for(TaskDto tDto:commPools){
            if(tDto.getId().equals(inspectionTaskPoolListDto.getTaskId())){
                taskDto = tDto;
                return taskDto;
            }
        }
        return taskDto;
    }

    private ApplicationDto updateApplication(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationViewService.updateApplicaiton(applicationDto);
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

    @Override
    @SearchTrack(catalog = "inspectionQuery",key = "supervisorPoolSearch")
    public SearchResult<InspectionSubPoolQueryDto> getSupPoolByParam(SearchParam searchParam) {
        SearchResult<InspectionSubPoolQueryDto> searchResult = inspectionTaskClient.searchInspectionSupPool(searchParam).getEntity();
        return searchResult;
    }

    private List<String> getWorkGroupLeadsByGroupId(String workGroupId, List<OrgUserDto> orgUserDtos) {
        List<String> leadName = IaisCommonUtils.genNewArrayList();
        if(!(StringUtil.isEmpty(workGroupId))){
            List<String> leadIds = organizationClient.getInspectionLead(workGroupId).getEntity();
            for(String id : leadIds){
                for(OrgUserDto oDto : orgUserDtos){
                    if(id.equals(oDto.getId())){
                        leadName.add(oDto.getDisplayName());
                    }
                }
            }
        }
        return leadName;
    }

    @Override
    public List<SelectOption> getCheckInspector(String[] nameValue, InspectionTaskPoolListDto inspectionTaskPoolListDto) {
        List<SelectOption> inspectorCheckList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < nameValue.length; i++) {
            for (SelectOption so : inspectionTaskPoolListDto.getInspectorOption()) {
                getInNameBySelectOption(inspectorCheckList, nameValue[i], so);
            }
        }
        return inspectorCheckList;
    }

    private void getInNameBySelectOption(List<SelectOption> nameList, String s, SelectOption so) {
        if(s.equals(so.getValue())){
            nameList.add(so);
        }
    }
}
