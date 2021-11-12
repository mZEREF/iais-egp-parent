package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.MasterCodePair;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.HcsaFeeBundleItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxMsgMaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.jwt.JwtEncoder;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.HalpSearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AssessmentGuideService;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import com.ecquaria.cloud.moh.iais.service.client.AppInboxClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2020-01-08 10:49
 **/
@Slf4j
@Delegator("interInboxDelegator")
@RequestMapping("/internetInbox")
public class InterInboxDelegator {
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private InboxService inboxService;
    @Autowired
    private LicenceInboxClient licenceInboxClient;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private InterInboxDelegator(InboxService inboxService){
        this.inboxService = inboxService;
    }
    @Autowired
    AppInboxClient appInboxClient;
    @Autowired
    AssessmentGuideService assessmentGuideService;
    private static final  String LIC_PRINT_FLAG = "InterInboxDelegator_lic_print_flag";
    public static final String twoSentences = "This following licences are bundled with this licence. Would you like to renew them as well: ";

    private static String msgStatus[] = {
            MessageConstants.MESSAGE_STATUS_READ,
            MessageConstants.MESSAGE_STATUS_UNREAD,
            MessageConstants.MESSAGE_STATUS_RESPONSE,
            MessageConstants.MESSAGE_STATUS_UNRESPONSE,
    };

    private static String msgArchiverStatus[] = {
            MessageConstants.MESSAGE_STATUS_ARCHIVER,
    };

    public void start(BaseProcessClass bpc) throws IllegalAccessException, ParseException, IOException {
        clearSessionAttr(bpc.request);
        IaisEGPHelper.clearSessionAttr(bpc.request,FilterParameter.class);
        initInboxDto(bpc);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INTERNAL_INBOX, AuditTrailConsts.FUNCTION_INBOX);
        ParamUtil.setSessionAttr(bpc.request,LIC_PRINT_FLAG,IaisEGPHelper.isActiveMigrated() ? AppConsts.NO : AppConsts.YES);
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

    public void toElis(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("Step ---> toElis"));
        HttpServletRequest request = bpc.request;
        String privateKey = ConfigHelper.getString("halp.elis.private.key");
        LoginContext loginContext = AccessUtil.getLoginUser(request);
        Claims claims = Jwts.claims();
        claims.put("uid", loginContext.getNricNum());
        claims.put("uen", loginContext.getUenNo());
        String iso8601ExpDateString  = Formatter.formatDateTime(new Date(), Formatter.DATE_ELIS);
        claims.put("iat", iso8601ExpDateString);
        JwtEncoder jwtEncoder = new JwtEncoder();
        String jwtStr = jwtEncoder.encode(claims, privateKey);
        String elisUrl = ConfigHelper.getString("moh.elis.url");
        log.info(StringUtil.changeForLog("Jwt token => " + jwtStr));
        log.info(StringUtil.changeForLog("Elis Url ==> " + elisUrl));
//        IaisEGPHelper.redirectUrl(bpc.response, elisUrl + "?authToken=" + StringUtil.escapeSecurityScript(jwtStr));
        ParamUtil.setRequestAttr(request, "ssoToElisUrl",
                elisUrl + "?authToken=" + StringUtil.escapeSecurityScript(jwtStr));
        IaisEGPHelper.doLogout(request);
    }

    public void toMOHAlert(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("Step ---> toMOHAlert"));
        HttpServletRequest request = bpc.request;
        String privateKey = ConfigHelper.getString("halp.mohAlert.private.key");
        LoginContext loginContext = AccessUtil.getLoginUser(request);
        Claims claims = Jwts.claims();
        claims.put("uid", loginContext.getNricNum());
        claims.put("uen", loginContext.getUenNo());
        String iso8601ExpDateString  = Formatter.formatDateTime(new Date(),Formatter.DATE_ELIS);
        claims.put("iat", iso8601ExpDateString);
        JwtEncoder jwtEncoder = new JwtEncoder();
        String jwtStr = jwtEncoder.encode(claims, privateKey);
        String alertUrl = ConfigHelper.getString("moh.mohAlert.url");
        log.info(StringUtil.changeForLog("Jwt token => " + jwtStr));
        log.info(StringUtil.changeForLog("Elis Url ==> " + alertUrl));
//        IaisEGPHelper.redirectUrl(bpc.response, alertUrl);
        ParamUtil.setRequestAttr(request, "ssoToAlertUrl",
                alertUrl + "?authToken=" + StringUtil.escapeSecurityScript(jwtStr));
        IaisEGPHelper.doLogout(request);
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
        List<SelectOption> inboxTypes = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_INBOX_MESSAGE_TYPE);
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
            if (MessageConstants.MESSAGE_TYPE_NOTIFICATION.equals(msgType) || MessageConstants.MESSAGE_TYPE_ANNONUCEMENT.equals(msgType)){
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
        if(MsgPage==null){
            MsgPage="msg_view";
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
            inboxMsgParam.addFilter("messageType",inboxType,true);
        }else{
            inboxMsgParam.removeFilter("messageType");
        }
        if(inboxService != null){
            inboxMsgParam.addFilter("interService", inboxService ,true);
        }else{
            inboxMsgParam.removeFilter("interService");
        }
        if(msgSubject != null){
            inboxMsgParam.addFilter("msgSubject",msgSubject,true);
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
        List<SelectOption> inboxTypes = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_INBOX_MESSAGE_TYPE);
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
    public void toLicencePage(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("Step ---> toLicencePage"));
        HttpServletRequest request = bpc.request;
        prepareLicSelectOption(request);
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(request,InboxConst.INTER_INBOX_USER_INFO);
        if (interInboxUserDto == null) {
            interInboxUserDto = initInboxDto(bpc);   // Walk around. No meaning.
        }
        SearchParam licParam = HalpSearchResultHelper.getSearchParam(request,"inboxLic");
        if(interInboxUserDto != null) {
            licParam.addFilter("licenseeId", interInboxUserDto.getLicenseeId(), true);
        }
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
            inboxLicParam.addFilter("licNo",licenceNo,true);
        }else{
            inboxLicParam.removeFilter("licNo");
        }
        if(serviceType == null){
            inboxLicParam.removeFilter("serviceType");
        }else {
            inboxLicParam.addFilter("serviceType",serviceType,true);
        }
        if(licStatus == null){
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
                String errStr = "Licence Start Date From cannot be later than Licence Start Date To";
                ParamUtil.setRequestAttr(request,InboxConst.LIC_DATE_ERR_MSG, errStr);
                Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                errorMap.put("INBOX_SEARCH_APP_ERR",errStr);
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
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
        LicenceDto licenceDto = licenceInboxClient.getLicDtoById(licId).getEntity();
        if(licenceDto != null){
            boolean isActive = licenceDto != null && ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceDto.getStatus());
           boolean isApprove= licenceDto!=null && ApplicationConsts.LICENCE_STATUS_APPROVED.equals(licenceDto.getStatus());
            if(!isActive && !isApprove){
                ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,MessageUtil.getMessageDesc("INBOX_ACK011"));
                List<String> licIdValues = IaisCommonUtils.genNewArrayList();
                licIdValues.add(licId);
                ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licIdValues);
                ParamUtil.setRequestAttr(bpc.request,"licIsAppealed",Boolean.FALSE);
                return;
            }
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(licenceDto.getCreatedAt());
            calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(systemParamConfig.getLicencePeriod()));
            boolean periodEqDay = calendar.getTime().after(new Date());
            if (!periodEqDay){
                ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,"The selected licence is not eligible for appeal");
                ParamUtil.setRequestAttr(bpc.request,"licIsAppealed",Boolean.FALSE);
                return;
            }
            List<AppPremiseMiscDto> entity = appInboxClient.getAppPremiseMiscDtoRelateId(licId).getEntity();
            List<LicenceDto> baseOrSpecLicenceDtos=licenceInboxClient.getBaseOrSpecLicence(licId).getEntity();
            if(IaisCommonUtils.isNotEmpty(baseOrSpecLicenceDtos)){
                for (LicenceDto licBs:baseOrSpecLicenceDtos
                ) {
                    List<AppPremiseMiscDto> entity2 = appInboxClient.getAppPremiseMiscDtoRelateId(licBs.getId()).getEntity();
                    if(!entity2.isEmpty()){
                        if(entity.isEmpty()){
                            entity=entity2;
                        }else {
                            entity.addAll(entity2);
                        }
                    }
                }
            }
            if(!entity.isEmpty()){
                Set<String> appStatus=IaisCommonUtils.genNewHashSet();
                appStatus.add(ApplicationConsts.APPLICATION_STATUS_DELETED);
                appStatus.add(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
                appStatus.add(ApplicationConsts.APPLICATION_STATUS_WITHDRAWN);
                appStatus.add(ApplicationConsts.APPLICATION_STATUS_REJECTED);
                for (AppPremiseMiscDto apc:entity
                     ) {
                    ApplicationDto applicationDto=appInboxClient.getApplicationByCorreId(apc.getAppPremCorreId()).getEntity();
                    if (!appStatus.contains(applicationDto.getStatus())){
                        ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,MessageUtil.getMessageDesc("APPEAL_ERR002"));
                        ParamUtil.setRequestAttr(bpc.request,"licIsAppealed",Boolean.FALSE);
                        return;
                    }

                }
            }
        }
        List<LicenceDto> licenceDtos = licenceInboxClient.isNewLicence(licId).getEntity();
        if(!licenceDtos.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,MessageUtil.getMessageDesc("APPEAL_ACK002"));
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
                String ack030 = MessageUtil.getMessageDesc("GENERAL_ACK030");
                String replace = ack030.replace("{draft application no}", stringBuilder.toString());
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
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }else{
            if(!result){
                ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,"An appeal has already been submitted for this licence/application");
            }else if(!map.isEmpty()){
                ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,MessageUtil.getMessageDesc("INBOX_ACK010"));
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
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
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
                    String ack030 = MessageUtil.getMessageDesc("GENERAL_ACK030");
                    String replace = ack030.replace("{draft application no}", stringBuilder.toString());
                    bpc.request.setAttribute("draftByLicAppId",replace);
                    bpc.request.setAttribute("isShow","1");
                    return;
                }
            }
            if(errorMap.isEmpty()){
                List<ApplicationSubDraftDto> applicationSubDraftDtos = inboxService.getDraftByLicAppIdAndStatus(licIdValue,ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT);
                if(!IaisCommonUtils.isEmpty(applicationSubDraftDtos)){
                    errorMap.put("errorMessage",MessageUtil.getMessageDesc("NEW_ERR0023"));
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
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            }else{
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
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
        List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos = hcsaConfigClient.getActiveBundleDtoList().getEntity();
        Map<String,List<HcsaFeeBundleItemDto>> map=new HashMap<>(10);
        if(hcsaFeeBundleItemDtos!=null){
            hcsaFeeBundleItemDtos.forEach((v)->{
                List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos1 = map.get(v.getBundleId());
                if(hcsaFeeBundleItemDtos1==null){
                    hcsaFeeBundleItemDtos1=new ArrayList<>();
                    hcsaFeeBundleItemDtos1.add(v);
                    map.put(v.getBundleId(),hcsaFeeBundleItemDtos1);
                }else {
                    hcsaFeeBundleItemDtos1.add(v);
                }
            });
        }
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
                        String ack030 = MessageUtil.getMessageDesc("GENERAL_ACK030");
                        String replace = ack030.replace("{draft application no}", stringBuilder.toString());
                        bpc.request.setAttribute("draftByLicAppId",replace);
                        bpc.request.setAttribute("isRenewShow","1");
                        ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licIdValue);
                        return;
                    }
                }
            }
            ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licIdValue);
            for (String licId:licIdValue) {
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
            if(result && licIdValue.size()==1){
                List<ApplicationSubDraftDto> applicationSubDraftDtos = inboxService.getDraftByLicAppIdAndStatus(licIdValue.get(0),ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT);
                if(!IaisCommonUtils.isEmpty(applicationSubDraftDtos)){
                    errorMap.put("errorMessage",MessageUtil.getMessageDesc("NEW_ERR0023"));
                    result = false;
                }
            }
            String bundle = bpc.request.getParameter("bundle");
            if(result){
                if("no".equals(bundle)){

                }else if("yes".equals(bundle)){
                    List<LicenceDto> bundleLicenceDtos = (List<LicenceDto> )bpc.request.getSession().getAttribute("bundleLicenceDtos");
                    if(bundleLicenceDtos!=null){
                        for (LicenceDto v : bundleLicenceDtos) {
                            licIdValue.add(v.getId());
                        }
                    }
                }else {
                    List<HcsaServiceDto> entity = hcsaConfigClient.getActiveServices().getEntity();
                    Map<String,HcsaServiceDto> hcsaServiceDtoMap=new HashMap<>(10);
                    for (HcsaServiceDto v : entity) {
                        hcsaServiceDtoMap.put(v.getSvcName(),v);
                    }
                    List<LicenceDto> bundleLicenceDtos=new ArrayList<>(10);
                    List<LicenceDto> list=new ArrayList<>(10);
                    //This can be optimized
                    for (String v : licIdValue) {
                        LicenceDto licenceDto = licenceInboxClient.getLicDtoById(v).getEntity();
                        list.add(licenceDto);
                        HcsaServiceDto hcsaServiceDto = hcsaServiceDtoMap.get(licenceDto.getSvcName());
                        ListIterator<HcsaFeeBundleItemDto> iterator = hcsaFeeBundleItemDtos.listIterator();
                        while (iterator.hasNext()){
                            HcsaFeeBundleItemDto next = iterator.next();
                            if(hcsaServiceDto.getSvcCode().equals(next.getSvcCode())){
                                List<LicenceDto> licenceDtos = licenceInboxClient.getBundleLicence(licenceDto).getEntity();
                                if(!licenceDtos.isEmpty()){
                                    List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos1 = map.get(next.getBundleId());
                                    for (LicenceDto dto : licenceDtos) {
                                        HcsaServiceDto hcsaServiceDto1 = hcsaServiceDtoMap.get(dto.getSvcName());
                                        for (HcsaFeeBundleItemDto feeBundleItemDto : hcsaFeeBundleItemDtos1) {
                                            if(!feeBundleItemDto.getSvcCode().equals(next.getSvcCode())&&hcsaServiceDto1.getSvcCode().equals(feeBundleItemDto.getSvcCode())){
                                                bundleLicenceDtos.add(dto);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    List<LicenceDto> dtoList=new ArrayList<>(bundleLicenceDtos.size());
                    List<LicenceDto> removeList=new ArrayList<>(10);
                    for (LicenceDto v : bundleLicenceDtos) {
                        for (LicenceDto licenceDto : list) {
                            if(v.getId().equals(licenceDto.getId())){
                                removeList.add(v);
                            }
                        }
                    }
                    bundleLicenceDtos.removeAll(removeList);
                    if(!bundleLicenceDtos.isEmpty()){
                        StringBuilder stringBuilder=new StringBuilder();
                        bundleLicenceDtos.forEach((v)->{
                            Map<String, String> map1 = inboxService.checkRenewalStatus(v.getId());
                            if(map1.isEmpty()){
                                stringBuilder.append(v.getLicenceNo());
                                dtoList.add(v);
                            }
                        });
                        if(!dtoList.isEmpty()){
                            stringBuilder.insert(0,twoSentences);
                            bpc.request.getSession().setAttribute("bundleLicenceDtos",dtoList);
                            bpc.request.setAttribute("draftByLicAppId", stringBuilder.toString());
                            bpc.request.setAttribute("isBundleShow","1");
                            ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licIdValue);
                            return;
                        }
                    }
                }
            }
            if (result){
                StringBuilder url = new StringBuilder();
                url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                        .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohWithOutRenewal");
                ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_LIC_ID_LIST_ATTR, (Serializable) licIdValue);
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
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
            String inbox_ack011 = MessageUtil.getMessageDesc("INBOX_ACK011");
            for(String licId : licIdValue){
                LicenceDto licenceDto = licenceInboxClient.getLicDtoById(licId).getEntity();
                if(licenceDto==null){
                    ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_CEASED_ERR_RESULT,Boolean.TRUE);
                    bpc.request.setAttribute("cessationError",inbox_ack011);
                    ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licIdValue);
                    return ;
                }else {
                    if( !ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceDto.getStatus())){
                        if(!(IaisEGPHelper.isActiveMigrated() &&ApplicationConsts.LICENCE_STATUS_APPROVED.equals(licenceDto.getStatus())&&licenceDto.getMigrated()!=0)){
                            ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_CEASED_ERR_RESULT,Boolean.TRUE);
                            bpc.request.setAttribute("cessationError",inbox_ack011);
                            ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licIdValue);
                            return ;
                        }
                    }
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
                    String ack030 = MessageUtil.getMessageDesc("GENERAL_ACK030");
                    String replace = ack030.replace("{draft application no}", stringBuilder.toString());
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
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            }
        }
    }

    @RequestMapping(value = "/licenceNo", method = RequestMethod.POST)
    public @ResponseBody
    boolean licDoPrint(HttpServletRequest request) {
        String [] licIds = request.getParameterValues("licenceNos");
        List<String> licIdValue = IaisCommonUtils.genNewArrayList();
        for (String item : licIds) {
            String[] strArr = item.split("@");
            String unMaskLic = MaskUtil.unMaskValue(strArr[0], strArr[1]);
            licIdValue.add(unMaskLic);
        }
        if(!IaisCommonUtils.isEmpty(licIdValue)) {
            ParamUtil.setSessionAttr(request, "lic-print-Ids", (Serializable) licIdValue);
        }
        return true;
    }

    /**
     *
     * @param bpc
     *
     * >>>>>>>>Application Inbox<<<<<<<<
     */
    public void toAppPage(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request,"licence_err_list",null);
        log.debug(StringUtil.changeForLog("Step ---> toAppPage"));
        HttpServletRequest request = bpc.request;
        prepareAppSelectOption(request);
        /**
         * Application SearchResult
         */
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(request,InboxConst.INTER_INBOX_USER_INFO);
        SearchParam appParam = HalpSearchResultHelper.getSearchParam(request,"inboxApp");
        appParam.addFilter("licenseeId", interInboxUserDto.getLicenseeId(),true);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.APPLICATION_QUERY_KEY,appParam);
        String repalceService = getRepalceService();
        appParam.setMainSql(appParam.getMainSql().replace("repalceService",repalceService));
        List<SelectOption> appTypes = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_APP_TYPE);
        List<SelectOption> appStatus = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_APP_STATUS);
        MasterCodePair mcp = new MasterCodePair("app_type", "app_type_desc", appTypes);
        MasterCodePair mcp_status = new MasterCodePair("status", "status_desc", appStatus);
        appParam.addMasterCode(mcp);
        appParam.addMasterCode(mcp_status);
        SearchResult appResult = inboxService.appDoQuery(appParam);
        if(!StringUtil.isEmpty(appResult)){
            List<InboxAppQueryDto> inboxAppQueryDtoList = appResult.getRows();
            List<RecallApplicationDto> finalRecallApplicationDtoList = IaisCommonUtils.genNewArrayList();
            Map<String,Boolean> mapCanInsps = inboxService.getMapCanInsp();
            inboxAppQueryDtoList.forEach(h ->{
                String status = h.getStatus();
                Integer selfAssmtFlag = h.getSelfAssmtFlag();
                if ((status.equals(ApplicationConsts.PENDING_ASO_REPLY)
                        || status.equals(ApplicationConsts.PENDING_PSO_REPLY)
                        || status.equals(ApplicationConsts.PENDING_INP_REPLY))
                        && selfAssmtFlag == 2){
                    h.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_CLARIFICATION);
                }
                h.setCanInspection(getCanInspFlow(h,mapCanInsps));
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
       List<HcsaServiceDto> hcsaServiceDtos = hcsaConfigClient.allHcsaService().getEntity();
       if(IaisCommonUtils.isEmpty(hcsaServiceDtos)){
           return null;
       }
       StringBuilder stringBuilder = new StringBuilder();
       stringBuilder.append(" ( CASE app.service_id ");
       for(HcsaServiceDto hcsaServiceDto :hcsaServiceDtos){
           stringBuilder.append(" WHEN '").append(hcsaServiceDto.getId()).append("' Then '").append(hcsaServiceDto.getSvcCode()).append("'  ");
       }
       stringBuilder.append("ELSE  'N/A' END )");
       return  stringBuilder.toString();
   }

    public void doInspection(BaseProcessClass bpc) throws IOException {
        HttpServletRequest request = bpc.request;
        log.debug(StringUtil.changeForLog("Step ---> doInspection"));
        String appGroupId = ParamUtil.getString(request, "action_grp_value");
        AppSubmissionDto appSubmissionDto = new AppSubmissionDto();
        if (appGroupId != null){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohSubmitInspectionDate").append("?pageFrom=dropdown");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            appSubmissionDto.setAppGrpId(appGroupId);
            ParamUtil.setSessionAttr(request, "AppSubmissionDto",appSubmissionDto);
        }
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
        if(applicationType == null){
            inboxParam.removeFilter("appType");
        }else{
            inboxParam.addFilter("appType", applicationType,true);
        }
        if(applicationStatus == null){
            inboxParam.removeFilter("appStatus");
        }else{
            List<String> inParams = IaisCommonUtils.genNewArrayList();
            if (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION.equals(applicationStatus)){
                inParams = MasterCodeUtil.getCodeKeyByCodeValue("Pending Inspection");
            }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(applicationStatus)){
                inParams = MasterCodeUtil.getCodeKeyByCodeValue("Pending Screening");
            } else if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01.equals(applicationStatus)){
                inParams = MasterCodeUtil.getCodeKeyByCodeValue("Pending Approval");
            }else if(ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(applicationStatus)){
                inParams = MasterCodeUtil.getCodeKeyByCodeValue("Approved");
            }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING.equals(applicationStatus)){
                inParams = MasterCodeUtil.getCodeKeyByCodeValue("Pending Re-Scheduling");
            }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_FE_APPOINTMENT_SCHEDULING.equals(applicationStatus)){
                inParams = MasterCodeUtil.getCodeKeyByCodeValue("Pending Scheduling");
            }else if(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(applicationStatus)){
                inParams = MasterCodeUtil.getCodeKeyByCodeValue("Pending Clarification");
            }
            else{
                inParams.add(applicationStatus);
            }
            SqlHelper.builderInSql(inboxParam, "B.status", "appStatus", inParams);
//            inboxParam.addFilter("appStatus", applicationStatus,true);
        }
        if(applicationNo != null){
            if(applicationNo.indexOf('%') != -1){
                applicationNo = applicationNo.replaceAll("%","//%");
                inboxParam.addFilter("appNo", applicationNo,true);
            }else{
                inboxParam.addFilter("appNo", applicationNo,true);
            }
        }else{
            inboxParam.removeFilter("appNo");
        }
        if(serviceType == null){
            inboxParam.removeFilter("serviceType");
        }else{
            if (!StringUtil.isEmpty(applicationStatus) && ApplicationConsts.APPLICATION_STATUS_DRAFT.equals(applicationStatus)){
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceType);
                if (hcsaServiceDto != null && !StringUtil.isEmpty(hcsaServiceDto.getSvcCode())){
                    inboxParam.addFilter("serviceCode",  hcsaServiceDto.getSvcCode() ,true);
                }
            }else{
//                String moduleStr = SqlHelper.constructInCondition("svc.module", mcb.length);
//                searchParam.addParam("module", moduleStr);
//                int indx = 0;
//                for (String s : mcb){
//                    searchParam.addFilter("svc.module"+indx, s);
//                    indx++;
//                }
                List<String> serviceNameList = IaisCommonUtils.genNewArrayList();
                serviceNameList.add(serviceType);
                List<HcsaServiceDto> entity = hcsaConfigClient.getHcsaServiceByNames(serviceNameList).getEntity();
                String moduleStr = SqlHelper.constructInCondition("B.service_id", entity.size());
                inboxParam.addParam("service_id", moduleStr);
                int indx = 0;
                for (HcsaServiceDto hcsaServiceDto : entity){
                    inboxParam.addFilter("B.service_id"+indx, hcsaServiceDto.getId());
                    indx++;
                }
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceType);
                if (hcsaServiceDto != null && !StringUtil.isEmpty(hcsaServiceDto.getSvcCode())){
                    inboxParam.addFilter("serviceCode",  hcsaServiceDto.getSvcCode());
                }
//                inboxParam.addFilter("serviceType", serviceType,true);
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
                String errStr = MessageUtil.getMessageDesc("INBOX_ERR007");
                ParamUtil.setRequestAttr(request,InboxConst.APP_DATE_ERR_MSG, errStr);
                Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                errorMap.put("INBOX_SEARCH_APP_ERR",errStr);
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
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
         List<LicenceDto> licenceDtos = licenceInboxClient.isNewApplication(appId).getEntity();
        ApplicationDto applicationDto = appInboxClient.getApplicationById(appId).getEntity();
 /*       //68521
        if(applicationDto!=null && applicationDto.getOriginLicenceId()!=null){
            LicenceDto entity = licenceInboxClient.getLicDtoById(applicationDto.getOriginLicenceId()).getEntity();
            if(entity!=null && ApplicationConsts.LICENCE_STATUS_IACTIVE.equals(entity.getStatus())){
                licenceDtos.add(entity);
            }
        }*/
        //EAS and MTS licence only one active/approve licence for appeal rejected
        if(applicationDto.getApplicationType().equals(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION)&&applicationDto.getStatus().equals(ApplicationConsts.APPLICATION_STATUS_REJECTED)){
            List<HcsaServiceDto> hcsaServiceDtos = IaisCommonUtils.genNewArrayList();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
            if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(hcsaServiceDto.getSvcCode()) || AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(hcsaServiceDto.getSvcCode())){
                hcsaServiceDtos.add(hcsaServiceDto);
            }
            if(!IaisCommonUtils.isEmpty(hcsaServiceDtos)){
                LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
                boolean canCreateEasOrMts = assessmentGuideService.canApplyEasOrMts(loginContext.getLicenseeId(),hcsaServiceDtos);
                if(!canCreateEasOrMts){
                    ParamUtil.setRequestAttr(bpc.request,InboxConst.APP_RECALL_RESULT,MessageUtil.getMessageDesc("NEW_ERR0029"));
                    ParamUtil.setRequestAttr(bpc.request,"appIsAppealed",Boolean.FALSE);
                    return;
                }
            }
        }
        if(!licenceDtos.isEmpty()){
            //change APPEAL_ACK002
            ParamUtil.setRequestAttr(bpc.request,InboxConst.APP_RECALL_RESULT,MessageUtil.getMessageDesc("APPEAL_ACK002"));
            ParamUtil.setRequestAttr(bpc.request,"appIsAppealed",Boolean.FALSE);
            return;
        }
        List<AppPremiseMiscDto> entity = appInboxClient.getAppPremiseMiscDtoRelateId(appId).getEntity();
        if(!entity.isEmpty()){
            ListIterator<AppPremiseMiscDto> appPremiseMiscDtoListIterator = entity.listIterator();
            while(appPremiseMiscDtoListIterator.hasNext()){
                AppPremiseMiscDto next = appPremiseMiscDtoListIterator.next();
                if(!ApplicationConsts.APPEAL_TYPE_APPLICAITON.equals(next.getAppealType()) && !ApplicationConsts.APPEAL_TYPE_LICENCE.equals(next.getAppealType())){
                    appPremiseMiscDtoListIterator.remove();
                }
            }
            if(!entity.isEmpty()){
                ParamUtil.setRequestAttr(bpc.request,InboxConst.APP_RECALL_RESULT,MessageUtil.getMessageDesc("APPEAL_ERR003"));
                ParamUtil.setRequestAttr(bpc.request,"appIsAppealed",Boolean.FALSE);
                return;
            }
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
                String ack030 = MessageUtil.getMessageDesc("GENERAL_ACK030");
                String replace = ack030.replace("{draft application no}", stringBuilder.toString());
                bpc.request.setAttribute("draftByLicAppId",replace);
                bpc.request.setAttribute("isAppealApplicationShow","1");
                bpc.request.setAttribute("appealApplication",appId);
                return;
            }
        }
        Calendar calendar=Calendar.getInstance();
        Date createAt = applicationDto.getCreateAt();
        calendar.setTime(createAt);
        calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(systemParamConfig.getAdditionalCgo()));
        boolean cgoEqDay = calendar.getTime().after(new Date());
        calendar.setTime(createAt);
        calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(systemParamConfig.getRestrictedName()));
        boolean nameEqDay = calendar.getTime().after(new Date());
        calendar.setTime(createAt);
        calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(systemParamConfig.getAppealOthers()));
        boolean otherEqDay = calendar.getTime().after(new Date());
        calendar.setTime(createAt);
        calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(systemParamConfig.getRenewalFee()));
        boolean feeEqDay = calendar.getTime().after(new Date());
        calendar.setTime(createAt);
        calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(systemParamConfig.getAgainstRejection()));
        boolean rejectEqDay = calendar.getTime().after(new Date());
        if (!cgoEqDay&&!nameEqDay&&!otherEqDay&&!feeEqDay&&!rejectEqDay){
            ParamUtil.setRequestAttr(bpc.request,InboxConst.APP_RECALL_RESULT,"The selected application is not eligible for appeal");
            ParamUtil.setRequestAttr(bpc.request,"appIsAppealed",Boolean.FALSE);
            return;
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
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }else{
            if(!result){
                ParamUtil.setRequestAttr(bpc.request,InboxConst.APP_RECALL_RESULT,"An appeal has already been submitted for this licence/application");
            } else if(!map.isEmpty()){
                ParamUtil.setRequestAttr(bpc.request,InboxConst.APP_RECALL_RESULT,MessageUtil.getMessageDesc("INBOX_ACK010"));
            }
            ParamUtil.setRequestAttr(bpc.request,"appIsAppealed",result);
        }
    }

    public void appDoWithDraw(BaseProcessClass bpc) throws IOException {
        HttpServletRequest request = bpc.request;
        String appId = ParamUtil.getMaskedString(request, InboxConst.ACTION_ID_VALUE);
        String appNo = ParamUtil.getString(request, InboxConst.ACTION_NO_VALUE);
        if (!appInboxClient.isApplicationWithdrawal(appId).getEntity()) {
            String withdrawalError = MessageUtil.getMessageDesc("WDL_EER001");
            ParamUtil.setRequestAttr(bpc.request,"appIsWithdrawal",Boolean.TRUE);
            bpc.request.setAttribute(InboxConst.APP_RECALL_RESULT,withdrawalError);
        } else {
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE + "MohWithdrawalApplication")
                    .append("?withdrawAppId=")
                    .append(MaskUtil.maskValue("withdrawAppId", appId))
                    .append("&withdrawAppNo=")
                    .append(MaskUtil.maskValue("withdrawAppNo", appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }
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
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }else if(InboxConst.APP_DO_DRAFT_TYPE_RENEW.equals(ParamUtil.getString(request, InboxConst.ACTION_TYPE_VALUE))){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohWithOutRenewal")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }else if(InboxConst.APP_DO_DRAFT_TYPE_APPEAL.equals(ParamUtil.getString(request, InboxConst.ACTION_TYPE_VALUE))){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohAppealApplication")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }
        else {
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohNewApplication")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);}
    }

    public void appDoDelete(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> appDoDelete start..."));
        String draft = ParamUtil.getString(bpc.request,InboxConst.ACTION_NO_VALUE);
        if(!StringUtil.isEmpty(draft)){
            log.debug(StringUtil.changeForLog("draft no. is not null"));
            log.info(StringUtil.changeForLog("delete draft start ..."));
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

    public void appDoRecall(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("Step ---> appDoRecall"));
        HttpServletRequest request = bpc.request;
        String appId = ParamUtil.getMaskedString(request, InboxConst.ACTION_ID_VALUE);
        String appNo = ParamUtil.getString(request, InboxConst.ACTION_NO_VALUE);
        String appGroupId = ParamUtil.getString(request, "action_grp_value");
        String appSelfFlag = ParamUtil.getString(request, "action_self_value");
        if ("appMakePayment".equals(appSelfFlag)){
            doPaymentAction(appGroupId,request,bpc.response);
        }else{
            RecallApplicationDto recallApplicationDto = new RecallApplicationDto();
            recallApplicationDto.setAppId(appId);
            recallApplicationDto.setAppNo(appNo);
            recallApplicationDto.setAppGrpId(appGroupId);
            recallApplicationDto = inboxService.recallApplication(recallApplicationDto);
            if ("RECALLMSG001".equals(recallApplicationDto.getMessage())){
                ParamUtil.setRequestAttr(bpc.request,"needDelDraftMsg",AppConsts.YES);
                ParamUtil.setRequestAttr(bpc.request,"delDraftAckMsg","This data is being processed. Please try again later");
            }
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
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

    public void appToAppView(BaseProcessClass bpc) throws IOException {
        String appNo = ParamUtil.getString(bpc.request, InboxConst.ACTION_NO_VALUE);
        String appType = ParamUtil.getString(bpc.request, InboxConst.ACTION_TYPE_VALUE);
        String appGroupId = ParamUtil.getString(bpc.request, "action_grp_value");
        String appSelfFlag = ParamUtil.getString(bpc.request, "action_self_value");
        ParamUtil.setSessionAttr(bpc.request, "isPopApplicationView", Boolean.FALSE);
        if (InboxConst.APP_DO_DRAFT_TYPE_APPEAL.equals(appType)){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohAppealApplication/")
                    .append("?appNo=")
                    .append(MaskUtil.maskValue("appNo",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }else if (InboxConst.APP_DO_DRAFT_TYPE_WITHDRAWAL.equals(appType)){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE + "MohWithdrawalApplication")
                    .append("?withdrawAppNo=")
                    .append(MaskUtil.maskValue("withdrawAppNo", appNo))
                    .append("&isDoView=Y");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }else if("appMakePayment".equals(appSelfFlag)){
            doPaymentAction(appGroupId,bpc.request,bpc.response);
        }
        else{
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohNewApplication/1/InboxToPreview")
                    .append("?appNo=")
                    .append(MaskUtil.maskValue("appNo",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }
    }


    private void doPaymentAction(String appGroupId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ApplicationGroupDto applicationGroupDto = inboxService.getAppGroupByGroupId(appGroupId);
        if (applicationGroupDto != null){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohRetriggerGiroPayment")
                    .append("?appGrpNo=")
                    .append(MaskUtil.maskValue("appGrpNo", applicationGroupDto.getGroupNo()));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), request);
            IaisEGPHelper.redirectUrl(response, tokenUrl);
        }
    }

    public void setNumInfoToRequest(HttpServletRequest request,InterInboxUserDto interInboxUserDto){
        if(interInboxUserDto != null) {
            Integer licActiveNum = inboxService.licActiveStatusNum(interInboxUserDto.getLicenseeId());
            Integer appDraftNum = inboxService.appDraftNum(interInboxUserDto.getLicenseeId());
            Integer unreadAndresponseNum = inboxService.unreadAndUnresponseNum(interInboxUserDto.getLicenseeId());
            ParamUtil.setRequestAttr(request, "unreadAndresponseNum", unreadAndresponseNum);
            ParamUtil.setRequestAttr(request, "licActiveNum", licActiveNum);
            ParamUtil.setRequestAttr(request, "appDraftNum", appDraftNum);
        }
    }
    /**
     *
     * @param request
     * @description Data to Form select part
     */
    private void prepareMsgSelectOption(HttpServletRequest request){
        ParamUtil.setRequestAttr(request, "inboxServiceSelect", getInboxServiceSelectList(true,true));
        List<SelectOption> inboxTypSelectList = IaisCommonUtils.genNewArrayList();
        inboxTypSelectList.add(new SelectOption(MessageConstants.MESSAGE_TYPE_NOTIFICATION, MasterCodeUtil.getCodeDesc(MessageConstants.MESSAGE_TYPE_NOTIFICATION)));
        inboxTypSelectList.add(new SelectOption(MessageConstants.MESSAGE_TYPE_ANNONUCEMENT, MasterCodeUtil.getCodeDesc(MessageConstants.MESSAGE_TYPE_ANNONUCEMENT)));
        inboxTypSelectList.add(new SelectOption(MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED, MasterCodeUtil.getCodeDesc(MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED)));
        ParamUtil.setRequestAttr(request, "inboxTypeSelect", inboxTypSelectList);
    }

    private List<SelectOption> getInboxServiceSelectList(boolean specialIdentification,boolean serviceCode){
        String specialIdentificationString  = specialIdentification ? "@" : "";
        List<SelectOption> inboxServiceSelectList = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceDto> hcsaServiceDtos = hcsaConfigClient.getActiveServices().getEntity();
        if(IaisCommonUtils.isNotEmpty(hcsaServiceDtos)){
            for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtos){
                inboxServiceSelectList.add(new SelectOption((serviceCode ? hcsaServiceDto.getSvcCode() :hcsaServiceDto.getSvcName()) +specialIdentificationString, hcsaServiceDto.getSvcName()));
            }
        }
        return inboxServiceSelectList;
    }

    private void prepareAppSelectOption(HttpServletRequest request){
        List<SelectOption> applicationTypeSelectList = IaisCommonUtils.genNewArrayList();
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_APPEAL, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_TYPE_APPEAL)));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_CESSATION, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_TYPE_CESSATION)));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION)));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION)));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_RENEWAL, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_TYPE_RENEWAL)));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)));
        applicationTypeSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL)));
        ParamUtil.setRequestAttr(request, "appTypeSelect", applicationTypeSelectList);

        List<SelectOption> appServiceStatusSelectList = IaisCommonUtils.genNewArrayList();
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_DRAFT, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_DRAFT)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_RECALLED, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_RECALLED)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION)));
//        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST, "Pending Internal Clarification"));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_GIRO_PAYMENT_FAIL, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_GIRO_PAYMENT_FAIL)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_PAYMENT_RESUBMIT, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_PAYMENT_RESUBMIT)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_FE_APPOINTMENT_SCHEDULING, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_FE_APPOINTMENT_SCHEDULING)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_APPROVED, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_APPROVED)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_REJECTED, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_REJECTED)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_WITHDRAWN, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_WITHDRAWN)));
        ParamUtil.setRequestAttr(request, "appStatusSelect", appServiceStatusSelectList);

        ParamUtil.setRequestAttr(request, "appServiceType", getInboxServiceSelectList(false,false));
    }

    private void prepareLicSelectOption(HttpServletRequest request){
        List<SelectOption> LicenceStatusList = IaisCommonUtils.genNewArrayList();
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_ACTIVE, MasterCodeUtil.getCodeDesc(ApplicationConsts.LICENCE_STATUS_ACTIVE)));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_APPROVED, MasterCodeUtil.getCodeDesc(ApplicationConsts.LICENCE_STATUS_APPROVED)));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_CEASED, MasterCodeUtil.getCodeDesc(ApplicationConsts.LICENCE_STATUS_CEASED)));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_EXPIRY, MasterCodeUtil.getCodeDesc(ApplicationConsts.LICENCE_STATUS_EXPIRY)));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_LAPSED, MasterCodeUtil.getCodeDesc(ApplicationConsts.LICENCE_STATUS_LAPSED)));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_IACTIVE, MasterCodeUtil.getCodeDesc(ApplicationConsts.LICENCE_STATUS_IACTIVE)));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_REVOKED, MasterCodeUtil.getCodeDesc(ApplicationConsts.LICENCE_STATUS_REVOKED)));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_SUSPENDED, MasterCodeUtil.getCodeDesc(ApplicationConsts.LICENCE_STATUS_SUSPENDED)));
        LicenceStatusList.add(new SelectOption(ApplicationConsts.LICENCE_STATUS_TRANSFERRED, MasterCodeUtil.getCodeDesc(ApplicationConsts.LICENCE_STATUS_TRANSFERRED)));
        ParamUtil.setRequestAttr(request, "licStatus", LicenceStatusList);

        ParamUtil.setRequestAttr(request, "licType", getInboxServiceSelectList(false,false));

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
        ParamUtil.setSessionAttr(request,AppConsts.SESSION_INTER_INBOX_MESSAGE_ID,null);
        ParamUtil.setSessionAttr(request,"DraftNumber",null);
        ParamUtil.setSessionAttr(request,"isSingle",null);
        // clear selectLicence
        Enumeration<?> names = request.getSession().getAttributeNames();
        if (names != null) {
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                if (name.startsWith("selectLicence")) {
                    request.getSession().removeAttribute(name);
                }
            }
        }
    }

    private InterInboxUserDto initInboxDto(BaseProcessClass bpc) throws IOException {
        LoginContext loginContext= (LoginContext)ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        if (loginContext == null){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN);
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }else{
            AuditTrailDto auditTrailDto = inboxService.getLastLoginInfo(loginContext.getLoginId(), bpc.request.getSession().getId());
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

            return interInboxUserDto;
        }

        return null;
    }

    private boolean getCanInspFlow(InboxAppQueryDto inboxAppQueryDto,Map<String,Boolean> mapCanInsps){
        return mapCanInsps.get(StringUtil.getNonNull(inboxAppQueryDto.getApplicationType())+"_"+StringUtil.getNonNull(inboxAppQueryDto.getCode())) != null && (inboxAppQueryDto.getHasSubmitPrefDate() == null || 0==inboxAppQueryDto.getHasSubmitPrefDate());
    }
}
