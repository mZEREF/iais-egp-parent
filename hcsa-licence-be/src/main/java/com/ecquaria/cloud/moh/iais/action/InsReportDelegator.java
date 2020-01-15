package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
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
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
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
//    @Autowired
//    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;
//    @Autowired
//    private HcsaConfigClient hcsaConfigClient;
//    @Autowired
//    private AppPremisesCorrClient appPremisesCorrClient;
//    @Autowired
//    AppInspectionStatusClient appInspectionStatusClient;
//    @Autowired
//    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;

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
        String userId = loginContext.getUserId();
        String taskId;
        taskId = ParamUtil.getRequestString(bpc.request,"taskId");
        if(StringUtil.isEmpty(taskId)){
            taskId = "44E99138-C82E-EA11-BE7D-000C29F371DC";
        }
        TaskDto taskDto = taskService.getTaskById(taskId);
        String correlationId = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = insRepService.getApplicationViewDto(correlationId);
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, "insRepDto");
        if (insRepDto == null) {
            insRepDto = insRepService.getInsRepDto(taskDto, applicationViewDto,loginContext);
        }
        List<SelectOption> riskOption = insRepService.getRiskOption(applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, "riskOption", (Serializable)riskOption);
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
    }

    public void inspectorReportSave(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the inspectorReportSave start ...."));
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, "insRepDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        AppPremisesRecommendationDto appPremisesRecommendationDto = prepareRecommendation(bpc, appPremisesCorrelationId);
        Map<String,String> errorMap = new HashMap<>(34);
        ValidationResult validationResult = WebValidationHelper.validateProperty(appPremisesRecommendationDto, "create");
        if (validationResult.isHasErrors()) {
            errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.FALSE);
            return;
        }
        insRepService.saveRecommendation(appPremisesRecommendationDto);
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
//        String serviceId = applicationDto.getServiceId();
//        String status = applicationDto.getStatus();
//        String taskKey = taskDto.getTaskKey();
        insRepService.routingTaskToAo1(taskDto,applicationDto,appPremisesCorrelationId);
        ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
//        ApplicationDto updateApplicationDto = updateApplicaitonStatus(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW);
//        updateInspectionStatus(applicationDto);
//        completedTask(taskDto);
//        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorySubStage(taskDto.getRefNo(), taskDto.getTaskKey()).getEntity();
//        String subStage = appPremisesRoutingHistoryDto.getSubStage();
//        createAppPremisesRoutingHistory(appPremisesCorrelationId, status, taskKey,null,InspectionConstants.PROCESS_DECI_REVIEW_INSPECTION_REPORT,RoleConsts.USER_ROLE_INSPECTIOR,subStage);
//        List<TaskDto> taskDtos = prepareTaskList(taskDto,serviceId,applicationDto);
//        taskService.createTasks(taskDtos);
//        createAppPremisesRoutingHistory(appPremisesCorrelationId,updateApplicationDto.getStatus(), taskKey,null,InspectionConstants.PROCESS_DECI_REVISE_INSPECTION_REPORT,RoleConsts.USER_ROLE_AO1,subStage);

    }

//    /**
//     * private utils
//     */
//
//
//    /**
//     * @param applicationDto
//     * @param appStatus      update application status
//     */
//    private ApplicationDto updateApplicaitonStatus(ApplicationDto applicationDto, String appStatus) {
//        applicationDto.setStatus(appStatus);
//        return insRepService.updateApplicaiton(applicationDto);
//    }
//
//    /**
//     * complete task
//     *
//     * @param taskDto
//     * @return
//     */
//    private TaskDto completedTask(TaskDto taskDto) {
//        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
//        taskDto.setSlaDateCompleted(new Date());
//        taskDto.setSlaRemainInDays(taskService.remainDays(taskDto));
//        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
//        return taskService.updateTask(taskDto);
//    }
//
//
//    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus,
//                                                                          String stageId, String internalRemarks, String processDec,String roleId,String subStage) {
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
//    private List<TaskDto> prepareTaskList(TaskDto taskDto,String serviceId,ApplicationDto applicationDto) throws FeignException {
//        List<TaskDto> list = new ArrayList<>();
//        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
//        hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
//        hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_AO1);
//        hcsaSvcStageWorkingGroupDto.setOrder(2);
//        HcsaSvcStageWorkingGroupDto dto = hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity();
//        List<ApplicationDto> applicationDtos = new ArrayList<>();
//        applicationDtos.add(applicationDto);
//        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos,HcsaConsts.ROUTING_STAGE_INS);
//        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
//        String schemeType =dto.getSchemeType();
//        String groupId = dto.getGroupId();
//       // dto.getStageWorkGroupId();
//        if(SystemParameterConstants.ROUND_ROBIN.equals(schemeType)){
//            TaskDto taskDto1 = taskService.getUserIdForWorkGroup(groupId);
//            taskDto.setUserId(taskDto1.getUserId());
//        }else {
//            taskDto.setUserId(null);
//        }
//        taskDto.setId(null);
//        taskDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
//        taskDto.setTaskType(schemeType);
//        taskDto.setWkGrpId(groupId);
//        taskDto.setDateAssigned(new Date());
//        taskDto.setSlaDateCompleted(null);
//        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
//        taskDto.setRoleId(RoleConsts.USER_ROLE_AO1);
//        taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT_REVIEW_AO1);
//        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
//        list.add(taskDto);
//        return list;
//    }
//
    private AppPremisesRecommendationDto prepareRecommendation (BaseProcessClass bpc,String appPremisesCorrelationId){
        String remarks = ParamUtil.getRequestString(bpc.request, "remarks");
        String recommendation = ParamUtil.getRequestString(bpc.request, "recommendation");
        String otherRecommendation = ParamUtil.getRequestString(bpc.request, "otherRecommendation");
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        appPremisesRecommendationDto.setRemarks(remarks);
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
        } else {
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
//
//    private void updateInspectionStatus(ApplicationDto applicationDto) {
//        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos =  appPremisesCorrClient.getAppPremisesCorrelationsByAppId(applicationDto.getId()).getEntity();
//        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremisesCorrelationDtos.get(0).getId()).getEntity();
//        appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_AO_RESULT);
//        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
//        appInspectionStatusClient.update(appInspectionStatusDto);
//    }
//
//    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId){
//        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = new ArrayList();
//        for(ApplicationDto applicationDto : applicationDtos){
//            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
//            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
//            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
//            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
//            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
//        }
//        return hcsaSvcStageWorkingGroupDtos;
//    }



}
