package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.SystemAssignTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.SuperPoolTaskQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.SystemAssignSearchQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.SystemSearchAssignPoolService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Process: MohSystemPoolAssign
 *
 * @author Shicheng
 * @date 2020/8/26 14:17
 **/
@Delegator("systemSearchAssignPool")
@Slf4j
public class SystemSearchAssignPoolDelegator {

    @Autowired
    private InspectionService inspectionService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private SystemSearchAssignPoolService systemSearchAssignPoolService;

    @Autowired
    private InspectionAssignTaskService inspectionAssignTaskService;

    @Autowired
    private SystemSearchAssignPoolDelegator(InspectionService inspectionService, TaskService taskService, ApplicationViewService applicationViewService,
                                            SystemSearchAssignPoolService systemSearchAssignPoolService){
        this.inspectionService = inspectionService;
        this.taskService = taskService;
        this.applicationViewService = applicationViewService;
        this.systemSearchAssignPoolService = systemSearchAssignPoolService;
    }

    /**
     * StartStep: systemPoolAssignStart
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignStart start ...."));
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_LOAD_LEVELING,  AuditTrailConsts.FUNCTION_SYS_ADMIN_ASSIGNMENT);
    }

    /**
     * StartStep: systemPoolAssignInit
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", null);
        ParamUtil.setSessionAttr(bpc.request, "systemPool", null);
        ParamUtil.setSessionAttr(bpc.request, "appTypeOption", null);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", null);
        ParamUtil.setSessionAttr(bpc.request, "systemSearchParam", null);
        ParamUtil.setSessionAttr(bpc.request, "systemSearchResult", null);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", null);
        ParamUtil.setSessionAttr(bpc.request, "systemAssignMap", null);
        ParamUtil.setSessionAttr(bpc.request, "stageOption", null);
        ParamUtil.setSessionAttr(bpc.request, "systemAssignTaskDto", null);
        ParamUtil.setSessionAttr(bpc.request, "systemPoolFilterAppNo", null);
        ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", null);
        ParamUtil.setSessionAttr(bpc.request, "systemPoolHciAddress", null);
    }

    /**
     * StartStep: systemPoolAssignPre
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignPre start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        SearchResult<SystemAssignSearchQueryDto> searchResult = (SearchResult) ParamUtil.getSessionAttr(bpc.request, "systemSearchResult");
        GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto)ParamUtil.getSessionAttr(bpc.request, "groupRoleFieldDto");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        //get userId
        String taskUserId = loginContext.getUserId();
        List<TaskDto> systemPool = systemSearchAssignPoolService.getSystemTaskPool(taskUserId);
        //First search
        if(searchResult == null && groupRoleFieldDto == null) {
            //get stage
            groupRoleFieldDto = systemSearchAssignPoolService.getSystemSearchStage();
            String curStageId = systemSearchAssignPoolService.getSysCurStageId(groupRoleFieldDto);
            if(!StringUtil.isEmpty(curStageId)){
                searchParam.addFilter("curStageId", curStageId,true);
            }
            if(!StringUtil.isEmpty(taskUserId)){
                searchParam.addFilter("taskUserId", taskUserId,true);
            }
            QueryHelp.setMainSql("inspectionQuery", "systemGroupPoolSearch",searchParam);
            searchResult = systemSearchAssignPoolService.getSystemGroupPoolByParam(searchParam);

            List<SelectOption> appTypeOption = inspectionService.getAppTypeOption();
            List<SelectOption> stageOption = groupRoleFieldDto.getStageOption();
            List<SelectOption> appStatusOption = systemSearchAssignPoolService.getAppStatusOption(groupRoleFieldDto);

            ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", groupRoleFieldDto);
            ParamUtil.setSessionAttr(bpc.request, "appTypeOption", (Serializable) appTypeOption);
            ParamUtil.setSessionAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
            ParamUtil.setSessionAttr(bpc.request, "stageOption", (Serializable) stageOption);
        }
        ParamUtil.setSessionAttr(bpc.request, "systemPool", (Serializable) systemPool);
        ParamUtil.setSessionAttr(bpc.request, "systemSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "systemSearchResult", searchResult);
    }

    private List<TaskDto> filterPoolByStage(List<TaskDto> systemPool, GroupRoleFieldDto groupRoleFieldDto) {
        String curStage = groupRoleFieldDto.getCurStage();
        List<TaskDto> systemFilterPool = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(systemPool) && !StringUtil.isEmpty(curStage)){
            Map<String, HcsaSvcRoutingStageDto> stageMap = groupRoleFieldDto.getStageMap();
            if(stageMap != null) {
                HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto = stageMap.get(curStage);
                if(hcsaSvcRoutingStageDto != null) {
                    String curStageId = hcsaSvcRoutingStageDto.getId();
                    for (TaskDto taskDto : systemPool) {
                        String stage = taskDto.getTaskKey();
                        if (!StringUtil.isEmpty(stage) && stage.equals(curStageId)) {
                            systemFilterPool.add(taskDto);
                        }
                    }
                }
            }
        }
        return systemFilterPool;
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc,boolean isNew){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, "systemSearchParam");
        int pageSize = SystemParamUtil.getDefaultPageSize();
        if(searchParam == null || isNew){
            searchParam = new SearchParam(SystemAssignSearchQueryDto.class.getName());
            searchParam.setPageSize(pageSize);
            searchParam.setPageNo(1);
            searchParam.setSort("GROUP_NO", SearchParam.ASCENDING);
        }
        return searchParam;
    }

    /**
     * StartStep: systemPoolAssignStep
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignStep start ...."));
    }

    /**
     * StartStep: systemPoolAssignDoSearch
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignDoSearch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignDoSearch start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        List<TaskDto> systemPool = (List<TaskDto>)ParamUtil.getSessionAttr(bpc.request, "systemPool");
        GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto)ParamUtil.getSessionAttr(bpc.request, "groupRoleFieldDto");
        //get search filter
        String systemAssignStage = ParamUtil.getRequestString(bpc.request, "systemAssignStage");
        String application_no = ParamUtil.getRequestString(bpc.request, "application_no");
        String application_type = ParamUtil.getRequestString(bpc.request, "application_type");
        String application_status = ParamUtil.getRequestString(bpc.request, "application_status");
        String hci_code = ParamUtil.getRequestString(bpc.request, "hci_code");
        String hci_name = ParamUtil.getRequestString(bpc.request, "hci_name");
        String hci_address = ParamUtil.getRequestString(bpc.request, "hci_address");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        //get system admin userId
        String taskUserId = loginContext.getUserId();
        //systemAssignStage is true and Set current stage
        boolean stageFlag = getStageBooleanAndSet(systemAssignStage, groupRoleFieldDto);
        if(stageFlag) {
            searchParam = getSearchParam(bpc, true);
            String curStageId = systemSearchAssignPoolService.getSysCurStageId(groupRoleFieldDto);
            if(!StringUtil.isEmpty(curStageId)){
                searchParam.addFilter("curStageId", curStageId,true);
            }
            //filter system admin user
            if(!StringUtil.isEmpty(taskUserId)){
                searchParam.addFilter("taskUserId", taskUserId,true);
            }
            if (!StringUtil.isEmpty(application_no)) {
                searchParam.addFilter("application_no", application_no, true);
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
                ParamUtil.setSessionAttr(bpc.request, "systemPoolHciAddress", hci_address);
            } else {
                ParamUtil.setSessionAttr(bpc.request, "systemPoolHciAddress", null);
            }
        }
        List<SelectOption> appStatusOption = systemSearchAssignPoolService.getAppStatusOption(groupRoleFieldDto);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
        ParamUtil.setSessionAttr(bpc.request, "systemPool", (Serializable) systemPool);
        ParamUtil.setSessionAttr(bpc.request, "systemSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", groupRoleFieldDto);
    }

    private boolean getStageBooleanAndSet(String systemAssignStage, GroupRoleFieldDto groupRoleFieldDto) {
        if(!StringUtil.isEmpty(systemAssignStage) && groupRoleFieldDto != null){
            List<SelectOption> stageOptions = groupRoleFieldDto.getStageOption();
            if(!IaisCommonUtils.isEmpty(stageOptions)){
                for(SelectOption so : stageOptions){
                    if(so != null) {
                        if (systemAssignStage.equals(so.getValue())) {
                            groupRoleFieldDto.setCurStage(systemAssignStage);
                            return true;
                        }
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * StartStep: systemPoolAssignSort
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignSort(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignSort start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doSorting(searchParam, bpc.request);
    }

    /**
     * StartStep: systemPoolAssignPage
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignPage start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    /**
     * StartStep: systemPoolAssignQuery
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignQuery(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignQuery start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        String hci_address = (String)ParamUtil.getSessionAttr(bpc.request, "reAssignPoolHciAddress");
        SearchResult<SystemAssignSearchQueryDto> searchResult;
        if(StringUtil.isEmpty(hci_address)) {
            QueryHelp.setMainSql("inspectionQuery", "systemGroupPoolSearch", searchParam);
            searchResult = systemSearchAssignPoolService.getSystemGroupPoolByParam(searchParam);
            ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", null);
        } else {
            //copy SearchParam for searchAllParam
            SearchParam searchAllParam = (SearchParam) CopyUtil.copyMutableObject(searchParam);
            searchAllParam.setPageSize(-1);
            //get all appGroupIds
            QueryHelp.setMainSql("inspectionQuery", "systemGroupPoolSearch", searchAllParam);
            searchResult = systemSearchAssignPoolService.getSystemGroupPoolByParam(searchAllParam);
            //set all address data map for filter address
            List<String> appGroupIds = systemSearchAssignPoolService.getSystemPoolAppGrpIdByResult(searchResult);
            HcsaTaskAssignDto hcsaTaskAssignDto = inspectionService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
            //filter unit no for group
            searchParam = inspectionAssignTaskService.setAppGrpIdsByUnitNos(searchParam, hci_address, hcsaTaskAssignDto, "T5.ID", "appGroup_list");
            QueryHelp.setMainSql("inspectionQuery", "systemGroupPoolSearch", searchParam);
            searchResult = systemSearchAssignPoolService.getSystemGroupPoolByParam(searchParam);

            ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", hcsaTaskAssignDto);
        }

        ParamUtil.setSessionAttr(bpc.request, "systemSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "systemSearchResult", searchResult);
    }

    /**
     * StartStep: systemPoolAssignAssign
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignAssign(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignAssign start ...."));
        GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto)ParamUtil.getSessionAttr(bpc.request, "groupRoleFieldDto");
        SystemAssignTaskDto systemAssignTaskDto = (SystemAssignTaskDto)ParamUtil.getSessionAttr(bpc.request, "systemAssignTaskDto");
        String taskId = ParamUtil.getMaskedString(bpc.request, "taskId");
        if(!StringUtil.isEmpty(taskId)) {
            systemAssignTaskDto = new SystemAssignTaskDto();
            //set MOH Officer Field Name
            groupRoleFieldDto = systemSearchAssignPoolService.setGroupMemberName(groupRoleFieldDto);
            TaskDto taskDto = taskService.getTaskById(taskId);
            ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(taskDto.getRefNo());
            systemAssignTaskDto.setTaskDto(taskDto);
            Map<String, SuperPoolTaskQueryDto> systemAssignMap = (Map<String, SuperPoolTaskQueryDto>) ParamUtil.getSessionAttr(bpc.request, "systemAssignMap");
            //get work group
            systemAssignTaskDto = systemSearchAssignPoolService.setWorkGroupAndOfficer(groupRoleFieldDto, systemAssignTaskDto);
            //set session inspector options
            Map<String, List<SelectOption>> inspectorByGroup = systemAssignTaskDto.getInspectorByGroup();
            List<SelectOption> workGroupOptions = systemAssignTaskDto.getWorkGroupOptions();
            if (inspectorByGroup != null) {
                for (Map.Entry<String, List<SelectOption>> map : inspectorByGroup.entrySet()) {
                    String groupNo = map.getKey();
                    List<SelectOption> officerOption = map.getValue();
                    ParamUtil.setSessionAttr(bpc.request, "sysMohOfficerOption" + groupNo, (Serializable) officerOption);
                }
            }
            //set task other data to show
            systemAssignTaskDto = systemSearchAssignPoolService.getDataForSystemAssignTask(systemAssignMap, systemAssignTaskDto, taskDto, applicationViewDto);
            ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
            ParamUtil.setSessionAttr(bpc.request, "workGroupOptions", (Serializable) workGroupOptions);
        }
        ParamUtil.setSessionAttr(bpc.request, "systemAssignTaskDto", systemAssignTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", groupRoleFieldDto);
    }

    /**
     * StartStep: systemPoolAssignVali
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignVali start ...."));
        GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto)ParamUtil.getSessionAttr(bpc.request, "groupRoleFieldDto");
        SystemAssignTaskDto systemAssignTaskDto = (SystemAssignTaskDto)ParamUtil.getSessionAttr(bpc.request, "systemAssignTaskDto");
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        String workGroupCheck = ParamUtil.getRequestString(bpc.request, "systemAssignWorkGroup");
        String userCheck = ParamUtil.getRequestString(bpc.request, "sysMohOfficerName" + workGroupCheck);
        if(!(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue))){
            List<String> workGroupNos = systemAssignTaskDto.getWorkGroupNos();
            boolean containsFlag = false;
            if(IaisCommonUtils.isEmpty(workGroupNos) || !workGroupNos.contains(workGroupCheck)) {
                systemAssignTaskDto.setCheckWorkGroup(null);
            } else {
                containsFlag = checkUserContainsFlag(userCheck, workGroupCheck, systemAssignTaskDto);
            }
            ValidationResult validationResult = WebValidationHelper.validateProperty(systemAssignTaskDto, "system");
            Map<String, String> errorMap = validationResult.retrieveAll();
            boolean errorFlag = true;
            if(StringUtil.isEmpty(userCheck) || !containsFlag){
                systemAssignTaskDto.setCheckUser(null);
                if(errorMap == null){
                    errorMap = IaisCommonUtils.genNewHashMap();
                }
                errorFlag = false;
                errorMap.put("systemUserCheck", MessageUtil.replaceMessage("GENERAL_ERR0006", groupRoleFieldDto.getGroupMemBerName(),"field"));
            }
            if (validationResult.isHasErrors() || !errorFlag) {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                systemAssignTaskDto.setCheckWorkGroup(workGroupCheck);
                systemAssignTaskDto.setCheckUser(userCheck);
                ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.TRUE);
            }
        } else {
            ParamUtil.setSessionAttr(bpc.request, "systemAssignTaskDto", null);
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        }
        ParamUtil.setSessionAttr(bpc.request, "systemAssignTaskDto", systemAssignTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", groupRoleFieldDto);
    }

    private boolean checkUserContainsFlag(String userCheck, String workGroupCheck, SystemAssignTaskDto systemAssignTaskDto) {
        if(StringUtil.isEmpty(userCheck)){
            return false;
        } else {
            Map<String, List<SelectOption>> inspectorByGroup = systemAssignTaskDto.getInspectorByGroup();
            if(inspectorByGroup != null){
                List<SelectOption> officerOption = inspectorByGroup.get(workGroupCheck);
                if(officerOption != null){
                    for(SelectOption so : officerOption){
                        if(!StringUtil.isEmpty(so.getValue()) && userCheck.equals(so.getValue())){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * StartStep: systemPoolAssignConfirm
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignConfirm(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignConfirm start ...."));
        GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto)ParamUtil.getSessionAttr(bpc.request, "groupRoleFieldDto");
        SystemAssignTaskDto systemAssignTaskDto = (SystemAssignTaskDto)ParamUtil.getSessionAttr(bpc.request, "systemAssignTaskDto");
        systemAssignTaskDto = systemSearchAssignPoolService.getCheckGroupNameAndUserName(systemAssignTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "systemAssignTaskDto", systemAssignTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", groupRoleFieldDto);
    }

    /**
     * StartStep: systemPoolAssignSuccess
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignSuccess start ...."));
        GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto)ParamUtil.getSessionAttr(bpc.request, "groupRoleFieldDto");
        SystemAssignTaskDto systemAssignTaskDto = (SystemAssignTaskDto)ParamUtil.getSessionAttr(bpc.request, "systemAssignTaskDto");
        systemSearchAssignPoolService.systemAssignTask(systemAssignTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "systemAssignTaskDto", systemAssignTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", groupRoleFieldDto);
    }
}
