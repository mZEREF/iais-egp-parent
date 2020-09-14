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
import com.ecquaria.cloud.moh.iais.common.dto.inbox.PoolRoleCheckDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.SuperPoolTaskQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
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
import com.ecquaria.cloud.moh.iais.helper.SysParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Process: MohInspectionInboxSearch
 *
 * @author Shicheng
 * @date 2019/11/14 18:01
 **/
@Delegator("inspectionSearchDelegator")
@Slf4j
public class InspectionSearchDelegator {

    @Autowired
    private InspectionService inspectionService;

    @Autowired
    private InspectionAssignTaskService inspectionAssignTaskService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private InspectionSearchDelegator(InspectionService inspectionService, ApplicationViewService applicationViewService,
                                      InspectionAssignTaskService inspectionAssignTaskService, TaskService taskService){
        this.inspectionService = inspectionService;
        this.applicationViewService = applicationViewService;
        this.inspectionAssignTaskService = inspectionAssignTaskService;
        this.taskService = taskService;
    }

    /**
     * StartStep: inspectionSupSearchStart
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchStart start ...."));
        AuditTrailHelper.auditFunction("Inspection Sup Assign", "Sup Assign Task");
    }

    /**
     * StartStep: inspectionSupSearchInit
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchInit start ...."));
        ParamUtil.setSessionAttr(bpc.request,"inspectionTaskPoolListDto", null);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", null);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", null);
        ParamUtil.setSessionAttr(bpc.request, "workGroupIds", null);
        ParamUtil.setSessionAttr(bpc.request, "appTypeOption", null);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", null);
        ParamUtil.setSessionAttr(bpc.request, "superPool", null);
        ParamUtil.setSessionAttr(bpc.request, "memberId", null);
        ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", null);
        ParamUtil.setSessionAttr(bpc.request, "poolRoleCheckDto", null);
        ParamUtil.setSessionAttr(bpc.request, "superPoolRoleIds", null);
        ParamUtil.setSessionAttr(bpc.request, "assignMap", null);
    }

    /**
     * StartStep: inspectionSupSearchPre
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchPre start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        SearchResult<InspectionSubPoolQueryDto> searchResult = (SearchResult) ParamUtil.getSessionAttr(bpc.request, "supTaskSearchResult");
        //First search
        if(searchResult == null) {
            LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            PoolRoleCheckDto poolRoleCheckDto = new PoolRoleCheckDto();
            poolRoleCheckDto = inspectionAssignTaskService.getRoleOptionByKindPool(loginContext, AppConsts.SUPERVISOR_POOL, poolRoleCheckDto);
            List<SelectOption> superPoolRoleIds = poolRoleCheckDto.getRoleOptions();
            List<String> workGroupIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "workGroupIds");
            if(IaisCommonUtils.isEmpty(workGroupIds)) {
                workGroupIds = inspectionService.getWorkGroupIdsByLogin(loginContext);
            }
            GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto)ParamUtil.getSessionAttr(bpc.request, "groupRoleFieldDto");
            if(groupRoleFieldDto == null){
                groupRoleFieldDto = inspectionAssignTaskService.getGroupRoleField(loginContext);
            }
            List<SelectOption> appTypeOption = inspectionService.getAppTypeOption();
            List<SelectOption> appStatusOption = inspectionService.getAppStatusOption(loginContext);
            //get Members Option
            groupRoleFieldDto = inspectionService.getInspectorOptionByLogin(loginContext, workGroupIds, groupRoleFieldDto);
            List<TaskDto> superPool = getSupervisorPoolByGroupWordId(workGroupIds, loginContext);
            List<String> appCorrId_list = inspectionService.getApplicationNoListByPool(superPool);
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < appCorrId_list.size(); i++) {
                sb.append(":appCorrId")
                        .append(i)
                        .append(',');
            }
            String inSql = sb.substring(0, sb.length() - 1) + ")";
            searchParam.addParam("appCorrId_list", inSql);
            for (int i = 0; i < appCorrId_list.size(); i++) {
                searchParam.addFilter("appCorrId" + i, appCorrId_list.get(i));
            }
            QueryHelp.setMainSql("inspectionQuery", "supervisorPoolSearch",searchParam);
            searchResult = inspectionService.getSupPoolByParam(searchParam);
            searchResult = inspectionService.getGroupLeadName(searchResult, loginContext, superPool);
            ParamUtil.setSessionAttr(bpc.request, "superPool", (Serializable) superPool);
            ParamUtil.setSessionAttr(bpc.request, "appTypeOption", (Serializable) appTypeOption);
            ParamUtil.setSessionAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
            ParamUtil.setSessionAttr(bpc.request, "memberOption", (Serializable) groupRoleFieldDto.getMemberOption());
            ParamUtil.setSessionAttr(bpc.request, "workGroupIds", (Serializable) workGroupIds);
            ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", groupRoleFieldDto);
            ParamUtil.setSessionAttr(bpc.request, "superPoolRoleIds", (Serializable) superPoolRoleIds);
            ParamUtil.setSessionAttr(bpc.request, "poolRoleCheckDto", poolRoleCheckDto);
            ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
        }
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", searchResult);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc,boolean isNew){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, "supTaskSearchParam");
        int pageSize = SysParamUtil.getDefaultPageSize();
        if(searchParam == null || isNew){
            searchParam = new SearchParam(InspectionSubPoolQueryDto.class.getName());
            searchParam.setPageSize(pageSize);
            searchParam.setPageNo(1);
            searchParam.setSort("GROUP_NO", SearchParam.ASCENDING);
        }
        return searchParam;
    }

    /**
     * StartStep: inspectionSupSearchStartStep1
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchStartStep1(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchStartStep1 start ...."));

    }

    /**
     * StartStep: inspectionSupSearchDoSearch
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchDoSearch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchDoSearch start ...."));
        SearchParam searchParam = getSearchParam(bpc, true);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        PoolRoleCheckDto poolRoleCheckDto = (PoolRoleCheckDto)ParamUtil.getSessionAttr(bpc.request, "poolRoleCheckDto");
        //get search filter
        String application_no = ParamUtil.getRequestString(bpc.request, "application_no");
        String application_type = ParamUtil.getRequestString(bpc.request, "application_type");
        String application_status = ParamUtil.getRequestString(bpc.request, "application_status");
        String hci_code = ParamUtil.getRequestString(bpc.request, "hci_code");
        String hci_name = ParamUtil.getRequestString(bpc.request, "hci_name");
        String hci_address = ParamUtil.getRequestString(bpc.request, "hci_address");
        String userIdKey = ParamUtil.getRequestString(bpc.request, "memberName");
        String roleIdCheck = ParamUtil.getRequestString(bpc.request, "supervisorRoleId");
        Map<String, String> roleMap = poolRoleCheckDto.getRoleMap();
        String roleId = getCheckRoleIdByMap(roleIdCheck, roleMap);
        loginContext.setCurRoleId(roleId);
        if(!StringUtil.isEmpty(roleId)){
            poolRoleCheckDto.setCheckCurRole(roleIdCheck);
            ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
        }
        GroupRoleFieldDto groupRoleFieldDto = inspectionAssignTaskService.getGroupRoleField(loginContext);
        //get Members Option
        List<String> workGroupIds = inspectionService.getWorkGroupIdsByLogin(loginContext);
        groupRoleFieldDto = inspectionService.getInspectorOptionByLogin(loginContext, workGroupIds, groupRoleFieldDto);
        //get userId
        String memberValue = "";
        if(!StringUtil.isEmpty(userIdKey)) {
            Map<String, String> userIdMap = groupRoleFieldDto.getUserIdMap();
            String userId = userIdMap.get(userIdKey);
            groupRoleFieldDto.setCheckUser(userIdKey);
            //get task ref_no by uerId
            memberValue = inspectionService.getMemberValueByWorkGroupUserId(userId);
            ParamUtil.setSessionAttr(bpc.request, "memberId", userId);
        } else {
            ParamUtil.setSessionAttr(bpc.request, "memberId", null);
        }
        List<TaskDto> superPool = getSupervisorPoolByGroupWordId(workGroupIds, loginContext);
        List<String> appCorrId_list = inspectionService.getApplicationNoListByPool(superPool);
        StringBuilder sb = new StringBuilder("(");
        for(int i = 0; i < appCorrId_list.size(); i++){
            sb.append(":appCorrId")
                    .append(i)
                    .append(',');
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
        if(!(StringUtil.isEmpty(memberValue))) {
            appCorIdStrs = memberValue.split(",");
            StringBuilder sb2 = new StringBuilder("(");
            for(int i = 0; i < appCorIdStrs.length; i++){
                sb2.append(":appCorId")
                        .append(i)
                        .append(',');
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
        List<SelectOption> appStatusOption = inspectionService.getAppStatusOption(loginContext);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
        ParamUtil.setSessionAttr(bpc.request, "superPool", (Serializable) superPool);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", groupRoleFieldDto);
        ParamUtil.setSessionAttr(bpc.request, "workGroupIds", (Serializable) workGroupIds);
    }

    private String getCheckRoleIdByMap(String roleIdCheck, Map<String, String> roleMap) {
        String roleId = "";
        if(roleMap != null && !StringUtil.isEmpty(roleIdCheck)){
            roleId = roleMap.get(roleIdCheck);
            if(!StringUtil.isEmpty(roleId)){
                return roleId;
            } else {
                return "";
            }
        }
        return roleId;
    }

    private List<TaskDto> getSupervisorPoolByGroupWordId(List<String> workGroupIds, LoginContext loginContext) {
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isEmpty(workGroupIds)){
            return null;
        }
        String curRole = loginContext.getCurRoleId();
        //get member role
        String memberRole;
        if (curRole.contains(RoleConsts.USER_LEAD)) {
            memberRole = curRole.replaceFirst(RoleConsts.USER_LEAD, "");
        } else {
            memberRole = curRole;
        }
        for(String workGrpId : workGroupIds){
            //filter System admin task and other role task
            List<String> userIdsByGroup = inspectionService.getUserIdByWorkGrpId(workGrpId);
            if(userIdsByGroup != null) {
                List<TaskDto> taskDtos = inspectionService.getSupervisorPoolByGroupWordId(workGrpId);
                for (TaskDto tDto : taskDtos) {
                    if (!StringUtil.isEmpty(tDto.getRoleId()) && (userIdsByGroup.contains(tDto.getUserId()) || StringUtil.isEmpty(tDto.getUserId()))) {
                        if (tDto.getRoleId().equals(curRole) || tDto.getRoleId().equals(memberRole)) {
                            taskDtoList.add(tDto);
                        }
                    }
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
    public void inspectionSupSearchSort(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchSort start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doSorting(searchParam, bpc.request);
    }

    /**
     * StartStep: inspectionSupSearchPage
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchPage start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    /**
     * StartStep: inspectionSupSearchQuery1
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchQuery1(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchQuery1 start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        List<TaskDto> superPool = (List<TaskDto>)ParamUtil.getSessionAttr(bpc.request, "superPool");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        QueryHelp.setMainSql("inspectionQuery", "supervisorPoolSearch",searchParam);
        SearchResult<InspectionSubPoolQueryDto> searchResult = inspectionService.getSupPoolByParam(searchParam);
        searchResult = inspectionService.getGroupLeadName(searchResult, loginContext, superPool);

        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", searchResult);
    }

    /**
     * StartStep: inspectionSupSearchAssign
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchAssign(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchAssign start ...."));
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        SearchResult<InspectionSubPoolQueryDto> searchResult = (SearchResult<InspectionSubPoolQueryDto>)ParamUtil.getSessionAttr(bpc.request, "supTaskSearchResult");
        String taskId = "";
        try{
            taskId = ParamUtil.getMaskedString(bpc.request, "taskId");
        }catch (MaskAttackException e){
            log.error(e.getMessage(), e);
            try{
                bpc.response.sendRedirect("https://"+bpc.request.getServerName()+"/hcsa-licence-web/CsrfErrorPage.jsp");
            } catch (IOException ioe){
                log.error(ioe.getMessage(), ioe);
                return;
            }
        }
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto)ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        if(!(StringUtil.isEmpty(taskId))){
            Map<String, SuperPoolTaskQueryDto> assignMap = (Map<String, SuperPoolTaskQueryDto>) ParamUtil.getSessionAttr(bpc.request, "assignMap");
            inspectionTaskPoolListDto = new InspectionTaskPoolListDto();
            inspectionTaskPoolListDto = inspectionService.getDataForAssignTask(assignMap, inspectionTaskPoolListDto, taskId);
            //get inspector Option
            inspectionTaskPoolListDto = inspectionService.inputInspectorOption(inspectionTaskPoolListDto, loginContext);
            if(inspectionTaskPoolListDto.getTaskDto() != null){
                String appPremCorrId = inspectionTaskPoolListDto.getTaskDto().getRefNo();
                ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(appPremCorrId);
                ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
                if(StringUtil.isEmpty(inspectionTaskPoolListDto.getTaskDto().getUserId())){
                    inspectionTaskPoolListDto.setEditHoursFlag(AppConsts.COMMON_POOL);
                }
            }
            if(!(IaisCommonUtils.isEmpty(inspectionTaskPoolListDto.getInspectorOption()))){
                inspectionTaskPoolListDto.setInspectorFlag(AppConsts.TRUE);
            } else {
                inspectionTaskPoolListDto.setInspectorFlag(AppConsts.FALSE);
            }
        }

        ParamUtil.setSessionAttr(bpc.request, "inspectionTaskPoolListDto", inspectionTaskPoolListDto);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", searchResult);
    }

    /**
     * StartStep: inspectionSupSearchValidate
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchValidate(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchValidate start ...."));
        InspectionTaskPoolListDto inspectionTaskPoolListDto = getValueFromPage(bpc);
        GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto)ParamUtil.getSessionAttr(bpc.request, "groupRoleFieldDto");
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        if(!(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue))){
            String propertyName;
            if(AppConsts.COMMON_POOL.equals(inspectionTaskPoolListDto.getEditHoursFlag())) {
                propertyName = AppConsts.COMMON_POOL;
            } else {
                propertyName = "create";
            }
            ValidationResult validationResult = WebValidationHelper.validateProperty(inspectionTaskPoolListDto, propertyName);
            Map<String, String> errorMap = validationResult.retrieveAll();
            boolean errorFlag = true;
            if("create".equals(propertyName)){
                List<SelectOption> inspectorCheck = inspectionTaskPoolListDto.getInspectorCheck();
                if(inspectorCheck == null){
                    if(errorMap == null){
                        errorMap = IaisCommonUtils.genNewHashMap();
                    }
                    errorFlag = false;
                    errorMap.put("inspectorCheck", MessageUtil.replaceMessage("GENERAL_ERR0006", groupRoleFieldDto.getGroupMemBerName(),"field"));
                }
            }
            if (validationResult.isHasErrors() || !errorFlag) {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.TRUE);
            }
        }else{
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        }
        ParamUtil.setSessionAttr(bpc.request,"inspectionTaskPoolListDto", inspectionTaskPoolListDto);
    }

    public InspectionTaskPoolListDto getValueFromPage(BaseProcessClass bpc) {
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto)ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        String[] nameValue = ParamUtil.getStrings(bpc.request,"inspectorCheck");
        String editHoursFlag = inspectionTaskPoolListDto.getEditHoursFlag();
        if(AppConsts.COMMON_POOL.equals(editHoursFlag)){
            String inspManHours = ParamUtil.getRequestString(bpc.request, "inspManHours");
            inspectionTaskPoolListDto.setInspManHours(inspManHours);
        }
        if(nameValue == null || nameValue.length <= 0) {
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
    public void inspectionSupSearchQuery2(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchQuery2 start ...."));
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto)ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        ParamUtil.setSessionAttr(bpc.request,"inspectionTaskPoolListDto", inspectionTaskPoolListDto);
    }

    /**
     * StartStep: inspectionSupSearchConfirm
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchConfirm(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchConfirm start ...."));
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto)ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        ParamUtil.setSessionAttr(bpc.request,"inspectionTaskPoolListDto", inspectionTaskPoolListDto);
    }

    /**
     * StartStep: InspectionInboxSearchQuery
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchSuccess start ...."));
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto)ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        List<TaskDto> superPool =(List<TaskDto>)ParamUtil.getSessionAttr(bpc.request, "superPool");
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        String taskId = inspectionTaskPoolListDto.getTaskId();
        TaskDto taskDto = taskService.getTaskById(taskId);
        if(TaskConsts.TASK_STATUS_COMPLETED.equals(taskDto.getTaskStatus()) || TaskConsts.TASK_STATUS_REMOVE.equals(taskDto.getTaskStatus())){
            ParamUtil.setRequestAttr(bpc.request,"taskHasBeenAssigned", AppConsts.TRUE);
        } else {
            inspectionService.routingTaskByPool(inspectionTaskPoolListDto, superPool, internalRemarks);
        }
        ParamUtil.setSessionAttr(bpc.request,"inspectionTaskPoolListDto", inspectionTaskPoolListDto);
        ParamUtil.setSessionAttr(bpc.request, "superPool", (Serializable) superPool);
    }
}
