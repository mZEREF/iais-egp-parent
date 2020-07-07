package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;


/**
 * @Process AuditListReminderJob
 *
 * @author wangyu
 * @date 2020/6/23 16:45
 **/
@JobHandler(value="auditListReminderJobHandler")
@Component
@Slf4j
public class AuditListReminderJobHandler extends IJobHandler {

    @Autowired
    private AuditSystemListService auditSystemListService;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private SystemBeLicClient systemBeLicClient;
    @Override
    public ReturnT<String> execute(String s) {
        logAbout("AuditListReminderJob");
        try{
             if(isSendEmail()){
                 auditSystemListService.sendMailForAuditPlaner(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_LIST_REMIND);
                 saveJobRemindMsgTrackingDto();
             }else {
                 JobLogger.log("AuditListReminderJob is not send email time");
             }

        }catch (Exception e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }

    private void logAbout(String methodName){
        log.info(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
        JobLogger.log(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }

    private boolean isSendEmail(){
        Calendar calendar =  Calendar.getInstance();
        calendar.setTime(new Date());
        int dateOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        // dateOfMonth = 0
        if( dateOfMonth >-1){
            int auditInspectorListReminderRate = systemParamConfig.getAuditInspectorListReminderRate();
            JobRemindMsgTrackingDto jobRemindMsgTrackingDto = systemBeLicClient.getJobRemindMsgTrackingDtoByMsgAAndCreatedAt(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_LIST_REMIND,"AuditListReminderJob").getEntity();
            if(jobRemindMsgTrackingDto == null ){
               return true;
            }
            Calendar calendar2 =  Calendar.getInstance();
            calendar2.setTime(jobRemindMsgTrackingDto.getCreateTime());
            calendar2.add(Calendar.MONTH,auditInspectorListReminderRate);
            if(IaisEGPHelper.getCompareDate(calendar2.getTime(),calendar.getTime()) == 1){
               return true;
            }
        }
        return false;
    }

    private void saveJobRemindMsgTrackingDto(){
        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        JobRemindMsgTrackingDto jobRemindMsgTrackingDto = new JobRemindMsgTrackingDto();
        jobRemindMsgTrackingDto.setRefNo(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_LIST_REMIND);
        jobRemindMsgTrackingDto.setMsgKey("AuditListReminderJob");
        jobRemindMsgTrackingDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        jobRemindMsgTrackingDto.setAuditTrailDto(intranet);
        systemBeLicClient.updateJobRemindMsgTrackingDto(jobRemindMsgTrackingDto);
    }
}
