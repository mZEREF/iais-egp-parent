package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.client.AppEicClient;
import com.ecquaria.cloud.moh.iais.client.AtEicClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * EicRecoverJob
 *
 * @author Jinhua
 * @date 2020/4/17 9:41
 */
@Slf4j
@Delegator("eicSelfRecoveDelegator")
public class EicSelfRecoveDelegator {
    @Autowired
    private AtEicClient atEicClient;
    @Autowired
    private AppEicClient appEicClient;

    public void selfRecover(BaseProcessClass bpc) {
        log.info("<======== Start EIC Self Recover Job =========>");

        log.info("<======== End EIC Self Recover Job =========>");
    }
}
