package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
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
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<TaskDto> getTasksByUserId(String userId) {
        return organizationClient.getTasksByUserId(userId).getEntity();
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
        Set<String> applicationNoSet = new HashSet<>();
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
    @Transactional(rollbackFor = Exception.class)
    public void routingTaskByPool(InspectionTaskPoolListDto inspectionTaskPoolListDto, List<TaskDto> commPools, String internalRemarks) {
        TaskDto taskDto = getTaskDtoByPool(commPools, inspectionTaskPoolListDto);
        ApplicationViewDto applicationViewDto = inspectionAssignTaskService.searchByAppNo(inspectionTaskPoolListDto.getApplicationNo());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        //create history, update application, update/create inspection status
        assignTaskForInspectors(inspectionTaskPoolListDto, commPools, internalRemarks, applicationDto, taskDto, applicationViewDto);

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
    public InspectionTaskPoolListDto inputInspectorOption(InspectionTaskPoolListDto inspectionTaskPoolListDto, LoginContext loginContext) {
        List<SelectOption> inspectorOption = IaisCommonUtils.genNewArrayList();
        List<OrgUserDto> orgUserDtoList = organizationClient.getUsersByWorkGroupName(inspectionTaskPoolListDto.getWorkGroupId(), AppConsts.COMMON_STATUS_ACTIVE).getEntity();
        String flag = AppConsts.FALSE;
        Set<String> roles = loginContext.getRoleIds();
        List<String> roleList = new ArrayList<>(roles);
        if(roleList.contains(RoleConsts.USER_ROLE_INSPECTIOR)){
            flag = AppConsts.TRUE;
        }
        for(OrgUserDto oDto:orgUserDtoList){
            if(!(oDto.getId().equals(loginContext.getUserId()))){
                SelectOption so = new SelectOption(oDto.getId(), oDto.getDisplayName());
                inspectorOption.add(so);
            } else {
                if(AppConsts.TRUE.equals(flag)){
                    SelectOption so = new SelectOption(oDto.getId(), oDto.getDisplayName());
                    inspectorOption.add(so);
                }
            }
        }
        inspectionTaskPoolListDto.setInspectorOption(inspectorOption);
        return inspectionTaskPoolListDto;
    }

    @Override
    public List<SelectOption> getInspectorOptionByLogin(LoginContext loginContext, List<String> workGroupIds) {
        List<SelectOption> inspectorOption = IaisCommonUtils.genNewArrayList();
        if(workGroupIds == null || workGroupIds.size() <= 0){
            return null;
        }
        List<String> userIdList = IaisCommonUtils.genNewArrayList();
        for(String workId:workGroupIds){
            List<OrgUserDto> orgUserDtoList = organizationClient.getUsersByWorkGroupName(workId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
            userIdList = getUserIdList(orgUserDtoList, userIdList);
        }
        List<OrgUserDto> orgUserDtos = IaisCommonUtils.genNewArrayList();
        if(userIdList != null && !(userIdList.isEmpty())){
            orgUserDtos = organizationClient.retrieveOrgUserAccount(userIdList).getEntity();
        }
        if(orgUserDtos != null && !(orgUserDtos.isEmpty())){
            for(OrgUserDto oDto:orgUserDtos){
                List<TaskDto> taskDtoList = organizationClient.getTasksByUserId(oDto.getId()).getEntity();
                String value = AppConsts.NO;
                if(taskDtoList != null && taskDtoList.size() > 0){
                    value = getOptionValue(taskDtoList);
                }
                SelectOption so = new SelectOption(value, oDto.getDisplayName());
                inspectorOption.add(so);
            }
        }

        return inspectorOption;
    }

    private String getOptionValue(List<TaskDto> taskDtoList) {
        String value = taskDtoList.get(0).getRefNo();
        taskDtoList.remove(0);
        if(taskDtoList != null && taskDtoList.size() > 0) {
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

    @Override
    @SearchTrack(catalog = "inspectionQuery",key = "assignInspectorSupper")
    public SearchResult<InspectionSubPoolQueryDto> getSupPoolByParam(SearchParam searchParam) {
        return inspectionTaskClient.searchInspectionSupPool(searchParam).getEntity();
    }

    @Override
    public SearchResult<InspectionTaskPoolListDto> getOtherDataForSr(SearchResult<InspectionSubPoolQueryDto> searchResult, List<TaskDto> commPools, LoginContext loginContext) {
        List<InspectionTaskPoolListDto> inspectionTaskPoolListDtoList = IaisCommonUtils.genNewArrayList();
        if(commPools == null || commPools.size() <= 0){
            return null;
        }
        for(TaskDto tDto:commPools){
            InspectionTaskPoolListDto inspectionTaskPoolListDto = new InspectionTaskPoolListDto();
            inspectionTaskPoolListDto.setApplicationNo(tDto.getRefNo());
            inspectionTaskPoolListDto.setTaskId(tDto.getId());
            if(StringUtil.isEmpty(tDto.getUserId())){
                inspectionTaskPoolListDto.setInspectorName("");
            } else {
                inspectionTaskPoolListDto.setInspector(tDto.getUserId());
                List<String> ids = IaisCommonUtils.genNewArrayList();
                ids.add(tDto.getUserId());
                List<OrgUserDto> orgUserDtos = organizationClient.retrieveOrgUserAccount(ids).getEntity();
                inspectionTaskPoolListDto.setInspectorName(orgUserDtos.get(0).getDisplayName());
            }
            inspectionTaskPoolListDto.setWorkGroupId(tDto.getWkGrpId());
            inspectionTaskPoolListDtoList.add(inspectionTaskPoolListDto);
        }
        List<String> ids = IaisCommonUtils.genNewArrayList();
        ids.add(loginContext.getUserId());
        List<OrgUserDto> orgUserDtos = organizationClient.retrieveOrgUserAccount(ids).getEntity();
        OrgUserDto orgUserDto = orgUserDtos.get(0);
        inspectionTaskPoolListDtoList = inputOtherData(searchResult.getRows(), inspectionTaskPoolListDtoList, orgUserDto);

        SearchResult<InspectionTaskPoolListDto> searchResult2 = new SearchResult<>();
        searchResult2.setRows(inspectionTaskPoolListDtoList);
        searchResult2.setRowCount(inspectionTaskPoolListDtoList.size());
        return searchResult2;
    }

    private List<InspectionTaskPoolListDto> inputOtherData(List<InspectionSubPoolQueryDto> rows, List<InspectionTaskPoolListDto> inspectionTaskPoolListDtoList, OrgUserDto orgUserDto) {
        for(InspectionSubPoolQueryDto iDto: rows){
            for(InspectionTaskPoolListDto itplDto:inspectionTaskPoolListDtoList){
                if((iDto.getApplicationNo()).equals(itplDto.getApplicationNo())){
                    itplDto.setServiceId(iDto.getServiceId());
                    HcsaServiceDto hcsaServiceDto = getHcsaServiceDtoByServiceId(iDto.getServiceId());
                    itplDto.setServiceName(hcsaServiceDto.getSvcName());
                    itplDto.setApplicationStatus(iDto.getApplicationStatus());
                    itplDto.setApplicationType(iDto.getApplicationType());
                    itplDto.setHciCode(iDto.getHciCode());
                    AppGrpPremisesDto appGrpPremisesDto = getAppGrpPremisesDtoByAppGroId(iDto.getId());
                    itplDto.setHciName(iDto.getHciName() + " / " + appGrpPremisesDto.getAddress());
                    itplDto.setSubmitDt(iDto.getSubmitDt());
                    itplDto.setApplicationType(iDto.getApplicationType());
                    itplDto.setInspectionTypeName(iDto.getInspectionType() == 0? "Post":"Pre");
                    itplDto.setServiceEndDate(hcsaServiceDto.getEndDate());
                    itplDto.setInspectionDate(new Date());
                    //itplDto.setInspectorLead(orgUserDto.getDisplayName());
                }
            }
        }
        return inspectionTaskPoolListDtoList;
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

    private void getInNameBySelectOption(List<SelectOption> nameList, String s, SelectOption so) {
        if(s.equals(so.getValue())){
            nameList.add(so);
        }
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
