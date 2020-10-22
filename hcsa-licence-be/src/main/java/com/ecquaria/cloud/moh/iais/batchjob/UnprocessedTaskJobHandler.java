package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppStageSlaTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.HcsaSvcKpiDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskEmailDto;
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
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
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
        log.debug(StringUtil.changeForLog("The NotifyUnprocessedTaskBatchjob is  start..." ));
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        List<TaskEmailDto> taskEmailDtoList = IaisCommonUtils.genNewArrayList();
        taskEmailDtoList = taskService.getEmailNotifyList();

        //get email template
        InspectionEmailTemplateDto inspectionEmailTemplateDto = inspEmailService.loadingEmailTemplate(EMAILMPLATEID);

        String templateHtml = inspectionEmailTemplateDto.getMessageContent();
        if(taskEmailDtoList != null) {
            for (TaskEmailDto item : taskEmailDtoList
            ) {
                try{
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
                        int sysday = systemParamConfig.getUnprocessedSystemAdmin() + kpi;
                        log.info(StringUtil.changeForLog("days:" +days));
                        log.info(StringUtil.changeForLog("kpi:" +kpi));
                        log.info(StringUtil.changeForLog("remThreshold:" +remThreshold));
                        log.info(StringUtil.changeForLog("systemParamConfig.getUnprocessedSystemAdmin():" +systemParamConfig.getUnprocessedSystemAdmin()));

                        if(days == remThreshold){
                            //judge is email sent
                            JobRemindMsgTrackingDto jobRemindMsgTrackingDto = systemBeLicClient.getJobRemindMsgTrackingDto(item.getId(),"unprocess officer").getEntity();
                            if(jobRemindMsgTrackingDto == null){
                                //send email to officer
                                List<String> email = IaisCommonUtils.genNewArrayList();
                                email.add(item.getUserEmail());
                                sendEmail(item,applicationDto,email);
                                //record email sent
                                JobRemindMsgTrackingDto createjob = new JobRemindMsgTrackingDto();
                                List<JobRemindMsgTrackingDto> list = IaisCommonUtils.genNewArrayList();
                                createjob.setMsgKey("unprocess officer");
                                createjob.setRefNo(item.getId());
                                createjob.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                                list.add(createjob);
                                systemBeLicClient.createJobRemindMsgTrackingDtos(list);
                            }
                        }else if(days == kpi){
                            //judge is email sent
                            JobRemindMsgTrackingDto jobRemindMsgTrackingDto = systemBeLicClient.getJobRemindMsgTrackingDto(item.getId(),"unprocess leader").getEntity();
                            if(jobRemindMsgTrackingDto == null){
                                //send email to leader and admin
                                List<String> email = IaisCommonUtils.genNewArrayList();
                                email.add(item.getLeaderEmailAddr());
                                sendEmail(item,applicationDto,email);
                                //record email sent
                                JobRemindMsgTrackingDto createjob = new JobRemindMsgTrackingDto();
                                List<JobRemindMsgTrackingDto> list = IaisCommonUtils.genNewArrayList();
                                createjob.setMsgKey("unprocess leader");
                                createjob.setRefNo(item.getId());
                                createjob.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                                list.add(createjob);
                                systemBeLicClient.createJobRemindMsgTrackingDtos(list);
                            }
                        }else if(days == sysday){
                            //judge is email sent
                            JobRemindMsgTrackingDto jobRemindMsgTrackingDto = systemBeLicClient.getJobRemindMsgTrackingDto(item.getId(),"unprocess leader").getEntity();
                            if(jobRemindMsgTrackingDto == null){
                                //send email to leader and admin
                                List<String> email = IaisCommonUtils.genNewArrayList();
                                List<OrgUserDto> systemAdmin = organizationClient.retrieveOrgUserAccountByRoleId(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN).getEntity();
                                if(!IaisCommonUtils.isEmpty(systemAdmin)){
                                    for (OrgUserDto orgUserDto:systemAdmin
                                         ) {
                                        email.add(orgUserDto.getEmail());
                                    }
                                    sendEmail(item,applicationDto,email);
                                    //record email sent
                                    JobRemindMsgTrackingDto createjob = new JobRemindMsgTrackingDto();
                                    List<JobRemindMsgTrackingDto> list = IaisCommonUtils.genNewArrayList();
                                    createjob.setMsgKey("unprocess SystemAdmin");
                                    createjob.setRefNo(item.getId());
                                    createjob.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                                    list.add(createjob);
                                    systemBeLicClient.createJobRemindMsgTrackingDtos(list);
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

    private void sendEmail(TaskEmailDto item, ApplicationDto application,List<String> emailAddr){
        EmailDto email = new EmailDto();
        switch (application.getApplicationType()){
            case ApplicationConsts.APPLICATION_TYPE_APPEAL:
                email.setSubject("Appeal Unprocessed Task Notification");
                break;
            case ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE:
                email.setSubject("RFC Unprocessed Task Notification");
                break;
            default:
                email.setSubject("Unprocessed Task Notification");
                break;

        }
        email.setReqRefNum(item.getId());
        email.setContent(application.getApplicationNo());
        email.setSender(mailSender);
        email.setClientQueryCode(item.getId());
        email.setReceipts(emailAddr);
        emailClient.sendNotification(email).getEntity();
    }
}
