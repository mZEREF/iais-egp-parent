package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * AppealApprovejobHandler
 *
 * @author suocheng
 * @date 9/23/2020
 */
@JobHandler(value="appealApprovejobHandler")
@Component
@Slf4j
public class AppealApprovejobHandler extends MohJobHandler{

    @Autowired
    private AppealApproveBatchjob appealApproveBatchjob;

    @Override
    public ReturnT<String> doExecute(String var1) throws Exception {
        try {
            AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET, this);
            appealApproveBatchjob.jobExecute();
            return  ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }
}
