package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
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
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
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
import sop.util.DateUtil;
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
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    private final String RECOMMENDATION_DTO= "appPremisesRecommendationDto";
    private final String RECOMMENDATION="recommendation";
    private final String CHRONO="chrono";
    private final String NUMBER="number";
    private final String OTHERS="Others";
    private final String APPROVAL="Approval";
    private final String REJECT="Reject";

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
        AuditTrailHelper.auditFunction("Inspection Report", "Assign Report");
    }

    public void AoInit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionReportInit start ...."));
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", null);
        ParamUtil.setSessionAttr(bpc.request, RECOMMENDATION_DTO, null);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", null);

        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String taskId = ParamUtil.getString(bpc.request,"taskId");
        if(StringUtil.isEmpty(taskId)){
            taskId = "BB8C47A3-9B37-EA11-BE7E-000C29F371DC";
        }
        TaskDto taskDto = taskService.getTaskById(taskId);
        String correlationId = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = insRepService.getApplicationViewDto(correlationId);
        InspectionReportDto insRepDto = insRepService.getInsRepDto(taskDto,applicationViewDto,loginContext);
        InspectionReportDto inspectorAo = insRepService.getInspectorAo(taskDto,applicationViewDto);
        insRepDto.setInspectors(inspectorAo.getInspectors());
        insRepDto.setReportNoteBy(inspectorAo.getReportNoteBy());
        insRepDto.setReportedBy(inspectorAo.getReportedBy());
        initAoRecommendation(correlationId,bpc);

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
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
    }


    public void AoReportPre(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>prepareReportData");
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
    }
    public void approve(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the inspectorReportAction start ...."));
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, "insRepDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        TaskDto taskDto =  (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        AppPremisesRecommendationDto preapreRecommendationDto = prepareRecommendation(bpc);
        ParamUtil.setSessionAttr(bpc.request, "preapreRecommendationDto", preapreRecommendationDto);
        ValidationResult validationResult = WebValidationHelper.validateProperty(preapreRecommendationDto, "edit");
        if (validationResult.isHasErrors()) {
            String report = "Y";
            ParamUtil.setRequestAttr(bpc.request, "report",report);
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.FALSE);
            return;
        }
        saveAoRecommendation(appPremisesCorrelationId,bpc,preapreRecommendationDto);
        insRepService.routingTaskToAo2(taskDto,applicationDto,appPremisesCorrelationId);
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
        ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
    }

    private AppPremisesRecommendationDto prepareSaveRecommendation (BaseProcessClass bpc,String appPremisesCorrelationId){
        String processingDecision = ParamUtil.getRequestString(bpc.request, "processingDecision");
        String chrono = ParamUtil.getRequestString(bpc.request, CHRONO);
        String number = ParamUtil.getRequestString(bpc.request, NUMBER);
        String periods = ParamUtil.getRequestString(bpc.request, "periods");
        ParamUtil.setSessionAttr(bpc.request, CHRONO, chrono);
        ParamUtil.setSessionAttr(bpc.request, NUMBER, number);
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        appPremisesRecommendationDto.setRecomInDate(new Date());
        appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
        if(APPROVAL.equals(processingDecision)){
            appPremisesRecommendationDto.setRecomDecision(ApplicationConsts.APPLICATION_STATUS_APPROVED);
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
        }else if(REJECT.equals(processingDecision)){
            appPremisesRecommendationDto.setRecomDecision(ApplicationConsts.PROCESSING_DECISION_REJECT);
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
        }
        return appPremisesRecommendationDto;
    }
    private AppPremisesRecommendationDto prepareRecommendation (BaseProcessClass bpc){
        String periods = ParamUtil.getRequestString(bpc.request, "periods");
        String riskLevel = ParamUtil.getRequestString(bpc.request, "riskLevel");
        String processingDecision = ParamUtil.getRequestString(bpc.request, "processingDecision");
        String followUpAction = ParamUtil.getRequestString(bpc.request, "followUpAction");
        String processRemarks = ParamUtil.getRequestString(bpc.request, "processRemarks");
        String recommendation = ParamUtil.getRequestString(bpc.request, RECOMMENDATION);
        String chrono = ParamUtil.getRequestString(bpc.request, CHRONO);
        String number = ParamUtil.getRequestString(bpc.request, NUMBER);
        ParamUtil.setSessionAttr(bpc.request, CHRONO, chrono);
        ParamUtil.setSessionAttr(bpc.request, NUMBER, number);
        String enforcement = ParamUtil.getRequestString(bpc.request, "engageEnforcement");
        String enforcementRemarks = ParamUtil.getRequestString(bpc.request, "enforcementRemarks");
        ParamUtil.setSessionAttr(bpc.request, CHRONO, chrono);
        ParamUtil.setSessionAttr(bpc.request, NUMBER, number);
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        appPremisesRecommendationDto.setRecommendation(recommendation);
        appPremisesRecommendationDto.setPeriod(periods);
        appPremisesRecommendationDto.setEngageEnforcement(enforcement);
        appPremisesRecommendationDto.setEngageEnforcementRemarks(enforcementRemarks);
        appPremisesRecommendationDto.setRiskLevel(riskLevel);
        appPremisesRecommendationDto.setFollowUpAction(followUpAction);
        appPremisesRecommendationDto.setProcessingDecision(processingDecision);
        appPremisesRecommendationDto.setProcessRemarks(processRemarks);
        return appPremisesRecommendationDto;
    }

    private List<SelectOption> getChronoOption() {
        List<SelectOption> ChronoResult = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption("Year", "Year");
        SelectOption so2 = new SelectOption("Month", "Month");
        SelectOption so3 = new SelectOption("Week", "Week");
        ChronoResult.add(so1);
        ChronoResult.add(so2);
        ChronoResult.add(so3);
        return ChronoResult;
    }

    private List<SelectOption> getriskLevel() {
        List<SelectOption> riskLevelResult = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption("Low", "Low");
        SelectOption so2 = new SelectOption("Moderate", "Moderate");
        SelectOption so3 = new SelectOption("High", "High");
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

    private void initAoRecommendation(String correlationId,BaseProcessClass bpc){
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        AppPremisesRecommendationDto engageRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_ENGAGE).getEntity();
        AppPremisesRecommendationDto riskRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_RISK_LEVEL).getEntity();
        AppPremisesRecommendationDto followRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_FOLLOW_UP_ACTION).getEntity();

        String option = null;
        String inspectorRemarks = null;
        if(appPremisesRecommendationDto!=null){
            String chronoUnit = appPremisesRecommendationDto.getChronoUnit();
            Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
            if(StringUtil.isEmpty(chronoUnit)&&StringUtil.isEmpty(recomInNumber)){
                option = appPremisesRecommendationDto.getRecomDecision();
            }else {
                option  = recomInNumber + chronoUnit;
            }
            inspectorRemarks = appPremisesRecommendationDto.getRemarks();
            ParamUtil.setSessionAttr(bpc.request, "option", option);
            ParamUtil.setSessionAttr(bpc.request, "inspectorRemarks", inspectorRemarks);
        }
        if(engageRecommendationDto!=null){
            String remarks = engageRecommendationDto.getRemarks();
            String engage = "on";
            ParamUtil.setSessionAttr(bpc.request, "remarks", remarks);
            ParamUtil.setSessionAttr(bpc.request, "engage", engage);
        }
        if(riskRecommendationDto!=null){
            String riskLevel = riskRecommendationDto.getRecomDecision();
            ParamUtil.setSessionAttr(bpc.request, "riskLevel", riskLevel);
        }
        if(followRecommendationDto!=null){
            String followRemarks = followRecommendationDto.getRemarks();
            ParamUtil.setSessionAttr(bpc.request, "followRemarks", followRemarks);
        }
    }

    private void saveAoRecommendation(String appPremisesCorrelationId,BaseProcessClass bpc,AppPremisesRecommendationDto preapreRecommendationDto){
        AppPremisesRecommendationDto recommendationDto = prepareSaveRecommendation(bpc, appPremisesCorrelationId);
        insRepService.updateRecommendation(recommendationDto);
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
        }
    }
}
