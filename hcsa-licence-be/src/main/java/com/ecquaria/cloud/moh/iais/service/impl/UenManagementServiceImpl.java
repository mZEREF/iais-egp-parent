package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.MohUenDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UenDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.EmailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.UenManagementService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.UenManagementBeClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * UenManagementServiceImpl
 *
 * @author junyu
 * @date 2020/1/22
 */
@Slf4j
@Service
public class UenManagementServiceImpl implements UenManagementService {
    @Autowired
    private UenManagementBeClient uenManagementClient;
    @Autowired
    private MsgTemplateClient systemAdminClient;
    @Autowired
    private EmailHelper emailHelper;
    @Autowired
    private InboxMsgService inboxMsgService;
    @Autowired
    ApplicationViewService applicationViewService;
    @Autowired
    ApplicationClient applicationClient;
    @Autowired
    EmailClient emailClient;

    @Value("${iais.email.sender}")
    private String mailSender;

    private UenDto getUenDetails(String uenNo) {
        //ACRA api
        return null;
    }

    @Override
    public boolean validityCheckforAcraissuedUen(MohUenDto mohUenDto) {
        MohUenDto mohUenDto1=uenManagementClient.getMohUenById(mohUenDto.getUenNo()).getEntity();
        //ACRA api
        return false;
    }

    @Override
    public boolean generatesMohIssuedUen(BaseProcessClass bpc, MohUenDto mohUenDto) throws IOException, TemplateException {
        MohUenDto mohUenDto1= uenManagementClient.generatesMohIssuedUen(mohUenDto).getEntity();
        //ACRA api
        sendUenEmail(bpc,"registrationUen");
        return false;
    }

    @Override
    public void sendUenEmail(BaseProcessClass bpc, String active) throws IOException, TemplateException {
        String appNo= (String) ParamUtil.getSessionAttr(bpc.request,"appNo");
        String uenNo=(String) ParamUtil.getSessionAttr(bpc.request,"uenNo");
        switch (active){
            case "registration":sendRegistrationEmail(appNo,uenNo);break;
            case "registrationUen":sendRegistrationUenEmail(appNo,uenNo);break;
            case "deRegistered":sendDeRegisteredEmail(appNo,uenNo);break;
            default:break;
        }

    }

    private void sendRegistrationEmail(String appNo,String uenNo) throws IOException, TemplateException {
        String templateId="D03B3C73-2893-EA11-BE7A-000C29D29DB0";
        sendUenEmails(appNo, uenNo, templateId);
    }
    private void sendRegistrationUenEmail(String appNo,String uenNo) throws IOException, TemplateException {
        String templateId="D03B3C73-2893-EA11-BE7A-000C29D29DB0";
        sendUenEmails(appNo, uenNo, templateId);
    }
    private void sendDeRegisteredEmail(String appNo,String uenNo) throws IOException, TemplateException {
        String templateId="7ED3CCAC-2893-EA11-BE7A-000C29D29DB0";
        sendUenEmails(appNo, uenNo, templateId);
    }

    private void sendUenEmails(String appNo, String uenNo, String templateId) throws IOException, TemplateException {
        MsgTemplateDto rfiEmailTemplateDto = systemAdminClient.getMsgTemplate(templateId).getEntity();
        Map<String,Object> map= IaisCommonUtils.genNewHashMap();
        map.put("UEN Number", StringUtil.viewHtml(uenNo));
        map.put("MOH_NAME", StringUtil.viewHtml(AppConsts.MOH_AGENCY_NAME));
        String mesContext= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getMessageContent(),map);
        ApplicationDto applicationDto=applicationViewService.getApplicaitonByAppNo(appNo);
        try{
            String licenseeId=applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity().getLicenseeId();
            InterMessageDto interMessageDto = new InterMessageDto();
            interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
            interMessageDto.setSubject(rfiEmailTemplateDto.getTemplateName());
            interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_NOTIFICATION);
            String mesNO = inboxMsgService.getMessageNo();
            interMessageDto.setRefNo(mesNO);
            interMessageDto.setService_id(applicationDto.getServiceId());
            interMessageDto.setMsgContent(rfiEmailTemplateDto.getMessageContent());
            interMessageDto.setStatus(MessageConstants.MESSAGE_STATUS_UNREAD);
            interMessageDto.setUserId(licenseeId);
            interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            inboxMsgService.saveInterMessage(interMessageDto);


            EmailDto emailDto=new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject(rfiEmailTemplateDto.getTemplateName());
            emailDto.setSender(mailSender);
            List<String> licenseeIds= IaisCommonUtils.genNewArrayList();
            licenseeIds.add(licenseeId);
            List<String> emailAddress = emailHelper.getEmailAddressListByLicenseeId(licenseeIds);
            emailDto.setReceipts(emailAddress);
            emailClient.sendNotification(emailDto);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

}
