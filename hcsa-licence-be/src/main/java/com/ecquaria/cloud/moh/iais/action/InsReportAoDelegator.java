package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
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
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.*;

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
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;
    @Autowired
    AppInspectionStatusClient appInspectionStatusClient;
    @Autowired
    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;


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
        taskId = ParamUtil.getRequestString(bpc.request,"taskId");
        if(StringUtil.isEmpty(taskId)){
            taskId = "44E99138-C82E-EA11-BE7D-000C29F371DC";
        }
        TaskDto taskDto = taskService.getTaskById(taskId);
        String correlationId = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = insRepService.getApplicationViewDto(correlationId);
        InspectionReportDto insRepDto = insRepService.getInsRepDto(taskDto,applicationViewDto,loginContext);
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        String chronoUnit = appPremisesRecommendationDto.getChronoUnit();
        Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
        String option  = recomInNumber + chronoUnit;
        List<SelectOption> riskOption = insRepService.getRiskOption(applicationViewDto);
        SelectOption so = new SelectOption("accept","accept");
        riskOption.add(so);
        ParamUtil.setSessionAttr(bpc.request, "riskOption", (Serializable)riskOption);
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

    public void back(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the back start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto =  (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        insRepService.routBackTaskToInspector(taskDto,applicationDto,appPremisesCorrelationId);
//        String appId = applicationDto.getId();
//        String status = applicationDto.getStatus();
//        String stageId = taskDto.getTaskKey();
//        updateApplicaiton(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION);
//        completedTask(taskDto);
//        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorySubStage(taskDto.getRefNo(), taskDto.getTaskKey()).getEntity();
//        String subStage = appPremisesRoutingHistoryDto.getSubStage();
//        createAppPremisesRoutingHistory(appPremisesCorrelationId, status, stageId,null,null,RoleConsts.USER_ROLE_AO1,subStage);
//        //robackId
//        String robackUserId = insRepService.getRobackUserId(appId, stageId);
//        List<TaskDto> taskDtos = prepareBackTaskList(taskDto, robackUserId);
//        taskService.createTasks(taskDtos);
//        createAppPremisesRoutingHistory(appPremisesCorrelationId,status, stageId,null,null,RoleConsts.USER_ROLE_INSPECTIOR,subStage);
    }


    public void approve(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the inspectorReportAction start ...."));
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, "insRepDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        TaskDto taskDto =  (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
//        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
//        appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
        AppPremisesRecommendationDto appPremisesRecommendationDto = prepareRecommendation(bpc, appPremisesCorrelationId);
        Map<String,String> errorMap = new HashMap<>(34);
        ValidationResult validationResult = WebValidationHelper.validateProperty(appPremisesRecommendationDto, "edit");
        if (validationResult.isHasErrors()) {
            errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.FALSE);
            return;
        }
        insRepService.updateRecommendation(appPremisesRecommendationDto);
        insRepService.routingTaskToAo2(taskDto,applicationDto,appPremisesCorrelationId);
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
        ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
//        String stageId = taskDto.getTaskKey();
//        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
//        String appId = applicationDto.getId();
//        String status = applicationDto.getStatus();
//        String serviceId = applicationDto.getServiceId();
//        String userId = insRepService.getRobackUserId(appId, stageId);
//        updateApplicaiton(applicationDto,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02);
//        completedTask(taskDto);
//        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorySubStage(taskDto.getRefNo(), taskDto.getTaskKey()).getEntity();
//        String subStage = appPremisesRoutingHistoryDto.getSubStage();
//        createAppPremisesRoutingHistory(appPremisesCorrelationId,status, stageId,null,null,RoleConsts.USER_ROLE_AO1,subStage);
//        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
//        hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
//        hcsaSvcStageWorkingGroupDto.setStageId(stageId);
//        hcsaSvcStageWorkingGroupDto.setOrder(1);
//        List<TaskDto> taskDtos = prepareTaskList(taskDto,hcsaSvcStageWorkingGroupDto,userId);
//        taskService.createTasks(taskDtos);
//        createAppPremisesRoutingHistory(appPremisesCorrelationId,status, stageId,null,null,RoleConsts.USER_ROLE_AO2,subStage);
    }

    private AppPremisesRecommendationDto prepareRecommendation (BaseProcessClass bpc,String appPremisesCorrelationId){
        String recommendation = ParamUtil.getRequestString(bpc.request, "recommendation");
        String otherRecommendation = ParamUtil.getRequestString(bpc.request, "otherRecommendation");
//        String inspectorRecommendation = ParamUtil.getRequestString(bpc.request, "inspectorRecommendation");
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        appPremisesRecommendationDto.setRecomInDate(new Date());
        appPremisesRecommendationDto.setRecomDecision(InspectionConstants.PROCESS_DECI_REVIEW_INSPECTION_REPORT);
        appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
        if(!StringUtil.isEmpty(recommendation)||!StringUtil.isEmpty(otherRecommendation)){
            if(!StringUtil.isEmpty(otherRecommendation)){
                appPremisesRecommendationDto.setRecommendation(otherRecommendation);
            }else {
                appPremisesRecommendationDto.setRecommendation(recommendation);
            }
        }
        if(!StringUtil.isEmpty(recommendation)){
            if ("Others".equals(recommendation)) {
                appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
                String[] split_number = otherRecommendation.split("\\D");
                String[] split_unit = otherRecommendation.split("\\d");
                String unit = split_unit[1];
                String number = split_number[0];
                appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
                appPremisesRecommendationDto.setChronoUnit(unit);
                appPremisesRecommendationDto.setRecomInNumber(Integer.parseInt(number));
            }else if("accept".equals(recommendation)){
                appPremisesRecommendationDto.setRecommendation("accept");
            }
            else {
                String[] split_number = recommendation.split("\\D");
                String[] split_unit = recommendation.split("\\d");
                String unit = split_unit[1];
                String number = split_number[0];
                appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
                appPremisesRecommendationDto.setChronoUnit(unit);
                appPremisesRecommendationDto.setRecomInNumber(Integer.parseInt(number));
            }
        }
        return appPremisesRecommendationDto;
    }




    /**
     * private utils
     */
//    private int remainDays(TaskDto taskDto) {
//        int result = 0;
//        String resultStr = DurationFormatUtils.formatPeriod(taskDto.getDateAssigned().getTime(), taskDto.getSlaDateCompleted().getTime(), "d");
//        log.debug(StringUtil.changeForLog("The resultStr is -->:") + resultStr);
//        return result;
//    }
//
//    /**
//     *
//     * @param applicationDto
//     * @param appStatus
//     * update application status
//     */
//    private ApplicationDto updateApplicaiton(ApplicationDto applicationDto, String appStatus) {
//        applicationDto.setStatus(appStatus);
//        return insRepService.updateApplicaiton(applicationDto);
//    }
//
//    /**
//     * complete task
//     * @param taskDto
//     * @return
//     */
//    private TaskDto completedTask(TaskDto taskDto) {
//        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
//        taskDto.setSlaDateCompleted(new Date());
//        taskDto.setSlaRemainInDays(remainDays(taskDto));
//        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
//        return taskService.updateTask(taskDto);
//    }
//
//
//
//    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus,
//                                                                         String stageId, String internalRemarks, String processDec,String roleId,String subStage) {
//        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
//        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
//        appPremisesRoutingHistoryDto.setStageId(stageId);
//        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
//        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
//        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
//        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
//        appPremisesRoutingHistoryDto.setProcessDecision(processDec);
//        appPremisesRoutingHistoryDto.setRoleId(roleId);
//        appPremisesRoutingHistoryDto.setSubStage(subStage);
//        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
//        return appPremisesRoutingHistoryDto;
//    }
//
//
//
//
//    private List<TaskDto> prepareBackTaskList(TaskDto taskDto,String userId) {
//        List<TaskDto> list = new ArrayList<>();
//        taskDto.setId(null);
//        taskDto.setTaskType(TaskConsts.TASK_TYPE_INSPECTION);
//        taskDto.setUserId(userId);
//        taskDto.setDateAssigned(new Date());
//        taskDto.setSlaDateCompleted(null);
//        taskDto.setSlaRemainInDays(null);
//        //taskDto.setScore(1);
//        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
//        taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
//        taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT);
//        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
//        list.add(taskDto);
//        return list;
//    }
//
//    private List<TaskDto> prepareTaskList(TaskDto taskDto,HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto,String userId) {
//        List<TaskDto> list = new ArrayList<>();
//        List<HcsaSvcStageWorkingGroupDto> listhcsaSvcStageWorkingGroupDto = hcsaConfigClient.getSvcWorkGroup(hcsaSvcStageWorkingGroupDto).getEntity();
//        List<HcsaSvcStageWorkingGroupDto> listhcsaSvcStageWorkingGroupDtos = hcsaConfigClient.getSvcWorkGroup(hcsaSvcStageWorkingGroupDto).getEntity();
//        listhcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(listhcsaSvcStageWorkingGroupDtos);
//        if(listhcsaSvcStageWorkingGroupDtos!=null && !listhcsaSvcStageWorkingGroupDtos.isEmpty()){
//            String schemeType = listhcsaSvcStageWorkingGroupDtos.get(0).getSchemeType();
//            Integer count = listhcsaSvcStageWorkingGroupDtos.get(0).getCount();
//            taskDto.setScore(count);
//            taskDto.setTaskType(schemeType);
//        }
//        taskDto.setId(null);
//        taskDto.setUserId(userId);
//        taskDto.setDateAssigned(new Date());
//        taskDto.setSlaDateCompleted(null);
//        taskDto.setSlaRemainInDays(null);
//
//        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
//        taskDto.setRoleId(RoleConsts.USER_ROLE_AO2);
//        taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_MAIN_FLOW);
//        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
//        list.add(taskDto);
//        return list;
//    }
//
//    private void updateInspectionStatus(ApplicationDto applicationDto) {
//        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos =  appPremisesCorrClient.getAppPremisesCorrelationsByAppId(applicationDto.getId()).getEntity();
//        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremisesCorrelationDtos.get(0).getId()).getEntity();
//        appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);
//        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
//        appInspectionStatusClient.update(appInspectionStatusDto);
//    }

}
