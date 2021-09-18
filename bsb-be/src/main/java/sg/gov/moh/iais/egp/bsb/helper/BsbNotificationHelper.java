package sg.gov.moh.iais.egp.bsb.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MessageTemplateUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.client.EmailSmsClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import sg.gov.moh.iais.egp.bsb.client.BsbEmailClient;
import sg.gov.moh.iais.egp.bsb.client.MsgTemplateClient;
import sg.gov.moh.iais.egp.bsb.client.OrganizationClient;
import sg.gov.moh.iais.egp.bsb.common.CustomerException;
import sg.gov.moh.iais.egp.bsb.constant.EmailConstants;
import sg.gov.moh.iais.egp.bsb.constant.UserRoleConstants;
import sg.gov.moh.iais.egp.bsb.dto.BsbEmailParam;
import sg.gov.moh.iais.egp.bsb.dto.EmailTemplateDto;
import sg.gov.moh.iais.egp.bsb.entity.FacilityAdmin;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AUTHOR: YiMing
 * DATE:2021/9/16 17:10
 * DESCRIPTION: TODO
 **/

@Slf4j
@Component
@EnableAsync
public class BsbNotificationHelper {

    private static final String MSG_ERROR_STATUS = "500";
    private static final String REF_ID_TYPE_APP_NO = "appNo";
    private static final String REF_ID_TYPE_FAC_ID = "facId";
    private static final String RECEIPT_ROLE_DUTY_OFFICER = "EM-DO";
    private static final String RECEIPT_ROLE_APPROVAL_OFFICER = "EM-AO";
    private static final String RECEIPT_ROLE_APPLICANT = "APPLICANT";

    @Autowired
    private Environment env;

    @Autowired
    private MsgTemplateClient msgTemplateClient;
    @Autowired
    private EmailSmsClient emailSmsClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private BsbEmailClient bsbEmailClient;

    @Value("${iais.email.sender}")
    private String mailSender;

    @Value("${iais.current.domain}")
    private String currentDomain;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;


    public void sendNotification(BsbEmailParam bsbEmailParam) {
       String msgTemplateId = bsbEmailParam.getMsgTemplateId();
       String refId = bsbEmailParam.getRefId();
       String refIdType = bsbEmailParam.getRefIdType();
       String reqRefNum = bsbEmailParam.getReqRefNum();
       String queryCode = bsbEmailParam.getQueryCode();
       String recipientType = bsbEmailParam.getRecipientType();
       Map<String,Object> msgContent = bsbEmailParam.getMsgContent();
       Map<String,Object> msgSubject = bsbEmailParam.getMsgSubject();
       Map<String,byte[]> attachments = bsbEmailParam.getAttachments();

       log.info("send email msgTemplateId = "+msgTemplateId+";refId ="+refId+"; refIdType = "+refIdType);

       MsgTemplateDto msgTemplateDto;
       if(recipientType != null && !recipientType.isEmpty()){
          msgTemplateDto =  msgTemplateClient.getMsgTemplate(msgTemplateId,recipientType).getEntity();
       }else{
           msgTemplateDto = msgTemplateClient.getMsgTemplate(msgTemplateId).getEntity();
       }

        String deliveryMode = msgTemplateDto.getDeliveryMode();

       if(EmailConstants.TEMPLETE_DELIVERY_MODE_EMAIL.equals(deliveryMode)){
           EmailDto emailDto = new EmailDto();
           EmailTemplateDto emailTemplateDto = new EmailTemplateDto();
           List<String> receiptEmails;
           List<String> ccEmails;
           List<String> bccEmails;
           if (msgTemplateDto.getRecipient() != null && msgTemplateDto.getRecipient().size() > 0) {
               emailTemplateDto = getRecript(msgTemplateDto.getRecipient(), refIdType, refId, emailTemplateDto);
               receiptEmails = emailTemplateDto.getReceiptEmail();
               if (!IaisCommonUtils.isEmpty(receiptEmails)) {
                   emailDto.setReceipts(receiptEmails);
               }
           }
           if (msgTemplateDto.getCcrecipient() != null && msgTemplateDto.getCcrecipient().size() > 0) {
               emailTemplateDto = getRecript(msgTemplateDto.getCcrecipient(), refIdType, refId, emailTemplateDto);
               ccEmails = emailTemplateDto.getReceiptEmail();
               if (!IaisCommonUtils.isEmpty(ccEmails)) {
                   emailDto.setCcList(ccEmails);
               }
           }
           if (msgTemplateDto.getBccrecipient() != null && msgTemplateDto.getBccrecipient().size() > 0) {
               emailTemplateDto = getRecript(msgTemplateDto.getBccrecipient(), refIdType, refId, emailTemplateDto);
               bccEmails = emailTemplateDto.getReceiptEmail();
               if (!IaisCommonUtils.isEmpty(bccEmails)) {
                   emailDto.setBccList(bccEmails);
               }
           }

           String subject = "";
           if(StringUtil.isNotEmpty(msgTemplateDto.getTemplateName())){
               subject = getEmailSubject(msgTemplateDto,msgSubject);
           }
           String content = getEmailContent(msgTemplateDto,msgContent);

           if(StringUtils.isEmpty(content) && "-".equals(content)){
               log.info("content is missing");
               try {
                   throw new CustomerException(MSG_ERROR_STATUS,"subMap missing parameter or template error");
               } catch (CustomerException e) {
                   e.printStackTrace();
               }
           }
           emailDto.setSubject(subject);
           emailDto.setContent(content);
           emailDto.setSender(this.mailSender);
           if(reqRefNum != null){
               emailDto.setReqRefNum(reqRefNum);
           }else {
               emailDto.setReqRefNum("no reqRefNum");
           }
           if(queryCode != null){
               emailDto.setClientQueryCode(queryCode);
           }else{
               emailDto.setClientQueryCode("no clientQueryCode");
           }
           //send email
           if(attachments != null && !attachments.isEmpty()){
               try {
                   emailSmsClient.sendEmail(emailDto,attachments);
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }else {

               //send other address
               if (!IaisCommonUtils.isEmpty(emailDto.getReceipts()) ||
                       !IaisCommonUtils.isEmpty(emailDto.getCcList()) ||
                       !IaisCommonUtils.isEmpty(emailDto.getBccList())) {
                   if (AppConsts.DOMAIN_INTERNET.equalsIgnoreCase(currentDomain)) {
                       String gatewayUrl = env.getProperty("iais.inter.gateway.url");
                       HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                       HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
                       IaisEGPHelper.callEicGatewayWithBody(gatewayUrl + "/v1/no-attach-emails", HttpMethod.POST, emailDto,
                               MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                               signature2.date(), signature2.authorization(), String.class);
                   } else {
                       try {
                           emailSmsClient.sendEmail(emailDto, null);
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
               } else {
                   log.info("No receipts. Won't send email.");
               }
           }

           //send changeable information
           Map<String, String> adminTypes = emailTemplateDto.getAdminTypes();
           Map<String, String> emailAddressMap = emailTemplateDto.getEmailAddress();
           if (!IaisCommonUtils.isEmpty(adminTypes)  && !IaisCommonUtils.isEmpty(emailAddressMap)) {
               for (Map.Entry<String, String> adminEntry : adminTypes.entrySet()) {
                   String key = adminEntry.getKey();
                   String adminValue = adminEntry.getValue();
                   List<String> officerEmails = IaisCommonUtils.genNewArrayList();
                   String officerEmail = emailAddressMap.get(key);
                   officerEmails.add(officerEmail);
                   if (!IaisCommonUtils.isEmpty(msgContent) && StringUtils.isNotEmpty(adminValue)) {
                       String [] admin = adminValue.split(":");
                       msgContent.put("Applicant",admin[0]);
                       msgContent.put("Admin", admin[1]);
                       content = getEmailContent(msgTemplateDto,msgContent);
                       emailDto.setContent(content);
                   }
                   emailDto.setReceipts(officerEmails);
                   //send email
                   if(attachments != null && !attachments.isEmpty()){
                       try {
                           emailSmsClient.sendEmail(emailDto,attachments);
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }else {
                       if (AppConsts.DOMAIN_INTERNET.equalsIgnoreCase(currentDomain)) {
                           String gatewayUrl = env.getProperty("iais.inter.gateway.url");
                           HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                           HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
                           IaisEGPHelper.callEicGatewayWithBody(gatewayUrl + "/v1/no-attach-emails", HttpMethod.POST, emailDto,
                                   MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                                   signature2.date(), signature2.authorization(), String.class);
                       } else {
                           try {
                               emailSmsClient.sendEmail(emailDto, null);
                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                       }
                   }

               }
           }


       }else if(EmailConstants.TEMPLETE_DELIVERY_MODE_SMS.equals(deliveryMode)){
           log.info("start send SMS ......");
       }else if(EmailConstants.TEMPLETE_DELIVERY_MODE_MSG.equals(deliveryMode)){
           log.info("start send MSG ......");
       }
    }

    private EmailTemplateDto getRecript(List<String> roles, String refIdType, String refId, EmailTemplateDto emailTemplateDto) {
        if(REF_ID_TYPE_APP_NO.equals(refIdType)){
             getRecriptAppNo(roles,refId,emailTemplateDto);
        }else if(REF_ID_TYPE_FAC_ID.equals(refIdType)){
             getRecriptFacId(roles,refId,emailTemplateDto);
        }else{
            getOfficer(roles,emailTemplateDto);
        }
        return emailTemplateDto;
    }

    private void getRecriptFacId(List<String> roles, String facId, EmailTemplateDto emailTemplateDto) {
     getFacApplicant(roles,facId,emailTemplateDto);
     getAssignedOfficer(roles,facId,emailTemplateDto);
     getOfficer(roles,emailTemplateDto);
    }

    private void getOfficer(List<String> roles, EmailTemplateDto emailTemplateDto) {
        List<String> adminRoles = IaisCommonUtils.genNewArrayList();
        List<String> passRoles = IaisCommonUtils.genNewArrayList();
        List<String> emails = IaisCommonUtils.genNewArrayList();
        adminRoles.add(UserRoleConstants.USER_ROLE_ASO);
        adminRoles.add(UserRoleConstants.USER_ROLE_PSO);
        adminRoles.add(UserRoleConstants.USER_ROLE_AO1);
        adminRoles.add(UserRoleConstants.USER_ROLE_AO2);
        adminRoles.add(UserRoleConstants.USER_ROLE_AO3);
        adminRoles.add(UserRoleConstants.USER_ROLE_INSPECTIOR);
        adminRoles.add(UserRoleConstants.USER_ROLE_INSPECTION_LEAD);
        adminRoles.add(UserRoleConstants.USER_ROLE_AUDIT_PLAN);
        if (roles.contains(UserRoleConstants.RECEIPT_ROLE_MOH_OFFICER)) {
            passRoles.addAll(adminRoles);
        } else {
            roles.forEach(r -> {
                String role = r.substring(3, r.length());
                if (adminRoles.contains(role)) {
                    passRoles.add(role);
                }
            });
        }
        if(!passRoles.isEmpty()){
            List<OrgUserDto> orgUserDtoList =  organizationClient.retrieveOrgUserByroleId(passRoles).getEntity();
            for (OrgUserDto orgUserDto : orgUserDtoList) {
                emails.add(orgUserDto.getEmail());
            }
            listDeduplicate(emails);
            emailTemplateDto.setReceiptEmail(emails);
        }
    }

    private void getAssignedOfficer(List<String> roles, String facId, EmailTemplateDto emailTemplateDto) {
        log.info("get assign officer...");
    }

    private void getFacApplicant(List<String> roles, String facId, EmailTemplateDto emailTemplateDto) {
        for (String role : roles) {
            if(RECEIPT_ROLE_APPLICANT.equals(role)){
              List<FacilityAdmin> admins = bsbEmailClient.queryEmailByFacId(facId).getEntity();
                Map<String, String> adminTypesMap = emailTemplateDto.getAdminTypes();
                Map<String, String> emailAddressMap = emailTemplateDto.getEmailAddress();

                if (adminTypesMap == null) {
                    adminTypesMap = IaisCommonUtils.genNewHashMap();
                }

                if (emailAddressMap == null) {
                    emailAddressMap = IaisCommonUtils.genNewHashMap();
                }

                int index = admins.size();
                for (FacilityAdmin u : admins) {
                    if (!StringUtils.isEmpty(u.getEmail())) {
                        adminTypesMap.put(String.valueOf(index), u.getType());
                        emailAddressMap.put(String.valueOf(index), u.getEmail());
                        index++;
                    }
                }
                emailTemplateDto.setAdminTypes(adminTypesMap);
                emailTemplateDto.setEmailAddress(emailAddressMap);
            }
        }
    }

    private void getApplicant(List<String> roles, String appNo, EmailTemplateDto emailTemplateDto) {
        for (String role : roles) {
            if(RECEIPT_ROLE_APPLICANT.equals(role)){
                List<FacilityAdmin> admins = bsbEmailClient.queryFacilityAdminByAppNo(appNo).getEntity();
                Map<String, String> adminTypesMap = emailTemplateDto.getAdminTypes();
                Map<String, String> emailAddressMap = emailTemplateDto.getEmailAddress();

                if (adminTypesMap == null) {
                    adminTypesMap = IaisCommonUtils.genNewHashMap();
                }

                if (emailAddressMap == null) {
                    emailAddressMap = IaisCommonUtils.genNewHashMap();
                }

                int index = admins.size();
                for (FacilityAdmin u : admins) {
                    if (!StringUtils.isEmpty(u.getEmail())) {
                        adminTypesMap.put(String.valueOf(index), u.getName()+":"+u.getType());
                        emailAddressMap.put(String.valueOf(index), u.getEmail());
                        index++;
                    }
                }
                emailTemplateDto.setAdminTypes(adminTypesMap);
                emailTemplateDto.setEmailAddress(emailAddressMap);
            }
        }
    }

    private void getRecriptAppNo(List<String> roles, String appNo, EmailTemplateDto emailTemplateDto) {
        getApplicant(roles,appNo,emailTemplateDto);
        getAssignedOfficer(roles,appNo,emailTemplateDto);
        getOfficer(roles,emailTemplateDto);
    }

    private void listDeduplicate(List<String> list) {
        Set<String> set = IaisCommonUtils.genNewHashSet();
        set.addAll(list);
        list.clear();
        list.addAll(set);
    }

    private String getEmailSubject(MsgTemplateDto emailTemplateDto, Map<String, Object> subMap) {
        String subject = "-";
        if (emailTemplateDto != null) {
            try {
                if (!IaisCommonUtils.isEmpty(subMap)) {
                    subject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(), subMap);
                } else {
                    subject = emailTemplateDto.getTemplateName();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return subject;
    }

    private String getEmailContent(MsgTemplateDto emailTemplateDto, Map<String, Object> subMap) {
        String mesContext = "-";
        if (emailTemplateDto != null) {
            try {
                if (!IaisCommonUtils.isEmpty(subMap)) {
                    mesContext = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getMessageContent(), subMap);
                }
                //replace num
                mesContext = MessageTemplateUtil.replaceNum(mesContext);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return mesContext;
    }
}
