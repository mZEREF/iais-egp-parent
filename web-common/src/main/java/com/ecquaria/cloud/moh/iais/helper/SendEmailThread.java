package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.client.IaisSystemClient;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.EmailSmsClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class SendEmailThread extends Thread {

    private String appLicationId ;
    private String templateId ;
    private String mailSender ;
    private String queryCode ;
    private String reqRefNum ;
    private Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
    private EmailHelper emailHelper;
    private IaisSystemClient iaisSystemClient;
    private EmailSmsClient emailSmsClient;
    public SendEmailThread(String templateId, String appLicationId, Map<String, Object> templateContent, String mailSender,String queryCode,String reqRefNum){
        this.appLicationId = appLicationId;
        this.templateId = templateId;
        this.mailSender = mailSender;
        this.templateContent = templateContent;
        this.queryCode = queryCode;
        this.reqRefNum = reqRefNum;
    }

    @Override
    public void run(){
        try
        {
            List<String> receiptemail = IaisCommonUtils.genNewArrayList();
            List<String> ccemail = IaisCommonUtils.genNewArrayList();
            List<String> bccemail = IaisCommonUtils.genNewArrayList();
            log.info(StringUtil.changeForLog("sendemail start... application is"+this.appLicationId + "templateId is "+ this.templateId+"thread name is " + Thread.currentThread().getName()));
            this.iaisSystemClient= BeanContext.getApplicationContext().getBean(IaisSystemClient.class);
            this.emailSmsClient= BeanContext.getApplicationContext().getBean(EmailSmsClient.class);
            this.emailHelper= BeanContext.getApplicationContext().getBean(EmailHelper.class);

            MsgTemplateDto msgTemplateDto = iaisSystemClient.getMsgTemplate(templateId).getEntity();
            EmailDto emailDto = new EmailDto();
            String mesContext = "";
            if (this.templateContent != null && !this.templateContent.isEmpty()){
                mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), this.templateContent);
            }else{
                mesContext = msgTemplateDto.getMessageContent();
            }

            emailDto.setContent(mesContext);
            emailDto.setSubject(msgTemplateDto.getTemplateName());
            emailDto.setSender(this.mailSender);
            if(msgTemplateDto.getRecipient()!= null && msgTemplateDto.getRecipient().size() > 0){
                receiptemail = emailHelper.getRecript(msgTemplateDto.getRecipient(),this.appLicationId);
               emailDto.setReceipts(receiptemail);
            }
            if(msgTemplateDto.getCcrecipient()!= null && msgTemplateDto.getCcrecipient().size() > 0){
                ccemail = emailHelper.getRecript(msgTemplateDto.getCcrecipient(),this.appLicationId);
                emailDto.setCcList(ccemail);
            }
            if(msgTemplateDto.getBccrecipient()!= null && msgTemplateDto.getBccrecipient().size() > 0){
                bccemail = emailHelper.getRecript(msgTemplateDto.getBccrecipient(),this.appLicationId);
                emailDto.setBccList(bccemail);
            }
            if(this.queryCode != null){
                emailDto.setClientQueryCode(this.queryCode);
            }else{
                emailDto.setClientQueryCode("no queryCode");
            }
            if(this.reqRefNum != null){
                emailDto.setReqRefNum(this.reqRefNum);
            }else{
                emailDto.setReqRefNum("no reqRefNum");
            }
            //send
            this.emailSmsClient.sendEmail(emailDto,null);
            log.info(StringUtil.changeForLog("sendemail end... queryCode is"+queryCode + "templateId is "+ this.templateId+"thread name is " + Thread.currentThread().getName()));

        }
        catch (Exception e)
        {
            log.info(e.getMessage());
        }
    }

}
