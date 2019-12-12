package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
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
    private AppPremisesCorrClient appPremisesCorrClient;

    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;

    @Autowired
    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;

    @Autowired
    private ApplicationViewService applicationViewService;

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
    public InspecTaskCreAndAssDto getInspecTaskCreAndAssDto(String applicationNo, List<TaskDto> commPools, LoginContext loginContext) {
        if(StringUtil.isEmpty(applicationNo)){
            applicationNo = SystemParameterConstants.PARAM_FALSE;
        }
        List<OrgUserDto> orgUserDtos = new ArrayList<>();
        for(TaskDto tDto:commPools){
            if(applicationNo.equals(tDto.getRefNo())){
                orgUserDtos =  organizationClient.getUsersByWorkGroupName(tDto.getWkGrpId(), tDto.getTaskStatus()).getEntity();
            }
        }

        ApplicationDto applicationDto = getApplicationDtoByAppNo(applicationNo);
        AppGrpPremisesDto appGrpPremisesDto = getAppGrpPremisesDtoByAppGroId(applicationDto.getId());
        HcsaServiceDto hcsaServiceDto = getHcsaServiceDtoByServiceId(applicationDto.getServiceId());
        ApplicationGroupDto applicationGroupDto = getApplicationGroupDtoByAppGroId(applicationDto.getAppGrpId());

        InspecTaskCreAndAssDto inspecTaskCreAndAssDto = new InspecTaskCreAndAssDto();
        inspecTaskCreAndAssDto.setApplicationNo(applicationNo);
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
        setInspectorLeadName(inspecTaskCreAndAssDto, orgUserDtos, loginContext);
        return inspecTaskCreAndAssDto;
    }

    private void setInspectorLeadName(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, List<OrgUserDto> orgUserDtos, LoginContext loginContext) {
        String userId = loginContext.getUserId();
        for(OrgUserDto orgUserDto:orgUserDtos){
            if(userId.equals(orgUserDto.getId())){
                inspecTaskCreAndAssDto.setInspectionLead(orgUserDto.getUserName());
            }
        }
    }

    private void setInspectorByOrgUserDto(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, List<OrgUserDto> orgUserDtos, LoginContext loginContext) {
        if(orgUserDtos == null || orgUserDtos.size() <= 0){
            inspecTaskCreAndAssDto.setInspector(null);
            return;
        }
        List<SelectOption> inspectorList = new ArrayList<>();
        Set<String> roles = loginContext.getRoleIds();
        List<String> roleList = new ArrayList<>(roles);
        if(roleList.contains("inspectorLead")){
            addInspector(inspectorList, orgUserDtos, loginContext, roleList);
        } else {
            for(OrgUserDto oDto:orgUserDtos){
                if(oDto.getId().equals(loginContext.getUserId())){
                    SelectOption so = new SelectOption(oDto.getId(), oDto.getUserName());
                    inspectorList.add(so);
                }
            }
        }
        inspecTaskCreAndAssDto.setInspector(inspectorList);
    }

    private void addInspector(List<SelectOption> inspectorList, List<OrgUserDto> orgUserDtos, LoginContext loginContext, List<String> roleList) {
        String flag = AppConsts.FALSE;
        if(roleList.contains("inspector")){
            flag = AppConsts.TRUE;
        }
        for(OrgUserDto oDto:orgUserDtos){
            if(!(oDto.getId().equals(loginContext.getUserId()))){
                SelectOption so = new SelectOption(oDto.getId(), oDto.getUserName());
                inspectorList.add(so);
            } else {
                if(AppConsts.TRUE.equals(flag)){
                    SelectOption so = new SelectOption(oDto.getId(), oDto.getUserName());
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
    public List<SelectOption> getAppTypeOption() {
        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION, ApplicationConsts.APPLICATION_TYPE_RENEWAL, ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE});
        return appTypeOption;
    }

    @Override
    public List<SelectOption> getCheckInspector(String[] nameValue, InspecTaskCreAndAssDto inspecTaskCreAndAssDto) {
        List<SelectOption> inspectorCheckList = new ArrayList<>();
        for (int i = 0; i < nameValue.length; i++) {
            for (SelectOption so : inspecTaskCreAndAssDto.getInspector()) {
                getInNameBySelectOption(inspectorCheckList, nameValue[i], so);
            }
        }
        return inspectorCheckList;
    }

    @Override
    public void assignTaskForInspectors(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto,
                                        ApplicationViewDto applicationViewDto, String internalRemarks, TaskDto taskDto) {
        try {
            List<SelectOption> inspectorCheckList = inspecTaskCreAndAssDto.getInspectorCheck();
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            for(TaskDto td:commPools) {
                if (td.getId().equals(inspecTaskCreAndAssDto.getTaskId())) {
                    td.setUserId(inspectorCheckList.get(0).getValue());
                    td.setDateAssigned(new Date());
                    updateTask(td);
                    inspectorCheckList.remove(0);
                    createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto.getStatus(),taskDto.getTaskKey(),internalRemarks);
                    ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING);
                    applicationViewDto.setApplicationDto(applicationDto1);
                    createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto1.getStatus(), HcsaConsts.ROUTING_STAGE_INS,null);
                    if(inspectorCheckList != null && inspectorCheckList.size() > 0){
                        createTaskByInspectorList(inspectorCheckList, commPools, inspecTaskCreAndAssDto);
                        for(int i = 0; i < inspectorCheckList.size(); i++){
                            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto1.getStatus(), HcsaConsts.ROUTING_STAGE_INS,null);
                        }
                    }
                }
            }
        } catch (Exception e){
            log.error(StringUtil.changeForLog("Error when Submit Assign Task Project: "), e);
            throw e;
        }
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
    public ApplicationViewDto searchByAppNo(String applicationNo) {
        return applicationClient.getAppViewByNo(applicationNo).getEntity();
    }

    @Override
    public void createTaskStatus(ApplicationDto applicationDto) {
        List<AppPremisesCorrelationDto> appPremCorrDtoList = appPremisesCorrClient.getAppPremisesCorrelationsByAppId(applicationDto.getId()).getEntity();
        List<AppInspectionStatusDto> appInspectionStatusDtos = new ArrayList<>();
        AppInspectionStatusDto appInspectionStatusDto = new AppInspectionStatusDto();
        appInspectionStatusDto.setAppPremCorreId(appPremCorrDtoList.get(0).getId());
        appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
        appInspectionStatusDtos.add(appInspectionStatusDto);
        appInspectionStatusClient.create(appInspectionStatusDtos);
    }

    @Override
    public AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String status, String stageId, String internalRemarks) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(status);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto).getEntity();
        return appPremisesRoutingHistoryDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void routingTaskByCommonPool(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto, String internalRemarks) {
        TaskDto taskDto = getTaskDtoByPool(commPools, inspecTaskCreAndAssDto);
        ApplicationViewDto applicationViewDto = searchByAppNo(inspecTaskCreAndAssDto.getApplicationNo());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        createTaskStatus(applicationDto);
        assignTaskForInspectors(commPools, inspecTaskCreAndAssDto, applicationViewDto, internalRemarks, taskDto);
    }

    private ApplicationDto updateApplication(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        return applicationViewService.updateApplicaiton(applicationDto);
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

    private void createTaskByInspectorList(List<SelectOption> inspectorCheckList, List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto) {
        List<TaskDto> taskDtoList = new ArrayList<>();
        for(SelectOption so : inspectorCheckList) {
            for (TaskDto td : commPools) {
                if(td.getId().equals(inspecTaskCreAndAssDto.getTaskId())){
                    td.setId(null);
                    td.setUserId(so.getValue());
                    td.setDateAssigned(new Date());
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
      * @Date 2019/11/22
      * @Param: appNo
      * @return: ApplicationDto
      * @Descripation: get ApplicationDto By Application No.
      */
    public ApplicationDto getApplicationDtoByAppNo(String appNo){
        return inspectionTaskClient.getApplicationDtoByAppNo(appNo).getEntity();
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
