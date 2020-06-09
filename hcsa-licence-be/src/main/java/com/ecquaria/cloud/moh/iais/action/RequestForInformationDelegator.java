package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoReplyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.NewRfiPageListDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.LicInspNcEmailService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RequestForInformationDelegator
 *
 * @author junyu
 * @date 2019/12/14
 */
@Slf4j
@Delegator("requestForInformationDelegator")
public class RequestForInformationDelegator {
    @Autowired
    RequestForInformationService requestForInformationService;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    HcsaConfigClient hcsaConfigClient;
    @Autowired
    HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private InboxMsgService inboxMsgService;
    @Autowired
    LicInspNcEmailService licInspNcEmailService;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    EmailClient emailClient;
    @Value("${iais.email.sender}")
    private String mailSender;


    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        AuditTrailHelper.auditFunction("RequestForInformation Management", "RequestForInformation Config");

        List<String> licenceIds = (List<String>) ParamUtil.getSessionAttr(request, "licRfiIds");
        if(licenceIds.size()==0){
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTRANET/MohOnlineEnquiries");
            IaisEGPHelper.sendRedirect(bpc.request, bpc.response, url.toString());
        }
        List<LicPremisesDto> licPremisesDtos=hcsaLicenceClient.getPremisesByLicIds(licenceIds).getEntity();
        ParamUtil.setSessionAttr(request,"licPremisesDtos", (Serializable) licPremisesDtos);
        ParamUtil.setSessionAttr(request,"id",null);
        ParamUtil.setSessionAttr(request, "licenceNo", null);
        ParamUtil.setSessionAttr(request, "reqInfoId", null);

        // 		Start->OnStepProcess
    }



    public void preReqForInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>preReqForInfo>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        List<LicPremisesDto> licPremisesDtos = (List<LicPremisesDto>) ParamUtil.getSessionAttr(request,"licPremisesDtos");
        List<LicPremisesReqForInfoDto> licPremisesReqForInfoDtoLists=IaisCommonUtils.genNewArrayList();
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        for(LicPremisesDto licPremisesDto:licPremisesDtos){
            List<LicPremisesReqForInfoDto> licPremisesReqForInfoDtoList= requestForInformationService.searchLicPremisesReqForInfo(licPremisesDto.getId());
            licPremisesReqForInfoDtoLists.addAll(licPremisesReqForInfoDtoList);
            for (LicPremisesReqForInfoDto licPreRfi:licPremisesReqForInfoDtoList
            ) {
                if(!licPreRfi.getRequestUser().equals(loginContext.getUserName())){
                    licPremisesReqForInfoDtoLists.remove(licPreRfi);
                }
            }
        }

        ParamUtil.setRequestAttr(request, "isValid", "Y");

        if(!licPremisesReqForInfoDtoLists.isEmpty()) {
            for (LicPremisesReqForInfoDto licPreRfi:licPremisesReqForInfoDtoLists
            ) {
                try {
                    licPreRfi.setEmail(IaisEGPHelper.getLicenseeEmailAddrs(licPreRfi.getLicenseeId()).get(0));
                }catch (Exception e){
                    licPreRfi.setEmail("-");
                }
            }
        }

        ParamUtil.setRequestAttr(request,"licPreReqForInfoDtoList", licPremisesReqForInfoDtoLists);

    }

    public void doReqForInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>doReqForInfo>>>>>>>>>>>>>>>>requestForInformation");
        try {
            String reqInfoId = ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            ParamUtil.setSessionAttr(bpc.request, "reqInfoId", reqInfoId);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        // 		doReqForInfo->OnStepProcess
    }

    public void preNewRfi(BaseProcessClass bpc) {
        log.info("=======>>>>>preNewRfi>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String[] lengths=ParamUtil.getStrings(request,"lengths");
        List<String> docTitle=IaisCommonUtils.genNewArrayList();
        if(lengths!=null){
            for (String len:lengths
            ) {
                docTitle.add(ParamUtil.getString(request,"docTitle"+len));
            }
        }else {
            docTitle=null;
        }
        String[] lengthsInfo=ParamUtil.getStrings(request,"lengthsInfo");
        List<String> infoTitle=IaisCommonUtils.genNewArrayList();
        if(lengths!=null){
            for (String len:lengthsInfo
            ) {
                infoTitle.add(ParamUtil.getString(request,"infoTitle"+len));
            }
        }else {
            infoTitle=null;
        }

        String decision=ParamUtil.getString(request, "decision");
        String date= ParamUtil.getString(request, "Due_date");
        String rfiTitle=ParamUtil.getString(request, "rfiTitle");
        String infoChk=ParamUtil.getString(request, "info");
        String docChk=ParamUtil.getString(request, "doc");
        String licenceNo=ParamUtil.getString(request,"licenceNo");
        String rfiStatus=ParamUtil.getString(request,"status");
        NewRfiPageListDto newRfiPageListDto=new NewRfiPageListDto();
        newRfiPageListDto.setDate(date);
        newRfiPageListDto.setDecision(decision);
        newRfiPageListDto.setLicenceNo(licenceNo);
        newRfiPageListDto.setRfiTitle(rfiTitle);
        newRfiPageListDto.setInfoChk(infoChk);
        newRfiPageListDto.setDocChk(docChk);
        newRfiPageListDto.setStatus(rfiStatus);
        newRfiPageListDto.setDocTitle(docTitle);
        newRfiPageListDto.setInfoTitle(infoTitle);
        ParamUtil.setRequestAttr(bpc.request, "newRfi", newRfiPageListDto);

        String[] status=new String[]{RequestForInformationConstants.RFI_NEW};
        List<SelectOption> salutationStatusList= MasterCodeUtil.retrieveOptionsByCodes(status);
        ParamUtil.setSessionAttr(bpc.request, "salutationStatusList", (Serializable) salutationStatusList);

        List<LicPremisesDto> licPremisesDtos = (List<LicPremisesDto>) ParamUtil.getSessionAttr(request,"licPremisesDtos");
        List<SelectOption> salutationLicList= IaisCommonUtils.genNewArrayList();
        Map<String, String> licLicPremMap=IaisCommonUtils.genNewHashMap();
        for(LicPremisesDto licPremisesDto:licPremisesDtos){
            SelectOption selectLicOption=new SelectOption();
            selectLicOption.setValue(licPremisesDto.getId());
            LicenceDto licenceDto=hcsaLicenceClient.getLicenceDtoById(licPremisesDto.getLicenceId()).getEntity();
            selectLicOption.setText(licenceDto.getLicenceNo());
            licLicPremMap.put(licenceDto.getLicenceNo(),licPremisesDto.getId());
            salutationLicList.add(selectLicOption);
        }
        ParamUtil.setSessionAttr(bpc.request, "salutationLicList", (Serializable) salutationLicList);
        ParamUtil.setSessionAttr(bpc.request, "licLicPremMap", (Serializable) licLicPremMap);
        // 		preNewRfi->OnStepProcess
    }
    public void doCreateRequest(BaseProcessClass bpc) throws ParseException, IOException, TemplateException {
        log.info("=======>>>>>doCreateRequest>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        errorMap=validate(request);
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
            //
            return;
        }


        String licenceNo = ParamUtil.getString(request, "licenceNo");
        String date= ParamUtil.getString(request, "Due_date");
        String rfiTitle=ParamUtil.getString(request, "rfiTitle");
        String reqType=ParamUtil.getString(request,"doc");
        String reqTypeInfo=ParamUtil.getString(request,"info");
        String[] lengths=ParamUtil.getStrings(request,"lengths");
        List<String> docTitle=IaisCommonUtils.genNewArrayList();
        if(lengths!=null){
            for (String len:lengths
            ) {
                docTitle.add(ParamUtil.getString(request,"docTitle"+len));
            }
        }else {
            docTitle=null;
        }
        String[] lengthsInfo=ParamUtil.getStrings(request,"lengthsInfo");
        List<String> infoTitle=IaisCommonUtils.genNewArrayList();
        if(lengths!=null){
            for (String len:lengthsInfo
            ) {
                infoTitle.add(ParamUtil.getString(request,"infoTitle"+len));
            }
        }else {
            infoTitle=null;
        }

        Map<String, String> lice= (Map<String, String>) ParamUtil.getSessionAttr(request, "licLicPremMap");
        String licPremId=lice.get(licenceNo);
        LicenceViewDto licenceViewDto=licInspNcEmailService.getLicenceDtoByLicPremCorrId(licPremId);
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=new LicPremisesReqForInfoDto();
        licPremisesReqForInfoDto.setReqType(RequestForInformationConstants.AD_HOC);
        Date dueDate;
        Calendar calendar = Calendar.getInstance();
        if(!StringUtil.isEmpty(date)){
            dueDate=Formatter.parseDate(date);
        }
        else {
            calendar.add(Calendar.DATE,14);
            dueDate =calendar.getTime();
        }
        licPremisesReqForInfoDto.setDueDateSubmission(dueDate);
        licPremisesReqForInfoDto.setLicPremId(licPremId);

        boolean isNeedDoc=false;
        List<LicPremisesReqForInfoDocDto> licPremisesReqForInfoDocDtos=IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(reqType)&&"documents".equals(reqType)) {
            isNeedDoc = true;
            for(String docTi :docTitle){
                LicPremisesReqForInfoDocDto licPremisesReqForInfoDocDto1=new LicPremisesReqForInfoDocDto();
                licPremisesReqForInfoDocDto1.setTitle(docTi);
                licPremisesReqForInfoDocDto1.setDocName("");
                licPremisesReqForInfoDocDtos.add(licPremisesReqForInfoDocDto1);
            }
        }
        List<LicPremisesReqForInfoReplyDto> licPremisesReqForInfoReplyDtos=IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(reqTypeInfo)&&"information".equals(reqTypeInfo)) {
            isNeedDoc = true;
            for(String infoTi :infoTitle){
                LicPremisesReqForInfoReplyDto licPremisesReqForInfoReplyDto=new LicPremisesReqForInfoReplyDto();
                licPremisesReqForInfoReplyDto.setTitle(infoTi);
                licPremisesReqForInfoReplyDtos.add(licPremisesReqForInfoReplyDto);
            }
        }
        licPremisesReqForInfoDto.setStatus(RequestForInformationConstants.RFI_NEW);
        licPremisesReqForInfoDto.setNeedDocument(isNeedDoc);
        licPremisesReqForInfoDto.setOfficerRemarks(rfiTitle);
        licPremisesReqForInfoDto.setRequestDate(new Date());
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        licPremisesReqForInfoDto.setRequestUser(loginContext.getUserName());
        licPremisesReqForInfoDto.setLicPremisesReqForInfoDocDto(licPremisesReqForInfoDocDtos);
        licPremisesReqForInfoDto.setLicPremisesReqForInfoReplyDtos(licPremisesReqForInfoReplyDtos);
        LicPremisesReqForInfoDto licPremisesReqForInfoDto1 = requestForInformationService.createLicPremisesReqForInfo(licPremisesReqForInfoDto);

        String templateId= MsgTemplateConstants.MSG_TEMPLATE_RFI;
        InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(templateId);
        String licenseeId=requestForInformationService.getLicPreReqForInfo(licPremisesReqForInfoDto1.getId()).getLicenseeId();
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
        Map<String,Object> map=IaisCommonUtils.genNewHashMap();
        StringBuilder stringBuilder=new StringBuilder();
        int i=0;
        if(!StringUtil.isEmpty(reqTypeInfo)&&"information".equals(reqTypeInfo)){
            for ( i=0;i<licPremisesReqForInfoDto1.getLicPremisesReqForInfoReplyDtos().size();i++) {
                stringBuilder.append("<p>   ").append(i+1).append(". ").append("Information : ").append(licPremisesReqForInfoDto1.getLicPremisesReqForInfoReplyDtos().get(i).getTitle()).append("</p>");
            }
        }
        if(licPremisesReqForInfoDto1.isNeedDocument()){
            for (int j=0;j<licPremisesReqForInfoDto1.getLicPremisesReqForInfoDocDto().size();j++) {
                stringBuilder.append("<p>   ").append(j+i+1).append(". ").append("Documentations : ").append(licPremisesReqForInfoDto1.getLicPremisesReqForInfoDocDto().get(j).getTitle()).append("</p>");
            }
        }
        map.put("APPLICANT_NAME",StringUtil.viewHtml(licenseeDto.getName()));
        map.put("APPLICATION_NUMBER",StringUtil.viewHtml(licenceNo));
        map.put("DETAILS",StringUtil.viewHtml(stringBuilder.toString()));
        map.put("EDITSELECT","");
        map.put("COMMENTS",StringUtil.viewHtml(""));
        String url = "https://" + systemParamConfig.getInterServerName() +
                "/hcsa-licence-web/eservice/INTERNET/MohClientReqForInfo" +
                "?licenseeId=" + licenseeId;
        map.put("A_HREF", url);
        map.put("MOH_NAME", StringUtil.viewHtml(AppConsts.MOH_AGENCY_NAME));
        String mesContext= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getMessageContent(),map);
        mesContext=mesContext.replace("Comments : -","");
        HashMap<String,String> mapPrem=IaisCommonUtils.genNewHashMap();
        mapPrem.put("licenseeId",licenseeId);

        EicRequestTrackingDto eicRequestTrackingDto=new EicRequestTrackingDto();
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        Date now = new Date();
        eicRequestTrackingDto.setActionClsName("com.ecquaria.cloud.moh.iais.service.RequestForInformationServiceImpl");
        eicRequestTrackingDto.setActionMethod("eicCallFeRfiLic");
        eicRequestTrackingDto.setModuleName("hcsa-licence-web-intranet");
        eicRequestTrackingDto.setDtoClsName(LicPremisesReqForInfoDto.class.getName());
        eicRequestTrackingDto.setDtoObject(JsonUtil.parseToJson(licPremisesReqForInfoDto1));
        eicRequestTrackingDto.setProcessNum(1);
        eicRequestTrackingDto.setFirstActionAt(now);
        eicRequestTrackingDto.setLastActionAt(now);
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        eicRequestTrackingDto.setRefNo(System.currentTimeMillis()+"");
        licPremisesReqForInfoDto1.setEventRefNo(eicRequestTrackingDto.getRefNo());
        licPremisesReqForInfoDto1.setAction("create");
        requestForInformationService.updateLicEicRequestTrackingDto(eicRequestTrackingDto);
        requestForInformationService.createFeRfiLicDto(licPremisesReqForInfoDto1);

        //send message to FE user.
        InterMessageDto interMessageDto = new InterMessageDto();
        interMessageDto.setMaskParams(mapPrem);
        interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
        List<LicAppCorrelationDto> licAppCorrelationDtos=hcsaLicenceClient.getLicCorrBylicId(licenceViewDto.getLicenceDto().getId()).getEntity();
        String subject=rfiEmailTemplateDto.getSubject().replace("Application Number",StringUtil.viewHtml(licenceNo));
        interMessageDto.setSubject(subject);
        interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED);
        String messageNo = inboxMsgService.getMessageNo();
        interMessageDto.setRefNo(messageNo);
        HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(licenceViewDto.getLicenceDto().getSvcName()).getEntity();
        interMessageDto.setService_id(svcDto.getId());
        interMessageDto.setMsgContent(mesContext);
        interMessageDto.setStatus(MessageConstants.MESSAGE_STATUS_UNREAD);
        interMessageDto.setUserId(licenseeId);
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        inboxMsgService.saveInterMessage(interMessageDto);
        log.debug(StringUtil.changeForLog("the do requestForInformation end ...."));

        try {
            EmailDto emailDto=new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject(subject);
            emailDto.setSender(mailSender);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(licPremId);
            emailClient.sendNotification(emailDto).getEntity();
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        HashMap msg=IaisCommonUtils.genNewHashMap();
        msg.put("Date",IaisEGPHelper.parseToString(new Date(), AppConsts.DEFAULT_DATE_FORMAT));
        ParamUtil.setRequestAttr(request,"ackMsg", MessageUtil.getMessageDesc("ACKRFI001",msg));



        // 		doCreateRequest->OnStepProcess
    }

    public void doCancel(BaseProcessClass bpc) {
        log.info("=======>>>>>doCancel>>>>>>>>>>>>>>>>requestForInformation");
        String reqInfoId = ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        requestForInformationService.deleteLicPremisesReqForInfo(reqInfoId);
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=new LicPremisesReqForInfoDto();
        licPremisesReqForInfoDto.setId(reqInfoId);
        licPremisesReqForInfoDto.setStatus(RequestForInformationConstants.RFI_CLOSE);
        licPremisesReqForInfoDto.setAction("delete");
        EicRequestTrackingDto eicRequestTrackingDto=new EicRequestTrackingDto();
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        Date now = new Date();
        eicRequestTrackingDto.setActionClsName("com.ecquaria.cloud.moh.iais.service.RequestForInformationServiceImpl");
        eicRequestTrackingDto.setActionMethod("eicCallFeRfiLic");
        eicRequestTrackingDto.setModuleName("hcsa-licence-web-intranet");
        eicRequestTrackingDto.setDtoClsName(LicPremisesReqForInfoDto.class.getName());
        eicRequestTrackingDto.setDtoObject(JsonUtil.parseToJson(licPremisesReqForInfoDto));
        eicRequestTrackingDto.setProcessNum(1);
        eicRequestTrackingDto.setFirstActionAt(now);
        eicRequestTrackingDto.setLastActionAt(now);
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        eicRequestTrackingDto.setRefNo(System.currentTimeMillis()+"");
        licPremisesReqForInfoDto.setEventRefNo(eicRequestTrackingDto.getRefNo());
        requestForInformationService.updateLicEicRequestTrackingDto(eicRequestTrackingDto);
        requestForInformationService.createFeRfiLicDto(licPremisesReqForInfoDto);
        // 		doCancel->OnStepProcess
    }


    public void preViewRfi(BaseProcessClass bpc) {
        log.info("=======>>>>>preViewRfi>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String id = (String) ParamUtil.getSessionAttr(bpc.request, "reqInfoId");
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=requestForInformationService.getLicPreReqForInfo(id);
        licPremisesReqForInfoDto.setOfficerRemarks(licPremisesReqForInfoDto.getOfficerRemarks());
        ParamUtil.setRequestAttr(request,"licPreReqForInfoDto",licPremisesReqForInfoDto);

        String[] status=new String[]{RequestForInformationConstants.RFI_RETRIGGER,RequestForInformationConstants.RFI_CLOSE};
        List<SelectOption> salutationStatusList= MasterCodeUtil.retrieveOptionsByCodes(status);
        ParamUtil.setSessionAttr(bpc.request, "salutationStatusList", (Serializable) salutationStatusList);
        // 		preViewRfi->OnStepProcess
    }
    public void doUpdate(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>preCancel>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String reqInfoId = ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        String date=ParamUtil.getString(request, "Due_date");
        Date dueDate;
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if(date==null){
            errorMap.put("Due_date","ERR0010");
        }else {
            date= ParamUtil.getString(request, "Due_date");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
            Date tomorrow= calendar.getTime();
            String now=new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).format(tomorrow);
            if(date.compareTo(now) <0 ){
                errorMap.put("Due_date","Due Date should be a future Date.");
            }
        }
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
            //
            return;
        }

        Calendar calendar = Calendar.getInstance();
        if(!StringUtil.isEmpty(date)){
            dueDate= Formatter.parseDate(date);
        }
        else {
            calendar.add(Calendar.DATE,RequestForInformationConstants.REMIND_INTERVAL_DAY);
            dueDate =calendar.getTime();
        }
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=requestForInformationService.getLicPreReqForInfo(reqInfoId);
        licPremisesReqForInfoDto.setDueDateSubmission(dueDate);
        licPremisesReqForInfoDto.setStatus(RequestForInformationConstants.RFI_RETRIGGER);
        requestForInformationService.updateLicPremisesReqForInfo(licPremisesReqForInfoDto);
        licPremisesReqForInfoDto.setAction("update");
        EicRequestTrackingDto eicRequestTrackingDto=new EicRequestTrackingDto();
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        Date now = new Date();
        eicRequestTrackingDto.setActionClsName("com.ecquaria.cloud.moh.iais.service.RequestForInformationServiceImpl");
        eicRequestTrackingDto.setActionMethod("eicCallFeRfiLic");
        eicRequestTrackingDto.setModuleName("hcsa-licence-web-intranet");
        eicRequestTrackingDto.setDtoClsName(LicPremisesReqForInfoDto.class.getName());
        eicRequestTrackingDto.setRefNo(System.currentTimeMillis()+"");
        licPremisesReqForInfoDto.setEventRefNo(eicRequestTrackingDto.getRefNo());
        eicRequestTrackingDto.setDtoObject(JsonUtil.parseToJson(licPremisesReqForInfoDto));
        eicRequestTrackingDto.setProcessNum(1);
        eicRequestTrackingDto.setFirstActionAt(now);
        eicRequestTrackingDto.setLastActionAt(now);
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        requestForInformationService.updateLicEicRequestTrackingDto(eicRequestTrackingDto);
        requestForInformationService.createFeRfiLicDto(licPremisesReqForInfoDto);
        // 		doUpdate->OnStepProcess
    }

    private Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String[] lengths=ParamUtil.getStrings(request,"lengths");
        String[] lengthsInfo=ParamUtil.getStrings(request,"lengthsInfo");
        String infoChk=ParamUtil.getString(request, "info");
        String docChk=ParamUtil.getString(request, "doc");
        if(infoChk==null && docChk==null){
            errMap.put("rfiSelect","Please select Type of Required Information.");

        }
        if(docChk!=null){
            for (String len:lengths
            ) {
                String s=ParamUtil.getString(request,"docTitle"+len);
                if(StringUtil.isEmpty(s)){
                    errMap.put("docTitle"+len,"ERR0010");
                }
            }
        }
        if(infoChk!=null){
            for (String len:lengthsInfo
            ) {
                String s=ParamUtil.getString(request,"infoTitle"+len);
                if(StringUtil.isEmpty(s)){
                    errMap.put("infoTitle"+len,"ERR0010");
                }
            }
        }
        String date=ParamUtil.getDate(request, "Due_date");

        if(date==null){
            errMap.put("Due_date","ERR0010");
        }else {
            date= ParamUtil.getString(request, "Due_date");
            String now=new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).format(new Date());
            if(date.compareTo(now) <0 ){
                errMap.put("Due_date","Due Date should be a future Date.");
            }
        }
        String rfiTitle=ParamUtil.getString(request, "rfiTitle");
        if(rfiTitle==null){
            errMap.put("rfiTitle","ERR0010");
        }
        String licenceNo=ParamUtil.getString(request, "licenceNo");
        Map<String, String> lice= (Map<String, String>) ParamUtil.getSessionAttr(request, "licLicPremMap");
        String licCorrId=lice.get(licenceNo);
        if(licenceNo==null){
            errMap.put("licenceNo","ERR0010");
        }else if(licCorrId==null){
            errMap.put("licenceNo","Please provide the correct licence no.");
            }else {
            List<LicPremisesReqForInfoDto> licPremisesReqForInfoDtoList= requestForInformationService.searchLicPremisesReqForInfo(licCorrId);
            if(!licPremisesReqForInfoDtoList.isEmpty()) {
                for (LicPremisesReqForInfoDto licPreRfi:licPremisesReqForInfoDtoList
                ) {
                    if(licPreRfi.getStatus().equals(RequestForInformationConstants.RFI_NEW)||licPreRfi.getStatus().equals(RequestForInformationConstants.RFI_RETRIGGER)){
                        errMap.put("LicencePending","Licence is still pending Applicant's input. Please do not submit any new Requset For Information.");
                    }
                }
            }
        }


        return errMap;
    }

    @GetMapping(value = "/file-repo")
    public @ResponseBody void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("file-repo start ...."));
        String fileRepoName = ParamUtil.getRequestString(request, "fileRepoName");
        String maskFileRepoIdName = ParamUtil.getRequestString(request, "filerepo");
        String fileRepoId = ParamUtil.getMaskedString(request, maskFileRepoIdName);
        if(StringUtil.isEmpty(fileRepoId)){
            log.debug(StringUtil.changeForLog("file-repo id is empty"));
            return;
        }
        byte[] fileData =requestForInformationService.downloadFile(fileRepoId);
        response.addHeader("Content-Disposition", "attachment;filename=" + fileRepoName);
        response.addHeader("Content-Length", "" + fileData.length);
        response.setContentType("application/x-octet-stream");
        OutputStream ops = new BufferedOutputStream(response.getOutputStream());
        ops.write(fileData);
        ops.close();
        ops.flush();
        log.debug(StringUtil.changeForLog("file-repo end ...."));
    }

    @GetMapping(value = "/new-rfi-html")
    public @ResponseBody String genNewRfiHtml(HttpServletRequest request){
        log.debug(StringUtil.changeForLog("the genPublicHolidayHtml start ...."));
        String length = ParamUtil.getString(request,"Length");

        if(length==null){
            length="0";
        }

        String sql = SqlMap.INSTANCE.getSql("ReqForInfoQuery", "rfi-new").getSqlStr();


//        List<LicPremisesDto> licPremisesDtos = (List<LicPremisesDto>) ParamUtil.getSessionAttr(request,"licPremisesDtos");
//        List<SelectOption> salutationLicList= IaisCommonUtils.genNewArrayList();
//        for(LicPremisesDto licPremisesDto:licPremisesDtos){
//            SelectOption selectLicOption=new SelectOption();
//            selectLicOption.setValue(licPremisesDto.getId());
//            LicenceDto licenceDto=hcsaLicenceClient.getLicenceDtoById(licPremisesDto.getLicenceId()).getEntity();
//            selectLicOption.setText(licenceDto.getLicenceNo());
//            salutationLicList.add(selectLicOption);
//        }
//        Map<String,String> rfiLicSelect = IaisCommonUtils.genNewHashMap();
//        rfiLicSelect.put("name", "licenceNo"+length);
//        rfiLicSelect.put("style", "display: none;");
//        String salutationLicSelectStr = generateDropDownHtml(rfiLicSelect, salutationLicList,"Please Select", null);
//        sql = sql.replace("(rfiLicSelect)", salutationLicSelectStr);

        sql=sql.replaceAll("indexRfi",length);

        return sql;
    }

    @GetMapping(value = "/new-rfi-info-html")
    public @ResponseBody String genNewRfiInfoHtml(HttpServletRequest request){
        log.debug(StringUtil.changeForLog("the genPublicHolidayHtml start ...."));
        String length = ParamUtil.getString(request,"Length");

        if(length==null){
            length="0";
        }

        String sql = SqlMap.INSTANCE.getSql("ReqForInfoQuery", "rfi-info-new").getSqlStr();


        sql=sql.replaceAll("indexRfi",length);

        return sql;
    }

    public static String generateDropDownHtml(Map<String, String> premisesOnSiteAttr, List<SelectOption> selectOptionList, String firestOption, String checkedVal){
        StringBuilder sBuffer = new StringBuilder();
        sBuffer.append("<select ");
        for(Map.Entry<String, String> entry : premisesOnSiteAttr.entrySet()){
            sBuffer.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\" ");
        }
        sBuffer.append(" >");
        if(!StringUtil.isEmpty(firestOption)){
            sBuffer.append("<option value=\"\">").append(firestOption).append("</option>");
        }
        for(SelectOption sp:selectOptionList){
            if(!StringUtil.isEmpty(checkedVal)){
                if(checkedVal.equals(sp.getValue())){
                    sBuffer.append("<option selected=\"selected\" value=\"").append(sp.getValue()).append("\">").append(sp.getText()).append("</option>");
                }else{
                    sBuffer.append("<option value=\"").append(sp.getValue()).append("\">").append(sp.getText()).append("</option>");
                }
            }else{
                sBuffer.append("<option value=\"").append(sp.getValue()).append("\">").append(sp.getText()).append("</option>");
            }
        }
        sBuffer.append("</select>");
        String classNameValue = premisesOnSiteAttr.get("class");
        String className = "premSelect";
        if(!StringUtil.isEmpty(classNameValue)){
            className =  classNameValue;
        }
        sBuffer.append("<div class=\"nice-select ").append(className).append("\" tabindex=\"0\">");
        if(!StringUtil.isEmpty(checkedVal)){
            sBuffer.append("<span selected=\"selected\" class=\"current\">").append(checkedVal).append("</span>");
        }else{
            if(!StringUtil.isEmpty(firestOption)){
                sBuffer.append("<span class=\"current\">").append(firestOption).append("</span>");
            }else{
                sBuffer.append("<span class=\"current\">").append(selectOptionList.get(0).getText()).append("</span>");
            }
        }
        sBuffer.append("<ul class=\"list mCustomScrollbar _mCS_2 mCS_no_scrollbar\">")
                .append("<div id=\"mCSB_2\" class=\"mCustomScrollBox mCS-light mCSB_vertical mCSB_inside\" tabindex=\"0\" style=\"max-height: none;\">")
                .append("<div id=\"mCSB_2_container\" class=\"mCSB_container mCS_y_hidden mCS_no_scrollbar_y\" style=\"position:relative; top:0; left:0;\" dir=\"ltr\">");

        if(!StringUtil.isEmpty(checkedVal)){
            for(SelectOption kv:selectOptionList){
                if(checkedVal.equals(kv.getValue())){
                    sBuffer.append("<li selected=\"selected\" data-value=\"").append(kv.getValue()).append("\" class=\"option selected\">").append(kv.getText()).append("</li>");
                }else{
                    sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option\">").append(kv.getText()).append("</li>");
                }
            }
        }else if(!StringUtil.isEmpty(firestOption)){
            sBuffer.append("<li data-value=\"\" class=\"option selected\">").append(firestOption).append("</li>");
            for(SelectOption kv:selectOptionList){
                sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option\">").append(kv.getText()).append("</li>");
            }
        }else{
            for(int i = 0;i<selectOptionList.size();i++){
                SelectOption kv = selectOptionList.get(i);
                if(i == 0){
                    sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option selected\">").append(kv.getText()).append("</li>");
                }else{
                    sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option\">").append(kv.getText()).append("</li>");
                }
            }
        }

        sBuffer.append("</div>")
                .append("<div id=\"mCSB_2_scrollbar_vertical\" class=\"mCSB_scrollTools mCSB_2_scrollbar mCS-light mCSB_scrollTools_vertical\" style=\"display: none;\">")
                .append("<div class=\"mCSB_draggerContainer\">")
                .append("<div id=\"mCSB_2_dragger_vertical\" class=\"mCSB_dragger\" style=\"position: absolute; min-height: 30px; top: 0px; height: 0px;\">")
                .append("<div class=\"mCSB_dragger_bar\" style=\"line-height: 30px;\">")
                .append("</div>")
                .append("</div>")
                .append("<div class=\"mCSB_draggerRail\"></div>")
                .append("</div>")
                .append("</div>")
                .append("</div>")
                .append("</ul>")
                .append("</div>");
        return sBuffer.toString();
    }
}
