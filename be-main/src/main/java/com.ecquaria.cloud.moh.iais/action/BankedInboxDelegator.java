package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * BankedInboxDelegator
 *
 * @author junyu
 * @date 2019/12/12
 */
@Delegator("bankedInboxDelegator")
@Slf4j
public class BankedInboxDelegator {
    @Autowired
    InspectionService inspectionService;

    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>inbox");

    }



    public void searchInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchInit start ...."));
        //AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request,"inspectionTaskPoolListDto", null);
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
    public void prepareData(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchPre start ...."));
        SearchParam searchParam = (SearchParam)ParamUtil.getSessionAttr(bpc.request, "supTaskSearchParam");
        SearchResult<InspecTaskCreAndAssDto> searchResult = (SearchResult) ParamUtil.getSessionAttr(bpc.request, "supTaskSearchResult");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        //List<String> workGroupIds = inspectionService.getWorkGroupIdsByLogin(loginContext);
        List<String> workGroupIds =new ArrayList<>();
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
        SearchParam searchParam = (SearchParam)ParamUtil.getSessionAttr(bpc.request, "supTaskSearchParam");

        List<String> workGroupIds = (List<String>)ParamUtil.getSessionAttr(bpc.request, "workGroupIds");
        String application_no = ParamUtil.getRequestString(bpc.request, "application_no");
        String application_type = ParamUtil.getRequestString(bpc.request, "application_type");
        String application_status = ParamUtil.getRequestString(bpc.request, "application_status");
        String hci_code = ParamUtil.getRequestString(bpc.request, "hci_code");
        String hci_name = ParamUtil.getRequestString(bpc.request, "hci_name");
        String hci_address = ParamUtil.getRequestString(bpc.request, "hci_address");
        String inspectorValue = ParamUtil.getMaskedString(bpc.request, "inspector_name");

        List<TaskDto> commPools = getCommPoolByGroupWordId(workGroupIds);

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
        } else {
            appNoStrs = new String[]{SystemParameterConstants.PARAM_FALSE};
        }
        String appNoStr = SqlHelper.constructInCondition("T5.APPLICATION_NO", appNoStrs.length);
        searchParam.addParam("appNo_list",appNoStr);
        for (int i = 0; i < appNoStrs.length; i++) {
            searchParam.addFilter("T5.APPLICATION_NO"+i, appNoStrs[i]);
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
        ParamUtil.setSessionAttr(bpc.request, "inspectorValue", inspectorValue);
    }

    private List<TaskDto> getCommPoolByGroupWordId(List<String> workGroupIds) {
        List<TaskDto> taskDtoList = new ArrayList<>();
        if(workGroupIds == null || workGroupIds.size() <= 0){
            return null;
        }
        for(String workGrpId:workGroupIds){
            for(TaskDto tDto:inspectionService.getSupervisorPoolByGroupWordId(workGrpId)){
                taskDtoList.add(tDto);
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
    public void searchSort(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchSort start ...."));
    }

    /**
     * StartStep: inspectionSupSearchPage
     *
     * @param bpc
     * @throws
     */
    public void searchPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchPage start ...."));
        SearchParam searchParam = (SearchParam)ParamUtil.getSessionAttr(bpc.request, "SupTaskSearchParam");
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    /**
     * StartStep: inspectionSupSearchQuery1
     *
     * @param bpc
     * @throws
     */
    public void searchQuery(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchQuery1 start ...."));
        SearchParam searchParam = (SearchParam)ParamUtil.getSessionAttr(bpc.request, "supTaskSearchParam");
        List<TaskDto> commPools =(List<TaskDto>)ParamUtil.getSessionAttr(bpc.request, "commPools");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        QueryHelp.setMainSql("inspectionQuery", "assignInspectorSupper",searchParam);
        SearchResult<InspectionSubPoolQueryDto> searchResult = inspectionService.getSupPoolByParam(searchParam);
        SearchResult<InspectionTaskPoolListDto> searchResult2 = inspectionService.getOtherDataForSr(searchResult, commPools, loginContext);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", searchResult2);
    }










}
