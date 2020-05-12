package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
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
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloudfeign.FeignException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

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
    @Autowired
    private ApplicationViewService applicationViewService;


    private final static String RECOMMENDATION_DTO = "appPremisesRecommendationDto";
    private final static String RECOMMENDATION = "recommendation";
    private final static String CHRONO = "chrono";
    private final static String NUMBER = "number";
    private final static String OTHERS = "Others";

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
    }

    public void inspectionReportInit(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the inspectionReportInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", null);
        ParamUtil.setSessionAttr(bpc.request, RECOMMENDATION_DTO, null);
        String taskId = ParamUtil.getMaskedString(bpc.request,"taskId");
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
//            InspectionReportDto inspectorUser = insRepService.getInspectorUser(taskDto, loginContext);
//            insRepDto.setReportedBy(inspectorUser.getReportedBy());
//            insRepDto.setReportNoteBy(inspectorUser.getReportNoteBy());
//            insRepDto.setInspectors(inspectorUser.getInspectors());
        }
        AppPremisesRecommendationDto accRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPECTYPE).getEntity();
        if(accRecommendationDto!=null){
            String recomDecision = accRecommendationDto.getRecomDecision();
            if(InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION_CONDITION.equals(recomDecision)){
                accRecommendationDto.setRecommendation(InspectionReportConstants.APPROVEDLTC);
                ParamUtil.setSessionAttr(bpc.request, RECOMMENDATION_DTO, accRecommendationDto);
            }
        }
        String appStatus = applicationViewDto.getApplicationDto().getStatus();
        if(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION.equals(appStatus)||ApplicationConsts.APPLICATION_STATUS_ROUTE_BACK.equals(appStatus)){
            initRecommendation(correlationId,applicationViewDto,bpc);
        }
        List<SelectOption> riskOption = insRepService.getRiskOption(applicationViewDto);
        List<SelectOption> chronoOption = getChronoOption();
        List<SelectOption> recommendationOption = getRecommendationOption();
        List<SelectOption> riskLevelOptions = getriskLevel();
        List<SelectOption> processingDe = getProcessingDecision(appStatus);
        String infoClassTop = "active";
        String infoClassBelow = "tab-pane active";
        String reportClassBelow = "tab-pane";
        ParamUtil.setSessionAttr(bpc.request, "infoClassTop", infoClassTop);
        ParamUtil.setSessionAttr(bpc.request, "reportClassTop", null);
        ParamUtil.setSessionAttr(bpc.request, "infoClassBelow", infoClassBelow);
        ParamUtil.setSessionAttr(bpc.request, "reportClassBelow", reportClassBelow);
        ParamUtil.setSessionAttr(bpc.request, "processingDe", (Serializable) processingDe);
        ParamUtil.setSessionAttr(bpc.request, "riskLevelOptions", (Serializable) riskLevelOptions);
        ParamUtil.setSessionAttr(bpc.request, "recommendationOption", (Serializable) recommendationOption);
        ParamUtil.setSessionAttr(bpc.request, "chronoOption", (Serializable) chronoOption);
        ParamUtil.setSessionAttr(bpc.request, "riskOption", (Serializable) riskOption);
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
    }

    public void inspectionReportPre(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionReportPre start ...."));
    }

    public void inspectorReportSave(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the inspectorReportSave start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        Date recomLiceStartDate = applicationViewDto.getRecomLiceStartDate();
        AppPremisesRecommendationDto appPremisesRecommendationDto = prepareRecommendation(bpc);
        ParamUtil.setSessionAttr(bpc.request, RECOMMENDATION_DTO, appPremisesRecommendationDto);
        Map<String, String> errorMap;
        ValidationResult validationResult = WebValidationHelper.validateProperty(appPremisesRecommendationDto, "save");
        if (validationResult.isHasErrors()) {
            errorMap = validationResult.retrieveAll();
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
        List<AppPremisesRecommendationDto> appPremisesRecommendationDtoList = prepareForSave(bpc, appPremisesCorrelationId,recomLiceStartDate);
        saveRecommendations(appPremisesRecommendationDtoList);
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String status = applicationDto.getStatus();
        String[] fastTracking =  ParamUtil.getStrings(bpc.request,"fastTracking");
        if(fastTracking!=null){
            applicationDto.setFastTracking(true);
        }

        if(ApplicationConsts.APPLICATION_STATUS_ROUTE_BACK.equals(status)){
            insRepService.routTastToRoutBack(taskDto, applicationDto, appPremisesCorrelationId,appPremisesRecommendationDto.getProcessRemarks());
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            return;
        }
        insRepService.routingTaskToAo1(taskDto, applicationDto, appPremisesCorrelationId,appPremisesRecommendationDto);
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
    }

    private AppPremisesRecommendationDto prepareRecommendation(BaseProcessClass bpc) {
        String riskLevel = ParamUtil.getRequestString(bpc.request, "riskLevel");
        String recommendation = ParamUtil.getRequestString(bpc.request, RECOMMENDATION);
        String periods = ParamUtil.getRequestString(bpc.request, "periods");
        String chrono = ParamUtil.getRequestString(bpc.request, CHRONO);
        String number = ParamUtil.getRequestString(bpc.request, NUMBER);
        String followUpAction = ParamUtil.getRequestString(bpc.request, "followUpAction");
        String enforcement = ParamUtil.getRequestString(bpc.request, "engageEnforcement");
        String enforcementRemarks = ParamUtil.getRequestString(bpc.request, "enforcementRemarks");
        String processRemarks = ParamUtil.getRequestString(bpc.request, "processRemarks");
        String processingDecision = ParamUtil.getRequestString(bpc.request, "processingDecision");

        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        appPremisesRecommendationDto.setRecommendation(recommendation);
        appPremisesRecommendationDto.setPeriod(periods);
        if(!StringUtil.isEmpty(number)){
            try {
                appPremisesRecommendationDto.setRecomInNumber(Integer.parseInt(number));
            }catch (NumberFormatException e){
                appPremisesRecommendationDto.setRecomInNumber(null);
            }
        }
        appPremisesRecommendationDto.setChronoUnit(chrono);
        appPremisesRecommendationDto.setEngageEnforcement(enforcement);
        appPremisesRecommendationDto.setEngageEnforcementRemarks(enforcementRemarks);
        appPremisesRecommendationDto.setRiskLevel(riskLevel);
        appPremisesRecommendationDto.setFollowUpAction(followUpAction);
        appPremisesRecommendationDto.setProcessingDecision(processingDecision);
        appPremisesRecommendationDto.setProcessRemarks(processRemarks);
        return appPremisesRecommendationDto;
    }

    private List<AppPremisesRecommendationDto> prepareForSave(BaseProcessClass bpc, String appPremisesCorrelationId,Date licDate) {
        List<AppPremisesRecommendationDto> appPremisesRecommendationDtos = IaisCommonUtils.genNewArrayList();
        String riskLevel = ParamUtil.getRequestString(bpc.request, "riskLevel");
        String remarks = ParamUtil.getRequestString(bpc.request, "remarks");
        String recommendation = ParamUtil.getRequestString(bpc.request, RECOMMENDATION);
        String periods = ParamUtil.getRequestString(bpc.request, "periods");
        String enforcement = ParamUtil.getRequestString(bpc.request, "engageEnforcement");
        String chrono = ParamUtil.getRequestString(bpc.request, CHRONO);
        String number = ParamUtil.getRequestString(bpc.request, NUMBER);
        String followUpAction = ParamUtil.getRequestString(bpc.request, "followUpAction");
        String enforcementRemarks = ParamUtil.getRequestString(bpc.request, "enforcementRemarks");
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        appPremisesRecommendationDto.setRemarks(remarks);
        appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
        appPremisesRecommendationDto.setRecomDecision(InspectionReportConstants.APPROVED);
        appPremisesRecommendationDto.setRecomInDate(licDate);
        if (OTHERS.equals(periods) && !StringUtil.isEmpty(chrono) && !StringUtil.isEmpty(number)) {
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
            appPremisesRecommendationDto.setChronoUnit(chrono);
            appPremisesRecommendationDto.setRecomInNumber(Integer.parseInt(number));
        } else if (!StringUtil.isEmpty(periods) && !OTHERS.equals(periods)) {
            String[] splitPeriods = periods.split("\\s+");
            String count = splitPeriods[0];
            String dateType = splitPeriods[1];
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
            appPremisesRecommendationDto.setChronoUnit(dateType);
            appPremisesRecommendationDto.setRecomInNumber(Integer.parseInt(count));
            appPremisesRecommendationDto.setRecommendation(recommendation);
        }else if(InspectionReportConstants.REJECTED.equals(recommendation)) {
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
            appPremisesRecommendationDto.setRecomDecision(InspectionReportConstants.REJECTED);
            appPremisesRecommendationDto.setRecomInNumber(0);
        }
        AppPremisesRecommendationDto engageEnforcementAppPremisesRecommendationDto = new AppPremisesRecommendationDto();
        engageEnforcementAppPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPCTION_ENGAGE);
        if(StringUtil.isEmpty(enforcement)){
            engageEnforcementAppPremisesRecommendationDto.setRemarks(null);
        }else {
            engageEnforcementAppPremisesRecommendationDto.setRemarks(enforcementRemarks);
        }
        engageEnforcementAppPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);

        AppPremisesRecommendationDto riskLevelAppPremisesRecommendationDto = new AppPremisesRecommendationDto();
        riskLevelAppPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPCTION_RISK_LEVEL);
        riskLevelAppPremisesRecommendationDto.setRecomDecision(riskLevel);
        riskLevelAppPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);

        AppPremisesRecommendationDto followAppPremisesRecommendationDto = new AppPremisesRecommendationDto();
        followAppPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPCTION_FOLLOW_UP_ACTION);
        followAppPremisesRecommendationDto.setRemarks(followUpAction);
        followAppPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);

        appPremisesRecommendationDtos.add(appPremisesRecommendationDto);
        appPremisesRecommendationDtos.add(engageEnforcementAppPremisesRecommendationDto);
        appPremisesRecommendationDtos.add(riskLevelAppPremisesRecommendationDto);
        appPremisesRecommendationDtos.add(followAppPremisesRecommendationDto);
        return appPremisesRecommendationDtos;
    }

    private void initRecommendation(String correlationId,ApplicationViewDto applicationViewDto,BaseProcessClass bpc){
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        AppPremisesRecommendationDto engageRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_ENGAGE).getEntity();
        AppPremisesRecommendationDto riskRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_RISK_LEVEL).getEntity();
        AppPremisesRecommendationDto followRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_FOLLOW_UP_ACTION).getEntity();

        AppPremisesRecommendationDto initRecommendationDto = new AppPremisesRecommendationDto();
        if (appPremisesRecommendationDto != null) {
            String chronoUnit = appPremisesRecommendationDto.getChronoUnit();
            Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
            String period = recomInNumber+" " + chronoUnit;
            List<String> periods = insRepService.getPeriods(applicationViewDto);
            if(periods!=null&&!periods.isEmpty()){
                if(periods.contains(period)){
                    initRecommendationDto.setPeriod(period);
                }else {
                    initRecommendationDto.setPeriod("Others");
                    initRecommendationDto.setRecomInNumber(recomInNumber);
                    initRecommendationDto.setChronoUnit(chronoUnit);
                }
            }
            String recomDecision = appPremisesRecommendationDto.getRecomDecision();
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
        }else {

        }
        if (riskRecommendationDto != null) {
            String riskLevel = riskRecommendationDto.getRecomDecision();
            initRecommendationDto.setRiskLevel(riskLevel);
        }
        if (followRecommendationDto != null) {
            String followRemarks = followRecommendationDto.getRemarks();
            initRecommendationDto.setFollowUpAction(followRemarks);
        }
        ParamUtil.setSessionAttr(bpc.request, RECOMMENDATION_DTO, initRecommendationDto);
    }

    private void saveRecommendations(List<AppPremisesRecommendationDto> appPremisesRecommendationDtoList){
        AppPremisesRecommendationDto appPremisesRecommendationDto1 = appPremisesRecommendationDtoList.get(0);
        AppPremisesRecommendationDto appPremisesRecommendationDto3 = appPremisesRecommendationDtoList.get(1);
        AppPremisesRecommendationDto appPremisesRecommendationDto4 = appPremisesRecommendationDtoList.get(2);
        AppPremisesRecommendationDto appPremisesRecommendationDto5 = appPremisesRecommendationDtoList.get(3);

        insRepService.updateRecommendation(appPremisesRecommendationDto1);
        String engageEnforcementRemarks = appPremisesRecommendationDto3.getRemarks();
        if (!StringUtil.isEmpty(engageEnforcementRemarks)) {
            appPremisesRecommendationDto3.setRemarks(engageEnforcementRemarks);
        }else {
            appPremisesRecommendationDto3.setRemarks(null);
        }
        insRepService.updateengageRecommendation(appPremisesRecommendationDto3);
        String riskLevel = appPremisesRecommendationDto4.getRecomDecision();
        if (!StringUtil.isEmpty(riskLevel)) {
            insRepService.updateRiskRecommendation(appPremisesRecommendationDto4);
        }
        String followRemarks = appPremisesRecommendationDto5.getRemarks();
        if (StringUtil.isEmpty(followRemarks)) {
            appPremisesRecommendationDto5.setRemarks(null);
        }
        insRepService.updateFollowRecommendation(appPremisesRecommendationDto5);
    }

    private List<SelectOption> getChronoOption() {
        List<SelectOption> ChronoResult = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption(RiskConsts.YEAR, "Year(s)");
        SelectOption so2 = new SelectOption(RiskConsts.MONTH, "Month(s)");
        ChronoResult.add(so1);
        ChronoResult.add(so2);
        return ChronoResult;
    }

    private List<SelectOption> getRecommendationOption() {
        List<SelectOption> recommendationResult = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption(InspectionReportConstants.APPROVED, "Proceed with Licence Issuance");
        SelectOption so2 = new SelectOption(InspectionReportConstants.APPROVEDLTC, "Proceed with Licence Issuance (with LTCs)");
        SelectOption so3 = new SelectOption(InspectionReportConstants.REJECTED, "Reject Licence");
        recommendationResult.add(so1);
        recommendationResult.add(so2);
        recommendationResult.add(so3);
        return recommendationResult;
    }

    private List<SelectOption> getriskLevel() {
        List<SelectOption> riskLevelResult = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption(InspectionReportConstants.LOW, "Low");
        SelectOption so2 = new SelectOption(InspectionReportConstants.MODERATE, "Moderate");
        SelectOption so3 = new SelectOption(InspectionReportConstants.HIGH, "High");
        riskLevelResult.add(so1);
        riskLevelResult.add(so2);
        riskLevelResult.add(so3);
        return riskLevelResult;
    }

    private List<SelectOption> getProcessingDecision(String status) {
        if(ApplicationConsts.APPLICATION_STATUS_ROUTE_BACK.equals(status)){
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
