package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

@Delegator("approvewithdrawalDelegator")
@Slf4j
public class ApproveWdAppBatchJob {


    @Autowired
    private AppealWdAppBatchjobHandler appealWdAppBatchjobHandler;


    public void startStep(BaseProcessClass bpc) {

    }

    public void approveWithdrawalStep(BaseProcessClass bpc) {
        try {
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            appealWdAppBatchjobHandler.jobRun();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
        }
    }

}
