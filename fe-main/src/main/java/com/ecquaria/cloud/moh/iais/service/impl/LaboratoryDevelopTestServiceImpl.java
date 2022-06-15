package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LaboratoryDevelopTestDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.LaboratoryDevelopTestService;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import com.ecquaria.cloud.moh.iais.service.client.FeMainMsgTemplateClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LaboratoryDevelopTestServiceImpl implements LaboratoryDevelopTestService {

    @Autowired
    private FeMainMsgTemplateClient feMainMsgTemplateClient;

    @Autowired
    private OrgUserManageService orgUserManageService;

    @Autowired
    private NotificationHelper notificationHelper;


    @Override
    public void sendLDTTestEmailAndSMS(LaboratoryDevelopTestDto laboratoryDevelopTestDto,String orgId,String licenceId) throws IOException, TemplateException {
        List<FeUserDto> feUserDtos = orgUserManageService.getAccountByOrgId(orgId);
        String applicantName = feUserDtos.get(0).getDisplayName();
        String LDTId = laboratoryDevelopTestDto.getLdtNo();
        sendNotification(applicantName,LDTId,licenceId);
        sendEmail(applicantName,LDTId,licenceId);
    }
  

    private void sendNotification(String applicantName,String LDTId,String licenceId) throws IOException, TemplateException {
        MsgTemplateDto msgTemplateDto = feMainMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_DS_SUBMITTED_ACK_MSG).getEntity();
        Map<String, Object> msgContentMap = IaisCommonUtils.genNewHashMap();
        msgContentMap.put("ApplicantName", applicantName);
        msgContentMap.put("LDTId", LDTId);
        msgContentMap.put("MOH_AGENCY_NAME",AppConsts.MOH_AGENCY_NAME);
        Map<String, Object> msgSubjectMap = IaisCommonUtils.genNewHashMap();
        msgSubjectMap.put("LDTId", LDTId);
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),msgSubjectMap);
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_DS_SUBMITTED_ACK_MSG);
        emailParam.setTemplateContent(msgContentMap);
        emailParam.setQueryCode(LDTId);
        emailParam.setReqRefNum(LDTId);
        emailParam.setRefId(licenceId);
        emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        emailParam.setSubject(subject);
        notificationHelper.sendNotification(emailParam);
        log.info(StringUtil.changeForLog("***************** send LDT Notification  end *****************"));
    }

    private void sendEmail(String applicantName,String LDTId,String licenceId) throws IOException, TemplateException {
        MsgTemplateDto msgTemplateDto = feMainMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_DS_SUBMITTED_ACK_EMAIL).getEntity();
        Map<String, Object> msgContentMap = IaisCommonUtils.genNewHashMap();
        msgContentMap.put("ApplicantName", applicantName);
        msgContentMap.put("LDTId", LDTId);
        msgContentMap.put("MOH_AGENCY_NAME",AppConsts.MOH_AGENCY_NAME);
        Map<String, Object> msgSubjectMap = IaisCommonUtils.genNewHashMap();
        msgSubjectMap.put("LDTId", LDTId);
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),msgSubjectMap);
        EmailParam emailParamSms = new EmailParam();
        emailParamSms.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_DS_SUBMITTED_ACK_EMAIL);
        emailParamSms.setTemplateContent(msgContentMap);
        emailParamSms.setQueryCode(LDTId);
        emailParamSms.setReqRefNum(LDTId);
        emailParamSms.setRefId(licenceId);
        emailParamSms.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
        emailParamSms.setSubject(subject);
        notificationHelper.sendNotification(emailParamSms);
    }

}
