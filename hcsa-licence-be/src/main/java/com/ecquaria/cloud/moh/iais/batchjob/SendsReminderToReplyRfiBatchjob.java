package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
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
    private ApplicationClient applicationClient;
    @Autowired
    EmailClient emailClient;
    @Autowired
    private InboxMsgService inboxMsgService;
    @Autowired
    HcsaConfigClient hcsaConfigClient;
    @Value("${iais.email.sender}")
    private String mailSender;
    @Value("${iais.system.adhoc.rfi.due.day}")
    private int rfiDueDay;
    @Value("${iais.system.adhoc.rfi.reminder.maxday}")
    private int maxReminderDay;
    @Value("${iais.system.adhoc.rfi.reminder}")
    private int maxReminderNum;

    @Autowired
    OrganizationClient organizationClient;
    public void start(BaseProcessClass bpc){
        logAbout("start");
    }

    public void sendMsg(BaseProcessClass bpc) throws IOException, TemplateException {
        logAbout("sendMsg");
        List<LicPremisesReqForInfoDto> licPremisesReqForInfoDtos= requestForInformationService.getAllReqForInfo();
        for (LicPremisesReqForInfoDto rfi:licPremisesReqForInfoDtos
             ) {
            if(rfi.getDueDateSubmission().compareTo(new Date())>0&&(rfi.getStatus().equals(RequestForInformationConstants.RFI_NEW)||rfi.getStatus().equals(RequestForInformationConstants.RFI_RETRIGGER))){
                reminder(rfi);
            }
        }

    }

    private void reminder(LicPremisesReqForInfoDto licPremisesReqForInfoDto) throws IOException, TemplateException {
        String templateId="BEFC2AF0-250C-EA11-BE78-000C29D29DB0";
        InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(templateId);
        LicenceViewDto licenceViewDto=licInspNcEmailService.getLicenceDtoByLicPremCorrId(rfiEmailTemplateDto.getLicPremCorrId());
        List<LicAppCorrelationDto> licAppCorrelationDtos=hcsaLicenceClient.getLicCorrBylicId(licenceViewDto.getLicenceDto().getId()).getEntity();
        ApplicationDto applicationDto=applicationClient.getApplicationById(licAppCorrelationDtos.get(0).getApplicationId()).getEntity();
        String licenseeId=requestForInformationService.getLicPreReqForInfo(licPremisesReqForInfoDto.getId()).getLicenseeId();
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
        String applicantName=licenseeDto.getName();
        HashMap<String,String> mapPrem=IaisCommonUtils.genNewHashMap();
        mapPrem.put("licenseeId",licenseeId);
        Map<String,Object> map=IaisCommonUtils.genNewHashMap();
        map.put("APPLICANT_NAME",StringUtil.viewHtml(applicantName));
        StringBuilder stringBuilder=new StringBuilder();
        int i=0;
        if(!IaisCommonUtils.isEmpty(licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos())){
            for ( i=0;i<licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos().size();i++) {
                stringBuilder.append("<p>   ").append(i+1).append(". ").append("information  ").append(licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos().get(i).getTitle()).append("</p>");
            }
        }
        if(licPremisesReqForInfoDto.isNeedDocument()){
            for (int j=0;j<licPremisesReqForInfoDto.getLicPremisesReqForInfoDocDto().size();j++) {
                stringBuilder.append("<p>   ").append(j+i+1).append(". ").append("Documentations  ").append(licPremisesReqForInfoDto.getLicPremisesReqForInfoDocDto().get(i).getTitle()).append("</p>");
            }
        }

        map.put("DETAILS",StringUtil.viewHtml(stringBuilder.toString()));
        map.put("MOH_NAME", StringUtil.viewHtml(AppConsts.MOH_AGENCY_NAME));
        String mesContext= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getMessageContent(),map);
        mesContext=mesContext.replace("Application Number",licenceViewDto.getLicenceDto().getLicenceNo());
        String subject=rfiEmailTemplateDto.getSubject().replace("Application Number",licenceViewDto.getLicenceDto().getLicenceNo());
        try{
            EmailDto emailDto=new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject(subject);
            emailDto.setSender(mailSender);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(licPremisesReqForInfoDto.getLicPremId());
            String requestRefNum = emailClient.sendNotification(emailDto).getEntity();
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        //send message to FE user.
        InterMessageDto interMessageDto = new InterMessageDto();
        interMessageDto.setMaskParams(mapPrem);
        interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
        interMessageDto.setSubject(subject);
        interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED);
        String messageNo = inboxMsgService.getMessageNo();
        interMessageDto.setRefNo(messageNo);
        HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(licenceViewDto.getLicenceDto().getSvcName()).getEntity();
        interMessageDto.setService_id(svcDto.getId());
        interMessageDto.setMsgContent(mesContext);
        interMessageDto.setStatus(MessageConstants.MESSAGE_STATUS_UNREAD);
        interMessageDto.setUserId(licenseeId);
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        inboxMsgService.saveInterMessage(interMessageDto);

        licPremisesReqForInfoDto.setReminder(licPremisesReqForInfoDto.getReminder()+1);
        Calendar cal = Calendar.getInstance();
        cal.setTime(licPremisesReqForInfoDto.getDueDateSubmission());
        cal.add(Calendar.DAY_OF_MONTH, rfiDueDay);
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,-maxReminderDay);
        Date outDate=calendar.getTime();
        licPremisesReqForInfoDto.setStatus(RequestForInformationConstants.RFI_RETRIGGER);
        if(licPremisesReqForInfoDto.getReminder()>maxReminderNum && licPremisesReqForInfoDto.getRequestDate().after(outDate)){
            licPremisesReqForInfoDto.setStatus(RequestForInformationConstants.RFI_CLOSE);
        }
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
        eicRequestTrackingDto.setDtoObject(JsonUtil.parseToJson(licPremisesReqForInfoDto));
        eicRequestTrackingDto.setProcessNum(1);
        eicRequestTrackingDto.setFirstActionAt(now);
        eicRequestTrackingDto.setLastActionAt(now);
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        eicRequestTrackingDto.setRefNo(System.currentTimeMillis()+"");
        licPremisesReqForInfoDto.setEventRefNo(eicRequestTrackingDto.getRefNo());
        requestForInformationService.updateLicEicRequestTrackingDto(eicRequestTrackingDto);
        requestForInformationService.createFeRfiLicDto(licPremisesReqForInfoDto);

    }
    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }
}
