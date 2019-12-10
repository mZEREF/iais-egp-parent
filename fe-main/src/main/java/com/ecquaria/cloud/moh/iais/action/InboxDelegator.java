package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.InboxConst;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-11-06 09:46
 **/

@Delegator("inboxDelegator")
@Slf4j
public class InboxDelegator {


    @Autowired
    private InboxService inboxService;

    @Autowired
    private InboxDelegator(InboxService inboxService){
        this.inboxService = inboxService;

    }

    FilterParameter appParameter = new FilterParameter();
    FilterParameter inboxParameter = new FilterParameter();
    FilterParameter licenceParameter = new FilterParameter();

    /**
     *
     * @param bpc
     * @Decription Step ---> Start
     */
    public void startStep(BaseProcessClass bpc) throws IllegalAccessException{
        log.debug(StringUtil.changeForLog("Step ---> Start"));
        IaisEGPHelper.clearSessionAttr(bpc.request,InboxConst.class);
        ParamUtil.setSessionAttr(bpc.request,"TAB_NO", "inboxTab");
    }

    /**
     *
     * @param bpc
     * @Description Step ---> PrepareDate
     */
    public void prepareDataStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> PrepareDate"));
        HttpServletRequest request = bpc.request;
        prepareSelectOption(bpc);
        inboxParameter.setClz(InboxQueryDto.class);
        inboxParameter.setSearchAttr(InboxConst.INBOX_PARAM);
        inboxParameter.setResultAttr(InboxConst.INBOX_RESULT);
        inboxParameter.setSortField("id");
        SearchParam inboxParam = SearchResultHelper.getSearchParam(request, true,inboxParameter);
        QueryHelp.setMainSql("interInboxQuery","inboxQuery",inboxParam);
        SearchResult inboxResult = inboxService.inboxDoQuery(inboxParam);

        if(!StringUtil.isEmpty(inboxResult)){
            ParamUtil.setSessionAttr(request,InboxConst.INBOX_PARAM, inboxParam);
            ParamUtil.setRequestAttr(request,InboxConst.INBOX_RESULT, inboxResult);
            ParamUtil.setRequestAttr(request,"pageCount", inboxResult.getPageCount(inboxParam.getPageSize()));
        }
        /**
         * Application SearchResult
         */
        appParameter.setClz(InboxAppQueryDto.class);
        appParameter.setSearchAttr(InboxConst.APP_PARAM);
        appParameter.setResultAttr(InboxConst.APP_RESULT);
        appParameter.setSortField("id");
        SearchParam appParam = SearchResultHelper.getSearchParam(request, true,appParameter);
        QueryHelp.setMainSql("interInboxQuery","applicationQuery",appParam);
        SearchResult appResult = inboxService.appDoQuery(appParam);

        if(!StringUtil.isEmpty(appResult)){
            ParamUtil.setSessionAttr(request,InboxConst.APP_PARAM, appParam);
            ParamUtil.setRequestAttr(request,InboxConst.APP_RESULT, appResult);
            ParamUtil.setRequestAttr(request,"pageCount", appResult.getPageCount(appParam.getPageSize()));
        }
        /**
         * Licence SearchResult
         */
        licenceParameter.setClz(InboxLicenceQueryDto.class);
        licenceParameter.setSearchAttr(InboxConst.LIC_PARAM);
        licenceParameter.setResultAttr(InboxConst.LIC_RESULT);
        licenceParameter.setSortField("licence_no");
        licenceParameter.setSortType(InboxConst.DESCENDING);
        SearchParam licParam = SearchResultHelper.getSearchParam(request, true,licenceParameter);
        QueryHelp.setMainSql("interInboxQuery","licenceQuery",licParam);
        if (licParam != null) {
            SearchResult licResult = inboxService.licenceDoQuery(licParam);
            if(!StringUtil.isEmpty(licResult)){
                ParamUtil.setSessionAttr(request,InboxConst.LIC_PARAM, licParam);
                ParamUtil.setRequestAttr(request,InboxConst.LIC_RESULT, licResult);
            }
            ParamUtil.setRequestAttr(request,"pageCount", licResult.getPageCount(licParam.getPageSize()));
        }
    }

    /**
     * @param bpc
     * @Description Step ---> DoSearch
     */
    public void doSearch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> DoSearch"));
        HttpServletRequest request = bpc.request;
        String switchAction = ParamUtil.getString(request, InboxConst.SWITCH_ACTION);
        if(switchAction.equals(InboxConst.SEARCH_INBOX)){
            String inboxType = ParamUtil.getString(request,"inboxType");
            String inboxService = ParamUtil.getString(request,"inboxService");
            String licenceNo = ParamUtil.getString(request,"inboxAdvancedSearch");
            Map<String,Object> inboxSearchMap = new HashMap<>();
            if (inboxType != null || inboxService != null){
                ParamUtil.setRequestAttr(request,"TAB_NO", "inboxTab");
            }
            if(inboxType != null && !inboxType.equals("All")){
                inboxSearchMap.put("messageType",inboxType);
            }
            if(inboxService != null && !inboxService.equals("All")){
                inboxSearchMap.put("interService",inboxService);
            }
            inboxParameter.setFilters(inboxSearchMap);
            inboxParameter.setPageNo(1);

            String applicationType = ParamUtil.getString(request,"appType");
            String applicationStatus = ParamUtil.getString(request,"appStatus");
            log.debug(StringUtil.changeForLog("Step ---> applicationType") + applicationType);
            Map<String,Object> appSearchMap = new HashMap<>();
            if (applicationType != null || applicationStatus != null){
                ParamUtil.setRequestAttr(request,"TAB_NO", "appTab");
            }
            if(applicationType != null && !applicationType.equals("All")){
                appSearchMap.put("appType",applicationType);
            }
            if(applicationStatus != null && !applicationStatus.equals("All")){
                appSearchMap.put("appStatus",applicationStatus);
            }
            appParameter.setFilters(appSearchMap);
            appParameter.setPageNo(1);
        }
    }

    /**
     * @param bpc
     * @Description Step ---> DoSort
     */
    public void doSort(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> DoSort"));
        HttpServletRequest request = bpc.request;
        String switchAction = ParamUtil.getString(request, InboxConst.SWITCH_ACTION);
        log.debug(StringUtil.changeForLog("switchAction" + switchAction));
    }

    public void doPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> DoPage"));
        int pageNo = ParamUtil.getInt(bpc.request,InboxConst.CRUD_ACTION_VALUE);
        log.debug(StringUtil.changeForLog("PageNo ....") + pageNo);
        inboxParameter.setPageNo(pageNo);
    }
    /**
     *
     * @param bpc
     * @Description Step ---> Switch
     */
    public void prepareSwitchStep(BaseProcessClass bpc){
        log.debug("The prepareSwitch start ...");
        String switchAction = ParamUtil.getString(bpc.request, InboxConst.SWITCH_ACTION);
        log.debug("*******************crudAction-->:" + switchAction);
        log.debug("The prepareSwitch end ...");
    }

    /**
     *
     * @param bpc
     * @description Data to Form select part
     */
    private void prepareSelectOption(BaseProcessClass bpc){
        List<SelectOption> inboxServiceSelectList = new ArrayList<>();
        inboxServiceSelectList.add(new SelectOption("All", "All"));
        inboxServiceSelectList.add(new SelectOption("Blood Banking", "Blood Banking"));
        inboxServiceSelectList.add(new SelectOption("3", "Second"));
        ParamUtil.setRequestAttr(bpc.request, "inboxServiceSelect", inboxServiceSelectList);

        List<SelectOption> inboxTypSelectList = new ArrayList<>();
        inboxTypSelectList.add(new SelectOption("All", "All"));
        inboxTypSelectList.add(new SelectOption("Notification", "Notification"));
        inboxTypSelectList.add(new SelectOption("Announcement", "Announcement"));
        inboxTypSelectList.add(new SelectOption("Query", "Query"));
        ParamUtil.setRequestAttr(bpc.request, "inboxTypeSelect", inboxTypSelectList);

        List<SelectOption> applicationTypeSelectList = new ArrayList<>();
        applicationTypeSelectList.add(new SelectOption("All", "All"));
        applicationTypeSelectList.add(new SelectOption("APTY002", "APTY002"));
        applicationTypeSelectList.add(new SelectOption("New Licence", "New Licence"));
        applicationTypeSelectList.add(new SelectOption("Group", "Group"));
        ParamUtil.setRequestAttr(bpc.request, "appTypeSelect", applicationTypeSelectList);


        List<SelectOption> applicationStatusSelectList = new ArrayList<>();
        applicationStatusSelectList.add(new SelectOption("All", "All"));
        applicationStatusSelectList.add(new SelectOption("Approved", "Approved"));
        applicationStatusSelectList.add(new SelectOption("Pending", "Pending"));
        applicationStatusSelectList.add(new SelectOption("Draft", "Draft"));
        ParamUtil.setRequestAttr(bpc.request, "appStatusSelect", applicationStatusSelectList);

        List<SelectOption> selectApplicationSelectList = new ArrayList<>();
        selectApplicationSelectList.add(new SelectOption("Edit", "Edit"));
        selectApplicationSelectList.add(new SelectOption("Withdraw", "Withdraw"));
        selectApplicationSelectList.add(new SelectOption("Make Payment", "Make Payment"));
        ParamUtil.setRequestAttr(bpc.request, "selectApplication", selectApplicationSelectList);

    }
}