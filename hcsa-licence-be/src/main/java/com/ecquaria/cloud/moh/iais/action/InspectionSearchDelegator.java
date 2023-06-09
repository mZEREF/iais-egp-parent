package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.PoolRoleCheckDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.SuperPoolTaskQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
    public static final String INSPECTION_TASKPOOL_LISTDTO = "inspectionTaskPoolListDto";
    public static final String SUP_TASK_SEARCH_PARAM = "supTaskSearchParam";
    public static final String SUP_TASK_SEARCH_RESULT = "supTaskSearchResult";
    public static final String WORK_GROUP_IDS = "workGroupIds";
    public static final String APP_STATUS_OPTION = "appStatusOption";
    public static final String SUPER_POOL = "superPool";
    public static final String MEMBER_ID = "memberId";
    public static final String GROUP_ROLE_FIELDDTO = "groupRoleFieldDto";
    public static final String POOL_ROLE_CHECKDTO = "poolRoleCheckDto";
    public static final String COMMON_POOL_STATUS = "commonPoolStatus";
    public static final String HCSA_TASK_ASSIGNDTO = "hcsaTaskAssignDto";
    public static final String SUPER_POOL_HCIADDRESS = "superPoolHciAddress";
    public static final String WRK_GRP_ID = "T7.WRK_GRP_ID";
    public static final String WORKGROUP_LIST = "workGroup_list";
    public static final String ROLE_ID = "T7.ROLE_ID";
    public static final String ROLEID_LIST = "roleId_List";
    public static final String SUPERVISOR_POOLSEARCH = "supervisorPoolSearch";
    public static final String INSPECTION_QUERY = "inspectionQuery";

    @Autowired
    private InspectionSearchDelegator(InspectionService inspectionService, ApplicationViewService applicationViewService,
                                      InspectionAssignTaskService inspectionAssignTaskService){
        this.inspectionService = inspectionService;
        this.applicationViewService = applicationViewService;
        this.inspectionAssignTaskService = inspectionAssignTaskService;
    }

    /**
     * StartStep: inspectionSupSearchStart
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchStart(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the inspectionSupSearchStart start ...."));
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_LOAD_LEVELING,  AuditTrailConsts.FUNCTION_SUPERVISOR_ASSIGNMENT);
    }

    /**
     * StartStep: inspectionSupSearchInit
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchInit(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the inspectionSupSearchInit start ...."));
        ParamUtil.setSessionAttr(bpc.request,INSPECTION_TASKPOOL_LISTDTO, null);
        ParamUtil.setSessionAttr(bpc.request, SUP_TASK_SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(bpc.request, SUP_TASK_SEARCH_RESULT, null);
        ParamUtil.setSessionAttr(bpc.request, WORK_GROUP_IDS, null);
        ParamUtil.setSessionAttr(bpc.request, "appTypeOption", null);
        ParamUtil.setSessionAttr(bpc.request, APP_STATUS_OPTION, null);
        ParamUtil.setSessionAttr(bpc.request, SUPER_POOL, null);
        ParamUtil.setSessionAttr(bpc.request, MEMBER_ID, null);
        ParamUtil.setSessionAttr(bpc.request, GROUP_ROLE_FIELDDTO, null);
        ParamUtil.setSessionAttr(bpc.request, POOL_ROLE_CHECKDTO, null);
        ParamUtil.setSessionAttr(bpc.request, "superPoolRoleIds", null);
        ParamUtil.setSessionAttr(bpc.request, "assignMap", null);
        ParamUtil.setSessionAttr(bpc.request, COMMON_POOL_STATUS, null);
        ParamUtil.setSessionAttr(bpc.request, HCSA_TASK_ASSIGNDTO, null);
        ParamUtil.setSessionAttr(bpc.request, SUPER_POOL_HCIADDRESS, null);
    }

    /**
     * StartStep: inspectionSupSearchPre
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchPre(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the inspectionSupSearchPre start ...."));
        try {
            SearchParam searchParam = getSearchParam(bpc);
            SearchResult<InspectionSubPoolQueryDto> searchResult = (SearchResult) ParamUtil.getSessionAttr(bpc.request, SUP_TASK_SEARCH_RESULT);
            //First search
            if (searchResult == null) {
                LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
                PoolRoleCheckDto poolRoleCheckDto = new PoolRoleCheckDto();
                poolRoleCheckDto = inspectionAssignTaskService.getRoleOptionByKindPool(loginContext, AppConsts.SUPERVISOR_POOL, poolRoleCheckDto);
                List<SelectOption> superPoolRoleIds = poolRoleCheckDto.getRoleOptions();
                List<String> workGroupIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, WORK_GROUP_IDS);
                if (IaisCommonUtils.isEmpty(workGroupIds)) {
                    workGroupIds = inspectionService.getWorkGroupIdsByLogin(loginContext);
                }
                GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto) ParamUtil.getSessionAttr(bpc.request, GROUP_ROLE_FIELDDTO);
                if (groupRoleFieldDto == null) {
                    groupRoleFieldDto = inspectionAssignTaskService.getGroupRoleField(loginContext);
                }
                List<SelectOption> appTypeOption = inspectionService.getAppTypeOption();
                List<SelectOption> appStatusOption = inspectionService.getAppStatusOption(loginContext, AppConsts.SUPERVISOR_POOL);
                //get Members Option
                groupRoleFieldDto = inspectionService.getInspectorOptionByLogin(loginContext, workGroupIds, groupRoleFieldDto);
                List<TaskDto> superPool = getSupervisorPoolByGroupWordId(workGroupIds, loginContext);
                int workGroupIdsSize = 0;
                if(!IaisCommonUtils.isEmpty(workGroupIds)) {
                    workGroupIdsSize = workGroupIds.size();
                    String workGroupId = SqlHelper.constructInCondition(WRK_GRP_ID, workGroupIdsSize);
                    searchParam.addParam(WORKGROUP_LIST, workGroupId);
                    for (int i = 0; i < workGroupIds.size(); i++) {
                        searchParam.addFilter(WRK_GRP_ID + i, workGroupIds.get(i));
                    }
                } else {
                    String workGroupId = SqlHelper.constructInCondition(WRK_GRP_ID, workGroupIdsSize);
                    searchParam.addParam(WORKGROUP_LIST, workGroupId);
                }
                if(loginContext != null && !StringUtil.isEmpty(loginContext.getCurRoleId())) {
                    List<String> roleIdList = new ArrayList<>(2);
                    String curRoleId = loginContext.getCurRoleId();
                    roleIdList.add(curRoleId);
                    roleIdList.add(curRoleId.replace(RoleConsts.USER_LEAD, ""));
                    String roleId = SqlHelper.constructInCondition(ROLE_ID, roleIdList.size());
                    searchParam.addParam(ROLEID_LIST, roleId);
                    for (int i = 0; i < roleIdList.size(); i++) {
                        searchParam.addFilter(ROLE_ID + i, roleIdList.get(i));
                    }
                } else {
                    String roleId = SqlHelper.constructInCondition(ROLE_ID, 0);
                    searchParam.addParam(ROLEID_LIST, roleId);
                }
                QueryHelp.setMainSql(INSPECTION_QUERY, SUPERVISOR_POOLSEARCH, searchParam);
                searchResult = inspectionService.getSupPoolByParam(searchParam);
                searchResult = inspectionService.getGroupLeadName(searchResult, loginContext);

                ParamUtil.setSessionAttr(bpc.request, SUPER_POOL, (Serializable) superPool);
                ParamUtil.setSessionAttr(bpc.request, "appTypeOption", (Serializable) appTypeOption);
                ParamUtil.setSessionAttr(bpc.request, APP_STATUS_OPTION, (Serializable) appStatusOption);
                ParamUtil.setSessionAttr(bpc.request, "memberOption", (Serializable) groupRoleFieldDto.getMemberOption());
                ParamUtil.setSessionAttr(bpc.request, WORK_GROUP_IDS, (Serializable) workGroupIds);
                ParamUtil.setSessionAttr(bpc.request, GROUP_ROLE_FIELDDTO, groupRoleFieldDto);
                ParamUtil.setSessionAttr(bpc.request, "superPoolRoleIds", (Serializable) superPoolRoleIds);
                ParamUtil.setSessionAttr(bpc.request, POOL_ROLE_CHECKDTO, poolRoleCheckDto);
                ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
            }
            ParamUtil.setSessionAttr(bpc.request, SUP_TASK_SEARCH_PARAM, searchParam);
            ParamUtil.setSessionAttr(bpc.request, SUP_TASK_SEARCH_RESULT, searchResult);
            ParamUtil.setRequestAttr(bpc.request, "supervisorErrorPage", AppConsts.TRUE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            ParamUtil.setRequestAttr(bpc.request, "supervisorErrorPage", AppConsts.FALSE);
        }
        log.info(StringUtil.changeForLog("the inspectionSupSearchPre end ...."));
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc,boolean isNew){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, SUP_TASK_SEARCH_PARAM);
        int pageSize = SystemParamUtil.getDefaultPageSize();
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
        log.info(StringUtil.changeForLog("the inspectionSupSearchStartStep1 start ...."));
    }

    /**
     * StartStep: inspectionSupSearchDoSearch
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchDoSearch(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the inspectionSupSearchDoSearch start ...."));
        SearchParam searchParam = getSearchParam(bpc, true);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        PoolRoleCheckDto poolRoleCheckDto = (PoolRoleCheckDto)ParamUtil.getSessionAttr(bpc.request, POOL_ROLE_CHECKDTO);
        //get search filter
        String application_no = ParamUtil.getRequestString(bpc.request, "application_no");
        String application_type = ParamUtil.getRequestString(bpc.request, "application_type");
        String application_status = ParamUtil.getRequestString(bpc.request, "superAppStatus");
        String hci_code = ParamUtil.getRequestString(bpc.request, "hci_code");
        String hci_name = ParamUtil.getRequestString(bpc.request, "hci_name");
        String hci_address = ParamUtil.getRequestString(bpc.request, "hci_address");
        String userIdKey = ParamUtil.getRequestString(bpc.request, "memberName");
        String roleIdCheck = ParamUtil.getRequestString(bpc.request, "supervisorRoleId");
        Map<String, String> roleMap = poolRoleCheckDto.getRoleMap();
        String roleId = getCheckRoleIdByMap(roleIdCheck, roleMap);
        if(!StringUtil.isEmpty(roleId)){
            loginContext.setCurRoleId(roleId);
            poolRoleCheckDto.setCheckCurRole(roleIdCheck);
            ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
        }
        GroupRoleFieldDto groupRoleFieldDto = inspectionAssignTaskService.getGroupRoleField(loginContext);
        //get Members Option
        List<String> workGroupIds = inspectionService.getWorkGroupIdsByLogin(loginContext);
        groupRoleFieldDto = inspectionService.getInspectorOptionByLogin(loginContext, workGroupIds, groupRoleFieldDto);
        //filter task pool by user
        if(!StringUtil.isEmpty(userIdKey)) {
            Map<String, String> userIdMap = groupRoleFieldDto.getUserIdMap();
            String userId = userIdMap.get(userIdKey);
            groupRoleFieldDto.setCheckUser(userIdKey);
            //get task ref_no by uerId
            if(!StringUtil.isEmpty(userId)){
                searchParam.addFilter("taskUserId", userId,true);
            }
            ParamUtil.setSessionAttr(bpc.request, MEMBER_ID, userId);
        } else {
            ParamUtil.setSessionAttr(bpc.request, MEMBER_ID, null);
        }
        //filter task pool by status
        List<TaskDto> superPool = getSupervisorPoolByGroupWordId(workGroupIds, loginContext);
        if(!StringUtil.isEmpty(application_status)) {
            //Filter the Common Pool Task in another place
            if (!application_status.equals(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT)) {
                searchParam.addFilter("application_status", application_status, true);
                ParamUtil.setSessionAttr(bpc.request, COMMON_POOL_STATUS, null);
            } else {//Filter the Common Pool Task
                searchParam.addParam(COMMON_POOL_STATUS, COMMON_POOL_STATUS);
                ParamUtil.setSessionAttr(bpc.request, COMMON_POOL_STATUS, COMMON_POOL_STATUS);
            }
        } else {
            ParamUtil.setSessionAttr(bpc.request, APP_STATUS_OPTION, null);
        }
        int workGroupIdsSize = 0;
        if(!IaisCommonUtils.isEmpty(workGroupIds)) {
            workGroupIdsSize = workGroupIds.size();
            String workGroupId = SqlHelper.constructInCondition(WRK_GRP_ID, workGroupIdsSize);
            searchParam.addParam(WORKGROUP_LIST, workGroupId);
            for (int i = 0; i < workGroupIds.size(); i++) {
                searchParam.addFilter(WRK_GRP_ID + i, workGroupIds.get(i));
            }
        } else {
            String workGroupId = SqlHelper.constructInCondition(WRK_GRP_ID, workGroupIdsSize);
            searchParam.addParam(WORKGROUP_LIST, workGroupId);
        }
        if(loginContext != null && !StringUtil.isEmpty(roleId)) {
            List<String> roleIdList = new ArrayList<>(2);
            roleIdList.add(roleId);
            roleIdList.add(roleId.replace(RoleConsts.USER_LEAD, ""));
            String roleIdStr = SqlHelper.constructInCondition(ROLE_ID, roleIdList.size());
            searchParam.addParam(ROLEID_LIST, roleIdStr);
            for (int i = 0; i < roleIdList.size(); i++) {
                searchParam.addFilter(ROLE_ID + i, roleIdList.get(i));
            }
        } else {
            String roleIdStr = SqlHelper.constructInCondition(ROLE_ID, 0);
            searchParam.addParam(ROLEID_LIST, roleIdStr);
        }
        if(!StringUtil.isEmpty(application_no)){
            searchParam.addFilter("application_no", application_no,true);
        }
        if(!StringUtil.isEmpty(application_type)){
            searchParam.addFilter("application_type", application_type,true);
        }
        if(!StringUtil.isEmpty(hci_code)){
            searchParam.addFilter("hci_code", hci_code,true);
        }
        if(!StringUtil.isEmpty(hci_name)){
            searchParam.addFilter("hci_name", hci_name,true);
        }
        if(!StringUtil.isEmpty(hci_address)){
            ParamUtil.setSessionAttr(bpc.request, SUPER_POOL_HCIADDRESS, hci_address);
        } else {
            ParamUtil.setSessionAttr(bpc.request, SUPER_POOL_HCIADDRESS, null);
        }
        List<SelectOption> appStatusOption = inspectionService.getAppStatusOption(loginContext, AppConsts.SUPERVISOR_POOL);
        ParamUtil.setSessionAttr(bpc.request, APP_STATUS_OPTION, (Serializable) appStatusOption);
        ParamUtil.setSessionAttr(bpc.request, SUPER_POOL, (Serializable) superPool);
        ParamUtil.setSessionAttr(bpc.request, SUP_TASK_SEARCH_PARAM, searchParam);
        ParamUtil.setSessionAttr(bpc.request, GROUP_ROLE_FIELDDTO, groupRoleFieldDto);
        ParamUtil.setSessionAttr(bpc.request, WORK_GROUP_IDS, (Serializable) workGroupIds);
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
            return Collections.emptyList();
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
                    if (!StringUtil.isEmpty(tDto.getRoleId()) && (userIdsByGroup.contains(tDto.getUserId()) || StringUtil.isEmpty(tDto.getUserId())) && (
                            tDto.getRoleId().equals(curRole) || tDto.getRoleId().equals(memberRole))) {
                        taskDtoList.add(tDto);
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
        log.info(StringUtil.changeForLog("the inspectionSupSearchSort start ...."));
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
        log.info(StringUtil.changeForLog("the inspectionSupSearchPage start ...."));
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
        log.info(StringUtil.changeForLog("the inspectionSupSearchQuery1 start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        String hci_address = (String)ParamUtil.getSessionAttr(bpc.request, SUPER_POOL_HCIADDRESS);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        SearchResult<InspectionSubPoolQueryDto> searchResult;
        if(StringUtil.isEmpty(hci_address)) {
            QueryHelp.setMainSql(INSPECTION_QUERY, SUPERVISOR_POOLSEARCH, searchParam);
            searchResult = inspectionService.getSupPoolByParam(searchParam);
            ParamUtil.setSessionAttr(bpc.request, HCSA_TASK_ASSIGNDTO, null);
        } else {
            //copy SearchParam for searchAllParam
            SearchParam searchAllParam = (SearchParam) CopyUtil.copyMutableObject(searchParam);
            searchAllParam.setPageSize(-1);
            //get all appGroupIds
            QueryHelp.setMainSql(INSPECTION_QUERY, SUPERVISOR_POOLSEARCH,searchAllParam);
            searchResult = inspectionService.getSupPoolByParam(searchAllParam);
            //set all address data map for filter address
            List<String> appGroupIds = inspectionService.getSuperPoolAppGrpIdByResult(searchResult);
            HcsaTaskAssignDto hcsaTaskAssignDto = inspectionService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
            //filter unit no for group
            searchParam = inspectionAssignTaskService.setAppGrpIdsByUnitNos(searchParam, hci_address, hcsaTaskAssignDto, "T5.ID", "appGroup_list");
            QueryHelp.setMainSql(INSPECTION_QUERY, SUPERVISOR_POOLSEARCH, searchParam);
            searchResult = inspectionService.getSupPoolByParam(searchParam);
            ParamUtil.setSessionAttr(bpc.request, HCSA_TASK_ASSIGNDTO, hcsaTaskAssignDto);
        }
        searchResult = inspectionService.getGroupLeadName(searchResult, loginContext);

        ParamUtil.setSessionAttr(bpc.request, SUP_TASK_SEARCH_PARAM, searchParam);
        ParamUtil.setSessionAttr(bpc.request, SUP_TASK_SEARCH_RESULT, searchResult);
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
        SearchResult<InspectionSubPoolQueryDto> searchResult = (SearchResult<InspectionSubPoolQueryDto>)ParamUtil.getSessionAttr(bpc.request, SUP_TASK_SEARCH_RESULT);
        String taskId = "";
        try{
            taskId = ParamUtil.getMaskedString(bpc.request, "taskId");
        }catch (MaskAttackException e){
            log.error(e.getMessage(), e);
            try{
                IaisEGPHelper.redirectUrl(bpc.response, "https://"+bpc.request.getServerName()+"/hcsa-licence-web/CsrfErrorPage.jsp");
            } catch (IOException ioe){
                log.error(ioe.getMessage(), ioe);
                return;
            }
        }
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto)ParamUtil.getSessionAttr(bpc.request, INSPECTION_TASKPOOL_LISTDTO);
        if(!(StringUtil.isEmpty(taskId))){
            Map<String, SuperPoolTaskQueryDto> assignMap = (Map<String, SuperPoolTaskQueryDto>) ParamUtil.getSessionAttr(bpc.request, "assignMap");
            inspectionTaskPoolListDto = new InspectionTaskPoolListDto();
            inspectionTaskPoolListDto = inspectionService.getDataForAssignTask(assignMap, inspectionTaskPoolListDto, taskId);
            //get inspector Option
            inspectionTaskPoolListDto = inspectionService.inputInspectorOption(inspectionTaskPoolListDto, loginContext);
            if(inspectionTaskPoolListDto.getTaskDto() != null){
                String appPremCorrId = inspectionTaskPoolListDto.getTaskDto().getRefNo();
                ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(appPremCorrId);
                ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
                ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
                //set hours flag
                setEditHoursFlagByAppAndUser(inspectionTaskPoolListDto, applicationDto);
            }
            if(!(IaisCommonUtils.isEmpty(inspectionTaskPoolListDto.getInspectorOption()))){
                inspectionTaskPoolListDto.setInspectorFlag(AppConsts.TRUE);
            } else {
                inspectionTaskPoolListDto.setInspectorFlag(AppConsts.FALSE);
            }
        }

        ParamUtil.setSessionAttr(bpc.request, INSPECTION_TASKPOOL_LISTDTO, inspectionTaskPoolListDto);
        ParamUtil.setSessionAttr(bpc.request, SUP_TASK_SEARCH_RESULT, searchResult);
    }

    private InspectionTaskPoolListDto setEditHoursFlagByAppAndUser(InspectionTaskPoolListDto inspectionTaskPoolListDto, ApplicationDto applicationDto) {
        List<String> appHoursStatusList = IaisCommonUtils.genNewArrayList();
        appHoursStatusList.add(ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL);
        appHoursStatusList.add(ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT);
        appHoursStatusList.add(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT);
        String appStatus = "";
        if(applicationDto != null) {
            appStatus = applicationDto.getStatus();
        }
        if (StringUtil.isEmpty(inspectionTaskPoolListDto.getTaskDto().getUserId()) && appHoursStatusList.contains(appStatus)) {
            inspectionTaskPoolListDto.setEditHoursFlag(AppConsts.COMMON_POOL);
        }
        return inspectionTaskPoolListDto;
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
        GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto)ParamUtil.getSessionAttr(bpc.request, GROUP_ROLE_FIELDDTO);
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
            if("create".equals(propertyName) || AppConsts.COMMON_POOL.equals(propertyName)){
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
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.TRUE);
            }
        }else{
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        }
        ParamUtil.setSessionAttr(bpc.request,INSPECTION_TASKPOOL_LISTDTO, inspectionTaskPoolListDto);
    }

    public InspectionTaskPoolListDto getValueFromPage(BaseProcessClass bpc) {
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto)ParamUtil.getSessionAttr(bpc.request, INSPECTION_TASKPOOL_LISTDTO);
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
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto)ParamUtil.getSessionAttr(bpc.request, INSPECTION_TASKPOOL_LISTDTO);
        ParamUtil.setSessionAttr(bpc.request,INSPECTION_TASKPOOL_LISTDTO, inspectionTaskPoolListDto);
    }

    /**
     * StartStep: inspectionSupSearchConfirm
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchConfirm(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchConfirm start ...."));
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto)ParamUtil.getSessionAttr(bpc.request, INSPECTION_TASKPOOL_LISTDTO);
        ParamUtil.setSessionAttr(bpc.request,INSPECTION_TASKPOOL_LISTDTO, inspectionTaskPoolListDto);
    }

    /**
     * StartStep: InspectionInboxSearchQuery
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchSuccess start ...."));
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto)ParamUtil.getSessionAttr(bpc.request, INSPECTION_TASKPOOL_LISTDTO);
        List<TaskDto> superPool =(List<TaskDto>)ParamUtil.getSessionAttr(bpc.request, SUPER_POOL);
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        String saveFlag = inspectionService.routingTaskByPool(inspectionTaskPoolListDto, superPool, internalRemarks);
        if(AppConsts.FAIL.equals(saveFlag)){
            ParamUtil.setRequestAttr(bpc.request,"taskHasBeenAssigned", AppConsts.TRUE);
        }
        ParamUtil.setSessionAttr(bpc.request,INSPECTION_TASKPOOL_LISTDTO, inspectionTaskPoolListDto);
        ParamUtil.setSessionAttr(bpc.request, SUPER_POOL, (Serializable) superPool);
    }
}
