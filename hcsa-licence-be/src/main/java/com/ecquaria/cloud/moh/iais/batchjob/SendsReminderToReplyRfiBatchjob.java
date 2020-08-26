package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
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
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    }

    private void reminder(LicPremisesReqForInfoDto licPremisesReqForInfoDto) throws IOException, TemplateException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        String templateId=MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER;
        InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(templateId);
        String licenseeId=requestForInformationService.getLicPreReqForInfo(licPremisesReqForInfoDto.getId()).getLicenseeId();
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
        String applicantName=licenseeDto.getName();
        Map<String,Object> map=IaisCommonUtils.genNewHashMap();
        StringBuilder stringBuilder=new StringBuilder();
        int i=0;
        if(!StringUtil.isEmpty(licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos())){
            for ( i=0;i<licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos().size();i++) {
                stringBuilder.append("<p>   ").append(i+1).append(". ").append("Information : ").append(licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos().get(i).getTitle()).append("</p>");
            }
        }
        if(licPremisesReqForInfoDto.isNeedDocument()){
            for (int j=0;j<licPremisesReqForInfoDto.getLicPremisesReqForInfoDocDto().size();j++) {
                stringBuilder.append("<p>   ").append(j+i+1).append(". ").append("Documentations : ").append(licPremisesReqForInfoDto.getLicPremisesReqForInfoDocDto().get(j).getTitle()).append("</p>");
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
        List<LicAppCorrelationDto> licAppCorrelationDtos=hcsaLicenceClient.getLicCorrBylicId(licenceViewDto.getLicenceDto().getId()).getEntity();
        ApplicationDto applicationDto=applicationClient.getApplicationById(licAppCorrelationDtos.get(0).getApplicationId()).getEntity();
        try{


            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_INBOX;
            Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
            emailMap.put("ApplicantName", applicantName);
            emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{RequestForInformationConstants.AD_HOC}).get(0).getText());
            emailMap.put("ApplicationNumber", licPremisesReqForInfoDto.getLicenceNo());
            emailMap.put("ApplicationDate", Formatter.formatDate(new Date()));
            emailMap.put("email", systemParamConfig.getSystemAddressOne());
            emailMap.put("TATtime", Formatter.formatDate(cal.getTime()));
            emailMap.put("systemLink", loginUrl);
            emailMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);

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
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER_SMS);
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
            notificationHelper.sendNotification(emailParam);
            //msg
            emailMap.put("systemLink", url);
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER_MSG);
            emailParam.setTemplateContent(emailMap);
            emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_ACTION_REQUIRED);
            emailParam.setMaskParams(mapPrem);
            emailParam.setRefId(applicationDto.getApplicationNo());
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
}
