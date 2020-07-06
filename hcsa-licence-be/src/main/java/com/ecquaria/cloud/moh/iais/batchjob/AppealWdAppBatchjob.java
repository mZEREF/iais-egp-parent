package com.ecquaria.cloud.moh.iais.batchjob;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

@Delegator("approvewithdrawalDelegator")
@Slf4j
public class AppealWdAppBatchjob {

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private ApplicationService applicationService;


    public void startStep(BaseProcessClass bpc) {

    }

    public void approveWithdrawalStep(BaseProcessClass bpc) {
        List<ApplicationDto> applicationDtoList = applicationClient.saveWithdrawn().getEntity();
        applicationDtoList.forEach(h -> {
            applicationService.updateFEApplicaiton(h);
        });
    }
}
