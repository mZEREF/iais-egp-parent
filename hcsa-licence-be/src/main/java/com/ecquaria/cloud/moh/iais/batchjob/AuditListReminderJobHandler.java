package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



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

    @Override
    public ReturnT<String> execute(String s) {
        logAbout("AuditListReminderJob");
        try{
            auditSystemListService.sendMailForAuditPlaner(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_LIST_REMIND);
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

}
