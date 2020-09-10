package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.MasterCodePair;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxMsgMaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.HalpSearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
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
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private InboxService inboxService;
    @Autowired
    private LicenceInboxClient licenceInboxClient;
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
        HttpServletRequest request = bpc.request;
        HalpSearchResultHelper.getSearchParam(request,"inboxMsg",true);
    }

    public void msgDoPage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam inboxParam = HalpSearchResultHelper.getSearchParam(request,"inboxMsg");
        HalpSearchResultHelper.doPage(request,inboxParam);
    }

    public void msgDoSort(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam inboxParam = HalpSearchResultHelper.getSearchParam(request,"inboxMsg");
        HalpSearchResultHelper.doSort(request,inboxParam);
    }

    public void msgToArchive(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        prepareMsgSelectOption(request);
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(request,InboxConst.INTER_INBOX_USER_INFO);
        SearchParam inboxParam = null;
        String page = ParamUtil.getRequestString(request,"msg_action_type");
        if("msgToArchive".equals(page)){
            inboxParam = HalpSearchResultHelper.getSearchParam(request,"inboxMsg",true);
        }else{
            inboxParam = HalpSearchResultHelper.getSearchParam(request,"inboxMsg");
        }
//        SearchParam inboxParam = HalpSearchResultHelper.getSearchParam(request,"inboxMsg");
        inboxParam.addFilter("userId", interInboxUserDto.getLicenseeId(),true);
        inboxParam.addFilter(InboxConst.MESSAGE_STATUS, msgArchiverStatus,true);
        List<SelectOption> inboxTypes = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_INBOx_MESSAGE_TYPE);
        MasterCodePair mcp = new MasterCodePair("message_type", "message_type_desc", inboxTypes);
        inboxParam.addMasterCode(mcp);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.MESSAGE_QUERY_KEY,inboxParam);
        SearchResult inboxResult = inboxService.inboxDoQuery(inboxParam);
        List<InboxQueryDto> inboxQueryDtoList = inboxResult.getRows();
        for (InboxQueryDto inboxQueryDto:inboxQueryDtoList
                ) {
            List<InboxMsgMaskDto> inboxMsgMaskDtoList = inboxService.getInboxMaskEntity(inboxQueryDto.getId());
            for (InboxMsgMaskDto inboxMsgMaskDto:inboxMsgMaskDtoList){
                inboxQueryDto.setMsgContent(inboxQueryDto.getMsgContent().replaceAll("="+inboxMsgMaskDto.getParamValue(),
                        "="+MaskUtil.maskValue(inboxMsgMaskDto.getParamName(),inboxMsgMaskDto.getParamValue())));
            }
        }
        if(!StringUtil.isEmpty(inboxResult)){
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
        String MsgPage = ParamUtil.getRequestString(request,"msg_page_action");
        String msgType = ParamUtil.getMaskedString(request,InboxConst.MSG_PAGE_TYPE);
        if(msgId==null){
            msgId= (String) ParamUtil.getSessionAttr(request,InboxConst.MSG_ACTION_ID);
        }
        if(msgType==null){
            msgType= (String) ParamUtil.getSessionAttr(request,InboxConst.MSG_PAGE_TYPE);
        }
        if ("msg_view".equals(MsgPage)){
            if (MessageConstants.MESSAGE_TYPE_NOTIFICATION.equals(msgType)){
                inboxService.updateMsgStatusTo(msgId,MessageConstants.MESSAGE_STATUS_READ);
            }else{
                inboxService.updateMsgStatusTo(msgId,MessageConstants.MESSAGE_STATUS_UNRESPONSE);
            }
        }
        String msgContent = ParamUtil.getMaskedString(request,InboxConst.CRUD_ACTION_VALUE);
        if(msgContent==null){
            msgContent= (String) ParamUtil.getSessionAttr(request,InboxConst.MESSAGE_CONTENT);
            msgContent = StringEscapeUtils.unescapeHtml4(msgContent.replaceAll("<.span*?>", ""));
        }
        ParamUtil.setRequestAttr(request,InboxConst.MESSAGE_CONTENT, msgContent);
        ParamUtil.setSessionAttr(request,AppConsts.SESSION_INTER_INBOX_MESSAGE_ID,msgId);
        ParamUtil.setRequestAttr(request,"msg_page_view", MsgPage);
        ParamUtil.setSessionAttr(request,"msgPageType",msgType);
        setNumInfoToRequest(request,interInboxUserDto);
    }

    public void msgDoSearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String inboxType = ParamUtil.getString(request,InboxConst.MESSAGE_TYPE);
        String inboxService = ParamUtil.getString(request,InboxConst.MESSAGE_SERVICE);
        String msgSubject = ParamUtil.getString(request,InboxConst.MESSAGE_SEARCH);
        SearchParam inboxMsgParam = HalpSearchResultHelper.getSearchParam(request,"inboxMsg",true);
        if(inboxType != null){
            if (inboxType.equals(InboxConst.SEARCH_ALL)){
                inboxMsgParam.removeFilter("messageType");
            }else{
                inboxMsgParam.addFilter("messageType",inboxType,true);
            }
        }
        if(inboxService != null){
            if (inboxService.equals(InboxConst.SEARCH_ALL)){
                inboxMsgParam.removeFilter("interService");
            }else{
                inboxMsgParam.addFilter("interService","%" + inboxService + "%",true);
            }
        }
        if(msgSubject != null){
            inboxMsgParam.addFilter("msgSubject",'%'+msgSubject+'%',true);
        }else{
            inboxMsgParam.removeFilter("msgSubject");
        }
    }

    public void prepareDate(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> Into Message Page"));
        HttpServletRequest request = bpc.request;
        prepareMsgSelectOption(request);
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(request,InboxConst.INTER_INBOX_USER_INFO);
        SearchParam inboxParam = null;
        String page = ParamUtil.getRequestString(request,"msg_action_type");
        if("toMsgPage".equals(page)){
            inboxParam = HalpSearchResultHelper.getSearchParam(request,"inboxMsg",true);
        }else{
            inboxParam = HalpSearchResultHelper.getSearchParam(request,"inboxMsg");
        }
        inboxParam.addFilter("userId", interInboxUserDto.getLicenseeId(),true);
        inboxParam.addFilter(InboxConst.MESSAGE_STATUS, msgStatus,true);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.MESSAGE_QUERY_KEY,inboxParam);
        List<SelectOption> inboxTypes = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_INBOx_MESSAGE_TYPE);
        MasterCodePair mcp = new MasterCodePair("message_type", "message_type_desc", inboxTypes);
        inboxParam.addMasterCode(mcp);
        SearchResult inboxResult = inboxService.inboxDoQuery(inboxParam);
        log.info("inboxResult start=>>>>>>>>");
        log.info(StringUtil.changeForLog("inboxResult ==>>> row " + inboxResult.getRowCount()) );

        List<InboxQueryDto> inboxQueryDtoList = inboxResult.getRows();
        for (InboxQueryDto inboxQueryDto:inboxQueryDtoList) {
            List<InboxMsgMaskDto> inboxMsgMaskDtoList = inboxService.getInboxMaskEntity(inboxQueryDto.getId());
            log.info(StringUtil.changeForLog("inboxMsgMaskDtoList ==>>> row " + inboxMsgMaskDtoList.size()));

            int i = 0;
            for (InboxMsgMaskDto inboxMsgMaskDto:inboxMsgMaskDtoList){

                log.info(StringUtil.changeForLog("inboxMsgMaskDtoList ==>>> count " + i++));

                inboxQueryDto.setMsgContent(inboxQueryDto.getMsgContent().replaceAll("="+inboxMsgMaskDto.getParamValue(),
                        "="+MaskUtil.maskValue(inboxMsgMaskDto.getParamName(),inboxMsgMaskDto.getParamValue())));
            }
        }
        log.info("inboxResult end=>>>>>>>>");
        if(!StringUtil.isEmpty(inboxResult)){
            ParamUtil.setSessionAttr(bpc.request,"licence_err_list",null);
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
        SearchParam licParam = HalpSearchResultHelper.getSearchParam(request,"inboxLic");
        licParam.addFilter("licenseeId",interInboxUserDto.getLicenseeId(),true);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.LICENCE_QUERY_KEY,licParam);
        List<SelectOption> licStatus = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_LICENCE_STATUS);
        MasterCodePair mcp_status = new MasterCodePair("lic.status", "LIC_STATUS_DESC", licStatus);
        licParam.addMasterCode(mcp_status);
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
        SearchParam inboxLicParam = HalpSearchResultHelper.getSearchParam(request,"inboxLic",true);
        if(licenceNo != null){
            inboxLicParam.addFilter("licNo",'%'+licenceNo+'%',true);
        }else{
            inboxLicParam.removeFilter("licNo");
        }
        if(serviceType == null || serviceType.equals(InboxConst.SEARCH_ALL)){
            inboxLicParam.removeFilter("serviceType");
        }else {
            inboxLicParam.addFilter("serviceType",serviceType,true);
        }
        if(licStatus == null || licStatus.equals(InboxConst.SEARCH_ALL)){
            inboxLicParam.removeFilter("licStatus");
        }else{
            inboxLicParam.addFilter("licStatus",licStatus,true);
        }
        if (licStartDate != null && licEndDate != null){
            if (licStartDate.compareTo(licEndDate)<=0){
                if(!StringUtil.isEmpty(fStartDate)){
                    inboxLicParam.addFilter("fStartDate",fStartDate,true);
                }else{
                    inboxLicParam.removeFilter("fStartDate");
                }
                if(!StringUtil.isEmpty(eStartDate)){
                    inboxLicParam.addFilter("eStartDate",eStartDate,true);
                }else{
                    inboxLicParam.removeFilter("eStartDate");
                }
            }
            else{
                ParamUtil.setRequestAttr(request,InboxConst.LIC_DATE_ERR_MSG, "Licence Start Date From cannot be later than Licence Start Date To");
            }
        }else{
            if(!StringUtil.isEmpty(fStartDate)){
                inboxLicParam.addFilter("fStartDate",fStartDate,true);
            }else{
                inboxLicParam.removeFilter("fStartDate");
            }
            if(!StringUtil.isEmpty(eStartDate)){
                inboxLicParam.addFilter("eStartDate",eStartDate,true);
            }else{
                inboxLicParam.removeFilter("eStartDate");
            }
        }
        if (licfExpiryDate != null && liceExpiryDate != null){
            if (licfExpiryDate.compareTo(liceExpiryDate)<=0){
                if(!StringUtil.isEmpty(fExpiryDate)){
                    inboxLicParam.addFilter("fExpiryDate",fExpiryDate,true);
                }else{
                    inboxLicParam.removeFilter("fExpiryDate");
                }
                if(!StringUtil.isEmpty(eExpiryDate)){
                    inboxLicParam.addFilter("eExpiryDate",eExpiryDate,true);
                }else{
                    inboxLicParam.removeFilter("eExpiryDate");
                }
            }else{
                ParamUtil.setRequestAttr(request,InboxConst.LIC_EXPIRY_ERR_MSG, "Licence Expiry Date From cannot be later than Licence Expiry Date To");
            }
        }else{
            if(!StringUtil.isEmpty(fExpiryDate)){
                inboxLicParam.addFilter("fExpiryDate",fExpiryDate,true);
            }else{
                inboxLicParam.removeFilter("fExpiryDate");
            }
            if(!StringUtil.isEmpty(eExpiryDate)){
                inboxLicParam.addFilter("eExpiryDate",eExpiryDate,true);
            }else{
                inboxLicParam.removeFilter("eExpiryDate");
            }
        }
    }

    public void licDoPage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam inboxParam = HalpSearchResultHelper.getSearchParam(request,"inboxLic");
        HalpSearchResultHelper.doPage(request,inboxParam);
    }

    public void licDoSort(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam inboxParam = HalpSearchResultHelper.getSearchParam(request,"inboxLic");
        HalpSearchResultHelper.doSort(request,inboxParam);
    }

    public void licDoAppeal(BaseProcessClass bpc) throws IOException {
        HttpServletRequest request = bpc.request;
        String licMaskId = ParamUtil.getString(bpc.request, "licenceNo");
        String licId = ParamUtil.getMaskedString(bpc.request,licMaskId);
        LicenceDto licenceDto = licenceInboxClient.getLicBylicId(licId).getEntity();
        boolean isActive = licenceDto != null && ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceDto.getStatus());
        if(!isActive){
            ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,MessageUtil.getMessageDesc("INBOX_ACK011"));
            List<String> licIdValues = IaisCommonUtils.genNewArrayList();
            licIdValues.add(licId);
            ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licIdValues);
            ParamUtil.setRequestAttr(bpc.request,"licIsAppealed",Boolean.FALSE);
            return;
        }
        List<ApplicationSubDraftDto> draftByLicAppId = inboxService.getDraftByLicAppId(licId);
        if(!draftByLicAppId.isEmpty()){
            String isNeedDelete = bpc.request.getParameter("isNeedDelete");
            StringBuilder stringBuilder=new StringBuilder();
            for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                stringBuilder.append(applicationSubDraftDto.getDraftNo()).append(' ');
            }
            if("delete".equals(isNeedDelete)){
                for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                    inboxService.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                }
            }else {
                String ack030 = MessageUtil.getMessageDesc("ACK030");
                String replace = ack030.replace("<draft application no>", stringBuilder.toString());
                bpc.request.setAttribute("draftByLicAppId",replace);
                bpc.request.setAttribute("isAppealShow","1");
                List<String> licIdValues = IaisCommonUtils.genNewArrayList();
                licIdValues.add(licId);
                ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licIdValues);
                return;
            }

        }
        Boolean result = inboxService.checkEligibility(licId);
        Map<String, String> map = inboxService.appealIsApprove(licId, "licence");
        if (result&&map.isEmpty()){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohAppealApplication")
                    .append("?appealingFor=")
                    .append(MaskUtil.maskValue("appealingFor",licId))
                    .append("&type=licence");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }else{
            if(!result){
                ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,"An appeal has already been submitted for this licence/application");
            }else if(!map.isEmpty()){
                ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,"There is already a pending application for the selected licence");
            }
            ParamUtil.setRequestAttr(bpc.request,"licIsAppealed",result);
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
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        //65741
        request.getSession().removeAttribute("AppSubmissionDto");
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
        if(licIdValue != null){
            List<String> licIdValues = IaisCommonUtils.genNewArrayList();
            licIdValues.add(licIdValue);
            ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licIdValues);
            Map<String, String> errorMap = inboxService.checkRfcStatus(licIdValue);
            List<ApplicationSubDraftDto> draftByLicAppId = inboxService.getDraftByLicAppId(licIdValue);
            if(!draftByLicAppId.isEmpty()){
                String isNeedDelete = bpc.request.getParameter("isNeedDelete");
                StringBuilder stringBuilder=new StringBuilder();
                for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                    stringBuilder.append(applicationSubDraftDto.getDraftNo()).append(' ');
                }
                if("delete".equals(isNeedDelete)){
                    for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                        inboxService.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                    }
                }else {
                    String ack030 = MessageUtil.getMessageDesc("ACK030");
                    String replace = ack030.replace("<draft application no>", stringBuilder.toString());
                    bpc.request.setAttribute("draftByLicAppId",replace);
                    bpc.request.setAttribute("isShow","1");
                    return;
                }
            }

            if(errorMap.isEmpty()){
                StringBuilder url = new StringBuilder();
                url.append(InboxConst.URL_HTTPS)
                        .append(bpc.request.getServerName())
                        .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohRequestForChange")
                        .append("?licenceId=")
                        .append(MaskUtil.maskValue("licenceId",licIdValue));
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                bpc.response.sendRedirect(tokenUrl);
            }else{
                ParamUtil.setRequestAttr(bpc.request,"licIsAmend",Boolean.TRUE);
                ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,errorMap.get("errorMessage"));
            }
        }
    }

    /**
     *
     * @param bpc
     *
     */
    public void licDoRenew(BaseProcessClass bpc) throws IOException  {
        boolean result = true;
        String [] licIds = ParamUtil.getStrings(bpc.request, "licenceNo");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String tmp = MessageUtil.getMessageDesc("INBOX_ACK015");
        StringBuilder errorMessage = new StringBuilder();
        if(licIds != null){
            List<String> licIdValue = IaisCommonUtils.genNewArrayList();
            for(String item:licIds){
                licIdValue.add(ParamUtil.getMaskedString(bpc.request,item));
            }
            if(licIdValue.size()==1){
                List<ApplicationSubDraftDto> draftByLicAppId = inboxService.getDraftByLicAppId(licIdValue.get(0));
                String isNeedDelete = bpc.request.getParameter("isNeedDelete");
                if(!draftByLicAppId.isEmpty()){
                    StringBuilder stringBuilder=new StringBuilder();
                    for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                        stringBuilder.append(applicationSubDraftDto.getDraftNo()).append(' ');
                    }
                    if("delete".equals(isNeedDelete)){
                        for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                            inboxService.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                        }
                    }else {
                        String ack030 = MessageUtil.getMessageDesc("ACK030");
                        String replace = ack030.replace("<draft application no>", stringBuilder.toString());
                        bpc.request.setAttribute("draftByLicAppId",replace);
                        bpc.request.setAttribute("isRenewShow","1");
                        ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licIdValue);
                        return;
                    }
                }
            }
            ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licIdValue);
            for (String licId:licIdValue
                 ) {
                errorMap = inboxService.checkRenewalStatus(licId);
                if(!(errorMap.isEmpty())){
                    String licenseNo = errorMap.get("errorMessage");
                    if(!StringUtil.isEmpty(licenseNo)){
                        if(StringUtil.isEmpty(errorMessage.toString())){
                            errorMessage.append(tmp).append(licenseNo);
                        }else{
                            errorMessage.append(", ").append(licenseNo);
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
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                bpc.response.sendRedirect(tokenUrl);
            }else{
                ParamUtil.setRequestAttr(bpc.request,"licIsRenewed",result);
                if(StringUtil.isEmpty(errorMessage.toString())){
                    String errorMessage2 = errorMap.get("errorMessage2");
                    if(StringUtil.isEmpty(errorMessage2)){
                        //RFC_ERR011
                        errorMessage.append(MessageUtil.getMessageDesc("RFC_ERR011"));
                    }else{
                        errorMessage.append(errorMessage2);
                    }
                }
                ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,errorMessage.toString());
            }
        }
    }

    public void licDoCease(BaseProcessClass bpc) throws IOException {
        String cessationError = null ;
        String [] licIds = ParamUtil.getStrings(bpc.request, "licenceNo");
        List<String> licIdValue = IaisCommonUtils.genNewArrayList();
        ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licIdValue);
        boolean result= false;
        if(licIds != null){
            for (String item : licIds) {
                licIdValue.add(ParamUtil.getMaskedString(bpc.request, item));
            }
            for(String licId : licIdValue){
                LicenceDto licenceDto = licenceInboxClient.getLicBylicId(licId).getEntity();
                if(licenceDto==null){
                    cessationError = MessageUtil.getMessageDesc("INBOX_ACK011");
                    ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_CEASED_ERR_RESULT,Boolean.TRUE);
                    bpc.request.setAttribute("cessationError",cessationError);
                    ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licIdValue);
                    return ;
                }
            }

            Map<String,Boolean> resultMap = inboxService.listResultCeased(licIdValue);
            for(Map.Entry<String,Boolean> entry : resultMap.entrySet()){
                if (!entry.getValue()){
                    result = true;
                    break;
                }
            }
            List<ApplicationSubDraftDto> draftByLicAppId = inboxService.getDraftByLicAppId(licIdValue.get(0));
            String isNeedDelete = bpc.request.getParameter("isNeedDelete");
            if(!draftByLicAppId.isEmpty()){
                StringBuilder stringBuilder=new StringBuilder();
                for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                    stringBuilder.append(applicationSubDraftDto.getDraftNo()).append(' ');
                }
                if("delete".equals(isNeedDelete)){
                    for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                        inboxService.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                    }
                }else {
                    String ack030 = MessageUtil.getMessageDesc("ACK030");
                    String replace = ack030.replace("<draft application no>", stringBuilder.toString());
                    bpc.request.setAttribute("draftByLicAppId",replace);
                    bpc.request.setAttribute("isCeaseShow","1");
                    bpc.request.setAttribute("appealApplication",licIdValue.get(0));
                    return;
                }
            }
            if(result) {
                cessationError = MessageUtil.getMessageDesc("CESS_ERR002");
                bpc.request.setAttribute("cessationError",cessationError);
                ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_CEASED_ERR_RESULT,Boolean.TRUE);
                ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licIdValue);
            }else{
                ParamUtil.setSessionAttr(bpc.request, "licIds", (Serializable) licIdValue);
                StringBuilder url = new StringBuilder();
                url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                        .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohCessationApplication");
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                bpc.response.sendRedirect(tokenUrl);
            }
        }
    }

    public void licDoPrint(BaseProcessClass bpc) throws IOException {
        String [] licIds = ParamUtil.getStrings(bpc.request, "licenceNo");
        List<String> licIdValue = IaisCommonUtils.genNewArrayList();
        for (String item : licIds) {
            licIdValue.add(ParamUtil.getMaskedString(bpc.request, item));
        }
        if(!IaisCommonUtils.isEmpty(licIdValue)) {
            ParamUtil.setSessionAttr(bpc.request, "lic-print-Ids", (Serializable) licIdValue);
        }
    }

    /**
     *
     * @param bpc
     *
     * >>>>>>>>Application Inbox<<<<<<<<
     */
    public void toAppPage(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request,"licence_err_list",null);
        log.debug(StringUtil.changeForLog("Step ---> toAppPage"));
        HttpServletRequest request = bpc.request;
        prepareAppSelectOption(request);
        /**
         * Application SearchResult
         */
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(request,InboxConst.INTER_INBOX_USER_INFO);
        SearchParam appParam = HalpSearchResultHelper.getSearchParam(request,"inboxApp");
        String repalceService = getRepalceService();
        if(StringUtil.isEmpty(repalceService)){
            appParam.addFilter("repalceService", "null",true);
        }else {
            appParam.addFilter("repalceService", repalceService,true);
        }
        appParam.addFilter("licenseeId", interInboxUserDto.getLicenseeId(),true);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.APPLICATION_QUERY_KEY,appParam);
        List<SelectOption> appTypes = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_APP_TYPE);
        List<SelectOption> appStatus = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_APP_STATUS);
        MasterCodePair mcp = new MasterCodePair("app_type", "app_type_desc", appTypes);
        MasterCodePair mcp_service = new MasterCodePair("app_type", "app_type_desc", appTypes);
        MasterCodePair mcp_status = new MasterCodePair("status", "status_desc", appStatus);
        appParam.addMasterCode(mcp);
        appParam.addMasterCode(mcp_status);
        SearchResult appResult = inboxService.appDoQuery(appParam);
        if(!StringUtil.isEmpty(appResult)){
            List<InboxAppQueryDto> inboxAppQueryDtoList = appResult.getRows();
            List<RecallApplicationDto> finalRecallApplicationDtoList = IaisCommonUtils.genNewArrayList();
            inboxAppQueryDtoList.forEach(h ->{
                RecallApplicationDto recallApplicationDto = new RecallApplicationDto();
                recallApplicationDto.setAppId(h.getId());
                recallApplicationDto.setAppNo(h.getApplicationNo());
                finalRecallApplicationDtoList.add(recallApplicationDto);
            });
            List<RecallApplicationDto> recallApplicationDtoList = inboxService.canRecallApplications(finalRecallApplicationDtoList);
            recallApplicationDtoList.forEach(h ->{
                inboxAppQueryDtoList.forEach(f -> {
                    if(f.getApplicationNo().equals(h.getAppNo())){
                        f.setCanRecall(h.getResult());
                    }
                });
            });
            ParamUtil.setSessionAttr(request,InboxConst.APP_PARAM, appParam);
            ParamUtil.setRequestAttr(request,InboxConst.APP_RESULT, appResult);

        }
        setNumInfoToRequest(request,interInboxUserDto);
        String delDraftConfMsg = MessageUtil.getMessageDesc("NEW_ACK002");
        ParamUtil.setRequestAttr(bpc.request,"delDraftConfMsg",delDraftConfMsg);

    }
   public  String getRepalceService(){
       List<HcsaServiceDto> hcsaServiceDtos = hcsaConfigClient.getActiveServices().getEntity();
       if(IaisCommonUtils.isEmpty(hcsaServiceDtos)){
           return null;
       }
       StringBuilder stringBuilder = new StringBuilder();
       stringBuilder.append(" ( CASE app.service_id ");
       for(HcsaServiceDto hcsaServiceDto :hcsaServiceDtos){
           stringBuilder.append(" WHEN '").append(hcsaServiceDto.getId()).append("' Then '").append(hcsaServiceDto.getSvcCode()).append("'  ");
       }
       stringBuilder.append("ELSE  null END )");
       return  stringBuilder.toString();
   }
    public void appSwitch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> appSwitch"));
//        ParamUtil.setRequestAttr(bpc.request,"appCannotRecall", false);
    }

    public void appDoSearch(BaseProcessClass bpc) throws ParseException {
        log.debug(StringUtil.changeForLog("Step ---> appDoSearch"));
        HttpServletRequest request = bpc.request;
        String applicationType = ParamUtil.getString(request,"appTypeSelect");
        String serviceType = ParamUtil.getString(request,"appServiceType");
        String applicationStatus = ParamUtil.getString(request,"appStatusSelect");
        String applicationNo = ParamUtil.getString(request,"appNoPath");
        Date startAppDate = Formatter.parseDate(ParamUtil.getString(request, "esd"));
        Date endAppDate = Formatter.parseDate(ParamUtil.getString(request, "eed"));
        String createDtStart = Formatter.formatDateTime(startAppDate, SystemAdminBaseConstants.DATE_FORMAT);
        String createDtEnd = Formatter.formatDateTime(endAppDate, SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
        SearchParam inboxParam = HalpSearchResultHelper.getSearchParam(request,"inboxApp",true);
        if(applicationType == null || applicationType.equals(InboxConst.SEARCH_ALL)){
            inboxParam.removeFilter("appType");
        }else{
            inboxParam.addFilter("appType", applicationType,true);
        }
        if(applicationStatus == null || applicationStatus.equals(InboxConst.SEARCH_ALL)){
            inboxParam.removeFilter("appStatus");
        }else{
            List<String> inParams = IaisCommonUtils.genNewArrayList();
            if (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION.equals(applicationStatus)){
                inParams = MasterCodeUtil.getCodeKeyByCodeValue("Pending Inspection");
            }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(applicationStatus)){
                inParams = MasterCodeUtil.getCodeKeyByCodeValue("Pending Screening");
            }
            else{
                inParams.add(applicationStatus);
            }
            SqlHelper.builderInSql(inboxParam, "status", "appStatus", inParams);
//            inboxParam.addFilter("appStatus", applicationStatus,true);
        }
        if(applicationNo != null){
            if(applicationNo.indexOf('%') != -1){
                applicationNo = applicationNo.replaceAll("%","//%");
                inboxParam.addFilter("appNo", "%"+applicationNo+"%",true);
            }else{
                inboxParam.addFilter("appNo", "%"+applicationNo+"%",true);
            }
        }else{
            inboxParam.removeFilter("appNo");
        }
        if(serviceType == null || serviceType.equals(InboxConst.SEARCH_ALL)){
            inboxParam.removeFilter("serviceType");
        }else{
            if (!StringUtil.isEmpty(applicationStatus) && ApplicationConsts.APPLICATION_STATUS_DRAFT.equals(applicationStatus)){
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceType);
                String svcCode = hcsaServiceDto.getSvcCode();
                if (!StringUtil.isEmpty(svcCode)){
                    inboxParam.addFilter("serviceCode", "%" + svcCode + "%",true);
                }
            }else{
                inboxParam.addFilter("serviceType", serviceType,true);
            }
        }
        if (startAppDate != null && endAppDate != null){
            if(startAppDate.compareTo(endAppDate)<=0){
                if(!StringUtil.isEmpty(createDtStart)){
                    inboxParam.addFilter("createDtStart", createDtStart,true);
                }else{
                    inboxParam.removeFilter("createDtStart");
                }
                if(!StringUtil.isEmpty(createDtEnd)){
                    inboxParam.addFilter("createDtEnd", createDtEnd,true);
                }else{
                    inboxParam.removeFilter("createDtEnd");
                }
            }else{
                ParamUtil.setRequestAttr(request,InboxConst.APP_DATE_ERR_MSG, "Date Submitted From cannot be later than Date Submitted To");
            }
        }else{
            if(!StringUtil.isEmpty(createDtStart)){
                inboxParam.addFilter("createDtStart", createDtStart,true);
            }else{
                inboxParam.removeFilter("createDtStart");
            }
            if(!StringUtil.isEmpty(createDtEnd)){
                inboxParam.addFilter("createDtEnd", createDtEnd,true);
            }else{
                inboxParam.removeFilter("createDtEnd");
            }
        }
    }

    public void appDoPage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam inboxParam = HalpSearchResultHelper.getSearchParam(request,"inboxApp");
        HalpSearchResultHelper.doPage(request,inboxParam);
    }

    public void appDoSort(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam inboxParam = HalpSearchResultHelper.getSearchParam(request,"inboxApp");
        HalpSearchResultHelper.doSort(request,inboxParam);
    }

    public void appDoAppeal(BaseProcessClass bpc) throws IOException {
        HttpServletRequest request = bpc.request;
        String appId =null;
        String isNeedDelete = bpc.request.getParameter("isNeedDelete");
        if("delete".equals(isNeedDelete)){
            appId=bpc.request.getParameter("action_id_value");
        }else {
            appId= ParamUtil.getMaskedString(request, InboxConst.ACTION_ID_VALUE);
        }
        List<ApplicationSubDraftDto> draftByLicAppId = inboxService.getDraftByLicAppId(appId);
        if(!draftByLicAppId.isEmpty()){
            StringBuilder stringBuilder=new StringBuilder();
            for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                stringBuilder.append(applicationSubDraftDto.getDraftNo()).append(' ');
            }
            if("delete".equals(isNeedDelete)){
                for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                    inboxService.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                }
            }else {
                String ack030 = MessageUtil.getMessageDesc("ACK030");
                String replace = ack030.replace("<draft application no>", stringBuilder.toString());
                bpc.request.setAttribute("draftByLicAppId",replace);
                bpc.request.setAttribute("isAppealApplicationShow","1");
                bpc.request.setAttribute("appealApplication",appId);
                return;
            }
        }
        Boolean result = inboxService.checkEligibility(appId);
        Map<String, String> map = inboxService.appealIsApprove(appId, "application");
        if (result&&map.isEmpty()){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohAppealApplication")
                    .append("?appealingFor=")
                    .append(MaskUtil.maskValue("appealingFor",appId))
                    .append("&type=application");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }else{
            if(!result){
                ParamUtil.setRequestAttr(bpc.request,InboxConst.APP_RECALL_RESULT,"An appeal has already been submitted for this licence/application");
            } else if(!map.isEmpty()){
                ParamUtil.setRequestAttr(bpc.request,InboxConst.APP_RECALL_RESULT,"There is already a pending application for the selected licence");
            }
            ParamUtil.setRequestAttr(bpc.request,"appIsAppealed",result);
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
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        bpc.response.sendRedirect(tokenUrl);
    }

    public void appDoDraft(BaseProcessClass bpc) throws IOException {
        log.debug("The prepareEdit start ...");
        HttpServletRequest request = bpc.request;

        String appNo = ParamUtil.getString(request, InboxConst.ACTION_NO_VALUE);
        String appStatus = ParamUtil.getString(request, "action_status_value");
        if (MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_RECALLED).equals(appStatus)){
            appNo = inboxService.getDraftByAppNo(appNo).getDraftNo();
        }
        if(InboxConst.APP_DO_DRAFT_TYPE_RFC.equals(ParamUtil.getString(request, InboxConst.ACTION_TYPE_VALUE))){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohRequestForChange/prepareDraft")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }else if(InboxConst.APP_DO_DRAFT_TYPE_RENEW.equals(ParamUtil.getString(request, InboxConst.ACTION_TYPE_VALUE))){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohWithOutRenewal")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }else if(InboxConst.APP_DO_DRAFT_TYPE_APPEAL.equals(ParamUtil.getString(request, InboxConst.ACTION_TYPE_VALUE))){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohAppealApplication")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }
        else {
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohNewApplication")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);}
    }

    public void appDoDelete(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> appDoDelete start..."));
        String draft = ParamUtil.getString(bpc.request,InboxConst.ACTION_NO_VALUE);
        if(!StringUtil.isEmpty(draft)){
            log.debug(StringUtil.changeForLog("draft no. is not null"));
            List<AuditTrailDto> adDtos = IaisCommonUtils.genNewArrayList(1);
            inboxService.deleteDraftByNo(draft);
            String delDraftAckMsg = MessageUtil.getMessageDesc("NEW_ACK003");
            ParamUtil.setRequestAttr(bpc.request,"needDelDraftMsg",AppConsts.YES);
            ParamUtil.setRequestAttr(bpc.request,"delDraftAckMsg",delDraftAckMsg);
        }
        log.debug(StringUtil.changeForLog("Step ---> appDoDelete end..."));
    }

    public void appDoReload(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("Step ---> appDoReload"));
        HttpServletRequest request = bpc.request;
        this.appDoDraft(bpc);
    }

    public void appDoRecall(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> appDoRecall"));
        HttpServletRequest request = bpc.request;
        String appId = ParamUtil.getMaskedString(request, InboxConst.ACTION_ID_VALUE);
        String appNo = ParamUtil.getString(request, InboxConst.ACTION_NO_VALUE);
        RecallApplicationDto recallApplicationDto = new RecallApplicationDto();
        recallApplicationDto.setAppId(appId);
        recallApplicationDto.setAppNo(appNo);
        try{
            inboxService.recallApplication(recallApplicationDto);
        }catch (Exception e){
            ParamUtil.setRequestAttr(bpc.request,"needDelDraftMsg",AppConsts.YES);
            ParamUtil.setRequestAttr(bpc.request,"delDraftAckMsg","The background task is not generated");
        }
    }

    public void doSelfAssMt(BaseProcessClass bpc) throws IOException {
        HttpServletRequest request = bpc.request;

        //selfDeclAction // selfDeclApplicationNumber // appGroupId
        String appSelfFlag = ParamUtil.getString(request, "action_self_value");
        String appGroupId = ParamUtil.getString(request, "action_grp_value");
        String selfDeclApplicationNumber = ParamUtil.getString(request, "action_no_value");
        StringBuilder url = new StringBuilder();
        if ("0".equals(appSelfFlag)){
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohSelfAssessmentSubmit")
                    .append("?appGroupId=")
                    .append(MaskUtil.maskValue("appGroupId",appGroupId))
                    .append("&selfDeclAction=inbox")
                    .append("&selfDeclApplicationNumber=")
                    .append(MaskUtil.maskValue("selfDeclApplicationNumber",selfDeclApplicationNumber));
        }else {
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohSelfAssessmentSubmit")
                    .append("?selfDeclAction=rfi")
                    .append("&selfDeclApplicationNumber=")
                    .append(MaskUtil.maskValue("selfDeclApplicationNumber",selfDeclApplicationNumber));
        }
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        bpc.response.sendRedirect(tokenUrl);
    }

    public void appToAppView(BaseProcessClass bpc) throws IOException {
        String appNo = ParamUtil.getString(bpc.request, InboxConst.ACTION_NO_VALUE);
        String appType = ParamUtil.getString(bpc.request, InboxConst.ACTION_TYPE_VALUE);
        if (InboxConst.APP_DO_DRAFT_TYPE_APPEAL.equals(appType)){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohAppealApplication/")
                    .append("?appNo=")
                    .append(MaskUtil.maskValue("appNo",appNo))
                    .append("&crud_action_type=inbox");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }else{
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohNewApplication/1/InboxToPreview")
                    .append("?appNo=")
                    .append(MaskUtil.maskValue("appNo",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }
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
        inboxServiceSelectList.add(new SelectOption("BLB@", "Blood Banking"));
        inboxServiceSelectList.add(new SelectOption("CLB@", "Clinical Laboratory"));
        inboxServiceSelectList.add(new SelectOption("RDS@", "Radiological Service"));
        inboxServiceSelectList.add(new SelectOption("TCB@", "Tissue Banking"));
        inboxServiceSelectList.add(new SelectOption("NMA@", "Nuclear Medicine (Assay)"));
        inboxServiceSelectList.add(new SelectOption("NMI@", "Nuclear Medicine (Imaging)"));
        ParamUtil.setRequestAttr(request, "inboxServiceSelect", inboxServiceSelectList);

        List<SelectOption> inboxTypSelectList = IaisCommonUtils.genNewArrayList();
        inboxTypSelectList.add(new SelectOption("All", "All"));
        inboxTypSelectList.add(new SelectOption(MessageConstants.MESSAGE_TYPE_NOTIFICATION, MasterCodeUtil.getCodeDesc(MessageConstants.MESSAGE_TYPE_NOTIFICATION)));
        inboxTypSelectList.add(new SelectOption(MessageConstants.MESSAGE_TYPE_ANNONUCEMENT, MasterCodeUtil.getCodeDesc(MessageConstants.MESSAGE_TYPE_ANNONUCEMENT)));
        inboxTypSelectList.add(new SelectOption(MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED, MasterCodeUtil.getCodeDesc(MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED)));
        ParamUtil.setRequestAttr(request, "inboxTypeSelect", inboxTypSelectList);
    }

    private void prepareAppSelectOption(HttpServletRequest request){
        List<SelectOption> applicationTypeSelectList = IaisCommonUtils.genNewArrayList();
        applicationTypeSelectList.add(new SelectOption("All", "All"));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_APPEAL, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_TYPE_APPEAL)));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK)));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_CESSATION, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_TYPE_CESSATION)));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION)));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_RENEWAL, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_TYPE_RENEWAL)));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL)));
        ParamUtil.setRequestAttr(request, "appTypeSelect", applicationTypeSelectList);

        List<SelectOption> appServiceStatusSelectList = IaisCommonUtils.genNewArrayList();
        appServiceStatusSelectList.add(new SelectOption("All", "All"));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_DRAFT, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_DRAFT)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_RECALLED, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_RECALLED)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION)));
//        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST, "Pending Internal Clarification"));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_FE_APPOINTMENT_SCHEDULING, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_FE_APPOINTMENT_SCHEDULING)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_APPROVED, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_APPROVED)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_REJECTED, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_REJECTED)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_WITHDRAWN, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_WITHDRAWN)));
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
    }

    private void prepareLicSelectOption(HttpServletRequest request){
        List<SelectOption> LicenceStatusList = IaisCommonUtils.genNewArrayList();
        LicenceStatusList.add(new SelectOption("All", "All"));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_ACTIVE, MasterCodeUtil.getCodeDesc(ApplicationConsts.LICENCE_STATUS_ACTIVE)));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_CEASED, MasterCodeUtil.getCodeDesc(ApplicationConsts.LICENCE_STATUS_CEASED)));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_EXPIRY, MasterCodeUtil.getCodeDesc(ApplicationConsts.LICENCE_STATUS_EXPIRY)));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_LAPSED, MasterCodeUtil.getCodeDesc(ApplicationConsts.LICENCE_STATUS_LAPSED)));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_APPROVED, MasterCodeUtil.getCodeDesc(ApplicationConsts.LICENCE_STATUS_APPROVED)));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_SUSPENDED, MasterCodeUtil.getCodeDesc(ApplicationConsts.LICENCE_STATUS_SUSPENDED)));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_REVOKED, MasterCodeUtil.getCodeDesc(ApplicationConsts.LICENCE_STATUS_REVOKED)));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_IACTIVE, MasterCodeUtil.getCodeDesc(ApplicationConsts.LICENCE_STATUS_IACTIVE)));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_TRANSFERRED, MasterCodeUtil.getCodeDesc(ApplicationConsts.LICENCE_STATUS_TRANSFERRED)));
        ParamUtil.setRequestAttr(request, "licStatus", LicenceStatusList);

        List<SelectOption> LicenceTypeList = IaisCommonUtils.genNewArrayList();
        LicenceTypeList.add(new SelectOption("All", "All"));
        LicenceTypeList.add(new SelectOption("Tissue Banking", "Tissue Banking"));
        LicenceTypeList.add(new SelectOption("Blood Banking", "Blood Banking"));
        LicenceTypeList.add(new SelectOption("Radiological Service", "Radiological Service"));
        LicenceTypeList.add(new SelectOption("Clinical Laboratory", "Clinical Laboratory"));
        LicenceTypeList.add(new SelectOption("Nuclear Medicine (Assay)", "Nuclear Medicine (Assay)"));
        LicenceTypeList.add(new SelectOption("Nuclear Medicine (Imaging)", "Nuclear Medicine (Imaging)"));
        ParamUtil.setRequestAttr(request, "licType", LicenceTypeList);

        List<SelectOption> LicenceActionsList = IaisCommonUtils.genNewArrayList();
        LicenceActionsList.add(new SelectOption("Appeal", "Appeal"));
        ParamUtil.setRequestAttr(request, "licActions", LicenceActionsList);

        List<SelectOption> LicenceNoActionsList = IaisCommonUtils.genNewArrayList();
        ParamUtil.setRequestAttr(request, "licNoActions", LicenceNoActionsList);
    }

    private void clearSessionAttr(HttpServletRequest request){
        ParamUtil.setSessionAttr(request,"licence_err_list", null);
        ParamUtil.setSessionAttr(request,InboxConst.INBOX_PARAM, null);
        ParamUtil.setSessionAttr(request,InboxConst.APP_PARAM, null);
        ParamUtil.setSessionAttr(request,InboxConst.LIC_PARAM, null);
        ParamUtil.setSessionAttr(request,InboxConst.INTER_INBOX_USER_INFO, null);

    }
}
