package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@JobHandler(value="updateDsCenterStatusBatchJob")
@Slf4j
public class UpdateDsCenterStatusBatchJob extends MohJobHandler{
    @Autowired
    private LicenceInboxClient licenceClient;

    @Override
    public ReturnT<String> doExecute(String var1) throws Exception {
        try {
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            licenceClient.updateBeDsCenterStatus();
        } catch (Throwable th) {
            log.error(th.getMessage(), th);
            JobLogger.log(th);
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
}
