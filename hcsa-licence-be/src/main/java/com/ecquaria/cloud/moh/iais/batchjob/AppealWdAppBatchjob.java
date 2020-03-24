package com.ecquaria.cloud.moh.iais.batchjob;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

@Delegator("approvewithdrawalDelegator")
@Slf4j
public class AppealWdAppBatchjob {

    @Autowired
    private ApplicationClient applicationClient;

    public void startStep(BaseProcessClass bpc) {

    }

    public void approveWithdrawalStep(BaseProcessClass bpc) {
        applicationClient.saveWithdrawn();
    }
}
