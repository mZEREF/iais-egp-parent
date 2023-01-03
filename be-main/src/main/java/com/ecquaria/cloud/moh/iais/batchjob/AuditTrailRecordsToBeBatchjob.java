package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.AuditTrailRecordsToBeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * SyncFEAuditTrailBatchjob
 *
 * @author junyu
 * @date 2020/4/16
 */
@Delegator("auditTrailRecordsToBeBatchjob")
@Slf4j
public class AuditTrailRecordsToBeBatchjob {
    @Autowired
    private AuditTrailRecordsToBeService auditTrailRecordsToBeService;

    public void start(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("The auditTrailRecordsToBeBatchjob start..." ));
    }

    public void preDate(BaseProcessClass bpc)  {
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        log.info(StringUtil.changeForLog("The auditTrailRecordsToBeBatchjob preDate bpc is :" + bpc ));
        log.debug(StringUtil.changeForLog("The auditTrailRecordsToBeBatchjob is start..." ));

        auditTrailRecordsToBeService.info();
        auditTrailRecordsToBeService.compress();
        log.info("------------------- compressFile  end --------------");

    }
}
