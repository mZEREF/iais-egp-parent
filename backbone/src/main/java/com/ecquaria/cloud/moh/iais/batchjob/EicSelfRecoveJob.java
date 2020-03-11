package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * RestBridgeSelfRecoveJob
 *
 * @author Jinhua
 * @date 2019/12/28 14:11
 */
@Delegator("eicSelfRecoveDelegator")
@Slf4j
public class EicSelfRecoveJob {
    public void selfRecover(BaseProcessClass bpc) {

    }
}
