package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.AppGroupMiscService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * NotificationApplicationUpdateBatchjob
 *
 * @author suocheng
 * @date 5/13/2020
 */
@Delegator("notificationApplicationUpdateBatchjob")
@Slf4j
public class NotificationApplicationUpdateBatchjob {
    @Autowired
    private AppGroupMiscService appGroupMiscService;


    public void doBatchJob(BaseProcessClass bpc) {
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        jobExecute();
    }
    public void jobExecute(){
        log.info(StringUtil.changeForLog("The NotificationApplicationUpdateBatchjob start ..."));
        appGroupMiscService.notificationApplicationUpdateBatchjob();
        log.info(StringUtil.changeForLog("The NotificationApplicationUpdateBatchjob end ..."));

    }

}
