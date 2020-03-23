package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReassignTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
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
        AccessUtil.initLoginUserInfo(bpc.request);
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
        ParamUtil.setSessionAttr(bpc.request, "reassignReason", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectionTaskPoolListDto", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectionReassignTaskDto", null);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", null);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", null);
        ParamUtil.setSessionAttr(bpc.request, "workGroupIds", null);
        ParamUtil.setSessionAttr(bpc.request, "appTypeOption", null);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectorOption", null);

        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<String> workGroupIds = inspectionService.getWorkGroupIdsByLogin(loginContext);
        SearchParam searchParam = getSearchParam(bpc);
        List<TaskDto> ReassignPools = getReassignPoolByGroupWordId(workGroupIds);
        List<String> appCorrId_list = inspectionService.getApplicationNoListByPool(ReassignPools);
        StringBuilder sb = new StringBuilder("(");
        for(int i = 0; i < appCorrId_list.size(); i++){
            sb.append(":appCorrId" + i).append(",");
        }
        String inSql = sb.substring(0, sb.length() - 1) + ")";
        searchParam.addParam("appCorrId_list", inSql);
        for(int i = 0; i < appCorrId_list.size(); i++){
            searchParam.addFilter("appCorrId" + i, appCorrId_list.get(i));
        }
        QueryHelp.setMainSql("inspectionQuery", "assignInspectorSupper", searchParam);
        SearchResult<InspectionSubPoolQueryDto> searchResult = inspectionService.getSupPoolByParam(searchParam);
        SearchResult<InspectionTaskPoolListDto> searchResult2 = inspectionService.getOtherDataForSr(searchResult, ReassignPools, loginContext);
        ParamUtil.setRequestAttr(bpc.request, "supTaskSearchResult", searchResult2);
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
        String curRoleId = loginContext.getCurRoleId();
        String user = null;
        String lead = null;
        if(RoleConsts.USER_ROLE_INSPECTION_LEAD.equals(curRoleId)){
            user = "Inspector";
            lead = "Inspection Lead";
        }else if(RoleConsts.USER_ROLE_AO1.equals(curRoleId)||RoleConsts.USER_ROLE_AO2.equals(curRoleId)||RoleConsts.USER_ROLE_AO3.equals(curRoleId)){
            user = "Admin Screening Officer";
            lead = "Admin Screening Supervisor";
        }else if(RoleConsts.USER_ROLE_PSO.equals(curRoleId)){
            user = "Professional Screening Officer";
            lead = "Professional Screening Supervisor";
        }else if(RoleConsts.USER_ROLE_ASO.equals(curRoleId)){
            user = "Admin Screening Officer";
            lead = "Admin Screening Supervisor";
        }
        ParamUtil.setSessionAttr(bpc.request, "user", user);
        ParamUtil.setSessionAttr(bpc.request, "lead", lead);
        //get groupId by login id
        List<String> workGroupIds = inspectionService.getWorkGroupIdsByLogin(loginContext);
        //select de option
        List<SelectOption> appTypeOption = inspectionService.getAppTypeOption();
        List<SelectOption> appStatusOption = inspectionService.getAppStatusOption();
        //get Inspector Option
        //List<SelectOption> inspectorOption = inspectionService.getInspectorOptionByLogin(loginContext, workGroupIds);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", searchResult);
        ParamUtil.setSessionAttr(bpc.request, "appTypeOption", (Serializable) appTypeOption);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
        //ParamUtil.setSessionAttr(bpc.request, "inspectorOption", (Serializable) inspectorOption);
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
        SearchParam searchParam = getSearchParam(bpc, true);
        List<String> workGroupIds = (List<String>)ParamUtil.getSessionAttr(bpc.request, "workGroupIds");
        String application_no = ParamUtil.getRequestString(bpc.request, "application_no");
        String application_type = ParamUtil.getRequestString(bpc.request, "application_type");
        String application_status = ParamUtil.getRequestString(bpc.request, "application_status");
        String hci_code = ParamUtil.getRequestString(bpc.request, "hci_code");
        String hci_name = ParamUtil.getRequestString(bpc.request, "hci_name");
        String hci_address = ParamUtil.getRequestString(bpc.request, "hci_address");
        //String inspectorValue = ParamUtil.getMaskedString(bpc.request, "inspector_name");
        String inspectorValue = ParamUtil.getMaskedString(bpc.request, "inspectorSearchTask_inspectorName");
        List<TaskDto> ReassignPools = getReassignPoolByGroupWordId(workGroupIds);
        inspectorValue = "DF18318E-8137-EA11-BE78-000C29D29DB0";
        List<String> appCorrId_list = inspectionService.getApplicationNoListByPool(ReassignPools);
        StringBuilder sb = new StringBuilder("(");
        for(int i = 0; i < appCorrId_list.size(); i++){
            sb.append(":appCorrId" + i).append(",");
        }
        String inSql = sb.substring(0, sb.length() - 1) + ")";
        searchParam.addParam("appCorrId_list", inSql);
        for(int i = 0; i < appCorrId_list.size(); i++){
            searchParam.addFilter("appCorrId" + i, appCorrId_list.get(i));
        }
        if(!StringUtil.isEmpty(application_no)){
            searchParam.addFilter("application_no", application_no,true);
        }
        String[] appCorIdStrs;
        if(!(StringUtil.isEmpty(inspectorValue))) {
            appCorIdStrs = inspectorValue.split(",");
            StringBuilder sb2 = new StringBuilder("(");
            for(int i = 0; i < appCorIdStrs.length; i++){
                sb2.append(":appCorId" + i).append(",");
            }
            String inSql2 = sb2.substring(0, sb2.length() - 1) + ")";
            searchParam.addParam("appCorId_list", inSql2);
            for(int i = 0; i < appCorIdStrs.length; i++){
                searchParam.addFilter("appCorId" + i, appCorIdStrs[i]);
            }
        }
        if(!StringUtil.isEmpty(application_type)){
            searchParam.addFilter("application_type", application_type,true);
        }
        if(!StringUtil.isEmpty(application_status)){
            searchParam.addFilter("application_status", application_status,true);
        }
        if(!StringUtil.isEmpty(hci_code)){
            searchParam.addFilter("hci_code", hci_code,true);
        }
        if(!StringUtil.isEmpty(hci_name)){
            searchParam.addFilter("hci_name", hci_name,true);
        }
        if(!StringUtil.isEmpty(hci_address)){
            searchParam.addFilter("hci_address", hci_address,true);
        }
        ParamUtil.setSessionAttr(bpc.request, "ReassignPools", (Serializable) ReassignPools);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "inspectorValue", inspectorValue);
    }

    private List<TaskDto> getReassignPoolByGroupWordId(List<String> workGroupIds) {
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
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
        InspectionReassignTaskDto inspectionReassignTaskDto = getValueFromPage(bpc);
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto)ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        if (!(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue))) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(inspectionReassignTaskDto, "create");
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
        ParamUtil.setSessionAttr(bpc.request, "inspectionReassignTaskDto", inspectionReassignTaskDto);
    }

    public InspectionReassignTaskDto getValueFromPage(BaseProcessClass bpc) {
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto)ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        InspectionReassignTaskDto inspectionReassignTaskDto = new InspectionReassignTaskDto();
        String reassignRemarks = ParamUtil.getString(bpc.request, "reassignRemarks");
        inspectionReassignTaskDto.setReassignRemarks(reassignRemarks);
        String inspectorCheck = ParamUtil.getString(bpc.request, "inspectorCheck");
        String[] nameValue = {};
        if(!StringUtil.isEmpty(inspectorCheck)){
            nameValue = new String[]{inspectorCheck};
        }else {
            nameValue = null;
        }
        if (nameValue == null || nameValue.length == 0) {
            inspectionReassignTaskDto.setInspectorCheck(null);
        } else {
            List<SelectOption> inspectorCheckList = inspectionService.getCheckInspector(nameValue, inspectionTaskPoolListDto);
            inspectionReassignTaskDto.setInspectorCheck(inspectorCheckList);
        }
        return inspectionReassignTaskDto;
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
        InspectionReassignTaskDto inspectionReassignTaskDto = (InspectionReassignTaskDto)ParamUtil.getSessionAttr(bpc.request, "inspectionReassignTaskDto");
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto) ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        ParamUtil.setSessionAttr(bpc.request, "inspectionTaskPoolListDto", inspectionTaskPoolListDto);
        ParamUtil.setSessionAttr(bpc.request, "taskId", taskId);
        ParamUtil.setSessionAttr(bpc.request, "inspectorCheck", inspectorCheck);
        ParamUtil.setSessionAttr(bpc.request, "inspectionReassignTaskDto", inspectionReassignTaskDto);
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
        InspectionReassignTaskDto inspectionReassignTaskDto = (InspectionReassignTaskDto)ParamUtil.getSessionAttr(bpc.request, "inspectionReassignTaskDto");
        String taskId = ParamUtil.getRequestString(bpc.request, "taskId");
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto) ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        ParamUtil.setSessionAttr(bpc.request, "inspectionTaskPoolListDto", inspectionTaskPoolListDto);
        ParamUtil.setSessionAttr(bpc.request, "inspectorCheck", inspectorCheck);
        ParamUtil.setSessionAttr(bpc.request, "taskId", taskId);
        ParamUtil.setSessionAttr(bpc.request, "inspectionReassignTaskDto", inspectionReassignTaskDto);
    }
    /**
     * StartStep: InspectionInboxSearchQuery
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchSuccess(BaseProcessClass bpc) {

        log.debug(StringUtil.changeForLog("the inspectionSupSearchSuccess start ...."));
        InspectionReassignTaskDto inspectionReassignTaskDto = (InspectionReassignTaskDto)ParamUtil.getSessionAttr(bpc.request, "inspectionReassignTaskDto");
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto) ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");

        //inspectionService.routingTaskByPool(inspectionTaskPoolListDto, ReassignPools, reassignReason);
        String reassignRemarks = inspectionReassignTaskDto.getReassignRemarks();
        routingTask(bpc,reassignRemarks);
        ParamUtil.setSessionAttr(bpc.request, "inspectionTaskPoolListDto", inspectionTaskPoolListDto);
        ParamUtil.setSessionAttr(bpc.request, "inspectionReassignTaskDto", inspectionReassignTaskDto);
    }



    private void routingTask(BaseProcessClass bpc,String reassignRemarks){
        //String taskId = ParamUtil.getMaskedString(bpc.request, "taskId");
        String taskId = ParamUtil.getRequestString(bpc.request, "taskId");
        String userId = ParamUtil.getRequestString(bpc.request, "inspectorCheck");
        TaskDto taskDto = taskService.getTaskById(taskId);
        String appNo = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = insRepService.getApplicationViewDto(appNo);
        String applicationNo = applicationViewDto.getApplicationDto().getApplicationNo();
        String appStatus = applicationViewDto.getApplicationDto().getStatus();
        String stageId = taskDto.getTaskKey();
        //remove this task and create the history
        removeTask(taskDto);
        createAppPremisesRoutingHistory(applicationNo,appStatus,stageId,reassignRemarks,InspectionConstants.PROCESS_DECI_SUPER_USER_POOL_REASSIGN, RoleConsts.USER_ROLE_INSPECTIOR);
        // send the task
        if(!StringUtil.isEmpty(stageId)) {
            String inspectorCheck = ParamUtil.getRequestString(bpc.request, "inspectorCheck");
            //add history for next stage start
            createTask(taskDto,userId);
            createAppPremisesRoutingHistory(applicationNo,appStatus,stageId,reassignRemarks,InspectionConstants.PROCESS_DECI_SUPER_USER_POOL_REASSIGN, RoleConsts.USER_ROLE_INSPECTIOR);
        }
    }
    private void removeTask(TaskDto taskDto) {
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        taskService.updateTask(taskDto);
    }

    private void createTask(TaskDto taskDto,String userId) {
        List<TaskDto> list = IaisCommonUtils.genNewArrayList();
        taskDto.setId(null);
        taskDto.setUserId(userId);
        taskDto.setDateAssigned(new Date());
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        list.add(taskDto);
        taskService.createTasks(list);
    }

//    private void updateTask(TaskDto taskDto,String userId) {
//        taskDto.setUserId(userId);
//        taskDto.setDateAssigned(new Date());
//        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
//        taskService.updateTask(taskDto);
//    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String appStatus,
                                                                         String stageId, String internalRemarks, String processDec,String roleId) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
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
