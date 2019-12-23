package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
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
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-11-06 09:46
 **/

@Slf4j
@Delegator("inboxDelegator")
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
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.MESSAGE_QUERY_KEY,inboxParam);
        SearchResult inboxResult = inboxService.inboxDoQuery(inboxParam);
        List<InboxQueryDto> inboxQueryDtoList = inboxResult.getRows();
        for (InboxQueryDto inboxQueryDto:inboxQueryDtoList
             ) {
//            String msgType = MasterCodeUtil.getCodeDesc(inboxQueryDto.getMessageType());
            String serviceName = inboxService.getServiceNameById(inboxQueryDto.getServiceId());
            inboxQueryDto.setProcessUrl(RedirectUtil.changeUrlToCsrfGuardUrlUrl(inboxQueryDto.getProcessUrl(), request));
//            inboxQueryDto.setMessageType(msgType);
            inboxQueryDto.setServiceId(serviceName);
        }
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
        appParameter.setSortField("CREATED_DT");
        SearchParam appParam = SearchResultHelper.getSearchParam(request, true,appParameter);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.APPLICATION_QUERY_KEY,appParam);
        SearchResult appResult = inboxService.appDoQuery(appParam);
        List<InboxAppQueryDto> inboxAppQueryDtoList = appResult.getRows();
        for (InboxAppQueryDto appDto:inboxAppQueryDtoList
             ) {
            String appType = MasterCodeUtil.getCodeDesc(appDto.getApplicationType());
            String appStatus = MasterCodeUtil.getCodeDesc(appDto.getStatus());
            String serviceName = "N.A.";
            if (appDto.getServiceId() != null){
                serviceName = inboxService.getServiceNameById(appDto.getServiceId());
            }
            appDto.setApplicationType(appType);
            appDto.setServiceId(serviceName);
            appDto.setStatus(appStatus);
        }

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
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.LICENCE_QUERY_KEY,licParam);
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

            /**
             * MESSAGE
             */
            String inboxType = ParamUtil.getString(request,InboxConst.MESSAGE_TYPE);
            String inboxService = ParamUtil.getString(request,InboxConst.MESSAGE_SERVICE);
            String msgSubject = ParamUtil.getString(request,InboxConst.MESSAGE_SEARCH);
            Map<String,Object> inboxSearchMap = new HashMap<>();

            if (inboxType != null || inboxService != null || msgSubject != null){
                ParamUtil.setRequestAttr(request,"TAB_NO", "inboxTab");
            }
            if(inboxType != null && !inboxType.equals(InboxConst.SEARCH_ALL)){
                inboxSearchMap.put("messageType",inboxType);
            }
            if(inboxService != null && !inboxService.equals(InboxConst.SEARCH_ALL)){
                inboxSearchMap.put("interService",inboxService);
            }
            if(msgSubject != null){
                inboxSearchMap.put("msgSubject",msgSubject);
            }
            inboxParameter.setFilters(inboxSearchMap);
            inboxParameter.setPageNo(1);

            /**
             * APPLICATION
             */
            String applicationType = ParamUtil.getString(request,"appType");
            String applicationStatus = ParamUtil.getString(request,"appStatus");
            String applicationNo = ParamUtil.getString(request,"applicationAdvancedSearch");
            log.debug(StringUtil.changeForLog("Step ---> applicationNo" + applicationNo));
            log.debug(StringUtil.changeForLog("Step ---> applicationType") + applicationType);
            Map<String,Object> appSearchMap = new HashMap<>();
            if (applicationType != null || applicationStatus != null || applicationNo != null){
                ParamUtil.setRequestAttr(request,"TAB_NO", "appTab");
            }
            if(applicationType != null && !applicationType.equals(InboxConst.SEARCH_ALL)){
                appSearchMap.put("appType",applicationType);
            }
            if(applicationStatus != null && !applicationStatus.equals(InboxConst.SEARCH_ALL)){

                appSearchMap.put("appStatus",applicationStatus);
            }
            if(applicationNo != null){
                appSearchMap.put("appNo",applicationNo);
            }
            appParameter.setFilters(appSearchMap);
            appParameter.setPageNo(1);

            /**
             * LICENCE
             */
            Map<String,Object> licSearchMap = new HashMap<>();
            String licenceNo = ParamUtil.getString(request,"licenseAdvancedSearch");
            if (licenceNo != null){
                ParamUtil.setRequestAttr(request,"TAB_NO", "licTab");
            }
            if(licenceNo != null){
                licSearchMap.put("licNo",licenceNo);
            }
            licenceParameter.setFilters(licSearchMap);
            licenceParameter.setPageNo(1);
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
        log.debug("***crudAction***:" + switchAction);
        log.debug("The prepareSwitch end ...");
    }

    /**
     * @param bpc
     * @Description Step ---> DoSort
     */
    public void doDraft(BaseProcessClass bpc) throws IOException {
        log.debug("The prepareEdit start ...");
        HttpServletRequest request = bpc.request;
        String appNo = ParamUtil.getString(request, "app_action_type");
        String action = ParamUtil.getString(request, InboxConst.CRUD_ACTION_VALUE);
        log.debug("The prepareEdit start---"+action+"--"+appNo);
//        String draftNo = inboxService.getDraftNumber(appNo);
        StringBuffer url = new StringBuffer();
        url.append("https://").append(bpc.request.getServerName())
                .append("/hcsa-licence-web/eservice/INTERNET/MohNewApplication")
                .append("?DraftNumber=")
                .append(appNo);
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
        bpc.response.sendRedirect(tokenUrl);
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
        inboxServiceSelectList.add(new SelectOption("Clinical Laboratory", "Clinical Laboratory"));
        inboxServiceSelectList.add(new SelectOption("Radiological Service", "Radiological Service"));
        inboxServiceSelectList.add(new SelectOption("Nuclear Medicine (Assay)", "Nuclear Medicine (Assay)"));
        ParamUtil.setRequestAttr(bpc.request, "inboxServiceSelect", inboxServiceSelectList);

        List<SelectOption> inboxTypSelectList = new ArrayList<>();
        inboxTypSelectList.add(new SelectOption("All", "All"));
        inboxTypSelectList.add(new SelectOption("Notification", "Notification"));
        inboxTypSelectList.add(new SelectOption("Announcement", "Announcement"));
        inboxTypSelectList.add(new SelectOption("Query", "Query"));
        ParamUtil.setRequestAttr(bpc.request, "inboxTypeSelect", inboxTypSelectList);

        List<SelectOption> applicationTypeSelectList = new ArrayList<>();
        applicationTypeSelectList.add(new SelectOption("All", "All"));
        applicationTypeSelectList.add(new SelectOption("APTY001", "Appeal "));
        applicationTypeSelectList.add(new SelectOption("APTY004", "Renewal"));
        applicationTypeSelectList.add(new SelectOption("APTY002", "New Application"));
        applicationTypeSelectList.add(new SelectOption("APTY003", "Reinstatement "));
        ParamUtil.setRequestAttr(bpc.request, "appTypeSelect", applicationTypeSelectList);


        List<SelectOption> applicationStatusSelectList = new ArrayList<>();
        applicationStatusSelectList.add(new SelectOption("All", "All"));
        applicationStatusSelectList.add(new SelectOption("APST008", "Draft"));
        applicationStatusSelectList.add(new SelectOption("APST000", "Rollback"));
        applicationStatusSelectList.add(new SelectOption("APST005", "Approved"));
        applicationStatusSelectList.add(new SelectOption("APST007", "Pending"));
        ParamUtil.setRequestAttr(bpc.request, "appStatusSelect", applicationStatusSelectList);

        List<SelectOption> selectApplicationSelectList = new ArrayList<>();
        selectApplicationSelectList.add(new SelectOption("Edit", "Edit"));
        selectApplicationSelectList.add(new SelectOption("Withdraw", "Withdraw"));
        selectApplicationSelectList.add(new SelectOption("Make Payment", "Make Payment"));
        ParamUtil.setRequestAttr(bpc.request, "selectApplication", selectApplicationSelectList);

    }

}