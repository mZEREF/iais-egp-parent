package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@JobHandler(value = "SendIncompleteCycleMsgJobHandler")
@Component
@Slf4j
public class SendIncompleteCycleMsgJobHandler extends IJobHandler {
    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            JobLogger.log(StringUtil.changeForLog("SendIncompleteCycleMsgJobHandler start ..."));
            log.info("-----SendIncompleteCycleMsgJobHandler start ------");
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            arDataSubmissionService.sendIncompleteCycleNotificationPeriod();
            JobLogger.log(StringUtil.changeForLog("SendIncompleteCycleMsgJobHandler end ..."));
            log.info("-----SendIncompleteCycleMsgJobHandler end ------");
            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }
}
