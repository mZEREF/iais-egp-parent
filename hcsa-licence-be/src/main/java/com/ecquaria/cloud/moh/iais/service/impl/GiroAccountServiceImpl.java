package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountFormDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.OrganizationPremisesViewQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MessageTemplateUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.GiroAccountService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailHistoryCommonClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailSmsClient;
import com.ecquaria.cloud.moh.iais.service.client.GiroAccountBeClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MasterCodeClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * GiroAccountServiceImpl
 *
 * @author junyu
 * @date 2021/3/3
 */
@Slf4j
@Service
@EnableAsync
public class GiroAccountServiceImpl implements GiroAccountService {

    @Value("${iais.email.sender}")
    private String mailSender;
    @Autowired
    private BeEicGatewayClient gatewayClient;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private GiroAccountBeClient giroAccountBeClient;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private EmailSmsClient emailSmsClient;
    @Autowired
    private EmailHistoryCommonClient emailHistoryCommonClient;
    @Autowired
    HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private MasterCodeClient masterCodeClient;
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
        log.info(StringUtil.changeForLog("=======>>>>>"+" Lic Giro Account Information Id :"+giroAccountInfoDtoList.get(0).getId()));
        gatewayClient.callEicWithTrack(giroAccountInfoDtoList, gatewayClient::updateGiroAccountInfo,
                this.getClass(), "eicCallFeGiroLic");
    }

    /**
     * EIC Tracking List
     *
     * @param jsonList
     */
    public void eicCallFeGiroLic(String jsonList) {
        if (StringUtil.isEmpty(jsonList)) {
            return;
        }
        List<GiroAccountInfoDto> giroAccountInfoDtoList = JsonUtil.parseToList(jsonList, GiroAccountInfoDto.class);
        gatewayClient.updateGiroAccountInfo(giroAccountInfoDtoList);
    }

    @Override
    @Async("emailAsyncExecutor")
    public void sendEmailForGiroAccountAndSMSAndMessage(GiroAccountInfoDto giroAccountInfoDto,int size) {
        try{
            List<LicenseeDto> licenseeDtos=organizationClient.getLicenseeByOrgId(giroAccountInfoDto.getOrganizationId()).getEntity();
            LicenceDto licenceDto=hcsaLicenceClient.getLicenceDtoById(giroAccountInfoDto.getLicenceId()).getEntity();
            Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
            subMap.put("ApplicationType", licenceDto.getSvcName());
            subMap.put("ApplicationNumber", licenceDto.getLicenceNo());
            String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_003_EMAIL,subMap);
            String smsSubject = getEmailSubject(MsgTemplateConstants. MSG_TEMPLATE_EN_FEP_003_SMS ,subMap);
            String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_003_MSG,subMap);
            log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
            log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
            log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
            Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
            List<OrgUserDto> orgUserDtoList = organizationClient.getOrgUserAccountSampleDtoByOrganizationId(giroAccountInfoDto.getOrganizationId()).getEntity();
            String applicantName=licenseeDtos.get(0).getName();

            templateContent.put("ApplicantName", applicantName);
            templateContent.put("ApplicationType",  licenceDto.getSvcName());
            templateContent.put("ApplicationNumber", licenceDto.getLicenceNo());
            templateContent.put("DDMMYYYY", Formatter.formatDateTime(new Date()));
            templateContent.put("email", systemParamConfig.getSystemAddressOne());
            String syName = "<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"<br/>"+AppConsts.MOH_AGENCY_NAME+"</b>";
            templateContent.put("MOH_AGENCY_NAME",syName);


            String emailContent = getEmailContent(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_003_EMAIL,templateContent);
            String smsContent = getEmailContent(MsgTemplateConstants. MSG_TEMPLATE_EN_FEP_003_SMS ,templateContent);
            //String messageContent = getEmailContent(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_003_MSG,templateContent);
            EmailDto emailDto = new EmailDto();
            List<String> receiptEmail=IaisCommonUtils.genNewArrayList();
            List<String> mobile = IaisCommonUtils.genNewArrayList();

            if(!IaisCommonUtils.isEmpty(orgUserDtoList)){
                for (OrgUserDto user:orgUserDtoList) {
                    receiptEmail.add(user.getEmail());
                    mobile.add(user.getMobileNo());
                }
            }
            Set<String> set = IaisCommonUtils.genNewHashSet();
            set.addAll(receiptEmail);
            receiptEmail.clear();
            receiptEmail.addAll(set);
            emailDto.setReceipts(receiptEmail);
            emailDto.setContent(emailContent);
            emailDto.setSubject(emailSubject);
            emailDto.setSender(this.mailSender);
            emailDto.setClientQueryCode(giroAccountInfoDto.getOrganizationId());
            emailDto.setReqRefNum(giroAccountInfoDto.getOrganizationId());
            emailSmsClient.sendEmail(emailDto, null);
            set.clear();
            set.addAll(mobile);
            mobile.clear();
            mobile.addAll(set);
            SmsDto smsDto = new SmsDto();
            smsDto.setSender(mailSender);
            smsDto.setContent(smsContent);
            smsDto.setOnlyOfficeHour(false);

            emailHistoryCommonClient.sendSMS(mobile, smsDto, giroAccountInfoDto.getOrganizationId());

            Set<String> svcCodeSet = IaisCommonUtils.genNewHashSet();

            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(licenceDto.getSvcName());
            if(hcsaServiceDto!=null){
                svcCodeSet.add(hcsaServiceDto.getSvcCode());
            }

            List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
            svcCodeList.addAll(svcCodeSet);
            InterMessageDto interMessageDto = new InterMessageDto();
            interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
            interMessageDto.setSubject(messageSubject);
            interMessageDto.setMessageType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            String mesNO = masterCodeClient.messageID().getEntity();
            interMessageDto.setRefNo(mesNO);
            if(IaisCommonUtils.isEmpty(svcCodeList)){
                interMessageDto.setService_id("");
            } else {
                StringBuilder svcCodeShow = new StringBuilder();
                for(String svcCode : svcCodeList){
                    svcCodeShow.append(svcCode);
                    svcCodeShow.append('@');
                }
                interMessageDto.setService_id(svcCodeShow.toString());
            }
            interMessageDto.setUserId(licenseeDtos.get(0).getId());
            interMessageDto.setStatus(MessageConstants.MESSAGE_STATUS_UNREAD);
            interMessageDto.setMsgContent(emailContent);
            HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
            interMessageDto.setMaskParams(maskParams);
            notificationHelper.callEicInterMsg(interMessageDto);
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
                    }
                    //replace num
                    mesContext = MessageTemplateUtil.replaceNum(mesContext);
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }
        return mesContext;
    }

}
