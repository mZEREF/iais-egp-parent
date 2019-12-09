package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionPreTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.List;

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
    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private InspectionAssignTaskService inspectionAssignTaskService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private InspectionPreDelegator(InspectionPreTaskService inspectionPreTaskService, ApplicationViewService applicationViewService, InspectionAssignTaskService inspectionAssignTaskService, TaskService taskService, AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient){
        this.inspectionPreTaskService = inspectionPreTaskService;
        this.taskService = taskService;
        this.appPremisesRoutingHistoryClient = appPremisesRoutingHistoryClient;
        this.inspectionAssignTaskService = inspectionAssignTaskService;
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
        ParamUtil.setSessionAttr(bpc.request, "appStatus", null);
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
        String taskId = ParamUtil.getRequestString(bpc.request, "taskId");
        TaskDto taskDto = taskService.getTaskById(taskId);
        String appStatus = inspectionPreTaskService.getAppStatusByTaskId(taskDto);
        List<SelectOption> processDecOption = inspectionPreTaskService.getProcessDecOption();
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        ParamUtil.setSessionAttr(bpc.request, "appStatus", appStatus);
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
        String approve = ParamUtil.getRequestString(bpc.request, "actionValue");
        if(InspectionConstants.SWITCH_ACTION_APPROVE.equals(approve)){
            TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
            ApplicationViewDto applicationViewDto = inspectionAssignTaskService.searchByAppNo(taskDto.getRefNo());
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            String preInspecRemarks = ParamUtil.getString(bpc.request,"preInspecRemarks");
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto.getStatus(),taskDto.getTaskKey(),preInspecRemarks);
            ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING);
            applicationViewDto.setApplicationDto(applicationDto1);
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto.getStatus(), HcsaConsts.ROUTING_STAGE_INS,null);

            taskService.updateTask(taskDto);
        }
    }

    private ApplicationDto updateApplication(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        return applicationViewService.updateApplicaiton(applicationDto);
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus,
                                                                         String stageId, String internalRemarks){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto).getEntity();
        return appPremisesRoutingHistoryDto;
    }

    /**
     * StartStep: inspectionPreInspectorRouteB
     *
     * @param bpc
     * @throws
     */
    public void inspectionPreInspectorRouteB(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionPreInspectorRouteB start ...."));
        String routeB = ParamUtil.getRequestString(bpc.request, "actionValue");
        if(InspectionConstants.SWITCH_ACTION_ROUTE_BACK.equals(routeB)){
            TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        }
    }
}
