package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.action.UploadDelegator;

import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Wenkang
 * @date 2020/7/21 10:10
 */
@JobHandler(value = "uploadDelegatorJob")
@Component
@Slf4j
public class UploadDelegatorJob extends IJobHandler {
    @Autowired
    private UploadDelegator uploadDelegator;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        try {
            //get all data of need Carry from DB
             uploadDelegator.jobStart();
        }catch (Exception e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
}
