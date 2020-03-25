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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
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

    private static String msgStatus[] = {
            MessageConstants.MESSAGE_STATUS_READ,
            MessageConstants.MESSAGE_STATUS_UNREAD,
            MessageConstants.MESSAGE_STATUS_RESPONSE,
            MessageConstants.MESSAGE_STATUS_UNRESPONSE,
    };

    private static String msgArchiverStatus[] = {
            MessageConstants.MESSAGE_STATUS_ARCHIVER,
    };
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
        log.debug(StringUtil.changeForLog("Login role information --->> ##User-Id:"+interInboxUserDto.getUserId()+"### Licensee-Id:"+interInboxUserDto.getLicenseeId()));
        ParamUtil.setSessionAttr(bpc.request,InboxConst.INTER_INBOX_USER_INFO, interInboxUserDto);
        AuditTrailHelper.auditFunction("main-web", "main web");
    }

    /**
     *
     * @param bpc
     *
     * >>>>>>>>Message Inbox<<<<<<<
     */
    public void toMsgPage(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("Step ---> toMsgPage"));
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
        SearchParam inboxParam = SearchResultHelper.getSearchParam(request,inboxParameter,true);
        inboxParam.addFilter("userId", interInboxUserDto.getLicenseeId(),true);
        inboxParam.addFilter(InboxConst.MESSAGE_STATUS, msgArchiverStatus,true);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.MESSAGE_QUERY_KEY,inboxParam);
        SearchResult inboxResult = inboxService.inboxDoQuery(inboxParam);
        List<InboxQueryDto> inboxQueryDtoList = inboxResult.getRows();
        for (InboxQueryDto inboxQueryDto:inboxQueryDtoList
                ) {
            String serviceName = inboxService.getServiceNameById(inboxQueryDto.getServiceId());
            inboxQueryDto.setServiceId(serviceName);
        }
        if(!StringUtil.isEmpty(inboxResult)){
            clearParameter("IIMT");
            ParamUtil.setSessionAttr(request,InboxConst.INBOX_PARAM, inboxParam);
            ParamUtil.setRequestAttr(request,InboxConst.INBOX_RESULT, inboxResult);
            ParamUtil.setRequestAttr(request,InboxConst.MESSAGE_PAGE, InboxConst.MESSAGE_CONTENT_VIEW);
        }
        setNumInfoToRequest(request,interInboxUserDto);
    }

    public void msgViewStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
    }

    public void msgDoArchive(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> msgDoArchive"));
        HttpServletRequest request = bpc.request;
        String[] msgIdList = ParamUtil.getStrings(request,"msgIdList");
        if (msgIdList != null){
            boolean archiveResult = inboxService.updateMsgStatus(msgIdList);
            ParamUtil.setRequestAttr(request,InboxConst.MESSAGE_ARCHIVE_RESULT, archiveResult);
        }
    }

    public void msgToView(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String msgContent = ParamUtil.getMaskedString(request,InboxConst.CRUD_ACTION_VALUE);
        ParamUtil.setRequestAttr(request,InboxConst.MESSAGE_CONTENT, msgContent);
        setNumInfoToRequest(request,interInboxUserDto);
    }

    public void msgDoSearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        Map<String,Object> inboxSearchMap = IaisCommonUtils.genNewHashMap();
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
        }else{

            inboxSearchMap.remove("msgSubject");
        }
        inboxParameter.setFilters(inboxSearchMap);
    }

    public void prepareDate(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> Into Message Page"));
        HttpServletRequest request = bpc.request;
        prepareMsgSelectOption(request);
        SearchParam inboxParam = SearchResultHelper.getSearchParam(request,inboxParameter,true);
        inboxParam.addFilter("userId", interInboxUserDto.getLicenseeId(),true);
        inboxParam.addFilter(InboxConst.MESSAGE_STATUS, msgStatus,true);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.MESSAGE_QUERY_KEY,inboxParam);
        SearchResult inboxResult = inboxService.inboxDoQuery(inboxParam);
        List<InboxQueryDto> inboxQueryDtoList = inboxResult.getRows();
        for (InboxQueryDto inboxQueryDto:inboxQueryDtoList
                ) {
            String serviceName = inboxService.getServiceNameById(inboxQueryDto.getServiceId());
            inboxQueryDto.setServiceId(serviceName);
        }
        if(!StringUtil.isEmpty(inboxResult)){
            clearParameter("IIMT");
            ParamUtil.setSessionAttr(request,InboxConst.INBOX_PARAM, inboxParam);
            ParamUtil.setRequestAttr(request,InboxConst.INBOX_RESULT, inboxResult);
            ParamUtil.setRequestAttr(request,InboxConst.MESSAGE_PAGE, InboxConst.MESSAGE_VIEW);
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
        SearchParam licParam = SearchResultHelper.getSearchParam(request,licenceParameter,true);
        licParam.addFilter("licenseeId",interInboxUserDto.getLicenseeId(),true);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.LICENCE_QUERY_KEY,licParam);
        SearchResult licResult = inboxService.licenceDoQuery(licParam);
        List<InboxLicenceQueryDto> inboxLicenceQueryDtoList = licResult.getRows();
        for (InboxLicenceQueryDto inboxLicenceQueryDto:inboxLicenceQueryDtoList){
            if ("LICEST001".equals(inboxLicenceQueryDto.getStatus())){
                inboxLicenceQueryDto.setStatus("Active");
            }
        }
        if(!StringUtil.isEmpty(licResult)){
            clearParameter("IILT");
            ParamUtil.setSessionAttr(request,InboxConst.LIC_PARAM, licParam);
            ParamUtil.setRequestAttr(request,InboxConst.LIC_RESULT, licResult);
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
        Map<String,Object> licSearchMap = IaisCommonUtils.genNewHashMap();
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
            licSearchMap.put("licNo",'%'+licenceNo+'%');
        }else{
            licSearchMap.remove("licNo");
        }
        if(serviceType == null || serviceType.equals(InboxConst.SEARCH_ALL)){
            licSearchMap.remove("serviceType");
        }else {
            licSearchMap.put("serviceType", serviceType);
        }
        if(licStatus == null || licStatus.equals(InboxConst.SEARCH_ALL)){
            licSearchMap.remove("licStatus");
        }else{
            licSearchMap.put("licStatus",licStatus);
        }
        if(!StringUtil.isEmpty(fStartDate)){
            licSearchMap.put("fStartDate",fStartDate);
        }else{
            licSearchMap.remove("fStartDate");
        }
        if(!StringUtil.isEmpty(eStartDate)){
            licSearchMap.put("eStartDate",eStartDate);
        }else{
            licSearchMap.remove("eStartDate");
        }
        if(!StringUtil.isEmpty(fExpiryDate)){
            licSearchMap.put("fExpiryDate",fExpiryDate);
        }else{
            licSearchMap.remove("fExpiryDate");
        }
        if(!StringUtil.isEmpty(eExpiryDate)){
            licSearchMap.put("eExpiryDate",eExpiryDate);
        }else{
            licSearchMap.remove("eExpiryDate");
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
        StringBuilder url = new StringBuilder();
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
            List<String> licIdValue = IaisCommonUtils.genNewArrayList();
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
            List<String> licIdValue = IaisCommonUtils.genNewArrayList();
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
        SearchParam appParam = SearchResultHelper.getSearchParam(request,appParameter,true);
        appParam.addFilter("licenseeId", interInboxUserDto.getLicenseeId(),true);
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
            clearParameter("IIAT");
            ParamUtil.setSessionAttr(request,InboxConst.APP_PARAM, appParam);
            ParamUtil.setRequestAttr(request,InboxConst.APP_RESULT, appResult);
        }
        setNumInfoToRequest(request,interInboxUserDto);
    }

    public void appSwitch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> appSwitch"));
    }

    public void appDoSearch(BaseProcessClass bpc) throws ParseException {
        log.debug(StringUtil.changeForLog("Step ---> appDoSearch"));
        HttpServletRequest request = bpc.request;
        Map<String,Object> appSearchMap = IaisCommonUtils.genNewHashMap();
        String applicationType = ParamUtil.getString(request,"appTypeSelect");
        String serviceType = ParamUtil.getString(request,"appServiceType");
        String applicationStatus = ParamUtil.getString(request,"appStatusSelect");
        String applicationNo = ParamUtil.getString(request,"appNoPath");
        String createDtStart = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "esd")),
                SystemAdminBaseConstants.DATE_FORMAT);
        String createDtEnd = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, "eed")),
                SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
        if(applicationType == null || applicationType.equals(InboxConst.SEARCH_ALL)){
            appSearchMap.remove("appType");
        }else{
            appSearchMap.put("appType",applicationType);
        }
        if(applicationStatus == null || applicationStatus.equals(InboxConst.SEARCH_ALL)){
            appSearchMap.remove("appStatus");
        }else{
            appSearchMap.put("appStatus",applicationStatus);
        }
        if(applicationNo != null){
            if(applicationNo.indexOf('%') != -1){
                applicationNo = applicationNo.replaceAll("%","//%");
                appSearchMap.put("appNo","%"+applicationNo+"%");
            }else{
                appSearchMap.put("appNo","%"+applicationNo+"%");
            }
        }else{
            appSearchMap.remove("appNo");
        }
        if(serviceType == null || serviceType.equals(InboxConst.SEARCH_ALL)){
            appSearchMap.remove("serviceType");
        }else{
            appSearchMap.put("serviceType",serviceType);
        }
        if(!StringUtil.isEmpty(createDtStart)){
            appSearchMap.put("createDtStart",createDtStart);
        }else{
            appSearchMap.remove("createDtStart");
        }
        if(!StringUtil.isEmpty(createDtEnd)){
            appSearchMap.put("createDtEnd",createDtEnd);
        }else{
            appSearchMap.remove("createDtEnd");
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
        StringBuilder url = new StringBuilder();
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
        StringBuilder url = new StringBuilder();
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
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohRequestForChange/prepareDraft")
                    .append("?DraftNumber=")
                    .append(appNo);
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }else if("APTY004".equals(ParamUtil.getMaskedString(request, InboxConst.ACTION_TYPE_VALUE))){
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohWithOutRenewal")
                    .append("?DraftNumber=")
                    .append(appNo);
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);

        }
        else {
            StringBuilder url = new StringBuilder();
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

    public void appDoRecall(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> appDoRecall"));
        HttpServletRequest request = bpc.request;
        String appId = ParamUtil.getString(request, InboxConst.ACTION_ID_VALUE);
        String appNo = ParamUtil.getString(request, InboxConst.ACTION_NO_VALUE);
        RecallApplicationDto recallApplicationDto = new RecallApplicationDto();
        recallApplicationDto.setAppId(appId);
        recallApplicationDto.setAppNo(appNo);
        Boolean recallResult = inboxService.recallApplication(recallApplicationDto);
        ParamUtil.setRequestAttr(request,InboxConst.APP_RECALL_RESULT, recallResult);
    }

    private void setNumInfoToRequest(HttpServletRequest request,InterInboxUserDto interInboxUserDto){
        Integer licActiveNum = inboxService.licActiveStatusNum(interInboxUserDto.getLicenseeId());
        Integer appDraftNum = inboxService.appDraftNum(interInboxUserDto.getLicenseeId());
        Integer unreadAndresponseNum = inboxService.unreadAndUnresponseNum(interInboxUserDto.getLicenseeId());
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
        List<SelectOption> inboxServiceSelectList = IaisCommonUtils.genNewArrayList();
        inboxServiceSelectList.add(new SelectOption("All", "Select a service"));
        inboxServiceSelectList.add(new SelectOption("34F99D15-820B-EA11-BE7D-000C29F371DC", "Blood Banking"));
        inboxServiceSelectList.add(new SelectOption("35F99D15-820B-EA11-BE7D-000C29F371DC", "Clinical Laboratory"));
        inboxServiceSelectList.add(new SelectOption("A21ADD49-820B-EA11-BE7D-000C29F371DC", "Nuclear Medicine (Assay)"));
        inboxServiceSelectList.add(new SelectOption("F27DD5E2-C90C-EA11-BE7D-000C29F371DC", "Nuclear Medicine (Imaging)"));
        ParamUtil.setRequestAttr(request, "inboxServiceSelect", inboxServiceSelectList);

        List<SelectOption> inboxTypSelectList = IaisCommonUtils.genNewArrayList();
        inboxTypSelectList.add(new SelectOption("All", "Select a type"));
        inboxTypSelectList.add(new SelectOption("MESTYPE001", "Notification"));
        inboxTypSelectList.add(new SelectOption("MESTYPE002", "Announcement"));
        inboxTypSelectList.add(new SelectOption("MESTYPE003", "Action Required"));
        ParamUtil.setRequestAttr(request, "inboxTypeSelect", inboxTypSelectList);
    }

    private void prepareAppSelectOption(HttpServletRequest request){
        List<SelectOption> applicationTypeSelectList = IaisCommonUtils.genNewArrayList();
        applicationTypeSelectList.add(new SelectOption("All", "All"));
        applicationTypeSelectList.add(new SelectOption("APTY001", "Renewal"));
        applicationTypeSelectList.add(new SelectOption("APTY006", "Withdrawal "));
        applicationTypeSelectList.add(new SelectOption("APTY008", "Cessation "));
        applicationTypeSelectList.add(new SelectOption("APTY005", "Request For Change"));
        applicationTypeSelectList.add(new SelectOption("APTY002", "New Licence Application"));
        ParamUtil.setRequestAttr(request, "appTypeSelect", applicationTypeSelectList);

        List<SelectOption> applicationStatusSelectList = IaisCommonUtils.genNewArrayList();
        applicationStatusSelectList.add(new SelectOption("All", "All"));
        applicationStatusSelectList.add(new SelectOption("APST008", "Draft"));
        applicationStatusSelectList.add(new SelectOption("APST000", "Rollback"));
        applicationStatusSelectList.add(new SelectOption("APST005", "Approved"));
        applicationStatusSelectList.add(new SelectOption("APST007", "Pending"));
        ParamUtil.setRequestAttr(request, "appStatusSelect", applicationStatusSelectList);

        List<SelectOption> appServiceStatusSelectList = IaisCommonUtils.genNewArrayList();
        appServiceStatusSelectList.add(new SelectOption("All", "All"));
        appServiceStatusSelectList.add(new SelectOption("All", "Draft"));
        appServiceStatusSelectList.add(new SelectOption("All", "Recalled"));
        appServiceStatusSelectList.add(new SelectOption("All", "Pending Payment"));
        appServiceStatusSelectList.add(new SelectOption("All", "Pending Clarification"));
        appServiceStatusSelectList.add(new SelectOption("All", "Pending Screening"));
        appServiceStatusSelectList.add(new SelectOption("All", "Pending Inspection"));
        appServiceStatusSelectList.add(new SelectOption("All", "Pending Appointment Scheduling"));
        appServiceStatusSelectList.add(new SelectOption("All", "Pending Appointment Re-Scheduling"));
        appServiceStatusSelectList.add(new SelectOption("All", "Pending NC Rectification"));
        appServiceStatusSelectList.add(new SelectOption("All", "Pending Approval"));
        appServiceStatusSelectList.add(new SelectOption("All", "Approved"));
        appServiceStatusSelectList.add(new SelectOption("All", "Rejected"));
        appServiceStatusSelectList.add(new SelectOption("All", "Withdrawn"));
        ParamUtil.setRequestAttr(request, "appStatusSelect", appServiceStatusSelectList);

        List<SelectOption> appServiceTypeSelectList = IaisCommonUtils.genNewArrayList();
        appServiceTypeSelectList.add(new SelectOption("All", "All"));
        appServiceTypeSelectList.add(new SelectOption("34F99D15-820B-EA11-BE7D-000C29F371DC", "Blood Banking"));
        appServiceTypeSelectList.add(new SelectOption("35F99D15-820B-EA11-BE7D-000C29F371DC", "Clinical Laboratory"));
        appServiceTypeSelectList.add(new SelectOption("7B450178-C70C-EA11-BE7D-000C29F371DC", "Tissue Banking"));
        appServiceTypeSelectList.add(new SelectOption("928ABF63-790B-EA11-BE7D-000C29F371DC", "Acute Hospital"));
        appServiceTypeSelectList.add(new SelectOption("938ABF63-790B-EA11-BE7D-000C29F371DC", "Community Hospital"));
        appServiceTypeSelectList.add(new SelectOption("948ABF63-790B-EA11-BE7D-000C29F371DC", "Medical Clinic"));
        appServiceTypeSelectList.add(new SelectOption("958ABF63-790B-EA11-BE7D-000C29F371DC", "Dental Clinic"));
        appServiceTypeSelectList.add(new SelectOption("968ABF63-790B-EA11-BE7D-000C29F371DC", "Renal Dialysis Centre"));
        appServiceTypeSelectList.add(new SelectOption("6B872C3E-7C0B-EA11-BE7D-000C29F371DC", "Ambulatory Surgical Centre"));
        appServiceTypeSelectList.add(new SelectOption("6D872C3E-7C0B-EA11-BE7D-000C29F371DC", "Nursing Home"));
        appServiceTypeSelectList.add(new SelectOption("6E872C3E-7C0B-EA11-BE7D-000C29F371DC", "Inpatient Palliative Care"));
        appServiceTypeSelectList.add(new SelectOption("6F872C3E-7C0B-EA11-BE7D-000C29F371DC", "Emergency Ambulance"));
        appServiceTypeSelectList.add(new SelectOption("70872C3E-7C0B-EA11-BE7D-000C29F371DC", "Medical Transport"));
        appServiceTypeSelectList.add(new SelectOption("F6E22EA0-7E0B-EA11-BE7D-000C29F371DC", "Neonatal ICU"));
        appServiceTypeSelectList.add(new SelectOption("F7E22EA0-7E0B-EA11-BE7D-000C29F371DC", "Sterile Pharmaceutical"));
        appServiceTypeSelectList.add(new SelectOption("F8E22EA0-7E0B-EA11-BE7D-000C29F371DC", "Haematopoietic Stem Cell Transplant"));
        appServiceTypeSelectList.add(new SelectOption("F9E22EA0-7E0B-EA11-BE7D-000C29F371DC", "Collaborative Prescribing"));
        appServiceTypeSelectList.add(new SelectOption("FAE22EA0-7E0B-EA11-BE7D-000C29F371DC", "Chronic Sick Unit"));
        appServiceTypeSelectList.add(new SelectOption("FBE22EA0-7E0B-EA11-BE7D-000C29F371DC", "Hyperbaric Oxygen Therapy"));
        appServiceTypeSelectList.add(new SelectOption("D387EBD9-810B-EA11-BE7D-000C29F371DC", "HIV"));
        appServiceTypeSelectList.add(new SelectOption("D487EBD9-810B-EA11-BE7D-000C29F371DC", "Pre-Implantation Genetics Diagnosis"));
        appServiceTypeSelectList.add(new SelectOption("D587EBD9-810B-EA11-BE7D-000C29F371DC", "Pre-Implantation Genetics Screening"));
        appServiceTypeSelectList.add(new SelectOption("D687EBD9-810B-EA11-BE7D-000C29F371DC", "Assisted Reproduction Services"));
        appServiceTypeSelectList.add(new SelectOption("D787EBD9-810B-EA11-BE7D-000C29F371DC", "Radiation Oncology Service / PBT"));
        appServiceTypeSelectList.add(new SelectOption("D887EBD9-810B-EA11-BE7D-000C29F371DC", "Cell, Tissue and Gene Therapy Service"));
        appServiceTypeSelectList.add(new SelectOption("30F99D15-820B-EA11-BE7D-000C29F371DC", "Specialized Interventional Procedures Services"));
        appServiceTypeSelectList.add(new SelectOption("31F99D15-820B-EA11-BE7D-000C29F371DC", "Organ Transplant"));
        appServiceTypeSelectList.add(new SelectOption("32F99D15-820B-EA11-BE7D-000C29F371DC", "Blood Transfusion Service"));
        appServiceTypeSelectList.add(new SelectOption("33F99D15-820B-EA11-BE7D-000C29F371DC", "Clinical Genetic and Genomic Service"));
        appServiceTypeSelectList.add(new SelectOption("A11ADD49-820B-EA11-BE7D-000C29F371DC", "Radiological Service"));
        appServiceTypeSelectList.add(new SelectOption("A31ADD49-820B-EA11-BE7D-000C29F371DC", "Telemedicine"));
        appServiceTypeSelectList.add(new SelectOption("A41ADD49-820B-EA11-BE7D-000C29F371DC", "Health Screening"));
        appServiceTypeSelectList.add(new SelectOption("6B872C3E-7C0B-EA11-BE7D-000C29F371DC", "Ambulatory Surgical Centre"));
        appServiceTypeSelectList.add(new SelectOption("A21ADD49-820B-EA11-BE7D-000C29F371DC", "Nuclear Medicine (Assay)"));
        appServiceTypeSelectList.add(new SelectOption("F27DD5E2-C90C-EA11-BE7D-000C29F371DC", "Nuclear Medicine (Imaging)"));
        ParamUtil.setRequestAttr(request, "appServiceType", appServiceTypeSelectList);

        List<SelectOption> selectDraftApplicationSelectList = IaisCommonUtils.genNewArrayList();
        selectDraftApplicationSelectList.add(new SelectOption("Reload", "Reload"));
        selectDraftApplicationSelectList.add(new SelectOption("Delete", "Delete"));
        ParamUtil.setRequestAttr(request, "selectDraftApplication", selectDraftApplicationSelectList);

        List<SelectOption> selectApplicationSelectList = IaisCommonUtils.genNewArrayList();
        selectApplicationSelectList.add(new SelectOption("Recall", "Recall"));
        selectApplicationSelectList.add(new SelectOption("Appeal", "Appeal"));
        selectApplicationSelectList.add(new SelectOption("Withdraw", "Withdraw"));
        ParamUtil.setRequestAttr(request, "selectApplication", selectApplicationSelectList);
    }

    private void prepareLicSelectOption(HttpServletRequest request){
        List<SelectOption> LicenceStatusList = IaisCommonUtils.genNewArrayList();
        LicenceStatusList.add(new SelectOption("All", "All"));
        LicenceStatusList.add(new SelectOption("LICEST001", "Active"));
        LicenceStatusList.add(new SelectOption("LICEST002", "Ceased"));
        LicenceStatusList.add(new SelectOption("LICEST003", "Expired"));
        LicenceStatusList.add(new SelectOption("LICEST004", "Lapsed "));
        LicenceStatusList.add(new SelectOption("LICEST005", "Approved  "));
        LicenceStatusList.add(new SelectOption("LICEST006", "Suspended "));
        LicenceStatusList.add(new SelectOption("LICEST007", "Revoked "));
        ParamUtil.setRequestAttr(request, "licStatus", LicenceStatusList);

        List<SelectOption> LicenceTypeList = IaisCommonUtils.genNewArrayList();
        LicenceTypeList.add(new SelectOption("All", "All"));
        LicenceTypeList.add(new SelectOption("Clinical Laboratory", "Clinical Laboratory"));
        LicenceTypeList.add(new SelectOption("Blood Transfusion Service", "Blood Transfusion"));
        ParamUtil.setRequestAttr(request, "licType", LicenceTypeList);

        List<SelectOption> LicenceActionsList = IaisCommonUtils.genNewArrayList();
        LicenceActionsList.add(new SelectOption("Appeal", "Appeal"));
        ParamUtil.setRequestAttr(request, "licActions", LicenceActionsList);
    }

    private void clearParameter(String tabName){
        if (InboxConst.INTER_INBOX_APPLICATION_TAB.equals(tabName)){
            inboxParameter.setFilters(null);
            licenceParameter.setFilters(null);
        }else if (InboxConst.INTER_INBOX_LICENSE_TAB.equals(tabName)){
            appParameter.setFilters(null);
            inboxParameter.setFilters(null);
        }else if (InboxConst.INTER_INBOX_MESSAGE_TAB.equals(tabName)){
            appParameter.setFilters(null);
            licenceParameter.setFilters(null);
        }
    }
}
