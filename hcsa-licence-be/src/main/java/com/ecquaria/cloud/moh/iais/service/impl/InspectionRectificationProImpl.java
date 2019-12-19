package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionPreTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspectionRectificationProService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/18 14:27
 **/
@Service
public class InspectionRectificationProImpl implements InspectionRectificationProService {

    @Autowired
    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;

    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;

    @Override
    public AppPremisesRoutingHistoryDto getAppHistoryByTask(String appId, String stageId) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorysByAppIdAndStageId(appId, stageId).getEntity();
        if(appPremisesRoutingHistoryDto == null){
            appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        }
        return appPremisesRoutingHistoryDto;
    }

    @Override
    public List<SelectOption> getProcessRecDecOption() {
        String[] processDecArr = new String[]
                {InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION, InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION,
                 InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION_CONDITION};
        List<SelectOption> processDecOption = MasterCodeUtil.retrieveOptionsByCodes(processDecArr);
        return processDecOption;
    }

    @Override
    public void routingTaskToReport(TaskDto taskDto, InspectionPreTaskDto inspectionPreTaskDto, ApplicationViewDto applicationViewDto) {
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto.getStatus(),taskDto.getTaskKey(),inspectionPreTaskDto.getInternalMarks(), InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION);
        createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto.getStatus(),taskDto.getTaskKey(),inspectionPreTaskDto.getInternalMarks(), InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION_CONDITION);
        ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION);
        applicationViewDto.setApplicationDto(applicationDto1);
        createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto1.getStatus(), HcsaConsts.ROUTING_STAGE_INS,null, null);
        taskDto.setSlaRemainInDays(taskService.remainDays(taskDto));
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        taskService.updateTask(taskDto);
        updateInspectionStatus(applicationDto);
    }

    private void updateInspectionStatus(ApplicationDto applicationDto) {
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos =  appPremisesCorrClient.getAppPremisesCorrelationsByAppId(applicationDto.getId()).getEntity();
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremisesCorrelationDtos.get(0).getId()).getEntity();
        appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_INSPECTION);
        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appInspectionStatusClient.update(appInspectionStatusDto);
    }

    private ApplicationDto updateApplication(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationViewService.updateApplicaiton(applicationDto);
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus,
                                                                         String stageId, String internalRemarks, String processDec){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setProcessDecision(processDec);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto).getEntity();
        return appPremisesRoutingHistoryDto;
    }

}
