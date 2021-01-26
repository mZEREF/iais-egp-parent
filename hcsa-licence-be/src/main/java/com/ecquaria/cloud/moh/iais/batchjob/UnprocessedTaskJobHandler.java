package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppStageSlaTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.HcsaSvcKpiDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskEmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.KpiAndReminderService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
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
 * NotifyUnprocessedTaskBatchjob
 *
 * @author Guyin
 * @date 12/04/2019
 */
@JobHandler(value="unprocessedTaskJobHandler")
@Component
@Slf4j
public class UnprocessedTaskJobHandler extends IJobHandler {
    @Autowired
    private TaskService taskService;
    @Autowired
    private InspEmailService inspEmailService;
    @Autowired
    private EmailClient emailClient;
    @Autowired
    private SystemParamConfig systemParamConfig;

    private final String EMAILMPLATEID = "A0D4EADD-D61B-EA11-BE7D-000C29F371DC";

    @Autowired
    MsgTemplateClient msgTemplateClient;

    @Autowired
    private GenerateIdClient generateIdClient;

    @Autowired
    ApplicationService applicationService;

    @Autowired
    SystemBeLicClient systemBeLicClient;

    @Autowired
    OrganizationClient organizationClient;

    @Autowired
    InsRepService insRepService;

    @Autowired
    InspectionAssignTaskService inspectionAssignTaskService;

    @Value("${iais.email.sender}")
    private String mailSender;

    @Autowired
    KpiAndReminderService kpiAndReminderService;
    @Override
    public ReturnT<String> execute(String s) throws IOException, TemplateException{
        log.info(StringUtil.changeForLog("The NotifyUnprocessedTaskBatchjob is  start..." ));
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        List<TaskEmailDto> taskEmailDtoList = IaisCommonUtils.genNewArrayList();
        taskEmailDtoList = taskService.getEmailNotifyList();

        int count = 0;
        if(taskEmailDtoList != null) {
            for (TaskEmailDto item : taskEmailDtoList
            ) {
                try{
                    count ++ ;
                    log.info(StringUtil.changeForLog("count:" +count));
                    log.info(StringUtil.changeForLog("getApplicationBytaskId:" +item.getRefNo()));
                    //get application
                    ApplicationDto applicationDto = applicationService.getApplicationBytaskId(item.getRefNo());
                    String stage;
                    if(HcsaConsts.ROUTING_STAGE_INS.equals(item.getTaskKey())){
                        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto =
                                insRepService.getAppPremisesRoutingHistorySubStage(item.getRefNo(), item.getTaskKey());
                        stage = appPremisesRoutingHistoryDto.getSubStage();
                    } else {
                        stage = item.getTaskKey();
                    }
                    log.info(StringUtil.changeForLog("applicationDto:" +applicationDto.getApplicationNo()));
                    HcsaSvcKpiDto hcsaSvcKpiDto = kpiAndReminderService.searchKpi(HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId()).getSvcCode(), applicationDto.getApplicationType());
                    if(hcsaSvcKpiDto != null) {
                        log.info(StringUtil.changeForLog("hcsaSvcKpiDto:" +hcsaSvcKpiDto.getRemThreshold()));
                        //get current stage worked days
                        int days = 0;
                        if(!StringUtil.isEmpty(stage)) {
                            AppStageSlaTrackingDto appStageSlaTrackingDto = inspectionAssignTaskService.searchSlaTrackById(applicationDto.getApplicationNo(), stage);
                            if (appStageSlaTrackingDto != null) {
                                days = appStageSlaTrackingDto.getKpiSlaDays();
                            }
                        }
                        //get warning value
                        Map<String, Integer> kpiMap = hcsaSvcKpiDto.getStageIdKpi();
                        int kpi = 0;
                        if(!StringUtil.isEmpty(stage)) {
                            if (kpiMap != null && kpiMap.get(stage) != null) {
                                kpi = kpiMap.get(stage);
                            }
                        }
                        //get threshold value
                        int remThreshold = 0;
                        if (hcsaSvcKpiDto.getRemThreshold() != null) {
                            remThreshold = hcsaSvcKpiDto.getRemThreshold();
                        }
                        int sysday = systemParamConfig.getUnprocessedSystemAdmin() + kpi + 1;
                        log.info(StringUtil.changeForLog("days:" +days));
                        log.info(StringUtil.changeForLog("kpi:" +kpi));
                        log.info(StringUtil.changeForLog("remThreshold:" +remThreshold));
                        log.info(StringUtil.changeForLog("systemParamConfig.getUnprocessedSystemAdmin():" +systemParamConfig.getUnprocessedSystemAdmin()));

                        if(days == remThreshold && remThreshold > 0){
                            log.info(StringUtil.changeForLog("send email to officer:"));
                                List<String> email = IaisCommonUtils.genNewArrayList();
                                List<String> sms = IaisCommonUtils.genNewArrayList();
                                email.add(item.getUserEmail());
                                sms.add(item.getUserMobile());
                                String subject = StringUtil.changeForLog("Reminder for "+applicationDto.getApplicationNo()+" processing");
                                MsgTemplateDto msgTemplateDto = generateIdClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_UNPROCESSED_TASK_1).getEntity();
                                Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
                                templateContent.put("officer", item.getUserName());
                                templateContent.put("applicationNo", applicationDto.getApplicationNo());
                                String mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), templateContent);

                                sendEmail(item,email,subject,mesContext);
                                sendSms(item,email,mesContext);
                        }else if(days == (kpi + 1) && kpi > 0){
                            List<OrgUserDto> leaders = taskService.getEmailNotifyLeader(item.getId());
                            log.info(StringUtil.changeForLog("send email to leader:"));
                            for (OrgUserDto leader: leaders
                                 ) {
                                List<String> email = IaisCommonUtils.genNewArrayList();
                                List<String> sms = IaisCommonUtils.genNewArrayList();
                                email.add(leader.getEmail());
                                sms.add(leader.getMobileNo());
                                String subject = StringUtil.changeForLog("Reminder for "+applicationDto.getApplicationNo()+" processing for "+ item.getUserName());
                                MsgTemplateDto msgTemplateDto = generateIdClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_UNPROCESSED_TASK_2).getEntity();
                                Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
                                templateContent.put("officer", item.getUserName());
                                templateContent.put("supervisor", leader.getDisplayName());
                                templateContent.put("applicationNo", applicationDto.getApplicationNo());
                                String mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), templateContent);

                                sendEmail(item,email,subject,mesContext);
                                sendSms(item,email,mesContext);
                            }
                        }else if(days == sysday){
                            log.info(StringUtil.changeForLog("send email to admin:"));
                                List<OrgUserDto> systemAdmin = organizationClient.retrieveOrgUserAccountByRoleId(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN).getEntity();
                                if(!IaisCommonUtils.isEmpty(systemAdmin)){
                                    for (OrgUserDto orgUserDto:systemAdmin
                                         ) {
                                        List<String> email = IaisCommonUtils.genNewArrayList();
                                        List<String> sms = IaisCommonUtils.genNewArrayList();
                                        email.add(orgUserDto.getEmail());
                                        sms.add(orgUserDto.getMobileNo());
                                        String subject = StringUtil.changeForLog("Reminder for "+applicationDto.getApplicationNo()+" processing for "+item.getUserName());
                                        MsgTemplateDto msgTemplateDto = generateIdClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_UNPROCESSED_TASK_3).getEntity();
                                        Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
                                        templateContent.put("systemAdmin", orgUserDto.getDisplayName());
                                        templateContent.put("officer", item.getUserName());
                                        templateContent.put("applicationNo", applicationDto.getApplicationNo());
                                        templateContent.put("days", systemParamConfig.getUnprocessedSystemAdmin());
                                        String mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), templateContent);
                                        sendEmail(item,email,subject,mesContext);
                                        sendSms(item,sms,mesContext);
                                    }
                                }

                        }
                    }

                }catch (Exception e){
                    JobLogger.log(e);
                    log.error(e.getMessage(), e);
                    continue;
                }

            }
        }


        log.debug(StringUtil.changeForLog("Unprocessed Task Notification end..." ));
        return ReturnT.SUCCESS;

    }

    private void sendEmail(TaskEmailDto item,List<String> emailAddr,String subject,String content){
        EmailDto email = new EmailDto();
        email.setSubject(subject);
        email.setReqRefNum(item.getId());
        email.setContent(content);
        email.setSender(mailSender);
        email.setClientQueryCode(item.getId());
        email.setReceipts(emailAddr);
        emailClient.sendNotification(email).getEntity();
    }

    private void sendSms(TaskEmailDto item,List<String> smsAddr,String content){
        SmsDto smsDto = new SmsDto();
        smsDto.setContent(content);
        smsDto.setReqRefNum(item.getId());
        smsDto.setReceipts(smsAddr);
        smsDto.setOnlyOfficeHour(true);
        smsDto.setSender(mailSender);
        emailClient.sendSMS(smsAddr,smsDto,item.getId());
    }
}
