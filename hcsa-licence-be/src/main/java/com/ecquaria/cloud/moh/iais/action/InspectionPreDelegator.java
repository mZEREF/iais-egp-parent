package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.AdhocChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessmentConfig;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionHistoryShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionPreTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.BeSelfChecklistHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AdhocChecklistService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspectionPreTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

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
    private InspectionPreDelegator(InspectionPreTaskService inspectionPreTaskService, TaskService taskService,
                                   AdhocChecklistService adhocChecklistService, ApplicationViewService applicationViewService){
        this.inspectionPreTaskService = inspectionPreTaskService;
        this.taskService = taskService;
        this.adhocChecklistService = adhocChecklistService;
        this.applicationViewService = applicationViewService;
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
        String taskId = ParamUtil.getMaskedString(bpc.request, "taskId");
        TaskDto taskDto = taskService.getTaskById(taskId);
        AuditTrailHelper.auditFunction("Inspector Pre Task", "Pre Inspection Task");
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", null);
        ParamUtil.setSessionAttr(bpc.request, "processDecOption", null);
        ParamUtil.setSessionAttr(bpc.request, ApplicationConsts.SESSION_PARAM_APPLICATIONDTO, null);
        ParamUtil.setSessionAttr(bpc.request, ApplicationConsts.SESSION_PARAM_APPLICATIONVIEWDTO, null);
        ParamUtil.setSessionAttr(bpc.request,"commonDto", null);
        ParamUtil.setSessionAttr(bpc.request,"serListDto", null);
        ParamUtil.setSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_CHECKLIST_LIST_ATTR, null);
        ParamUtil.setSessionAttr(bpc.request, "actionValue", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectionHistoryShowDtos", null);
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
        //get application info show
        ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(taskDto.getRefNo());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String appStatus = applicationDto.getStatus();
        inspectionPreTaskDto.setAppStatus(appStatus);
        //get process decision
        List<SelectOption> processDecOption = inspectionPreTaskService.getProcessDecOption(applicationDto.getApplicationType());
        //Audit application doesn't do back and rfi
        if(!ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationDto.getApplicationType())) {
            //get Request For Information
            List<SelectOption> rfiCheckOption = inspectionPreTaskService.getRfiCheckOption();
            //set stage and userId map
            inspectionPreTaskDto = inspectionPreTaskService.getPreInspRbOption(applicationDto.getApplicationNo(), inspectionPreTaskDto);
            List<SelectOption> preInspRbOption = inspectionPreTaskDto.getPreInspRbOption();
            inspectionPreTaskDto.setPreInspRfiOption(rfiCheckOption);
            inspectionPreTaskDto.setPreInspRbOption(preInspRbOption);
            ParamUtil.setSessionAttr(bpc.request, "preInspRbOption", (Serializable) preInspRbOption);
        }
        //adhocChecklist
        List<ChecklistConfigDto> inspectionChecklist = adhocChecklistService.getInspectionChecklist(applicationDto);
        //Self-Checklist
        List<SelfAssessment> selfAssessments = BeSelfChecklistHelper.receiveSelfAssessmentDataByCorrId(taskDto.getRefNo());
        setPreInspSelfChecklistInfo(selfAssessments, bpc);
        //Inspection history
        List<InspectionHistoryShowDto> inspectionHistoryShowDtos = inspectionPreTaskService.getInspectionHistory(applicationDto.getOriginLicenceId());

        ParamUtil.setSessionAttr(bpc.request, "inspectionHistoryShowDtos", (Serializable) inspectionHistoryShowDtos);
        ParamUtil.setSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_CHECKLIST_LIST_ATTR, (Serializable) inspectionChecklist);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "processDecOption", (Serializable) processDecOption);
        ParamUtil.setSessionAttr(bpc.request, ApplicationConsts.SESSION_PARAM_APPLICATIONDTO, applicationDto);
        ParamUtil.setSessionAttr(bpc.request, ApplicationConsts.SESSION_PARAM_APPLICATIONVIEWDTO, applicationViewDto);
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
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        } else if(InspectionConstants.SWITCH_ACTION_REQUEST_INFORMATION.equals(actionValue)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(inspectionPreTaskDto,"prerfi");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        } else if(InspectionConstants.SWITCH_ACTION_ROUTE_BACK.equals(actionValue)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(inspectionPreTaskDto,"preback");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        } else if(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue) ||
                InspectionConstants.SWITCH_ACTION_EDIT.equals(actionValue) ||
                InspectionConstants.SWITCH_ACTION_SELF.equals(actionValue)){
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        }

        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "actionValue", actionValue);
    }

    private InspectionPreTaskDto setValidateValue(String processDec, String checkRbStage, InspectionPreTaskDto inspectionPreTaskDto, String preInspecComments) {
        if(InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY.equals(processDec)){
            inspectionPreTaskDto.setSelectValue(processDec);
        } else if(InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION.equals(processDec)){
            inspectionPreTaskDto.setSelectValue(processDec);
        } else if(InspectionConstants.PROCESS_DECI_ROUTE_BACK_APSO.equals(processDec)){
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
        if(adhocCheckListConifgDto != null){
            adhocChecklistService.saveAdhocChecklist(adhocCheckListConifgDto);
        }
        if(inspectionChecklist == null){
            inspectionChecklist = adhocChecklistService.getInspectionChecklist((applicationDto));
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
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        AdhocCheckListConifgDto adhocCheckListConifgDto = (AdhocCheckListConifgDto) ParamUtil.getSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR);
        if(adhocCheckListConifgDto != null){
            adhocChecklistService.saveAdhocChecklist(adhocCheckListConifgDto);
        }
        inspectionPreTaskService.routingBack(taskDto, inspectionPreTaskDto, loginContext);
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
        }
        inspectionPreTaskService.routingAsoPsoBack(taskDto, inspectionPreTaskDto, loginContext);
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
        ParamUtil.setRequestAttr(bpc.request, "successPage", "routeBack");
        ParamUtil.setSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, adhocCheckListConifgDto);
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
