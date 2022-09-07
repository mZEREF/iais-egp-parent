package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2019/12/5 13:16
 */
@Delegator(value = "insReportAo")
@Slf4j
public class InsReportAoDelegator  {

    @Autowired
    private InsRepService insRepService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private InspEmailService inspEmailService;
    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;
    @Autowired
    private FillupChklistService fillupChklistService;
    @Autowired
    private VehicleCommonController vehicleCommonController;
    @Autowired
    private InspectionService inspectionService;

    private final static String APPROVAL="Approval";
    private final static String REJECT="Reject";
    private final static String INSREPDTO="insRepDto";
    private final static String APPLICATIONVIEWDTO="applicationViewDto";
    private final static String TASKDTO="taskDto";
    private final static String ROLLBACK_OPTIONS = "rollBackOptions";
    private final static String ROLLBACK_VALUE_MAP = "rollBackValueMap";


    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
    }

    public void clearSession( HttpServletRequest request ){
        ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.REPORT_ACK_CLARIFICATION_FLAG,null);
        ParamUtil.setSessionAttr(request, INSREPDTO, null);
        ParamUtil.setSessionAttr(request, APPLICATIONVIEWDTO, null);
        vehicleCommonController.clearVehicleInformationSession(request);
        ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE,null);
        ParamUtil.setSessionAttr(request, ROLLBACK_OPTIONS, null);
        ParamUtil.setSessionAttr(request, ROLLBACK_VALUE_MAP, null);
    }
    public void AoInit(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the inspectionReportInit start ...."));
        HttpServletRequest request = bpc.request;
        clearSession(request);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String taskId = null;
        try{
            taskId = ParamUtil.getMaskedString(request,"taskId");
        }catch(MaskAttackException e){
            log.error(e.getMessage(),e);
            IaisEGPHelper.redirectUrl(bpc.response, "https://"+request.getServerName()+"/hcsa-licence-web/CsrfErrorPage.jsp");
        }
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION,  AuditTrailConsts.FUNCTION_INSPECTION_REPORT);
        TaskDto taskDto = taskService.getTaskById(taskId);
        String correlationId = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = insRepService.getApplicationViewDto(correlationId, taskDto.getRoleId());
        if(fillupChklistService.checklistNeedVehicleSeparation(applicationViewDto)){
            ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE,AppConsts.YES);
        }
        AuditTrailHelper.auditFunctionWithAppNo(AuditTrailConsts.MODULE_INSPECTION, AuditTrailConsts.FUNCTION_INSPECTION_REPORT,
                applicationViewDto.getApplicationDto().getApplicationNo());
        InspectionReportDto insRepDto = insRepService.getInsRepDto(taskDto,applicationViewDto,loginContext);
        InspectionReportDto inspectorAo = insRepService.getInspectorAo(taskDto, applicationViewDto);
        insRepDto.setInspectors(inspectorAo.getInspectors());
        vehicleCommonController.initAoRecommendation(correlationId,bpc,applicationViewDto.getApplicationDto().getApplicationType());
        Map<String, AppPremisesRoutingHistoryDto> rollBackValueMap = IaisCommonUtils.genNewHashMap();
        List<SelectOption> rollBackStage = inspectionService.getRollBackSelectOptions(applicationViewDto.getRollBackHistroyList(), rollBackValueMap, taskDto.getRoleId(), Collections.singletonList(RoleConsts.USER_ROLE_INSPECTIOR));

        String infoClassTop = "active";
        String infoClassBelow = "tab-pane active";
        String reportClassBelow = "tab-pane";
        String kpiInfo = MessageUtil.getMessageDesc("LOLEV_ACK051");
        ParamUtil.setSessionAttr(request, "kpiInfo", kpiInfo);
        ParamUtil.setSessionAttr(request, "infoClassTop", infoClassTop);
        ParamUtil.setSessionAttr(request,"appType",null);
        ParamUtil.setSessionAttr(request, "reportClassTop", null);
        ParamUtil.setSessionAttr(request, "infoClassBelow", infoClassBelow);
        ParamUtil.setSessionAttr(request, "reportClassBelow", reportClassBelow);
        List<SelectOption> processingDe = getProcessingDecision(applicationViewDto.getApplicationDto());
        ParamUtil.setSessionAttr(request, "processingDe", (Serializable) processingDe);
        ParamUtil.setSessionAttr(request, INSREPDTO, insRepDto);
        ParamUtil.setSessionAttr(request, APPLICATIONVIEWDTO, applicationViewDto);
        ParamUtil.setSessionAttr(request, TASKDTO, taskDto);
        SearchParam searchParamGroup = (SearchParam)ParamUtil.getSessionAttr(request, "backendinboxSearchParam");
        ParamUtil.setSessionAttr(request,"backSearchParamFromHcsaApplication",searchParamGroup);
        ParamUtil.setSessionAttr(request, ROLLBACK_OPTIONS, (Serializable) rollBackStage);
        ParamUtil.setSessionAttr(request, ROLLBACK_VALUE_MAP, (Serializable) rollBackValueMap);
        vehicleCommonController.setVehicleInformation(request,taskDto,applicationViewDto);
    }

    public void AoReportPre(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>prepareReportData");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, APPLICATIONVIEWDTO);
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        ParamUtil.setSessionAttr(bpc.request,"appType",applicationType);
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

    public void back(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the back start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, APPLICATIONVIEWDTO);
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, TASKDTO);
        String historyRemarks = ParamUtil.getRequestString(bpc.request, "processRemarks");
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        log.debug(StringUtil.changeForLog("the saveAoRecommendation start ...."));
        ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
        insRepService.routBackTaskToInspector(taskDto,applicationDto,appPremisesCorrelationId,historyRemarks);
    }

    public void rollBack(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the rollback start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, APPLICATIONVIEWDTO);
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, TASKDTO);
        String rollBackTo = ParamUtil.getRequestString(bpc.request, "rollBackTo");
        if(StringUtil.isEmpty(rollBackTo)){
            Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
            errMap.put("rollBackTo", "GENERAL_ERR0006");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
        }else {
            Map<String, AppPremisesRoutingHistoryDto> rollBackValueMap = (Map<String, AppPremisesRoutingHistoryDto>) ParamUtil.getSessionAttr(bpc.request, ROLLBACK_VALUE_MAP);
            inspectionService.rollBack(bpc, taskDto, applicationViewDto, rollBackValueMap.get(rollBackTo), ParamUtil.getRequestString(bpc.request, "processRemarks"));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            ParamUtil.setSessionAttr(bpc.request,HcsaLicenceBeConstant.REPORT_ACK_CLARIFICATION_FLAG,"rollBack");
        }

    }

    public void laterally(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        ApplicationViewDto applicationViewDto= (ApplicationViewDto) ParamUtil.getSessionAttr(request,APPLICATIONVIEWDTO);
        TaskDto taskDto= (TaskDto) ParamUtil.getSessionAttr(request,TASKDTO);
        String historyRemarks = ParamUtil.getRequestString(bpc.request, "processRemarks");

        String lrSelect = ParamUtil.getRequestString(bpc.request, "lrSelect");
        log.info(StringUtil.changeForLog("The lrSelect is -->:"+lrSelect));
        if(StringUtil.isNotEmpty(lrSelect)){
            String[] lrSelects =  lrSelect.split("_");
            String aoWorkGroupId = lrSelects[0];
            String aoUserId = lrSelects[1];
            inspEmailService.completedTask(taskDto);
            List<TaskDto> taskDtos = IaisCommonUtils.genNewArrayList();
            taskDto.setUserId(aoUserId);
            taskDto.setDateAssigned(new Date());
            taskDto.setId(null);
            taskDto.setWkGrpId(aoWorkGroupId);
            taskDto.setSlaDateCompleted(null);
            taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
            taskDtos.add(taskDto);
            taskService.createTasks(taskDtos);
            createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW, ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY,taskDto,userId,historyRemarks, taskDto.getTaskKey());
            createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW, taskDto,userId,"",taskDto.getTaskKey());
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            ParamUtil.setSessionAttr(bpc.request,HcsaLicenceBeConstant.REPORT_ACK_CLARIFICATION_FLAG,"laterally");
        }else {
            Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
            errMap.put("lrSelect", "GENERAL_ERR0006");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
        }
    }

    public void approve(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the inspectorReportAction start ...."));
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, INSREPDTO);
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, APPLICATIONVIEWDTO);
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        AppPremisesCorrelationDto newAppPremisesCorrelationDto = applicationViewDto.getNewAppPremisesCorrelationDto();
        String newCorrelationId = newAppPremisesCorrelationDto.getId();
        TaskDto taskDto =  (TaskDto)ParamUtil.getSessionAttr(bpc.request, TASKDTO);
        String ao1Sel = ParamUtil.getString(bpc.request, "aoSelect");
        String aoId = null;
        if (!StringUtil.isEmpty(ao1Sel)) {
            String[] ao1SelStrs = ao1Sel.split("_");
            aoId = ao1SelStrs[ao1SelStrs.length - 1];
        }
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        String[] fastTracking =  ParamUtil.getStrings(bpc.request,"fastTracking");
        String historyRemarks = ParamUtil.getRequestString(bpc.request, "processRemarks");
        if(fastTracking!=null){
            applicationDto.setFastTracking(true);
        }
        if(ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_AO.equals(applicationDto.getStatus())){
            insRepService.routTaskToRoutBack(bpc,taskDto, applicationDto, appPremisesCorrelationId,historyRemarks);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            ParamUtil.setSessionAttr(bpc.request,HcsaLicenceBeConstant.REPORT_ACK_CLARIFICATION_FLAG,SystemAdminBaseConstants.YES);
            return;
        }
        if (ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(applicationDto.getStatus())) {
            insRepService.routTaskToRoutBackAo3(bpc, taskDto, applicationDto, appPremisesCorrelationId, historyRemarks,true);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            ParamUtil.setSessionAttr(bpc.request,HcsaLicenceBeConstant.REPORT_ACK_CLARIFICATION_FLAG,SystemAdminBaseConstants.YES);
            return;
        }
        insRepService.routingTaskToAo2(taskDto,applicationDto,appPremisesCorrelationId,historyRemarks,newCorrelationId,aoId);
        ParamUtil.setSessionAttr(bpc.request, INSREPDTO, insRepDto);
        ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
    }



    private List<SelectOption> getProcessingDecision(ApplicationDto applicationDto) {
        String status = applicationDto.getStatus();
        if(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(status)||ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_AO.equals(status)){
            List<SelectOption> riskLevelResult = IaisCommonUtils.genNewArrayList();
            SelectOption so1 = new SelectOption("submit", MasterCodeUtil.getCodeDesc(ApplicationConsts.PROCESSING_DECISION_REPLY));
            riskLevelResult.add(so1);
            return riskLevelResult;
        }

        List<SelectOption> riskLevelResult = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption(APPROVAL, MasterCodeUtil.getCodeDesc(InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_INSPECTION_REPORT));
        SelectOption so2 = new SelectOption(REJECT, MasterCodeUtil.getCodeDesc(InspectionConstants.PROCESS_DECI_REVISE_INSPECTION_REPORT));
        riskLevelResult.add(so1);
        riskLevelResult.add(so2);
        String appType = applicationDto.getApplicationType();
        if (!(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(appType) || ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(appType) || ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(appType))) {
            riskLevelResult.add(new SelectOption("rollBack", MasterCodeUtil.getCodeDesc(InspectionConstants.PROCESS_DECI_ROLL_BACK)));
        }
        riskLevelResult.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY, "Route Laterally"));
        return riskLevelResult;
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String appStatus, String decision,
                                                                         TaskDto taskDto, String userId, String remarks,String subStage) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(taskDto.getTaskKey());
        appPremisesRoutingHistoryDto.setProcessDecision(decision);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(userId);
        appPremisesRoutingHistoryDto.setInternalRemarks(remarks);
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setRoleId(taskDto.getRoleId());
        appPremisesRoutingHistoryDto.setWrkGrpId(taskDto.getWkGrpId());
        appPremisesRoutingHistoryDto.setSubStage(subStage);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
        return appPremisesRoutingHistoryDto;
    }
}
