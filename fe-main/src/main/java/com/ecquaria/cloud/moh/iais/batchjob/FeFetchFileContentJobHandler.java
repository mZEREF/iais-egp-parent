package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.BatchJobConstant;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.client.FeMainFileRepoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FeMainFileRepoClient
 *
 * @author Jinhua
 * @date 2020/9/23 15:38
 */
@Component
@JobHandler(value="feFetchFileContentJobHandler")
@Slf4j
public class FeFetchFileContentJobHandler extends MohJobHandler {
    @Autowired
    private FeMainFileRepoClient feMainFileRepoClient;

    @Override
    public ReturnT<String> doExecute(String str) throws Exception {
        try {
            AuditTrailHelper.setupBatchJobAuditTrail(AppConsts.DOMAIN_INTRANET, this);
            feMainFileRepoClient.fetchFileContent();
        } catch (Throwable th) {
            log.error(th.getMessage(), th);
            JobLogger.log(th);
            return ReturnT.FAIL;
        }

        return ReturnT.SUCCESS;
    }
}
