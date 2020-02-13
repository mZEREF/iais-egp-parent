package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author weilu
 * @date 2020/2/13 16:20
 */
@Delegator("CessationLicenceBatchJob")
@Slf4j
public class CessationLicenceBatchJob {
    public void doBatchJob(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("The CessationLicenceBatchJob is start ..."));

    }
}
