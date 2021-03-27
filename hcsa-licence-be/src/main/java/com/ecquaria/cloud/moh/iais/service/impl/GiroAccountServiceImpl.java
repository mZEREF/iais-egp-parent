package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountFormDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.OrganizationPremisesViewQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MessageTemplateUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.GiroAccountService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailHistoryCommonClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailSmsClient;
import com.ecquaria.cloud.moh.iais.service.client.GiroAccountBeClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GiroAccountServiceImpl
 *
 * @author junyu
 * @date 2021/3/3
 */
@Slf4j
@Service
public class GiroAccountServiceImpl implements GiroAccountService {

    @Autowired
    private LicEicClient licEicClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.email.sender}")
    private String mailSender;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Autowired
    private BeEicGatewayClient gatewayClient;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    GiroAccountBeClient giroAccountBeClient;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private EmailSmsClient emailSmsClient;
    @Autowired
    private EmailHistoryCommonClient emailHistoryCommonClient;
    @Override
    public SearchResult<GiroAccountInfoQueryDto> searchGiroInfoByParam(SearchParam searchParam) {
        return giroAccountBeClient.searchGiroInfoByParam(searchParam).getEntity();
    }

    @Override
    public SearchResult<OrganizationPremisesViewQueryDto> searchOrgPremByParam(SearchParam searchParam) {
        return giroAccountBeClient.searchOrgPremByParam(searchParam).getEntity();
    }

    @Override
    public List<GiroAccountInfoDto> createGiroAccountInfo(List<GiroAccountInfoDto> giroAccountInfoDto) {
        return giroAccountBeClient.createGiroAccountInfo(giroAccountInfoDto).getEntity();
    }

    @Override
    public void updateGiroAccountInfo(List<GiroAccountInfoDto> giroAccountInfoDto) {
        giroAccountBeClient.updateGiroAccountInfo(giroAccountInfoDto);
    }

    @Override
    public List<GiroAccountFormDocDto> findGiroAccountFormDocDtoListByAcctId(String acctId) {
        return giroAccountBeClient.findGiroAccountFormDocDtoListByAcctId(acctId).getEntity();
    }

    @Override
    public GiroAccountInfoDto findGiroAccountInfoDtoByAcctId(String acctId) {
        return giroAccountBeClient.findGiroAccountInfoDtoByAcctId(acctId).getEntity();
    }


    @Override
    public void syncFeGiroAcctDto(List<GiroAccountInfoDto> giroAccountInfoDtoList) {
        EicRequestTrackingDto trackDto = getLicEicRequestTrackingDtoByRefNo(giroAccountInfoDtoList.get(0).getEventRefNo());
        eicCallFeGiroLic(giroAccountInfoDtoList);
        trackDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        updateGiroAccountInfoTrackingDto(trackDto);

    }

    public void eicCallFeGiroLic(List<GiroAccountInfoDto> giroAccountInfoDtoList) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        log.info(StringUtil.changeForLog("=======>>>>>"+" Lic Giro Account Information Id :"+giroAccountInfoDtoList.get(0).getId()));

        gatewayClient.updateGiroAccountInfo(giroAccountInfoDtoList,
                signature.date(), signature.authorization(), signature2.date(), signature2.authorization());
    }

    @Override
    public void updateGiroAccountInfoTrackingDto(EicRequestTrackingDto licEicRequestTrackingDto) {
        licEicClient.saveEicTrack(licEicRequestTrackingDto);
    }


    public EicRequestTrackingDto getLicEicRequestTrackingDtoByRefNo(String refNo) {
        return licEicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
    }


    @Override
    public void sendEmailForGiroAccountAndSMSAndMessage(GiroAccountInfoDto giroAccountInfoDto) {
        try{
            List<LicenseeDto> licenseeDtos=organizationClient.getLicenseeByOrgId(giroAccountInfoDto.getOrganizationId()).getEntity();
            String applicationNumber = giroAccountInfoDto.getHciCode();
            Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
            subMap.put("ApplicationType", "HCI");
            subMap.put("ApplicationNumber", applicationNumber);
            String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_003_EMAIL,subMap);
            String smsSubject = getEmailSubject(MsgTemplateConstants. MSG_TEMPLATE_EN_FEP_003_SMS ,subMap);
            String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_003_MSG,subMap);
            log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
            log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
            log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
            Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
            List<OrgUserDto> orgUserDtoList = organizationClient.getOrgUserAccountSampleDtoByOrganizationId(giroAccountInfoDto.getOrganizationId()).getEntity();
            String applicantName=licenseeDtos.get(0).getName();
            if(orgUserDtoList!=null&&orgUserDtoList.get(0)!=null){
                applicantName=orgUserDtoList.get(0).getDisplayName();
            }
            templateContent.put("ApplicantName", applicantName);
            templateContent.put("ApplicationType",  "HCI");
            templateContent.put("ApplicationNumber", applicationNumber);
            //todo need create new giro account time
            templateContent.put("DDMMYYYY", Formatter.formatDateTime(new Date()));
            templateContent.put("email", systemParamConfig.getSystemAddressOne());
            String syName = "<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"<br/>"+AppConsts.MOH_AGENCY_NAME+"</b>";
            templateContent.put("MOH_AGENCY_NAME",syName);


            String emailContent = getEmailContent(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_003_EMAIL,templateContent);
            String smsContent = getEmailContent(MsgTemplateConstants. MSG_TEMPLATE_EN_FEP_003_SMS ,templateContent);
            String messageContent = getEmailContent(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_003_MSG,templateContent);
            EmailDto emailDto = new EmailDto();
            List<String> receiptEmail=IaisCommonUtils.genNewArrayList();
            List<String> mobile = IaisCommonUtils.genNewArrayList();

            for (OrgUserDto user:orgUserDtoList
                 ) {
                receiptEmail.add(user.getEmail());
                mobile.add(user.getMobileNo());

            }
            emailDto.setReceipts(receiptEmail);
            emailDto.setContent(emailContent);
            emailDto.setSubject(emailSubject);
            emailDto.setSender(this.mailSender);
            emailDto.setClientQueryCode(giroAccountInfoDto.getHciCode());
            emailDto.setReqRefNum(giroAccountInfoDto.getOrganizationId());
            emailSmsClient.sendEmail(emailDto, null);

            SmsDto smsDto = new SmsDto();
            smsDto.setSender(mailSender);
            smsDto.setContent(smsContent);
            smsDto.setOnlyOfficeHour(false);

            emailHistoryCommonClient.sendSMS(mobile, smsDto, giroAccountInfoDto.getHciCode());

            EmailParam msgParam = new EmailParam();
            msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_003_MSG);
            msgParam.setTemplateContent(templateContent);
            msgParam.setSubject(messageSubject);
            msgParam.setQueryCode(giroAccountInfoDto.getHciCode());
            msgParam.setReqRefNum(licenseeDtos.get(0).getId());
            msgParam.setRefId(licenseeDtos.get(0).getId());
//
            List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
            List<HcsaServiceDto> hcsaServiceDtoList = hcsaConfigClient.getActiveServices().getEntity();
            svcCodeList.add(hcsaServiceDtoList.get(0).getSvcCode());
            msgParam.setSvcCodeList(svcCodeList);
            msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            notificationHelper.sendNotification(msgParam);
            log.info("end send email sms and msg");
        }catch (Exception e){
            log.error(e.getMessage(),e);
            log.info("send app sumbit email fail");
        }
    }

    private String getEmailSubject(String templateId, Map<String, Object> subMap){
        String subject = "-";
        if(!StringUtil.isEmpty(templateId)){
            MsgTemplateDto emailTemplateDto =notificationHelper.getMsgTemplate(templateId);
            if(emailTemplateDto != null){
                try {
                    if(!IaisCommonUtils.isEmpty(subMap)){
                        subject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(),subMap);
                    }else{
                        subject = emailTemplateDto.getTemplateName();
                    }
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }
        return subject;
    }

    private String getEmailContent(String templateId, Map<String, Object> subMap){
        String mesContext = "-";
        if(!StringUtil.isEmpty(templateId)){
            MsgTemplateDto emailTemplateDto =notificationHelper.getMsgTemplate(templateId);
            if(emailTemplateDto != null){
                try {
                    if(!IaisCommonUtils.isEmpty(subMap)){
                        mesContext = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getMessageContent(), subMap);
                        //replace num
                        mesContext = MessageTemplateUtil.replaceNum(mesContext);
                    }else{
                        mesContext = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getMessageContent(), subMap);
                        //replace num
                        mesContext = MessageTemplateUtil.replaceNum(mesContext);
                    }
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }
        return mesContext;
    }

}
