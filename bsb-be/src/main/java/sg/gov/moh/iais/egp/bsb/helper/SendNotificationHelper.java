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
import java.util.List;
import java.util.Map;
import java.util.Set;

import static sg.gov.moh.iais.egp.bsb.constant.EmailConstants.*;
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
    private static final String RECEIPT_ROLE_DUTY_OFFICER           = "EM-DO";
    private static final String RECEIPT_ROLE_APPROVAL_OFFICER       = "EM-AO";
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
        if(notification != null && StringUtil.isNotEmpty(notification.getApplicationNo()) && StringUtil.isNotEmpty(notification.getStatus())){
            if(IaisCommonUtils.isEmpty(notification.getContentParams())){
                getNotificationParams(notification);
            }
            String status = notification.getStatus();
            switch (status){
                case STATUS_NEW_APP_SUBMITTED:
                    sendEmail(MSG_TEMPLATE_NEW_APP_APPLICANT_SUBMIT,notification);
                    break;
                case STATUS_NEW_APP_NON_SELF_ASSESSMENT:
                    sendEmail(MSG_TEMPLATE_NEW_APP_NO_SELF_ASSESSMENT,notification);
                    break;
                case STATUS_NEW_APP_REJECT:
                    sendEmail(MSG_TEMPLATE_NEW_APP_REJECT,notification);
                    break;
                case STATUS_NEW_APP_APPROVED:
                    sendEmail(MSG_TEMPLATE_NEW_APP_APPROVAL,notification);
                    break;
                case STATUS_NEW_APP_REQUEST_FOR_INFO:
                    sendEmail(MSG_TEMPLATE_NEW_APP_REQUEST_FOR_INFO,notification);
                    break;
                case STATUS_NEW_APP_REMIND_PNEF_INVENTORY:
                    sendEmail(MSG_TEMPLATE_NEW_APP_REMIND_PNEF_INVENTORY,notification);
                    break;
                case STATUS_NEW_APP_REGISTERED_FACILITY_SUCCESSFUL:
                    sendEmail(MSG_TEMPLATE_NEW_APP_REGISTER_FACILITY_SUCCESSFUL,notification);
                    break;
                case STATUS_REVOCATION_APPROVAL_AO:
                    sendEmail(MSG_TEMPLATE_REVOCATION_AO_APPROVED,notification);
                    break;
                case STATUS_REVOCATION_APPROVAL_USER:
                    sendEmail(MSG_TEMPLATE_REVOCATION_USER_APPROVED,notification);
                    break;
                default:
                    try {
                        throw new CustomerException(MSG_ERROR_STATUS,"Missing object parameters");
                    } catch (CustomerException e) {
                        e.printStackTrace();
                    }
                    break;
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
       if(IaisCommonUtils.isEmpty(letter.getContentParams())){
           getLetterParams(letter);
       }
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
       if(STATUS_NEW_APP_APPROVED.equals(notification.getStatus()) || STATUS_NEW_APP_REJECT.equals(notification.getStatus())){
           map.put("Applicant",notification.getApplicationName());
           map.put("applicationNo",notification.getApplicationNo());
           map.put("applicationType",notification.getApplicationType());
           subMap.put("applicationNo",notification.getApplicationNo());
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
       notification.setContentParams(map);

       //make sure subjectParams is null
       if(IaisCommonUtils.isEmpty(notification.getSubjectParams())){
           notification.setSubjectParams(subMap);
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
           }
       }else{
           try {
               throw new CustomerException(MSG_ERROR_STATUS,"Object properties is null");
           } catch (CustomerException e) {
               e.printStackTrace();
           }
       }
   }

    private void listDeduplicate(List<String> list){
        Set<String> set = IaisCommonUtils.genNewHashSet();
        set.addAll(list);
        list.clear();
        list.addAll(set);
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
       listDeduplicate(receiptEmail);
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
