package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private InspecAssignTaskDelegator(InspectionAssignTaskService inspectionAssignTaskService){
        this.inspectionAssignTaskService = inspectionAssignTaskService;
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
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if(!StringUtil.isEmpty(applicationNo) && !(AppConsts.NO.equals(applicationNo))){
            List<TaskDto> commPools = (List<TaskDto>)ParamUtil.getSessionAttr(bpc.request, "commPools");
            Map<String, String> map = (Map<String, String>)ParamUtil.getSessionAttr(bpc.request, "appNoTaskIdMap");
            ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", null);
            String taskId = map.get(applicationNo);
            inspecTaskCreAndAssDto = inspectionAssignTaskService.getInspecTaskCreAndAssDto(applicationNo, commPools, loginContext);
            inspecTaskCreAndAssDto.setTaskId(taskId);
            ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", inspecTaskCreAndAssDto);
        }
        if(loginContext.getRoleIds().contains(RoleConsts.USER_ROLE_INSPECTIOR)){
            ParamUtil.setRequestAttr(bpc.request,"isInspector",AppConsts.TRUE);
        } else {
            ParamUtil.setRequestAttr(bpc.request,"isInspector",AppConsts.FALSE);
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
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if(!(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue))){
            if(loginContext.getRoleIds().contains(RoleConsts.USER_ROLE_INSPECTIOR)){
                inspecTaskCreAndAssDto = getValueFromPage(bpc);
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        } else {
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.FALSE);
        }
        ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", inspecTaskCreAndAssDto);
    }

    public InspecTaskCreAndAssDto getValueFromPage(BaseProcessClass bpc) {
        InspecTaskCreAndAssDto inspecTaskCreAndAssDto = (InspecTaskCreAndAssDto)ParamUtil.getSessionAttr(bpc.request, "inspecTaskCreAndAssDto");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String[] nameValue = new String[]{loginContext.getUserId()};
        if(nameValue == null || nameValue.length <= 0) {
            inspecTaskCreAndAssDto.setInspectorCheck(null);
        } else {
            List<SelectOption> inspectorCheckList = inspectionAssignTaskService.getCheckInspector(nameValue, inspecTaskCreAndAssDto);
            inspecTaskCreAndAssDto.setInspectorCheck(inspectorCheckList);
        }
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
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        inspectionAssignTaskService.routingTaskByCommonPool(commPools, inspecTaskCreAndAssDto, internalRemarks);
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
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String application_no = ParamUtil.getRequestString(bpc.request, "application_no");
        String application_type = ParamUtil.getRequestString(bpc.request, "application_type");
        String hci_code = ParamUtil.getRequestString(bpc.request, "hci_code");
        String hci_name = ParamUtil.getRequestString(bpc.request, "hci_name");
        String sub_date2 = ParamUtil.getRequestString(bpc.request, "sub_date");
        String hci_address = ParamUtil.getRequestString(bpc.request, "hci_address");
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
        if(!StringUtil.isEmpty(sub_date2)){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date sub_date1 = sdf.parse(sub_date2);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            String sub_date = sdf2.format(sub_date1);
            searchParam.addFilter("sub_date",sub_date,true);
        }
        if(!StringUtil.isEmpty(hci_address)){
            searchParam.addFilter("blk_no", hci_address,true);
            searchParam.addFilter("floor_no", hci_address,true);
            searchParam.addFilter("unit_no", hci_address,true);
            searchParam.addFilter("street_name", hci_address,true);
            searchParam.addFilter("building_name", hci_address,true);
            searchParam.addFilter("postal_code", hci_address,true);
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
