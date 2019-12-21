package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloudfeign.FeignException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author weilu
 * @date 2019/12/5 13:16
 */
@Delegator(value = "insReportAo")
@Slf4j
public class InsReportAoDelegator {

    @Autowired
    private InsRepService insRepService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;


    public void start(BaseProcessClass bpc) {

        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
        AuditTrailHelper.auditFunction("Inspection Report", "Assign Report");
    }

    public void AoInit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionReportInit start ...."));
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", null);
        ParamUtil.setSessionAttr(bpc.request, "appPremisesRecommendationDto", null);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", null);
    }


    public void AoReportPre(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>prepareReportData");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String taskId;
        taskId = (String)ParamUtil.getSessionAttr(bpc.request, "taskId");
        if(StringUtil.isEmpty(taskId)){
            taskId = "7260C794-2C22-EA11-BE7D-000C29F371DC";
        }
        TaskDto taskDto = taskService.getTaskById(taskId);
        String appNo = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = insRepService.getApplicationViewDto(appNo);
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        InspectionReportDto insRepDto = insRepService.getInsRepDto(taskDto,applicationViewDto,loginContext);
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationId, "report").getEntity();
        String chronoUnit = appPremisesRecommendationDto.getChronoUnit();
        Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
        String option  = recomInNumber + chronoUnit;

        ParamUtil.setSessionAttr(bpc.request, "option", option);
        ParamUtil.setSessionAttr(bpc.request, "appPremisesRecommendationDto", appPremisesRecommendationDto);
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
    }

    public void action(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the action start ...."));
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, "insRepDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto =  (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
    }

    public void back(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the back start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto =  (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String appId = applicationDto.getId();
        String status = applicationDto.getStatus();
        String stageId = taskDto.getTaskKey();
        updateApplicaiton(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION);
        completedTask(taskDto);
        createAppPremisesRoutingHistory(appPremisesCorrelationId, status, stageId,null,null,RoleConsts.USER_ROLE_AO1);
        String robackUserId = insRepService.getRobackUserId(appId, stageId);
        List<TaskDto> taskDtos = prepareBackTaskList(taskDto, robackUserId);
        taskService.createTasks(taskDtos);
        createAppPremisesRoutingHistory(appPremisesCorrelationId,status, stageId,null,null,RoleConsts.USER_ROLE_INSPECTIOR);
    }


    public void approve(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the inspectorReportAction start ...."));
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, "insRepDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto =  (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
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
        }else{
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
        String stageId = taskDto.getTaskKey();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String status = applicationDto.getStatus();
        updateApplicaiton(applicationDto,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02);
        completedTask(taskDto);
        createAppPremisesRoutingHistory(appPremisesCorrelationId,status, stageId,null,null,RoleConsts.USER_ROLE_AO1);
        List<TaskDto> taskDtos = prepareTaskList(taskDto);
        taskService.createTasks(taskDtos);
        createAppPremisesRoutingHistory(appPremisesCorrelationId,status, stageId,null,null,RoleConsts.USER_ROLE_AO2);
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
     *
     * @param applicationDto
     * @param appStatus
     * update application status
     */
    private ApplicationDto updateApplicaiton(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        return insRepService.updateApplicaiton(applicationDto);
    }

    /**
     * complete task
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
                                                                         String stageId, String internalRemarks, String processDec,String roleId) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setProcessDecision(processDec);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
        return appPremisesRoutingHistoryDto;
    }




    private List<TaskDto> prepareBackTaskList(TaskDto taskDto,String userId) {
        List<TaskDto> list = new ArrayList<>();
        taskDto.setId(null);
        taskDto.setTaskType(TaskConsts.TASK_TYPE_INSPECTION);
        taskDto.setUserId(userId);
        taskDto.setDateAssigned(new Date());
        taskDto.setSlaDateCompleted(null);
        taskDto.setSlaRemainInDays(null);
        //taskDto.setScore(1);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
        taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        list.add(taskDto);
        taskService.createTasks(list);
        return list;
    }

    private List<TaskDto> prepareTaskList(TaskDto taskDto) {
        List<TaskDto> list = new ArrayList<>();
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();

        //List<HcsaSvcStageWorkingGroupDto> listhcsaSvcStageWorkingGroupDto = hcsaConfigClient.getSvcWorkGroup(hcsaSvcStageWorkingGroupDto).getEntity();
        taskDto.setId(null);
        taskDto.setUserId(null);
        taskDto.setDateAssigned(null);
        taskDto.setSlaDateCompleted(null);
        taskDto.setSlaRemainInDays(null);
        taskDto.setScore(0);

        taskDto.setTaskType(TaskConsts.TASK_TYPE_MAIN_FLOW_SUPER);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setRoleId(RoleConsts.USER_ROLE_AO2);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        list.add(taskDto);
        return list;
    }
}
