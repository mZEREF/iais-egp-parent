package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.batchjob.NotificationCycleOvertime;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

@Delegator("notificationCycleDelegator")
public class NotificationCycleDelegator {
    @Autowired
    private NotificationCycleOvertime  noJob;

    public void start(BaseProcessClass bpc) throws Exception{
        noJob.execute("");
    }
}
