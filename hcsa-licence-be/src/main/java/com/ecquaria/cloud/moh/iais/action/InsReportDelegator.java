package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.EngineHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionReportConstants;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
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
    private FillupChklistService fillupChklistService;
    @Autowired
    private VehicleCommonController vehicleCommonController;
    private final static String RECOMMENDATION_DTO = "appPremisesRecommendationDto";
    private final static String RECOMMENDATION = "recommendation";
    private final static String CHRONO = "chrono";
    private final static String NUMBER = "number";
    private final static String OTHERS = "Others";

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
        clearSession(bpc.request);
        SearchParam searchParamGroup = (SearchParam)ParamUtil.getSessionAttr(bpc.request, "backendinboxSearchParam");
        ParamUtil.setSessionAttr(bpc.request,"backSearchParamFromHcsaApplication",searchParamGroup);
    }
    public void clearSession(HttpServletRequest request ){
        ParamUtil.setSessionAttr(request, "insRepDto", null);
        ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.REPORT_ACK_CLARIFICATION_FLAG,null);
        ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE,null);
        vehicleCommonController.clearVehicleInformationSession(request);
    }

    public void inspectionReportInit(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the inspectionReportInit start ...."));
        String taskId = null;
        HttpServletRequest request = bpc.request;
        try{
            taskId = ParamUtil.getMaskedString(request,"taskId");
        }catch(MaskAttackException e){
            log.error(e.getMessage(),e);
            IaisEGPHelper.redirectUrl(bpc.response, "https://"+request.getServerName()+"/hcsa-licence-web/CsrfErrorPage.jsp");
        }
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, AuditTrailConsts.FUNCTION_INSPECTION_REPORT);
        TaskDto taskDto = taskService.getTaskById(taskId);
        String correlationId = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = insRepService.getApplicationViewDto(correlationId);
        AuditTrailHelper.auditFunctionWithAppNo(AuditTrailConsts.MODULE_INSPECTION, AuditTrailConsts.FUNCTION_INSPECTION_REPORT, applicationViewDto.getApplicationDto().getApplicationNo());
        if(fillupChklistService.checklistNeedVehicleSeparation(applicationViewDto)){
            ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE,AppConsts.YES);
        }
        ParamUtil.setSessionAttr(request, "taskDto", taskDto);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(request, "insRepDto");
        if (insRepDto == null) {
            insRepDto = insRepService.getInsRepDto(taskDto, applicationViewDto, loginContext);
            InspectionReportDto inspectorUser = insRepService.getInspectorUser(taskDto, loginContext);
            insRepDto.setInspectors(inspectorUser.getInspectors());
        }
        String appStatus = applicationViewDto.getApplicationDto().getStatus();
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        if (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW.equals(appStatus) || ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR.equals(appStatus)|| ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(appStatus) || ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION.equals(appStatus)) {
            appPremisesRecommendationDto = initRecommendation(correlationId, applicationViewDto, bpc);
        }
        String recommendation = appPremisesRecommendationDto.getRecommendation();
        if(StringUtil.isEmpty(recommendation)){
            String periodDefault = insRepService.getPeriodDefault(applicationViewDto,taskDto);
            appPremisesRecommendationDto.setPeriod(periodDefault);
        }
        AppPremisesRecommendationDto accRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPECTYPE).getEntity();
        if (accRecommendationDto != null) {
            String recomDecision = accRecommendationDto.getRecomDecision();
            if (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT.equals(appStatus)&&InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION_CONDITION.equals(recomDecision)) {
                appPremisesRecommendationDto.setRecommendation(InspectionReportConstants.APPROVEDLTC);
            }
        }
        String riskLevelForSave = appPremisesRecommendationDto.getRiskLevel();
        List<SelectOption> riskOption = insRepService.getRiskOption(applicationViewDto);
        List<SelectOption> chronoOption = getChronoOption();
        List<SelectOption> recommendationOption = getRecommendationOption(applicationType);
        List<SelectOption> processingDe = getProcessingDecision(appStatus);
        String infoClassTop = "active";
        String infoClassBelow = "tab-pane active";
        String reportClassBelow = "tab-pane";
        String kpiInfo = MessageUtil.getMessageDesc("LOLEV_ACK051");
        ParamUtil.setSessionAttr(request, "kpiInfo", kpiInfo);
        ParamUtil.setRequestAttr(request, "appPremisesRecommendationDto", appPremisesRecommendationDto);
        ParamUtil.setSessionAttr(request, "appType", null);
        ParamUtil.setSessionAttr(request, "infoClassTop", infoClassTop);
        ParamUtil.setSessionAttr(request, "reportClassTop", null);
        ParamUtil.setSessionAttr(request, "infoClassBelow", infoClassBelow);
        ParamUtil.setSessionAttr(request, "reportClassBelow", reportClassBelow);
        ParamUtil.setSessionAttr(request, "processingDe", (Serializable) processingDe);
        ParamUtil.setSessionAttr(request, "recommendationOption", (Serializable) recommendationOption);
        ParamUtil.setSessionAttr(request, "chronoOption", (Serializable) chronoOption);
        ParamUtil.setSessionAttr(request, "riskOption", (Serializable) riskOption);
        ParamUtil.setSessionAttr(request, "insRepDto", insRepDto);
        ParamUtil.setSessionAttr(request, "applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(request, "riskLevelForSave", riskLevelForSave);
        vehicleCommonController.setVehicleInformation(request,taskDto,applicationViewDto);
    }

    public void inspectionReportPre(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionReportPre start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        ParamUtil.setSessionAttr(bpc.request, "appType", applicationType);
    }

    public void inspectorReportSave(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the inspectorReportSave start ...."));
        HttpServletRequest request = bpc.request;
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        Date recomLiceStartDate = applicationViewDto.getRecomLiceStartDate();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String status = applicationDto.getStatus();
        String applicationType = applicationDto.getApplicationType();
        List<String> appTypes = IaisCommonUtils.genNewArrayList();
        appTypes.add(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        appTypes.add(ApplicationConsts.APPLICATION_TYPE_APPEAL);
        appTypes.add(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL);
        appTypes.add(ApplicationConsts.APPLICATION_TYPE_CESSATION);
        AppPremisesRecommendationDto appPremisesRecommendationDto = prepareRecommendation(bpc,applicationType,appTypes);
        ParamUtil.setSessionAttr(bpc.request, RECOMMENDATION_DTO, appPremisesRecommendationDto);
        if (ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationType)) {
            appPremisesRecommendationDto.setRecommendation("Audit");
        }else if (ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationType)) {
            appPremisesRecommendationDto.setRecommendation("Post");
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ValidationResult validationResult = WebValidationHelper.validateProperty(appPremisesRecommendationDto, "save");
        if(appTypes.contains(applicationType)){
            String recommendationRfc = ParamUtil.getRequestString(bpc.request, "recommendationRfc");
            if(StringUtil.isEmpty(recommendationRfc)){
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","Recommendation", "field");
                errorMap.put("recommendationRfc", errMsg);
            }
        }
        if (validationResult.isHasErrors()) {
            Map<String, String> stringStringMap = validationResult.retrieveAll();
            errorMap.putAll(stringStringMap);
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            WebValidationHelper.saveAuditTrailForNoUseResult(applicationDto,errorMap);
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
        // save veh inf
        insRepService.saveAppVehs((String)ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.APP_VEHICLE_FLAG),ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equalsIgnoreCase(applicationType) ? applicationViewDto.getVehicleRfcShowDtos():applicationViewDto.getAppSvcVehicleDtos());
        if (ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR.equals(status)) {
            insRepService.routTaskToRoutBack(bpc, taskDto, applicationDto, appPremisesCorrelationId, appPremisesRecommendationDto.getProcessRemarks());
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            ParamUtil.setSessionAttr(bpc.request,HcsaLicenceBeConstant.REPORT_ACK_CLARIFICATION_FLAG,SystemAdminBaseConstants.YES);
            return;
        }
        if (ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(status)) {
            //insRepService.routTaskToRoutBackAo3(bpc, taskDto, applicationDto, appPremisesCorrelationId, appPremisesRecommendationDto.getProcessRemarks(),false);
            log.info(StringUtil.changeForLog("The inspectionReport do the broadcast reply"));
            EngineHelper.delegate("hcsaApplicationDelegator", "broadcastReply", bpc);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            ParamUtil.setSessionAttr(bpc.request,HcsaLicenceBeConstant.REPORT_ACK_CLARIFICATION_FLAG,SystemAdminBaseConstants.YES);
            return;
        }
        insRepService.routingTaskToAo1(taskDto, applicationDto, appPremisesCorrelationId, appPremisesRecommendationDto);
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
    }

    private AppPremisesRecommendationDto prepareRecommendation(BaseProcessClass bpc,String appType,List<String> appTypes) {
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
        if(appTypes.contains(appType)){
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
           if(InspectionReportConstants.RFC_APPROVED.equals(recommendationRfc)){
               appPremisesRecommendationDto.setRecomInNumber(6);
           }else {
               appPremisesRecommendationDto.setRecomInNumber(0);
           }
           //appPremisesRecommendationDto.setChronoUnit(AppointmentConstants.RECURRENCE_MONTH);
           appPremisesRecommendationDto.setRemarks(remarks);
           appPremisesRecommendationDto.setRecommendation(recommendationRfc);
           appPremisesRecommendationDto.setRecomDecision(recommendationRfc);
           appPremisesRecommendationDto.setRecomInDate(licDate);
           appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
           appPremisesRecommendationDto.setPeriod(AppointmentConstants.RECURRENCE_MONTH);
           appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
        } else {
            appPremisesRecommendationDto.setRemarks(remarks);
            appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
            appPremisesRecommendationDto.setRecomInDate(licDate);
            if(InspectionReportConstants.APPROVED.equals(recommendation)||InspectionReportConstants.APPROVEDLTC.equals(recommendation)){
                appPremisesRecommendationDto.setRecomDecision(recommendation);
                if (OTHERS.equals(periods) && !StringUtil.isEmpty(chrono) && !StringUtil.isEmpty(number)) {
                    appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
                    Integer num = Integer.valueOf(number);
                    if(AppointmentConstants.RECURRENCE_YEAR.equals(chrono)){
                        chrono = AppointmentConstants.RECURRENCE_MONTH ;
                        num = Integer.valueOf(Integer.parseInt(number) * 12);
                        //BestPractice = RECURRENCE_YEAR, Determine that user Other has selected the year
                        appPremisesRecommendationDto.setBestPractice(AppointmentConstants.RECURRENCE_YEAR);
                    }
                    appPremisesRecommendationDto.setChronoUnit(chrono);
                    appPremisesRecommendationDto.setRecomInNumber(num);
                    appPremisesRecommendationDto.setRecommendation(recommendation);
                } else if (!StringUtil.isEmpty(periods) && !OTHERS.equals(periods) && InspectionReportConstants.APPROVEDLTC.equals(recommendation) || InspectionReportConstants.APPROVED.equals(recommendation)) {
                    String[] splitPeriods = periods.split("\\s+");
                    String count = splitPeriods[0];
                    String dateType = splitPeriods[1];
                    appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
                    appPremisesRecommendationDto.setChronoUnit(dateType);
                    appPremisesRecommendationDto.setRecomInNumber(Integer.valueOf(count));
                    appPremisesRecommendationDto.setRecommendation(recommendation);
                }
            }else {
                appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
                appPremisesRecommendationDto.setRecomDecision(InspectionReportConstants.REJECTED);
                appPremisesRecommendationDto.setRecomInNumber(0);
                appPremisesRecommendationDto.setRecommendation(recommendation);
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

        String riskLevelForSave = (String)ParamUtil.getSessionAttr(bpc.request, "riskLevelForSave");
        AppPremisesRecommendationDto riskLevelRecommendationDto = new AppPremisesRecommendationDto();
        riskLevelRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPCTION_RISK_LEVEL);
        riskLevelRecommendationDto.setRemarks(riskLevelForSave);
        riskLevelRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);

        appPremisesRecommendationDtos.add(appPremisesRecommendationDto);
        appPremisesRecommendationDtos.add(engageEnforcementAppPremisesRecommendationDto);
        appPremisesRecommendationDtos.add(followAppPremisesRecommendationDto);
        appPremisesRecommendationDtos.add(riskLevelRecommendationDto);
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
                    if(AppointmentConstants.RECURRENCE_YEAR.equalsIgnoreCase(appPremisesRecommendationDto.getBestPractice())){
                        initRecommendationDto.setRecomInNumber(recomInNumber/12);
                        initRecommendationDto.setChronoUnit(AppointmentConstants.RECURRENCE_YEAR);
                    }
                }
            }
            if (InspectionReportConstants.REJECTED.equals(recomDecision)) {
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
        AppPremisesRecommendationDto appPremisesRecommendationDto4 = appPremisesRecommendationDtoList.get(3);

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
        String remarks = appPremisesRecommendationDto4.getRemarks();
        if(!StringUtil.isEmpty(remarks)) {
            insRepService.updateRiskLevelRecommendation(appPremisesRecommendationDto4);
        }

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
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) ||
            ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(appType) ||
            ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(appType) ||
            ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(appType)) {
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
