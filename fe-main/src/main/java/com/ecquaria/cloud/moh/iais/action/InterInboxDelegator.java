package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.*;
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.constant.InboxConst;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import com.ecquaria.cloud.submission.client.App;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
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

    public void start(BaseProcessClass bpc) throws IllegalAccessException, ParseException {
        clearSessionAttr(bpc.request);
        IaisEGPHelper.clearSessionAttr(bpc.request,FilterParameter.class);
        LoginContext loginContext= (LoginContext)ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        AuditTrailDto auditTrailDto = inboxService.getLastLoginInfo(loginContext.getLoginId());
        InterInboxUserDto interInboxUserDto = new InterInboxUserDto();
        interInboxUserDto.setLicenseeId(loginContext.getLicenseeId());
        interInboxUserDto.setUserId(loginContext.getUserId());
        interInboxUserDto.setOrgId(loginContext.getOrgId());
        interInboxUserDto.setFunctionName(auditTrailDto.getFunctionName());
        interInboxUserDto.setLicenseNo(auditTrailDto.getLicenseNum());
        interInboxUserDto.setModuleName(auditTrailDto.getModule());
        interInboxUserDto.setLastLogin(Formatter.parseDateTime(auditTrailDto.getActionTime()));
        interInboxUserDto.setUserDomain(loginContext.getUserDomain());
        log.debug(StringUtil.changeForLog("Login role information --->> ##User-Id:"+interInboxUserDto.getUserId()+"### Licensee-Id:"+interInboxUserDto.getLicenseeId()));
        ParamUtil.setSessionAttr(bpc.request,InboxConst.INTER_INBOX_USER_INFO, interInboxUserDto);
        AuditTrailHelper.auditFunction("main-web", "main web");
    }

    public void initToPage(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("Step ---> initToPage"));
        HttpServletRequest request = bpc.request;
        String initpage = ParamUtil.getRequestString(request,"initPage");
        if (!StringUtil.isEmpty(initpage)){
            ParamUtil.setRequestAttr(request,"init_to_page",initpage);
        }else {
            ParamUtil.setRequestAttr(request, "init_to_page", "");
        }
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
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(request,InboxConst.INTER_INBOX_USER_INFO);
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
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(request,InboxConst.INTER_INBOX_USER_INFO);
        String msgId = ParamUtil.getMaskedString(request,InboxConst.MSG_ACTION_ID);
        inboxService.updateMsgStatusToRead(msgId);
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
            inboxSearchMap.put("msgSubject",'%'+msgSubject+'%');
        }else{
            inboxSearchMap.remove("msgSubject");
        }
        inboxParameter.setFilters(inboxSearchMap);
    }

    public void prepareDate(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> Into Message Page"));
        HttpServletRequest request = bpc.request;
        prepareMsgSelectOption(request);
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(request,InboxConst.INTER_INBOX_USER_INFO);
        SearchParam inboxParam = SearchResultHelper.getSearchParam(request,inboxParameter,true);
        inboxParam.addFilter("userId", interInboxUserDto.getLicenseeId(),true);
        inboxParam.addFilter(InboxConst.MESSAGE_STATUS, msgStatus,true);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.MESSAGE_QUERY_KEY,inboxParam);
        SearchResult inboxResult = inboxService.inboxDoQuery(inboxParam);
        List<InboxQueryDto> inboxQueryDtoList = inboxResult.getRows();
        for (InboxQueryDto inboxQueryDto:inboxQueryDtoList
                ) {
            List<InboxMsgMaskDto> inboxMsgMaskDtoList = inboxService.getInboxMaskEntity(inboxQueryDto.getId());
            for (InboxMsgMaskDto inboxMsgMaskDto:inboxMsgMaskDtoList){
                inboxQueryDto.setMsgContent(inboxQueryDto.getMsgContent().replaceAll(inboxMsgMaskDto.getParamValue(),
                        MaskUtil.maskValue(inboxMsgMaskDto.getParamName(),inboxMsgMaskDto.getParamValue())));
            }
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
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(request,InboxConst.INTER_INBOX_USER_INFO);
        SearchParam licParam = SearchResultHelper.getSearchParam(request,licenceParameter,true);
        licParam.addFilter("licenseeId",interInboxUserDto.getLicenseeId(),true);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.LICENCE_QUERY_KEY,licParam);
        SearchResult licResult = inboxService.licenceDoQuery(licParam);
        List<InboxLicenceQueryDto> inboxLicenceQueryDtoList = licResult.getRows();
        inboxLicenceQueryDtoList.stream().forEach(h -> {
            List<PremisesDto> premisesDtoList = inboxService.getPremisesByLicId(h.getId());
            List<String> addressList = IaisCommonUtils.genNewArrayList();
            for (PremisesDto premisesDto:premisesDtoList
                 ) {
                addressList.add(MiscUtil.getAddress(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode()));
                h.setPremisesDtoList(addressList);
            }

        });
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
        Date licStartDate = Formatter.parseDate(ParamUtil.getString(request, "fStartDate"));
        Date licEndDate = Formatter.parseDate(ParamUtil.getString(request, "eStartDate"));
        Date licfExpiryDate = Formatter.parseDate(ParamUtil.getString(request, "fExpiryDate"));
        Date liceExpiryDate = Formatter.parseDate(ParamUtil.getString(request, "eExpiryDate"));
        String fStartDate = Formatter.formatDateTime(licStartDate, SystemAdminBaseConstants.DATE_FORMAT);
        String eStartDate = Formatter.formatDateTime(licEndDate, SystemAdminBaseConstants.DATE_FORMAT);
        String fExpiryDate = Formatter.formatDateTime(licfExpiryDate, SystemAdminBaseConstants.DATE_FORMAT);
        String eExpiryDate = Formatter.formatDateTime(liceExpiryDate, SystemAdminBaseConstants.DATE_FORMAT);
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
        if (licStartDate != null && licEndDate != null){
            if (licStartDate.before(licEndDate)){
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
            }else{
                ParamUtil.setRequestAttr(request,InboxConst.LIC_DATE_ERR_MSG, "Licence Start Date From cannot be later than Licence Start Date To");
            }
        }
        if (licfExpiryDate != null && liceExpiryDate != null){
            if (licfExpiryDate.before(liceExpiryDate)){
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
            }else{
                ParamUtil.setRequestAttr(request,InboxConst.LIC_EXPIRY_ERR_MSG, "Licence Expiry Date From cannot be later than Licence Expiry Date To");
            }
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
        String licId = ParamUtil.getMaskedString(bpc.request, InboxConst.ACTION_ID_VALUE);
        Boolean result = inboxService.checkEligibility(licId);
        if (result){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohAppealApplication")
                    .append("?appealingFor=")
                    .append(MaskUtil.maskValue("appealingFor",licId))
                    .append("&type=licence");
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }else{
            ParamUtil.setRequestAttr(bpc.request,"licIsAppealed",result);
            ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,"An appeal has already been submitted for this licence/application");
        }
    }

    public void licToView(BaseProcessClass bpc) throws IOException {
        HttpServletRequest request = bpc.request;
        String licId = ParamUtil.getMaskedString(request, InboxConst.ACTION_ID_VALUE);
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohLicenceView")
                .append("?licenceId=")
                .append(licId);
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
        url.append(InboxConst.URL_HTTPS)
           .append(bpc.request.getServerName())
                .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohRequestForChange")
                .append("?licenceId=")
                .append(MaskUtil.maskValue("licenceId",licIdValue));
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
        bpc.response.sendRedirect(tokenUrl);
    }

    /**
     *
     * @param bpc
     *
     */
    public void licDoRenew(BaseProcessClass bpc) throws IOException {
        boolean result = true;
        String [] licIds = ParamUtil.getStrings(bpc.request, "licenceNo");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String tmp = "The selected licence(s) is/are not eligible for renewal: ";
        StringBuilder errorMessage = new StringBuilder();
        if(licIds != null){
            List<String> licIdValue = IaisCommonUtils.genNewArrayList();
            for(String item:licIds){
                licIdValue.add(ParamUtil.getMaskedString(bpc.request,item));
            }
            for (String licId:licIdValue
                 ) {
                errorMap = inboxService.checkRenewalStatus(licId);
                if(!(errorMap.isEmpty())){
                    String licenseNo = errorMap.get("errorMessage");
                    if(!StringUtil.isEmpty(licenseNo)){
                        if(StringUtil.isEmpty(errorMessage.toString())){
                            errorMessage.append(tmp + licenseNo);
                        }else{
                            errorMessage.append(", " + licenseNo);
                        }
                    }
                    result = false;
                }
            }
            if (result){
                StringBuilder url = new StringBuilder();
                url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                        .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohWithOutRenewal");
                ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_LIC_ID_LIST_ATTR, (Serializable) licIdValue);
                String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
                bpc.response.sendRedirect(tokenUrl);
            }else{
                ParamUtil.setRequestAttr(bpc.request,"licIsRenewed",result);
                if(StringUtil.isEmpty(errorMessage.toString())){
                    errorMessage.append("There is already a pending application for the selected licence");
                }
                ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,errorMessage.toString());
            }
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
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohCessationApplication");
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
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(request,InboxConst.INTER_INBOX_USER_INFO);
        SearchParam appParam = SearchResultHelper.getSearchParam(request,appParameter,true);
        appParam.addFilter("licenseeId", interInboxUserDto.getLicenseeId(),true);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.APPLICATION_QUERY_KEY,appParam);
        SearchResult appResult = inboxService.appDoQuery(appParam);
        List<InboxAppQueryDto> inboxAppQueryDtoList = appResult.getRows();
        for (InboxAppQueryDto appDto:inboxAppQueryDtoList) {
            String serviceName = "N/A";
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
//        ParamUtil.setRequestAttr(bpc.request,"appCannotRecall", false);
    }

    public void appDoSearch(BaseProcessClass bpc) throws ParseException {
        log.debug(StringUtil.changeForLog("Step ---> appDoSearch"));
        HttpServletRequest request = bpc.request;
        Map<String,Object> appSearchMap = IaisCommonUtils.genNewHashMap();
        String applicationType = ParamUtil.getString(request,"appTypeSelect");
        String serviceType = ParamUtil.getString(request,"appServiceType");
        String applicationStatus = ParamUtil.getString(request,"appStatusSelect");
        String applicationNo = ParamUtil.getString(request,"appNoPath");
        Date startAppDate = Formatter.parseDate(ParamUtil.getString(request, "esd"));
        Date endAppDate = Formatter.parseDate(ParamUtil.getString(request, "eed"));
        String createDtStart = Formatter.formatDateTime(startAppDate, SystemAdminBaseConstants.DATE_FORMAT);
        String createDtEnd = Formatter.formatDateTime(endAppDate, SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
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
        if (startAppDate != null && endAppDate != null){
            if(startAppDate.before(endAppDate)){
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
            }else{
                ParamUtil.setRequestAttr(request,InboxConst.APP_DATE_ERR_MSG, "Date Submitted From cannot be later than Date Submitted To");
            }
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
        String appId = ParamUtil.getMaskedString(request, InboxConst.ACTION_ID_VALUE);
        Boolean result = inboxService.checkEligibility(appId);
        if (result){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohAppealApplication")
                    .append("?appealingFor=")
                    .append(MaskUtil.maskValue("appealingFor",appId))
                    .append("&type=application");
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }else{
            ParamUtil.setRequestAttr(bpc.request,"appIsAppealed",result);
            ParamUtil.setRequestAttr(bpc.request,InboxConst.APP_RECALL_RESULT,"An appeal has already been submitted for this licence/application");
        }
    }

    public void appDoWithDraw(BaseProcessClass bpc) throws IOException {
        HttpServletRequest request = bpc.request;
        String appId = ParamUtil.getMaskedString(request, InboxConst.ACTION_ID_VALUE);
        String appNo = ParamUtil.getString(request, InboxConst.ACTION_NO_VALUE);
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohWithdrawalApplication")
                .append("?withdrawAppId=")
                .append(MaskUtil.maskValue("withdrawAppId",appId))
                .append("&withdrawAppNo=")
                .append(MaskUtil.maskValue("withdrawAppNo",appNo));
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
        bpc.response.sendRedirect(tokenUrl);
    }

    public void appDoDraft(BaseProcessClass bpc) throws IOException {
        log.debug("The prepareEdit start ...");
        HttpServletRequest request = bpc.request;
        String appNo = ParamUtil.getString(request, InboxConst.ACTION_NO_VALUE);
        if(InboxConst.APP_DO_DRAFT_TYPE_RFC.equals(ParamUtil.getString(request, InboxConst.ACTION_TYPE_VALUE))){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohRequestForChange/prepareDraft")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }else if(InboxConst.APP_DO_DRAFT_TYPE_RENEW.equals(ParamUtil.getString(request, InboxConst.ACTION_TYPE_VALUE))){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohWithOutRenewal")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }else if(InboxConst.APP_DO_DRAFT_TYPE_APPEAL.equals(ParamUtil.getString(request, InboxConst.ACTION_TYPE_VALUE))){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohAppealApplication")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }
        else {
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohNewApplication")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);}
    }

    public void appDoDelete(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> appDoDelete start..."));
        String draft = ParamUtil.getString(bpc.request,InboxConst.ACTION_NO_VALUE);
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
        String appId = ParamUtil.getMaskedString(request, InboxConst.ACTION_ID_VALUE);
        String appNo = ParamUtil.getString(request, InboxConst.ACTION_NO_VALUE);
        RecallApplicationDto recallApplicationDto = new RecallApplicationDto();
        recallApplicationDto.setAppId(appId);
        recallApplicationDto.setAppNo(appNo);
        Boolean recallResult = inboxService.recallApplication(recallApplicationDto);
        ParamUtil.setRequestAttr(request,"appCannotRecall", recallResult);
        ParamUtil.setRequestAttr(request,InboxConst.APP_RECALL_RESULT, "The application can not recall");
    }

    public void appToAppView(BaseProcessClass bpc) throws IOException {
        String appNo = ParamUtil.getString(bpc.request, InboxConst.ACTION_NO_VALUE);
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohNewApplication/1/InboxToPreview")
                .append("?appNo=")
                .append(MaskUtil.maskValue("appNo",appNo));
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
        bpc.response.sendRedirect(tokenUrl);
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
        inboxServiceSelectList.add(new SelectOption("All", "All"));
        inboxServiceSelectList.add(new SelectOption("34F99D15-820B-EA11-BE7D-000C29F371DC", "Blood Banking"));
        inboxServiceSelectList.add(new SelectOption("7B450178-C70C-EA11-BE7D-000C29F371DC", "Tissue Banking"));
        inboxServiceSelectList.add(new SelectOption("35F99D15-820B-EA11-BE7D-000C29F371DC", "Clinical Laboratory"));
        inboxServiceSelectList.add(new SelectOption("A21ADD49-820B-EA11-BE7D-000C29F371DC", "Nuclear Medicine (Assay)"));
        inboxServiceSelectList.add(new SelectOption("F27DD5E2-C90C-EA11-BE7D-000C29F371DC", "Nuclear Medicine (Imaging)"));
        ParamUtil.setRequestAttr(request, "inboxServiceSelect", inboxServiceSelectList);

        List<SelectOption> inboxTypSelectList = IaisCommonUtils.genNewArrayList();
        inboxTypSelectList.add(new SelectOption("All", "All"));
        inboxTypSelectList.add(new SelectOption(MessageConstants.MESSAGE_TYPE_NOTIFICATION, "Notification"));
        inboxTypSelectList.add(new SelectOption(MessageConstants.MESSAGE_TYPE_ANNONUCEMENT, "Announcement"));
        inboxTypSelectList.add(new SelectOption(MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED, "Action Required"));
        ParamUtil.setRequestAttr(request, "inboxTypeSelect", inboxTypSelectList);
    }

    private void prepareAppSelectOption(HttpServletRequest request){
        List<SelectOption> applicationTypeSelectList = IaisCommonUtils.genNewArrayList();
        applicationTypeSelectList.add(new SelectOption("All", "All"));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_RENEWAL, "Renewal"));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_APPEAL, "Appeal"));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL, "Withdrawal"));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_CESSATION, "Cessation "));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE, "Request For Change"));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION, "New Licence Application"));
        ParamUtil.setRequestAttr(request, "appTypeSelect", applicationTypeSelectList);

        List<SelectOption> applicationStatusSelectList = IaisCommonUtils.genNewArrayList();
        applicationStatusSelectList.add(new SelectOption("All", "All"));
        applicationStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_DRAFT, "Draft"));
        applicationStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_ROUTE_BACK, "Rollback"));
        applicationStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_APPROVED, "Approved"));
        applicationStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING, "Pending"));
        ParamUtil.setRequestAttr(request, "appStatusSelect", applicationStatusSelectList);

        List<SelectOption> appServiceStatusSelectList = IaisCommonUtils.genNewArrayList();
        appServiceStatusSelectList.add(new SelectOption("All", "All"));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_DRAFT, "Draft"));
        appServiceStatusSelectList.add(new SelectOption("APST050", "Recalled"));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT, "Pending Payment"));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION, "Pending Clarification"));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING, "Pending Screening"));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION, "Pending Inspection"));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_FE_APPOINTMENT_SCHEDULING, "Pending Appointment Scheduling"));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING, "Pending Appointment Re-Scheduling"));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION, "Pending NC Rectification"));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01, "Pending Approval"));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_APPROVED, "Approved"));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_REJECTED, "Rejected"));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_WITHDRAWN, "Withdrawn"));
        ParamUtil.setRequestAttr(request, "appStatusSelect", appServiceStatusSelectList);

        List<SelectOption> appServiceTypeSelectList = IaisCommonUtils.genNewArrayList();
        appServiceTypeSelectList.add(new SelectOption("All", "All"));
        appServiceTypeSelectList.add(new SelectOption("34F99D15-820B-EA11-BE7D-000C29F371DC", "Blood Banking"));
        appServiceTypeSelectList.add(new SelectOption("35F99D15-820B-EA11-BE7D-000C29F371DC", "Clinical Laboratory"));
        appServiceTypeSelectList.add(new SelectOption("A11ADD49-820B-EA11-BE7D-000C29F371DC", "Radiological Service"));
        appServiceTypeSelectList.add(new SelectOption("7B450178-C70C-EA11-BE7D-000C29F371DC", "Tissue Banking"));
        appServiceTypeSelectList.add(new SelectOption("A21ADD49-820B-EA11-BE7D-000C29F371DC", "Nuclear Medicine (Assay)"));
        appServiceTypeSelectList.add(new SelectOption("F27DD5E2-C90C-EA11-BE7D-000C29F371DC", "Nuclear Medicine (Imaging)"));
        ParamUtil.setRequestAttr(request, "appServiceType", appServiceTypeSelectList);

        List<SelectOption> selectDraftApplicationSelectList = IaisCommonUtils.genNewArrayList();
        selectDraftApplicationSelectList.add(new SelectOption(InboxConst.DRAFT_APP_ACTION_EDIT, InboxConst.DRAFT_APP_ACTION_EDIT));
        selectDraftApplicationSelectList.add(new SelectOption(InboxConst.DRAFT_APP_ACTION_DELETE, InboxConst.DRAFT_APP_ACTION_DELETE));
        ParamUtil.setRequestAttr(request, "selectDraftApplication", selectDraftApplicationSelectList);

        List<SelectOption> selectApplicationSelectList = IaisCommonUtils.genNewArrayList();
        selectApplicationSelectList.add(new SelectOption(InboxConst.APP_ACTION_RECALL, InboxConst.APP_ACTION_RECALL));
        selectApplicationSelectList.add(new SelectOption(InboxConst.APP_ACTION_APPEAL, InboxConst.APP_ACTION_APPEAL));
        selectApplicationSelectList.add(new SelectOption(InboxConst.APP_ACTION_WITHDRAW, InboxConst.APP_ACTION_WITHDRAW));
        ParamUtil.setRequestAttr(request, "selectApplication", selectApplicationSelectList);

        List<SelectOption> selectWithdrawalSelectList = IaisCommonUtils.genNewArrayList();
        selectWithdrawalSelectList.add(new SelectOption(InboxConst.APP_ACTION_RECALL, InboxConst.APP_ACTION_RECALL));
        ParamUtil.setRequestAttr(request, "selectWithdrawApplication", selectWithdrawalSelectList);

        List<SelectOption> selectAppealSelectList = IaisCommonUtils.genNewArrayList();
        selectWithdrawalSelectList.add(new SelectOption(InboxConst.APP_ACTION_RECALL, InboxConst.APP_ACTION_RECALL));
        selectWithdrawalSelectList.add(new SelectOption(InboxConst.APP_ACTION_WITHDRAW, InboxConst.APP_ACTION_WITHDRAW));
        ParamUtil.setRequestAttr(request, "selectWithdrawApplication", selectAppealSelectList);

        List<SelectOption> selectApproveOrRejectSelectList = IaisCommonUtils.genNewArrayList();
        selectApproveOrRejectSelectList.add(new SelectOption(InboxConst.APP_ACTION_RECALL, InboxConst.APP_ACTION_RECALL));
        selectApproveOrRejectSelectList.add(new SelectOption(InboxConst.APP_ACTION_APPEAL, InboxConst.APP_ACTION_APPEAL));
        ParamUtil.setRequestAttr(request, "selectApproveOrRejectSelectList", selectApproveOrRejectSelectList);
    }

    private void prepareLicSelectOption(HttpServletRequest request){
        List<SelectOption> LicenceStatusList = IaisCommonUtils.genNewArrayList();
        LicenceStatusList.add(new SelectOption("All", "All"));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_ACTIVE, "Active"));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_EXPIRY, "Expired"));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_CEASED, "Ceased"));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_LAPSED, "Lapsed"));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_APPROVED, "Approved"));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_SUSPENDED, "Suspended"));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_REVOKED, "Revoked"));
        ParamUtil.setRequestAttr(request, "licStatus", LicenceStatusList);

        List<SelectOption> LicenceTypeList = IaisCommonUtils.genNewArrayList();
        LicenceTypeList.add(new SelectOption("All", "All"));
        LicenceTypeList.add(new SelectOption("Clinical Laboratory", "Clinical Laboratory"));
        LicenceTypeList.add(new SelectOption("Blood Transfusion Service", "Blood Transfusion"));
        ParamUtil.setRequestAttr(request, "licType", LicenceTypeList);

        List<SelectOption> LicenceActionsList = IaisCommonUtils.genNewArrayList();
        LicenceActionsList.add(new SelectOption("Appeal", "Appeal"));
        ParamUtil.setRequestAttr(request, "licActions", LicenceActionsList);

        List<SelectOption> LicenceNoActionsList = IaisCommonUtils.genNewArrayList();
        ParamUtil.setRequestAttr(request, "licNoActions", LicenceNoActionsList);
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

    private void clearSessionAttr(HttpServletRequest request){
        ParamUtil.setSessionAttr(request,InboxConst.INBOX_PARAM, null);
        ParamUtil.setSessionAttr(request,InboxConst.APP_PARAM, null);
        ParamUtil.setSessionAttr(request,InboxConst.LIC_PARAM, null);
        ParamUtil.setSessionAttr(request,InboxConst.INTER_INBOX_USER_INFO, null);
    }
}
