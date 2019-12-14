package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.*;

/**
 * @author weilu
 * date 2019/11/20 17:15
 */


@Delegator(value = "insReport")
@Slf4j
public class InsReportDelegator {

    @Autowired
    private InsRepService insRepService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;


    public void start(BaseProcessClass bpc) {

        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
        AuditTrailHelper.auditFunction("Inspection Report", "Assign Report");
    }

    public void inspectionReportInit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionReportInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", null);
    }


    public void inspectionReportPre(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>inspectionReportPre");
        String taskId = "46512333-7A16-EA11-BE7D-000C29F371DC";
        TaskDto taskDto = taskService.getTaskById(taskId);
        String appNo = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = insRepService.getApplicationViewDto(appNo);
        InspectionReportDto insRepDto = insRepService.getInsRepDto(appNo, applicationViewDto);
//        SelectOption so1 = new SelectOption("Reject", "Reject");
//        SelectOption so2 = new SelectOption("1Y", "1year");
//        SelectOption so3 = new SelectOption("2Y", "2year");
//        List<SelectOption> inspectionReportTypeOption = new ArrayList<>();
//        inspectionReportTypeOption.add(so1);
//        inspectionReportTypeOption.add(so2);
//        inspectionReportTypeOption.add(so3);
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
    }

    public void confirmData(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>confirmData");
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, "insRepDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String Remarks = ParamUtil.getRequestString(bpc.request, "remarks");
        String recommendation = ParamUtil.getRequestString(bpc.request, "recommendation");
        String riskRecommendation = ParamUtil.getRequestString(bpc.request, "riskRecommendation");
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        appPremisesRecommendationDto.setRemarks(Remarks);
        appPremisesRecommendationDto.setRecomType("report");

        if ("reject".equals(recommendation)) {
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
            appPremisesRecommendationDto.setRecomDecision("Rejection");
        }else if(StringUtil.isEmpty(recommendation)){
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
            appPremisesRecommendationDto.setRecomType(riskRecommendation);
            appPremisesRecommendationDto.setRecomDecision("approve");
        } else {
//            int i = Integer.parseInt(recommendation);
//            appPremisesRecommendationDto.setRecomInNumber(i);
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
        }
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        ParamUtil.setSessionAttr(bpc.request, "appPremisesRecommendationDto", appPremisesRecommendationDto);
    }

    public void inspectorReportSave(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the inspectorReportSave start ...."));
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, "insRepDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        AppPremisesRecommendationDto appPremisesRecommendationDto = (AppPremisesRecommendationDto)ParamUtil.getSessionAttr(bpc.request, "appPremisesRecommendationDto");
        String Remarks = ParamUtil.getRequestString(bpc.request, "remarks");
        String recommendation = ParamUtil.getRequestString(bpc.request, "recommendation");
        String riskRecommendation = ParamUtil.getRequestString(bpc.request, "riskRecommendation");
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        appPremisesRecommendationDto.setRemarks(Remarks);
        appPremisesRecommendationDto.setRecomType("report");

        if ("reject".equals(recommendation)) {
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
//            appPremisesRecommendationDto.setRecomDecision("Rejection");
            insRepService.saveRecommendation(appPremisesRecommendationDto);
        }else if(StringUtil.isEmpty(recommendation)){
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
//            appPremisesRecommendationDto.setRecomDecision("approve");
            appPremisesRecommendationDto.setRecomType(riskRecommendation);
            insRepService.saveRecommendation(appPremisesRecommendationDto);
        } else {
//            int i = Integer.parseInt(recommendation);
//            appPremisesRecommendationDto.setRecomInNumber(i);
            appPremisesRecommendationDto.setRecomDecision("change");
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
            insRepService.saveRecommendation(appPremisesRecommendationDto);
        }

        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        updateApplicaiton(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW);
        String status = applicationDto.getStatus();
        String taskKey = taskDto.getTaskKey();
        createAppPremisesRoutingHistory(appPremisesCorrelationId, status, taskKey);
        completedTask(taskDto);
        List<TaskDto> taskDtos = prepareTaskList(taskDto);
        taskService.createTasks(taskDtos);
        createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(), InspectionConstants.INSPECTION_STATUS_PENDING_AO_RESULT, taskKey);

    }
    /**
     * private utils
     */
    private int remainDays(TaskDto taskDto) {
        int result = 0;
        String resultStr = DurationFormatUtils.formatPeriod(taskDto.getDateAssigned().getTime(), taskDto.getSlaDateCompleted().getTime(), "d");
        log.debug(StringUtil.changeForLog("The resultStr is -->:") + resultStr);
        return result;
    }

    /**
     * @param applicationDto
     * @param appStatus      update application status
     */
    private ApplicationDto updateApplicaiton(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        return insRepService.updateApplicaiton(applicationDto);
    }

    /**
     * complete task
     *
     * @param taskDto
     * @return
     */
    private TaskDto completedTask(TaskDto taskDto) {
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setSlaRemainInDays(remainDays(taskDto));
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return taskService.updateTask(taskDto);
    }


    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus,
                                                                         String stageId) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
        return appPremisesRoutingHistoryDto;
    }
    private List<TaskDto> prepareTaskList(TaskDto taskDto){
        List<TaskDto> list = new ArrayList<>();
        taskDto.setId(null);
        taskDto.setUserId("69F8BB01-F70C-EA11-BE7D-000C29F371DC");
        taskDto.setSlaDateCompleted(null);
        taskDto.setSlaRemainInDays(null);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        list.add(taskDto);
        return list;
    }
}
