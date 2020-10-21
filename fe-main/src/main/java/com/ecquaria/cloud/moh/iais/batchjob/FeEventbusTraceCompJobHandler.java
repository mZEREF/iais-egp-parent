package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.client.EventBusClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FrontendEventbusTraceCompJobHandler
 *
 * @author Jinhua
 * @date 2020/9/23 10:52
 */
@Component
@JobHandler(value="feEventbusTraceCompJobHandler")
@Slf4j
public class FeEventbusTraceCompJobHandler extends MohJobHandler {
    @Autowired
    private EventBusClient eventBusClient;

    @Override
    public ReturnT<String> doExecute(String str) throws Exception {
        try {
            AuditTrailHelper.setupBatchJobAuditTrail(AppConsts.DOMAIN_INTRANET, this);
            eventBusClient.trackCompersation();
        } catch (Throwable th) {
            log.error(th.getMessage(), th);
            JobLogger.log(th);
            return ReturnT.FAIL;
        }

        return ReturnT.SUCCESS;
    }

}
