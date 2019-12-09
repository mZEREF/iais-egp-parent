package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.HashMap;
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
    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;

    @Autowired
    private InspecAssignTaskDelegator(InspectionAssignTaskService inspectionAssignTaskService, ApplicationViewService applicationViewService, AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient){
        this.inspectionAssignTaskService = inspectionAssignTaskService;
        this.appPremisesRoutingHistoryClient = appPremisesRoutingHistoryClient;
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
        log.info("Step 1 ==============>" + bpc.request.getSession().getId());
        AuditTrailHelper.auditFunction("Inspection Assign", "Assign Task");
    }

    /**
     * StartStep: inspectionAllotTaskInspectorInit
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorInit start ...."));
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request,"inspectionTaskPoolListDtoList", null);
        ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", null);
        ParamUtil.setSessionAttr(bpc.request, "cPoolSearchParam", null);
        ParamUtil.setSessionAttr(bpc.request, "cPoolSearchResult", null);
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
        if(searchParam == null || isNew){
            searchParam = new SearchParam(InspectionCommonPoolQueryDto.class.getName());
            searchParam.setPageSize(10);
            searchParam.setPageNo(1);
            searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
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
        String applicationNo = ParamUtil.getMaskedString(bpc.request,"applicationNo");
        if(!StringUtil.isEmpty(applicationNo) && !(AppConsts.NO.equals(applicationNo))){
            List<TaskDto> commPools = (List<TaskDto>)ParamUtil.getSessionAttr(bpc.request, "commPools");
            Map<String, String> map = (Map<String, String>)ParamUtil.getSessionAttr(bpc.request, "appNoTaskIdMap");
            ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", null);
            LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            String taskId = map.get(applicationNo);
            inspecTaskCreAndAssDto = inspectionAssignTaskService.getInspecTaskCreAndAssDto(applicationNo, commPools, loginContext);
            inspecTaskCreAndAssDto.setTaskId(taskId);
            ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", inspecTaskCreAndAssDto);
        }
        ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", inspecTaskCreAndAssDto);
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
        String[] nameValue = ParamUtil.getStrings(bpc.request,"inspector");
        if(nameValue == null || nameValue.length < 0) {
            inspecTaskCreAndAssDto.setInspectorCheck(null);
        } else {
            List<SelectOption> inspectorCheckList = inspectionAssignTaskService.getCheckInspector(nameValue, inspecTaskCreAndAssDto);
            inspecTaskCreAndAssDto.setInspectorCheck(inspectorCheckList);
        }
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        if(!(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue))){
            ValidationResult validationResult = WebValidationHelper.validateProperty(inspecTaskCreAndAssDto,"create");
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
        ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", inspecTaskCreAndAssDto);
    }

    /**
     * StartStep: inspectionAllotTaskInspectorQuery
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorQuery(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorQuery start ...."));
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
        List<TaskDto> commPools = (List<TaskDto>)ParamUtil.getSessionAttr(bpc.request, "commPools");
        inspectionAssignTaskService.assignTaskForInspectors(commPools, inspecTaskCreAndAssDto);
        TaskDto taskDto = getTaskDtoByPool(commPools, inspecTaskCreAndAssDto);
        ApplicationViewDto applicationViewDto = inspectionAssignTaskService.searchByAppNo(inspecTaskCreAndAssDto.getApplicationNo());
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto.getStatus(),taskDto.getTaskKey(),internalRemarks);
        ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING);
        applicationViewDto.setApplicationDto(applicationDto1);
        createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto.getStatus(),HcsaConsts.ROUTING_STAGE_INS,null);
        ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", inspecTaskCreAndAssDto);
    }

    private TaskDto getTaskDtoByPool(List<TaskDto> commPools, InspecTaskCreAndAssDto inspecTaskCreAndAssDto) {
        TaskDto taskDto = new TaskDto();
        for(TaskDto tDto:commPools){
            if(tDto.getId().equals(inspecTaskCreAndAssDto.getTaskId())){
                taskDto = tDto;
            }
        }
        return taskDto;
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
    public void inspectionAllotTaskInspectorSearch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorSearch start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String application_no = ParamUtil.getRequestString(bpc.request, "application_no");
        String application_type = ParamUtil.getRequestString(bpc.request, "application_type");
        String hci_code = ParamUtil.getRequestString(bpc.request, "hci_code");
        String hci_name = ParamUtil.getRequestString(bpc.request, "hci_name");
        String sub_date = ParamUtil.getRequestString(bpc.request, "sub_date");
        List<TaskDto> commPools = inspectionAssignTaskService.getCommPoolByGroupWordId(loginContext);
        setMapTaskId(bpc, commPools);
        String[] applicationNo_list = inspectionAssignTaskService.getApplicationNoListByPool(commPools);
        if(applicationNo_list == null || applicationNo_list.length == 0){
            applicationNo_list = new String[]{SystemParameterConstants.PARAM_FALSE};
        }
        String applicationStr = SqlHelper.constructInCondition("T1.APPLICATION_NO",applicationNo_list.length);
        searchParam.addParam("applicationNo_list",applicationStr);
        for (int i = 0 ; i<applicationNo_list.length; i++ ) {
            searchParam.addFilter("T1.APPLICATION_NO"+i,applicationNo_list[i]);
        }
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
        if(!StringUtil.isEmpty(sub_date)){
            searchParam.addFilter("sub_date",sub_date,true);
        }
        ParamUtil.setSessionAttr(bpc.request, "cPoolSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "commPools", (Serializable) commPools);
    }

    private void setMapTaskId(BaseProcessClass bpc, List<TaskDto> commPools) {
        Map<String, String> appNoTaskIdMap = new HashMap<>();
        if(commPools != null || commPools.size() > 0){
            for(TaskDto td:commPools){
                appNoTaskIdMap.put(td.getRefNo(), td.getId());
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "appNoTaskIdMap", (Serializable) appNoTaskIdMap);
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
        QueryHelp.setMainSql("inspectionQuery", "assignInspector",searchParam);
        SearchResult<InspectionCommonPoolQueryDto> searchResult = inspectionAssignTaskService.getSearchResultByParam(searchParam);
        searchResult = inspectionAssignTaskService.getAddressByResult(searchResult);
        ParamUtil.setSessionAttr(bpc.request, "cPoolSearchParam", searchParam);
        ParamUtil.setRequestAttr(bpc.request, "cPoolSearchResult", searchResult);
    }
}
