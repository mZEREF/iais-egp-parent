package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 12/16/2019 4:03 PM
 */
@Delegator("inspectionReassignTask")
@Slf4j
public class InspecReassignTaskDelegator {

    @Autowired
    private InspectionService inspectionService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;
    @Autowired
    private InsRepService insRepService;

    public InspecReassignTaskDelegator(InspectionService inspectionService, TaskService taskService, AppPremisesRoutingHistoryService appPremisesRoutingHistoryService, InsRepService insRepService) {
        this.inspectionService = inspectionService;
        this.taskService = taskService;
        this.appPremisesRoutingHistoryService = appPremisesRoutingHistoryService;
        this.insRepService = insRepService;
    }

    /**
     * StartStep: inspectionReassignTask
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchStart(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchStart start ...."));
        AuditTrailHelper.auditFunction("Inspection Reassign Task", "Sup Assign Task");
    }

    /**
     * StartStep: inspectionSupSearchInit
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchInit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchInit start ...."));
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, "reassignReason", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectionTaskPoolListDto", null);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", null);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", null);
        ParamUtil.setSessionAttr(bpc.request, "workGroupIds", null);
        ParamUtil.setSessionAttr(bpc.request, "appTypeOption", null);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectorOption", null);
    }

    /**
     * StartStep: inspectionSupSearchPre
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchPre(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchPre start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        SearchResult<InspectionSubPoolQueryDto> searchResult = (SearchResult) ParamUtil.getSessionAttr(bpc.request, "supTaskSearchResult");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        //get groupId by login id
        List<String> workGroupIds = inspectionService.getWorkGroupIdsByLogin(loginContext);
        //select de option
        List<SelectOption> appTypeOption = inspectionService.getAppTypeOption();
        List<SelectOption> appStatusOption = inspectionService.getAppStatusOption();
        //get Inspector Option
        List<SelectOption> inspectorOption = inspectionService.getInspectorOptionByLogin(loginContext, workGroupIds);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", searchResult);
        ParamUtil.setSessionAttr(bpc.request, "appTypeOption", (Serializable) appTypeOption);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
        ParamUtil.setSessionAttr(bpc.request, "inspectorOption", (Serializable) inspectorOption);
        ParamUtil.setSessionAttr(bpc.request, "workGroupIds", (Serializable) workGroupIds);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc) {
        return getSearchParam(bpc, false);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc, boolean isNew) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, "supTaskSearchParam");
        if (searchParam == null || isNew) {
            searchParam = new SearchParam(InspectionSubPoolQueryDto.class.getName());
            searchParam.setPageSize(10);
            searchParam.setPageNo(1);
            searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
        }
        return searchParam;
    }

    /**
     * StartStep: inspectionSupSearchStartStep1
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchStartStep1(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchStartStep1 start ...."));

    }

    /**
     * StartStep: inspectionSupSearchDoSearch
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchDoSearch(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchDoSearch start ...."));
        SearchParam searchParam = getSearchParam(bpc);

        List<String> workGroupIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "workGroupIds");
        String application_no = ParamUtil.getRequestString(bpc.request, "application_no");
        String application_type = ParamUtil.getRequestString(bpc.request, "application_type");
        String application_status = ParamUtil.getRequestString(bpc.request, "application_status");
        String hci_code = ParamUtil.getRequestString(bpc.request, "hci_code");
        String hci_name = ParamUtil.getRequestString(bpc.request, "hci_name");
        String hci_address = ParamUtil.getRequestString(bpc.request, "hci_address");
        String inspectorValue = ParamUtil.getMaskedString(bpc.request, "inspector_name");

        List<TaskDto> ReassignPools = getReassignPoolByGroupWordId(workGroupIds);

        String[] applicationNo_list = inspectionService.getApplicationNoListByPool(ReassignPools);
        if (applicationNo_list == null || applicationNo_list.length == 0) {
            applicationNo_list = new String[]{SystemParameterConstants.PARAM_FALSE};
        }
        String applicationStr = SqlHelper.constructInCondition("T1.APPLICATION_NO", applicationNo_list.length);
        searchParam.addParam("applicationNo_list", applicationStr);
        for (int i = 0; i < applicationNo_list.length; i++) {
            searchParam.addFilter("T1.APPLICATION_NO" + i, applicationNo_list[i]);
        }

        if (!StringUtil.isEmpty(application_no)) {
            searchParam.addFilter("application_no", application_no, true);
        }
        String[] appNoStrs;
        if (!(StringUtil.isEmpty(inspectorValue))) {
            appNoStrs = inspectorValue.split(",");
            String appNoStr = SqlHelper.constructInCondition("T5.APPLICATION_NO", appNoStrs.length);
            searchParam.addParam("appNo_list", appNoStr);
            for (int i = 0; i < appNoStrs.length; i++) {
                searchParam.addFilter("T5.APPLICATION_NO" + i, appNoStrs[i]);
            }
        }
        if (!StringUtil.isEmpty(application_type)) {
            searchParam.addFilter("application_type", application_type, true);
        }
        if (!StringUtil.isEmpty(application_status)) {
            searchParam.addFilter("application_status", application_status, true);
        }
        if (!StringUtil.isEmpty(hci_code)) {
            searchParam.addFilter("hci_code", hci_code, true);
        }
        if (!StringUtil.isEmpty(hci_name)) {
            searchParam.addFilter("hci_name", hci_name, true);
        }
        if (!StringUtil.isEmpty(hci_address)) {
            searchParam.addFilter("blk_no", hci_address, true);
            searchParam.addFilter("floor_no", hci_address, true);
            searchParam.addFilter("unit_no", hci_address, true);
            searchParam.addFilter("street_name", hci_address, true);
            searchParam.addFilter("building_name", hci_address, true);
            searchParam.addFilter("postal_code", hci_address, true);
        }
        ParamUtil.setSessionAttr(bpc.request, "ReassignPools", (Serializable) ReassignPools);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "inspectorValue", inspectorValue);
    }

    private List<TaskDto> getReassignPoolByGroupWordId(List<String> workGroupIds) {
        List<TaskDto> taskDtoList = new ArrayList<>();
        if (workGroupIds == null || workGroupIds.size() <= 0) {
            return null;
        }
        for (String workGrpId : workGroupIds) {
            List<TaskDto> reassignPoolByGroupWordId = inspectionService.getReassignPoolByGroupWordId(workGrpId);
            if (reassignPoolByGroupWordId != null && !reassignPoolByGroupWordId.isEmpty()) {
                for (TaskDto tDto : reassignPoolByGroupWordId) {
                    taskDtoList.add(tDto);
                }
            }

        }
        return taskDtoList;
    }

    /**
     * StartStep: inspectionSupSearchSort
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchSort(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchSort start ...."));
    }

    /**
     * StartStep: inspectionSupSearchPage
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchPage(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchPage start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doPaging(searchParam, bpc.request);
    }

    /**
     * StartStep: inspectionSupSearchQuery1
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchQuery1(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchQuery1 start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        List<TaskDto> ReassignPools = (List<TaskDto>) ParamUtil.getSessionAttr(bpc.request, "ReassignPools");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        QueryHelp.setMainSql("inspectionQuery", "assignInspectorSupper", searchParam);
        SearchResult<InspectionSubPoolQueryDto> searchResult = inspectionService.getSupPoolByParam(searchParam);
        SearchResult<InspectionTaskPoolListDto> searchResult2 = inspectionService.getOtherDataForSr(searchResult, ReassignPools, loginContext);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", searchResult2);
    }

    /**
     * StartStep: inspectionSupSearchAssign
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchAssign(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchAssign start ...."));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        SearchResult<InspectionTaskPoolListDto> searchResult = (SearchResult<InspectionTaskPoolListDto>) ParamUtil.getSessionAttr(bpc.request, "supTaskSearchResult");
        String taskId = ParamUtil.getMaskedString(bpc.request, "taskId");
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto) ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        if (!(StringUtil.isEmpty(taskId))) {
            inspectionTaskPoolListDto = new InspectionTaskPoolListDto();
            for (InspectionTaskPoolListDto iDto : searchResult.getRows()) {
                if (taskId.equals(iDto.getTaskId())) {
                    inspectionTaskPoolListDto = iDto;
                }
            }
            //get inspector Option for dropdown
            inspectionTaskPoolListDto = inspectionService.reassignInspectorOption(inspectionTaskPoolListDto, taskId);
            List<SelectOption> inspectorOption = inspectionTaskPoolListDto.getInspectorOption();
            ParamUtil.setSessionAttr(bpc.request, "inspectorOption", (Serializable) inspectorOption);
        }

        ParamUtil.setSessionAttr(bpc.request, "inspectionTaskPoolListDto", inspectionTaskPoolListDto);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", searchResult);
        ParamUtil.setSessionAttr(bpc.request, "taskId", taskId);
    }

    /**
     * StartStep: inspectionSupSearchValidate
     * @param bpc
     * @throws`
     */
    public void inspectionSupSearchValidate(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchValidate start ...."));
        InspectionTaskPoolListDto inspectionTaskPoolListDto = getValueFromPage(bpc);
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        String reassignReason = ParamUtil.getString(bpc.request, "reassignReason");
        if (!(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue))) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(inspectionTaskPoolListDto, "create");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.TRUE);
            }
        } else {
            ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.TRUE);
        }
        ParamUtil.setSessionAttr(bpc.request, "inspectionTaskPoolListDto", inspectionTaskPoolListDto);
        ParamUtil.setSessionAttr(bpc.request, "reassignReason", reassignReason);
    }

    public InspectionTaskPoolListDto getValueFromPage(BaseProcessClass bpc) {
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto) ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        String[] nameValue = ParamUtil.getStrings(bpc.request, "inspectorCheck");
        if (nameValue == null || nameValue.length < 0) {
            inspectionTaskPoolListDto.setInspectorCheck(null);
        } else {
            List<SelectOption> inspectorCheckList = inspectionService.getCheckInspector(nameValue, inspectionTaskPoolListDto);
            inspectionTaskPoolListDto.setInspectorCheck(inspectorCheckList);
        }
        return inspectionTaskPoolListDto;

    }

    /**
     * StartStep: inspectionSupSearchQuery2
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchQuery2(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchQuery2 start ...."));
        String taskId = ParamUtil.getRequestString(bpc.request, "taskId");
        String inspectorCheck = ParamUtil.getRequestString(bpc.request, "inspectorCheck");
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto) ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        ParamUtil.setSessionAttr(bpc.request, "inspectionTaskPoolListDto", inspectionTaskPoolListDto);
        ParamUtil.setSessionAttr(bpc.request, "taskId", taskId);
        ParamUtil.setSessionAttr(bpc.request, "inspectorCheck", inspectorCheck);
    }

    /**
     * StartStep: inspectionSupSearchConfirm
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchConfirm(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchConfirm start ...."));
        String inspectorCheck = ParamUtil.getRequestString(bpc.request, "inspectorCheck");
        String taskId = ParamUtil.getRequestString(bpc.request, "taskId");
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto) ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        ParamUtil.setSessionAttr(bpc.request, "inspectionTaskPoolListDto", inspectionTaskPoolListDto);
        ParamUtil.setSessionAttr(bpc.request, "inspectorCheck", inspectorCheck);
        ParamUtil.setSessionAttr(bpc.request, "taskId", taskId);
    }
    /**
     * StartStep: InspectionInboxSearchQuery
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchSuccess(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchSuccess start ...."));
        String reassignReason = (String) ParamUtil.getSessionAttr(bpc.request, "reassignReason");
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto) ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        List<TaskDto> ReassignPools = (List<TaskDto>) ParamUtil.getSessionAttr(bpc.request, "ReassignPools");
        String internalRemarks = ParamUtil.getString(bpc.request, "internalRemarks");
//        inspectionService.routingTaskByPool(inspectionTaskPoolListDto, ReassignPools, reassignReason);
        routingTask(bpc);
        ParamUtil.setSessionAttr(bpc.request, "inspectionTaskPoolListDto", inspectionTaskPoolListDto);
    }













    private void routingTask(BaseProcessClass bpc) throws FeignException {
        //String taskId = ParamUtil.getMaskedString(bpc.request, "taskId");
        String taskId = ParamUtil.getRequestString(bpc.request, "taskId");
        String userId = ParamUtil.getRequestString(bpc.request, "inspectorCheck");
        TaskDto taskDto = taskService.getTaskById(taskId);
        String appNo = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = insRepService.getApplicationViewDto(appNo);
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        String appStatus = applicationViewDto.getApplicationDto().getStatus();
        String stageId = taskDto.getTaskKey();
        //remove this task and create the history
//        removeTask(taskDto);
        createAppPremisesRoutingHistory(appPremisesCorrelationId,appStatus,stageId,null,null, RoleConsts.USER_ROLE_INSPECTIOR);
        // send the task
        if(!StringUtil.isEmpty(stageId)) {
            String inspectorCheck = ParamUtil.getRequestString(bpc.request, "inspectorCheck");
            //add history for next stage start
            updateTask(taskDto,userId);
            createAppPremisesRoutingHistory(appPremisesCorrelationId,appStatus,stageId,null,null, RoleConsts.USER_ROLE_INSPECTIOR);
        }
    }

    private void removeTask(TaskDto taskDto) {
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        taskService.updateTask(taskDto);
    }

    private void createTask(TaskDto taskDto,String userId) {
        List<TaskDto> list = new ArrayList<>();
        taskDto.setId(null);
        taskDto.setUserId(userId);
        taskDto.setDateAssigned(new Date());
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        list.add(taskDto);
        taskService.createTasks(list);
    }

    private void updateTask(TaskDto taskDto,String userId) {
        taskDto.setUserId(userId);
        taskDto.setDateAssigned(new Date());
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        taskService.updateTask(taskDto);
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus,
                                                                         String stageId, String internalRemarks, String processDec,String roleId) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setProcessDecision(processDec);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
        return appPremisesRoutingHistoryDto;
    }







}
