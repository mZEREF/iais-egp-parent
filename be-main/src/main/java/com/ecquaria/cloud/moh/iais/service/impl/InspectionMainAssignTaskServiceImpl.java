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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.BelicationViewMainService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.BeInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.BePremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.BelicationClient;
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
 * @date 2019/11/22 10:19
 **/
@Service
@Slf4j
public class InspectionMainAssignTaskServiceImpl implements InspectionMainAssignTaskService {
    @Autowired
    private InspectionTaskMainClient inspectionTaskClient;

    @Autowired
    private HcsaConfigMainClient hcsaConfigClient;

    @Autowired
    private OrganizationMainClient organizationClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private BelicationClient belicationClient;

    @Autowired
    private BeInspectionStatusClient beInspectionStatusClient;

    @Autowired
    private BePremisesRoutingHistoryClient bePremisesRoutingHistoryClient;

    @Autowired
    private BelicationViewMainService belicationViewService;

    @Override
    public List<TaskDto> getCommPoolByGroupWordId(LoginContext loginContext) {
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
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
        return null;
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
    public void assignTaskForInspectors(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto,
                                        ApplicationViewDto applicationViewDto, String internalRemarks, TaskDto taskDto) {
        try {
            List<SelectOption> inspectorCheckList = inspecTaskCreAndAssDto.getInspectorCheck();
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            for(TaskDto td:commPools) {
                if (td.getId().equals(inspecTaskCreAndAssDto.getTaskId())
                        && !IaisCommonUtils.isEmpty(inspectorCheckList)) {
                    if(!StringUtil.isEmpty(inspectorCheckList.get(0).getValue())){
                        td.setUserId(inspectorCheckList.get(0).getValue());
                    }
                        td.setDateAssigned(new Date());
                        td.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                        updateTask(td);
                    inspectorCheckList.remove(0);
                    createAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),taskDto.getTaskKey(),internalRemarks);
                    ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING);
                    applicationViewDto.setApplicationDto(applicationDto1);
                    createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(),applicationDto1.getStatus(), HcsaConsts.ROUTING_STAGE_INS,null);
                    if(inspectorCheckList != null && inspectorCheckList.size() > 0){
                        createTaskByInspectorList(inspectorCheckList, commPools, inspecTaskCreAndAssDto);
                        for(int i = 0; i < inspectorCheckList.size(); i++){
                            createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(),applicationDto1.getStatus(), HcsaConsts.ROUTING_STAGE_INS,null);
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
    public ApplicationViewDto searchByAppNo(String applicationNo) {
        return belicationClient.getAppViewByCorrelationId(applicationNo).getEntity();
    }

    @Override
    public AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String status, String stageId, String internalRemarks) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(status);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto = bePremisesRoutingHistoryClient.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto).getEntity();
        return appPremisesRoutingHistoryDto;
    }

    @Override
    public void routingTaskByCommonPool(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto, String internalRemarks) {
        TaskDto taskDto = getTaskDtoByPool(commPools, inspecTaskCreAndAssDto);
        ApplicationViewDto applicationViewDto = searchByAppNo(inspecTaskCreAndAssDto.getApplicationNo());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        beInspectionStatusClient.createAppInspectionStatusByAppDto(applicationDto);
        assignTaskForInspectors(commPools, inspecTaskCreAndAssDto, applicationViewDto, internalRemarks, taskDto);
    }

    @Override
    public HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId) {
        return hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
    }

    private ApplicationDto updateApplication(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return belicationViewService.updateApplicaiton(applicationDto);
    }

    private TaskDto getTaskDtoByPool(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto) {
        TaskDto taskDto = new TaskDto();
        for(TaskDto tDto:commPools){
            if(tDto.getId().equals(inspecTaskCreAndAssDto.getTaskId())){
                taskDto = tDto;
                break;
            }
        }
        return taskDto;
    }

    private void createTaskByInspectorList(List<SelectOption> inspectorCheckList, List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto) {
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
        for(SelectOption so : inspectorCheckList) {
            for (TaskDto td : commPools) {
                if(td.getId().equals(inspecTaskCreAndAssDto.getTaskId())){
                    td.setId(null);
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
      * @Date 2019/11/23
      * @Param: appGroupId
      * @return: AppGrpPremisesDto
      * @Descripation: get Application Group Premises By Application Id
      */
    public AppGrpPremisesDto getAppGrpPremisesDtoByAppGroId(String appCorrId){
        return inspectionTaskClient.getAppGrpPremisesDtoByAppGroId(appCorrId).getEntity();
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

    @Override
    public String taskRead(String taskId){
        return organizationClient.taskRead(taskId).getEntity();
    }
}
