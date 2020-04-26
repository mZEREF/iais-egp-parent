package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionReportConstants;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
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

import javax.validation.constraints.Null;

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
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    private final static String RECOMMENDATION_DTO= "appPremisesRecommendationDto";
    private final static String OTHERS="Others";
    private final static String APPROVAL="Approval";
    private final static String REJECT="Reject";
    private final static String INSREPDTO="insRepDto";
    private final static String APPLICATIONVIEWDTO="applicationViewDto";
    private final static String TASKDTO="taskDto";


    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
    }

    public void AoInit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionReportInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, INSREPDTO, null);
        ParamUtil.setSessionAttr(bpc.request, RECOMMENDATION_DTO, null);
        ParamUtil.setSessionAttr(bpc.request, APPLICATIONVIEWDTO, null);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String taskId = ParamUtil.getMaskedString(bpc.request,"taskId");
        AuditTrailHelper.auditFunction("InspectionAO Report", "InspectionAO Report");
        TaskDto taskDto = taskService.getTaskById(taskId);
        String correlationId = taskDto.getRefNo();
        ApplicationViewDto  applicationViewDto = insRepService.getApplicationViewDto(correlationId);
        AuditTrailHelper.auditFunctionWithAppNo("Inspection Report", "AO1 process Report",
                applicationViewDto.getApplicationDto().getApplicationNo());
        InspectionReportDto insRepDto = insRepService.getInsRepDto(taskDto,applicationViewDto,loginContext);
        InspectionReportDto inspectorAo = insRepService.getInspectorAo(taskDto,applicationViewDto);
        insRepDto.setInspectors(inspectorAo.getInspectors());
        insRepDto.setReportNoteBy(inspectorAo.getReportNoteBy());
        insRepDto.setReportedBy(inspectorAo.getReportedBy());
        initAoRecommendation(correlationId,bpc);

        String infoClassTop = "active";
        String infoClassBelow = "tab-pane active";
        String reportClassBelow = "tab-pane";
        ParamUtil.setSessionAttr(bpc.request, "infoClassTop", infoClassTop);
        ParamUtil.setSessionAttr(bpc.request, "reportClassTop", null);
        ParamUtil.setSessionAttr(bpc.request, "infoClassBelow", infoClassBelow);
        ParamUtil.setSessionAttr(bpc.request, "reportClassBelow", reportClassBelow);
        List<SelectOption> riskOption = insRepService.getRiskOption(applicationViewDto);
        List<SelectOption> recommendationOption = getRecommendationOption();
        List<SelectOption> chronoOption = getChronoOption();
        List<SelectOption> riskLevelOptions = getriskLevel();
        List<SelectOption> processingDe = getProcessingDecision();
        ParamUtil.setSessionAttr(bpc.request, "recommendationOption", (Serializable) recommendationOption);
        ParamUtil.setSessionAttr(bpc.request, "processingDe", (Serializable) processingDe);
        ParamUtil.setSessionAttr(bpc.request, "riskLevelOptions", (Serializable) riskLevelOptions);
        ParamUtil.setSessionAttr(bpc.request, "chronoOption", (Serializable) chronoOption);
        ParamUtil.setSessionAttr(bpc.request, "riskOption", (Serializable)riskOption);
        ParamUtil.setSessionAttr(bpc.request, INSREPDTO, insRepDto);
        ParamUtil.setSessionAttr(bpc.request, APPLICATIONVIEWDTO, applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, TASKDTO, taskDto);
    }

    public void AoReportPre(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>prepareReportData");
    }

    public void action(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the action start ...."));
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, INSREPDTO);
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, APPLICATIONVIEWDTO);
        TaskDto taskDto =  (TaskDto)ParamUtil.getSessionAttr(bpc.request, TASKDTO);
        ParamUtil.setSessionAttr(bpc.request, INSREPDTO, insRepDto);
        ParamUtil.setSessionAttr(bpc.request, APPLICATIONVIEWDTO, applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, TASKDTO, taskDto);
    }

    public void back(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the back start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, APPLICATIONVIEWDTO);
        TaskDto taskDto =  (TaskDto)ParamUtil.getSessionAttr(bpc.request, TASKDTO);
        String historyRemarks = ParamUtil.getRequestString(bpc.request, "processingDecision");
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        AppPremisesRecommendationDto appPremisesRecommendationDto = (AppPremisesRecommendationDto)ParamUtil.getSessionAttr(bpc.request, RECOMMENDATION_DTO);
        AppPremisesRecommendationDto preapreRecommendationDto = prepareRecommendation(bpc,appPremisesRecommendationDto);
        ParamUtil.setSessionAttr(bpc.request, "preapreRecommendationDto", preapreRecommendationDto);
        ValidationResult validationResult = WebValidationHelper.validateProperty(preapreRecommendationDto, "edit");
        if (validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.FALSE);
            String reportClassTop = "active";
            String infoClassBelow = "tab-pane";
            String reportClassBelow = "tab-pane active";
            ParamUtil.setSessionAttr(bpc.request, "infoClassTop", null);
            ParamUtil.setSessionAttr(bpc.request, "reportClassTop", reportClassTop);
            ParamUtil.setSessionAttr(bpc.request, "infoClassBelow", infoClassBelow);
            ParamUtil.setSessionAttr(bpc.request, "reportClassBelow", reportClassBelow);
            return;
        }
        saveAoRecommendation(appPremisesCorrelationId,preapreRecommendationDto);
        insRepService.routBackTaskToInspector(taskDto,applicationDto,appPremisesCorrelationId,historyRemarks);
    }

    public void approve(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the inspectorReportAction start ...."));
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, INSREPDTO);
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, APPLICATIONVIEWDTO);
        AppPremisesRecommendationDto appPremisesRecommendationDto = (AppPremisesRecommendationDto)ParamUtil.getSessionAttr(bpc.request, RECOMMENDATION_DTO);
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        TaskDto taskDto =  (TaskDto)ParamUtil.getSessionAttr(bpc.request, TASKDTO);
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        AppPremisesRecommendationDto preapreRecommendationDto = prepareRecommendation(bpc,appPremisesRecommendationDto);
        ParamUtil.setSessionAttr(bpc.request, "preapreRecommendationDto", preapreRecommendationDto);
        ValidationResult validationResult = WebValidationHelper.validateProperty(preapreRecommendationDto, "edit");
        if (validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.FALSE);
            String reportClassTop = "active";
            String infoClassBelow = "tab-pane";
            String reportClassBelow = "tab-pane active";
            ParamUtil.setSessionAttr(bpc.request, "infoClassTop", null);
            ParamUtil.setSessionAttr(bpc.request, "reportClassTop", reportClassTop);
            ParamUtil.setSessionAttr(bpc.request, "infoClassBelow", infoClassBelow);
            ParamUtil.setSessionAttr(bpc.request, "reportClassBelow", reportClassBelow);
            return;
        }
        String[] fastTracking =  ParamUtil.getStrings(bpc.request,"fastTracking");
        if(fastTracking!=null){
            applicationDto.setFastTracking(true);
        }
        saveAoRecommendation(appPremisesCorrelationId,preapreRecommendationDto);
        insRepService.routingTaskToAo2(taskDto,applicationDto,appPremisesCorrelationId);
        ParamUtil.setSessionAttr(bpc.request, INSREPDTO, insRepDto);
        ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
    }

    private AppPremisesRecommendationDto prepareRecommendation (BaseProcessClass bpc,AppPremisesRecommendationDto appPremisesRecommendationDto){
        String riskLevel = ParamUtil.getRequestString(bpc.request, "riskLevel");
        String processingDecision = ParamUtil.getRequestString(bpc.request, "processingDecision");
        String followUpAction = ParamUtil.getRequestString(bpc.request, "followUpAction");
        String processRemarks = ParamUtil.getRequestString(bpc.request, "processRemarks");
        String enforcement = ParamUtil.getRequestString(bpc.request, "engageEnforcement");
        String enforcementRemarks = ParamUtil.getRequestString(bpc.request, "enforcementRemarks");

        appPremisesRecommendationDto.setEngageEnforcement(enforcement);
        appPremisesRecommendationDto.setEngageEnforcementRemarks(enforcementRemarks);
        appPremisesRecommendationDto.setRiskLevel(riskLevel);
        appPremisesRecommendationDto.setFollowUpAction(followUpAction);
        appPremisesRecommendationDto.setProcessingDecision(processingDecision);
        appPremisesRecommendationDto.setProcessRemarks(processRemarks);
        return appPremisesRecommendationDto;
    }

    private void initAoRecommendation(String correlationId,BaseProcessClass bpc){
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        AppPremisesRecommendationDto engageRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_ENGAGE).getEntity();
        AppPremisesRecommendationDto riskRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_RISK_LEVEL).getEntity();
        AppPremisesRecommendationDto followRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_FOLLOW_UP_ACTION).getEntity();

        AppPremisesRecommendationDto initRecommendationDto = new AppPremisesRecommendationDto();
        String period;
        if (appPremisesRecommendationDto != null) {
            String reportRemarks = appPremisesRecommendationDto.getRemarks();
            initRecommendationDto.setRemarks(reportRemarks);
            String chronoUnit = appPremisesRecommendationDto.getChronoUnit();
            String codeDesc = MasterCodeUtil.getCodeDesc(chronoUnit);
            Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
            period  = recomInNumber+" " + codeDesc;
            initRecommendationDto.setPeriod(period);
        }
        if (engageRecommendationDto != null) {
            String remarks = engageRecommendationDto.getRemarks();
            String engage = "on";
            initRecommendationDto.setEngageEnforcement(engage);
            initRecommendationDto.setEngageEnforcementRemarks(remarks);
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

    private void saveAoRecommendation(String appPremisesCorrelationId,AppPremisesRecommendationDto preapreRecommendationDto){
        String engageEnforcement = preapreRecommendationDto.getEngageEnforcement();
        String riskLevel = preapreRecommendationDto.getRiskLevel();
        String followUpAction = preapreRecommendationDto.getFollowUpAction();
        if(!StringUtil.isEmpty(engageEnforcement)){
            AppPremisesRecommendationDto recommendationDtoEngage = new AppPremisesRecommendationDto();
            recommendationDtoEngage.setAppPremCorreId(appPremisesCorrelationId);
            String engageEnforcementRemarks = preapreRecommendationDto.getEngageEnforcementRemarks();
            recommendationDtoEngage.setRemarks(engageEnforcementRemarks);
            recommendationDtoEngage.setRecomType(InspectionConstants.RECOM_TYPE_INSPCTION_ENGAGE);
            insRepService.updateengageRecommendation(recommendationDtoEngage);
        }else {
            AppPremisesRecommendationDto recommendationDtoEngage = new AppPremisesRecommendationDto();
            recommendationDtoEngage.setAppPremCorreId(appPremisesCorrelationId);
            recommendationDtoEngage.setRemarks(null);
            insRepService.updateengageRecommendation(recommendationDtoEngage);
        }
        if(!StringUtil.isEmpty(riskLevel)){
            AppPremisesRecommendationDto recommendationDtoRisk = new AppPremisesRecommendationDto();
            recommendationDtoRisk.setAppPremCorreId(appPremisesCorrelationId);
            recommendationDtoRisk.setRecomDecision(riskLevel);
            recommendationDtoRisk.setRecomType(InspectionConstants.RECOM_TYPE_INSPCTION_RISK_LEVEL);
            insRepService.updateRiskRecommendation(recommendationDtoRisk);
        }
        if(!StringUtil.isEmpty(followUpAction)){
            AppPremisesRecommendationDto followRecommendationDtoFollow = new AppPremisesRecommendationDto();
            followRecommendationDtoFollow.setAppPremCorreId(appPremisesCorrelationId);
            followRecommendationDtoFollow.setRemarks(followUpAction);
            followRecommendationDtoFollow.setRecomType(InspectionConstants.RECOM_TYPE_INSPCTION_FOLLOW_UP_ACTION);
            insRepService.updateFollowRecommendation(followRecommendationDtoFollow);
        }else {
            AppPremisesRecommendationDto followRecommendationDtoFollow = new AppPremisesRecommendationDto();
            followRecommendationDtoFollow.setAppPremCorreId(appPremisesCorrelationId);
            followRecommendationDtoFollow.setRemarks(null);
            insRepService.updateFollowRecommendation(followRecommendationDtoFollow);
        }
    }

    private List<SelectOption> getChronoOption() {
        List<SelectOption> chronoResult = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption("Year", "Year");
        SelectOption so2 = new SelectOption("Month", "Month");
        SelectOption so3 = new SelectOption("Week", "Week");
        chronoResult.add(so1);
        chronoResult.add(so2);
        chronoResult.add(so3);
        return chronoResult;
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

    private List<SelectOption> getProcessingDecision() {
        List<SelectOption> riskLevelResult = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption(APPROVAL, "Acknowledge Inspection Report");
        SelectOption so2 = new SelectOption(REJECT, "Revise Inspection Report");
        riskLevelResult.add(so1);
        riskLevelResult.add(so2);
        return riskLevelResult;
    }

    private List<SelectOption> getRecommendationOption() {
        List<SelectOption> recommendationResult = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption("Approval", "ACCEPT");
        SelectOption so2 = new SelectOption("Reject", "REJECT");
        SelectOption so3 = new SelectOption(OTHERS, "Other");
        recommendationResult.add(so1);
        recommendationResult.add(so2);
        recommendationResult.add(so3);
        return recommendationResult;
    }


}
