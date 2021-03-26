package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.PatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * PatchDataJobHandler
 *
 * @author Jinhua
 * @date 2021/3/26 15:33
 */
@Component
@JobHandler(value="patchDataJobHandler")
@Slf4j
public class PatchDataJobHandler extends IJobHandler {
    @Autowired
    private PatchService patchService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            JobLogger.log("<=== Start to patch ===>");
            log.info("<=== Start to patch ===>");
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            patchService.patchWorkingGrpWithRole();
            JobLogger.log("<=== End to patch ===>");
            log.info("<=== End to patch ===>");
        } catch (Throwable th) {
            log.error(th.getMessage(), th);
            JobLogger.log(th);
            return ReturnT.FAIL;
        }

        return ReturnT.SUCCESS;
    }
}
