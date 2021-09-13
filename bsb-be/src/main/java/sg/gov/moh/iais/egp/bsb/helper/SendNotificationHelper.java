package sg.gov.moh.iais.egp.bsb.helper;

import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MessageTemplateUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.EmailSmsClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskOrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import sg.gov.moh.iais.egp.bsb.client.FacilityClient;
import sg.gov.moh.iais.egp.bsb.common.CustomerException;
import sg.gov.moh.iais.egp.bsb.dto.Letter;
import sg.gov.moh.iais.egp.bsb.dto.Notification;
import sg.gov.moh.iais.egp.bsb.entity.Facility;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.UserRoleConstants.*;

/**
 * AUTHOR: YiMing
 * DATE:2021/9/7 17:05
 * DESCRIPTION: TODO
 **/
@Slf4j
@Component
@EnableAsync
public class SendNotificationHelper {

    @Autowired
    private MsgTemplateClient msgTemplateClient;
    @Autowired
    private EmailSmsClient emailSmsClient;
    @Autowired
    private FacilityClient facilityClient;
    @Autowired
    private TaskOrganizationClient taskOrganizationClient;

    @Value("${iais.email.sender}")
    private String mailSender;

    private static final String MSG_ERROR_STATUS = "500";
    private static final String MSG_TEMPLATE_NEW_APP_REJECT = "0E4B4155-942F-434B-A111-FC6C8843A36A";
    private static final String MSG_TEMPLATE_NEW_APP_APPROVAL = "85665530-65AB-4610-AB97-C6F15F841685";
    private static final String MSG_TEMPLATE_APPROVAL_FOR_UNCERTIFIED_FACILITY = "71A3B01F-DE69-4A7B-99A0-B58213466515";
    private static final String MSG_TEMPLATE_APPROVAL_LETTER_FOR_CERTIFIED_FACILITY = "CECEDC58-6341-4B06-8348-5177B8E4EA23";
    private static final String MSG_TEMPLATE_APPROVAL_LETTER_FOR_LARGE_SCALE_PRODUCTION_FIRST_THIRD = "15806B26-F535-4289-A302-44048C137ED2";
    private static final String MSG_TEMPLATE_APPROVAL_LETTER_OF_POSSESSION_FOR_FIRST_TIME_USE_OF_AGENT = "AC706878-115E-4C9A-947A-4AA7499A841E";
    private static final String MSG_TEMPLATE_APPROVAL_LETTER_FOR_SPECIAL_APPROVAL = "C8A559A1-F4B0-4CE4-8C6C-F8389E01DDF3";
    private static final String MSG_TEMPLATE_APPROVAL_LETTER_FOR_MOH_APPROVED_FACILITY_CERTIFIER = "7E99B5E1-F09F-4580-861E-0B50EA141089";
    private static final String MSG_TEMPLATE_RENEWAL_OF_REGISTRATION_AS_A_CERTIFIED_FACILITY = "540AE4CF-9B0A-4370-AD42-EAB80F162C35";
    private static final String MSG_TEMPLATE_NOTICE_OF_REVOCATION_FOR_APPROVAL = "7965B021-E60E-4E1B-B478-9F28361CA958";
    private static final String MSG_TEMPLATE_NOTICE_OF_REVOCATION_FOR_REGISTERED_FACILITY = "9EA3812F-DB00-4D5D-8C77-17B0C6F1DA82";
    private static final String MSG_TEMPLATE_NOTICE_OF_REVOCATION_FOR_CERTIFIED_FACILITY = "CA386361-8A26-4850-B522-7967204D4FC7";
    private static final String MSG_LETTER_TYPE_UNCERTIFIED_FACILITY                            = "letter1";
    private static final String MSG_LETTER_TYPE_CERTIFIED_FACILITY                              = "letter2";
    private static final String MSG_LETTER_TYPE_LARGE_SCALE_PRODUCTION                          = "letter3";
    private static final String MSG_LETTER_TYPE_POSSESSION_FOR_FIRST_TIME_USE_OF_AGENT          = "letter4";
    private static final String MSG_LETTER_TYPE_SPECIAL_APPROVAL                                = "letter5";
    private static final String MSG_LETTER_TYPE_MOH_APPROVED_FACILITY_CERTIFIER                 = "letter6";
    private static final String MSG_LETTER_TYPE_RENEWAL_OF_REGISTRATION_AS_A_CERTIFIED_FACILITY = "letter7";
    private static final String MSG_LETTER_TYPE_NOTICE_OF_REVOCATION_FOR_APPROVAL               = "letter8";
    private static final String MSG_LETTER_TYPE_NOTICE_OF_REVOCATION_FOR_REGISTERED_FACILITY    = "letter9";
    private static final String MSG_LETTER_TYPE_NOTICE_OF_REVOCATION_FOR_CERTIFIED_FACILITY     = "letter10";
//    private static final String MSG_NOTIFICATION_APP_APPROVAL_ID                                = "BISNEW003";
//    private static final String MSG_NOTIFICATION_APP_REJECT_ID                                  = "BISNEW004";
//    private static final String MSG_NOTIFICATION_BIO_REV_APPROVAL_ID                            = "BISEmail001";
//    private static final String MSG_NOTIFICATION_BIO_REV_REJECT_ID                              = "BISEmail002";

    private static final String STATUS_NEW_APPLICATION_SUCCESSFUL   = "app001";
    private static final String STATUS_NEW_APPLICATION_UNSUCCESSFUL = "app002";
    private static final String STATUS_REVOCATION_APPROVAL_AO       = "rej001";
    private static final String STATUS_REVOCATION_APPROVAL_USER     = "rej002";
    private static final String RECEIPT_ROLE_DUTY_OFFICER           = "EM-DO";
    private static final String RECEIPT_ROLE_APPROVAL_OFFICER           = "EM-AO";
    private static final String RECEIPT_ROLE_APPLICANT              = "APPLICANT";

   public void sendLetter(Letter letter){
       if(letter != null && letter.getApplicationNo() != null && letter.getLetterType() != null){
           String letterType = letter.getLetterType();
           if(MSG_LETTER_TYPE_UNCERTIFIED_FACILITY.equals(letterType)){
               generateLetter(MSG_TEMPLATE_APPROVAL_FOR_UNCERTIFIED_FACILITY,letter);
           }else if(MSG_LETTER_TYPE_CERTIFIED_FACILITY.equals(letterType)){
               generateLetter(MSG_TEMPLATE_APPROVAL_LETTER_FOR_CERTIFIED_FACILITY,letter);
           }else if(MSG_LETTER_TYPE_LARGE_SCALE_PRODUCTION.equals(letterType)){
               generateLetter(MSG_TEMPLATE_APPROVAL_LETTER_FOR_LARGE_SCALE_PRODUCTION_FIRST_THIRD,letter);
           }else if(MSG_LETTER_TYPE_POSSESSION_FOR_FIRST_TIME_USE_OF_AGENT.equals(letterType)){
               generateLetter(MSG_TEMPLATE_APPROVAL_LETTER_OF_POSSESSION_FOR_FIRST_TIME_USE_OF_AGENT,letter);
           }else if(MSG_LETTER_TYPE_SPECIAL_APPROVAL.equals(letterType)){
               generateLetter(MSG_TEMPLATE_APPROVAL_LETTER_FOR_SPECIAL_APPROVAL,letter);
           }else if(MSG_LETTER_TYPE_MOH_APPROVED_FACILITY_CERTIFIER.equals(letterType)){
               generateLetter(MSG_TEMPLATE_APPROVAL_LETTER_FOR_MOH_APPROVED_FACILITY_CERTIFIER,letter);
           }else if(MSG_LETTER_TYPE_RENEWAL_OF_REGISTRATION_AS_A_CERTIFIED_FACILITY.equals(letterType)){
               generateLetter(MSG_TEMPLATE_RENEWAL_OF_REGISTRATION_AS_A_CERTIFIED_FACILITY,letter);
           }else if(MSG_LETTER_TYPE_NOTICE_OF_REVOCATION_FOR_APPROVAL.equals(letterType)){
               generateLetter(MSG_TEMPLATE_NOTICE_OF_REVOCATION_FOR_APPROVAL,letter);
           }else if(MSG_LETTER_TYPE_NOTICE_OF_REVOCATION_FOR_REGISTERED_FACILITY.equals(letterType)){
               generateLetter(MSG_TEMPLATE_NOTICE_OF_REVOCATION_FOR_REGISTERED_FACILITY,letter);
           }else if(MSG_LETTER_TYPE_NOTICE_OF_REVOCATION_FOR_CERTIFIED_FACILITY.equals(letterType)){
               generateLetter(MSG_TEMPLATE_NOTICE_OF_REVOCATION_FOR_CERTIFIED_FACILITY,letter);
           }
       }
   }

    public void sendNotification(Notification notification) {
        if(notification != null && StringUtil.isNotEmpty(notification.getApplicationNo()) && StringUtil.isNotEmpty(notification.getApplicationType()) && StringUtil.isNotEmpty(notification.getApplicationName())){
            getNotificationParams(notification);
            if(STATUS_NEW_APPLICATION_SUCCESSFUL.equals(notification.getStatus())){
                sendEmail(MSG_TEMPLATE_NEW_APP_APPROVAL,notification);
            }else if(STATUS_NEW_APPLICATION_UNSUCCESSFUL.equals(notification.getStatus())){
                sendEmail(MSG_TEMPLATE_NEW_APP_REJECT,notification);
            }
        }else{
            try {
                throw new CustomerException(MSG_ERROR_STATUS,"Missing object parameters");
            } catch (CustomerException e) {
                e.printStackTrace();
            }
        }

    }


   private void sendEmail(String templateId,Notification notification) {
       EmailDto emailDto = new EmailDto();
       MsgTemplateDto msgTemplateDto = getMsgTemplate(templateId);
       getReceipt(emailDto,msgTemplateDto,notification.getApplicationNo());
       String emailContent = getEmailContent(msgTemplateDto,notification.getContentParams());
       String emailSubject = getEmailSubject(msgTemplateDto,notification.getSubjectParams());
       emailDto.setContent(emailContent);
       emailDto.setSubject(emailSubject);
       emailDto.setSender(this.mailSender);
       emailDto.setClientQueryCode(notification.getApplicationNo());
       emailDto.setReqRefNum(notification.getApplicationNo());
       try {
           emailSmsClient.sendEmail(emailDto, null);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }


   public void generateLetter(String templateId, Letter letter){
       EmailDto emailDto = new EmailDto();
       MsgTemplateDto msgTemplateDto = getMsgTemplate(templateId);
       getLetterParams(letter);
       getReceipt(emailDto,msgTemplateDto,letter.getApplicationNo());
       String emailContent = getEmailContent(msgTemplateDto,letter.getContentParams());
       emailDto.setContent(emailContent);
       emailDto.setSender(this.mailSender);
       emailDto.setSubject("Sms");
       emailDto.setClientQueryCode(letter.getApplicationNo());
       emailDto.setReqRefNum(letter.getApplicationNo());
       try {
           emailSmsClient.sendEmail(emailDto,null);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

   public void getLetterParams(Letter letter){
       if(letter != null && StringUtil.isNotEmpty(letter.getLetterType())){
           Map<String, Object> map = IaisCommonUtils.genNewHashMap();
           map.put("Applicant",letter.getApplicant());
           map.put("EmailAddress",letter.getEmailAddress());
           map.put("SubmissionDate",letter.getSubmissionDate());
           map.put("Date",letter.getNowDate());
           map.put("applicationNo",letter.getApplicationNo());
           String letterType = letter.getLetterType();
           if(MSG_LETTER_TYPE_UNCERTIFIED_FACILITY.equals(letterType) || MSG_LETTER_TYPE_CERTIFIED_FACILITY.equals(letterType) || MSG_LETTER_TYPE_NOTICE_OF_REVOCATION_FOR_APPROVAL.equals(letterType)){
               map.put("Designation",letter.getDesignation());
           }
           if(MSG_LETTER_TYPE_UNCERTIFIED_FACILITY.equals(letterType)){
               map.put("CompanyName",letter.getCompanyName());
               map.put("LaboratoryName",letter.getLaboratoryName());
           }
           if(MSG_LETTER_TYPE_UNCERTIFIED_FACILITY.equals(letterType) || MSG_LETTER_TYPE_LARGE_SCALE_PRODUCTION.equals(letterType) || MSG_LETTER_TYPE_POSSESSION_FOR_FIRST_TIME_USE_OF_AGENT.equals(letterType) || MSG_LETTER_TYPE_SPECIAL_APPROVAL.equals(letterType)){
               map.put("Agent",letter.getAgent());
           }
           if(MSG_LETTER_TYPE_CERTIFIED_FACILITY.equals(letterType)  || MSG_LETTER_TYPE_RENEWAL_OF_REGISTRATION_AS_A_CERTIFIED_FACILITY.equals(letterType)){
               map.put("FacilityName",letter.getFacilityName());
           }
           if(!MSG_LETTER_TYPE_UNCERTIFIED_FACILITY.equals(letterType) && !MSG_LETTER_TYPE_NOTICE_OF_REVOCATION_FOR_APPROVAL.equals(letterType)){
               map.put("RegistrationNo",letter.getRegistrationNo());
           }
           if(!MSG_LETTER_TYPE_UNCERTIFIED_FACILITY.equals(letterType) && !MSG_LETTER_TYPE_CERTIFIED_FACILITY.equals(letterType)){
               map.put("Specialist",letter.getSpecialist());
           }
           if(MSG_LETTER_TYPE_MOH_APPROVED_FACILITY_CERTIFIER.equals(letterType) || MSG_LETTER_TYPE_NOTICE_OF_REVOCATION_FOR_CERTIFIED_FACILITY.equals(letterType)){
               map.put("ExpiryDate",letter.getExpiryDate());
           }
           letter.setContentParams(map);
       }
   }

   public void getNotificationParams(Notification notification){
       Map<String, Object> map = IaisCommonUtils.genNewHashMap();
       Map<String,Object> subMap = IaisCommonUtils.genNewHashMap();
       if(STATUS_NEW_APPLICATION_SUCCESSFUL.equals(notification.getStatus()) || STATUS_NEW_APPLICATION_UNSUCCESSFUL.equals(notification.getStatus())){
           map.put("Applicant",notification.getApplicationName());
           map.put("applicationNo",notification.getApplicationNo());
           map.put("applicationType",notification.getApplicationType());
           notification.setContentParams(map);
           subMap.put("applicationNo",notification.getApplicationNo());
           notification.setSubjectParams(subMap);
       }else if(STATUS_REVOCATION_APPROVAL_AO.equals(notification.getStatus())){
           map.put("applicationNo",notification.getApplicationNo());
           map.put("ApprovalNo",notification.getApprovalNo());
           map.put("FacilityType",notification.getFacilityType());
           map.put("ApprovalType",notification.getApprovalType());
           map.put("FacilityCertifier",notification.getFacilityCertifier());
           map.put("officer",notification.getOfficer());
           subMap.put("applicationNo",notification.getApplicationNo());
       }else if(STATUS_REVOCATION_APPROVAL_USER.equals(notification.getStatus())){
           map.put("Reason",notification.getReason());
           map.put("Date",notification.getDate());
           map.put("applicationNo",notification.getApplicationNo());
           map.put("FacilityName",notification.getFacilityName());
           map.put("FacilityAddress",notification.getFacilityAddress());
       }
   }


    private void getReceipt(EmailDto emailDto,MsgTemplateDto msgTemplateDto,String appNo){
       if(msgTemplateDto != null){
           if(msgTemplateDto.getRecipient() != null && !msgTemplateDto.getRecipient().isEmpty()){
               log.info("enter getReceipt TO"+appNo);
               List<String> emailAddress = getEmailAddressByRole(msgTemplateDto.getRecipient(),appNo);
               emailDto.setReceipts(emailAddress);
           }
           if(msgTemplateDto.getCcrecipient() != null&& !msgTemplateDto.getCcrecipient().isEmpty()){
               log.info("enter getReceipt CC"+appNo);
               emailDto.setCcList(getEmailAddressByRole(msgTemplateDto.getCcrecipient(),appNo));
           }
           if(msgTemplateDto.getBccrecipient() != null && !msgTemplateDto.getBccrecipient().isEmpty()) {
               log.info("enter getReceipt BC" + appNo);
               emailDto.setCcList(getEmailAddressByRole(msgTemplateDto.getBccrecipient(), appNo));
           }else{
               try {
                   throw new CustomerException(MSG_ERROR_STATUS,"Object properties is null");
               } catch (CustomerException e) {
                   e.printStackTrace();
               }
           }
       }
   }


    /**
     * get email by different role
     * @param recipients
     * @param appNo
     * @return
     */
   private List<String> getEmailAddressByRole(List<String> recipients,String appNo){
       List<String> receiptEmail = IaisCommonUtils.genNewArrayList();
       for (String recipient : recipients) {
           if(RECEIPT_ROLE_APPLICANT.equals(recipient)){
               if(StringUtil.isNotEmpty(appNo)){
                   Facility facility = facilityClient.queryEmailByAppNo(appNo).getEntity();
                   //get applicant's email
                   if(facility != null){
                       receiptEmail.add(facility.getEmailAddr());
                   }
               }
           }else if(RECEIPT_ROLE_DUTY_OFFICER.equals(recipient)){
               //role is duty officer
             List<OrgUserDto> userDtoList  =  taskOrganizationClient.retrieveOrgUserAccountByRoleId(USER_ROLE_DO).getEntity();
             log.info("31231231"+userDtoList.get(0).getEmail());
               for (OrgUserDto orgUserDto : userDtoList) {
                   receiptEmail.add(orgUserDto.getEmail());
               }
           }else if(RECEIPT_ROLE_APPROVAL_OFFICER.equals(recipient)){
               List<String> userRoles = IaisCommonUtils.genNewArrayList();
               userRoles.add(USER_ROLE_AO1);
               userRoles.add(USER_ROLE_AO2);
               userRoles.add(USER_ROLE_AO3);
               List<OrgUserDto>userDtoList = taskOrganizationClient.retrieveOrgUserByroleId(userRoles).getEntity();
               for (OrgUserDto orgUserDto : userDtoList) {
                   receiptEmail.add(orgUserDto.getEmail());
               }
           }
       }
       return receiptEmail;
   }

    private MsgTemplateDto getMsgTemplate(String templateId){
    return msgTemplateClient.getMsgTemplate(templateId).getEntity();
   }

    /**
     *
     * @param emailTemplateDto
     * @param subMap
     * Query email subject based on templateId
     * @return
     */
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

    private String getEmailContent(MsgTemplateDto emailTemplateDto, Map<String, Object> subMap){
            String mesContext = "-";
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
        return mesContext;
    }
}
