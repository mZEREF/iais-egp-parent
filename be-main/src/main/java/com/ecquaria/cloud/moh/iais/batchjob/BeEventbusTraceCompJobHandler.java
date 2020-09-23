package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.service.client.EventBusClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * BeEventbusTraceCompJobHandler
 *
 * @author Jinhua
 * @date 2020/9/23 13:59
 */
@Component
@JobHandler(value="beEventbusTraceCompJobHandler")
@Slf4j
public class BeEventbusTraceCompJobHandler extends MohJobHandler {
    @Autowired
    private EventBusClient eventBusClient;

    @Override
    public ReturnT<String> doExecute(String str) throws Exception {
        try {
            eventBusClient.trackCompersation();
        } catch (Throwable th) {
            log.error(th.getMessage(), th);
            JobLogger.log(th);
            return ReturnT.FAIL;
        }

        return ReturnT.SUCCESS;
    }

}
