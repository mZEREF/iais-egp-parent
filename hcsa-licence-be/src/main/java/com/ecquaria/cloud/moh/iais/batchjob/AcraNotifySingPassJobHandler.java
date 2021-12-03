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
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.client.AcraUenBeClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.sz.commons.util.DateUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private MsgTemplateClient msgTemplateClient;

    @Override
    public ReturnT<String> execute(String s) throws IOException, TemplateException{
        log.info(StringUtil.changeForLog("AcraNotifySingPassJobHandler start..." ));
        //90days
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Date today = calendar.getTime();
        int firReminder = systemParamConfig.getSingpassCeasedReminderFir();
        calendar.add(Calendar.MONTH, - firReminder);
        Date firReminderDate = calendar.getTime();
        List<LicenseeDto> licenseeDtoList90 = organizationClient.getLicenseeDtoOvertime(String.valueOf(DateUtil.daysBetween(today, firReminderDate))).getEntity();
        for (LicenseeDto item: licenseeDtoList90) {
            sendEmail(item, MsgTemplateConstants.MSG_TEMPLATE_UEN_002_EMAIL,MsgTemplateConstants.MSG_TEMPLATE_UEN_002_SMS,MsgTemplateConstants.MSG_TEMPLATE_UEN_002_MSG);
        }
        //60days
        int secReminder = systemParamConfig.getSingpassCeasedReminderSec();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, - secReminder);
        Date secReminderDate = calendar.getTime();
        List<LicenseeDto> licenseeDtoList60 = organizationClient.getLicenseeDtoOvertime(String.valueOf(DateUtil.daysBetween(today, secReminderDate))).getEntity();
        for (LicenseeDto item: licenseeDtoList60) {
            sendEmail(item, MsgTemplateConstants.MSG_TEMPLATE_UEN_003_EMAIL,MsgTemplateConstants.MSG_TEMPLATE_UEN_003_SMS,MsgTemplateConstants.MSG_TEMPLATE_UEN_003_MSG);
        }
        //30days
        int thirdReminder = systemParamConfig.getSingpassCeasedReminderThird();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, - thirdReminder);
        Date thirdReminderDate = calendar.getTime();
        List<LicenseeDto> licenseeDtoList30 = organizationClient.getLicenseeDtoOvertime(String.valueOf(DateUtil.daysBetween(today, thirdReminderDate))).getEntity();
        for (LicenseeDto item: licenseeDtoList30) {
            sendEmail(item, MsgTemplateConstants.MSG_TEMPLATE_UEN_004_EMAIL,MsgTemplateConstants.MSG_TEMPLATE_UEN_004_SMS,MsgTemplateConstants.MSG_TEMPLATE_UEN_004_MSG);
        }
        log.info(StringUtil.changeForLog("AcraNotifySingPassJobHandler end..." ));
        return ReturnT.SUCCESS;
    }

    private void sendEmail(LicenseeDto licenseeDto,String emailId,String smsId,String msgId){
        OrganizationDto organization = organizationClient.getOrganizationById(licenseeDto.getOrganizationId()).getEntity();
        List<OrgUserDto> orgUserList = organizationClient.getOrgUserAccountSampleDtoByOrganizationId(licenseeDto.getOrganizationId()).getEntity();
        if (IaisCommonUtils.isNotEmpty(orgUserList)&& Optional.ofNullable(organization).isPresent()){
            for (OrgUserDto user : orgUserList){
                String applicantName = user.getDisplayName();
                List<ApplicationGroupDto> applicationGroup = applicationClient.getApplicationGroupByLicensee(licenseeDto.getId()).getEntity();
                if (IaisCommonUtils.isNotEmpty(applicationGroup)){
                    for (ApplicationGroupDto group : applicationGroup){
                        sendEachApplication(licenseeDto, group, organization.getUenNo(), applicantName, emailId, smsId, msgId);
                    }
                }
            }
        }
    }

    private void sendEachApplication(LicenseeDto licenseeDto, ApplicationGroupDto applicationGroup, String uen, String applicantName, String emailId,String smsId,String msgId){
        String emailSubject = getEmailSubject(emailId,null);
        String smsSubject = getEmailSubject(smsId ,null);
        String messageSubject = getEmailSubject(msgId,null);

        List<ApplicationDto> applicationList = applicationClient.getAppDtosByAppGrpId(applicationGroup.getId()).getEntity();
        if (IaisCommonUtils.isNotEmpty(applicationList)){
            for (ApplicationDto app : applicationList){
                AppGrpPremisesEntityDto appGrpPremisesEntity = applicationClient.getPremisesByAppNo(app.getApplicationNo()).getEntity();
                if (Optional.ofNullable(appGrpPremisesEntity).isPresent()){
                    Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
                    templateContent.put("HCI_Name", appGrpPremisesEntity.getHciName());
                    String address = MiscUtil.getAddressForApp(appGrpPremisesEntity.getBlkNo(),appGrpPremisesEntity.getStreetName(),
                            appGrpPremisesEntity.getBuildingName(),appGrpPremisesEntity.getFloorNo(),appGrpPremisesEntity.getUnitNo(),appGrpPremisesEntity.getPostalCode()
                            ,appGrpPremisesEntity.getAppPremisesOperationalUnitDtos());
                    templateContent.put("HCI_Address", address);
                    log.info(StringUtil.changeForLog("HCI_Address = " + address));
                    templateContent.put("UEN_No", uen);
                    templateContent.put("Applicant", applicantName);
                    HcsaServiceDto hcsaService = hcsaConfigClient.getHcsaServiceDtoByServiceId(app.getServiceId()).getEntity();

                    List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
                    if (Optional.ofNullable(hcsaService).isPresent()){
                        templateContent.put("ServiceName", hcsaService.getSvcName());
                        svcCodeList.add(hcsaService.getSvcCode());
                    }

                    LicAppCorrelationDto licAppCorrelation = hcsaLicenceClient.getOneLicAppCorrelationByApplicationId(app.getId()).getEntity();

                    EmailParam emailParam = new EmailParam();
                    EmailParam msgParam = new EmailParam();
                    EmailParam smsParam = new EmailParam();

                    if (Optional.ofNullable(licAppCorrelation).isPresent()){
                        LicenceDto licence = hcsaLicenceClient.getLicenceDtoById(licAppCorrelation.getLicenceId()).getEntity();
                        if (Optional.ofNullable(licence).isPresent()){
                            templateContent.put("LicenceNo", licence.getLicenceNo());
                            emailParam.setQueryCode(licence.getLicenceNo());
                            emailParam.setReqRefNum(licence.getLicenceNo());
                            emailParam.setRefId(licence.getId());
                            emailParam.setQueryCode(licence.getLicenceNo());
                            emailParam.setReqRefNum(licence.getLicenceNo());
                            emailParam.setRefId(licence.getId());

                            smsParam.setQueryCode(licence.getLicenceNo());
                            smsParam.setReqRefNum(licence.getLicenceNo());
                            smsParam.setRefId(licence.getId());

                            msgParam.setQueryCode(licence.getLicenceNo());
                            msgParam.setReqRefNum(licence.getLicenceNo());
                            msgParam.setRefId(licence.getId());
                        }
                    }

                    String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
                    StringBuilder hrefStr = new StringBuilder();
                    hrefStr.append("<a href=\"").append(loginUrl).append("\">HALP</a>");

                    templateContent.put("HALP", hrefStr.toString());
                    templateContent.put("emailAddress", systemParamConfig.getSystemAddressOne());
                    templateContent.put("telNo", systemParamConfig.getSystemPhoneNumber());

                    if (Optional.ofNullable(licenseeDto.getSingpassExpiredDate()).isPresent()){
                        templateContent.put("GraceDate", Formatter.formatDate(licenseeDto.getSingpassExpiredDate()));
                    }

                    emailParam.setTemplateId(emailId);
                    emailParam.setTemplateContent(templateContent);
                    emailParam.setSubject(emailSubject);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);

                    notificationHelper.sendNotification(emailParam);
                    log.info(StringUtil.changeForLog("send email end"));

                    smsParam.setTemplateId(smsId);
                    smsParam.setSubject(smsSubject);
                    smsParam.setTemplateContent(templateContent);
                    smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
                    notificationHelper.sendNotification(smsParam);
                    log.info(StringUtil.changeForLog("send sms end"));

                    msgParam.setTemplateId(msgId);
                    msgParam.setTemplateContent(templateContent);
                    msgParam.setSubject(messageSubject);

                    msgParam.setSvcCodeList(svcCodeList);
                    msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    notificationHelper.sendNotification(msgParam);
                    log.info(StringUtil.changeForLog("send msg end"));
                }
            }
        }

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
