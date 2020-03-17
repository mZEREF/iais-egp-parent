package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.AdhocChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionPreTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AdhocChecklistService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspectionPreTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

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
        AccessUtil.initLoginUserInfo(bpc.request);
        AuditTrailHelper.auditFunction("Inspector Pre Task", "Pre Inspection Task");
    }

    /**
     * StartStep: inspectionPreInspectorInit
     *
     * @param bpc
     * @throws
     */
    public void inspectionPreInspectorInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionPreInspectorInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", null);
        ParamUtil.setSessionAttr(bpc.request, "processDecOption", null);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", null);
        ParamUtil.setSessionAttr(bpc.request, "inboxUrl", null);
        ParamUtil.setSessionAttr(bpc.request, ApplicationConsts.SESSION_PARAM_APPLICATIONDTO, null);
        ParamUtil.setSessionAttr(bpc.request, ApplicationConsts.SESSION_PARAM_APPLICATIONVIEWDTO, null);
        ParamUtil.setSessionAttr(bpc.request,"commonDto", null);
        ParamUtil.setSessionAttr(bpc.request,"serListDto", null);
        ParamUtil.setSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_CHECKLIST_LIST_ATTR, null);
        ParamUtil.setSessionAttr(bpc.request, "actionValue", null);
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
            String taskId = ParamUtil.getRequestString(bpc.request, "taskId");
            taskDto = taskService.getTaskById(taskId);
        }
        ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(taskDto.getRefNo());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String appStatus = applicationDto.getStatus();
        inspectionPreTaskDto.setAppStatus(appStatus);
        setInboxUrlToSession(bpc);
        List<SelectOption> processDecOption = inspectionPreTaskService.getProcessDecOption();
        List<ChecklistConfigDto> inspectionChecklist = adhocChecklistService.getInspectionChecklist(applicationDto);

        ParamUtil.setSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_CHECKLIST_LIST_ATTR, (Serializable) inspectionChecklist);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "processDecOption", (Serializable) processDecOption);
        ParamUtil.setSessionAttr(bpc.request, ApplicationConsts.SESSION_PARAM_APPLICATIONDTO, applicationDto);
        ParamUtil.setSessionAttr(bpc.request, ApplicationConsts.SESSION_PARAM_APPLICATIONVIEWDTO, applicationViewDto);
    }

    private void setInboxUrlToSession(BaseProcessClass bpc) {
        StringBuilder sb = new StringBuilder("https://");
        String url = MessageConstants.MESSAGE_CALL_BACK_URL_BEINBOX;
        sb.append(bpc.request.getServerName());
        if (!url.startsWith("/")) {
            sb.append("/");
        }
        sb.append(url);
        if (url.indexOf("?") >= 0) {
            sb.append("&");
        } else {
            sb.append("?");
        }
        String inboxUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(sb.toString(), bpc.request);
        ParamUtil.setSessionAttr(bpc.request, "inboxUrl", inboxUrl);
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
        inspectionPreTaskDto.setReMarks(preInspecRemarks);
        if(InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY.equals(processDec)){
            inspectionPreTaskDto.setSelectValue(processDec);
        } else if(InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION.equals(processDec)){
            inspectionPreTaskDto.setSelectValue(processDec);
        } else {
            inspectionPreTaskDto.setSelectValue(null);
        }

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
        } else if(InspectionConstants.SWITCH_ACTION_ROUTE_BACK.equals(actionValue)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(inspectionPreTaskDto,"back");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        }else if(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue) ||
                InspectionConstants.SWITCH_ACTION_EDIT.equals(actionValue) ||
                InspectionConstants.SWITCH_ACTION_SELF.equals(actionValue)){
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        }

        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "actionValue", actionValue);
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
    public void inspectionPreInspectorRouteB(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionPreInspectorRouteB start ...."));
        InspectionPreTaskDto inspectionPreTaskDto = (InspectionPreTaskDto)ParamUtil.getSessionAttr(bpc.request, "inspectionPreTaskDto");
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        AdhocCheckListConifgDto adhocCheckListConifgDto = (AdhocCheckListConifgDto) ParamUtil.getSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR);
        if(adhocCheckListConifgDto != null){
            adhocChecklistService.saveAdhocChecklist(adhocCheckListConifgDto);
        }
        inspectionPreTaskService.routingBack(taskDto, inspectionPreTaskDto.getReMarks());
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
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
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        Map<InspectionFillCheckListDto, List<InspectionFillCheckListDto>> mapInDto = inspectionPreTaskService.getSelfCheckListByCorrId(taskDto.getRefNo());
        InspectionFillCheckListDto inspectionFillCheckListDto = new InspectionFillCheckListDto();
        List<InspectionFillCheckListDto> ifcDtos = IaisCommonUtils.genNewArrayList();
        if(mapInDto != null) {
            for (Map.Entry<InspectionFillCheckListDto, List<InspectionFillCheckListDto>> entry : mapInDto.entrySet()) {
                inspectionFillCheckListDto = entry.getKey();
                ifcDtos = entry.getValue();
            }
        }
        ParamUtil.setSessionAttr(bpc.request,"commonDto",inspectionFillCheckListDto);
        ParamUtil.setSessionAttr(bpc.request,"serListDto", (Serializable) ifcDtos);
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
