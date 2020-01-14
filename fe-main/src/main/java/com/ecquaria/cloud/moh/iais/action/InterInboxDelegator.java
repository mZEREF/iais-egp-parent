package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.InboxConst;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2020-01-08 10:49
 **/
@Slf4j
@Delegator("interInboxDelegator")
public class InterInboxDelegator {

    @Autowired
    private InboxService inboxService;

    @Autowired
    private InterInboxDelegator(InboxService inboxService){
        this.inboxService = inboxService;

    }

    private FilterParameter appParameter = new FilterParameter.Builder()
            .clz(InboxAppQueryDto.class)
            .searchAttr(InboxConst.APP_PARAM)
            .resultAttr(InboxConst.APP_RESULT)
            .sortField("CREATED_DT").sortType(InboxConst.DESCENDING).build();

    private FilterParameter inboxParameter = new FilterParameter.Builder()
            .clz(InboxQueryDto.class)
            .searchAttr(InboxConst.INBOX_PARAM)
            .resultAttr(InboxConst.INBOX_RESULT)
            .sortField("id").build();

    private FilterParameter licenceParameter = new FilterParameter.Builder()
            .clz(InboxLicenceQueryDto.class)
            .searchAttr(InboxConst.LIC_PARAM)
            .resultAttr(InboxConst.LIC_RESULT)
            .sortField("START_DATE").sortType(InboxConst.DESCENDING).build();

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        IaisEGPHelper.clearSessionAttr(bpc.request,InboxConst.class);
        AuditTrailHelper.auditFunction("main-web", "main web");
    }

    /**
     *
     * @param bpc
     *
     * >>>>>>>>Message Inbox<<<<<<<
     */
    public void toMsgPage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

    }

    public void msgDoPage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchResultHelper.doPage(request,inboxParameter);
    }

    public void msgDoSort(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchResultHelper.doSort(request,inboxParameter);
    }

    public void msgDoSearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String inboxType = ParamUtil.getString(request,InboxConst.MESSAGE_TYPE);
        String inboxService = ParamUtil.getString(request,InboxConst.MESSAGE_SERVICE);
        String msgSubject = ParamUtil.getString(request,InboxConst.MESSAGE_SEARCH);
        Map<String,Object> inboxSearchMap = new HashMap<>();
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
    }

    public void prepareDate(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        prepareMsgSelectOption(request);
        SearchParam inboxParam = SearchResultHelper.getSearchParam(request,inboxParameter,true);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.MESSAGE_QUERY_KEY,inboxParam);
        SearchResult inboxResult = inboxService.inboxDoQuery(inboxParam);
        List<InboxQueryDto> inboxQueryDtoList = inboxResult.getRows();
        for (InboxQueryDto inboxQueryDto:inboxQueryDtoList
                ) {
            String serviceName = inboxService.getServiceNameById(inboxQueryDto.getServiceId());
            inboxQueryDto.setProcessUrl(RedirectUtil.changeUrlToCsrfGuardUrlUrl(inboxQueryDto.getProcessUrl(), request));
            inboxQueryDto.setServiceId(serviceName);
        }
        if(!StringUtil.isEmpty(inboxResult)){
            ParamUtil.setSessionAttr(request,InboxConst.INBOX_PARAM, inboxParam);
            ParamUtil.setRequestAttr(request,InboxConst.INBOX_RESULT, inboxResult);
        }
        Integer licActiveNum = inboxService.licActiveStatusNum();
        Integer appDraftNum = inboxService.appDraftNum();
        ParamUtil.setRequestAttr(request,"licActiveNum", licActiveNum);
        ParamUtil.setRequestAttr(request,"appDraftNum", appDraftNum);



    }

    public void prepareSwitch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
    }

    /**
     *
     * @param bpc
     *
     * >>>>>>>Licence Inbox<<<<<<<
     */
    public void toLicencePage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> toLicencePage"));
        HttpServletRequest request = bpc.request;
        prepareLicSelectOption(request);
        SearchParam licParam = SearchResultHelper.getSearchParam(request,licenceParameter,true);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.LICENCE_QUERY_KEY,licParam);
        SearchResult licResult = inboxService.licenceDoQuery(licParam);
        if(!StringUtil.isEmpty(licResult)){
            ParamUtil.setSessionAttr(request,InboxConst.LIC_PARAM, licParam);
            ParamUtil.setRequestAttr(request,InboxConst.LIC_RESULT, licResult);
        }
        Integer licActiveNum = inboxService.licActiveStatusNum();
        Integer appDraftNum = inboxService.appDraftNum();
        ParamUtil.setRequestAttr(request,"licActiveNum", licActiveNum);
        ParamUtil.setRequestAttr(request,"appDraftNum", appDraftNum);
    }

    public void licSwitch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request,"lic_action_type");
        log.debug(StringUtil.changeForLog("LicenceSwitch  ----> " + type));
    }

    public void licDoSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        Map<String,Object> licSearchMap = new HashMap<>();
        String licenceNo = ParamUtil.getString(request,"licNoPath");
        String serviceType = ParamUtil.getString(request,"licType");
        String licStatus = ParamUtil.getString(request,"licStatus");
        String fStartDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "fStartDate")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String eStartDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "eStartDate")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String fExpiryDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "fExpiryDate")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String eExpiryDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "eExpiryDate")),
                SystemAdminBaseConstants.DATE_FORMAT);
        if(licenceNo != null){
            licSearchMap.put("licNo",licenceNo);
        }
        if(serviceType != null && !serviceType.equals(InboxConst.SEARCH_ALL)){
            licSearchMap.put("serviceType",serviceType);
        }
        if(licStatus != null && !licStatus.equals(InboxConst.SEARCH_ALL)){
            licSearchMap.put("licStatus",licStatus);
        }
        if(!StringUtil.isEmpty(fStartDate)){
            licSearchMap.put("fStartDate",fStartDate);
        }
        if(!StringUtil.isEmpty(eStartDate)){
            licSearchMap.put("eStartDate",eStartDate);
        }
        if(!StringUtil.isEmpty(fExpiryDate)){
            licSearchMap.put("fExpiryDate",fExpiryDate);
        }
        if(!StringUtil.isEmpty(eExpiryDate)){
            licSearchMap.put("eExpiryDate",eExpiryDate);
        }
        licenceParameter.setFilters(licSearchMap);
        licenceParameter.setPageNo(1);
    }

    public void licDoPage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchResultHelper.doPage(request,licenceParameter);
    }

    public void licDoSort(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchResultHelper.doSort(request,licenceParameter);
    }

    /**
     *
     * @param bpc
     *
     * >>>>>>>>Application Inbox<<<<<<<<
     */
    public void toAppPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> toAppPage"));
        HttpServletRequest request = bpc.request;
        prepareAppSelectOption(request);
        /**
         * Application SearchResult
         */
        SearchParam appParam = SearchResultHelper.getSearchParam(request,appParameter,true);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.APPLICATION_QUERY_KEY,appParam);
        SearchResult appResult = inboxService.appDoQuery(appParam);
        List<InboxAppQueryDto> inboxAppQueryDtoList = appResult.getRows();
        for (InboxAppQueryDto appDto:inboxAppQueryDtoList) {
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
        }
        Integer licActiveNum = inboxService.licActiveStatusNum();
        Integer appDraftNum = inboxService.appDraftNum();
        ParamUtil.setRequestAttr(request,"licActiveNum", licActiveNum);
        ParamUtil.setRequestAttr(request,"appDraftNum", appDraftNum);
    }

    public void appSwitch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
    }

    public void appDoSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String applicationType = ParamUtil.getString(request,"appTypeSelect");
        String serviceType = ParamUtil.getString(request,"appServiceType");
        String applicationStatus = ParamUtil.getString(request,"appStatusSelect");
        String applicationNo = ParamUtil.getString(request,"appNoPath");
        String createDtStart = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "esd")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String createDtEnd = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "eed")),
                SystemAdminBaseConstants.DATE_FORMAT);
        Map<String,Object> appSearchMap = new HashMap<>();
        if(applicationType != null && !applicationType.equals(InboxConst.SEARCH_ALL)){
            appSearchMap.put("appType",applicationType);
        }
        if(applicationStatus != null && !applicationStatus.equals(InboxConst.SEARCH_ALL)){
            appSearchMap.put("appStatus",applicationStatus);
        }
        if(applicationNo != null && !applicationNo.equals(InboxConst.SEARCH_ALL)){
            appSearchMap.put("appNo",applicationNo);
        }
        if(serviceType != null && !serviceType.equals(InboxConst.SEARCH_ALL)){
            appSearchMap.put("serviceType",serviceType);
        }
        if(!StringUtil.isEmpty(createDtStart)){
            appSearchMap.put("createDtStart",createDtStart);
        }
        if(!StringUtil.isEmpty(createDtEnd)){
            appSearchMap.put("createDtEnd",createDtEnd);
        }
        appParameter.setFilters(appSearchMap);
        appParameter.setPageNo(1);

    }

    public void appDoPage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchResultHelper.doPage(request,appParameter);
    }

    public void appDoSort(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchResultHelper.doSort(request,appParameter);
    }

    public void appDoDraft(BaseProcessClass bpc) throws IOException {
        log.debug("The prepareEdit start ...");
        HttpServletRequest request = bpc.request;
        String appNo = ParamUtil.getString(request, InboxConst.CRUD_ACTION_VALUE);
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
     * @param request
     * @description Data to Form select part
     */
    private void prepareMsgSelectOption(HttpServletRequest request){
        List<SelectOption> inboxServiceSelectList = new ArrayList<>();
        inboxServiceSelectList.add(new SelectOption("All", "All"));
        inboxServiceSelectList.add(new SelectOption("34F99D15-820B-EA11-BE7D-000C29F371DC", "Blood Banking"));
        inboxServiceSelectList.add(new SelectOption("35F99D15-820B-EA11-BE7D-000C29F371DC", "Clinical Laboratory"));
        ParamUtil.setRequestAttr(request, "inboxServiceSelect", inboxServiceSelectList);

        List<SelectOption> inboxTypSelectList = new ArrayList<>();
        inboxTypSelectList.add(new SelectOption("All", "All"));
        inboxTypSelectList.add(new SelectOption("MESTYPE001", "Notification"));
        inboxTypSelectList.add(new SelectOption("MESTYPE002", "Announcement"));
        inboxTypSelectList.add(new SelectOption("MESTYPE003", "Query"));
        ParamUtil.setRequestAttr(request, "inboxTypeSelect", inboxTypSelectList);
    }

    private void prepareAppSelectOption(HttpServletRequest request){
        List<SelectOption> applicationTypeSelectList = new ArrayList<>();
        applicationTypeSelectList.add(new SelectOption("All", "All"));
        applicationTypeSelectList.add(new SelectOption("APTY001", "Appeal"));
        applicationTypeSelectList.add(new SelectOption("APTY004", "Renewal"));
        applicationTypeSelectList.add(new SelectOption("APTY002", "New Application"));
        applicationTypeSelectList.add(new SelectOption("APTY003", "Reinstatement "));
        ParamUtil.setRequestAttr(request, "appTypeSelect", applicationTypeSelectList);


        List<SelectOption> applicationStatusSelectList = new ArrayList<>();
        applicationStatusSelectList.add(new SelectOption("All", "All"));
        applicationStatusSelectList.add(new SelectOption("APST008", "Draft"));
        applicationStatusSelectList.add(new SelectOption("APST000", "Rollback"));
        applicationStatusSelectList.add(new SelectOption("APST005", "Approved"));
        applicationStatusSelectList.add(new SelectOption("APST007", "Pending"));
        ParamUtil.setRequestAttr(request, "appStatusSelect", applicationStatusSelectList);

        List<SelectOption> appServiceTypeSelectList = new ArrayList<>();
        appServiceTypeSelectList.add(new SelectOption("All", "All"));
        appServiceTypeSelectList.add(new SelectOption("34F99D15-820B-EA11-BE7D-000C29F371DC", "Blood Banking"));
        appServiceTypeSelectList.add(new SelectOption("35F99D15-820B-EA11-BE7D-000C29F371DC", "Clinical Laboratory"));
        ParamUtil.setRequestAttr(request, "appServiceType", appServiceTypeSelectList);

        List<SelectOption> selectDraftApplicationSelectList = new ArrayList<>();
        selectDraftApplicationSelectList.add(new SelectOption("All", "All"));
        selectDraftApplicationSelectList.add(new SelectOption("Reload", "Reload"));
        selectDraftApplicationSelectList.add(new SelectOption("Delete", "Delete"));
        ParamUtil.setRequestAttr(request, "selectDraftApplication", selectDraftApplicationSelectList);

        List<SelectOption> selectApplicationSelectList = new ArrayList<>();
        selectApplicationSelectList.add(new SelectOption("All", "All"));
        selectApplicationSelectList.add(new SelectOption("Recall", "Recall"));
        selectApplicationSelectList.add(new SelectOption("Withdraw", "Withdraw"));
        ParamUtil.setRequestAttr(request, "selectApplication", selectApplicationSelectList);
    }

    private void prepareLicSelectOption(HttpServletRequest request){
        List<SelectOption> LicenceStatusList = new ArrayList<>();
        LicenceStatusList.add(new SelectOption("All", "All"));
        LicenceStatusList.add(new SelectOption("LICEST001", "Action"));
        ParamUtil.setRequestAttr(request, "licStatus", LicenceStatusList);

        List<SelectOption> LicenceTypeList = new ArrayList<>();
        LicenceTypeList.add(new SelectOption("All", "All"));
        LicenceTypeList.add(new SelectOption("Clinical Laboratory", "Clinical Laboratory"));
        LicenceTypeList.add(new SelectOption("Blood Transfusion Service", "Blood Transfusion"));
        ParamUtil.setRequestAttr(request, "licType", LicenceTypeList);
    }

}
