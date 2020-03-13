package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.InboxConst;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
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

    private InterInboxUserDto interInboxUserDto;

    private Map<String,Object> appSearchMap = new HashMap<>();
    private Map<String,Object> inboxSearchMap = new HashMap<>();
    private Map<String,Object> licSearchMap = new HashMap<>();

    private FilterParameter appParameter = new FilterParameter.Builder()
            .clz(InboxAppQueryDto.class)
            .searchAttr(InboxConst.APP_PARAM)
            .resultAttr(InboxConst.APP_RESULT)
            .sortField("CREATED_DT").sortType(InboxConst.DESCENDING).build();

    private FilterParameter inboxParameter = new FilterParameter.Builder()
            .clz(InboxQueryDto.class)
            .searchAttr(InboxConst.INBOX_PARAM)
            .resultAttr(InboxConst.INBOX_RESULT)
            .sortField("created_dt")
            .sortType(SearchParam.DESCENDING).build();

    private FilterParameter licenceParameter = new FilterParameter.Builder()
            .clz(InboxLicenceQueryDto.class)
            .searchAttr(InboxConst.LIC_PARAM)
            .resultAttr(InboxConst.LIC_RESULT)
            .sortField("START_DATE").sortType(InboxConst.DESCENDING).build();

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        IaisEGPHelper.clearSessionAttr(bpc.request,InboxConst.class);
        AccessUtil.initLoginUserInfo(bpc.request);
        LoginContext loginContext= (LoginContext)ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        interInboxUserDto = inboxService.getUserInfoByUserId(loginContext.getUserId());
        ParamUtil.setSessionAttr(bpc.request,"inter-inbox-user-info", interInboxUserDto);
        AuditTrailHelper.auditFunction("main-web", "main web");
    }

    /**
     *
     * @param bpc
     *
     * >>>>>>>>Message Inbox<<<<<<<
     */
    public void toMsgPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> toMsgPage"));

    }

    public void msgDoPage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchResultHelper.doPage(request,inboxParameter);
    }


    public void msgDoSort(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchResultHelper.doSort(request,inboxParameter);
    }

    public void msgToArchive(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        prepareMsgSelectOption(request);
        String msgStatus[] = new String[]{
                MessageConstants.MESSAGE_STATUS_UNREAD
        };
        inboxSearchMap.put("msgStatus",msgStatus);
        inboxSearchMap.put("userId",interInboxUserDto.getUserId());
        inboxParameter.setFilters(inboxSearchMap);
        SearchParam inboxParam = SearchResultHelper.getSearchParam(request,inboxParameter,true);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.MESSAGE_QUERY_KEY,inboxParam);
        SearchResult inboxResult = inboxService.inboxDoQuery(inboxParam);
        List<InboxQueryDto> inboxQueryDtoList = inboxResult.getRows();
        for (InboxQueryDto inboxQueryDto:inboxQueryDtoList
                ) {
            String serviceName = inboxService.getServiceNameById(inboxQueryDto.getServiceId());
            inboxQueryDto.setServiceId(serviceName);
        }
        if(!StringUtil.isEmpty(inboxResult)){
            ParamUtil.setSessionAttr(request,InboxConst.INBOX_PARAM, inboxParam);
            ParamUtil.setRequestAttr(request,InboxConst.INBOX_RESULT, inboxResult);
            ParamUtil.setRequestAttr(request,InboxConst.MESSAGE_PAGE, InboxConst.MESSAGE_CONTENT_VIEW);
            cleanParameter("MSG");
            clearMsgFilter();
        }
        setNumInfoToRequest(request,interInboxUserDto);
    }

    public void msgViewStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
    }

    public void msgToView(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String msgContent = ParamUtil.getMaskedString(request,InboxConst.CRUD_ACTION_VALUE);
        ParamUtil.setRequestAttr(request,InboxConst.MESSAGE_CONTENT, msgContent);
        setNumInfoToRequest(request,interInboxUserDto);
    }

    public void msgDoSearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String inboxType = ParamUtil.getString(request,InboxConst.MESSAGE_TYPE);
        String inboxService = ParamUtil.getString(request,InboxConst.MESSAGE_SERVICE);
        String msgSubject = ParamUtil.getString(request,InboxConst.MESSAGE_SEARCH);
        if(inboxType != null){
            if (inboxType.equals(InboxConst.SEARCH_ALL)){
                inboxSearchMap.remove("messageType");
            }else{
                inboxSearchMap.put("messageType",inboxType);
            }
        }
        if(inboxService != null){
            if (inboxService.equals(InboxConst.SEARCH_ALL)){
                inboxSearchMap.remove("interService");
            }else{
                inboxSearchMap.put("interService",inboxService);
            }
        }
        if(msgSubject != null){
            inboxSearchMap.put("msgSubject",msgSubject);
        }
        inboxParameter.setFilters(inboxSearchMap);
    }

    public void prepareDate(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        prepareMsgSelectOption(request);
        String msgStatus[] = new String[]{
                MessageConstants.MESSAGE_STATUS_READ,
//                MessageConstants.MESSAGE_STATUS_UNREAD,
                MessageConstants.MESSAGE_STATUS_RESPONSE,
                MessageConstants.MESSAGE_STATUS_UNRESPONSE,
        };
        inboxSearchMap.put("userId",interInboxUserDto.getUserId());
        inboxSearchMap.put("msgStatus",msgStatus);
        inboxParameter.setFilters(inboxSearchMap);
        SearchParam inboxParam = SearchResultHelper.getSearchParam(request,inboxParameter,true);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.MESSAGE_QUERY_KEY,inboxParam);
        SearchResult inboxResult = inboxService.inboxDoQuery(inboxParam);
        List<InboxQueryDto> inboxQueryDtoList = inboxResult.getRows();
        for (InboxQueryDto inboxQueryDto:inboxQueryDtoList
                ) {
            String serviceName = inboxService.getServiceNameById(inboxQueryDto.getServiceId());
            inboxQueryDto.setServiceId(serviceName);
        }
        if(!StringUtil.isEmpty(inboxResult)){
            ParamUtil.setSessionAttr(request,InboxConst.INBOX_PARAM, inboxParam);
            ParamUtil.setRequestAttr(request,InboxConst.INBOX_RESULT, inboxResult);
            ParamUtil.setRequestAttr(request,InboxConst.MESSAGE_PAGE, InboxConst.MESSAGE_VIEW);
            cleanParameter("MSG");
            clearMsgFilter();
        }
        setNumInfoToRequest(request,interInboxUserDto);

    }

    public void prepareSwitch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> prepareSwitch"));
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
        licSearchMap.put("licenseeId",interInboxUserDto.getLicenseeId());
        licenceParameter.setFilters(licSearchMap);
        SearchParam licParam = SearchResultHelper.getSearchParam(request,licenceParameter,true);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.LICENCE_QUERY_KEY,licParam);
        SearchResult licResult = inboxService.licenceDoQuery(licParam);
        log.debug(licParam.getMainSql());
        List<InboxLicenceQueryDto> inboxLicenceQueryDtoList = licResult.getRows();
        for (InboxLicenceQueryDto inboxLicenceQueryDto:inboxLicenceQueryDtoList){
            if ("LICEST001".equals(inboxLicenceQueryDto.getStatus())){
                inboxLicenceQueryDto.setStatus("Active");
            }
        }
        if(!StringUtil.isEmpty(licResult)){
            ParamUtil.setSessionAttr(request,InboxConst.LIC_PARAM, licParam);
            ParamUtil.setRequestAttr(request,InboxConst.LIC_RESULT, licResult);
            cleanParameter("LIC");
        }
        setNumInfoToRequest(request,interInboxUserDto);
    }

    public void licSwitch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request,"lic_action_type");
        log.debug(StringUtil.changeForLog("LicenceSwitch  ----> " + type));
    }

    public void licDoSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
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
            licSearchMap.put("licNo","%"+licenceNo+"%");
        }
        if(serviceType != null){
            if (serviceType.equals(InboxConst.SEARCH_ALL)){
                licSearchMap.remove("serviceType");
            }else {
                licSearchMap.put("serviceType", serviceType);
            }
        }
        if(licStatus != null){
            if (licStatus.equals(InboxConst.SEARCH_ALL)){
                licSearchMap.remove("licStatus");
            }else{
                licSearchMap.put("licStatus",licStatus);
            }

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

    public void licDoAppeal(BaseProcessClass bpc) throws IOException {
        HttpServletRequest request = bpc.request;
        String licNo = ParamUtil.getString(bpc.request, InboxConst.CRUD_ACTION_VALUE);
        StringBuffer url = new StringBuffer();
        url.append("https://").append(bpc.request.getServerName())
                .append("/hcsa-licence-web/eservice/INTERNET/MohAppealApplication")
                .append("?appealingFor=")
                .append(licNo)
                .append("&type=licence");
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
        bpc.response.sendRedirect(tokenUrl);

    }

    public void licToView(BaseProcessClass bpc) throws IOException {
        HttpServletRequest request = bpc.request;
        String licId = ParamUtil.getMaskedString(request, InboxConst.ACTION_ID_VALUE);
        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName())
                .append("/hcsa-licence-web/eservice/INTERNET/MohLicenceView")
                .append("?licenceId=").append(licId);
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
        bpc.response.sendRedirect(tokenUrl);
    }

    /**
     *
     * @param bpc
     *
     */
    public void licDoAmend(BaseProcessClass bpc) throws IOException {
        String licId = ParamUtil.getString(bpc.request, "licenceNo");
        String licIdValue = ParamUtil.getMaskedString(bpc.request, licId);
        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName())
                .append("/hcsa-licence-web/eservice/INTERNET/MohRequestForChange")
                .append("?licenceId=").append(licIdValue);
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
        bpc.response.sendRedirect(tokenUrl);
    }

    /**
     *
     * @param bpc
     *
     */
    public void licDoRenew(BaseProcessClass bpc) throws IOException {
        String [] licIds = ParamUtil.getStrings(bpc.request, "licenceNo");
        if(licIds != null){
            List<String> licIdValue = new ArrayList<>();
            for(String item:licIds){
                licIdValue.add(ParamUtil.getMaskedString(bpc.request,item));
            }
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohWithOutRenewal");
            ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_LIC_ID_LIST_ATTR, (Serializable) licIdValue);
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);

        }
    }

    public void licDoCease(BaseProcessClass bpc) throws IOException {
        String [] licIds = ParamUtil.getStrings(bpc.request, "licenceNo");
        if(licIds != null) {
            List<String> licIdValue = new ArrayList<>();
            for (String item : licIds) {
                licIdValue.add(ParamUtil.getMaskedString(bpc.request, item));
            }
            ParamUtil.setSessionAttr(bpc.request, "licIds", (Serializable) licIdValue);
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohCessationApplication");
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }
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
        appSearchMap.put("licenseeId",interInboxUserDto.getLicenseeId());
        appParameter.setFilters(appSearchMap);
        SearchParam appParam = SearchResultHelper.getSearchParam(request,appParameter,true);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.APPLICATION_QUERY_KEY,appParam);
        SearchResult appResult = inboxService.appDoQuery(appParam);
        List<InboxAppQueryDto> inboxAppQueryDtoList = appResult.getRows();
        for (InboxAppQueryDto appDto:inboxAppQueryDtoList) {
            String serviceName = "N.A.";
            if (appDto.getServiceId() != null){
                serviceName = inboxService.getServiceNameById(appDto.getServiceId());
            }
            appDto.setServiceId(serviceName);
        }

        if(!StringUtil.isEmpty(appResult)){
            ParamUtil.setSessionAttr(request,InboxConst.APP_PARAM, appParam);
            ParamUtil.setRequestAttr(request,InboxConst.APP_RESULT, appResult);
            cleanParameter("APP");
        }
        setNumInfoToRequest(request,interInboxUserDto);
    }

    public void appSwitch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> appSwitch"));
    }

    public void appDoSearch(BaseProcessClass bpc) throws ParseException {
        log.debug(StringUtil.changeForLog("Step ---> appDoSearch"));
        HttpServletRequest request = bpc.request;
        String applicationType = ParamUtil.getString(request,"appTypeSelect");
        String serviceType = ParamUtil.getString(request,"appServiceType");
        String applicationStatus = ParamUtil.getString(request,"appStatusSelect");
        String applicationNo = ParamUtil.getString(request,"appNoPath");
        String createDtStart = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "esd")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String createDtEnd = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "eed")),
                SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
        if(applicationType != null){
            if (applicationType.equals(InboxConst.SEARCH_ALL)){
                appSearchMap.remove("appType");
            }else{
                appSearchMap.put("appType",applicationType);
            }
        }
        if(applicationStatus != null){
            if (applicationStatus.equals(InboxConst.SEARCH_ALL)){
                appSearchMap.remove("appStatus");
            }else{
                appSearchMap.put("appStatus",applicationStatus);
            }
        }
        if(applicationNo != null){
            if (applicationNo.equals(InboxConst.SEARCH_ALL)){
                appSearchMap.remove("appNo");
            }
            else if(applicationNo.indexOf("%") != -1){
                applicationNo = applicationNo.replaceAll("%","//%");
                appSearchMap.put("appNo","%"+applicationNo+"%");
            }
        }
        if(serviceType != null){
            if (serviceType.equals(InboxConst.SEARCH_ALL)){
                appSearchMap.remove("serviceType");
            }else{
                appSearchMap.put("serviceType",serviceType);
            }
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

    public void appDoAppeal(BaseProcessClass bpc) throws IOException {
        HttpServletRequest request = bpc.request;
        String appNo = ParamUtil.getString(request, InboxConst.ACTION_NO_VALUE);
        StringBuffer url = new StringBuffer();
        url.append("https://").append(bpc.request.getServerName())
                .append("/hcsa-licence-web/eservice/INTERNET/MohAppealApplication")
                .append("?appealingFor=")
                .append(appNo)
                .append("&type=application");
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
        bpc.response.sendRedirect(tokenUrl);
    }

    public void appDoWithDraw(BaseProcessClass bpc) throws IOException {
        HttpServletRequest request = bpc.request;
        String appId = ParamUtil.getString(request, InboxConst.ACTION_ID_VALUE);
        String appNo = ParamUtil.getString(request, InboxConst.ACTION_NO_VALUE);
        StringBuffer url = new StringBuffer();
        url.append("https://").append(bpc.request.getServerName())
                .append("/hcsa-licence-web/eservice/INTERNET/MohWithdrawalApplication")
                .append("?appId=")
                .append(appId)
                .append("&appNo=")
                .append(appNo);
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
        bpc.response.sendRedirect(tokenUrl);
    }

    public void appDoDraft(BaseProcessClass bpc) throws IOException {
        log.debug("The prepareEdit start ...");
        HttpServletRequest request = bpc.request;
        String appNo = ParamUtil.getMaskedString(request, InboxConst.ACTION_NO_VALUE);
        if("APTY005".equals(ParamUtil.getMaskedString(request, InboxConst.ACTION_TYPE_VALUE))){
            StringBuffer url = new StringBuffer();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohRequestForChange/prepareDraft")
                    .append("?DraftNumber=")
                    .append(appNo);
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }else if("APTY004".equals(ParamUtil.getMaskedString(request, InboxConst.ACTION_TYPE_VALUE))){
            StringBuffer url = new StringBuffer();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohWithOutRenewal")
                    .append("?DraftNumber=")
                    .append(appNo);
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);

        }
        else {
            StringBuffer url = new StringBuffer();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohNewApplication")
                    .append("?DraftNumber=")
                    .append(appNo);
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);}
    }

    public void appDoDelete(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> appDoDelete start..."));
        String draft = ParamUtil.getString(bpc.request,"action_no_value");
        if(!StringUtil.isEmpty(draft)){
            inboxService.updateDraftStatus(draft,AppConsts.COMMON_STATUS_DELETED);
        }
        log.debug(StringUtil.changeForLog("Step ---> appDoDelete end..."));
    }

    public void appDoReload(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> appDoReload"));
        HttpServletRequest request = bpc.request;
    }




    private void setNumInfoToRequest(HttpServletRequest request,InterInboxUserDto interInboxUserDto){
        Integer licActiveNum = inboxService.licActiveStatusNum(interInboxUserDto.getLicenseeId());
        Integer appDraftNum = inboxService.appDraftNum(interInboxUserDto.getLicenseeId());
        Integer unreadAndresponseNum = inboxService.unreadAndUnresponseNum(interInboxUserDto.getUserId());
        ParamUtil.setRequestAttr(request,"unreadAndresponseNum", unreadAndresponseNum);
        ParamUtil.setRequestAttr(request,"licActiveNum", licActiveNum);
        ParamUtil.setRequestAttr(request,"appDraftNum", appDraftNum);
    }
    /**
     *
     * @param request
     * @description Data to Form select part
     */
    private void prepareMsgSelectOption(HttpServletRequest request){
        List<SelectOption> inboxServiceSelectList = new ArrayList<>();
        inboxServiceSelectList.add(new SelectOption("All", "Select a service"));
        inboxServiceSelectList.add(new SelectOption("34F99D15-820B-EA11-BE7D-000C29F371DC", "Blood Banking"));
        inboxServiceSelectList.add(new SelectOption("35F99D15-820B-EA11-BE7D-000C29F371DC", "Clinical Laboratory"));
        inboxServiceSelectList.add(new SelectOption("A21ADD49-820B-EA11-BE7D-000C29F371DC", "Nuclear Medicine (Assay)"));
        inboxServiceSelectList.add(new SelectOption("F27DD5E2-C90C-EA11-BE7D-000C29F371DC", "Nuclear Medicine (Imaging)"));
        ParamUtil.setRequestAttr(request, "inboxServiceSelect", inboxServiceSelectList);

        List<SelectOption> inboxTypSelectList = new ArrayList<>();
        inboxTypSelectList.add(new SelectOption("All", "Select a type"));
        inboxTypSelectList.add(new SelectOption("MESTYPE001", "Notification"));
        inboxTypSelectList.add(new SelectOption("MESTYPE002", "Announcement"));
        inboxTypSelectList.add(new SelectOption("MESTYPE003", "Action Required"));
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

        List<SelectOption> appServiceStatusSelectList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.getCategoryId(MasterCodeUtil.CATE_ID_APP_STATUS));
        appServiceStatusSelectList.add(0,new SelectOption("All", "All"));
        ParamUtil.setRequestAttr(request, "appStatusSelect", appServiceStatusSelectList);

        List<SelectOption> appServiceTypeSelectList = new ArrayList<>();
        appServiceTypeSelectList.add(new SelectOption("All", "All"));
        appServiceTypeSelectList.add(new SelectOption("34F99D15-820B-EA11-BE7D-000C29F371DC", "Blood Banking"));
        appServiceTypeSelectList.add(new SelectOption("35F99D15-820B-EA11-BE7D-000C29F371DC", "Clinical Laboratory"));
        appServiceTypeSelectList.add(new SelectOption("A21ADD49-820B-EA11-BE7D-000C29F371DC", "Nuclear Medicine (Assay)"));
        appServiceTypeSelectList.add(new SelectOption("F27DD5E2-C90C-EA11-BE7D-000C29F371DC", "Nuclear Medicine (Imaging)"));
        ParamUtil.setRequestAttr(request, "appServiceType", appServiceTypeSelectList);

        List<SelectOption> selectDraftApplicationSelectList = new ArrayList<>();
        selectDraftApplicationSelectList.add(new SelectOption("Reload", "Reload"));
        selectDraftApplicationSelectList.add(new SelectOption("Delete", "Delete"));
        ParamUtil.setRequestAttr(request, "selectDraftApplication", selectDraftApplicationSelectList);

        List<SelectOption> selectApplicationSelectList = new ArrayList<>();
        selectApplicationSelectList.add(new SelectOption("Recall", "Recall"));
        selectApplicationSelectList.add(new SelectOption("Appeal", "Appeal"));
        selectApplicationSelectList.add(new SelectOption("Withdraw", "Withdraw"));
        ParamUtil.setRequestAttr(request, "selectApplication", selectApplicationSelectList);
    }

    private void prepareLicSelectOption(HttpServletRequest request){
        List<SelectOption> LicenceStatusList = new ArrayList<>();
        LicenceStatusList.add(new SelectOption("All", "All"));
        LicenceStatusList.add(new SelectOption("LICEST001", "Active"));
        LicenceStatusList.add(new SelectOption("LICEST002", "Ceased"));
        LicenceStatusList.add(new SelectOption("LICEST003", "Expired"));
        LicenceStatusList.add(new SelectOption("LICEST004", "Lapsed "));
        LicenceStatusList.add(new SelectOption("LICEST005", "Approved  "));
        LicenceStatusList.add(new SelectOption("LICEST006", "Suspended "));
        LicenceStatusList.add(new SelectOption("LICEST007", "Revoked "));
        ParamUtil.setRequestAttr(request, "licStatus", LicenceStatusList);

        List<SelectOption> LicenceTypeList = new ArrayList<>();
        LicenceTypeList.add(new SelectOption("All", "All"));
        LicenceTypeList.add(new SelectOption("Clinical Laboratory", "Clinical Laboratory"));
        LicenceTypeList.add(new SelectOption("Blood Transfusion Service", "Blood Transfusion"));
        ParamUtil.setRequestAttr(request, "licType", LicenceTypeList);

        List<SelectOption> LicenceActionsList = new ArrayList<>();
        LicenceActionsList.add(new SelectOption("Appeal", "Appeal"));
        ParamUtil.setRequestAttr(request, "licActions", LicenceActionsList);
    }

    private void cleanParameter(String tabName){
        if ("MSG".equals(tabName)){
            appSearchMap.clear();
            licSearchMap.clear();
        }
        if ("APP".equals(tabName)){
            licSearchMap.clear();
            inboxSearchMap.clear();
        }
        if ("LIC".equals(tabName)){
            appSearchMap.clear();
            inboxSearchMap.clear();
        }
    }

    private void clearMsgFilter(){
        inboxSearchMap.remove("interService");
        inboxSearchMap.remove("messageType");
    }
    
}
