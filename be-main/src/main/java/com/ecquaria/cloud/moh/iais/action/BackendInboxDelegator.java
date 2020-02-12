package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppInGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewMainService;
import com.ecquaria.cloud.moh.iais.service.BroadcastMainService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BankedInboxDelegator
 *
 * @author junyu
 * @date 2019/12/12
 */
@Delegator("backendInboxDelegator")
@Slf4j
public class BackendInboxDelegator {
    @Autowired
    InspectionMainService inspectionService;

    @Autowired
    TaskService taskService;

    @Autowired
    ApplicationViewMainService applicationViewService;

    @Autowired
    private BroadcastMainService broadcastService;

    private String application_no;
    private String application_type;
    private String application_status;
    private String hci_code;
    private String hci_name;
    private String hci_address;
    private SearchParam searchParam;
    private List<TaskDto> commPools;
    public void start(BaseProcessClass bpc){
        AccessUtil.initLoginUserInfo(bpc.request);
        List<SelectOption> selectOptionArrayList = new ArrayList<>();
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        for (String item : loginContext.getRoleIds()) {
            selectOptionArrayList.add(new SelectOption(item,item));
        }
        log.debug(StringUtil.changeForLog("the BackendInboxDelegator start ...."));
        String curRole = "";
        if(StringUtil.isEmpty(loginContext.getCurRoleId())){
            curRole = "Please select";
        }else{
            curRole = loginContext.getCurRoleId();
        }

        ParamUtil.setSessionAttr(bpc.request, "searchParamAjax",null);
        ParamUtil.setSessionAttr(bpc.request, "taskList",null);
        ParamUtil.setSessionAttr(bpc.request, "hastaskList",null);
        ParamUtil.setSessionAttr(bpc.request, "appNoUrl",null);
        ParamUtil.setSessionAttr(bpc.request, "taskMap",null);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", null);
        ParamUtil.setSessionAttr(bpc.request, "roleIds", (Serializable) selectOptionArrayList);
        AuditTrailHelper.auditFunction("Inspection Sup Assign", "Sup Assign Task");

    }



    public void searchInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchInit start ...."));
        AccessUtil.initLoginUserInfo(bpc.request);
        application_no = "";
        application_type = "";
        application_status = "";
        hci_code = "";
        hci_name = "";
        hci_address = "";
        ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
        searchParam = getSearchParam(bpc,true);
    }

    /**
     * StartStep: inspectionSupSearchPre
     *
     * @param bpc
     * @throws
     */
    public void prepareData(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchPre start ...."));
        searchParam = getSearchParam(bpc);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        commPools = getCommPoolBygetUserId(loginContext.getUserId(),loginContext.getCurRoleId());
        List<String> workGroupIds = inspectionService.getWorkGroupIdsByLogin(loginContext);
        List<SelectOption> appTypeOption = inspectionService.getAppTypeOption();
        List<SelectOption> appStatusOption = inspectionService.getAppStatusOption();

        ParamUtil.setRequestAttr(bpc.request,"hci_code",hci_code);
        ParamUtil.setRequestAttr(bpc.request,"curRole",loginContext.getCurRoleId());
        ParamUtil.setRequestAttr(bpc.request,"hci_name",hci_name);
        ParamUtil.setRequestAttr(bpc.request,"blk_no",hci_address);
        ParamUtil.setRequestAttr(bpc.request,"application_no",application_no);
        ParamUtil.setRequestAttr(bpc.request, "supTaskSearchParam", searchParam);
        ParamUtil.setRequestAttr(bpc.request, "appTypeOption", (Serializable) appTypeOption);
        ParamUtil.setRequestAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
        ParamUtil.setRequestAttr(bpc.request, "workGroupIds", (Serializable) workGroupIds);
        ParamUtil.setRequestAttr(bpc.request, "curRole", loginContext.getCurRoleId());
        String swithtype = (String)ParamUtil.getRequestAttr(bpc.request, "SearchSwitchType");
        if(swithtype == null || swithtype ==""){
            ParamUtil.setRequestAttr(bpc.request, "SearchSwitchType","search");
        }
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc,boolean isNew){
        if(searchParam == null || isNew){
            searchParam = new SearchParam(InspectionSubPoolQueryDto.class.getName());
            searchParam.setPageSize(10);
            searchParam.setPageNo(1);
            searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
        }else{
            String pageSizeString = ParamUtil.getRequestString(bpc.request, "pageJumpNoPageSize");
            String pageNoString = ParamUtil.getRequestString(bpc.request, "pageJumpNoTextchangePage");
            int pageSize = 10;
            int pageNo = 1;
            if(pageSizeString != null){
                pageSize = Integer.parseInt(pageSizeString);
            }
            if(pageNoString != null){
                pageNo = Integer.parseInt(pageNoString);
            }
            searchParam.setPageSize(pageSize);
            searchParam.setPageNo(pageNo);
        }
        return searchParam;
    }

    /**
     * StartStep: inspectionSupSearchStartStep1
     *
     * @param bpc
     * @throws
     */
    public void searchStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchStartStep1 start ...."));

    }

    /**
     * StartStep: inspectionSupSearchDoSearch
     *
     * @param bpc
     * @throws
     */
    public void doSearch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchDoSearch start ...."));
        searchParam = getSearchParam(bpc);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        application_no = ParamUtil.getString(bpc.request, "application_no");
        application_type = ParamUtil.getString(bpc.request, "application_type");
        application_status = ParamUtil.getString(bpc.request, "application_status");
        hci_code = ParamUtil.getString(bpc.request, "hci_code");
        hci_name = ParamUtil.getString(bpc.request, "hci_name");
        hci_address = ParamUtil.getString(bpc.request, "hci_name");

        String inspectorValue = loginContext.getLoginId();


        String[] applicationNo_list = inspectionService.getApplicationNoListByPool(commPools);
        if(applicationNo_list == null || applicationNo_list.length == 0){
            applicationNo_list = new String[]{SystemParameterConstants.PARAM_FALSE};
        }
        String applicationStr = SqlHelper.constructInCondition("T1.APPLICATION_NO",applicationNo_list.length);
        searchParam.addParam("applicationNo_list", applicationStr);
        for (int i = 0; i<applicationNo_list.length; i++ ) {
            searchParam.addFilter("T1.APPLICATION_NO"+i, applicationNo_list[i]);
        }

        if(!StringUtil.isEmpty(application_no)){
            searchParam.addFilter("application_no", application_no,true);
        }
        String[] appNoStrs;
        if(!(StringUtil.isEmpty(inspectorValue))) {
            appNoStrs = inspectorValue.split(",");
            String appNoStr = SqlHelper.constructInCondition("T5.APPLICATION_NO", appNoStrs.length);
            searchParam.addParam("appNo_list",appNoStr);
            for (int i = 0; i < appNoStrs.length; i++) {
                searchParam.addFilter("T5.APPLICATION_NO"+i, appNoStrs[i]);
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
            searchParam.addFilter("hci_name", "%" +hci_name +"%",true);
        }
        if(!StringUtil.isEmpty(hci_address)){
            searchParam.addFilter("address", "%" +hci_address +"%",true);
        }
    }

    /**
     * StartStep: inspectionSupSearchsearchPage
     *
     * @param bpc
     * @throws
     */
    public void searchPage(BaseProcessClass bpc){
        String curRole = ParamUtil.getRequestString(bpc.request, "roleIds");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        loginContext.setCurRoleId(curRole);
        commPools = getCommPoolBygetUserId(loginContext.getUserId(),loginContext.getCurRoleId());
    }

    private List<TaskDto> getCommPoolBygetUserId(String getUserId, String curRole) {
        List<TaskDto> taskDtoList = new ArrayList<>();
        if(getUserId == null){
            return null;
        }
        if(curRole == null){
            return null;
        }
        for(TaskDto tDto:inspectionService.getTasksByUserIdAndRole(getUserId,curRole)){
            taskDtoList.add(tDto);
        }
        return taskDtoList;
    }

    /**
     * StartStep: searchSort
     *
     * @param bpc
     * @throws
     */
    public void searchSort(BaseProcessClass bpc){
        }


    /**
     * StartStep: doApprove
     *
     * @param bpc
     * @throws
     */
    public void doApprove(BaseProcessClass bpc)  throws FeignException, CloneNotSupportedException {
        String[] taskList =  ParamUtil.getStrings(bpc.request, "taskcheckbox");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if(!StringUtil.isEmpty(taskList)){
            for (String item:taskList
            ) {
                TaskDto taskDto = taskService.getTaskById(item);
                String correlationId = taskDto.getRefNo();
                AppPremisesCorrelationDto appPremisesCorrelationDto = applicationViewService.getLastAppPremisesCorrelationDtoById(correlationId);
                if(appPremisesCorrelationDto!=null){
                    correlationId =  appPremisesCorrelationDto.getId();
                    taskDto.setRefNo(correlationId);
                }
                ApplicationViewDto applicationViewDto = applicationViewService.searchByCorrelationIdo(correlationId);

                if(RoleConsts.USER_ROLE_AO1.equals(loginContext.getCurRoleId())){
                    log.debug(StringUtil.changeForLog("the do rontingTaskToAO2 start ...."));
                    routingTask(bpc, HcsaConsts.ROUTING_STAGE_AO2, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02, RoleConsts.USER_ROLE_AO2,applicationViewDto,taskDto);
                    log.debug(StringUtil.changeForLog("the do rontingTaskToAO2 end ...."));
                }else if(RoleConsts.USER_ROLE_AO2.equals(loginContext.getCurRoleId())){
                    log.debug(StringUtil.changeForLog("the do rontingTaskToAO3 start ...."));
                    routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO3,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03,RoleConsts.USER_ROLE_AO3,applicationViewDto,taskDto);
                    log.debug(StringUtil.changeForLog("the do rontingTaskToAO3 end ...."));
                }else if(RoleConsts.USER_ROLE_AO3.equals(loginContext.getCurRoleId())){
                    log.debug(StringUtil.changeForLog("the do approve start ...."));
                    routingTask(bpc,null,ApplicationConsts.APPLICATION_STATUS_APPROVED,null,applicationViewDto,taskDto);
                    log.debug(StringUtil.changeForLog("the do approve end ...."));
                }
            }
            //update commPools
            commPools = getCommPoolBygetUserId(loginContext.getUserId(),loginContext.getCurRoleId());
        }

    }

    private void routingTask(BaseProcessClass bpc,String stageId,String appStatus,String roleId ,ApplicationViewDto applicationViewDto,TaskDto taskDto) throws FeignException, CloneNotSupportedException {

        //get the user for this applicationNo
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
        BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();

        //complated this task and create the history
        broadcastOrganizationDto.setRollBackComplateTask((TaskDto) CopyUtil.copyMutableObject(taskDto));
        taskDto =  completedTask(taskDto);
        broadcastOrganizationDto.setComplateTask(taskDto);
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        String processDecision = ParamUtil.getString(bpc.request,"nextStage");
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(taskDto.getRefNo(),
                applicationDto.getStatus(),taskDto.getTaskKey(), taskDto.getWkGrpId(),internalRemarks,processDecision,taskDto.getRoleId());
        broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
        //update application status
        broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto) CopyUtil.copyMutableObject(applicationDto));
        String oldStatus = applicationDto.getStatus();
        applicationDto.setStatus(appStatus);

        broadcastApplicationDto.setApplicationDto(applicationDto);

        if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(appStatus)){
            List<ApplicationDto> applicationDtoList = applicationViewService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());
            boolean isAllSubmit = applicationViewService.isOtherApplicaitonSubmit(applicationDtoList,applicationDto.getId(),
                    ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03);
            if(isAllSubmit){
                // send the task to Ao3
                TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(applicationDtoList,
                        HcsaConsts.ROUTING_STAGE_AO3,roleId,IaisEGPHelper.getCurrentAuditTrailDto());
                List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
                List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
                broadcastOrganizationDto.setOneSubmitTaskList(taskDtos);
                broadcastApplicationDto.setOneSubmitTaskHistoryList(appPremisesRoutingHistoryDtos);
            }
        }else{
            //setCreateTask
            TaskDto newTaskDto = taskService.getRoutingTask(applicationDto,stageId,roleId,taskDto.getRefNo());
            broadcastOrganizationDto.setCreateTask(newTaskDto);
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(taskDto.getRefNo(),applicationDto.getStatus(),stageId,
                    taskDto.getWkGrpId(),null,null,roleId);
            broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
        }

        //save the broadcast
        broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastApplicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        String eventRefNo = EventBusHelper.getEventRefNo();
        broadcastOrganizationDto.setEventRefNo(eventRefNo);
        broadcastApplicationDto.setEventRefNo(eventRefNo);
        broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,bpc.process,true);
        broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,bpc.process,true);
    }

    private TaskDto completedTask(TaskDto taskDto){
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setSlaRemainInDays(remainDays(taskDto));
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return taskDto;
    }

    private int remainDays(TaskDto taskDto){
        int result = 0;
        //todo: wait count kpi
        // String  resultStr = DurationFormatUtils.formatPeriod(taskDto.getDateAssigned().getTime(),taskDto.getSlaDateCompleted().getTime(), "d");
        // log.debug(StringUtil.changeForLog("The resultStr is -->:")+resultStr);
        return  result;
    }

    private AppPremisesRoutingHistoryDto getAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus,
                                                                      String stageId,String wrkGrpId, String internalRemarks,String processDecision,
                                                                      String roleId){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setProcessDecision(processDecision);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setWrkGrpId(wrkGrpId);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return appPremisesRoutingHistoryDto;
    }

    /**
     * StartStep: inspectionSupSearchQuery1
     *
     * @param bpc
     * @throws
     */
    public void searchQuery(BaseProcessClass bpc) throws ParseException {
        log.debug(StringUtil.changeForLog("the inspectionSupSearchQuery1 start ...."));
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);

        SearchParam searchParamGroup = new SearchParam(InspectionAppGroupQueryDto.class.getName());
        searchParamGroup.setPageSize(10);
        searchParamGroup.setPageNo(1);

        searchParamGroup.setSort("SUBMIT_DT", SearchParam.ASCENDING);

        SearchParam searchParamAjax = new SearchParam(InspectionAppInGroupQueryDto.class.getName());
        searchParamAjax.setPageSize(100);
        searchParamAjax.setPageNo(1);
        searchParamAjax.setSort("APPLICATION_NO", SearchParam.ASCENDING);

        if(commPools != null && !commPools.isEmpty()){
            String inspectorValue = loginContext.getLoginId();
            StringBuilder sb = new StringBuilder("(");
            int i =0;
            for (TaskDto item: commPools) {
                sb.append(":itemKey" + i).append(",");
                i++;
            }
            String inSql = sb.substring(0, sb.length() - 1) + ")";
            searchParamGroup.addParam("remises_corr_id_in", inSql);
            searchParamAjax.addParam("remises_corr_id_in", inSql);
            i = 0;
            for (TaskDto item: commPools) {
                searchParamGroup.addFilter("itemKey" + i,
                        item.getRefNo());
                searchParamAjax.addFilter("itemKey" + i,
                        item.getRefNo());
                i ++;
            }

            if(!StringUtil.isEmpty(application_no)){
                searchParamGroup.addFilter("application_no", application_no,true);
                searchParamAjax.addFilter("application_no", application_no,true);
            }else{
                searchParamGroup.removeFilter("application_no");
                searchParamAjax.removeFilter("application_no");
            }

            if(!StringUtil.isEmpty(application_type)){
                searchParamGroup.addFilter("application_type", application_type,true);
                searchParamAjax.addFilter("application_type", application_type,true);
            }else{
                searchParamGroup.removeFilter("application_type");
                searchParamAjax.removeFilter("application_type");
            }
            if(!StringUtil.isEmpty(hci_code)){
                searchParamGroup.addFilter("hci_code", hci_code,true);
                searchParamAjax.addFilter("hci_code", hci_code,true);
            }else{
                searchParamGroup.removeFilter("hci_code");
                searchParamAjax.removeFilter("hci_code");
            }
            if(!StringUtil.isEmpty(application_status)){
                searchParamGroup.addFilter("application_status", application_status,true);
                searchParamAjax.addFilter("application_status", application_status,true);
            }else{
                searchParamGroup.removeFilter("application_status");
                searchParamAjax.removeFilter("application_status");
            }
            if(!StringUtil.isEmpty(hci_name)){
                searchParamGroup.addFilter("hci_name", "%" +hci_name +"%",true);
                searchParamAjax.addFilter("hci_name", "%" +hci_name +"%",true);
            }else{
                searchParamGroup.removeFilter("hci_name");
                searchParamAjax.removeFilter("hci_name");
            }

            if(!StringUtil.isEmpty(hci_address)){
                searchParamGroup.addFilter("address", "%" +hci_address +"%",true);
                searchParamAjax.addFilter("address", "%" +hci_address +"%",true);
            }else{
                searchParamGroup.removeFilter("address");
                searchParamAjax.removeFilter("address");
            }

            CrudHelper.doPaging(searchParamGroup,bpc.request);
            QueryHelp.setMainSql("inspectionQuery", "AppGroup",searchParamGroup);
            log.debug(StringUtil.changeForLog("searchResult3 searchParamGroup = "+JsonUtil.parseToJson(searchParamGroup)));
            SearchResult<InspectionAppGroupQueryDto> searchResult3 = inspectionService.searchInspectionBeAppGroup(searchParamGroup);
            List<InspectionAppGroupQueryDto> inspectionAppGroupQueryDtoList = searchResult3.getRows();
            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat newformat =  new SimpleDateFormat("dd/MM/yyyy");
            for (InspectionAppGroupQueryDto item:inspectionAppGroupQueryDtoList
                 ) {
                long lt = format.parse(item.getSubmitDate()).getTime();
                Date date = new Date(lt);
                String newdate = newformat.format(date);
                item.setSubmitDate(newdate);
                if(item.getPaymentstatus() == null || item.getPaymentstatus().isEmpty()){
                    item.setPaymentstatus("N/A");
                }else{
                    item.setPaymentstatus(MasterCodeUtil.getCodeDesc(item.getPaymentstatus()));
                }

                item.setApplicationType(MasterCodeUtil.getCodeDesc(item.getApplicationType()));
            }

            ParamUtil.setRequestAttr(bpc.request, "supTaskSearchResult", searchResult3);
            ParamUtil.setSessionAttr(bpc.request, "searchParamAjax", searchParamAjax);
        }else{
            ParamUtil.setRequestAttr(bpc.request, "supTaskSearchResult", null);
        }
        ParamUtil.setRequestAttr(bpc.request,"curRole",loginContext.getCurRoleId());
        Map<String,String> appNoUrl = new HashMap<>();

        Map<String,TaskDto> taskMap = new HashMap<>();


            Map<String,String> taskList = new HashMap<>();
            if(commPools != null && commPools.size() >0){
                for (TaskDto item:commPools
                ) {
                    appNoUrl.put(item.getRefNo(), generateProcessUrl(item, bpc.request));
                    taskList.put(item.getRefNo(), item.getId());
                    taskMap.put(item.getRefNo(), item);
                }
            }
        if(RoleConsts.USER_ROLE_AO1.equals(loginContext.getCurRoleId()) || RoleConsts.USER_ROLE_AO2.equals(loginContext.getCurRoleId()) || RoleConsts.USER_ROLE_AO3.equals(loginContext.getCurRoleId())){
            ParamUtil.setSessionAttr(bpc.request, "taskList",(Serializable) taskList);
            ParamUtil.setSessionAttr(bpc.request, "hastaskList",AppConsts.TRUE);
        }else{
            ParamUtil.setSessionAttr(bpc.request, "hastaskList",AppConsts.FALSE);
        }

        ParamUtil.setSessionAttr(bpc.request, "appNoUrl",(Serializable) appNoUrl);
        ParamUtil.setSessionAttr(bpc.request, "taskMap",(Serializable) taskMap);
        ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.TRUE);

    }

    private String generateProcessUrl(TaskDto dto, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder("https://");
        String url = dto.getProcessUrl();
        sb.append(request.getServerName());
        if (!url.startsWith("/")) {
            sb.append("/");
        }
        sb.append(url);
        if (url.indexOf("?") >= 0) {
            sb.append("&");
        } else {
            sb.append("?");
        }
        sb.append("taskId=").append(dto.getId());

        return RedirectUtil.changeUrlToCsrfGuardUrlUrl(sb.toString(), request);
    }







}
