package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.LicInspNcEmailService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * SendsReminderToReplyRfiDelegator
 *
 * @author junyu
 * @date 2020/1/8
 */
@Delegator("sendsReminderToReplyRfiDelegator")
@Slf4j
public class SendsReminderToReplyRfiBatchjob {
    @Autowired
    RequestForInformationService requestForInformationService;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    LicInspNcEmailService licInspNcEmailService;
    @Autowired
    HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    EmailClient emailClient;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    HcsaConfigClient hcsaConfigClient;
    @Autowired
    ApplicationClient applicationClient;
    @Autowired
    private SystemBeLicClient systemBeLicClient;
    @Value("${iais.system.rfc.sms.reminder.day}")
    String reminderMax1Day;
    @Value("${iais.system.rfc.sms.sec.reminder.day}")
    String reminderMax2Day;
    @Value("${iais.system.rfc.sms.third.reminder.day}")
    String reminderMax3Day;

    @Autowired
    OrganizationClient organizationClient;
    public void start(BaseProcessClass bpc){
        logAbout("start");
        AuditTrailHelper.setupBatchJobAuditTrail(this);
    }

    public void sendMsg(BaseProcessClass bpc)  {
        logAbout("sendMsg");
        List<LicPremisesReqForInfoDto> licPremisesReqForInfoDtos= requestForInformationService.getAllReqForInfo();

        for (LicPremisesReqForInfoDto rfi:licPremisesReqForInfoDtos
        ) {
            if(rfi.getReminder()<3){
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(rfi.getDueDateSubmission());
                String reminderMaxDay;
                switch (rfi.getReminder()){
                    case 0:reminderMaxDay=reminderMax1Day;break;
                    case 1:reminderMaxDay=reminderMax2Day;break;
                    case 2:reminderMaxDay=reminderMax3Day;break;
                    default:reminderMaxDay="0";
                }
                cal1.add(Calendar.DAY_OF_MONTH, Integer.parseInt(reminderMaxDay)-1);
                if(cal1.getTime().compareTo(new Date())<0&&(rfi.getStatus().equals(RequestForInformationConstants.RFI_NEW)||rfi.getStatus().equals(RequestForInformationConstants.RFI_RETRIGGER))){
                    try {
                        reminder(rfi);
                    }catch (Exception e){
                        log.info(e.getMessage(),e);
                    }
                }
            }
        }
        try {
            getInfo();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

    }

    private void reminder(LicPremisesReqForInfoDto licPremisesReqForInfoDto) throws IOException, TemplateException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, systemParamConfig.getRfiDueDate());
        String templateId=MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER;
        InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(templateId);
        String licenseeId=requestForInformationService.getLicPreReqForInfo(licPremisesReqForInfoDto.getId()).getLicenseeId();
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
        List<OrgUserDto> orgUserDtoList = organizationClient.getOrgUserAccountSampleDtoByOrganizationId(licenseeDto.getOrganizationId()).getEntity();
        String applicantName=licenseeDto.getName();
        if(orgUserDtoList!=null&&orgUserDtoList.get(0)!=null){
            applicantName=orgUserDtoList.get(0).getDisplayName();
        }
        Map<String,Object> map=IaisCommonUtils.genNewHashMap();
        StringBuilder stringBuilder=new StringBuilder();
        int i=0;
        if(!StringUtil.isEmpty(licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos())){
            for ( i=0;i<licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos().size();i++) {
                stringBuilder.append("<p>   ").append(' ').append("Information : ").append(licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos().get(i).getTitle()).append("</p>");
            }
        }
        if(licPremisesReqForInfoDto.isNeedDocument()){
            for (int j=0;j<licPremisesReqForInfoDto.getLicPremisesReqForInfoDocDto().size();j++) {
                stringBuilder.append("<p>   ").append(' ').append("Documentations : ").append(licPremisesReqForInfoDto.getLicPremisesReqForInfoDocDto().get(j).getTitle()).append("</p>");
            }
        }
        String url = "https://" + systemParamConfig.getInterServerName() +
                "/hcsa-licence-web/eservice/INTERNET/MohClientReqForInfo" +
                "?licenseeId=" + licPremisesReqForInfoDto.getLicenseeId();
        map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{RequestForInformationConstants.AD_HOC}).get(0).getText());
        map.put("ApplicationNumber", licPremisesReqForInfoDto.getLicenceNo());
        String subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
        HashMap<String,String> mapPrem=IaisCommonUtils.genNewHashMap();
        mapPrem.put("licenseeId",licPremisesReqForInfoDto.getLicenseeId());
        LicenceViewDto licenceViewDto= hcsaLicenceClient.getLicenceViewDtoByLicPremCorrId(licPremisesReqForInfoDto.getLicPremId()).getEntity();
        try{


            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
            Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
            emailMap.put("ApplicantName", applicantName);
            emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{RequestForInformationConstants.AD_HOC}).get(0).getText());
            emailMap.put("ApplicationNumber", licPremisesReqForInfoDto.getLicenceNo());
            emailMap.put("ApplicationDate", Formatter.formatDate(licPremisesReqForInfoDto.getRequestDate()));
            emailMap.put("email", systemParamConfig.getSystemAddressOne());
            emailMap.put("TATtime", Formatter.formatDate(cal.getTime()));
            emailMap.put("systemLink", loginUrl);
            emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
        emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");

            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER);
            emailParam.setTemplateContent(emailMap);
            emailParam.setQueryCode(licPremisesReqForInfoDto.getLicenceNo());
            emailParam.setReqRefNum(licPremisesReqForInfoDto.getLicenceNo());
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
            emailParam.setRefId(licenceViewDto.getLicenceDto().getId());
            emailParam.setSubject(subject);
            //email
            notificationHelper.sendNotification(emailParam);
            //sms
            rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER_SMS);
            subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
            emailParam.setSubject(subject);
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER_SMS);
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
            notificationHelper.sendNotification(emailParam);
            //msg
            rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER_MSG);
            subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
            emailParam.setSubject(subject);
            HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(licenceViewDto.getLicenceDto().getSvcName()).getEntity();
            List<String> svcCode=IaisCommonUtils.genNewArrayList();
            svcCode.add(svcDto.getSvcCode());
            emailMap.put("systemLink", url);
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER_MSG);
            emailParam.setTemplateContent(emailMap);
            emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            emailParam.setMaskParams(mapPrem);
            emailParam.setSvcCodeList(svcCode);
            emailParam.setRefId(licenceViewDto.getLicenceDto().getId());
            notificationHelper.sendNotification(emailParam);

        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

        licPremisesReqForInfoDto.setReminder(licPremisesReqForInfoDto.getReminder()+1);
        licPremisesReqForInfoDto.setStatus(RequestForInformationConstants.RFI_RETRIGGER);
        licPremisesReqForInfoDto.setDueDateSubmission(cal.getTime());
        LicPremisesReqForInfoDto licPremisesReqForInfoDto1 = requestForInformationService.updateLicPremisesReqForInfo(licPremisesReqForInfoDto);
        licPremisesReqForInfoDto1.setAction("update");

        EicRequestTrackingDto eicRequestTrackingDto=new EicRequestTrackingDto();
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        Date now = new Date();
        eicRequestTrackingDto.setActionClsName("com.ecquaria.cloud.moh.iais.service.RequestForInformationServiceImpl");
        eicRequestTrackingDto.setActionMethod("eicCallFeRfiLic");
        eicRequestTrackingDto.setModuleName("hcsa-licence-web-intranet");
        eicRequestTrackingDto.setDtoClsName(LicPremisesReqForInfoDto.class.getName());
        eicRequestTrackingDto.setDtoObject(JsonUtil.parseToJson(licPremisesReqForInfoDto1));
        eicRequestTrackingDto.setProcessNum(1);
        eicRequestTrackingDto.setFirstActionAt(now);
        eicRequestTrackingDto.setLastActionAt(now);
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        eicRequestTrackingDto.setRefNo(System.currentTimeMillis()+"");
        licPremisesReqForInfoDto1.setEventRefNo(eicRequestTrackingDto.getRefNo());
        requestForInformationService.updateLicEicRequestTrackingDto(eicRequestTrackingDto);
        requestForInformationService.createFeRfiLicDto(licPremisesReqForInfoDto1);

    }
    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }
    private void sendEmail(ApplicationDto applicationDto,String time) throws Exception{
        Map<String,Object> map=IaisCommonUtils.genNewHashMap();
        InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER);
        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, systemParamConfig.getRfiDueDate());
        String appGrpId = applicationDto.getAppGrpId();
        ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(appGrpId).getEntity();
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(applicationGroupDto.getLicenseeId());
        String applicantName=licenseeDto.getName();
        map.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        map.put("ApplicationNumber", applicationDto.getApplicationNo());
        String subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
        Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
        emailMap.put("ApplicantName", applicantName);
        emailMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        emailMap.put("ApplicationNumber", applicationDto.getApplicationNo());
        emailMap.put("systemLink", loginUrl);
        emailMap.put("email", systemParamConfig.getSystemAddressOne());
        emailMap.put("ApplicationDate", Formatter.formatDate(applicationDto.getModifiedAt()));
        emailMap.put("TATtime", Formatter.formatDate(cal.getTime()));
        emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
        emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER);
        emailParam.setTemplateContent(emailMap);
        emailParam.setQueryCode(applicationDto.getAppGrpId());
        emailParam.setReqRefNum(applicationDto.getAppGrpId());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setSubject(subject);
        //email
        notificationHelper.sendNotification(emailParam);
        saveMailJob(applicationDto.getId(),"sendRfi"+time);
        //sms
        rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER_SMS);
        subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
        emailParam.setSubject(subject);
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER_SMS);
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        notificationHelper.sendNotification(emailParam);
        rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER_MSG);
        subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
        emailParam.setSubject(subject);
        HcsaServiceDto svcDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(applicationDto.getServiceId()).getEntity();
        List<String> svcCode=IaisCommonUtils.genNewArrayList();
        svcCode.add(svcDto.getSvcCode());
        String url="";
        HashMap<String,String> mapPrem=IaisCommonUtils.genNewHashMap();
/*
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(applicationDto.getId()).getEntity();
*/
        /*if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationDto.getApplicationType())){
            List<AppPremiseMiscDto> entity = applicationClient.getAppPremiseMiscDtoRelateId(appPremisesCorrelationDto.getId()).getEntity();
            if(entity!=null){
                for(AppPremiseMiscDto appPremiseMiscDto : entity){
                    String relateRecId = appPremiseMiscDto.getRelateRecId();
                    if(ApplicationConsts.APPEAL_TYPE_APPLICAITON.equals(appPremiseMiscDto.getAppealType())){
                        url = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_CALL_BACK_URL_Appeal + relateRecId + "&type=application";
                    }else {
                        url = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_CALL_BACK_URL_Appeal + relateRecId + "&type=licence" ;
                    }
                    mapPrem.put("appealingFor",relateRecId);
                }
            }
        }else if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationDto.getApplicationType()) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationDto.getApplicationType())){
            url = HmacConstants.HTTPS +"://"+systemParamConfig.getInterServerName()+ MessageConstants.MESSAGE_CALL_BACK_URL_NEWAPPLICATION+applicationDto.getApplicationNo();
            mapPrem.put("appNo",applicationDto.getApplicationNo());
        }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType())){
            url = HmacConstants.HTTPS +"://"+systemParamConfig.getInterServerName()+ MessageConstants.MESSAGE_CALL_BACK_URL_NEWAPPLICATION+applicationDto.getApplicationNo();
            mapPrem.put("appNo",applicationDto.getApplicationNo());
        }else if(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationDto.getApplicationType())){
            mapPrem.put("rfiWithdrawAppNo", applicationDto.getApplicationNo());
            url = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + InboxConst.URL_LICENCE_WEB_MODULE + "MohWithdrawalApplication?rfiWithdrawAppNo=" + applicationDto.getApplicationNo();
        }else if(ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationDto.getApplicationType())){
            String appGrpPremId = "";
            if (appPremisesCorrelationDto != null) {
                appGrpPremId = appPremisesCorrelationDto.getAppGrpPremId();
                mapPrem.put("premiseId", appGrpPremId);
            }
            mapPrem.put("appId", applicationDto.getId());
            url = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + InboxConst.URL_LICENCE_WEB_MODULE + "MohCessationApplication?appId=" + applicationDto.getId() + "&premiseId=" + appGrpPremId;
        }*/
        url=HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        emailMap.put("systemLink", url);
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER_MSG);
        emailParam.setTemplateContent(emailMap);
        emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        emailParam.setMaskParams(mapPrem);
        emailParam.setSvcCodeList(svcCode);
        emailParam.setRefId(applicationDto.getApplicationNo());
        notificationHelper.sendNotification(emailParam);
    }

    private void getInfo() throws Exception{
        List<ApplicationDto> applicationDtos = applicationClient.getRfiReminder().getEntity();
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();

        ListIterator<ApplicationDto> iterator = applicationDtos.listIterator();
        while (iterator.hasNext()){
            ApplicationDto applicationDto = iterator.next();
            Date modifiedAt = applicationDto.getModifiedAt();
            calendar.setTime(modifiedAt);
            calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(reminderMax1Day));
            Date firstTime = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(reminderMax2Day));
            Date secondTime = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(reminderMax3Day));
            Date thirdTime = calendar.getTime();
            if(date.after(firstTime)&&date.before(secondTime)){
                boolean checkEmailIsSend = checkEmailIsSend(applicationDto.getId(), "sendRfi" + Integer.parseInt(reminderMax1Day));
                if(checkEmailIsSend){
                    try {
                        sendEmail(applicationDto,reminderMax1Day);
                    }catch (Exception e){
                       log.error(e.getMessage(),e);
                    }
                }
            }else if(date.after(secondTime)&&date.before(thirdTime)){
                boolean checkEmailIsSend = checkEmailIsSend(applicationDto.getId(), "sendRfi" + Integer.parseInt(reminderMax2Day));
                if(checkEmailIsSend){
                    try {
                        sendEmail(applicationDto,reminderMax2Day);
                    }catch (Exception e){
                        log.error(e.getMessage(),e);
                    }
                }
            }else if(date.after(thirdTime)){
                boolean checkEmailIsSend = checkEmailIsSend(applicationDto.getId(), "sendRfi" + Integer.parseInt(reminderMax3Day));
                if(checkEmailIsSend){
                    try {
                        sendEmail(applicationDto,reminderMax3Day);
                    }catch (Exception e){
                        log.error(e.getMessage(),e);
                    }
                }
            }
        }

    }

    private boolean checkEmailIsSend(String applicationId,String magKey){
        try {
            JobRemindMsgTrackingDto auto_renew = systemBeLicClient.getJobRemindMsgTrackingDto(applicationId, magKey).getEntity();
            if(auto_renew==null){
                return true;
            }else {
                log.info(StringUtil.changeForLog(JsonUtil.parseToJson(auto_renew)+"auto_renew"));
                return false;
            }

        }catch (Exception e){
            log.info(e.getMessage(),e);
            log.info(StringUtil.changeForLog("-----have error---"));
            return false;
        }
    }
    private void saveMailJob(String applicationId,String magKey){
        JobRemindMsgTrackingDto jobRemindMsgTrackingDto=new JobRemindMsgTrackingDto();
        jobRemindMsgTrackingDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        jobRemindMsgTrackingDto.setMsgKey(magKey);
        jobRemindMsgTrackingDto.setRefNo(applicationId);
        List<JobRemindMsgTrackingDto> jobRemindMsgTrackingDtos=new ArrayList<>(1);
        jobRemindMsgTrackingDtos.add(jobRemindMsgTrackingDto);
        systemBeLicClient.createJobRemindMsgTrackingDtos(jobRemindMsgTrackingDtos).getEntity();
    }
}
