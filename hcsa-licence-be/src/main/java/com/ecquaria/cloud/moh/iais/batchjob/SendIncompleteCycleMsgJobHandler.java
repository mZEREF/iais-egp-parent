package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IncompleteCycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.AssistedReproductionService;
import com.ecquaria.cloud.moh.iais.service.LicenseeService;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@JobHandler(value = "SendIncompleteCycleMsgJobHandler")
@Component
@Slf4j
public class SendIncompleteCycleMsgJobHandler extends IJobHandler {
    private final static String INCOMPLETE_CYCLE_MESSAGE_KEY = "send_incomplete_cycle_job";
    @Autowired
    AssistedReproductionService assistedReproductionService;
    @Autowired
    SystemBeLicClient systemBeLicClient;
    @Autowired
    LicenseeService licenseeService;
    @Autowired
    NotificationHelper notificationHelper;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            JobLogger.log(StringUtil.changeForLog("SendIncompleteCycleMsgJobHandler start ..."));
            log.info("-----SendIncompleteCycleMsgJobHandler start ------");
            AuditTrailHelper.setupBatchJobAuditTrail(this);

            int firstDays = Integer.parseInt(MasterCodeUtil.getCodeDesc("DSARICN001"));
            int preDays = Integer.parseInt(MasterCodeUtil.getCodeDesc("DSARICN002"));
            List<IncompleteCycleDto> overDayNotCompletedCycle = assistedReproductionService.getOverDayNotCompletedCycleDto(firstDays);
            if (IaisCommonUtils.isNotEmpty(overDayNotCompletedCycle)) {
                List<JobRemindMsgTrackingDto> jobRemindMsgTrackingDtos = IaisCommonUtils.genNewArrayList();
                for (IncompleteCycleDto incompleteCycleDto : overDayNotCompletedCycle) {
                    String LicenseeId = incompleteCycleDto.getLicenseeId();
                    DataSubmissionDto dataSubmissionDto = incompleteCycleDto.getDataSubmissionDto();
                    String cycleId = dataSubmissionDto.getCycleId();
                    JobRemindMsgTrackingDto jobRemindMsgTrackingDto = systemBeLicClient.getJobRemindMsgTrackingDtoByMsgAAndCreatedAt(cycleId, INCOMPLETE_CYCLE_MESSAGE_KEY).getEntity();
                    String submissionerName = licenseeService.getLicenseeDtoById(LicenseeId).getName();
                    PatientInfoDto patientInfoDto = assistedReproductionService.patientInfoDtoBySubmissionId(dataSubmissionDto.getId());
                    if (patientInfoDto != null) {
                        String patientName = patientInfoDto.getPatient().getName();
                        String submissionNo = dataSubmissionDto.getSubmissionNo();
                        if (jobRemindMsgTrackingDto == null) {
                            // send first
                            String dateStr = Formatter.formatDateTime(dataSubmissionDto.getSubmitDt(), "dd/MM/yyyy HH:mm:ss");
                            sendFirstNotification(LicenseeId, submissionerName, patientName, submissionNo, dateStr);
                            addJobRemindMsgTrack(jobRemindMsgTrackingDtos, cycleId);
                        } else {
                            Date date = new Date(new Date().getTime() - 1000 * 60 * 60 * 24L * preDays);
                            if (dataSubmissionDto.getSubmitDt().before(date)) {
                                // send pre
                                sendPertNotification(LicenseeId, submissionerName, patientName, submissionNo);
                                addJobRemindMsgTrack(jobRemindMsgTrackingDtos, cycleId);
                            }
                        }
                    } else {
                        log.warn("sendIncompleteCycleMsg no find patientInfoDto, submission id is {}", dataSubmissionDto.getId());
                    }
                }
                systemBeLicClient.createJobRemindMsgTrackingDtos(jobRemindMsgTrackingDtos);
            }

            JobLogger.log(StringUtil.changeForLog("SendIncompleteCycleMsgJobHandler end ..."));
            log.info("-----SendIncompleteCycleMsgJobHandler end ------");
            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }

    private void addJobRemindMsgTrack(List<JobRemindMsgTrackingDto> jobRemindMsgTrackingDtos, String cycleId) {
        AuditTrailDto intranet = AuditTrailHelper.getCurrentAuditTrailDto();
        JobRemindMsgTrackingDto jobRemindMsgTrackingDto = new JobRemindMsgTrackingDto();
        jobRemindMsgTrackingDto.setAuditTrailDto(intranet);
        jobRemindMsgTrackingDto.setMsgKey(INCOMPLETE_CYCLE_MESSAGE_KEY);
        jobRemindMsgTrackingDto.setRefNo(cycleId);
        jobRemindMsgTrackingDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        jobRemindMsgTrackingDtos.add(jobRemindMsgTrackingDto);
    }

    private void sendFirstNotification(String licenseeId, String submissionerName, String patientName, String submssionNo, String dateStr) throws TemplateException, IOException {
        MsgTemplateDto msgTemplateDto = notificationHelper.getMsgTemplate(
                MsgTemplateConstants.MSG_TEMPLATE_AR_INCOMPLETE_CYCLE_MSG);

        Map<String, Object> msgContentMap = IaisCommonUtils.genNewHashMap();
        msgContentMap.put("submissionerName", submissionerName);
        msgContentMap.put("patientName", patientName);
        msgContentMap.put("date", dateStr);
        msgContentMap.put("submissionId", submssionNo);

        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_AR_INCOMPLETE_CYCLE_MSG);
        msgParam.setTemplateContent(msgContentMap);
        msgParam.setSubject(msgTemplateDto.getTemplateName());
        msgParam.setQueryCode(submssionNo);
        msgParam.setReqRefNum(submssionNo);
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        msgParam.setRefId(licenseeId);
        notificationHelper.sendNotification(msgParam);
        //send email
        EmailParam emailParamEmail = MiscUtil.transferEntityDto(msgParam, EmailParam.class);
        emailParamEmail.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_AR_INCOMPLETE_CYCLE_EMAIL);
        emailParamEmail.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENSEE_ID);
        notificationHelper.sendNotification(emailParamEmail);
    }

    private void sendPertNotification(String licenseeId, String submissionerName, String patientName, String submssionNo) throws TemplateException, IOException {
        MsgTemplateDto msgTemplateDto = notificationHelper.getMsgTemplate(
                MsgTemplateConstants.MSG_TEMPLATE_AR_INCOMPLETE_CYCLE_PER_MSG);

        Map<String, Object> msgContentMap = IaisCommonUtils.genNewHashMap();
        msgContentMap.put("submissionerName", submissionerName);
        msgContentMap.put("patientName", patientName);
        msgContentMap.put("submissionId", submssionNo);

        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_AR_INCOMPLETE_CYCLE_PER_MSG);
        msgParam.setTemplateContent(msgContentMap);
        msgParam.setSubject(msgTemplateDto.getTemplateName());
        msgParam.setQueryCode(submssionNo);
        msgParam.setReqRefNum(submssionNo);
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        msgParam.setRefId(licenseeId);
        notificationHelper.sendNotification(msgParam);
        //send email
        EmailParam emailParamEmail = MiscUtil.transferEntityDto(msgParam, EmailParam.class);
        emailParamEmail.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_AR_INCOMPLETE_CYCLE_PER_EMAIL);
        emailParamEmail.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENSEE_ID);
        notificationHelper.sendNotification(emailParamEmail);
    }
}
