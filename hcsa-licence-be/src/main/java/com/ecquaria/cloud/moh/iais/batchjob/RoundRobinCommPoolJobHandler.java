package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RoundRobinCommPoolJobHandler
 *
 * @author suocheng
 * @date 9/23/2020
 */
@JobHandler(value="roundRobinCommPoolJobHandler")
@Component
@Slf4j
public class RoundRobinCommPoolJobHandler extends MohJobHandler {

    @Autowired
    private RoundRobinCommPoolBatchJob roundRobinCommPoolBatchJob;

    @Override
    public ReturnT<String> doExecute(String var1) throws Exception {
        try {
            roundRobinCommPoolBatchJob.jobExecute();
            return  ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }
}
