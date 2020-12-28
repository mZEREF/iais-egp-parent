package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.client.AcraUenBeClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * AcraNotifySingPassJobHandler
 *
 * @author Guyin
 * @date 12/04/2020
 */
@JobHandler(value="AcraNotifySingPassJobHandler")
@Component
@Slf4j
public class AcraNotifySingPassJobHandler extends IJobHandler {

    @Autowired
    AcraUenBeClient acraUenBeClient;

    @Autowired
    LicenceService licenceService;

    @Autowired
    OrganizationClient organizationClient;

    @Autowired
    ApplicationClient applicationClient;

    @Autowired
    HcsaConfigClient hcsaConfigClient;

    @Autowired
    HcsaLicenceClient hcsaLicenceClient;

    @Autowired
    NotificationHelper notificationHelper;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Autowired
    private MsgTemplateClient msgTemplateClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    final static int OUTDATEMONTH = 6;
    @Override
    public ReturnT<String> execute(String s) throws IOException, TemplateException{
        log.info(StringUtil.changeForLog("AcraNotifySingPassJobHandler start..." ));
        OrganizationDto organizationDto = new OrganizationDto();
        //90days
        List<LicenseeDto> licenseeDtoList90 = IaisCommonUtils.genNewArrayList();
        licenseeDtoList90 = organizationClient.getLicenseeDtoOvertime("90").getEntity();
        for (LicenseeDto item: licenseeDtoList90
             ) {
            sendEmail(item, MsgTemplateConstants.MSG_TEMPLATE_UEN_002_EMAIL,MsgTemplateConstants.MSG_TEMPLATE_UEN_002_SMS,MsgTemplateConstants.MSG_TEMPLATE_UEN_002_MSG);
        }
        //60days
        List<LicenseeDto> licenseeDtoList60 = IaisCommonUtils.genNewArrayList();
        licenseeDtoList60 = organizationClient.getLicenseeDtoOvertime("60").getEntity();
        for (LicenseeDto item: licenseeDtoList60
        ) {
            sendEmail(item, MsgTemplateConstants.MSG_TEMPLATE_UEN_003_EMAIL,MsgTemplateConstants.MSG_TEMPLATE_UEN_003_SMS,MsgTemplateConstants.MSG_TEMPLATE_UEN_003_MSG);
        }
        //30days
        List<LicenseeDto> licenseeDtoList30 = IaisCommonUtils.genNewArrayList();
        licenseeDtoList30 = organizationClient.getLicenseeDtoOvertime("30").getEntity();
        for (LicenseeDto item: licenseeDtoList30
        ) {
            sendEmail(item, MsgTemplateConstants.MSG_TEMPLATE_UEN_004_EMAIL,MsgTemplateConstants.MSG_TEMPLATE_UEN_004_SMS,MsgTemplateConstants.MSG_TEMPLATE_UEN_004_MSG);
        }
        log.info(StringUtil.changeForLog("AcraNotifySingPassJobHandler end..." ));
        return ReturnT.SUCCESS;

    }

    private void sendEmail(LicenseeDto licenseeDto,String emailId,String smsId,String msgId){

        String emailSubject = getEmailSubject(emailId,null);
        String smsSubject = getEmailSubject(smsId ,null);
        String messageSubject = getEmailSubject(msgId,null);


        List<OrgUserDto> orgUserDtoList = organizationClient.getOrgUserAccountSampleDtoByOrganizationId(licenseeDto.getOrganizationId()).getEntity();
        OrganizationDto organizationDto = organizationClient.getOrganizationById(licenseeDto.getOrganizationId()).getEntity();
        String applicantName = orgUserDtoList.get(0).getDisplayName();
        List<ApplicationGroupDto> applicationGroupDtos = applicationClient.getApplicationGroupByLicensee(licenseeDto.getId()).getEntity();
        List<ApplicationDto> applicationDtoList = applicationClient.getAppDtosByAppGrpId(applicationGroupDtos.get(0).getId()).getEntity();
        AppGrpPremisesEntityDto appGrpPremisesEntityDto = applicationClient.getPremisesByAppNo(applicationDtoList.get(0).getApplicationNo()).getEntity();

        Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
        templateContent.put("HCI_Name", appGrpPremisesEntityDto.getHciName());
        String address = MiscUtil.getAddress(appGrpPremisesEntityDto.getBlkNo(),appGrpPremisesEntityDto.getStreetName(),appGrpPremisesEntityDto.getBuildingName(),appGrpPremisesEntityDto.getFloorNo(),appGrpPremisesEntityDto.getUnitNo(),appGrpPremisesEntityDto.getPostalCode());
        templateContent.put("HCI_Address", address);
        log.info(StringUtil.changeForLog("HCI_Address = " + address));
        templateContent.put("UEN_No", organizationDto.getUenNo());
        templateContent.put("Applicant", applicantName);
        HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(applicationDtoList.get(0).getServiceId()).getEntity();
        templateContent.put("ServiceName", hcsaServiceDto.getSvcName());
        LicAppCorrelationDto licAppCorrelationDto = hcsaLicenceClient.getOneLicAppCorrelationByApplicationId(applicationDtoList.get(0).getId()).getEntity();
        LicenceDto licenceDto = hcsaLicenceClient.getLicdtoByOrgId(licAppCorrelationDto.getLicenceId()).getEntity();
        templateContent.put("LicenceNo", licenceDto.getLicenceNo());

        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        templateContent.put("HALP", loginUrl);
        templateContent.put("emailAddress", systemParamConfig.getSystemAddressOne());
        templateContent.put("telNo", systemParamConfig.getSystemAddressOne());

        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(emailId);
        emailParam.setTemplateContent(templateContent);
        emailParam.setSubject(emailSubject);
        emailParam.setQueryCode(licenceDto.getLicenceNo());
        emailParam.setReqRefNum(licenceDto.getLicenceNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
        emailParam.setRefId(licenceDto.getId());
        notificationHelper.sendNotification(emailParam);
        log.info(StringUtil.changeForLog("send email end"));

        EmailParam smsParam = new EmailParam();
        smsParam.setTemplateId(smsId);
        smsParam.setSubject(smsSubject);
        smsParam.setTemplateContent(templateContent);
        smsParam.setQueryCode(licenceDto.getLicenceNo());
        smsParam.setReqRefNum(licenceDto.getLicenceNo());
        smsParam.setRefId(licenceDto.getId());
        smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
        notificationHelper.sendNotification(smsParam);
        log.info(StringUtil.changeForLog("send sms end"));

        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(msgId);
        msgParam.setTemplateContent(templateContent);
        msgParam.setSubject(messageSubject);
        msgParam.setQueryCode(licenceDto.getLicenceNo());
        msgParam.setReqRefNum(licenceDto.getLicenceNo());
        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        svcCodeList.add(hcsaServiceDto.getSvcCode());
        msgParam.setSvcCodeList(svcCodeList);
        msgParam.setRefId(licenceDto.getId());
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        notificationHelper.sendNotification(msgParam);
        log.info(StringUtil.changeForLog("send msg end"));

    }

    private String getEmailSubject(String templateId,Map<String, Object> subMap){
        String subject = "-";
        if(!StringUtil.isEmpty(templateId)){
            MsgTemplateDto emailTemplateDto = msgTemplateClient.getMsgTemplate(templateId).getEntity();
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
}
