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
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.*;

/**
 * @author weilu
 * @date 2020/2/14 14:00
 */
@Delegator(value = "auditInspectionReport")
@Slf4j
public class AuditInspectionReport {

    @Autowired
    private InsRepService insRepService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    private ApplicationViewService applicationViewService;

    private final String RECOMMENDATION_DTO = "appPremisesRecommendationDto";
    private final String RECOMMENDATION = "recommendation";
    private final String CHRONO = "chrono";
    private final String NUMBER = "number";
    private final String OTHERS = "Others";

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
        AuditTrailHelper.auditFunction("Audit Inspection Report", "Assign Report");
    }

    public void init(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", null);
        ParamUtil.setSessionAttr(bpc.request, RECOMMENDATION_DTO, null);
        String taskId = ParamUtil.getRequestString(bpc.request, "taskId");
        if (StringUtil.isEmpty(taskId)) {
            taskId = "F2733132-A137-EA11-BE7E-000C29F371DC";
        }
        TaskDto taskDto = taskService.getTaskById(taskId);
        String correlationId = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(correlationId);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, "insRepDto");
        if (insRepDto == null) {
            insRepDto = insRepService.getInsRepDto(taskDto, applicationViewDto, loginContext);
            InspectionReportDto inspectorUser = insRepService.getInspectorUser(taskDto, loginContext);
            insRepDto.setReportedBy(inspectorUser.getReportedBy());
            insRepDto.setReportNoteBy(inspectorUser.getReportNoteBy());
            insRepDto.setInspectors(inspectorUser.getInspectors());
        }
        //initRecommendation(correlationId,applicationViewDto,bpc);
        List<SelectOption> riskOption = insRepService.getRiskOption(applicationViewDto);
        List<SelectOption> chronoOption = getChronoOption();
        List<SelectOption> recommendationOption = getRecommendationOption();
        List<SelectOption> riskLevelOptions = getriskLevel();
        List<SelectOption> frameworknOption = getFrameworknOption();
        List<SelectOption> processingDe = getProcessingDecision();
        ParamUtil.setSessionAttr(bpc.request, "processingDe", (Serializable) processingDe);
        ParamUtil.setSessionAttr(bpc.request, "riskLevelOptions", (Serializable) riskLevelOptions);
        ParamUtil.setSessionAttr(bpc.request, "recommendationOption", (Serializable) recommendationOption);
        ParamUtil.setSessionAttr(bpc.request, "chronoOption", (Serializable) chronoOption);
        ParamUtil.setSessionAttr(bpc.request, "riskOption", (Serializable) riskOption);
        ParamUtil.setSessionAttr(bpc.request, "frameworknOption", (Serializable) frameworknOption);
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
    }

    public void prepare(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
    }

    public void saveData(BaseProcessClass bpc) throws FeignException {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
        log.debug(StringUtil.changeForLog("the inspectorReportSave start ...."));
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, "insRepDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        AppPremisesRecommendationDto appPremisesRecommendationDto = prepareRecommendation(bpc);
        ParamUtil.setSessionAttr(bpc.request, RECOMMENDATION_DTO, appPremisesRecommendationDto);
        Map<String, String> errorMap = new HashMap<>(34);
        ValidationResult validationResult = WebValidationHelper.validateProperty(appPremisesRecommendationDto, "save");
        if (validationResult.isHasErrors()) {
            errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            return;
        }
        List<AppPremisesRecommendationDto> appPremisesRecommendationDtoList = prepareForSave(bpc, appPremisesCorrelationId);
        saveRecommendations(appPremisesRecommendationDtoList);
//        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
//        AppPremisesRecommendationDto appPremisesRecDto = appPremisesRecommendationDtoList.get(0);
//        insRepService.routingTaskToAo1(taskDto, applicationDto, appPremisesCorrelationId,appPremisesRecDto);
//        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
    }

    private AppPremisesRecommendationDto prepareRecommendation(BaseProcessClass bpc) {
        String riskLevel = ParamUtil.getRequestString(bpc.request, "riskLevel");
        String processingDecision = ParamUtil.getRequestString(bpc.request, "processingDecision");
        String processRemarks = ParamUtil.getRequestString(bpc.request, "processRemarks");
        String remarks = ParamUtil.getRequestString(bpc.request, "remarks");
        String recommendation = ParamUtil.getRequestString(bpc.request, RECOMMENDATION);
        String periods = ParamUtil.getRequestString(bpc.request, "periods");
        String followUpAction = ParamUtil.getRequestString(bpc.request, "followUpAction");
        String chrono = ParamUtil.getRequestString(bpc.request, CHRONO);
        String number = ParamUtil.getRequestString(bpc.request, NUMBER);
        String tcuNeeded = ParamUtil.getRequestString(bpc.request, "tcuNeeded");
        String tcuDateStr = ParamUtil.getRequestString(bpc.request, "tcuDate");
        Date tcuDate = DateUtil.parseDate(tcuDateStr, "dd/MM/yyyy");
        String enforcement = ParamUtil.getRequestString(bpc.request, "engageEnforcement");
        String enforcementRemarks = ParamUtil.getRequestString(bpc.request, "enforcementRemarks");
        ParamUtil.setSessionAttr(bpc.request, CHRONO, chrono);
        ParamUtil.setSessionAttr(bpc.request, NUMBER, number);
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        appPremisesRecommendationDto.setRemarks(remarks);
        appPremisesRecommendationDto.setRecommendation(recommendation);
        appPremisesRecommendationDto.setPeriod(periods);
        appPremisesRecommendationDto.setTcuNeeded(tcuNeeded);
        appPremisesRecommendationDto.setTcuDate(tcuDate);
        appPremisesRecommendationDto.setEngageEnforcement(enforcement);
        appPremisesRecommendationDto.setEngageEnforcementRemarks(enforcementRemarks);
        appPremisesRecommendationDto.setRiskLevel(riskLevel);
        appPremisesRecommendationDto.setFollowUpAction(followUpAction);
        appPremisesRecommendationDto.setProcessingDecision(processingDecision);
        appPremisesRecommendationDto.setProcessRemarks(processRemarks);
        return appPremisesRecommendationDto;
    }

    private List<AppPremisesRecommendationDto> prepareForSave(BaseProcessClass bpc, String appPremisesCorrelationId) {
        List<AppPremisesRecommendationDto> appPremisesRecommendationDtos = new ArrayList<>();
        String riskLevel = ParamUtil.getRequestString(bpc.request, "riskLevel");
        String remarks = ParamUtil.getRequestString(bpc.request, "remarks");
        String followUpAction = ParamUtil.getRequestString(bpc.request, "followUpAction");
        String periods = ParamUtil.getRequestString(bpc.request, "periods");
        String recommendation = ParamUtil.getRequestString(bpc.request, RECOMMENDATION);
        String chrono = ParamUtil.getRequestString(bpc.request, CHRONO);
        String number = ParamUtil.getRequestString(bpc.request, NUMBER);
        String tcuDateStr = ParamUtil.getRequestString(bpc.request, "tcuDate");
        Date tcuDate = DateUtil.parseDate(tcuDateStr, "dd/MM/yyyy");
        String enforcementRemarks = ParamUtil.getRequestString(bpc.request, "enforcementRemarks");
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        appPremisesRecommendationDto.setRemarks(remarks);
        appPremisesRecommendationDto.setRecomInDate(new Date());
        appPremisesRecommendationDto.setRecomDecision(InspectionConstants.PROCESS_DECI_REVIEW_INSPECTION_REPORT);
        appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
        appPremisesRecommendationDto.setRecommendation(recommendation);
        appPremisesRecommendationDto.setRecomDecision("Approved");
        if (OTHERS.equals(periods) && !StringUtil.isEmpty(chrono) && !StringUtil.isEmpty(number)) {
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
            appPremisesRecommendationDto.setChronoUnit(chrono);
            appPremisesRecommendationDto.setRecomInNumber(Integer.parseInt(number));
        } else if (!StringUtil.isEmpty(periods) && !OTHERS.equals(periods)) {
            String[] split_number = periods.split("\\D");
            String[] split_unit = periods.split("\\d");
            String chronoRe = split_unit[1];
            String numberRe = split_number[0];
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
            appPremisesRecommendationDto.setChronoUnit(chronoRe);
            appPremisesRecommendationDto.setRecomInNumber(Integer.parseInt(numberRe));
            appPremisesRecommendationDto.setRecommendation(recommendation);
        }else if("Reject".equals(recommendation)) {
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
            appPremisesRecommendationDto.setProcessingDecision("Reject");
        }

        AppPremisesRecommendationDto tcuAppPremisesRecommendationDto = new AppPremisesRecommendationDto();
        tcuAppPremisesRecommendationDto.setRecomInDate(tcuDate);
        tcuAppPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPCTION_TCU_NEEDED);
        tcuAppPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);

        AppPremisesRecommendationDto engageEnforcementAppPremisesRecommendationDto = new AppPremisesRecommendationDto();
        engageEnforcementAppPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPCTION_ENGAGE);
        engageEnforcementAppPremisesRecommendationDto.setRemarks(enforcementRemarks);
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
        appPremisesRecommendationDtos.add(tcuAppPremisesRecommendationDto);
        appPremisesRecommendationDtos.add(engageEnforcementAppPremisesRecommendationDto);
        appPremisesRecommendationDtos.add(riskLevelAppPremisesRecommendationDto);
        appPremisesRecommendationDtos.add(followAppPremisesRecommendationDto);
        return appPremisesRecommendationDtos;
    }

    private List<LicPremisesRecommendationDto> transform(List<AppPremisesRecommendationDto> appPremisesRecommendationDtos){
        List<LicPremisesRecommendationDto> licPremisesRecommendationDtos = new ArrayList<>();
        for(AppPremisesRecommendationDto appPremisesRecommendationDto :appPremisesRecommendationDtos){
            LicPremisesRecommendationDto licPremisesRecommendationDto = new LicPremisesRecommendationDto();
            String appPremCorreId = appPremisesRecommendationDto.getAppPremCorreId();
            licPremisesRecommendationDto.setLicPremId("CFB51822-A656-EA11-BE7F-000C29F371DC");
            String recomType = appPremisesRecommendationDto.getRecomType();
            licPremisesRecommendationDto.setRecomType(recomType);
            String recomDecision = appPremisesRecommendationDto.getRecomDecision();
            licPremisesRecommendationDto.setRecomDecision(recomDecision);
            Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
            licPremisesRecommendationDto.setRecomInNumber(recomInNumber);
            String chronoUnit = appPremisesRecommendationDto.getChronoUnit();
            licPremisesRecommendationDto.setChronoUnit(chronoUnit);
            Date recomInDate = appPremisesRecommendationDto.getRecomInDate();
            licPremisesRecommendationDto.setRecomInDate(recomInDate);
            String bestPractice = appPremisesRecommendationDto.getBestPractice();
            licPremisesRecommendationDto.setBestPractice(bestPractice);
            String remarks = appPremisesRecommendationDto.getRemarks();
            licPremisesRecommendationDto.setRemarks(remarks);
            licPremisesRecommendationDtos.add(licPremisesRecommendationDto);
        }
        return licPremisesRecommendationDtos;


    }

    private List<SelectOption> getChronoOption() {
        List<SelectOption> ChronoResult = new ArrayList<>();
        SelectOption so1 = new SelectOption("Year", "Year");
        SelectOption so2 = new SelectOption("Month", "Month");
        SelectOption so3 = new SelectOption("Week", "Week");
        ChronoResult.add(so1);
        ChronoResult.add(so2);
        ChronoResult.add(so3);
        return ChronoResult;
    }

    private List<SelectOption> getRecommendationOption() {
        List<SelectOption> recommendationResult = new ArrayList<>();
        SelectOption so1 = new SelectOption("Approved", "Proceed with Licence Issuance");
        SelectOption so2 = new SelectOption("Approved", "Proceed with Licence Issuance (with LTCs)");
        SelectOption so3 = new SelectOption("Rejected", "Reject Licence");
        recommendationResult.add(so1);
        recommendationResult.add(so2);
        recommendationResult.add(so3);
        return recommendationResult;
    }

    private List<SelectOption> getriskLevel() {
        List<SelectOption> riskLevelResult = new ArrayList<>();
        SelectOption so1 = new SelectOption("Low", "Low");
        SelectOption so2 = new SelectOption("Moderate", "Moderate");
        SelectOption so3 = new SelectOption("High", "High");
        riskLevelResult.add(so1);
        riskLevelResult.add(so2);
        riskLevelResult.add(so3);
        return riskLevelResult;
    }

    private List<SelectOption> getFrameworknOption() {
        List<SelectOption> recommendationResult = new ArrayList<>();
        SelectOption so1 = new SelectOption("LGR", "Leadership and Governance Risk");
        SelectOption so2 = new SelectOption("CR", "Compliance Risk");
        recommendationResult.add(so1);
        recommendationResult.add(so2);
        return recommendationResult;
    }
    private List<SelectOption> getProcessingDecision() {
        List<SelectOption> riskLevelResult = new ArrayList<>();
        SelectOption so1 = new SelectOption("submit", "Submit Inspection Report for review");
        riskLevelResult.add(so1);
        return riskLevelResult;
    }

    private void initRecommendation(String correlationId, ApplicationViewDto applicationViewDto, BaseProcessClass bpc){
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        AppPremisesRecommendationDto tcuRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_TCU_NEEDED).getEntity();
        AppPremisesRecommendationDto engageRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_ENGAGE).getEntity();
        AppPremisesRecommendationDto riskRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_RISK_LEVEL).getEntity();
        AppPremisesRecommendationDto followRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_FOLLOW_UP_ACTION).getEntity();
        if (appPremisesRecommendationDto != null) {
            String chronoUnit = appPremisesRecommendationDto.getChronoUnit();
            Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
            String option = recomInNumber + chronoUnit;
            List<String> periods = insRepService.getPeriods(applicationViewDto);
            if (periods != null && !periods.isEmpty()) {
                for (String period : periods) {
                    if (option.equals(period)) {
                        ParamUtil.setSessionAttr(bpc.request, "option", option);
                        break;
                    } else {
                        ParamUtil.setSessionAttr(bpc.request, "option", "Others");
                        ParamUtil.setSessionAttr(bpc.request, "recnumber", recomInNumber);
                        ParamUtil.setSessionAttr(bpc.request, "recchrono", chronoUnit);
                        break;
                    }
                }
            }
            String recomDecision = appPremisesRecommendationDto.getRecomDecision();
            String codeDesc = MasterCodeUtil.getCodeDesc(recomDecision);
            ParamUtil.setSessionAttr(bpc.request, "recomDecision", codeDesc);
        } else {
            ParamUtil.setSessionAttr(bpc.request, "option", null);
        }

        if (appPremisesRecommendationDto != null) {
            String reportRemarks = appPremisesRecommendationDto.getRemarks();
            ParamUtil.setSessionAttr(bpc.request, "reportRemarks", reportRemarks);
        }

        if (tcuRecommendationDto != null) {
            Date recomInDate = tcuRecommendationDto.getRecomInDate();
            String tcuNeed = "on";
            ParamUtil.setSessionAttr(bpc.request, "recomInDate", recomInDate);
            ParamUtil.setSessionAttr(bpc.request, "tcuNeed", tcuNeed);
        }
        if (engageRecommendationDto != null) {
            String remarks = engageRecommendationDto.getRemarks();
            String engage = "on";
            ParamUtil.setSessionAttr(bpc.request, "remarks", remarks);
            ParamUtil.setSessionAttr(bpc.request, "engage", engage);
        }
        if (riskRecommendationDto != null) {
            String riskLevel = riskRecommendationDto.getRecomDecision();
            ParamUtil.setSessionAttr(bpc.request, "riskLevel", riskLevel);
        }
        if (followRecommendationDto != null) {
            String followRemarks = followRecommendationDto.getRemarks();
            ParamUtil.setSessionAttr(bpc.request, "followRemarks", followRemarks);
        }
    }

    private void saveRecommendations(List<AppPremisesRecommendationDto> appPremisesRecommendationDtoList){
        List<LicPremisesRecommendationDto> licPremisesRecommendationDtos = transform(appPremisesRecommendationDtoList);
        LicPremisesRecommendationDto licPremisesRecommendationDto1 = licPremisesRecommendationDtos.get(0);
        LicPremisesRecommendationDto licPremisesRecommendationDto2 = licPremisesRecommendationDtos.get(1);
        LicPremisesRecommendationDto licPremisesRecommendationDto3 = licPremisesRecommendationDtos.get(2);
        LicPremisesRecommendationDto licPremisesRecommendationDto4 = licPremisesRecommendationDtos.get(3);
        LicPremisesRecommendationDto licPremisesRecommendationDto5 = licPremisesRecommendationDtos.get(4);

        insRepService.saveAuditRecommendation(licPremisesRecommendationDto1);
        Date recomInDate = licPremisesRecommendationDto2.getRecomInDate();
        if (recomInDate != null) {
            insRepService.saveAuditRecommendation(licPremisesRecommendationDto2);
        }
        String remarks = licPremisesRecommendationDto3.getRemarks();
        if (!StringUtil.isEmpty(remarks)) {
            licPremisesRecommendationDto3.setRemarks(remarks);
            insRepService.saveAuditRecommendation(licPremisesRecommendationDto3);
        }
        String riskLevel = licPremisesRecommendationDto4.getRecomDecision();
        if (!StringUtil.isEmpty(riskLevel)) {
            insRepService.saveAuditRecommendation(licPremisesRecommendationDto4);
        }
        String followRemarks = licPremisesRecommendationDto5.getRemarks();
        if (!StringUtil.isEmpty(followRemarks)) {
            insRepService.saveAuditRecommendation(licPremisesRecommendationDto5);
        }
    }
}
