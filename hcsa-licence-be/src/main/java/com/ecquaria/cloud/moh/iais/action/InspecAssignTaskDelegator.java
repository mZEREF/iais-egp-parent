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
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Process: MohInspectionAllotTaskInspector
 *
 * @author Shicheng
 * @date 2019/11/22 10:16
 **/
@Delegator("inspecAssignTaskDelegator")
@Slf4j
public class InspecAssignTaskDelegator {

    @Autowired
    private InspectionAssignTaskService inspectionAssignTaskService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private InspectionService inspectionService;

    @Autowired
    private InspecAssignTaskDelegator(ApplicationViewService applicationViewService, InspectionAssignTaskService inspectionAssignTaskService){
        this.inspectionAssignTaskService = inspectionAssignTaskService;
        this.applicationViewService = applicationViewService;
    }

    /**
     * StartStep: inspectionAllotTaskInspectorStart
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorStart start ...."));
        log.info(StringUtil.changeForLog("Step 1 ==============>" + bpc.request.getSession().getId()));
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_LOAD_LEVELING, AuditTrailConsts.FUNCTION_COMMON_POOL);
    }

    /**
     * StartStep: inspectionAllotTaskInspectorInit
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorInit start ...."));
        ParamUtil.setSessionAttr(bpc.request,"inspectionTaskPoolListDtoList", null);
        ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", null);
        ParamUtil.setSessionAttr(bpc.request, "cPoolSearchParam", null);
        ParamUtil.setSessionAttr(bpc.request, "cPoolSearchResult", null);
        ParamUtil.setSessionAttr(bpc.request, "commPools", null);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", null);
        ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", null);
        ParamUtil.setSessionAttr(bpc.request, "sub_date2", null);
        ParamUtil.setSessionAttr(bpc.request, "poolRoleCheckDto", null);
        ParamUtil.setSessionAttr(bpc.request, "commonPoolRoleIds", null);
        ParamUtil.setSessionAttr(bpc.request, "comPoolHciAddress", null);
        ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", null);
    }

    /**
     * StartStep: inspectionAllotTaskInspectorPre
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorPre start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        SearchResult<InspectionCommonPoolQueryDto> searchResult = (SearchResult) ParamUtil.getSessionAttr(bpc.request, "cPoolSearchResult");
        if(searchResult == null){
            LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            PoolRoleCheckDto poolRoleCheckDto = new PoolRoleCheckDto();
            poolRoleCheckDto = inspectionAssignTaskService.getRoleOptionByKindPool(loginContext, AppConsts.COMMON_POOL, poolRoleCheckDto);
            List<SelectOption> commonPoolRoleIds = poolRoleCheckDto.getRoleOptions();
            GroupRoleFieldDto groupRoleFieldDto = inspectionAssignTaskService.getGroupRoleField(loginContext);
            //get task by user workGroupId
            if (loginContext == null) {
                throw new IaisRuntimeException("Login context Null");
            }
            List<TaskDto> commPools = inspectionAssignTaskService.getCommPoolByGroupWordId(loginContext);
            List<String> workGroupIds = new ArrayList<>(loginContext.getWrkGrpIds());
            int workGroupIdsSize = 0;
            if(!IaisCommonUtils.isEmpty(workGroupIds)) {
                workGroupIdsSize = workGroupIds.size();
                String workGroupId = SqlHelper.constructInCondition("T7.WRK_GRP_ID", workGroupIdsSize);
                searchParam.addParam("workGroup_list", workGroupId);
                for (int i = 0; i < workGroupIds.size(); i++) {
                    searchParam.addFilter("T7.WRK_GRP_ID" + i, workGroupIds.get(i));
                }
            } else {
                String workGroupId = SqlHelper.constructInCondition("T7.WRK_GRP_ID", workGroupIdsSize);
                searchParam.addParam("workGroup_list", workGroupId);
            }
            //set role filter
            String curRoleId;
            if(loginContext != null && !StringUtil.isEmpty(loginContext.getCurRoleId())){
                curRoleId = loginContext.getCurRoleId();
            } else {
                curRoleId = RoleConsts.USER_LEAD;
            }
            searchParam.addFilter("commonPoolRoleId", curRoleId,true);

            QueryHelp.setMainSql("inspectionQuery", "assignCommonTask",searchParam);
            searchResult = inspectionAssignTaskService.getSearchResultByParam(searchParam);
            searchResult = inspectionAssignTaskService.getAddressByResult(searchResult);
            //set all address data map for filter address
            List<String> appGroupIds = inspectionAssignTaskService.getComPoolAppGrpIdByResult(searchResult);
            HcsaTaskAssignDto hcsaTaskAssignDto = inspectionService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);

            ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", hcsaTaskAssignDto);
            ParamUtil.setSessionAttr(bpc.request, "commPools", (Serializable) commPools);
            ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", groupRoleFieldDto);
            ParamUtil.setSessionAttr(bpc.request, "commonPoolRoleIds", (Serializable) commonPoolRoleIds);
            ParamUtil.setSessionAttr(bpc.request, "poolRoleCheckDto", poolRoleCheckDto);
            ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
        }
        List<SelectOption> appTypeOption = inspectionAssignTaskService.getAppTypeOption();

        ParamUtil.setSessionAttr(bpc.request, "cPoolSearchResult", searchResult);
        ParamUtil.setSessionAttr(bpc.request, "cPoolSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "appTypeOption", (Serializable) appTypeOption);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc,boolean isNew){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, "cPoolSearchParam");
        int pageSize = SystemParamUtil.getDefaultPageSize();
        if(searchParam == null || isNew){
            searchParam = new SearchParam(InspectionCommonPoolQueryDto.class.getName());
            searchParam.setPageSize(pageSize);
            searchParam.setPageNo(1);
            searchParam.setSort("GROUP_NO", SearchParam.ASCENDING);
        }
        return searchParam;
    }
    /**
     * StartStep: inspectionAllotTaskInspectorStep1
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorStep1(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorStep1 start ...."));
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorStep1 end ...."));
    }

    /**
     * StartStep: inspectionAllotTaskInspectorAssign
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorAssign(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorAssign start ...."));
        InspecTaskCreAndAssDto inspecTaskCreAndAssDto = (InspecTaskCreAndAssDto)ParamUtil.getSessionAttr(bpc.request, "inspecTaskCreAndAssDto");
        HcsaTaskAssignDto hcsaTaskAssignDto = (HcsaTaskAssignDto)ParamUtil.getSessionAttr(bpc.request, "hcsaTaskAssignDto");
        SearchResult<InspectionCommonPoolQueryDto> searchResult = (SearchResult<InspectionCommonPoolQueryDto>) ParamUtil.getSessionAttr(bpc.request, "cPoolSearchResult");
        String appCorrelationId = "";
        try{
            appCorrelationId = ParamUtil.getMaskedString(bpc.request,"appCorrelationId");
        }catch (MaskAttackException e){
            log.error(e.getMessage(), e);
            try{
                IaisEGPHelper.redirectUrl(bpc.response, "https://"+bpc.request.getServerName()+"/hcsa-licence-web/CsrfErrorPage.jsp");
            } catch (IOException ioe){
                log.error(ioe.getMessage(), ioe);
                return;
            }
        }
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if(!StringUtil.isEmpty(appCorrelationId) && !(AppConsts.NO.equals(appCorrelationId))){
            ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(appCorrelationId);
            List<TaskDto> commPools = (List<TaskDto>)ParamUtil.getSessionAttr(bpc.request, "commPools");
            Map<String, String> map = (Map<String, String>)ParamUtil.getSessionAttr(bpc.request, "appCorrIdTaskIdMap");
            ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", null);
            String taskId = map.get(appCorrelationId);
            inspecTaskCreAndAssDto = new InspecTaskCreAndAssDto();
            inspecTaskCreAndAssDto.setTaskId(taskId);
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            if(applicationDto.isFastTracking()){
                inspecTaskCreAndAssDto.setFastTrackCheck(AppConsts.TRUE);
            }
            //set fastTrackFlag
            inspecTaskCreAndAssDto = setFastTrackFlag(inspecTaskCreAndAssDto, applicationDto);
            inspecTaskCreAndAssDto = inspectionAssignTaskService.getInspecTaskCreAndAssDto(appCorrelationId, commPools, loginContext, inspecTaskCreAndAssDto, hcsaTaskAssignDto);
            //set Edit Hours Flag
            inspecTaskCreAndAssDto = setEditHoursFlagByAppAndUser(inspecTaskCreAndAssDto, applicationDto);
            ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", inspecTaskCreAndAssDto);
            ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        }

        ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", inspecTaskCreAndAssDto);
        ParamUtil.setSessionAttr(bpc.request,"cPoolSearchResult", searchResult);
    }

    private InspecTaskCreAndAssDto setEditHoursFlagByAppAndUser(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, ApplicationDto applicationDto) {
        List<String> appHoursStatusList = IaisCommonUtils.genNewArrayList();
        appHoursStatusList.add(ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL);
        appHoursStatusList.add(ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT);
        appHoursStatusList.add(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT);
        String appStatus = "";
        if(applicationDto != null) {
            appStatus = applicationDto.getStatus();
        }
        if(appHoursStatusList.contains(appStatus)) {
            inspecTaskCreAndAssDto.setEditHoursFlag(AppConsts.COMMON_POOL);
        }
        return inspecTaskCreAndAssDto;
    }

    private InspecTaskCreAndAssDto setFastTrackFlag(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, ApplicationDto applicationDto) {
        boolean fastTrackFlag = false;
        String appStatus = applicationDto.getStatus();
        if(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(appStatus)){
            fastTrackFlag = true;
        }
        if(applicationDto.isFastTracking()){
            inspecTaskCreAndAssDto.setFastTrackCheckFlag(true);
        } else {
            inspecTaskCreAndAssDto.setFastTrackCheckFlag(false);
        }
        inspecTaskCreAndAssDto.setFastTrackFlag(fastTrackFlag);
        return inspecTaskCreAndAssDto;
    }

    /**
     * StartStep: inspectionAllotTaskInspectorAction
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorAction(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorAction start ...."));
        InspecTaskCreAndAssDto inspecTaskCreAndAssDto = (InspecTaskCreAndAssDto)ParamUtil.getSessionAttr(bpc.request, "inspecTaskCreAndAssDto");
        SearchResult<InspectionCommonPoolQueryDto> searchResult = (SearchResult) ParamUtil.getSessionAttr(bpc.request, "cPoolSearchResult");
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if(InspectionConstants.SWITCH_ACTION_CONFIRM.equals(actionValue)){
            inspecTaskCreAndAssDto = getValueFromPage(bpc);
            if(RoleConsts.USER_ROLE_INSPECTION_LEAD.equals(loginContext.getCurRoleId()) || RoleConsts.USER_ROLE_INSPECTIOR.equals(loginContext.getCurRoleId())){
                ValidationResult validationResult = WebValidationHelper.validateProperty(inspecTaskCreAndAssDto, AppConsts.COMMON_POOL);
                if (validationResult.isHasErrors()) {
                    Map<String, String> errorMap = validationResult.retrieveAll();
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                    WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                    ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
                } else {
                    ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.TRUE);
                }
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        } else if(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue)){
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        } else {
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.FALSE);
        }
        ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", inspecTaskCreAndAssDto);
        ParamUtil.setSessionAttr(bpc.request, "cPoolSearchResult", searchResult);
    }

    public InspecTaskCreAndAssDto getValueFromPage(BaseProcessClass bpc) {
        InspecTaskCreAndAssDto inspecTaskCreAndAssDto = (InspecTaskCreAndAssDto)ParamUtil.getSessionAttr(bpc.request, "inspecTaskCreAndAssDto");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if(RoleConsts.USER_ROLE_INSPECTION_LEAD.equals(loginContext.getCurRoleId()) || RoleConsts.USER_ROLE_INSPECTIOR.equals(loginContext.getCurRoleId())){
            String inspManHours = ParamUtil.getRequestString(bpc.request, "inspManHours");
            inspecTaskCreAndAssDto.setInspManHours(inspManHours);
        }
        String[] fastTrackCommon = ParamUtil.getStrings(bpc.request, "fastTrackCommon");
        if(fastTrackCommon != null && fastTrackCommon.length > 0){
            inspecTaskCreAndAssDto.setFastTrackCheck(AppConsts.TRUE);
        } else {
            inspecTaskCreAndAssDto.setFastTrackCheck(null);
        }
        SelectOption so = new SelectOption(loginContext.getUserId(), "text");
        List<SelectOption> inspectorCheckList = IaisCommonUtils.genNewArrayList();
        inspectorCheckList.add(so);
        inspecTaskCreAndAssDto.setInspectorCheck(inspectorCheckList);
        return inspecTaskCreAndAssDto;
    }

    /**
     * StartStep: inspectionAllotTaskInspectorQuery
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorQuery(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorQuery start ...."));
        SearchResult<InspectionCommonPoolQueryDto> searchResult = (SearchResult) ParamUtil.getSessionAttr(bpc.request, "cPoolSearchResult");
        ParamUtil.setSessionAttr(bpc.request,"cPoolSearchResult", searchResult);
    }

    /**
     * StartStep: mohComPoolMulAssign
     *
     * @param bpc
     * @throws
     */
    public void mohComPoolMulAssign(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohComPoolMulAssign start ...."));
        SearchResult<InspectionCommonPoolQueryDto> searchResult = (SearchResult) ParamUtil.getSessionAttr(bpc.request, "cPoolSearchResult");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<TaskDto> commPools = (List<TaskDto>)ParamUtil.getSessionAttr(bpc.request, "commPools");
        String[] appNoChecks = ParamUtil.getStrings(bpc.request,"comPoolMulCheck");
        String comPoolAck = inspectionAssignTaskService.assignMultTaskByAppNos(appNoChecks, loginContext, commPools);
        if(!StringUtil.isEmpty(comPoolAck)){
            ParamUtil.setRequestAttr(bpc.request,"taskHasBeenAssigned", AppConsts.TRUE);
        }
        ParamUtil.setSessionAttr(bpc.request,"cPoolSearchResult", searchResult);
    }

    /**
     * StartStep: inspectionAllotTaskInspectorSuccess
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorSuccess start ...."));
        InspecTaskCreAndAssDto inspecTaskCreAndAssDto = (InspecTaskCreAndAssDto)ParamUtil.getSessionAttr(bpc.request, "inspecTaskCreAndAssDto");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<TaskDto> commPools = (List<TaskDto>)ParamUtil.getSessionAttr(bpc.request, "commPools");
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        String saveFlag = inspectionAssignTaskService.routingTaskByCommonPool(commPools, inspecTaskCreAndAssDto, internalRemarks, loginContext);
        if(AppConsts.FAIL.equals(saveFlag)){
            ParamUtil.setRequestAttr(bpc.request,"taskHasBeenAssigned", AppConsts.TRUE);
        }
        ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", inspecTaskCreAndAssDto);
    }


    /**
     * StartStep: inspectionAllotTaskInspectorConfirm
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorConfirm(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorConfirm start ...."));
        InspecTaskCreAndAssDto inspecTaskCreAndAssDto = (InspecTaskCreAndAssDto)ParamUtil.getSessionAttr(bpc.request, "inspecTaskCreAndAssDto");
        ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", inspecTaskCreAndAssDto);
    }

    /**
     * StartStep: inspectionAllotTaskInspectorSearch
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorSearch(BaseProcessClass bpc) throws ParseException {
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorSearch start ...."));
        SearchParam searchParam = getSearchParam(bpc, true);
        PoolRoleCheckDto poolRoleCheckDto = (PoolRoleCheckDto)ParamUtil.getSessionAttr(bpc.request, "poolRoleCheckDto");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String application_no = ParamUtil.getRequestString(bpc.request, "application_no");
        String application_type = ParamUtil.getRequestString(bpc.request, "application_type");
        String hci_code = ParamUtil.getRequestString(bpc.request, "hci_code");
        String hci_name = ParamUtil.getRequestString(bpc.request, "hci_name");
        String sub_date2 = ParamUtil.getRequestString(bpc.request, "sub_date");
        String hci_address = ParamUtil.getRequestString(bpc.request, "hci_address");
        String roleIdCheck = ParamUtil.getRequestString(bpc.request, "commonRoleId");
        Map<String, String> roleMap = poolRoleCheckDto.getRoleMap();
        String roleId = getCheckRoleIdByMap(roleIdCheck, roleMap);
        if (loginContext == null) {
            throw new IaisRuntimeException("Login context Null");
        }
        loginContext.setCurRoleId(roleId);
        if(!StringUtil.isEmpty(roleId)){
            poolRoleCheckDto.setCheckCurRole(roleIdCheck);
            ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
        }
        List<TaskDto> commPools = inspectionAssignTaskService.getCommPoolByGroupWordId(loginContext);
        GroupRoleFieldDto groupRoleFieldDto = inspectionAssignTaskService.getGroupRoleField(loginContext);
        List<String> workGroupIds = new ArrayList<>(loginContext.getWrkGrpIds());
        int workGroupIdsSize = 0;
        if(!IaisCommonUtils.isEmpty(workGroupIds)) {
            workGroupIdsSize = workGroupIds.size();
            String workGroupId = SqlHelper.constructInCondition("T7.WRK_GRP_ID", workGroupIdsSize);
            searchParam.addParam("workGroup_list", workGroupId);
            for (int i = 0; i < workGroupIds.size(); i++) {
                searchParam.addFilter("T7.WRK_GRP_ID" + i, workGroupIds.get(i));
            }
        } else {
            String workGroupId = SqlHelper.constructInCondition("T7.WRK_GRP_ID", workGroupIdsSize);
            searchParam.addParam("workGroup_list", workGroupId);
        }
        String curRoleId;
        if(loginContext != null && !StringUtil.isEmpty(loginContext.getCurRoleId())){
            curRoleId = loginContext.getCurRoleId();
        } else {
            curRoleId = RoleConsts.USER_LEAD;
        }
        searchParam.addFilter("commonPoolRoleId", curRoleId,true);
        if(!StringUtil.isEmpty(application_no)){
            searchParam.addFilter("application_no",application_no,true);
        }
        if(!StringUtil.isEmpty(application_type)){
            searchParam.addFilter("application_type",application_type,true);
        }
        if(!StringUtil.isEmpty(hci_code)){
            searchParam.addFilter("hci_code",hci_code,true);
        }
        if(!StringUtil.isEmpty(hci_name)){
            searchParam.addFilter("hci_name",hci_name,true);
        }
        if(!StringUtil.isEmpty(sub_date2)){
            SimpleDateFormat sdf = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT);
            Date sub_date1 = sdf.parse(sub_date2);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            String sub_date = sdf2.format(sub_date1);
            searchParam.addFilter("sub_date",sub_date,true);
        }
        if(!StringUtil.isEmpty(hci_address)){
            ParamUtil.setSessionAttr(bpc.request, "comPoolHciAddress", hci_address);
        } else {
            ParamUtil.setSessionAttr(bpc.request, "comPoolHciAddress", null);
        }
        ParamUtil.setSessionAttr(bpc.request, "cPoolSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "sub_date2", sub_date2);
        ParamUtil.setSessionAttr(bpc.request, "commPools", (Serializable) commPools);
        ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", groupRoleFieldDto);
        ParamUtil.setSessionAttr(bpc.request, "poolRoleCheckDto", poolRoleCheckDto);
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

    /**
     * StartStep: inspectionAllotTaskInspectorSort
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorSort(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorSort start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doSorting(searchParam, bpc.request);
    }

    /**
     * StartStep: inspectionAllotTaskInspectorPage
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorPage start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    /**
     * StartStep: inspectionAllotTaskInspectorQuery2
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorQuery2(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorQuery2 start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        //address for second search
        String hci_address = (String)ParamUtil.getSessionAttr(bpc.request, "comPoolHciAddress");
        SearchResult<InspectionCommonPoolQueryDto> searchResult;
        if(StringUtil.isEmpty(hci_address)) {
            QueryHelp.setMainSql("inspectionQuery", "assignCommonTask",searchParam);
            searchResult = inspectionAssignTaskService.getSearchResultByParam(searchParam);
            ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", null);
        } else {
            //copy SearchParam for searchAllParam
            SearchParam searchAllParam = (SearchParam) CopyUtil.copyMutableObject(searchParam);
            searchAllParam.setPageSize(-1);
            //get all appGroupIds
            QueryHelp.setMainSql("inspectionQuery", "assignCommonTask",searchAllParam);
            searchResult = inspectionAssignTaskService.getSearchResultByParam(searchAllParam);
            //set all address data map for filter address
            List<String> appGroupIds = inspectionAssignTaskService.getComPoolAppGrpIdByResult(searchResult);
            HcsaTaskAssignDto hcsaTaskAssignDto = inspectionService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
            //filter unit no for group
            searchParam = inspectionAssignTaskService.setAppGrpIdsByUnitNos(searchParam, hci_address, hcsaTaskAssignDto, "T5.ID", "appGroup_list");
            QueryHelp.setMainSql("inspectionQuery", "assignCommonTask",searchParam);
            searchResult = inspectionAssignTaskService.getSearchResultByParam(searchParam);
            ParamUtil.setSessionAttr(bpc.request, "hcsaTaskAssignDto", hcsaTaskAssignDto);
        }
        searchResult = inspectionAssignTaskService.getAddressByResult(searchResult);

        ParamUtil.setSessionAttr(bpc.request, "cPoolSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "cPoolSearchResult", searchResult);
    }
}
