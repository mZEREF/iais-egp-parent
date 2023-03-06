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
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxMsgMaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageSearchDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserRoleAccessMatrixDto;
import com.ecquaria.cloud.moh.iais.common.jwt.JwtEncoder;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.FeMainConst;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FeInboxHelper;
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
import com.ecquaria.cloud.privilege.Privilege;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    private static final  String INBOX_MSG = "inboxMsg";
    private static final  String LICENCE_ERR_LIST = "licence_err_list";
    private static final  String INBOX_LIC = "inboxLic";
    private static final  String LIC_STATUS = "licStatus";
    private static final  String FSTART_DATE = "fStartDate";
    private static final  String ESTART_DATE = "eStartDate";
    private static final  String FEXPIRY_DATE = "fExpiryDate";
    private static final  String EEXPIRY_DATE = "eExpiryDate";
    private static final  String SERVICE_TYPE = "serviceType";
    private static final  String LICENCE_NO = "licenceNo";
    private static final  String LIC_IS_APPEALED = "licIsAppealed";
    private static final  String IS_NEED_DELETE = "isNeedDelete";
    private static final  String DELETE = "delete";
    private static final  String GENERAL_ACK030 = "GENERAL_ACK030";
    private static final  String DRAFT_APPLICATION_NO = "{draft application no}";
    private static final  String DRAFT_BY_LICAPPID = "draftByLicAppId";
    private static final  String MOH_APPEAL_APPLICATION = "MohAppealApplication";
    private static final  String CESSATION_ERROR = "cessationError";
    private static final  String INBOX_APP = "inboxApp";
    private static final  String ACTION_GRP_VALUE = "action_grp_value";
    private static final  String APP_NO = "appNo";
    private static final  String CREATE_DTSTART = "createDtStart";
    private static final  String CREATE_DTEND = "createDtEnd";
    private static final  String APPIS_APPEALED = "appIsAppealed";


    private static String[] msgStatus = {
            MessageConstants.MESSAGE_STATUS_READ,
            MessageConstants.MESSAGE_STATUS_UNREAD,
            MessageConstants.MESSAGE_STATUS_RESPONSE,
            MessageConstants.MESSAGE_STATUS_UNRESPONSE,
    };

    private static String[] msgArchiverStatus = {
            MessageConstants.MESSAGE_STATUS_ARCHIVER,
    };

    public void start(BaseProcessClass bpc) throws IllegalAccessException, ParseException, IOException {
        clearSessionAttr(bpc.request);
        IaisEGPHelper.clearSessionAttr(bpc.request,FilterParameter.class);
        initInboxDto(bpc);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INTERNAL_INBOX, AuditTrailConsts.FUNCTION_INBOX);
        ParamUtil.setSessionAttr(bpc.request,LIC_PRINT_FLAG,ConfigHelper.getString("inbox.lic.print.flag","1"));
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
        HalpSearchResultHelper.getSearchParam(request,INBOX_MSG,true);
    }

    public void msgDoPage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam inboxParam = HalpSearchResultHelper.getSearchParam(request,INBOX_MSG);
        HalpSearchResultHelper.doPage(request,inboxParam);
    }

    public void msgDoSort(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam inboxParam = HalpSearchResultHelper.getSearchParam(request,INBOX_MSG);
        HalpSearchResultHelper.doSort(request,inboxParam);
    }

    public void msgToArchive(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        prepareMsgSelectOption(request);
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(request,InboxConst.INTER_INBOX_USER_INFO);
        SearchParam inboxParam;
        String page = ParamUtil.getRequestString(request,"msg_action_type");
        if("msgToArchive".equals(page)){
            inboxParam = HalpSearchResultHelper.getSearchParam(request,INBOX_MSG,true);
        }else{
            inboxParam = HalpSearchResultHelper.getSearchParam(request,INBOX_MSG);
        }
        inboxParam.addFilter("userId", interInboxUserDto.getLicenseeId(),true);
        inboxParam.addFilter(InboxConst.MESSAGE_STATUS, msgArchiverStatus,true);
        List<SelectOption> inboxTypes = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_INBOX_MESSAGE_TYPE);
        MasterCodePair mcp = new MasterCodePair("message_type", "message_type_desc", inboxTypes);
        inboxParam.addMasterCode(mcp);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.MESSAGE_QUERY_KEY,inboxParam);
        SearchResult<InboxQueryDto> inboxResult = inboxService.inboxDoQuery(inboxParam);
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
       log.info("BaseProcessClass : {}",bpc.getClass().getName());
    }

    public void msgDoArchive(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> msgDoArchive"));
        HttpServletRequest request = bpc.request;
        String[] msgIdList = ParamUtil.getMaskedStrings(request,"msgIdList");
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
            if(StringUtil.isNotEmpty(msgContent)){
                msgContent = StringUtil.unescapeHtml(msgContent.replaceAll("<.span*?>", ""));
            }
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
        String inboxServices = ParamUtil.getString(request,InboxConst.MESSAGE_SERVICE);
        String msgSubject = ParamUtil.getString(request,InboxConst.MESSAGE_SEARCH);
        SearchParam inboxMsgParam = HalpSearchResultHelper.getSearchParam(request,INBOX_MSG,true);
        if(inboxType != null){
            inboxMsgParam.addFilter("messageType",inboxType,true);
        }else{
            inboxMsgParam.removeFilter("messageType");
        }
        if(inboxServices != null){
            inboxMsgParam.addFilter("interService", inboxServices ,true);
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
            inboxParam = HalpSearchResultHelper.getSearchParam(request,INBOX_MSG,true);
        }else{
            inboxParam = HalpSearchResultHelper.getSearchParam(request,INBOX_MSG);
        }
        inboxParam.addFilter("userId", interInboxUserDto.getLicenseeId(),true);
        inboxParam.addFilter(InboxConst.MESSAGE_STATUS, msgStatus,true);
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.MESSAGE_QUERY_KEY,inboxParam);
        List<SelectOption> inboxTypes = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_INBOX_MESSAGE_TYPE);
        MasterCodePair mcp = new MasterCodePair("message_type", "message_type_desc", inboxTypes);
        inboxParam.addMasterCode(mcp);
        SearchResult<InboxQueryDto> inboxResult = inboxService.inboxDoQuery(inboxParam);
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
            ParamUtil.setSessionAttr(bpc.request,LICENCE_ERR_LIST,null);
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
        SearchParam licParam = HalpSearchResultHelper.getSearchParam(request,INBOX_LIC);
        if(interInboxUserDto != null) {
            licParam.addFilter("licenseeId", interInboxUserDto.getLicenseeId(), true);
        }
        QueryHelp.setMainSql(InboxConst.INBOX_QUERY,InboxConst.LICENCE_QUERY_KEY,licParam);
        List<SelectOption> licStatus = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_LICENCE_STATUS);
        MasterCodePair mcp_status = new MasterCodePair("lic.status", "LIC_STATUS_DESC", licStatus);
        licParam.addMasterCode(mcp_status);
        SearchResult<InboxLicenceQueryDto> licResult = inboxService.licenceDoQuery(licParam);
        List<InboxLicenceQueryDto> inboxLicenceQueryDtoList = licResult.getRows();
        inboxLicenceQueryDtoList.stream().forEach(h -> {
            List<PremisesDto> premisesDtoList = inboxService.getPremisesByLicId(h.getId());
            List<String> addressList = IaisCommonUtils.genNewArrayList();
            for (PremisesDto premisesDto : premisesDtoList) {
                addressList.add(IaisCommonUtils.getAddress(premisesDto));
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
        String licStatus = ParamUtil.getString(request,LIC_STATUS);
        Date licStartDate = Formatter.parseDate(ParamUtil.getString(request, FSTART_DATE));
        Date licEndDate = Formatter.parseDate(ParamUtil.getString(request, ESTART_DATE));
        Date licfExpiryDate = Formatter.parseDate(ParamUtil.getString(request, FEXPIRY_DATE));
        Date liceExpiryDate = Formatter.parseDate(ParamUtil.getString(request, EEXPIRY_DATE));
        String fStartDate = Formatter.formatDateTime(licStartDate, SystemAdminBaseConstants.DATE_FORMAT);
        String eStartDate = Formatter.formatDateTime(licEndDate, SystemAdminBaseConstants.DATE_FORMAT);
        String fExpiryDate = Formatter.formatDateTime(licfExpiryDate, SystemAdminBaseConstants.DATE_FORMAT);
        String eExpiryDate = Formatter.formatDateTime(liceExpiryDate, SystemAdminBaseConstants.DATE_FORMAT);
        SearchParam inboxLicParam = HalpSearchResultHelper.getSearchParam(request,INBOX_LIC,true);
        if(licenceNo != null){
            inboxLicParam.addFilter("licNo",licenceNo,true);
        }else{
            inboxLicParam.removeFilter("licNo");
        }
        if(serviceType == null){
            inboxLicParam.removeFilter(SERVICE_TYPE);
        }else {
            inboxLicParam.addFilter(SERVICE_TYPE,serviceType,true);
        }
        if(licStatus == null){
            inboxLicParam.removeFilter(LIC_STATUS);
        }else{
            inboxLicParam.addFilter(LIC_STATUS,licStatus,true);
        }
        if (licStartDate != null && licEndDate != null){
            if (licStartDate.compareTo(licEndDate)<=0){
                if(!StringUtil.isEmpty(fStartDate)){
                    inboxLicParam.addFilter(FSTART_DATE,fStartDate,true);
                }else{
                    inboxLicParam.removeFilter(FSTART_DATE);
                }
                if(!StringUtil.isEmpty(eStartDate)){
                    inboxLicParam.addFilter(ESTART_DATE,eStartDate,true);
                }else{
                    inboxLicParam.removeFilter(ESTART_DATE);
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
                inboxLicParam.addFilter(FSTART_DATE,fStartDate,true);
            }else{
                inboxLicParam.removeFilter(FSTART_DATE);
            }
            if(!StringUtil.isEmpty(eStartDate)){
                inboxLicParam.addFilter(ESTART_DATE,eStartDate,true);
            }else{
                inboxLicParam.removeFilter(ESTART_DATE);
            }
        }
        if (licfExpiryDate != null && liceExpiryDate != null){
            if (licfExpiryDate.compareTo(liceExpiryDate)<=0){
                if(!StringUtil.isEmpty(fExpiryDate)){
                    inboxLicParam.addFilter(FEXPIRY_DATE,fExpiryDate,true);
                }else{
                    inboxLicParam.removeFilter(FEXPIRY_DATE);
                }
                if(!StringUtil.isEmpty(eExpiryDate)){
                    inboxLicParam.addFilter(EEXPIRY_DATE,eExpiryDate,true);
                }else{
                    inboxLicParam.removeFilter(EEXPIRY_DATE);
                }
            }else{
                ParamUtil.setRequestAttr(request,InboxConst.LIC_EXPIRY_ERR_MSG, "Licence Expiry Date From cannot be later than Licence Expiry Date To");
            }
        }else{
            if(!StringUtil.isEmpty(fExpiryDate)){
                inboxLicParam.addFilter(FEXPIRY_DATE,fExpiryDate,true);
            }else{
                inboxLicParam.removeFilter(FEXPIRY_DATE);
            }
            if(!StringUtil.isEmpty(eExpiryDate)){
                inboxLicParam.addFilter(EEXPIRY_DATE,eExpiryDate,true);
            }else{
                inboxLicParam.removeFilter(EEXPIRY_DATE);
            }
        }
    }

    public void licDoPage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam inboxParam = HalpSearchResultHelper.getSearchParam(request,INBOX_LIC);
        HalpSearchResultHelper.doPage(request,inboxParam);
    }

    public void licDoSort(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam inboxParam = HalpSearchResultHelper.getSearchParam(request,INBOX_LIC);
        HalpSearchResultHelper.doSort(request,inboxParam);
    }

    public void licDoAppeal(BaseProcessClass bpc) throws IOException {
        String licMaskId = ParamUtil.getString(bpc.request, LICENCE_NO);
        String licId = ParamUtil.getMaskedString(bpc.request,licMaskId);
        LicenceDto licenceDto = licenceInboxClient.getLicDtoById(licId).getEntity();
        if(licenceDto != null){
            boolean isActive = licenceDto != null && ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceDto.getStatus());
           boolean isApprove= licenceDto!=null && ApplicationConsts.LICENCE_STATUS_APPROVED.equals(licenceDto.getStatus());
            if(!isActive && !isApprove){
                ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,MessageUtil.getMessageDesc("INBOX_ACK011"));
                List<String> licIdValues = IaisCommonUtils.genNewArrayList();
                licIdValues.add(licId);
                ParamUtil.setSessionAttr(bpc.request,LICENCE_ERR_LIST,(Serializable) licIdValues);
                ParamUtil.setRequestAttr(bpc.request,LIC_IS_APPEALED,Boolean.FALSE);
                return;
            }
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(licenceDto.getCreatedAt());
            calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(systemParamConfig.getLicencePeriod()));
            boolean periodEqDay = calendar.getTime().after(new Date());
            if (!periodEqDay){
                // APPEAL_ACK003 - The selected licence is not eligible for appeal
                ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG, MessageUtil.getMessageDesc("APPEAL_ACK003"));
                ParamUtil.setRequestAttr(bpc.request,LIC_IS_APPEALED,Boolean.FALSE);
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
                        ParamUtil.setRequestAttr(bpc.request,LIC_IS_APPEALED,Boolean.FALSE);
                        return;
                    }

                }
            }
        }
        List<LicenceDto> licenceDtos = licenceInboxClient.isNewLicence(licId).getEntity();
        if(!licenceDtos.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,MessageUtil.getMessageDesc("APPEAL_ACK002"));
            ParamUtil.setRequestAttr(bpc.request,LIC_IS_APPEALED,Boolean.FALSE);
            return;
        }
        List<ApplicationSubDraftDto> draftByLicAppId = inboxService.getDraftByLicAppId(licId);
        if(!draftByLicAppId.isEmpty()){
            String isNeedDelete = bpc.request.getParameter(IS_NEED_DELETE);
            StringBuilder stringBuilder=new StringBuilder();
            for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                stringBuilder.append(applicationSubDraftDto.getDraftNo()).append(' ');
            }
            if(DELETE.equals(isNeedDelete)){
                for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                    inboxService.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                }
            }else {
                String ack030 = MessageUtil.getMessageDesc(GENERAL_ACK030);
                String replace = ack030.replace(DRAFT_APPLICATION_NO, stringBuilder.toString());
                bpc.request.setAttribute(DRAFT_BY_LICAPPID,replace);
                bpc.request.setAttribute("isAppealShow","1");
                List<String> licIdValues = IaisCommonUtils.genNewArrayList();
                licIdValues.add(licId);
                ParamUtil.setSessionAttr(bpc.request,LICENCE_ERR_LIST,(Serializable) licIdValues);
                return;
            }

        }
        Boolean result = inboxService.checkEligibility(licId);
        Map<String, String> map = inboxService.appealIsApprove(licId, "licence");
        if (result&&map.isEmpty()){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+MOH_APPEAL_APPLICATION)
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
            ParamUtil.setRequestAttr(bpc.request,LIC_IS_APPEALED,result);
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
        String licId = ParamUtil.getString(bpc.request, LICENCE_NO);
        String licIdValue = ParamUtil.getMaskedString(bpc.request, licId);
        if(licIdValue != null){
            List<String> licIdValues = IaisCommonUtils.genNewArrayList();
            licIdValues.add(licIdValue);
            ParamUtil.setSessionAttr(bpc.request,LICENCE_ERR_LIST,(Serializable) licIdValues);
            Map<String, String> errorMap = inboxService.checkRfcStatus(licIdValue);
            List<ApplicationSubDraftDto> draftByLicAppId = inboxService.getDraftByLicAppId(licIdValue);
            if(!draftByLicAppId.isEmpty()){
                String isNeedDelete = bpc.request.getParameter(IS_NEED_DELETE);
                StringBuilder stringBuilder=new StringBuilder();
                for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                    stringBuilder.append(applicationSubDraftDto.getDraftNo()).append(' ');
                }
                if(DELETE.equals(isNeedDelete)){
                    for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                        inboxService.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                    }
                }else {
                    String ack030 = MessageUtil.getMessageDesc(GENERAL_ACK030);
                    String replace = ack030.replace(DRAFT_APPLICATION_NO, stringBuilder.toString());
                    bpc.request.setAttribute(DRAFT_BY_LICAPPID,replace);
                    bpc.request.setAttribute("isShow","1");
                    return;
                }
            }
            if(errorMap.isEmpty()){
                List<ApplicationSubDraftDto> applicationSubDraftDtos = inboxService.getDraftByLicAppIdAndStatus(licIdValue,ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT);
                if(!IaisCommonUtils.isEmpty(applicationSubDraftDtos)){
                    errorMap.put(FeMainConst.ERR_MSG_KEY_MSG, MessageUtil.getMessageDesc("NEW_ERR0023"));
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
                ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,errorMap.get(FeMainConst.ERR_MSG_KEY_MSG));
            }
        }
    }

    /**
     * Step: licDoRenew
     *
     * @param bpc
     *
     */
    public void licDoRenew(BaseProcessClass bpc) throws IOException {
        String[] licIds = ParamUtil.getStrings(bpc.request, LICENCE_NO);
        if (licIds != null) {
            List<String> licenceIds = IaisCommonUtils.genNewArrayList();
            for (String item : licIds) {
                licenceIds.add(ParamUtil.getMaskedString(bpc.request, item));
            }
            boolean result = inboxService.checkRenewalStatus(licenceIds, bpc.request);
            log.info(StringUtil.changeForLog("Check Renewal Status Result: " + result));
            if (result) {
                ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_LIC_ID_LIST_ATTR, (Serializable) licenceIds);
                String url = InboxConst.URL_HTTPS + bpc.request.getServerName() +
                        InboxConst.URL_LICENCE_WEB_MODULE + "MohWithOutRenewal";
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url, bpc.request);
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            } else {
                ParamUtil.setSessionAttr(bpc.request, LICENCE_ERR_LIST, (Serializable) licenceIds);
            }
        }
    }

    public boolean checkRenewDraft(String licId, HttpServletRequest request) {
        List<ApplicationSubDraftDto> draftByLicAppId = inboxService.getDraftByLicAppId(licId);
        String isNeedDelete = ParamUtil.getString(request, IS_NEED_DELETE);
        if (!draftByLicAppId.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId) {
                stringBuilder.append(applicationSubDraftDto.getDraftNo()).append(' ');
            }
            if (DELETE.equals(isNeedDelete)) {
                for (ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId) {
                    inboxService.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                }
            } else {
                String ack030 = MessageUtil.getMessageDesc(GENERAL_ACK030);
                String replace = ack030.replace(DRAFT_APPLICATION_NO, stringBuilder.toString());
                ParamUtil.setRequestAttr(request, DRAFT_BY_LICAPPID, replace);
                ParamUtil.setRequestAttr(request, "isRenewShow", "1");
                ParamUtil.setSessionAttr(request, LICENCE_ERR_LIST, IaisCommonUtils.genNewArrayListWithData(licId));
                return false;
            }
        }
        List<ApplicationSubDraftDto> applicationSubDraftDtos = inboxService.getDraftByLicAppIdAndStatus(licId,
                ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT);
        if (!IaisCommonUtils.isEmpty(applicationSubDraftDtos)) {
            ParamUtil.setRequestAttr(request, InboxConst.LIC_ACTION_ERR_MSG, MessageUtil.getMessageDesc("NEW_ERR0023"));
            return false;
        }
        return true;
    }

    // check lic spec,select lic need have base
    public boolean checkIsBaseRenew(List<String> licIds){
        for (String licId : licIds) {}
        return true;
    }

    public void licDoCease(BaseProcessClass bpc) throws IOException {
        String cessationError = null ;
        String [] licIds = ParamUtil.getStrings(bpc.request, LICENCE_NO);
        List<String> licIdValue = IaisCommonUtils.genNewArrayList();
        ParamUtil.setSessionAttr(bpc.request,LICENCE_ERR_LIST,(Serializable) licIdValue);
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
                    bpc.request.setAttribute(CESSATION_ERROR,inbox_ack011);
                    ParamUtil.setSessionAttr(bpc.request,LICENCE_ERR_LIST,(Serializable) licIdValue);
                    return ;
                }else {
                    if (!ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceDto.getStatus()) &&
                            !(IaisEGPHelper.isActiveMigrated() && ApplicationConsts.LICENCE_STATUS_APPROVED.equals(licenceDto.getStatus()) && licenceDto.getMigrated() != 0)) {
                        ParamUtil.setRequestAttr(bpc.request, InboxConst.LIC_CEASED_ERR_RESULT, Boolean.TRUE);
                        bpc.request.setAttribute(CESSATION_ERROR, inbox_ack011);
                        ParamUtil.setSessionAttr(bpc.request, LICENCE_ERR_LIST, (Serializable) licIdValue);
                        return;
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
            String isNeedDelete = bpc.request.getParameter(IS_NEED_DELETE);
            if(!draftByLicAppId.isEmpty()){
                StringBuilder stringBuilder=new StringBuilder();
                for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                    stringBuilder.append(applicationSubDraftDto.getDraftNo()).append(' ');
                }
                if(DELETE.equals(isNeedDelete)){
                    for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                        inboxService.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                    }
                }else {
                    String ack030 = MessageUtil.getMessageDesc(GENERAL_ACK030);
                    String replace = ack030.replace(DRAFT_APPLICATION_NO, stringBuilder.toString());
                    bpc.request.setAttribute(DRAFT_BY_LICAPPID,replace);
                    bpc.request.setAttribute("isCeaseShow","1");
                    bpc.request.setAttribute("appealApplication",licIdValue.get(0));
                    return;
                }
            }
            if(result) {
                cessationError = MessageUtil.getMessageDesc("CESS_ERR002");
                bpc.request.setAttribute(CESSATION_ERROR,cessationError);
                ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_CEASED_ERR_RESULT,Boolean.TRUE);
                ParamUtil.setSessionAttr(bpc.request,LICENCE_ERR_LIST,(Serializable) licIdValue);
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

    @PostMapping(value = "/licenceNo")
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
        ParamUtil.setSessionAttr(bpc.request,LICENCE_ERR_LIST,null);
        log.debug(StringUtil.changeForLog("Step ---> toAppPage"));
        HttpServletRequest request = bpc.request;
        prepareAppSelectOption(request);
        /**
         * Application SearchResult
         */
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(request,InboxConst.INTER_INBOX_USER_INFO);
        SearchParam appParam = HalpSearchResultHelper.getSearchParam(request,INBOX_APP);
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
        SearchResult<InboxAppQueryDto> appResult = inboxService.appDoQuery(appParam);
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
        String appGroupId = ParamUtil.getString(request, ACTION_GRP_VALUE);
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
        SearchParam inboxParam = HalpSearchResultHelper.getSearchParam(request,INBOX_APP,true);
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
        }
        if(applicationNo != null){
            if(applicationNo.indexOf('%') != -1){
                applicationNo = applicationNo.replaceAll("%","//%");
                inboxParam.addFilter(APP_NO, applicationNo,true);
            }else{
                inboxParam.addFilter(APP_NO, applicationNo,true);
            }
        }else{
            inboxParam.removeFilter(APP_NO);
        }
        if(serviceType == null){
            inboxParam.removeFilter(SERVICE_TYPE);
        }else{
            if (!StringUtil.isEmpty(applicationStatus) && ApplicationConsts.APPLICATION_STATUS_DRAFT.equals(applicationStatus)){
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceType);
                if (hcsaServiceDto != null && !StringUtil.isEmpty(hcsaServiceDto.getSvcCode())){
                    inboxParam.addFilter("serviceCode",  hcsaServiceDto.getSvcCode() ,true);
                }
            }else{
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
            }
        }
        if (startAppDate != null && endAppDate != null){
            if(startAppDate.compareTo(endAppDate)<=0){
                if(!StringUtil.isEmpty(createDtStart)){
                    inboxParam.addFilter(CREATE_DTSTART, createDtStart,true);
                }else{
                    inboxParam.removeFilter(CREATE_DTSTART);
                }
                if(!StringUtil.isEmpty(createDtEnd)){
                    inboxParam.addFilter(CREATE_DTEND, createDtEnd,true);
                }else{
                    inboxParam.removeFilter(CREATE_DTEND);
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
                inboxParam.addFilter(CREATE_DTSTART, createDtStart,true);
            }else{
                inboxParam.removeFilter(CREATE_DTSTART);
            }
            if(!StringUtil.isEmpty(createDtEnd)){
                inboxParam.addFilter(CREATE_DTEND, createDtEnd,true);
            }else{
                inboxParam.removeFilter(CREATE_DTEND);
            }
        }
    }

    public void appDoPage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam inboxParam = HalpSearchResultHelper.getSearchParam(request,INBOX_APP);
        HalpSearchResultHelper.doPage(request,inboxParam);
    }

    public void appDoSort(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam inboxParam = HalpSearchResultHelper.getSearchParam(request,INBOX_APP);
        HalpSearchResultHelper.doSort(request,inboxParam);
    }

    public void appDoAppeal(BaseProcessClass bpc) throws IOException {
        HttpServletRequest request = bpc.request;
        String appId =null;
        String isNeedDelete = bpc.request.getParameter(IS_NEED_DELETE);
        if(DELETE.equals(isNeedDelete)){
            appId=bpc.request.getParameter("action_id_value");
        }else {
            appId= ParamUtil.getMaskedString(request, InboxConst.ACTION_ID_VALUE);
        }
         List<LicenceDto> licenceDtos = licenceInboxClient.isNewApplication(appId).getEntity();
        ApplicationDto applicationDto = appInboxClient.getApplicationById(appId).getEntity();
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
                    ParamUtil.setRequestAttr(bpc.request,APPIS_APPEALED,Boolean.FALSE);
                    return;
                }
            }
        }
        if(!licenceDtos.isEmpty()){
            //change APPEAL_ACK002
            ParamUtil.setRequestAttr(bpc.request,InboxConst.APP_RECALL_RESULT,MessageUtil.getMessageDesc("APPEAL_ACK002"));
            ParamUtil.setRequestAttr(bpc.request,APPIS_APPEALED,Boolean.FALSE);
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
                ParamUtil.setRequestAttr(bpc.request,APPIS_APPEALED,Boolean.FALSE);
                return;
            }
        }
        List<ApplicationSubDraftDto> draftByLicAppId = inboxService.getDraftByLicAppId(appId);
        if(!draftByLicAppId.isEmpty()){
            StringBuilder stringBuilder=new StringBuilder();
            for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                stringBuilder.append(applicationSubDraftDto.getDraftNo()).append(' ');
            }
            if(DELETE.equals(isNeedDelete)){
                for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                    inboxService.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                }
            }else {
                String ack030 = MessageUtil.getMessageDesc(GENERAL_ACK030);
                String replace = ack030.replace(DRAFT_APPLICATION_NO, stringBuilder.toString());
                bpc.request.setAttribute(DRAFT_BY_LICAPPID,replace);
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
            // APPEAL_ACK004 - The selected application is not eligible for appeal
            ParamUtil.setRequestAttr(bpc.request,InboxConst.APP_RECALL_RESULT, MessageUtil.getMessageDesc("APPEAL_ACK004"));
            ParamUtil.setRequestAttr(bpc.request,APPIS_APPEALED,Boolean.FALSE);
            return;
        }
        Boolean result = inboxService.checkEligibility(appId);
        Map<String, String> map = inboxService.appealIsApprove(appId, "application");
        if (result&&map.isEmpty()){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+MOH_APPEAL_APPLICATION)
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
            ParamUtil.setRequestAttr(bpc.request,APPIS_APPEALED,result);
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
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+MOH_APPEAL_APPLICATION)
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        } else {
            ParamUtil.setSessionAttr(bpc.request, HalpAssessmentGuideDelegator.APP_SVC_RELATED_INFO_LIST, null);
            ParamUtil.setSessionAttr(bpc.request, HalpAssessmentGuideDelegator.APP_SELECT_SERVICE, null);
            ParamUtil.setSessionAttr(bpc.request, "appLicBundleDtoList", null);
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE + "MohNewApplication")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber", appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }
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
        String appGroupId = ParamUtil.getString(request, ACTION_GRP_VALUE);
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
        String appGroupId = ParamUtil.getString(request, ACTION_GRP_VALUE);
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
        String appGroupId = ParamUtil.getString(bpc.request, ACTION_GRP_VALUE);
        String appSelfFlag = ParamUtil.getString(bpc.request, "action_self_value");
        ParamUtil.setSessionAttr(bpc.request, "isPopApplicationView", Boolean.FALSE);
        if (InboxConst.APP_DO_DRAFT_TYPE_APPEAL.equals(appType)){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohAppealApplication/")
                    .append("?appNo=")
                    .append(MaskUtil.maskValue(APP_NO,appNo));
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
                    .append(MaskUtil.maskValue(APP_NO,appNo));
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
            LoginContext lc = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = lc.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
            Integer licActiveNum = inboxService.licActiveStatusNum(HcsaServiceCacheHelper.controlServices(2,interInboxUserDto.getLicenseeId(),userRoleAccessMatrixDtos));
            Integer appDraftNum = inboxService.appDraftNum(HcsaServiceCacheHelper.controlServices(1,interInboxUserDto.getLicenseeId(),userRoleAccessMatrixDtos));
            InterMessageSearchDto imsDto = HcsaServiceCacheHelper.controlServices(0,interInboxUserDto.getLicenseeId(),userRoleAccessMatrixDtos);
            if (imsDto.getServiceCodes() == null) {
                imsDto.setServiceCodes(IaisCommonUtils.genNewArrayList());
            }
            List<String> privilegeIds = AccessUtil.getLoginUser(request).getPrivileges().stream().map(Privilege::getId).collect(Collectors.toList());
            imsDto.getServiceCodes().addAll(HalpSearchResultHelper.getDsTypes(privilegeIds));
            Integer unreadAndresponseNum = inboxService.unreadAndUnresponseNum(imsDto);
            ParamUtil.setRequestAttr(request, "unreadAndresponseNum", unreadAndresponseNum);
            ParamUtil.setRequestAttr(request, "licActiveNum", licActiveNum);
            ParamUtil.setRequestAttr(request, "appDraftNum", appDraftNum);
            ParamUtil.setRequestAttr(request, "dssDraftNum", inboxService.dssDraftNum(
                    HalpSearchResultHelper.initInterDssSearchDto(request,interInboxUserDto.getLicenseeId(),interInboxUserDto.getUserId())));
        }
    }
    /**
     *
     * @param request
     * @description Data to Form select part
     */
    private void prepareMsgSelectOption(HttpServletRequest request){
        ParamUtil.setRequestAttr(request, "inboxServiceSelect", getInboxServiceSelectList(true,true,request));
        List<SelectOption> inboxTypSelectList = IaisCommonUtils.genNewArrayList();
        inboxTypSelectList.add(new SelectOption(MessageConstants.MESSAGE_TYPE_NOTIFICATION, MasterCodeUtil.getCodeDesc(MessageConstants.MESSAGE_TYPE_NOTIFICATION)));
        inboxTypSelectList.add(new SelectOption(MessageConstants.MESSAGE_TYPE_ANNONUCEMENT, MasterCodeUtil.getCodeDesc(MessageConstants.MESSAGE_TYPE_ANNONUCEMENT)));
        inboxTypSelectList.add(new SelectOption(MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED, MasterCodeUtil.getCodeDesc(MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED)));
        ParamUtil.setRequestAttr(request, "inboxTypeSelect", inboxTypSelectList);
    }

    private List<SelectOption> getInboxServiceSelectList(boolean specialIdentification,boolean serviceCode,HttpServletRequest request){
        String specialIdentificationString  = specialIdentification ? "@" : "";
        List<SelectOption> inboxServiceSelectList = IaisCommonUtils.genNewArrayList();
        LoginContext lc = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = lc.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        List<String> serviceNames = HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos);
        if(IaisCommonUtils.isNotEmpty(serviceNames)){
            Map<String,String> map = HcsaServiceCacheHelper.receiveAllHcsaService().stream().collect(Collectors.toMap( HcsaServiceDto::getSvcName,HcsaServiceDto::getSvcCode, (v1, v2) -> v1));
             serviceNames.stream().forEach(svcName -> {
                String svcCode = map.get(svcName);
                inboxServiceSelectList.add(new SelectOption((serviceCode ?  svcCode: svcName) +specialIdentificationString,svcName));
            });
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
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_FE_APPOINTMENT_SCHEDULING, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_FE_APPOINTMENT_SCHEDULING)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_APPROVED, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_APPROVED)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_REJECTED, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_REJECTED)));
        appServiceStatusSelectList.add(new SelectOption(ApplicationConsts.APPLICATION_STATUS_WITHDRAWN, MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_WITHDRAWN)));
        ParamUtil.setRequestAttr(request, "appStatusSelect", appServiceStatusSelectList);

        ParamUtil.setRequestAttr(request, "appServiceType", getInboxServiceSelectList(false,false,request));
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
        ParamUtil.setRequestAttr(request, LIC_STATUS, LicenceStatusList);

        ParamUtil.setRequestAttr(request, "licType", getInboxServiceSelectList(false,false,request));

        List<SelectOption> LicenceActionsList = IaisCommonUtils.genNewArrayList();
        LicenceActionsList.add(new SelectOption("Appeal", "Appeal"));
        ParamUtil.setRequestAttr(request, "licActions", LicenceActionsList);

        List<SelectOption> LicenceNoActionsList = IaisCommonUtils.genNewArrayList();
        ParamUtil.setRequestAttr(request, "licNoActions", LicenceNoActionsList);
    }

    private void clearSessionAttr(HttpServletRequest request){
        ParamUtil.setSessionAttr(request,LICENCE_ERR_LIST, null);
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
            ParamUtil.setSessionAttr(bpc.request,"canCreateDs",Boolean.FALSE);
            ParamUtil.setSessionAttr(bpc.request,"canEditDs",Boolean.FALSE);
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

            List<String> privilegeIds = AccessUtil.getLoginUser(bpc.request).getPrivileges().stream().map(Privilege::getId).collect(Collectors.toList());
            //for new
            boolean canCreateDs =  FeInboxHelper.canCreateDs(privilegeIds);
            ParamUtil.setSessionAttr(bpc.request,"canCreateDs",canCreateDs);
            //for Amend
            boolean canAmendDs =  FeInboxHelper.canAmendDs(privilegeIds);
            ParamUtil.setSessionAttr(bpc.request,"canAmendDs",canAmendDs);

            return interInboxUserDto;
        }

        return null;
    }

    private boolean getCanInspFlow(InboxAppQueryDto inboxAppQueryDto,Map<String,Boolean> mapCanInsps){
        return mapCanInsps.get(StringUtil.getNonNull(inboxAppQueryDto.getApplicationType())+"_"+StringUtil.getNonNull(inboxAppQueryDto.getCode())) != null
                && ((inboxAppQueryDto.getHasSubmitPrefDate() == null || 0 == inboxAppQueryDto.getHasSubmitPrefDate())
                || (inboxAppQueryDto.getSelfAssmtFlag() == 0 || inboxAppQueryDto.getSelfAssmtFlag() == 2));
    }
}
