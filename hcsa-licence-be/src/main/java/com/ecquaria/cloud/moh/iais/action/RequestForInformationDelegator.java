package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoReplyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.NewRfiPageListDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.LicInspNcEmailService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Comparator;
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
    ApplicationClient applicationClient;
    @Autowired
    LicInspNcEmailService licInspNcEmailService;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    EmailClient emailClient;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    OrganizationClient organizationClient;
    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_LICENCE_MANAGEMENT, AuditTrailConsts.FUNCTION_REQUEST_FOR_INFORMATION);

        List<String> licenceIds = (List<String>) ParamUtil.getSessionAttr(request, "licRfiIds");
        if(licenceIds.size()==0){
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTRANET/MohLicenceManagement");
            IaisEGPHelper.sendRedirect(bpc.request, bpc.response, url.toString());
        }
        List<LicPremisesDto> licPremisesDtos=hcsaLicenceClient.getPremisesByLicIds(licenceIds).getEntity();
        ParamUtil.setSessionAttr(request,"licPremisesDtos", (Serializable) licPremisesDtos);
        ParamUtil.setSessionAttr(request,"id",null);
        ParamUtil.setSessionAttr(request, "licenceNo", null);
        ParamUtil.setSessionAttr(request, "reqInfoId", null);
        ParamUtil.setSessionAttr(request,"rfiMulNum",systemParamConfig.getAdhocRfiMultipleRequest());

        // 		Start->OnStepProcess
    }



    public void preReqForInfo(BaseProcessClass bpc) {
        log.info("=======>>>>>preReqForInfo>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        List<LicPremisesDto> licPremisesDtos = (List<LicPremisesDto>) ParamUtil.getSessionAttr(request,"licPremisesDtos");
        List<LicPremisesReqForInfoDto> licPremisesReqForInfoDtoLists=IaisCommonUtils.genNewArrayList();
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String searchEmail=ParamUtil.getString(request,"searchEmail");
        ParamUtil.setSessionAttr(request,"searchEmail",searchEmail);
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
        List<LicPremisesReqForInfoDto> licPremisesReqForInfoDtoListSearch=IaisCommonUtils.genNewArrayList();
        licPremisesReqForInfoDtoListSearch.addAll(licPremisesReqForInfoDtoLists);
        if(!licPremisesReqForInfoDtoLists.isEmpty()) {
            for (LicPremisesReqForInfoDto licPreRfi:licPremisesReqForInfoDtoLists
            ) {
                try {
                    licPreRfi.setEmail(IaisEGPHelper.getLicenseeEmailAddrs(licPreRfi.getLicenseeId()).get(0));
                }catch (Exception e){
                    licPreRfi.setEmail("-");
                }
                if(!StringUtil.isEmpty(searchEmail)){
                    if(!licPreRfi.getEmail().contains(searchEmail)){
                        licPremisesReqForInfoDtoListSearch.remove(licPreRfi);
                    }
                }
            }
        }
        licPremisesReqForInfoDtoListSearch.sort(Comparator.comparing(LicPremisesReqForInfoDto::getRequestDate).reversed());
        ParamUtil.setRequestAttr(request,"licPreReqForInfoDtoList", licPremisesReqForInfoDtoListSearch);

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
        if(lengthsInfo!=null){
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
        String actionType = ParamUtil.getRequestString(bpc.request, "crud_action_type");
        if(date==null&& "new".equals(actionType)){
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.DATE, systemParamConfig.getRfiDueDate());
            String dueDay=new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).format(calendar.getTime());
            newRfiPageListDto.setDate(dueDay);
        }
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
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
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
        if(lengthsInfo!=null){
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
        if(docTitle!=null && !StringUtil.isEmpty(reqType) && "documents".equals(reqType)) {
            isNeedDoc = true;
            int seqNum=1;
            for(String docTi :docTitle){
                LicPremisesReqForInfoDocDto licPremisesReqForInfoDocDto1=new LicPremisesReqForInfoDocDto();
                licPremisesReqForInfoDocDto1.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                licPremisesReqForInfoDocDto1.setTitle(docTi);
                licPremisesReqForInfoDocDto1.setDocName("");
                licPremisesReqForInfoDocDto1.setSeqNum(seqNum);
                licPremisesReqForInfoDocDtos.add(licPremisesReqForInfoDocDto1);
                seqNum++;
            }
        }
        List<LicPremisesReqForInfoReplyDto> licPremisesReqForInfoReplyDtos=IaisCommonUtils.genNewArrayList();
        if(infoTitle!=null && !StringUtil.isEmpty(reqTypeInfo)&&"information".equals(reqTypeInfo)) {

            for(String infoTi :infoTitle){
                LicPremisesReqForInfoReplyDto licPremisesReqForInfoReplyDto=new LicPremisesReqForInfoReplyDto();
                licPremisesReqForInfoReplyDto.setTitle(infoTi);
                licPremisesReqForInfoReplyDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
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

        String licenseeId=requestForInformationService.getLicPreReqForInfo(licPremisesReqForInfoDto1.getId()).getLicenseeId();
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
        List<OrgUserDto> orgUserDtoList = organizationClient.getOrgUserAccountSampleDtoByOrganizationId(licenseeDto.getOrganizationId()).getEntity();
        String applicantName=licenseeDto.getName();
        if(orgUserDtoList!=null&&orgUserDtoList.get(0)!=null){
            applicantName=orgUserDtoList.get(0).getDisplayName();
        }
        StringBuilder stringBuilder=requestForInformationService.setEmailAppend(licPremisesReqForInfoDto1, !StringUtil.isEmpty(reqTypeInfo)&&"information".equals(reqTypeInfo));
        String url = "https://" + systemParamConfig.getInterServerName() +
                "/hcsa-licence-web/eservice/INTERNET/MohClientReqForInfo" +
                "?licenseeId=" + licenseeId;
        Map<String,Object> map=IaisCommonUtils.genNewHashMap();
        map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{RequestForInformationConstants.AD_HOC}).get(0).getText());
        map.put("ApplicationNumber", licenceNo);
        InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI);
        String subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
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


        try {

            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
            Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
            emailMap.put("ApplicantName", applicantName);
            emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{RequestForInformationConstants.AD_HOC}).get(0).getText());
            emailMap.put("ApplicationNumber", licenceNo);
            emailMap.put("ApplicationDate", Formatter.formatDate(new Date()));
            emailMap.put("email", systemParamConfig.getSystemAddressOne());
            emailMap.put("TATtime", Formatter.formatDate(dueDate));
            emailMap.put("Remarks", stringBuilder.toString());
            emailMap.put("systemLink", loginUrl);
            emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
            emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI);
            emailParam.setTemplateContent(emailMap);
            emailParam.setQueryCode(licenceNo);
            emailParam.setReqRefNum(licenceNo);
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
            emailParam.setRefId(licenceViewDto.getLicenceDto().getId());
            emailParam.setSubject(subject);
            //email
            notificationHelper.sendNotification(emailParam);
            //sms
            rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_SMS);
            subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
            EmailParam smsParam = new EmailParam();
            smsParam.setTemplateContent(emailMap);
            smsParam.setQueryCode(licenceNo);
            smsParam.setReqRefNum(licenceNo);
            smsParam.setRefId(licenceViewDto.getLicenceDto().getId());
            smsParam.setSubject(subject);
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_SMS);
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
            notificationHelper.sendNotification(smsParam);
            //msg
            EmailParam msgParam = new EmailParam();
            msgParam.setQueryCode(licenceNo);
            msgParam.setReqRefNum(licenceNo);
            HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(licenceViewDto.getLicenceDto().getSvcName()).getEntity();
            List<String> svcCode=IaisCommonUtils.genNewArrayList();
            svcCode.add(svcDto.getSvcCode());
            rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_MSG);
            subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
            msgParam.setSubject(subject);
            emailMap.put("systemLink", url);
            msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_MSG);
            msgParam.setTemplateContent(emailMap);
            msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_ACTION_REQUIRED);
            msgParam.setMaskParams(mapPrem);
            msgParam.setSvcCodeList(svcCode);
            msgParam.setRefId(licenceViewDto.getLicenceDto().getId());
            notificationHelper.sendNotification(msgParam);

        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        ParamUtil.setRequestAttr(request,"ackMsg", MessageUtil.dateIntoMessage("RFI_ACK001"));



        // 		doCreateRequest->OnStepProcess
    }

    public void doCancel(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>preCancel>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String status=ParamUtil.getString(bpc.request,"status");
        String reqInfoId = ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        String date=ParamUtil.getString(request, "Due_date");
        Date dueDate;
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if(status.equals(RequestForInformationConstants.RFI_NEW)||status.equals(RequestForInformationConstants.RFI_RETRIGGER)){
            if(date==null){
                errorMap.put("Due_date",MessageUtil.replaceMessage("GENERAL_ERR0006","Due Date","field"));
            }else {
                String newDate= ParamUtil.getString(request, "Due_date");
                dueDate=Formatter.parseDate(newDate);
                newDate=new SimpleDateFormat(SystemAdminBaseConstants.DATE_FORMAT).format(dueDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                Date tomorrow= calendar.getTime();
                String now=new SimpleDateFormat(SystemAdminBaseConstants.DATE_FORMAT).format(tomorrow);
                if(newDate.compareTo(now) <0 ){
                    errorMap.put("Due_date","PRF_ERR007");
                }
            }
        }
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=requestForInformationService.getLicPreReqForInfo(reqInfoId);
        if (!errorMap.isEmpty()) {
            LicenceDto licenceDto=new LicenceDto();
            licenceDto.setLicenceNo(licPremisesReqForInfoDto.getLicenceNo());
            WebValidationHelper.saveAuditTrailForNoUseResult(licenceDto,errorMap);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
            //
            return;
        }

        Calendar calendar = Calendar.getInstance();
        if(!StringUtil.isEmpty(date)){
            dueDate=Formatter.parseDate(date);
        }
        else {
            calendar.add(Calendar.DATE,systemParamConfig.getRfiDueDate());
            dueDate =calendar.getTime();
        }
        licPremisesReqForInfoDto.setDueDateSubmission(dueDate);
        licPremisesReqForInfoDto.setStatus(status);
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
    }


    public void preViewRfi(BaseProcessClass bpc) throws ParseException {
        log.info("=======>>>>>preViewRfi>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String id = (String) ParamUtil.getSessionAttr(bpc.request, "reqInfoId");
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=requestForInformationService.getLicPreReqForInfo(id);
        String date=ParamUtil.getString(request, "Due_date");
        String errMsg=ParamUtil.getRequestString(request,IntranetUserConstant.ERRORMSG);
        if(errMsg==null&&date!=null){
            licPremisesReqForInfoDto.setDueDateSubmission(Formatter.parseDate(date));
        }
        if(errMsg!=null&&date==null){
            licPremisesReqForInfoDto.setDueDateSubmission(null);
        }

        ParamUtil.setRequestAttr(request,"licPreReqForInfoDto",licPremisesReqForInfoDto);

        String[] status=new String[]{licPremisesReqForInfoDto.getStatus()};
        if(licPremisesReqForInfoDto.getStatus().equals(RequestForInformationConstants.RFI_CLOSE)){
            status=new String[]{RequestForInformationConstants.RFI_CLOSE_OFFICER,RequestForInformationConstants.RFI_RETRIGGER};
        }
        if(licPremisesReqForInfoDto.getStatus().equals(RequestForInformationConstants.RFI_CLOSE_OFFICER)){
            status=new String[]{RequestForInformationConstants.RFI_CLOSE_OFFICER};
        }
        List<SelectOption> salutationStatusList= MasterCodeUtil.retrieveOptionsByCodes(status);
        ParamUtil.setSessionAttr(bpc.request, "salutationStatusList", (Serializable) salutationStatusList);
        // 		preViewRfi->OnStepProcess
    }
    public void doUpdate(BaseProcessClass bpc) throws ParseException, IOException, TemplateException {
        log.info("=======>>>>>preCancel>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        String status=ParamUtil.getString(bpc.request,"status");
        String reqInfoId = ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        String date=ParamUtil.getString(request, "Due_date");
        Date dueDate;
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if(status.equals(RequestForInformationConstants.RFI_NEW)||status.equals(RequestForInformationConstants.RFI_RETRIGGER)){
            if(date==null){
                errorMap.put("Due_date",MessageUtil.replaceMessage("GENERAL_ERR0006","Due Date","field"));
            }else {
                String newDate= ParamUtil.getString(request, "Due_date");
                dueDate=Formatter.parseDate(newDate);
                newDate=new SimpleDateFormat(SystemAdminBaseConstants.DATE_FORMAT).format(dueDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                Date tomorrow= calendar.getTime();
                String now=new SimpleDateFormat(SystemAdminBaseConstants.DATE_FORMAT).format(tomorrow);
                if(newDate.compareTo(now) <0 ){
                    errorMap.put("Due_date","PRF_ERR007");
                }
            }
        }
        LicPremisesReqForInfoDto licPremisesReqForInfoDto=requestForInformationService.getLicPreReqForInfo(reqInfoId);
        if (!errorMap.isEmpty()) {
            LicenceDto licenceDto=new LicenceDto();
            licenceDto.setLicenceNo(licPremisesReqForInfoDto.getLicenceNo());
            WebValidationHelper.saveAuditTrailForNoUseResult(licenceDto,errorMap);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
            //
            return;
        }

        Calendar calendar = Calendar.getInstance();
        if(!StringUtil.isEmpty(date)){
            dueDate=Formatter.parseDate(date);
        }
        else {
            calendar.add(Calendar.DATE,systemParamConfig.getRfiDueDate());
            dueDate =calendar.getTime();
        }
        licPremisesReqForInfoDto.setDueDateSubmission(dueDate);
        licPremisesReqForInfoDto.setStatus(status);
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

        InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI);
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licPremisesReqForInfoDto.getLicenseeId());
        List<OrgUserDto> orgUserDtoList = organizationClient.getOrgUserAccountSampleDtoByOrganizationId(licenseeDto.getOrganizationId()).getEntity();
        String applicantName=licenseeDto.getName();
        if(orgUserDtoList!=null&&orgUserDtoList.get(0)!=null){
            applicantName=orgUserDtoList.get(0).getDisplayName();
        }
        Map<String,Object> map=IaisCommonUtils.genNewHashMap();
        StringBuilder stringBuilder=requestForInformationService.setEmailAppend(licPremisesReqForInfoDto, !StringUtil.isEmpty(licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos()));
        String url = "https://" + systemParamConfig.getInterServerName() +
                "/hcsa-licence-web/eservice/INTERNET/MohClientReqForInfo" +
                "?licenseeId=" + licPremisesReqForInfoDto.getLicenseeId();
        map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{RequestForInformationConstants.AD_HOC}).get(0).getText());
        map.put("ApplicationNumber", licPremisesReqForInfoDto.getLicenceNo());
        String subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
        HashMap<String,String> mapPrem=IaisCommonUtils.genNewHashMap();
        mapPrem.put("licenseeId",licPremisesReqForInfoDto.getLicenseeId());

        log.debug(StringUtil.changeForLog("the do requestForInformation end ...."));
        if(status.equals(RequestForInformationConstants.RFI_NEW)||status.equals(RequestForInformationConstants.RFI_RETRIGGER)){
            try {

                String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
                Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                emailMap.put("ApplicantName", applicantName);
                emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{RequestForInformationConstants.AD_HOC}).get(0).getText());
                emailMap.put("ApplicationNumber", licPremisesReqForInfoDto.getLicenceNo());
                emailMap.put("ApplicationDate", Formatter.formatDate(new Date()));
                emailMap.put("email", systemParamConfig.getSystemAddressOne());
                emailMap.put("TATtime", Formatter.formatDate(dueDate));
                emailMap.put("Remarks", stringBuilder.toString());
                emailMap.put("systemLink", loginUrl);
                emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
                emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI);
                emailParam.setTemplateContent(emailMap);
                emailParam.setQueryCode(licPremisesReqForInfoDto.getLicenceNo());
                emailParam.setReqRefNum(licPremisesReqForInfoDto.getLicenceNo());
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                LicenceViewDto licenceViewDto= hcsaLicenceClient.getLicenceViewDtoByLicPremCorrId(licPremisesReqForInfoDto.getLicPremId()).getEntity();
                emailParam.setRefId(licenceViewDto.getLicenceDto().getId());
                emailParam.setSubject(subject);
                //email
                notificationHelper.sendNotification(emailParam);
                //sms
                rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_SMS);
                subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
                EmailParam smsParam = new EmailParam();
                smsParam.setTemplateContent(emailMap);
                smsParam.setQueryCode(licPremisesReqForInfoDto.getLicenceNo());
                smsParam.setReqRefNum(licPremisesReqForInfoDto.getLicenceNo());
                smsParam.setRefId(licenceViewDto.getLicenceDto().getId());
                smsParam.setSubject(subject);
                smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_SMS);
                smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
                notificationHelper.sendNotification(smsParam);
                //msg
                EmailParam msgParam = new EmailParam();
                msgParam.setQueryCode(licPremisesReqForInfoDto.getLicenceNo());
                msgParam.setReqRefNum(licPremisesReqForInfoDto.getLicenceNo());
                HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(licenceViewDto.getLicenceDto().getSvcName()).getEntity();
                List<String> svcCode=IaisCommonUtils.genNewArrayList();
                svcCode.add(svcDto.getSvcCode());
                rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_MSG);
                subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
                msgParam.setSubject(subject);
                emailMap.put("systemLink", url);
                msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_MSG);
                msgParam.setTemplateContent(emailMap);
                msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_ACTION_REQUIRED);
                msgParam.setMaskParams(mapPrem);
                msgParam.setSvcCodeList(svcCode);
                msgParam.setRefId(licenceViewDto.getLicenceDto().getId());
                notificationHelper.sendNotification(msgParam);

            }catch (Exception e){
                log.error(e.getMessage(), e);
            }
        }


        // 		doUpdate->OnStepProcess
    }

    private Map<String, String> validate(HttpServletRequest request) throws ParseException {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String[] lengths=ParamUtil.getStrings(request,"lengths");
        String[] lengthsInfo=ParamUtil.getStrings(request,"lengthsInfo");
        String infoChk=ParamUtil.getString(request, "info");
        String docChk=ParamUtil.getString(request, "doc");
        if(infoChk==null && docChk==null&&systemParamConfig.getAdhocRfiMultipleRequest()>0){
            errMap.put("rfiSelect","PRF_ERR008");

        }
        String errTitle=MessageUtil.replaceMessage("GENERAL_ERR0006","Title","field");
        if(docChk!=null&&systemParamConfig.getAdhocRfiMultipleRequest()>0){
            int i=0;
            for (String len:lengths
            ) {
                String s=ParamUtil.getString(request,"docTitle"+len);
                if(StringUtil.isEmpty(s)){
                    errMap.put("docTitle"+i,errTitle);
                }
                i++;
            }
        }
        if(infoChk!=null&&systemParamConfig.getAdhocRfiMultipleRequest()>0){
            int i=0;
            for (String len:lengthsInfo
            ) {
                String s=ParamUtil.getString(request,"infoTitle"+len);
                if(StringUtil.isEmpty(s)){
                    errMap.put("infoTitle"+i,errTitle);
                }
                i++;
            }
        }
        String date=ParamUtil.getDate(request, "Due_date");

        if(date==null){
            errMap.put("Due_date",MessageUtil.replaceMessage("GENERAL_ERR0006","Due Date","field"));
        }else {
            date= ParamUtil.getString(request, "Due_date");
            Date dueDate=Formatter.parseDate(date);
            date=new SimpleDateFormat(SystemAdminBaseConstants.DATE_FORMAT).format(dueDate);
            String now=new SimpleDateFormat(SystemAdminBaseConstants.DATE_FORMAT).format(new Date());
            if(date.compareTo(now) <=0 ){
                errMap.put("Due_date","PRF_ERR007");
            }
        }
        String rfiTitle=ParamUtil.getString(request, "rfiTitle");
        if(rfiTitle==null){
            errMap.put("rfiTitle",MessageUtil.replaceMessage("GENERAL_ERR0006","Title","field"));
        }
        String licenceNo=ParamUtil.getString(request, "licenceNo");
        Map<String, String> lice= (Map<String, String>) ParamUtil.getSessionAttr(request, "licLicPremMap");
        String licCorrId=lice.get(licenceNo);
        if(licenceNo==null){
            errMap.put("licenceNo",MessageUtil.replaceMessage("GENERAL_ERR0006","Licence No","field"));
        }else if(licCorrId==null){
            errMap.put("licenceNo","PRF_ERR006");
        }else {
            List<LicPremisesReqForInfoDto> licPremisesReqForInfoDtoList= requestForInformationService.searchLicPremisesReqForInfo(licCorrId);
            if(!licPremisesReqForInfoDtoList.isEmpty()) {
                for (LicPremisesReqForInfoDto licPreRfi:licPremisesReqForInfoDtoList
                ) {
                    if(licPreRfi.getStatus().equals(RequestForInformationConstants.RFI_NEW)||licPreRfi.getStatus().equals(RequestForInformationConstants.RFI_RETRIGGER)){
                        errMap.put("LicencePending","PRF_ERR009");
                    }
                }
            }
        }


        return errMap;
    }

    @GetMapping(value = "/file-repo")
    public @ResponseBody void fileDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug(StringUtil.changeForLog("file-repo start ...."));
        String fileRepoName = ParamUtil.getRequestString(request, "fileRepoName");
        String maskFileRepoIdName = ParamUtil.getRequestString(request, "filerepo");
        String fileRepoId = ParamUtil.getMaskedString(request, maskFileRepoIdName);
        if(StringUtil.isEmpty(fileRepoId)){
            log.debug(StringUtil.changeForLog("file-repo id is empty"));
            return;
        }
        byte[] fileData =requestForInformationService.downloadFile(fileRepoId);
        if (fileData == null || fileData.length == 0) {
            IaisEGPHelper.redirectUrl(response, "https://" + request.getServerName() + "/main-web/404-error.jsp");
        } else {
            response.addHeader("Content-Disposition", "attachment;filename=\"" + fileRepoName+"\"");
            response.addHeader("Content-Length", "" + fileData.length);
            response.setContentType("application/x-octet-stream");
            OutputStream ops = new BufferedOutputStream(response.getOutputStream());
            ops.write(fileData);
            ops.close();
            ops.flush();
        }
        log.debug(StringUtil.changeForLog("file-repo end ...."));
    }

    @GetMapping(value = "/new-rfi-html")
    public @ResponseBody String genNewRfiHtml(HttpServletRequest request){
        log.debug(StringUtil.changeForLog("the genPublicHolidayHtml start ...."));
        String length = ParamUtil.getString(request,"Length");

        if(length==null){
            length="0";
        }
        int len = Integer.parseInt(length);
        String sql = SqlMap.INSTANCE.getSql("ReqForInfoQuery", "rfi-new").getSqlStr();


        sql=sql.replaceAll("indexTitleRfi",String.valueOf((len+1)));
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
        int len = Integer.parseInt(length);
        String sql = SqlMap.INSTANCE.getSql("ReqForInfoQuery", "rfi-info-new").getSqlStr();

        sql=sql.replaceAll("indexTitleRfi",String.valueOf((len+1)));
        sql=sql.replaceAll("indexRfi",length);

        return sql;
    }

}
