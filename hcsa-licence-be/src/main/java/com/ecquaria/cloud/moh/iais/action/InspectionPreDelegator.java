package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionPreTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
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
        if(inspectionPreTaskDto == null){
            inspectionPreTaskDto = new InspectionPreTaskDto();
        }
        String taskId = ParamUtil.getRequestString(bpc.request, "taskId");
        TaskDto taskDto = taskService.getTaskById(taskId);
        String appStatus = inspectionPreTaskService.getAppStatusByTaskId(taskDto);
        List<SelectOption> processDecOption = inspectionPreTaskService.getProcessDecOption();
        inspectionPreTaskDto.setAppStatus(appStatus);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "processDecOption", (Serializable) processDecOption);
    }

    /**
     * StartStep: inspectionPreInspectorStep1
     *
     * @param bpc
     * @throws
     */
    public void inspectionPreInspectorStep1(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionPreInspectorStep1 start ...."));
    }

    /**
     * StartStep: inspectionPreInspectorApprove
     *
     * @param bpc
     * @throws
     */
    public void inspectionPreInspectorApprove(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionPreInspectorApprove start ...."));
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        InspectionPreTaskDto inspectionPreTaskDto = (InspectionPreTaskDto)ParamUtil.getSessionAttr(bpc.request, "inspectionPreTaskDto");
        String preInspecRemarks = ParamUtil.getString(bpc.request,"preInspecRemarks");
        inspectionPreTaskDto.setReMarks(preInspecRemarks);
        if(InspectionConstants.PROCESS_DECI_MARK_INSPE_TASK_READY.equals(actionValue)){
            TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
            inspectionPreTaskDto.setSelectValue(actionValue);
            inspectionPreTaskService.routingTask(taskDto, preInspecRemarks);
        } else {
            inspectionPreTaskDto.setSelectValue(null);
        }
        if(!(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue)) || !(InspectionConstants.SWITCH_ACTION_EDIT.equals(actionValue))){
            ValidationResult validationResult = WebValidationHelper.validateProperty(inspectionPreTaskDto,"create");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, DemoConstants.ERRORMAP, errorMap);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        }else{
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        }
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
    }

    /**
     * StartStep: inspectionPreInspectorRouteB
     *
     * @param bpc
     * @throws
     */
    public void inspectionPreInspectorRouteB(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionPreInspectorRouteB start ...."));
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        InspectionPreTaskDto inspectionPreTaskDto = (InspectionPreTaskDto)ParamUtil.getSessionAttr(bpc.request, "inspectionPreTaskDto");
        String preInspecRemarks = ParamUtil.getString(bpc.request,"preInspecRemarks");
        inspectionPreTaskDto.setReMarks(preInspecRemarks);
        if(InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION.equals(actionValue)){
            TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
            inspectionPreTaskDto.setSelectValue(actionValue);
        } else {
            inspectionPreTaskDto.setSelectValue(null);
        }
        if(!(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue)) || !(InspectionConstants.SWITCH_ACTION_EDIT.equals(actionValue))){
            ValidationResult validationResult = WebValidationHelper.validateProperty(inspectionPreTaskDto,"back");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, DemoConstants.ERRORMAP, errorMap);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        }else{
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        }
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
    }
}
