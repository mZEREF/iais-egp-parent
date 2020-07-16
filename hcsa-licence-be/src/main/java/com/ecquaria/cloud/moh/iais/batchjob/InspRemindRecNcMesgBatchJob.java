package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Process MohRemindRecNcMesg
 *
 * @author Shicheng
 * @date 2020/4/29 13:59
 **/
@Delegator("remindRecNcMesgBatchJob")
@Slf4j
public class InspRemindRecNcMesgBatchJob {

    @Autowired
    private InspEmailService inspEmailService;

    @Autowired
    private EmailClient emailClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Value("${iais.email.sender}")
    private String mailSender;

    @Autowired
    private SystemBeLicClient systemBeLicClient;

    /**
     * StartStep: remindRecNcMesgStart
     *
     * @param bpc
     * @throws
     */
    public void remindRecNcMesgStart(BaseProcessClass bpc){
        logAbout("Remind Applicant Rectify N/C");
    }

    /**
     * StartStep: remindRecNcMesgStep
     *
     * @param bpc
     * @throws
     */
    public void remindRecNcMesgStep(BaseProcessClass bpc){
        logAbout("Remind Applicant Rectify N/C Do");
        List<ApplicationDto> applicationDtos = applicationClient.getApplicationByStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION).getEntity();
        if(IaisCommonUtils.isEmpty(applicationDtos)){
            return;
        }
        int weeks = systemParamConfig.getReminderRectification();
        int days = weeks * 7;
        log.info(StringUtil.changeForLog("System days = " + days));
        JobLogger.log(StringUtil.changeForLog("System days = " + days));
        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        for(ApplicationDto applicationDto : applicationDtos){
            ApplicationGroupDto applicationGroupDto = inspectionTaskClient.getApplicationGroupDtoByAppGroId(applicationDto.getAppGrpId()).getEntity();
            String licenseeId = applicationGroupDto.getLicenseeId();
            JobRemindMsgTrackingDto jobRemindMsgTrackingDto2 = systemBeLicClient.getJobRemindMsgTrackingDto(applicationDto.getApplicationNo(), MessageConstants.JOB_REMIND_MSG_KEY_REMIND_RECTIFICATION_EMAIL).getEntity();
            if(jobRemindMsgTrackingDto2 == null) {
                log.info(StringUtil.changeForLog("jobRemindMsgTrackingDto2 null"));
                JobLogger.log(StringUtil.changeForLog("jobRemindMsgTrackingDto2 null"));
                inspectionDateSendEmail(licenseeId, applicationDto.getId());
                createJobRemindMsgTrackingDto(intranet, applicationDto.getApplicationNo());
            } else {
                Date createDate = jobRemindMsgTrackingDto2.getCreateTime();
                log.info(StringUtil.changeForLog("jobRemindMsgTrackingDto2 not null, CreateDate = " + createDate));
                JobLogger.log(StringUtil.changeForLog("jobRemindMsgTrackingDto2 not null, CreateDate = " + createDate));
                List<Date> dayList = MiscUtil.getDateInPeriodByRecurrence(createDate, new Date());
                int nowDays = dayList.size();
                log.info(StringUtil.changeForLog("jobRemindMsgTrackingDto2 not null, nowDays = " + nowDays));
                JobLogger.log(StringUtil.changeForLog("jobRemindMsgTrackingDto2 not null, nowDays = " + nowDays));
                if(nowDays > days){
                    inspectionDateSendEmail(licenseeId, applicationDto.getId());
                    jobRemindMsgTrackingDto2.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    jobRemindMsgTrackingDto2.setAuditTrailDto(intranet);
                    systemBeLicClient.updateJobRemindMsgTrackingDto(jobRemindMsgTrackingDto2);
                    createJobRemindMsgTrackingDto(intranet, applicationDto.getApplicationNo());
                }
            }
        }
    }

    private void createJobRemindMsgTrackingDto(AuditTrailDto intranet, String applicationNo) {
        List<JobRemindMsgTrackingDto> jobRemindMsgTrackingDtos = IaisCommonUtils.genNewArrayList();
        JobRemindMsgTrackingDto jobRemindMsgTrackingDto = new JobRemindMsgTrackingDto();
        jobRemindMsgTrackingDto.setAuditTrailDto(intranet);
        jobRemindMsgTrackingDto.setMsgKey(MessageConstants.JOB_REMIND_MSG_KEY_REMIND_RECTIFICATION_EMAIL);
        jobRemindMsgTrackingDto.setRefNo(applicationNo);
        jobRemindMsgTrackingDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        jobRemindMsgTrackingDto.setId(null);
        jobRemindMsgTrackingDtos.add(jobRemindMsgTrackingDto);
        systemBeLicClient.createJobRemindMsgTrackingDtos(jobRemindMsgTrackingDtos);
    }

    private void inspectionDateSendEmail(String licenseeId, String appId) {
        InspectionEmailTemplateDto inspectionEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_REMIND_NC_RECTIFICATION);
        if(inspectionEmailTemplateDto != null) {
            String mesContext;
            try {
                mesContext = MsgUtil.getTemplateMessageByContent(inspectionEmailTemplateDto.getMessageContent(), null);
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage(), e);
                throw new IaisRuntimeException(e);
            }
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject(inspectionEmailTemplateDto.getSubject());
            emailDto.setSender(mailSender);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(appId);
            emailClient.sendNotification(emailDto);
        }
    }

    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The*****" + methodName +"******Start****"));
    }
}
