package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MessageTemplateUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Process MohRemindDoPreInspTask
 *
 * @author Shicheng
 * @date 2020/7/29 17:49
 **/
@JobHandler(value="remindInspectorPreInspTaskJobHandler")
@Component
@Slf4j
public class RemindInspectorPreInspTaskJobHandler extends IJobHandler {

    @Autowired
    private InspectionAssignTaskService inspectionAssignTaskService;

    @Value("${iais.email.sender}")
    private String mailSender;

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private EmailClient emailClient;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private InspectionService inspectionService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            logAbout("Remind Inspector Do Pre Inspection");
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            List<ApplicationDto> applicationDtos =
                    applicationClient.getApplicationByStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS).getEntity();
            if(!IaisCommonUtils.isEmpty(applicationDtos)){
                for(ApplicationDto applicationDto : applicationDtos){
                    try {
                        sendEmailByInspReadiness(applicationDto);
                    } catch (Exception e) {
                        JobLogger.log(e);
                        log.error(e.getMessage(), e);
                        continue;
                    }
                }
            }
            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }

    public void remindDoPreInspTaskStep(BaseProcessClass bpc) {
        logAbout("Remind Inspector Do Pre Inspection");
        List<ApplicationDto> applicationDtos =
                applicationClient.getApplicationByStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS).getEntity();
        if(!IaisCommonUtils.isEmpty(applicationDtos)){
            for(ApplicationDto applicationDto : applicationDtos){
                try {
                    sendEmailByInspReadiness(applicationDto);
                } catch (Exception e) {
                    JobLogger.log(e);
                    log.error(e.getMessage(), e);
                    continue;
                }
            }
        }
    }

    private void sendEmailAndSmsByTask(Map<String, Object> templateMap, TaskDto taskDto) {
        String userId = taskDto.getUserId();
        String workGrpId = taskDto.getWkGrpId();
        //send sms
        List<String> leads = organizationClient.getInspectionLead(workGrpId).getEntity();
        if (!StringUtil.isEmpty(userId)) {
            OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
            if (orgUserDto != null && AppConsts.COMMON_STATUS_ACTIVE.equals(orgUserDto.getStatus())) {
                sendSms(orgUserDto, taskDto);
            }
        }
        if (!IaisCommonUtils.isEmpty(leads)) {
            for (String lead : leads) {
                if (!StringUtil.isEmpty(lead) && !lead.equals(userId)) {
                    OrgUserDto orgUserDtoLead = organizationClient.retrieveOrgUserAccountById(lead).getEntity();
                    if (orgUserDtoLead != null && AppConsts.COMMON_STATUS_ACTIVE.equals(orgUserDtoLead.getStatus())) {
                        sendSms(orgUserDtoLead, taskDto);
                    }
                }
            }
        }
        //send email
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_REMIND_INSPECTOR_PRE_INSP_READY).getEntity();
        if(msgTemplateDto == null){
            return;
        }
        if(AppConsts.COMMON_STATUS_IACTIVE.equals(msgTemplateDto.getStatus())){
            log.info("msgTemplateDto is INACTIVE.......");
            JobLogger.log(StringUtil.changeForLog("msgTemplateDto is INACTIVE......."));
        } else {
            if (msgTemplateDto != null) {
                if (!StringUtil.isEmpty(userId)) {
                    OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
                    if (orgUserDto != null && AppConsts.COMMON_STATUS_ACTIVE.equals(orgUserDto.getStatus())) {
                        sendEmail(orgUserDto, msgTemplateDto, taskDto, templateMap);
                    }
                }
                if (!IaisCommonUtils.isEmpty(leads)) {
                    for (String lead : leads) {
                        if (!StringUtil.isEmpty(lead) && !lead.equals(userId)) {
                            OrgUserDto orgUserDtoLead = organizationClient.retrieveOrgUserAccountById(lead).getEntity();
                            if (orgUserDtoLead != null && AppConsts.COMMON_STATUS_ACTIVE.equals(orgUserDtoLead.getStatus())) {
                                sendEmail(orgUserDtoLead, msgTemplateDto, taskDto, templateMap);
                            }
                        }
                    }
                }
            }
        }
    }

    private void sendSms(OrgUserDto orgUserDto, TaskDto taskDto) {
        int smsFlag = systemParamConfig.getEgpSmsNotifications();
        if (0 == smsFlag) {
            log.info("please turn on sms param.......");
            JobLogger.log(StringUtil.changeForLog("please turn on sms param......."));
        } else {
            List<String> mobile = IaisCommonUtils.genNewArrayList();
            String phoneNo = orgUserDto.getMobileNo();
            if(!StringUtil.isEmpty(phoneNo)) {
                mobile.add(phoneNo);
            }
            if (!IaisCommonUtils.isEmpty(mobile)) {
                SmsDto smsDto = new SmsDto();
                smsDto.setSender(mailSender);
                smsDto.setContent("MOH HALP - [Internal] Inspection Task not ready for inspection");
                smsDto.setOnlyOfficeHour(true);
                smsDto.setReceipts(mobile);
                smsDto.setReqRefNum(taskDto.getApplicationNo());
                emailClient.sendSMS(mobile, smsDto, taskDto.getApplicationNo());
            } else {
                log.info("mobile is null.......");
                JobLogger.log(StringUtil.changeForLog("mobile is null......."));
            }
        }
    }

    private void sendEmail(OrgUserDto orgUserDto, MsgTemplateDto msgTemplateDto, TaskDto taskDto, Map<String, Object> templateMap) {
        int emailFlag = systemParamConfig.getEgpEmailNotifications();
        if (0 == emailFlag) {
            log.info("please turn on email param.......");
            JobLogger.log(StringUtil.changeForLog("please turn on email param......."));
        } else {
            List<String> receiptEmail = IaisCommonUtils.genNewArrayList();
            String emailAddress = orgUserDto.getEmail();
            if(!StringUtil.isEmpty(emailAddress)) {
                receiptEmail.add(emailAddress);
            }
            templateMap.put("officer_name", orgUserDto.getDisplayName());
            String emailTemplate = msgTemplateDto.getMessageContent();
            //replace num
            emailTemplate = MessageTemplateUtil.replaceNum(emailTemplate);
            //get mesContext
            String mesContext;
            try {
                mesContext = MsgUtil.getTemplateMessageByContent(emailTemplate, templateMap);
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage(), e);
                throw new IaisRuntimeException(e);
            }
            if(!IaisCommonUtils.isEmpty(receiptEmail)) {
                EmailDto emailDto = new EmailDto();
                emailDto.setContent(mesContext);
                emailDto.setSubject(msgTemplateDto.getTemplateName());
                emailDto.setSender(mailSender);
                emailDto.setReceipts(receiptEmail);
                emailDto.setClientQueryCode(taskDto.getRefNo());
                emailDto.setReqRefNum(taskDto.getRefNo());
                emailClient.sendNotification(emailDto);
            } else {
                log.info("receiptEmail is null.......");
                JobLogger.log(StringUtil.changeForLog("receiptEmail is null......."));
            }
        }
    }

    private void sendEmailByInspReadiness(ApplicationDto applicationDto) {
        int days = systemParamConfig.getPreInspTaskTeminder();
        log.info(StringUtil.changeForLog("System Inspection days = " + days));
        JobLogger.log(StringUtil.changeForLog("System Inspection days = " + days));
        String appNo = applicationDto.getApplicationNo();
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremCorrByAppNo(appNo).getEntity();
        String appPremCorrId = appPremisesCorrelationDto.getId();
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrId, InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
        Date inspDate = appPremisesRecommendationDto.getRecomInDate();
        log.info(StringUtil.changeForLog("Inspection Date = " + inspDate));
        JobLogger.log(StringUtil.changeForLog("Inspection Date = " + inspDate));
        List<Date> dayList = MiscUtil.getDateInPeriodByRecurrence(new Date(), inspDate);
        if(!IaisCommonUtils.isEmpty(dayList)){
            int nowDays = dayList.size() - 1;
            log.info(StringUtil.changeForLog("jobRemindMsgTrackingDto2 not null, nowDays = " + nowDays));
            JobLogger.log(StringUtil.changeForLog("jobRemindMsgTrackingDto2 not null, nowDays = " + nowDays));
            if(days == nowDays){
                Map<String, Object> templateMap = getEmailField(applicationDto, appPremisesCorrelationDto);
                List<TaskDto> taskDtos = organizationClient.getCurrTaskByRefNo(appPremCorrId).getEntity();
                if(!IaisCommonUtils.isEmpty(taskDtos)){
                    for(TaskDto taskDto : taskDtos){
                        if(taskDto != null) {
                            //send email and sms
                            sendEmailAndSmsByTask(templateMap, taskDto);
                        }
                    }
                }
            }
        }
    }

    private Map<String, Object> getEmailField(ApplicationDto applicationDto, AppPremisesCorrelationDto appPremisesCorrelationDto) {
        Map<String, Object> templateMap = IaisCommonUtils.genNewHashMap();
        AppGrpPremisesDto appGrpPremisesDto = inspectionAssignTaskService.getAppGrpPremisesDtoByAppGroId(appPremisesCorrelationDto.getId());
        List<String> appGroupIds = IaisCommonUtils.genNewArrayList();
        appGroupIds.add(applicationDto.getAppGrpId());
        HcsaTaskAssignDto hcsaTaskAssignDto = inspectionService.getHcsaTaskAssignDtoByAppGrp(appGroupIds);
        String address = inspectionAssignTaskService.getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
        String hciName = appGrpPremisesDto.getHciName();
        String hciCode = appGrpPremisesDto.getHciCode();
        if(StringUtil.isEmpty(hciName)){
            hciName = "";
        }
        if(StringUtil.isEmpty(hciCode)){
            hciCode = "";
        }
        String appNo = applicationDto.getApplicationNo();
        String appType = applicationDto.getApplicationType();
        String serviceId = applicationDto.getServiceId();
        HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
        String appGroupId = applicationDto.getAppGrpId();
        ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(appGroupId).getEntity();
        Date appDate = applicationGroupDto.getSubmitDt();
        if(appDate == null){
            appDate = new Date();
        }
        String appDateStr = Formatter.formatDateTime(appDate, "dd/MM/yyyy HH:mm:ss");
        templateMap.put("appNo", appNo);
        if(!StringUtil.isEmpty(hciCode)) {
            templateMap.put("hciName", hciName);
        }
        if(!StringUtil.isEmpty(hciCode)) {
            templateMap.put("hciCode", hciCode);
        }
        templateMap.put("hciAddress", address);
        templateMap.put("serviceName", hcsaServiceDto.getSvcName());
        templateMap.put("applicationNo", appNo);
        String appTypeShow = MasterCodeUtil.getCodeDesc(appType);
        templateMap.put("appType", appTypeShow);
        templateMap.put("appDate", appDateStr);
        templateMap.put("officer_name", "officer_name");
        return templateMap;
    }

    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The*****" + methodName +"******Start****"));
    }
}
