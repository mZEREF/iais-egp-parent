package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionPreTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.InspectionRectificationProService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/18 14:22
 **/
@Delegator("inspectionRectificationProDelegator")
@Slf4j
public class InspectionRectificationProDelegator {

    @Autowired
    private InspectionRectificationProService inspectionRectificationProService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private InspectionRectificationProDelegator(TaskService taskService, InspectionRectificationProService inspectionRectificationProService){
        this.inspectionRectificationProService = inspectionRectificationProService;
        this.taskService = taskService;
    }

    /**
     * StartStep: inspectorProRectificationStart
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectorProRectificationStart start ...."));
        AuditTrailHelper.auditFunction("Inspection Rectification Process", "Inspector Processing Rectification");
    }

    /**
     * StartStep: inspectorProRectificationInit
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectorProRectificationInit start ...."));
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", null);
    }

    /**
     * StartStep: inspectorProRectificationPre
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectorProRectificationPre start ...."));
        InspectionPreTaskDto inspectionPreTaskDto = (InspectionPreTaskDto)ParamUtil.getSessionAttr(bpc.request, "inspectionPreTaskDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        if(inspectionPreTaskDto == null){
            inspectionPreTaskDto = new InspectionPreTaskDto();
            String taskId = ParamUtil.getRequestString(bpc.request, "taskId");
            taskDto = taskService.getTaskById(taskId);
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = inspectionRectificationProService.getAppHistoryByTask(applicationDto.getId(), InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION_CONDITION);
            inspectionPreTaskDto.setReMarks(appPremisesRoutingHistoryDto.getInternalRemarks());
            inspectionPreTaskDto.setAppStatus(applicationDto.getStatus());

        }
        List<SelectOption> processDecOption = inspectionRectificationProService.getProcessRecDecOption();
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);

    }

    /**
     * StartStep: inspectorProRectificationValid
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationValid(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectorProRectificationValid start ...."));
    }

    /**
     * StartStep: inspectorProRectificationReq
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationReq(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectorProRectificationReq start ...."));
    }

    /**
     * StartStep: inspectorProRectificationAcc
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationAcc(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectorProRectificationAcc start ...."));
    }

    /**
     * StartStep: inspectorProRectificationAccCond
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationAccCond(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectorProRectificationAccCond start ...."));
    }
}
