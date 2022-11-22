package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.EngineHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionReportConstants;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.InspectionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApptInspectionDateService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.validation.InspectionCheckListItemValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private HcsaApplicationDelegator hcsaApplicationDelegator;
    @Autowired
    private InspectReviseNcEmailDelegator inspectReviseNcEmailDelegator;
    @Autowired
    private InspectionService inspectionService;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    private ApptInspectionDateService apptInspectionDateService;
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
        ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE,null);
        ParamUtil.setSessionAttr(request,"rollBackOptions",null);
        ParamUtil.setSessionAttr(request,"rollBackToValueMap",null);
        ParamUtil.setSessionAttr(request, "lrSelect", null);
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
        ApplicationViewDto applicationViewDto = insRepService.getApplicationViewDto(correlationId, taskDto.getRoleId());
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
        insRepDto.setAppPremSpecialSubSvcRelDtoList(applicationViewDto.getAppPremSpecialSubSvcRelDtoList().stream()
                .filter(dto->!ApplicationConsts.RECORD_ACTION_CODE_REMOVE.equals(dto.getActCode()))
                .collect(Collectors.toList()));
        String appStatus = applicationViewDto.getApplicationDto().getStatus();
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        if (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW.equals(appStatus) || ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR.equals(appStatus)|| ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(appStatus) || ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION.equals(appStatus)) {
            appPremisesRecommendationDto = insRepService.initRecommendation(correlationId, applicationViewDto);
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
        Map<String, AppPremisesRoutingHistoryDto> historyDtoMap = IaisCommonUtils.genNewHashMap();
        List<SelectOption> rollBackSelectOptions = inspectionService.getRollBackSelectOptions(applicationViewDto.getRollBackHistroyList(), historyDtoMap, taskDto.getRoleId());
        ParamUtil.setSessionAttr(request,"rollBackOptions",(Serializable) rollBackSelectOptions);
        ParamUtil.setSessionAttr(request,"rollBackValueMap", (Serializable) historyDtoMap);
        String riskLevelForSave = appPremisesRecommendationDto.getRiskLevel();
        List<SelectOption> riskOption = insRepService.getRiskOption(applicationViewDto);
        List<SelectOption> chronoOption = insRepService.getChronoOption();
        List<SelectOption> recommendationOption = insRepService.getRecommendationOption(applicationType);
        List<SelectOption> processingDe = getProcessingDecision(applicationViewDto);
        String infoClassTop = "active";
        String infoClassBelow = "tab-pane active";
        String reportClassBelow = "tab-pane";
        String kpiInfo = MessageUtil.getMessageDesc("LOLEV_ACK051");
        ParamUtil.setSessionAttr(request, "kpiInfo", kpiInfo);
        ParamUtil.setRequestAttr(request, "appPremisesRecommendationDto", appPremisesRecommendationDto);
        ParamUtil.setSessionAttr(request, "appType", null);
        ParamUtil.setSessionAttr(request, "infoClassTop", infoClassTop);
        ParamUtil.setSessionAttr(request, "reportClassTop", null);
        ParamUtil.setSessionAttr(request, "processClassTop", null);
        ParamUtil.setSessionAttr(request, "infoClassBelow", infoClassBelow);
        ParamUtil.setSessionAttr(request, "reportClassBelow", reportClassBelow);
        ParamUtil.setSessionAttr(request, "processClassBelow", "tab-pane");
        ParamUtil.setSessionAttr(request, "processingDe", (Serializable) processingDe);
        ParamUtil.setSessionAttr(request, "recommendationOption", (Serializable) recommendationOption);
        ParamUtil.setSessionAttr(request, "chronoOption", (Serializable) chronoOption);
        ParamUtil.setSessionAttr(request, "riskOption", (Serializable) riskOption);
        ParamUtil.setSessionAttr(request, "insRepDto", insRepDto);
        ParamUtil.setSessionAttr(request, "applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(request, "riskLevelForSave", riskLevelForSave);
        ParamUtil.setSessionAttr(bpc.request, HcsaLicenceBeConstant.APP_SPECIAL_FLAG,
                applicationService.getSubSvcFlagToShowOrEdit(taskDto,applicationViewDto));
        ParamUtil.setSessionAttr(bpc.request, HcsaLicenceBeConstant.APP_OTHER_FLAG,
                applicationService.getSubSvcFlagToShowOrEdit(taskDto,applicationViewDto));
        vehicleCommonController.setVehicleInformation(request,taskDto,applicationViewDto);
    }

    public void inspectionReportPre(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionReportPre start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        ParamUtil.setSessionAttr(bpc.request, "appType", applicationType);
        //Can edit application
        InspectionHelper.checkForEditingApplication(bpc.request);
        List<AppPremSubSvcRelDto> specialServiceList=applicationViewDto.getAppPremSpecialSubSvcRelDtoList();
        if (IaisCommonUtils.isNotEmpty(specialServiceList)){
            ParamUtil.setRequestAttr(bpc.request, "addSpecialServiceList", specialServiceList.stream()
                    .filter(dto->ApplicationConsts.RECORD_ACTION_CODE_ADD.equals(dto.getActCode()))
                    .collect(Collectors.toList()));
            ParamUtil.setRequestAttr(bpc.request, "removeSpecialServiceList", specialServiceList.stream()
                    .filter(dto->ApplicationConsts.RECORD_ACTION_CODE_REMOVE.equals(dto.getActCode()))
                    .collect(Collectors.toList()));
        }
        List<AppPremSubSvcRelDto> otherServiceList=applicationViewDto.getAppPremOthersSubSvcRelDtoList();
        if (IaisCommonUtils.isNotEmpty(otherServiceList)){
            ParamUtil.setRequestAttr(bpc.request, "addOtherServiceList", otherServiceList.stream()
                    .filter(dto->ApplicationConsts.RECORD_ACTION_CODE_ADD.equals(dto.getActCode()))
                    .collect(Collectors.toList()));
            ParamUtil.setRequestAttr(bpc.request, "removeOtherServiceList", otherServiceList.stream()
                    .filter(dto->ApplicationConsts.RECORD_ACTION_CODE_REMOVE.equals(dto.getActCode()))
                    .collect(Collectors.toList()));
        }
    }

    public void inspectorReportSave(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the inspectorReportSave start ...."));
        HttpServletRequest request = bpc.request;
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String ao1Sel = ParamUtil.getString(bpc.request, "aoSelect");
        String ao1UserId = null;
        if (!StringUtil.isEmpty(ao1Sel)) {
            ParamUtil.setRequestAttr(request, "aoSelectVal", ao1Sel);
            String[] ao1SelStrs = ao1Sel.split("_");
            ao1UserId= ao1SelStrs[ao1SelStrs.length - 1];
        }
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
        if("rollBack".equals(appPremisesRecommendationDto.getProcessingDecision())){
            String rollBackTo = ParamUtil.getRequestString(bpc.request, "rollBackTo");
            if(StringUtil.isEmpty(rollBackTo)){
                errorMap.put("rollBackTo", "GENERAL_ERR0006");
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                WebValidationHelper.saveAuditTrailForNoUseResult(applicationDto,errorMap);
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
                //set open process tab
                ParamUtil.setSessionAttr(bpc.request, "infoClassTop", null);
                ParamUtil.setSessionAttr(bpc.request, "reportClassTop", null);
                ParamUtil.setSessionAttr(bpc.request, "processClassTop", "active");
                ParamUtil.setSessionAttr(bpc.request, "infoClassBelow", "tab-pane");
                ParamUtil.setSessionAttr(bpc.request, "reportClassBelow", "tab-pane");
                ParamUtil.setSessionAttr(bpc.request, "processClassBelow", "tab-pane active");
            }else {
                Map<String, AppPremisesRoutingHistoryDto> historyDtoMap = (Map<String, AppPremisesRoutingHistoryDto>) ParamUtil.getSessionAttr(request, "rollBackValueMap");
                inspectionService.rollBack(bpc, taskDto, applicationViewDto, historyDtoMap.get(rollBackTo), appPremisesRecommendationDto.getProcessRemarks());
                ParamUtil.setSessionAttr(bpc.request,HcsaLicenceBeConstant.REPORT_ACK_CLARIFICATION_FLAG,"rollBack");
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            }
            return;
        }

        if(ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(appPremisesRecommendationDto.getProcessingDecision())){
            apptInspectionDateService.createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(),applicationViewDto.getApplicationDto().getStatus(), ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION,taskDto,taskDto.getUserId(),appPremisesRecommendationDto.getProcessRemarks(), HcsaConsts.ROUTING_STAGE_INS);
            hcsaApplicationDelegator.requestForInformation(bpc);
            ParamUtil.setSessionAttr(bpc.request,HcsaLicenceBeConstant.REPORT_ACK_CLARIFICATION_FLAG,ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            return;
        } else if("route".equals(appPremisesRecommendationDto.getProcessingDecision())) {
            ParamUtil.setSessionAttr(request, "lrSelect", null);
            String lrSelect = ParamUtil.getRequestString(request, "lrSelect");
            ParamUtil.setSessionAttr(request, "lrSelect", lrSelect);
            if (StringUtil.isEmpty(appPremisesRecommendationDto.getProcessRemarks())) {
                errorMap.put("internalRemarks1", "GENERAL_ERR0006");
            }
            if (StringUtil.isEmpty(lrSelect)) {
                errorMap.put("lrSelectIns", "GENERAL_ERR0006");
            }
            if (errorMap.isEmpty()){
                log.info(StringUtil.changeForLog("The lrSelect is -->:"+lrSelect));
                String[] lrSelects =  lrSelect.split("_");
                String workGroupId = lrSelects[0];
                String userId = lrSelects[1];
                inspEmailService.completedTask(taskDto);
                List<TaskDto> taskDtos = IaisCommonUtils.genNewArrayList();
                taskDto.setUserId(userId);
                taskDto.setDateAssigned(new Date());
                taskDto.setId(null);
                taskDto.setWkGrpId(workGroupId);
                taskDto.setSlaDateCompleted(null);
                taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
                taskDtos.add(taskDto);
                taskService.createTasks(taskDtos);
                apptInspectionDateService.createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW, ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY,taskDto,userId,appPremisesRecommendationDto.getProcessRemarks(), HcsaConsts.ROUTING_STAGE_INS);
                apptInspectionDateService.createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW, taskDto,userId,"",HcsaConsts.ROUTING_STAGE_INS);
                ParamUtil.setSessionAttr(bpc.request,HcsaLicenceBeConstant.REPORT_ACK_CLARIFICATION_FLAG,"route");
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
                ParamUtil.setRequestAttr(bpc.request, "askType", "laterally");
            } else {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                WebValidationHelper.saveAuditTrailForNoUseResult(applicationDto,errorMap);
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
                ParamUtil.setSessionAttr(bpc.request, "lrSelect", lrSelect);
                ParamUtil.setSessionAttr(bpc.request, "infoClassTop", null);
                ParamUtil.setSessionAttr(bpc.request, "reportClassTop", null);
                ParamUtil.setSessionAttr(bpc.request, "processClassTop", "active");
                ParamUtil.setSessionAttr(bpc.request, "infoClassBelow", "tab-pane");
                ParamUtil.setSessionAttr(bpc.request, "reportClassBelow", "tab-pane");
                ParamUtil.setSessionAttr(bpc.request, "processClassBelow", "tab-pane active");
            }
            return;
        }
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
            ParamUtil.setSessionAttr(bpc.request, "processClassTop", null);
            ParamUtil.setSessionAttr(bpc.request, "infoClassBelow", infoClassBelow);
            ParamUtil.setSessionAttr(bpc.request, "reportClassBelow", reportClassBelow);
            ParamUtil.setSessionAttr(bpc.request, "processClassBelow", "tab-pane");
            return;
        }
        List<AppPremisesRecommendationDto> appPremisesRecommendationDtoList = prepareForSave(bpc, appPremisesCorrelationId, recomLiceStartDate, applicationType);
        insRepService.saveRecommendations(appPremisesRecommendationDtoList);
        String[] fastTracking = ParamUtil.getStrings(bpc.request, "fastTracking");
        if (fastTracking != null) {
            applicationDto.setFastTracking(true);
        }
        // save veh inf
        insRepService.saveAppVehs((String)ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.APP_VEHICLE_FLAG),ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equalsIgnoreCase(applicationType) ? applicationViewDto.getVehicleRfcShowDtos():applicationViewDto.getAppSvcVehicleDtos());
        // save SubService
        insRepService.saveSubService((String)ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.APP_SPECIAL_FLAG),applicationViewDto.getAppPremSpecialSubSvcRelDtoList());
        insRepService.saveSubService((String)ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.APP_OTHER_FLAG),applicationViewDto.getAppPremOthersSubSvcRelDtoList());
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
        insRepService.routingTaskToAo1(taskDto, applicationDto, appPremisesCorrelationId, appPremisesRecommendationDto, ao1UserId);
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
    }

    public AppPremisesRecommendationDto prepareRecommendation(BaseProcessClass bpc,String appType,List<String> appTypes) {
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

    public List<AppPremisesRecommendationDto> prepareForSave(BaseProcessClass bpc, String appPremisesCorrelationId, Date licDate, String appType) {
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
           if (InspectionReportConstants.APPROVED.equals(recommendation) || InspectionReportConstants.APPROVEDLTC.equals(recommendation)) {
               appPremisesRecommendationDto.setRecomDecision(recommendation);
               if (OTHERS.equals(periods) && !StringUtil.isEmpty(chrono) && !StringUtil.isEmpty(number)) {
                   appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
                   Integer num = Integer.valueOf(number);
                   if (AppointmentConstants.RECURRENCE_YEAR.equals(chrono)) {
                       chrono = AppointmentConstants.RECURRENCE_MONTH;
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
           } else {
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


    private List<SelectOption> getProcessingDecision(ApplicationViewDto applicationViewDto) {
        String status = applicationViewDto.getApplicationDto().getStatus();
        List<SelectOption> riskLevelResult = IaisCommonUtils.genNewArrayList();
        SelectOption route = new SelectOption("route",MasterCodeUtil.getCodeDesc(ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY));
        riskLevelResult.add(route);
        if (ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR.equals(status)||ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(status)) {
            SelectOption so1 = new SelectOption("submit", MasterCodeUtil.getCodeDesc(ApplicationConsts.PROCESSING_DECISION_REPLY));
            riskLevelResult.add(so1);
            if(ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR.equals(status)){
                String applicationGroupId = applicationViewDto.getApplicationDto().getAppGrpId();
                Integer rfiCount = applicationService.getAppBYGroupIdAndStatus(applicationGroupId,
                        ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION);
                log.info(StringUtil.changeForLog("The rfiCount is -->:" + rfiCount));
                Map<String, String> map = applicationService.checkApplicationByAppGrpNo(
                        applicationViewDto.getApplicationGroupDto().getGroupNo());
                String canEdit = map.get(HcsaAppConst.CAN_RFI);
                if (AppConsts.YES.equals(canEdit) && rfiCount == 0) {
                    riskLevelResult.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION,
                            "Request For Information"));
                }
            }
            return riskLevelResult;
        }
        SelectOption so1 = new SelectOption("submit", MasterCodeUtil.getCodeDesc(InspectionConstants.PROCESS_DECI_REVIEW_INSPECTION_REPORT));
        riskLevelResult.add(so1);
        String appType = applicationViewDto.getApplicationDto().getApplicationType();
        if (!(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(appType) || ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(appType) || ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(appType))) {
            riskLevelResult.add(new SelectOption("rollBack",  MasterCodeUtil.getCodeDesc(InspectionConstants.PROCESS_DECI_ROLL_BACK)));
        }
        route = new SelectOption("route",MasterCodeUtil.getCodeDesc(ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY));
        riskLevelResult.add(route);
        return riskLevelResult;
    }


    public void preCheckList(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String doCheckList=ParamUtil.getRequestString(request,"doCheckList");
        if(!IaisEGPConstant.YES.equals(doCheckList)){
            TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
            inspectReviseNcEmailDelegator.setCheckDataHaveFinished(request,taskDto);
            inspectReviseNcEmailDelegator.setSelectionsForDDMMAndAuditRiskSelect(request);
            inspectReviseNcEmailDelegator.preCheckList(bpc);
        }

    }

    public void checkListNext(BaseProcessClass bpc)  {
        inspectReviseNcEmailDelegator.checkListNext(bpc);
        String actionAdditional = ParamUtil.getString(bpc.request, "crud_action_additional");
        if("editInspectorReport".equals(actionAdditional)){
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", actionAdditional);
            return;
        }
        if("processing".equals(actionAdditional)){
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", actionAdditional);
            return;
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);

    }

    public void doCheckList(BaseProcessClass bpc){
        log.info("=======>>>>>doCheckList>>>>>>>>>>>>>>>>doCheckList");
        HttpServletRequest request = bpc.request;
        inspectReviseNcEmailDelegator.setCheckListData(request);
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,"serListDto");
        InspectionCheckListItemValidate inspectionCheckListItemValidate = new InspectionCheckListItemValidate();
        Map errMap = inspectionCheckListItemValidate.validate(request);
        if (!errMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            serListDto.setCheckListTab("chkList");
            ParamUtil.setSessionAttr(request, "serListDto", serListDto);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        } else {
            serListDto.setCheckListTab("chkList");
            ParamUtil.setSessionAttr(request, "serListDto", serListDto);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }
        inspectReviseNcEmailDelegator.setChangeTabForChecklist(request);
        ParamUtil.setRequestAttr(request, "doCheckList", IaisEGPConstant.YES);

    }

    public void preViewCheckList(BaseProcessClass bpc) throws IOException{


        inspectReviseNcEmailDelegator.preViewCheckList(bpc);

    }
}
