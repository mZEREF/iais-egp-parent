package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.client.BeMainFileRepoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * BeFetchFileContentJobHandler
 *
 * @author Jinhua
 * @date 2020/9/23 14:43
 */
@Component
@JobHandler(value="beFetchFileContentJobHandler")
@Slf4j
public class BeFetchFileContentJobHandler extends MohJobHandler {
    @Autowired
    private BeMainFileRepoClient beMainFileRepoClient;

    @Override
    public ReturnT<String> doExecute(String str) throws Exception {
        try {
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            beMainFileRepoClient.fetchFileContent();
        } catch (RuntimeException th) {
            log.error(th.getMessage(), th);
            JobLogger.log(th);
            return ReturnT.FAIL;
        }

        return ReturnT.SUCCESS;
    }
}
