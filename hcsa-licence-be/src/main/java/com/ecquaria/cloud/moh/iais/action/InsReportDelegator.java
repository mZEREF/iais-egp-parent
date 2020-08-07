package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionReportConstants;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloudfeign.FeignException;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ecquaria.sz.commons.util.Calculator;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.validation.constraints.Null;

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
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;


    private final static String RECOMMENDATION_DTO = "appPremisesRecommendationDto";
    private final static String RECOMMENDATION = "recommendation";
    private final static String CHRONO = "chrono";
    private final static String NUMBER = "number";
    private final static String OTHERS = "Others";

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", null);
        ParamUtil.setSessionAttr(bpc.request, RECOMMENDATION_DTO, null);
        ParamUtil.setSessionAttr(bpc.request,"askType",null);
    }

    public void inspectionReportInit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionReportInit start ...."));
        String taskId = ParamUtil.getMaskedString(bpc.request, "taskId");
        AuditTrailHelper.auditFunction("Inspection Report", "Inspection Report");
        TaskDto taskDto = taskService.getTaskById(taskId);
        String correlationId = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = insRepService.getApplicationViewDto(correlationId);
        AuditTrailHelper.auditFunctionWithAppNo("Inspection Report", "Inspector generate Report", applicationViewDto.getApplicationDto().getApplicationNo());
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, "insRepDto");
        if (insRepDto == null) {
            insRepDto = insRepService.getInsRepDto(taskDto, applicationViewDto, loginContext);
            InspectionReportDto inspectorUser = insRepService.getInspectorUser(taskDto, loginContext);
            insRepDto.setInspectors(inspectorUser.getInspectors());
        }
        AppPremisesRecommendationDto accRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPECTYPE).getEntity();
        if (accRecommendationDto != null) {
            String recomDecision = accRecommendationDto.getRecomDecision();
            if (InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION_CONDITION.equals(recomDecision)) {
                accRecommendationDto.setRecommendation(InspectionReportConstants.APPROVEDLTC);
                ParamUtil.setSessionAttr(bpc.request, RECOMMENDATION_DTO, accRecommendationDto);
            }
        }
        String appStatus = applicationViewDto.getApplicationDto().getStatus();
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        if (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION.equals(appStatus) || ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR.equals(appStatus)|| ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(appStatus)) {
            appPremisesRecommendationDto = initRecommendation(correlationId, applicationViewDto, bpc);
        }
        List<SelectOption> riskOption = insRepService.getRiskOption(applicationViewDto);
        List<SelectOption> chronoOption = getChronoOption();
        List<SelectOption> recommendationOption = getRecommendationOption(applicationType);
        List<SelectOption> processingDe = getProcessingDecision(appStatus);
        String periodDefault = insRepService.getPeriodDefault(applicationViewDto,taskDto);
        appPremisesRecommendationDto.setPeriod(periodDefault);
        ParamUtil.setRequestAttr(bpc.request, "appPremisesRecommendationDto", appPremisesRecommendationDto);
        String infoClassTop = "active";
        String infoClassBelow = "tab-pane active";
        String reportClassBelow = "tab-pane";
        ParamUtil.setSessionAttr(bpc.request, "appType", null);
        ParamUtil.setSessionAttr(bpc.request, "infoClassTop", infoClassTop);
        ParamUtil.setSessionAttr(bpc.request, "reportClassTop", null);
        ParamUtil.setSessionAttr(bpc.request, "infoClassBelow", infoClassBelow);
        ParamUtil.setSessionAttr(bpc.request, "reportClassBelow", reportClassBelow);
        ParamUtil.setSessionAttr(bpc.request, "processingDe", (Serializable) processingDe);
        ParamUtil.setSessionAttr(bpc.request, "recommendationOption", (Serializable) recommendationOption);
        ParamUtil.setSessionAttr(bpc.request, "chronoOption", (Serializable) chronoOption);
        ParamUtil.setSessionAttr(bpc.request, "riskOption", (Serializable) riskOption);
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
    }

    public void inspectionReportPre(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionReportPre start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        ParamUtil.setSessionAttr(bpc.request, "appType", applicationType);
    }

    public void inspectorReportSave(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the inspectorReportSave start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        Date recomLiceStartDate = applicationViewDto.getRecomLiceStartDate();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String status = applicationDto.getStatus();
        String applicationType = applicationDto.getApplicationType();
        AppPremisesRecommendationDto appPremisesRecommendationDto = prepareRecommendation(bpc,applicationType);
        ParamUtil.setSessionAttr(bpc.request, RECOMMENDATION_DTO, appPremisesRecommendationDto);
        if (ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationType)) {
            appPremisesRecommendationDto.setRecommendation("Audit");
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ValidationResult validationResult = WebValidationHelper.validateProperty(appPremisesRecommendationDto, "save");
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)){
            String recommendationRfc = ParamUtil.getRequestString(bpc.request, "recommendationRfc");
            if(StringUtil.isEmpty(recommendationRfc)){
                errorMap.put("recommendationRfc", "ERR0009");
            }
        }
        if (validationResult.isHasErrors()) {
            Map<String, String> stringStringMap = validationResult.retrieveAll();
            errorMap.putAll(stringStringMap);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            String reportClassTop = "active";
            String infoClassBelow = "tab-pane";
            String reportClassBelow = "tab-pane active";
            ParamUtil.setSessionAttr(bpc.request, "infoClassTop", null);
            ParamUtil.setSessionAttr(bpc.request, "reportClassTop", reportClassTop);
            ParamUtil.setSessionAttr(bpc.request, "infoClassBelow", infoClassBelow);
            ParamUtil.setSessionAttr(bpc.request, "reportClassBelow", reportClassBelow);
            return;
        }
        List<AppPremisesRecommendationDto> appPremisesRecommendationDtoList = prepareForSave(bpc, appPremisesCorrelationId, recomLiceStartDate, applicationType);
        saveRecommendations(appPremisesRecommendationDtoList);
        String[] fastTracking = ParamUtil.getStrings(bpc.request, "fastTracking");
        if (fastTracking != null) {
            applicationDto.setFastTracking(true);
        }
        if (ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR.equals(status)) {
            insRepService.routTaskToRoutBack(bpc, taskDto, applicationDto, appPremisesCorrelationId, appPremisesRecommendationDto.getProcessRemarks());
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            ParamUtil.setSessionAttr(bpc.request,"askType","Y");
            return;
        }
        if (ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(status)) {
            insRepService.routTaskToRoutBackAo3(bpc, taskDto, applicationDto, appPremisesCorrelationId, appPremisesRecommendationDto.getProcessRemarks());
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            return;
        }
        insRepService.routingTaskToAo1(taskDto, applicationDto, appPremisesCorrelationId, appPremisesRecommendationDto);
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
    }

    private AppPremisesRecommendationDto prepareRecommendation(BaseProcessClass bpc,String appType) {
        String recommendation = ParamUtil.getRequestString(bpc.request, RECOMMENDATION);
        String recommendationRfc = ParamUtil.getRequestString(bpc.request, "recommendationRfc");
        String periods = ParamUtil.getRequestString(bpc.request, "periods");
        String chrono = ParamUtil.getRequestString(bpc.request, CHRONO);
        String number = ParamUtil.getRequestString(bpc.request, NUMBER);
        String followUpAction = ParamUtil.getRequestString(bpc.request, "followUpAction");
        String enforcement = ParamUtil.getRequestString(bpc.request, "engageEnforcement");
        String enforcementRemarks = ParamUtil.getRequestString(bpc.request, "enforcementRemarks");
        String processRemarks = ParamUtil.getRequestString(bpc.request, "processRemarks");
        String processingDecision = ParamUtil.getRequestString(bpc.request, "processingDecision");

        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
            appPremisesRecommendationDto.setRecommendation(recommendationRfc);
        }else {
            appPremisesRecommendationDto.setRecommendation(recommendation);
        }
        appPremisesRecommendationDto.setPeriod(periods);
        if (!StringUtil.isEmpty(number)) {
            try {
                appPremisesRecommendationDto.setRecomInNumber(Integer.valueOf(number));
            } catch (NumberFormatException e) {
                appPremisesRecommendationDto.setRecomInNumber(null);
            }
        }
        appPremisesRecommendationDto.setChronoUnit(chrono);
        appPremisesRecommendationDto.setEngageEnforcement(enforcement);
        appPremisesRecommendationDto.setEngageEnforcementRemarks(enforcementRemarks);
        appPremisesRecommendationDto.setFollowUpAction(followUpAction);
        appPremisesRecommendationDto.setProcessingDecision(processingDecision);
        appPremisesRecommendationDto.setProcessRemarks(processRemarks);
        return appPremisesRecommendationDto;
    }

    private List<AppPremisesRecommendationDto> prepareForSave(BaseProcessClass bpc, String appPremisesCorrelationId, Date licDate, String appType) {
        List<AppPremisesRecommendationDto> appPremisesRecommendationDtos = IaisCommonUtils.genNewArrayList();
        String remarks = ParamUtil.getRequestString(bpc.request, "remarks");
        String recommendation = ParamUtil.getRequestString(bpc.request, RECOMMENDATION);
        String recommendationRfc = ParamUtil.getRequestString(bpc.request, "recommendationRfc");
        String periods = ParamUtil.getRequestString(bpc.request, "periods");
        String enforcement = ParamUtil.getRequestString(bpc.request, "engageEnforcement");
        String chrono = ParamUtil.getRequestString(bpc.request, CHRONO);
        String number = ParamUtil.getRequestString(bpc.request, NUMBER);
        String followUpAction = ParamUtil.getRequestString(bpc.request, "followUpAction");
        String enforcementRemarks = ParamUtil.getRequestString(bpc.request, "enforcementRemarks");
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
       if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)) {
            appPremisesRecommendationDto.setRemarks(remarks);
            appPremisesRecommendationDto.setRecommendation(recommendationRfc);
            appPremisesRecommendationDto.setRecomDecision(recommendationRfc);
            appPremisesRecommendationDto.setRecomInDate(licDate);
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
            appPremisesRecommendationDto.setRecomInNumber(6);
            appPremisesRecommendationDto.setPeriod(AppointmentConstants.RECURRENCE_MONTH);
            appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
        } else {
            appPremisesRecommendationDto.setRemarks(remarks);
            appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
            appPremisesRecommendationDto.setRecomDecision(InspectionReportConstants.APPROVED);
            appPremisesRecommendationDto.setRecomInDate(licDate);
            if (OTHERS.equals(periods) && !StringUtil.isEmpty(chrono) && !StringUtil.isEmpty(number)) {
                appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
                Integer num = Integer.valueOf(number);
                if(AppointmentConstants.RECURRENCE_YEAR.equals(chrono)){
                    chrono = AppointmentConstants.RECURRENCE_MONTH ;
                    num *=12;
                }
                appPremisesRecommendationDto.setChronoUnit(chrono);
                appPremisesRecommendationDto.setRecomInNumber(num);
            } else if (!StringUtil.isEmpty(periods) && !OTHERS.equals(periods) && InspectionReportConstants.APPROVEDLTC.equals(recommendation) || InspectionReportConstants.APPROVED.equals(recommendation)) {
                String[] splitPeriods = periods.split("\\s+");
                String count = splitPeriods[0];
                String dateType = splitPeriods[1];
                appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
                appPremisesRecommendationDto.setChronoUnit(dateType);
                appPremisesRecommendationDto.setRecomInNumber(Integer.valueOf(count));
                appPremisesRecommendationDto.setRecommendation(recommendation);
            } else if (InspectionReportConstants.REJECTED.equals(recommendation)) {
                appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
                appPremisesRecommendationDto.setRecomDecision(InspectionReportConstants.REJECTED);
                appPremisesRecommendationDto.setRecomInNumber(0);
            }
        }
        AppPremisesRecommendationDto engageEnforcementAppPremisesRecommendationDto = new AppPremisesRecommendationDto();
        engageEnforcementAppPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPCTION_ENGAGE);
        if (StringUtil.isEmpty(enforcement)) {
            engageEnforcementAppPremisesRecommendationDto.setRemarks(null);
        } else {
            engageEnforcementAppPremisesRecommendationDto.setRemarks(enforcementRemarks);
        }
        engageEnforcementAppPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);

        AppPremisesRecommendationDto followAppPremisesRecommendationDto = new AppPremisesRecommendationDto();
        followAppPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPCTION_FOLLOW_UP_ACTION);
        followAppPremisesRecommendationDto.setRemarks(followUpAction);
        followAppPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);

        appPremisesRecommendationDtos.add(appPremisesRecommendationDto);
        appPremisesRecommendationDtos.add(engageEnforcementAppPremisesRecommendationDto);
        appPremisesRecommendationDtos.add(followAppPremisesRecommendationDto);
        return appPremisesRecommendationDtos;
    }

    private AppPremisesRecommendationDto initRecommendation(String correlationId, ApplicationViewDto applicationViewDto, BaseProcessClass bpc) {
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        AppPremisesRecommendationDto engageRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_ENGAGE).getEntity();
        AppPremisesRecommendationDto followRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_FOLLOW_UP_ACTION).getEntity();

        AppPremisesRecommendationDto initRecommendationDto = new AppPremisesRecommendationDto();
        if (appPremisesRecommendationDto != null) {
            String chronoUnit = appPremisesRecommendationDto.getChronoUnit();
            Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
            String recomDecision = appPremisesRecommendationDto.getRecomDecision();
            String period = recomInNumber + " " + chronoUnit;
            List<String> periods = insRepService.getPeriods(applicationViewDto);
            if (periods != null && !periods.isEmpty() && !InspectionReportConstants.REJECTED.equals(recomDecision)) {
                if (periods.contains(period)) {
                    initRecommendationDto.setPeriod(period);
                } else {
                    initRecommendationDto.setPeriod("Others");
                    initRecommendationDto.setRecomInNumber(recomInNumber);
                    initRecommendationDto.setChronoUnit(chronoUnit);
                }
            } else if (InspectionReportConstants.REJECTED.equals(recomDecision)) {
                initRecommendationDto.setPeriod(null);
            }
            initRecommendationDto.setRecommendation(recomDecision);
        }

        if (appPremisesRecommendationDto != null) {
            String reportRemarks = appPremisesRecommendationDto.getRemarks();
            initRecommendationDto.setRemarks(reportRemarks);
        }
        if (engageRecommendationDto != null) {
            String remarks = engageRecommendationDto.getRemarks();
            String engage = "on";
            initRecommendationDto.setEngageEnforcement(engage);
            initRecommendationDto.setEngageEnforcementRemarks(remarks);
        }
        if (followRecommendationDto != null) {
            String followRemarks = followRecommendationDto.getRemarks();
            initRecommendationDto.setFollowUpAction(followRemarks);
        }
        return initRecommendationDto;
    }

    private void saveRecommendations(List<AppPremisesRecommendationDto> appPremisesRecommendationDtoList) {
        AppPremisesRecommendationDto appPremisesRecommendationDto1 = appPremisesRecommendationDtoList.get(0);
        AppPremisesRecommendationDto appPremisesRecommendationDto2 = appPremisesRecommendationDtoList.get(1);
        AppPremisesRecommendationDto appPremisesRecommendationDto3 = appPremisesRecommendationDtoList.get(2);

        if (!StringUtil.isEmpty(appPremisesRecommendationDto1.getRecommendation())) {
            insRepService.updateRecommendation(appPremisesRecommendationDto1);
        }
        String engageEnforcementRemarks = appPremisesRecommendationDto2.getRemarks();
        if (!StringUtil.isEmpty(engageEnforcementRemarks)) {
            appPremisesRecommendationDto2.setRemarks(engageEnforcementRemarks);
        } else {
            appPremisesRecommendationDto2.setRemarks(null);
        }
        insRepService.updateengageRecommendation(appPremisesRecommendationDto2);
        String followRemarks = appPremisesRecommendationDto3.getRemarks();
        if (!StringUtil.isEmpty(followRemarks)) {
            appPremisesRecommendationDto3.setRemarks(followRemarks);
        }else {
            appPremisesRecommendationDto3.setRemarks(null);
        }
        insRepService.updateFollowRecommendation(appPremisesRecommendationDto3);
    }

    private List<SelectOption> getChronoOption() {
        List<SelectOption> ChronoResult = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption(RiskConsts.YEAR, "Year(s)");
        SelectOption so2 = new SelectOption(RiskConsts.MONTH, "Month(s)");
        ChronoResult.add(so1);
        ChronoResult.add(so2);
        return ChronoResult;
    }

    private List<SelectOption> getRecommendationOption(String appType) {
        List<SelectOption> recommendationResult = IaisCommonUtils.genNewArrayList();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)) {
            SelectOption so1 = new SelectOption(InspectionReportConstants.RFC_APPROVED, "Approve");
            SelectOption so3 = new SelectOption(InspectionReportConstants.RFC_REJECTED, "Reject");
            recommendationResult.add(so1);
            recommendationResult.add(so3);
        } else {
            SelectOption so1 = new SelectOption(InspectionReportConstants.APPROVED, "Proceed with Licence Issuance");
            SelectOption so2 = new SelectOption(InspectionReportConstants.APPROVEDLTC, "Proceed with Licence Issuance (with LTCs)");
            SelectOption so3 = new SelectOption(InspectionReportConstants.REJECTED, "Reject Licence");
            recommendationResult.add(so1);
            recommendationResult.add(so2);
            recommendationResult.add(so3);
        }
        return recommendationResult;
    }

    private List<SelectOption> getProcessingDecision(String status) {
        if (ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR.equals(status)||ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(status)) {
            List<SelectOption> riskLevelResult = IaisCommonUtils.genNewArrayList();
            SelectOption so1 = new SelectOption("submit", "Give Clarification");
            riskLevelResult.add(so1);
            return riskLevelResult;
        }
        List<SelectOption> riskLevelResult = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption("submit", "Submit Inspection Report for review");
        riskLevelResult.add(so1);
        return riskLevelResult;
    }
}
