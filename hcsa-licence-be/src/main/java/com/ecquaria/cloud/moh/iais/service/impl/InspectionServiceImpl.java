package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
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
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.SuperPoolTaskQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Override
    public List<SelectOption> getAppTypeOption() {
        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION, ApplicationConsts.APPLICATION_TYPE_RENEWAL, ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE});
        return appTypeOption;
    }

    @Override
    public List<SelectOption> getAppStatusOption() {
        String[] statusStrs = new String[]{
                ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION,
                ApplicationConsts.APPLICATION_STATUS_APPROVED,
                ApplicationConsts.APPLICATION_STATUS_REJECTED,
                ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS,
                ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING,
                ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01,
                ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02,
                ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03
                                         };
        List<SelectOption> appStatusOption = MasterCodeUtil.retrieveOptionsByCodes(statusStrs);
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
            appCorrIdList.add(AppConsts.NO);
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
    @Transactional(rollbackFor = Exception.class)
    public void routingTaskByPool(InspectionTaskPoolListDto inspectionTaskPoolListDto, List<TaskDto> commPools, String internalRemarks) {
        TaskDto taskDto = getTaskDtoByPool(commPools, inspectionTaskPoolListDto);
        ApplicationViewDto applicationViewDto = inspectionAssignTaskService.searchByAppCorrId(taskDto.getRefNo());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        //create history, update application, update/create inspection status
        assignTaskForInspectors(inspectionTaskPoolListDto, commPools, internalRemarks, applicationDto, taskDto, applicationViewDto);
    }

    @Override
    public List<String> getWorkGroupIdsByLogin(LoginContext loginContext) {
        List<String> workGroupIdList = IaisCommonUtils.genNewArrayList();
        List<UserGroupCorrelationDto> userGroupCorrelationDtos = organizationClient.getUserGroupCorreByUserId(loginContext.getUserId()).getEntity();
        for(UserGroupCorrelationDto ugcDto:userGroupCorrelationDtos){
            if(ugcDto.getIsLeadForGroup() == 1){
                workGroupIdList.add(ugcDto.getGroupId());
            }
        }
        return workGroupIdList;
    }

    @Override
    public InspectionTaskPoolListDto inputInspectorOption(InspectionTaskPoolListDto inspectionTaskPoolListDto, LoginContext loginContext) {
        Set<String> roles = loginContext.getRoleIds();
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
        Set<String> roles = loginContext.getRoleIds();
        List<String> roleList = new ArrayList<>(roles);
        inspectionTaskPoolListDto.setCurRole(loginContext.getCurRoleId());
        inspectionTaskPoolListDto.setRoles(roleList);
        inspectionTaskPoolListDto.setLoginContextId(loginContext.getUserId());
        List<SelectOption> inspectorOption = IaisCommonUtils.genNewArrayList();
        List<OrgUserDto> orgUserDtoList = organizationClient.getUsersByWorkGroupName(inspectionTaskPoolListDto.getWorkGroupId(), AppConsts.COMMON_STATUS_ACTIVE).getEntity();
        TaskDto taskDto = organizationClient.getTaskById(taskId).getEntity();
        String userId = taskDto.getUserId();
        for(OrgUserDto oDto:orgUserDtoList){
            if(!(oDto.getId().equals(userId))){
                SelectOption so = new SelectOption(oDto.getId(), oDto.getDisplayName());
                inspectorOption.add(so);
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
    public SearchResult<InspectionSubPoolQueryDto> getGroupLeadName(SearchResult<InspectionSubPoolQueryDto> searchResult, LoginContext loginContext, List<TaskDto> superPool) {
        if(!IaisCommonUtils.isEmpty(searchResult.getRows())){
            for(InspectionSubPoolQueryDto iDto : searchResult.getRows()){
                List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationClient.getPremCorrDtoByAppGroupId(iDto.getId()).getEntity();
                if(!IaisCommonUtils.isEmpty(appPremisesCorrelationDtos) && !IaisCommonUtils.isEmpty(superPool)) {
                    for (TaskDto taskDto : superPool) {
                        iDto = getLeadByAppPremCorrId(taskDto, appPremisesCorrelationDtos, iDto);
                    }
                }
            }
        }
        return searchResult;
    }

    @Override
    public SearchResult<SuperPoolTaskQueryDto> getSupPoolSecondByParam(SearchParam searchParam) {
        return organizationClient.supervisorSecondSearch(searchParam).getEntity();
    }

    @Override
    public SearchResult<SuperPoolTaskQueryDto> getSecondSearchOtherData(SearchResult<SuperPoolTaskQueryDto> searchResult) {
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
                superPoolTaskQueryDto.setAppStatus(MasterCodeUtil.getCodeDesc(applicationDto.getStatus()));
                //get HCI data
                AppGrpPremisesDto appGrpPremisesDto = inspectionAssignTaskService.getAppGrpPremisesDtoByAppGroId(superPoolTaskQueryDto.getTaskRefNo());
                String address = inspectionAssignTaskService.getAddress(appGrpPremisesDto);
                superPoolTaskQueryDto.setHciCode(appGrpPremisesDto.getHciCode());
                if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                    superPoolTaskQueryDto.setHciAddress(appGrpPremisesDto.getHciName() + " / " + address);
                } else {
                    superPoolTaskQueryDto.setHciAddress(address);
                }
                //get inspection date
                AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(superPoolTaskQueryDto.getTaskRefNo(), InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
                if(appPremisesRecommendationDto != null){
                    superPoolTaskQueryDto.setInspectionDate(appPremisesRecommendationDto.getRecomInDate());
                    String inspDateStr = Formatter.formatDateTime(appPremisesRecommendationDto.getRecomInDate(), "dd/MM/yyyy");
                    superPoolTaskQueryDto.setInspectionDateStr(inspDateStr);
                } else {
                    superPoolTaskQueryDto.setInspectionDate(null);
                    superPoolTaskQueryDto.setInspectionDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                }
                //get license date
                if(StringUtil.isEmpty(applicationDto.getOriginLicenceId())){
                    superPoolTaskQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                } else {
                    LicenceDto licenceDto = hcsaLicenceClient.getLicenceDtoById(applicationDto.getOriginLicenceId()).getEntity();
                    Date licExpiryDate = licenceDto.getExpiryDate();
                    superPoolTaskQueryDto.setLicenceExpiryDate(licExpiryDate);
                    String licExpiryDateStr = Formatter.formatDateTime(appPremisesRecommendationDto.getRecomInDate(), "dd/MM/yyyy");
                    superPoolTaskQueryDto.setLicenceExpiryDateStr(licExpiryDateStr);
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
        List<OrgUserDto> orgUserDtos = organizationClient.getUsersByWorkGroupName(taskDto.getWkGrpId(), AppConsts.COMMON_STATUS_ACTIVE).getEntity();
        List<String> leadName = getWorkGroupLeadsByGroupId(taskDto.getWkGrpId(), orgUserDtos);
        Set<String> leadNameSet = new HashSet<>(leadName);
        leadName = new ArrayList<>(leadNameSet);

        inspectionTaskPoolListDto.setTaskId(taskId);
        inspectionTaskPoolListDto.setTaskDto(taskDto);
        inspectionTaskPoolListDto.setWorkGroupId(superPoolTaskQueryDto.getWorkGroupId());
        inspectionTaskPoolListDto.setInspectorLeads(leadName);
        inspectionTaskPoolListDto.setApplicationStatus(applicationDto.getStatus());
        inspectionTaskPoolListDto.setApplicationNo(applicationDto.getApplicationNo());
        inspectionTaskPoolListDto.setApplicationType(applicationDto.getApplicationType());
        inspectionTaskPoolListDto.setSubmitDt(applicationGroupDto.getSubmitDt());
        inspectionTaskPoolListDto.setServiceName(hcsaServiceDto.getSvcName());
        inspectionTaskPoolListDto.setHciCode(superPoolTaskQueryDto.getHciCode());
        inspectionTaskPoolListDto.setHciName(superPoolTaskQueryDto.getHciAddress());
        //todo: get authentic Inspection Type
        inspectionTaskPoolListDto.setInspectionTypeName(InspectionConstants.INSPECTION_TYPE_ONSITE);

        return inspectionTaskPoolListDto;
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
            for(AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos) {
                if (appPremisesCorrelationDto.getId().equals(taskDto.getRefNo())) {
                    List<OrgUserDto> orgUserDtos = organizationClient.getUsersByWorkGroupName(taskDto.getWkGrpId(), AppConsts.COMMON_STATUS_ACTIVE).getEntity();
                    List<String> leadName = getWorkGroupLeadsByGroupId(taskDto.getWkGrpId(), orgUserDtos);
                    Set<String> leadNameSet = new HashSet<>(leadName);
                    leadName = new ArrayList<>(leadNameSet);
                    iDto.setGroupLead(leadName);
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
        String value = taskDtoList.get(0).getRefNo();
        taskDtoList.remove(0);
        if(!IaisCommonUtils.isEmpty(taskDtoList)) {
            for (TaskDto tDto : taskDtoList) {
                value = value + "," + tDto.getRefNo();
            }
        }
        return value;
    }

    private List<String> getUserIdList(List<OrgUserDto> orgUserDtoList, List<String> userIdList) {
        for(OrgUserDto oDto:orgUserDtoList){
            userIdList.add(oDto.getId());
        }
        return userIdList;
    }

    @Override
    public void assignTaskForInspectors(InspectionTaskPoolListDto inspectionTaskPoolListDto, List<TaskDto> commPools,
                                        String internalRemarks, ApplicationDto applicationDto, TaskDto taskDto, ApplicationViewDto applicationViewDto) {
        try {
            List<SelectOption> inspectorCheckList = inspectionTaskPoolListDto.getInspectorCheck();
            List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
            applicationDtos.add(applicationDto);
            List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos,HcsaConsts.ROUTING_STAGE_INS);
            hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
            inspectionTaskPoolListDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
            for(TaskDto td:commPools) {
                if (td.getId().equals(inspectionTaskPoolListDto.getTaskId())) {
                    if(StringUtil.isEmpty(td.getUserId())){
                        td.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
                        td.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                        inspectionTaskPoolListDto.setTaskDto(td);
                        inspectionTaskPoolListDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                        inspectionTaskPoolListDto.setTaskDtos(commPools);
                        inspectionTaskPoolListDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
                        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorySubStage(td.getRefNo(), td.getTaskKey()).getEntity();
                        createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(),taskDto.getTaskKey(),internalRemarks, InspectionConstants.PROCESS_DECI_SUPER_USER_POOL_ASSIGN, td.getRoleId(), appPremisesRoutingHistoryDto.getSubStage(), td.getWkGrpId());
                        if(inspectorCheckList != null && inspectorCheckList.size() > 0){
                            for(int i = 0; i < inspectorCheckList.size(); i++){
                                if(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(applicationDto.getStatus())){
                                    //todo:set authentic Application status
                                    ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
                                    applicationService.updateFEApplicaiton(applicationDto1);
                                    inspectionTaskPoolListDto.setApplicationStatus(applicationDto1.getStatus());
                                    createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(), applicationDto1.getStatus(), taskDto.getTaskKey(), null, null, td.getRoleId(), null, td.getWkGrpId());
                                } else {
                                    createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), null, null, td.getRoleId(), appPremisesRoutingHistoryDto.getSubStage(), td.getWkGrpId());
                                }
                            }
                        }
                        organizationClient.assignSupTasks(inspectionTaskPoolListDto);
                    } else {
                        td.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
                        td.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                        inspectionTaskPoolListDto.setTaskDto(td);
                        inspectionTaskPoolListDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                        inspectionTaskPoolListDto.setTaskDtos(commPools);
                        organizationClient.assignSupTasks(inspectionTaskPoolListDto);
                        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorySubStage(td.getRefNo(), td.getTaskKey()).getEntity();
                        createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), internalRemarks, InspectionConstants.PROCESS_DECI_SUPER_USER_POOL_ASSIGN, td.getRoleId(), appPremisesRoutingHistoryDto.getSubStage(), td.getWkGrpId());
                        if(inspectorCheckList != null && inspectorCheckList.size() > 0){
                            for(int i = 0; i < inspectorCheckList.size(); i++){
                                createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), internalRemarks, null, td.getRoleId(), appPremisesRoutingHistoryDto.getSubStage(), td.getWkGrpId());
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

    private TaskDto getTaskDtoByPool(List<TaskDto> commPools, InspectionTaskPoolListDto inspectionTaskPoolListDto) {
        TaskDto taskDto = new TaskDto();
        for(TaskDto tDto:commPools){
            if(tDto.getId().equals(inspectionTaskPoolListDto.getTaskId())){
                taskDto = tDto;
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
