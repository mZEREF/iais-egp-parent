package sg.gov.moh.iais.egp.bsb.helper;

import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MessageTemplateUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.client.EmailSmsClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private NotificationHelper notificationHelper;
    @Autowired
    private EmailSmsClient emailSmsClient;

    @Value("${iais.email.sender}")
    private String mailSender;

    private static final String MSG_TEMPLATE_NEW_APP_REJECT = "0E4B4155-942F-434B-A111-FC6C8843A36A";
    private static final String MSG_TEMPLATE_NEW_APP_APPROVAL = "85665530-65AB-4610-AB97-C6F15F841685";


    public void rejectSendNotification(String applicationNo,String applicationType,String applicationName,String status) throws IOException {
       Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put("Applicant",applicationName);
        map.put("applicationNo",applicationNo);
        map.put("applicationType",applicationType);
       Map<String,Object> subMap = IaisCommonUtils.genNewHashMap();
       subMap.put("applicationNo",applicationNo);
        sendEmail(MSG_TEMPLATE_NEW_APP_REJECT,map,subMap,applicationNo);
    }

   public void sendNotification(String applicationNo,String applicationType,String applicationName) throws IOException {
       //send email
       Map<String, Object> map = IaisCommonUtils.genNewHashMap();
       map.put("Applicant",applicationName);
       map.put("applicationNo",applicationNo);
       map.put("applicationType",applicationType);
       Map<String,Object> subMap = IaisCommonUtils.genNewHashMap();
       subMap.put("applicationNo",applicationNo);
       sendEmail(MSG_TEMPLATE_NEW_APP_APPROVAL,map,subMap,applicationNo);
   }


   private void sendEmail(String templateId,Map<String,Object> map,Map<String,Object>subMap,String applicationNo) throws IOException {
       EmailDto emailDto = new EmailDto();
       List<String> receiptEmail=IaisCommonUtils.genNewArrayList();
       receiptEmail.add("tangtang@toppanecquaria.com");
       receiptEmail.add("yiming@ecquaria.com");
       Set<String> set = IaisCommonUtils.genNewHashSet();
       set.addAll(receiptEmail);
       receiptEmail.clear();
       receiptEmail.addAll(set);
       emailDto.setReceipts(receiptEmail);
       String emailContent = getEmailContent(templateId,map);
       String emailSubject = getEmailSubject(templateId,subMap);
       emailDto.setContent(emailContent);
       emailDto.setSubject(emailSubject);
       emailDto.setSender(this.mailSender);
       emailDto.setClientQueryCode(applicationNo);
       emailDto.setReqRefNum(applicationNo);
       emailSmsClient.sendEmail(emailDto, null);
   }

    /**
     *
     * @param templateId
     * @param subMap
     * Query email subject based on templateId
     * @return
     */
    private String getEmailSubject(String templateId, Map<String, Object> subMap) {
        String subject = "-";
        if (!StringUtil.isEmpty(templateId)) {
            MsgTemplateDto emailTemplateDto = msgTemplateClient.getMsgTemplate(templateId).getEntity();
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
