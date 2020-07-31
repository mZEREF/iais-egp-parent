package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.AuditSystemPotitalListService;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @Process AuditListReminderJob
 *
 * @author wangyu
 * @date 2020/6/23 16:45
 **/
@JobHandler(value="auditTcuListReminderJobHandler")
@Component
@Slf4j
public class AuditTcuListReminderJobHandler extends IJobHandler {

    @Autowired
    private AuditSystemListService auditSystemListService;
    @Autowired
    AuditSystemPotitalListService auditSystemPotitalListService;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private SystemBeLicClient systemBeLicClient;
    @Override
    public ReturnT<String> execute(String s) {
        logAbout("AuditTcuListReminderJob");
        try{
              if(isSendEmail() && isHaveTcuAudited(auditSystemPotitalListService.getSystemPotentailAdultList())){
                  auditSystemListService.sendMailForAuditPlaner(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_TCU_REMIND);
                  saveJobRemindMsgTrackingDto();
              }else {
                  JobLogger.log("AuditTcuListReminderJob is no tcu task or no system send email time");
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

    private boolean isHaveTcuAudited( List<AuditTaskDataFillterDto> auditTaskDataFillterDtos){
        if(IaisCommonUtils.isEmpty(auditTaskDataFillterDtos)){
            return false;
        }
        for(AuditTaskDataFillterDto auditTaskDataFillterDto : auditTaskDataFillterDtos){
            if( !auditTaskDataFillterDto.isAudited()){
              return true;
            }
        }
        return false;
    }
    private boolean isSendEmail(){
        int auditInspectorListReminderWeek = systemParamConfig.getAuditInspectorListTcuReminderWeek();
        Calendar calendar =  Calendar.getInstance();
        calendar.setTime(new Date());
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        if((weekDay -1 == auditInspectorListReminderWeek) || (weekDay == 1 && auditInspectorListReminderWeek ==7)){
            JobRemindMsgTrackingDto jobRemindMsgTrackingDto = systemBeLicClient.getJobRemindMsgTrackingDtoByMsgAAndCreatedAt(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_TCU_REMIND,"AuditTcuListReminderJob").getEntity();
            if(jobRemindMsgTrackingDto == null ){
                return true;
            }
            Calendar calendar2 =  Calendar.getInstance();
            calendar2.setTime(jobRemindMsgTrackingDto.getCreateTime());
            if(IaisEGPHelper.getCompareDate(calendar2.getTime(),calendar.getTime()) != 1){
            return true;
            }
        }
        return false;
    }

    private void saveJobRemindMsgTrackingDto(){
        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        JobRemindMsgTrackingDto jobRemindMsgTrackingDto = new JobRemindMsgTrackingDto();
        jobRemindMsgTrackingDto.setRefNo(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_TCU_REMIND);
        jobRemindMsgTrackingDto.setMsgKey("AuditTcuListReminderJob");
        jobRemindMsgTrackingDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        jobRemindMsgTrackingDto.setAuditTrailDto(intranet);
        systemBeLicClient.updateJobRemindMsgTrackingDto(jobRemindMsgTrackingDto);
    }

}
