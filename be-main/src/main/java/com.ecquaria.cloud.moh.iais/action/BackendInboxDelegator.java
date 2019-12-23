package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppInGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
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
    InspectionService inspectionService;

    public void start(BaseProcessClass bpc){
        AccessUtil.initLoginUserInfo(bpc.request);
        List<SelectOption> selectOptionArrayList = new ArrayList<>();
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        for (String item : loginContext.getRoleIds()) {
            selectOptionArrayList.add(new SelectOption(item,item));
        }
        log.debug(StringUtil.changeForLog("the BackendInboxDelegator start ...."));
        String curRole = "";
        if(loginContext.getCurRoleId() == null || loginContext.getCurRoleId() == ""){
            curRole = "Please select";
        }else{
            curRole = loginContext.getCurRoleId();
        }

        ParamUtil.setSessionAttr(bpc.request, "curRole", curRole);

        ParamUtil.setRequestAttr(bpc.request, "roleIds", selectOptionArrayList);
        AuditTrailHelper.auditFunction("Inspection Sup Assign", "Sup Assign Task");

    }



    public void searchInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchInit start ...."));
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request,"inspectionTaskPoolListDto", null);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", null);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", null);
        ParamUtil.setSessionAttr(bpc.request, "workGroupIds", null);
        ParamUtil.setSessionAttr(bpc.request, "appTypeOption", null);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", null);
        ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
    }

    /**
     * StartStep: inspectionSupSearchPre
     *
     * @param bpc
     * @throws
     */
    public void prepareData(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchPre start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        SearchResult<InspectionSubPoolQueryDto> searchResult = (SearchResult) ParamUtil.getSessionAttr(bpc.request, "supTaskSearchResult");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);

        List<String> workGroupIds = inspectionService.getWorkGroupIdsByLogin(loginContext);
        List<SelectOption> appTypeOption = inspectionService.getAppTypeOption();
        List<SelectOption> appStatusOption = inspectionService.getAppStatusOption();

        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", searchResult);
        ParamUtil.setSessionAttr(bpc.request, "appTypeOption", (Serializable) appTypeOption);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
        ParamUtil.setSessionAttr(bpc.request, "workGroupIds", (Serializable) workGroupIds);

        String swithtype = (String)ParamUtil.getRequestAttr(bpc.request, "SearchSwitchType");
        if(swithtype == null || swithtype ==""){
            ParamUtil.setRequestAttr(bpc.request, "SearchSwitchType","search");
        }
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc,boolean isNew){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, "supTaskSearchParam");
        if(searchParam == null || isNew){
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
        SearchParam searchParam = getSearchParam(bpc);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);

        String application_no = ParamUtil.getString(bpc.request, "application_no");
        String application_type = ParamUtil.getString(bpc.request, "application_type");
        String application_status = ParamUtil.getString(bpc.request, "application_status");
        String hci_code = ParamUtil.getString(bpc.request, "hci_code");
        String hci_name = ParamUtil.getString(bpc.request, "hci_name");
        String hci_address = ParamUtil.getString(bpc.request, "hci_address");

        ParamUtil.setSessionAttr(bpc.request,"application_no", application_no);
        ParamUtil.setSessionAttr(bpc.request, "application_type", application_type);
        ParamUtil.setSessionAttr(bpc.request, "application_status", application_status);
        ParamUtil.setSessionAttr(bpc.request, "hci_code", hci_code);
        ParamUtil.setSessionAttr(bpc.request, "hci_name", hci_name);
        ParamUtil.setSessionAttr(bpc.request, "hci_address", hci_address);

        String inspectorValue = loginContext.getLoginId();
        String curRole = loginContext.getCurRoleId();
        List<TaskDto> commPools = getCommPoolBygetUserId(loginContext.getUserId(),curRole);

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
            searchParam.addFilter("hci_name", hci_name,true);
        }
        if(!StringUtil.isEmpty(hci_address)){
            searchParam.addFilter("blk_no", hci_address,true);
            searchParam.addFilter("floor_no", hci_address,true);
            searchParam.addFilter("unit_no", hci_address,true);
            searchParam.addFilter("street_name", hci_address,true);
            searchParam.addFilter("building_name", hci_address,true);
            searchParam.addFilter("postal_code", hci_address,true);
        }
        ParamUtil.setSessionAttr(bpc.request, "commPools", (Serializable) commPools);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", searchParam);
//        ParamUtil.setSessionAttr(bpc.request, "inspectorValue", inspectorValue);
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
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
        List<TaskDto> commPools = getCommPoolBygetUserId(loginContext.getUserId(),curRole);
        ParamUtil.setSessionAttr(bpc.request, "commPools", (Serializable) commPools);
        ParamUtil.setSessionAttr(bpc.request,"inspectionTaskPoolListDto", null);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", null);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", null);
        ParamUtil.setSessionAttr(bpc.request, "workGroupIds", null);
        ParamUtil.setSessionAttr(bpc.request, "appTypeOption", null);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", null);
        ParamUtil.setSessionAttr(bpc.request, "curRole", curRole);
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
     * StartStep: inspectionSupSearchSort
     *
     * @param bpc
     * @throws
     */
    public void searchSort(BaseProcessClass bpc){
        }


    /**
     * StartStep: inspectionSupSearchQuery1
     *
     * @param bpc
     * @throws
     */
    public void searchQuery(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchQuery1 start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        List<TaskDto> commPools =(List<TaskDto>)ParamUtil.getSessionAttr(bpc.request, "commPools");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);

        QueryHelp.setMainSql("inspectionQuery", "assignInspectorSupper",searchParam);
        SearchResult<InspectionSubPoolQueryDto> searchResult = inspectionService.getSupPoolByParam(searchParam);
        SearchResult<InspectionTaskPoolListDto> searchResult2 = inspectionService.getOtherDataForSr(searchResult, commPools, loginContext);

        SearchParam searchParamGroup = new SearchParam(InspectionAppGroupQueryDto.class.getName());
        searchParamGroup.setPageSize(10);
        searchParamGroup.setPageNo(1);
        searchParamGroup.setSort("SUBMIT_DT", SearchParam.ASCENDING);

        SearchParam searchParamAjax = new SearchParam(InspectionAppInGroupQueryDto.class.getName());
        searchParamAjax.setPageSize(10);
        searchParamAjax.setPageNo(1);
        searchParamAjax.setSort("ID", SearchParam.ASCENDING);

        if(searchResult2 != null && searchResult2.getRowCount() > 0){
            List<InspectionTaskPoolListDto> rows = searchResult2.getRows();
            String inspectorValue = loginContext.getLoginId();
            int size = 0;
            for (InspectionTaskPoolListDto item:rows
            ) {
                if(inspectorValue.equals(item.getInspectorName())){
                    size++;
                }
            }
            String appNoStr = SqlHelper.constructInCondition("T1.APPLICATION_NO",size);
            searchParamGroup.addParam("applicationNo_list",appNoStr);
            Integer i =0;

            for (InspectionTaskPoolListDto item:rows
                 ) {
                if(inspectorValue.equals(item.getInspectorName())){
                    searchParamGroup.addFilter("T1.APPLICATION_NO"+i,
                            item.getApplicationNo());
                    i ++;
                }
            }

            String application_no = (String)ParamUtil.getSessionAttr(bpc.request, "application_no");
            String application_type = (String)ParamUtil.getSessionAttr(bpc.request, "application_type");
            String hci_code = (String)ParamUtil.getSessionAttr(bpc.request, "hci_code");
            String application_status = (String)ParamUtil.getSessionAttr(bpc.request, "application_status");
            String hci_name = (String)ParamUtil.getSessionAttr(bpc.request, "hci_name");
            String hci_address = (String)ParamUtil.getSessionAttr(bpc.request, "hci_address");
            if(!StringUtil.isEmpty(application_no)){
                searchParamGroup.addFilter("application_no", application_no,true);
            }

            if(!StringUtil.isEmpty(application_type)){
                searchParamGroup.addFilter("application_type", application_type,true);
            }
            if(!StringUtil.isEmpty(hci_code)){
                searchParamGroup.addFilter("hci_code", hci_code,true);
            }
            if(!StringUtil.isEmpty(application_status)){
                searchParamGroup.addFilter("application_status", application_status,true);
            }
            if(!StringUtil.isEmpty(hci_name)){
                searchParamGroup.addFilter("hci_name", hci_name,true);
            }
            if(!StringUtil.isEmpty(hci_address)){
                searchParamGroup.addFilter("blk_no", hci_address,true);
                searchParamGroup.addFilter("floor_no", hci_address,true);
                searchParamGroup.addFilter("unit_no", hci_address,true);
                searchParamGroup.addFilter("street_name", hci_address,true);
                searchParamGroup.addFilter("building_name", hci_address,true);
                searchParamGroup.addFilter("postal_code", hci_address,true);
            }


            searchParamAjax.addParam("applicationNo_list",appNoStr);
            Integer ajaxi =0;
            for (InspectionTaskPoolListDto item:rows
            ) {
                if(inspectorValue.equals(item.getInspectorName())){
                searchParamAjax.addFilter("T1.APPLICATION_NO"+ajaxi,
                        item.getApplicationNo());
                ajaxi ++;
                }
            }

            if(!StringUtil.isEmpty(application_no)){
                searchParamAjax.addFilter("application_no", application_no,true);
            }
            if(!StringUtil.isEmpty(application_type)){
                searchParamAjax.addFilter("application_type", application_type,true);
            }
            if(!StringUtil.isEmpty(application_status)){
                searchParamAjax.addFilter("application_status", application_status,true);
            }
            if(!StringUtil.isEmpty(hci_code)){
                searchParamAjax.addFilter("hci_code", hci_code,true);
            }
            if(!StringUtil.isEmpty(hci_name)){
                searchParamAjax.addFilter("hci_name", hci_name,true);
            }
            if(!StringUtil.isEmpty(hci_address)){
                searchParamAjax.addFilter("blk_no", hci_address,true);
                searchParamAjax.addFilter("floor_no", hci_address,true);
                searchParamAjax.addFilter("unit_no", hci_address,true);
                searchParamAjax.addFilter("street_name", hci_address,true);
                searchParamAjax.addFilter("building_name", hci_address,true);
                searchParamAjax.addFilter("postal_code", hci_address,true);
            }

            QueryHelp.setMainSql("inspectionQuery", "AppGroup",searchParamGroup);
            SearchResult<InspectionAppGroupQueryDto> searchResult3 = inspectionService.searchInspectionBeAppGroup(searchParamGroup);
            ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", searchResult3);
            ParamUtil.setSessionAttr(bpc.request, "searchParamAjax", searchParamAjax);
        }else{
            ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", null);
        }

        Map<String,String> appNoUrl = new HashMap<>();
        if(commPools != null && commPools.size() >0){
            for (TaskDto item:commPools
            ) {
                appNoUrl.put(item.getRefNo(), generateProcessUrl(item, bpc.request));
            }
        }



        ParamUtil.setSessionAttr(bpc.request, "appNoUrl",(Serializable) appNoUrl);
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
