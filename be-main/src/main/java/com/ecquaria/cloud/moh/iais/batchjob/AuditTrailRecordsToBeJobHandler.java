package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.AuditTrailRecordsToBeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * AuditTrailRecordsToBeJobHandler
 *
 * @author junyu
 * @date 2020/7/3
 */
@JobHandler(value="auditTrailRecordsToBeJobHandler")
@Component
@Slf4j
public class AuditTrailRecordsToBeJobHandler extends IJobHandler {
    @Autowired
    private AuditTrailRecordsToBeService auditTrailRecordsToBeService;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET, this);
            auditTrailRecordsToBeService.info();
            auditTrailRecordsToBeService.compress();
            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }
}
