package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.job.executor.util.SpringHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.AdhocChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessmentConfig;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionHistoryShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionPreTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.ChecklistConstant;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.BeSelfChecklistHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AdhocChecklistService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InspectionPreTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Process: MohInspectionPreInspector
 *
 * @author Shicheng
 * @date 2019/12/9 9:15
 **/
@Delegator("inspectionPreDelegator")
@Slf4j
public class InspectionPreDelegator {

    @Autowired
    private InspectionPreTaskService inspectionPreTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AdhocChecklistService adhocChecklistService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private FillupChklistService fillupChklistService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private InspectionService inspectionService;

    @Autowired
    private InspectionPreDelegator(InspectionPreTaskService inspectionPreTaskService, TaskService taskService, AdhocChecklistService adhocChecklistService,
                                   ApplicationViewService applicationViewService, FillupChklistService fillupChklistService){
        this.inspectionPreTaskService = inspectionPreTaskService;
        this.taskService = taskService;
        this.adhocChecklistService = adhocChecklistService;
        this.applicationViewService = applicationViewService;
        this.fillupChklistService = fillupChklistService;
    }

    /**
     * StartStep: inspectionPreInspectorStart
     *
     * @param bpc
     * @throws
     */
    public void inspectionPreInspectorStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionPreInspectorStart start ...."));
    }

    /**
     * StartStep: inspectionPreInspectorInit
     *
     * @param bpc
     * @throws
     */
    public void inspectionPreInspectorInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionPreInspectorInit start ...."));
        String preInspInitFlag = (String) ParamUtil.getRequestAttr(bpc.request, "preInspInitFlag");
        String taskId = "";
        try{
            taskId = ParamUtil.getMaskedString(bpc.request,"taskId");
        }catch (MaskAttackException e){
            log.error(e.getMessage(), e);
            try{
                IaisEGPHelper.redirectUrl(bpc.response, "https://" + bpc.request.getServerName() + "/hcsa-licence-web/CsrfErrorPage.jsp");
            } catch (IOException ioe){
                log.error(ioe.getMessage(), ioe);
                return;
            }
        }
        TaskDto taskDto = taskService.getTaskById(taskId);
        AuditTrailHelper.auditFunctionWithAppNo(AuditTrailConsts.MODULE_INSPECTION, AuditTrailConsts.FUNCTION_PRE_INSPECTION, taskDto.getApplicationNo());
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", null);
        ParamUtil.setSessionAttr(bpc.request, "processDecOption", null);
        ParamUtil.setSessionAttr(bpc.request, ApplicationConsts.SESSION_PARAM_APPLICATIONDTO, null);
        ParamUtil.setSessionAttr(bpc.request, ApplicationConsts.SESSION_PARAM_APPLICATIONVIEWDTO, null);
        ParamUtil.setSessionAttr(bpc.request,"commonDto", null);
        ParamUtil.setSessionAttr(bpc.request,"serListDto", null);
        ParamUtil.setSessionAttr(bpc.request,"isSaveRfiSelect",null);
        ParamUtil.setSessionAttr(bpc.request,"rfiAppEditSelectDto",null);
        ParamUtil.setSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_CHECKLIST_LIST_ATTR, null);
        ParamUtil.setSessionAttr(bpc.request, "actionValue", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectionHistoryShowDtos", null);
        ParamUtil.setSessionAttr(bpc.request, "appEditSelectDto", null);
        if(StringUtil.isEmpty(preInspInitFlag)) {
            ParamUtil.setSessionAttr(bpc.request, "rfiUpWindowsCheck", null);
        }
        ParamUtil.setSessionAttr(bpc.request, ChecklistConstant.ADHOC_ITEM_ACTION_FLAG, "N");
        ParamUtil.setSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, null);
        SearchParam searchParamGroup = (SearchParam)ParamUtil.getSessionAttr(bpc.request, "backendinboxSearchParam");
        ParamUtil.setSessionAttr(bpc.request,"backSearchParamFromHcsaApplication",searchParamGroup);
        ParamUtil.setSessionAttr(bpc.request, "rollBackOptions", null);
        ParamUtil.setSessionAttr(bpc.request, "rollBackValueMap", null);
    }

    /**
     * StartStep: inspectionPreInspectorPre
     *
     * @param bpc
     * @throws
     */
    public void inspectionPreInspectorPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionPreInspectorPre start ...."));
        InspectionPreTaskDto inspectionPreTaskDto = (InspectionPreTaskDto)ParamUtil.getSessionAttr(bpc.request, "inspectionPreTaskDto");
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        if(inspectionPreTaskDto == null){
            inspectionPreTaskDto = new InspectionPreTaskDto();
        }
         ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request,
                 ApplicationConsts.SESSION_PARAM_APPLICATIONVIEWDTO);
        if(applicationViewDto == null) {
            //get application info show
            applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(taskDto.getRefNo(), taskDto.getRoleId());
        }
        if(applicationViewDto != null) {
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            String appStatus = applicationDto.getStatus();
            inspectionPreTaskDto.setAppStatus(appStatus);
            //get process decision
            List<SelectOption> processDecOption = inspectionPreTaskService.getProcessDecOption(applicationDto,
                    applicationViewDto.getApplicationGroupDto().getGroupNo());
            //Audit application doesn't do back and rfi
            if(!ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationDto.getApplicationType())) {
                if (!ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationDto.getApplicationType())) {
                    //route back set stage and userId map
                    inspectionPreTaskDto = inspectionPreTaskService.getPreInspRbOption(applicationViewDto, inspectionPreTaskDto);
                    List<SelectOption> preInspRbOption = inspectionPreTaskDto.getPreInspRbOption();
                    inspectionPreTaskDto.setPreInspRbOption(preInspRbOption);
                    ParamUtil.setSessionAttr(bpc.request, "preInspRbOption", (Serializable) preInspRbOption);
                }
                //get Request For Information
                List<SelectOption> rfiCheckOption = inspectionPreTaskService.getRfiCheckOption(applicationDto.getApplicationType());
                inspectionPreTaskDto.setPreInspRfiOption(rfiCheckOption);
            }
            //adhocChecklist
            boolean needVehicle = fillupChklistService.checklistNeedVehicleSeparation(applicationViewDto);
            List<ChecklistConfigDto> inspectionChecklist = adhocChecklistService.getInspectionChecklist(applicationDto, needVehicle);
            //Self-Checklist
            List<SelfAssessment> selfAssessments = BeSelfChecklistHelper.receiveSelfAssessmentDataByCorrId(taskDto.getRefNo());
            setPreInspSelfChecklistInfo(selfAssessments, bpc);
            //Inspection history
            List<InspectionHistoryShowDto> inspectionHistoryShowDtos = inspectionPreTaskService.getInspectionHistory(applicationDto.getOriginLicenceId(), applicationDto.getId());
            ParamUtil.setSessionAttr(bpc.request, "inspectionHistoryShowDtos", (Serializable) inspectionHistoryShowDtos);
            ParamUtil.setSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_CHECKLIST_LIST_ATTR, (Serializable) inspectionChecklist);
            ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
            ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
            ParamUtil.setSessionAttr(bpc.request, "processDecOption", (Serializable) processDecOption);
            ParamUtil.setSessionAttr(bpc.request, ApplicationConsts.SESSION_PARAM_APPLICATIONDTO, applicationDto);
            ParamUtil.setSessionAttr(bpc.request, ApplicationConsts.SESSION_PARAM_APPLICATIONVIEWDTO, applicationViewDto);
            Map<String, AppPremisesRoutingHistoryDto> rollBackValueMap = IaisCommonUtils.genNewHashMap();
            List<SelectOption> rollBackStage = inspectionService.getRollBackSelectOptions(applicationViewDto.getRollBackHistroyList(), rollBackValueMap, taskDto.getRoleId());
            ParamUtil.setSessionAttr(bpc.request, "rollBackOptions", (Serializable) rollBackStage);
            ParamUtil.setSessionAttr(bpc.request, "rollBackValueMap", (Serializable) rollBackValueMap);
            // for edit application - need session - applicationViewDto
            checkForEditingApplication(bpc.request);
        }
    }

    private void checkForEditingApplication(HttpServletRequest request) {
        // check from editing application
        String appError = ParamUtil.getString(request, HcsaAppConst.ERROR_APP);
        if (StringUtil.isNotEmpty(appError)) {
            ParamUtil.setRequestAttr(request, HcsaAppConst.ERROR_APP, StringUtil.clarify(appError));
        }
        // show edit application
        boolean showBtn = true;
        ParamUtil.setRequestAttr(request, HcsaAppConst.SHOW_EDIT_BTN, showBtn
                && SpringHelper.getBean(ApplicationDelegator.class).checkData(HcsaAppConst.CHECKED_BTN_SHOW, request));
    }

    private void setPreInspSelfChecklistInfo(List<SelfAssessment> selfAssessments, BaseProcessClass bpc) {
        if(!IaisCommonUtils.isEmpty(selfAssessments)){
            //one refNo(appPremCorrId) --> one SelfAssessment
            List<SelfAssessmentConfig> selfAssessmentConfigs = selfAssessments.get(0).getSelfAssessmentConfig();
            if(!IaisCommonUtils.isEmpty(selfAssessmentConfigs)) {
                List<SelfAssessmentConfig> selfAssessmentConfigList = IaisCommonUtils.genNewArrayList();
                for(SelfAssessmentConfig selfAssessmentConfig : selfAssessmentConfigs){
                    if(selfAssessmentConfig == null){
                        continue;
                    }
                    if(selfAssessmentConfig.isCommon()){
                        List<PremCheckItem> premCheckItems = selfAssessmentConfig.getQuestion();
                        ParamUtil.setSessionAttr(bpc.request, "commonDto", (Serializable) premCheckItems);
                    } else {
                        selfAssessmentConfigList.add(selfAssessmentConfig);
                    }
                }
                ParamUtil.setSessionAttr(bpc.request, "serListDto123", (Serializable) selfAssessmentConfigList);
            }
        }
    }

    /**
     * StartStep: inspectionPreInspectorStep1
     *
     * @param bpc
     * @throws
     */
    public void inspectionPreInspectorStep1(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionPreInspectorStep1 start ...."));
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        ApplicationDto applicationDto = (ApplicationDto)ParamUtil.getSessionAttr(bpc.request, ApplicationConsts.SESSION_PARAM_APPLICATIONDTO);
        InspectionPreTaskDto inspectionPreTaskDto = (InspectionPreTaskDto)ParamUtil.getSessionAttr(bpc.request, "inspectionPreTaskDto");
        String preInspecRemarks = ParamUtil.getString(bpc.request,"preInspecRemarks");
        String processDec = ParamUtil.getRequestString(bpc.request,"selectValue");
        String preInspecComments = ParamUtil.getRequestString(bpc.request,"preInspecComments");
        String checkRbStage = ParamUtil.getRequestString(bpc.request,"checkRbStage");
        String[] preInspRfiCheckStr = ParamUtil.getStrings(bpc.request,"preInspRfiCheck");

        List<String> preInspRfiCheck = getPreInspListByArray(preInspRfiCheckStr);
        inspectionPreTaskDto.setPreInspRfiCheck(preInspRfiCheck);
        inspectionPreTaskDto.setReMarks(preInspecRemarks);
        //set value with processDec
        inspectionPreTaskDto = setValidateValue(processDec, checkRbStage, inspectionPreTaskDto, preInspecComments);

        if(InspectionConstants.SWITCH_ACTION_APPROVE.equals(actionValue)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(inspectionPreTaskDto,"create");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        } else if(InspectionConstants.SWITCH_ACTION_REQUEST_INFORMATION.equals(actionValue)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(inspectionPreTaskDto,"prerfi");

            if(preInspRfiCheck != null) {
                //Request for information cannot be made at the same time
                int appInspRfiResult = inspectionPreTaskService.preInspRfiTogether(applicationDto);
                validationResult = validatePreInspRfiTogether(validationResult, appInspRfiResult);
                Map<String, String> errorMap = validationResult.retrieveAll();
                if(errorMap == null || errorMap.size() < 1) {
                    //choose rfi app and don't check app pop-up windows is error
                    validationResult = validateAppRfiCheck(validationResult, preInspRfiCheck, bpc);
                }
            }
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
                ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, ApplicationConsts.SESSION_PARAM_APPLICATIONVIEWDTO);
                if (applicationViewDto != null) {
                    Map<String, String> map = applicationService.checkApplicationByAppGrpNo(
                            applicationViewDto.getApplicationGroupDto().getGroupNo());
                    String canEdit = map.get(HcsaAppConst.CAN_RFI);
                    if (AppConsts.NO.equals(canEdit)) {
                        String appError = map.get(HcsaAppConst.ERROR_APP);
                        if (StringUtil.isNotEmpty(appError)) {
                            ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.ERROR_APP, appError);
                            ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
                        }
                    } else {
                        String appNo = applicationViewDto.getApplicationDto().getApplicationNo();
                        String appStatus = map.get(appNo);
                        if (StringUtil.isNotEmpty(appStatus) && IaisCommonUtils.getNonDoRFIStatus().contains(appStatus)) {
                            ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.ERROR_APP, MessageUtil.replaceMessage("GENERAL_ERR0061",
                                    "edited", "action"));
                            ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
                        }
                    }
                }
            }
        } else if(InspectionConstants.SWITCH_ACTION_ROUTE_BACK.equals(actionValue)){
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        } else if(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue) ||
                InspectionConstants.SWITCH_ACTION_EDIT.equals(actionValue) ||
                InspectionConstants.SWITCH_ACTION_SELF.equals(actionValue)){
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        }

        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "actionValue", actionValue);
    }

    private ValidationResult validatePreInspRfiTogether(ValidationResult validationResult, int appInspRfiResult) {
        if(appInspRfiResult > 0){
            Map<String, String> errorMap = validationResult.retrieveAll();
            errorMap.put("preInspRfiTogether","GENERAL_ERR0045");
            validationResult.setHasErrors(true);
        }
        return validationResult;
    }

    private ValidationResult validateAppRfiCheck(ValidationResult validationResult, List<String> preInspRfiCheck, BaseProcessClass bpc) {
        if(!IaisCommonUtils.isEmpty(preInspRfiCheck)){

            if(preInspRfiCheck.contains(InspectionConstants.SWITCH_ACTION_APPLICATION)){
                Map<String, String> errorMap = validationResult.retrieveAll();
                //rfiSelectValue and check value
                String rfiSelectValue = ParamUtil.getRequestString(bpc.request, "rfiSelectValue");
                List<String> rfiUpWindowsCheck = (List<String>)ParamUtil.getSessionAttr(bpc.request, "rfiUpWindowsCheck");
                if(StringUtil.isEmpty(rfiSelectValue) || IaisCommonUtils.isEmpty(rfiUpWindowsCheck)){
                    String nextStage = MessageUtil.dateIntoMessage("RFI_ERR001");
                    errorMap.put("nextStage", nextStage);
                    validationResult.setHasErrors(true);
                }
            }
        }
        return validationResult;
    }

    private InspectionPreTaskDto setValidateValue(String processDec, String checkRbStage, InspectionPreTaskDto inspectionPreTaskDto, String preInspecComments) {
        if(InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY.equals(processDec)){
            inspectionPreTaskDto.setSelectValue(processDec);
        } else if(InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION.equals(processDec)){
            inspectionPreTaskDto.setSelectValue(processDec);
            inspectionPreTaskDto.setPreInspecComments(preInspecComments);
        } else if(InspectionConstants.PROCESS_DECI_ROUTE_BACK_APSO.equals(processDec) || InspectionConstants.PROCESS_DECI_ROLL_BACK.equals(processDec)){
            if(!StringUtil.isEmpty(checkRbStage)){
                String userId = inspectionPreTaskDto.getStageUserIdMap().get(checkRbStage);
                if(!StringUtil.isEmpty(userId)){
                    inspectionPreTaskDto.setCheckRbStage(checkRbStage);
                } else {
                    inspectionPreTaskDto.setCheckRbStage(null);
                }
            } else {
                inspectionPreTaskDto.setCheckRbStage(null);
            }
            inspectionPreTaskDto.setSelectValue(processDec);
            inspectionPreTaskDto.setPreInspecComments(preInspecComments);
        } else {
            inspectionPreTaskDto.setSelectValue(null);
        }
        return inspectionPreTaskDto;
    }

    private List<String> getPreInspListByArray(String[] preInspRfiCheckStr) {
        List<String> preInspRfiCheck = IaisCommonUtils.genNewArrayList();
        if(preInspRfiCheckStr != null && preInspRfiCheckStr.length > 0){
            for(int i = 0; i < preInspRfiCheckStr.length; i++){
                preInspRfiCheck.add(preInspRfiCheckStr[i]);
            }
        } else {
            return null;
        }
        return preInspRfiCheck;
    }

    /**
     * StartStep: inspectionPreInspectorApprove
     *
     * @param bpc
     * @throws
     */
    public void inspectionPreInspectorApprove(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionPreInspectorApprove start ...."));
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        InspectionPreTaskDto inspectionPreTaskDto = (InspectionPreTaskDto)ParamUtil.getSessionAttr(bpc.request, "inspectionPreTaskDto");
        AdhocCheckListConifgDto adhocCheckListConifgDto = (AdhocCheckListConifgDto) ParamUtil.getSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR);
        ApplicationDto applicationDto = (ApplicationDto)ParamUtil.getSessionAttr(bpc.request, ApplicationConsts.SESSION_PARAM_APPLICATIONDTO);
        List<ChecklistConfigDto> inspectionChecklist = (List<ChecklistConfigDto>)ParamUtil.getSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_CHECKLIST_LIST_ATTR);
        //adhoc Checklist and send email
        if(adhocCheckListConifgDto != null){
            adhocChecklistService.saveAdhocChecklist(adhocCheckListConifgDto);
            ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, ApplicationConsts.SESSION_PARAM_APPLICATIONVIEWDTO);
            fillupChklistService.sendModifiedChecklistEmailToAOStage(applicationViewDto);
        }
        //set Checklist
        if (inspectionChecklist == null) {
            ApplicationViewDto appView = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request,
                    ApplicationConsts.SESSION_PARAM_APPLICATIONVIEWDTO);
            boolean needVehicle = appView != null && fillupChklistService.checklistNeedVehicleSeparation(appView);
            inspectionChecklist = adhocChecklistService.getInspectionChecklist(applicationDto, needVehicle);
        }
        //generate self report
        if(taskDto != null) {
            inspectionPreTaskService.selfAssMtPdfReport(taskDto.getRefNo());
        }
        //save Vehicle List
        ApplicationViewDto appViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request,
                ApplicationConsts.SESSION_PARAM_APPLICATIONVIEWDTO);
        if (IaisCommonUtils.isNotEmpty(appViewDto.getAppSvcVehicleDtos())) {
            List<String> veNameList = IaisCommonUtils.genNewArrayList();
            for (AppSvcVehicleDto asvDto : appViewDto.getAppSvcVehicleDtos()) {
                veNameList.add(asvDto.getVehicleName());
            }
            inspectionPreTaskService.saveVehicleNames(veNameList, appViewDto.getAppPremisesCorrelationId());
        }

        inspectionPreTaskService.routingTask(taskDto, inspectionPreTaskDto.getReMarks(), inspectionChecklist);
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
        ParamUtil.setSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, adhocCheckListConifgDto);
        ParamUtil.setSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_CHECKLIST_LIST_ATTR, (Serializable) inspectionChecklist);
    }

    /**
     * StartStep: inspectionPreInspectorRouteB
     *
     * @param bpc
     * @throws
     */
    public void inspectionPreInspectorRouteB(BaseProcessClass bpc) throws IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the inspectionPreInspectorRouteB start ...."));
        InspectionPreTaskDto inspectionPreTaskDto = (InspectionPreTaskDto)ParamUtil.getSessionAttr(bpc.request, "inspectionPreTaskDto");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<PremCheckItem> premCheckItems = (List<PremCheckItem>)ParamUtil.getSessionAttr(bpc.request, "commonDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, ApplicationConsts.SESSION_PARAM_APPLICATIONVIEWDTO);
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        AdhocCheckListConifgDto adhocCheckListConifgDto = (AdhocCheckListConifgDto) ParamUtil.getSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR);
        if(adhocCheckListConifgDto != null){
            adhocChecklistService.saveAdhocChecklist(adhocCheckListConifgDto);
            fillupChklistService.sendModifiedChecklistEmailToAOStage(applicationViewDto);
        }
        String successInfo = MessageUtil.dateIntoMessage("RFI_ACK001");
        ParamUtil.setRequestAttr(bpc.request, "successPage", "requestForInfo");
        ParamUtil.setRequestAttr(bpc.request, "successInfo", successInfo);
        inspectionPreTaskService.routingBack(taskDto, inspectionPreTaskDto, loginContext, premCheckItems, applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
        ParamUtil.setSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, adhocCheckListConifgDto);
    }

    /**
     * StartStep: inspectionPreInspectorBack
     *
     * @param bpc
     * @throws
     */
    public void inspectionPreInspectorBack(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionPreInspectorBack start ...."));
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        InspectionPreTaskDto inspectionPreTaskDto = (InspectionPreTaskDto)ParamUtil.getSessionAttr(bpc.request, "inspectionPreTaskDto");
        AdhocCheckListConifgDto adhocCheckListConifgDto = (AdhocCheckListConifgDto) ParamUtil.getSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR);
        if(adhocCheckListConifgDto != null){
            adhocChecklistService.saveAdhocChecklist(adhocCheckListConifgDto);
            ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, ApplicationConsts.SESSION_PARAM_APPLICATIONVIEWDTO);
            fillupChklistService.sendModifiedChecklistEmailToAOStage(applicationViewDto);
        }
        inspectionPreTaskService.routingAsoPsoBack(taskDto, inspectionPreTaskDto, loginContext);
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
        ParamUtil.setRequestAttr(bpc.request, "successPage", "routeBack");
        ParamUtil.setSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, adhocCheckListConifgDto);
    }

    /**
     * StartStep: inspectionPreInspectorRollBack
     *
     * @param bpc
     * @throws
     */
    public void inspectionPreInspectorRollBack(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionPreInspectorRollBack start ...."));
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request,
                ApplicationConsts.SESSION_PARAM_APPLICATIONVIEWDTO);
        InspectionPreTaskDto inspectionPreTaskDto = (InspectionPreTaskDto) ParamUtil.getSessionAttr(bpc.request, "inspectionPreTaskDto");
        Map<String, AppPremisesRoutingHistoryDto> rollBackValueMap = (Map<String, AppPremisesRoutingHistoryDto>) ParamUtil.getSessionAttr(bpc.request, "rollBackValueMap");
        String rollBackTo = ParamUtil.getRequestString(bpc.request, "rollBackTo");
        inspectionService.rollBack(bpc, taskDto, applicationViewDto, rollBackValueMap.get(rollBackTo), inspectionPreTaskDto.getReMarks());
        ParamUtil.setRequestAttr(bpc.request, "successPage", "rollBack");
        log.debug(StringUtil.changeForLog("the inspectionPreInspectorRollBack end ...."));
    }

    /**
     * StartStep: inspectionPreInspectorSelf
     *
     * @param bpc
     * @throws
     */
    public void inspectionPreInspectorSelf(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionPreInspectorSelf start ...."));
    }

    /**
     * StartStep: inspectionPreInspector
     *
     * @param bpc
     * @throws
     */
    public void inspectionPreInspector(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionPreInspector start ...."));
    }
}
