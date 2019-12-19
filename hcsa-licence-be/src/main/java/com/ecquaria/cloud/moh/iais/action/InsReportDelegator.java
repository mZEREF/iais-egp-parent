package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
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
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
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
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", null);
        ParamUtil.setSessionAttr(bpc.request, "appPremisesRecommendationDto", null);
    }


    public void inspectionReportPre(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>inspectionReportPre");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String taskId = "46512333-7A16-EA11-BE7D-000C29F371DC";
        TaskDto taskDto = taskService.getTaskById(taskId);
        String wkGrpId = taskDto.getWkGrpId();


        String appNo = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = insRepService.getApplicationViewDto(appNo);
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, "insRepDto");
        if (insRepDto == null) {
            insRepDto = insRepService.getInsRepDto(taskDto, applicationViewDto);
        }
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
    }

    public void confirmData(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>confirmData");
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, "insRepDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String remarks = ParamUtil.getRequestString(bpc.request, "remarks");
        String recommendation = ParamUtil.getRequestString(bpc.request, "recommendation");
        String otherRecommendation = ParamUtil.getRequestString(bpc.request, "otherRecommendation");
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        appPremisesRecommendationDto.setRemarks(remarks);
        appPremisesRecommendationDto.setRecomType("report");

        if ("Others".equals(recommendation)) {
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
            String[] split_number = otherRecommendation.split("\\D");
            String[] split_unit = otherRecommendation.split("\\d");
            String unit = split_unit[1];
            String number = split_number[0];
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
            appPremisesRecommendationDto.setChronoUnit(unit);
            appPremisesRecommendationDto.setRecomInNumber(Integer.parseInt(number));
        } else {
            String[] split_number = recommendation.split("\\D");
            String[] split_unit = recommendation.split("\\d");
            String unit = split_unit[1];
            String number = split_number[0];
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
            appPremisesRecommendationDto.setChronoUnit(unit);
            appPremisesRecommendationDto.setRecomInNumber(Integer.parseInt(number));
        }
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        ParamUtil.setSessionAttr(bpc.request, "appPremisesRecommendationDto", appPremisesRecommendationDto);
        ParamUtil.setSessionAttr(bpc.request, "remarks", remarks);
        ParamUtil.setSessionAttr(bpc.request, "recommendation", recommendation);
        ParamUtil.setSessionAttr(bpc.request, "otherRecommendation", otherRecommendation);
    }

    public void inspectorReportSave(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the inspectorReportSave start ...."));
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, "insRepDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        AppPremisesRecommendationDto appPremisesRecommendationDto = (AppPremisesRecommendationDto) ParamUtil.getSessionAttr(bpc.request, "appPremisesRecommendationDto");
        String Remarks = ParamUtil.getRequestString(bpc.request, "remarks");
        String recommendation = ParamUtil.getRequestString(bpc.request, "recommendation");
        String otherRecommendation = ParamUtil.getRequestString(bpc.request, "otherRecommendation");
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        appPremisesRecommendationDto.setRemarks(Remarks);
        appPremisesRecommendationDto.setRecomType("report");
        if ("Others".equals(recommendation)) {
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
            String[] split_number = otherRecommendation.split("\\D");
            String[] split_unit = otherRecommendation.split("\\d");
            String unit = split_unit[1];
            String number = split_number[0];
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
            appPremisesRecommendationDto.setChronoUnit(unit);
            appPremisesRecommendationDto.setRecomInNumber(Integer.parseInt(number));
            insRepService.saveRecommendation(appPremisesRecommendationDto);
        } else {
            String[] split_number = recommendation.split("\\D");
            String[] split_unit = recommendation.split("\\d");
            String unit = split_unit[1];
            String number = split_number[0];
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
            appPremisesRecommendationDto.setChronoUnit(unit);
            appPremisesRecommendationDto.setRecomInNumber(Integer.parseInt(number));
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

    private List<TaskDto> prepareTaskList(TaskDto taskDto) {
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
