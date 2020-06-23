package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.AuditSystemPotitalListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Override
    public ReturnT<String> execute(String s) {
        logAbout("AuditTcuListReminderJob");
        try{
              if(isHaveTcuAudited(auditSystemPotitalListService.getSystemPotentailAdultList())){
                  auditSystemListService.sendMailForAuditPlaner(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_TCU_REMIND);
              }else {
                  JobLogger.log("AuditTcuListReminderJob is no tcu task");
              }
        }catch (Exception e){
            JobLogger.log(e.getMessage());
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

}
