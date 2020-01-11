package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.AdhocChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionPreTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AdhocChecklistService;
import com.ecquaria.cloud.moh.iais.service.InspectionPreTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

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
    private InspectionPreDelegator(InspectionPreTaskService inspectionPreTaskService, TaskService taskService){
        this.inspectionPreTaskService = inspectionPreTaskService;
        this.taskService = taskService;
    }

    /**
     * StartStep: inspectionPreInspectorStart
     *
     * @param bpc
     * @throws
     */
    public void inspectionPreInspectorStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionPreInspectorStart start ...."));
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
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", null);
        ParamUtil.setSessionAttr(bpc.request, "processDecOption", null);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", null);
        ParamUtil.setSessionAttr(bpc.request, "inboxUrl", null);
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
            if(StringUtil.isEmpty(taskId)){
                taskId = "CBD90C65-2F34-EA11-BE7D-000C29F371DC";
            }
            taskDto = taskService.getTaskById(taskId);
            String appStatus = inspectionPreTaskService.getAppStatusByTaskId(taskDto);
            inspectionPreTaskDto.setAppStatus(appStatus);
        }
        setInboxUrlToSession(bpc);
        List<SelectOption> processDecOption = inspectionPreTaskService.getProcessDecOption();
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "processDecOption", (Serializable) processDecOption);
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
        }else if(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue) || InspectionConstants.SWITCH_ACTION_EDIT.equals(actionValue)){
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        }

        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
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
        if(adhocCheckListConifgDto != null){
            adhocChecklistService.saveAdhocChecklist(adhocCheckListConifgDto);
        }
        inspectionPreTaskService.routingTask(taskDto, inspectionPreTaskDto.getReMarks());
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
        ParamUtil.setSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, adhocCheckListConifgDto);
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
}
