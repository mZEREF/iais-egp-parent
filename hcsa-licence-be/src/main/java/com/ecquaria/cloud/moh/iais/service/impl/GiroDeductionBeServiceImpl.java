package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.GiroDeductionBeService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2020/10/19 15:52
 **/
@Service
@Slf4j
public class GiroDeductionBeServiceImpl implements GiroDeductionBeService {

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private NotificationHelper notificationHelper;

    @Override
    public  List<ApplicationGroupDto> sendMessageEmail(List<String> appGroupList) {
        List<ApplicationGroupDto> applicationGroupDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appGroupList)){
            for(String appGroupNo : appGroupList){
                ApplicationGroupDto applicationGroupDto = applicationClient.getAppGrpByNo(appGroupNo).getEntity();
                //todo status before insp, after insp
                applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_GIRO_RETRIGGER);
                applicationGroupDtos.add(applicationGroupDto);
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                String applicantId = applicationGroupDto.getSubmitBy();
                OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicantId).getEntity();
                String applicantName = orgUserDto.getDisplayName();
                map.put("applicant", applicantName);
                String appType = applicationGroupDto.getAppType();
                String appTypeShow = MasterCodeUtil.getCodeDesc(appType);
                map.put("applicationType", appTypeShow);
                map.put("applicationNo", appGroupNo);
                String strSubmitDt = Formatter.formatDateTime(applicationGroupDto.getSubmitDt(), "dd/MM/yyyy");
                map.put("applicationDate", strSubmitDt);
                map.put("paymentAmount", applicationGroupDto.getAmount());
                String address1 = systemParamConfig.getSystemAddressOne();
                map.put("email_address", address1);
                List<ApplicationDto> applicationDtos = applicationClient.getGroupAppsByNo(applicationGroupDto.getId()).getEntity();
                sendEmailByAppGroup(map, appTypeShow, appGroupNo, applicationGroupDto);
                sendMessageByAppGroup(map, appTypeShow, applicationDtos, appGroupNo);
            }
            //todo eic update appGroup

        } else {
            log.info("Giro Deduction appGroupList is null");
        }
        applicationClient.updateApplications(applicationGroupDtos);
        return applicationGroupDtos;
    }

    private void sendMessageByAppGroup(Map<String, Object> map, String appTypeShow, List<ApplicationDto> applicationDtos, String appGroupNo) {
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_GIRO_RETRIGGERS).getEntity();
        String subject = "";
        if(msgTemplateDto != null){
            String templateName = msgTemplateDto.getTemplateName();
            if(!StringUtil.isEmpty(templateName)){
                Map<String, Object> mapSubject = IaisCommonUtils.genNewHashMap();
                mapSubject.put("appTypeShow", appTypeShow);
                mapSubject.put("appGroupNo", appGroupNo);
                try {
                    subject = MsgUtil.getTemplateMessageByContent(templateName, mapSubject);
                } catch (IOException | TemplateException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        StringBuilder url = new StringBuilder();
        url.append(HmacConstants.HTTPS + "://").append(systemParamConfig.getInterServerName())
                .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohRetriggerGiroPayment")
                .append("?appGrpNo=")
                .append(appGroupNo);
        HashMap<String,String> mapPrem=IaisCommonUtils.genNewHashMap();
        mapPrem.put("appGrpNo",appGroupNo);
        map.put("systemLink", url);
        ApplicationDto appDto = applicationDtos.get(0);
        String appNo = appDto.getApplicationNo();
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_GIRO_RETRIGGERS);
        emailParam.setTemplateContent(map);
        emailParam.setQueryCode(appNo);
        emailParam.setReqRefNum(appNo);
        emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        emailParam.setRefId(appNo);
        //todo
        List<String> serviceCodes = IaisCommonUtils.genNewArrayList();
        for(ApplicationDto applicationDto : applicationDtos){
            String serviceId = applicationDto.getServiceId();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
            String serviceCode = hcsaServiceDto.getSvcCode();
            if(!StringUtil.isEmpty(serviceCode)){
                serviceCodes.add(serviceCode);
            }
        }
        emailParam.setMaskParams(mapPrem);
        emailParam.setSvcCodeList(serviceCodes);
        emailParam.setSubject(subject);
        notificationHelper.sendNotification(emailParam);
    }

    private void sendEmailByAppGroup(Map<String, Object> map, String appTypeShow, String appGroupNo, ApplicationGroupDto applicationGroupDto) {
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_GIRO_RETRIGGERS_EMAIL).getEntity();
        String subject = "";
        if(msgTemplateDto != null){
            String templateName = msgTemplateDto.getTemplateName();
            if(!StringUtil.isEmpty(templateName)){
                Map<String, Object> mapSubject = IaisCommonUtils.genNewHashMap();
                mapSubject.put("appTypeShow", appTypeShow);
                mapSubject.put("appGroupNo", appGroupNo);
                try {
                    subject = MsgUtil.getTemplateMessageByContent(templateName, mapSubject);
                } catch (IOException | TemplateException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        map.put("systemLink", loginUrl);
        String appGrpId = applicationGroupDto.getId();
        try{
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_GIRO_RETRIGGERS_EMAIL);
            emailParam.setTemplateContent(map);
            emailParam.setQueryCode(appGrpId);
            emailParam.setReqRefNum(appGrpId);
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP_GRP);
            emailParam.setRefId(appGrpId);
            emailParam.setSubject(subject);
            notificationHelper.sendNotification(emailParam);
            EmailParam smsParam = new EmailParam();
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_GIRO_RETRIGGERS_SMS);
            smsParam.setQueryCode(appGrpId);
            smsParam.setReqRefNum(appGrpId);
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP_GRP);
            smsParam.setRefId(appGrpId);
            emailParam.setSubject(subject);
            notificationHelper.sendNotification(smsParam);
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }
}
