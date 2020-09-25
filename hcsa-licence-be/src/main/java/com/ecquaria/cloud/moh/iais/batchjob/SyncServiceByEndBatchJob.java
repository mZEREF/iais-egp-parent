package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Shicheng
 * @date 2020/9/25 9:45
 **/
@Delegator("syncServiceByEndBatchJob")
@Slf4j
public class SyncServiceByEndBatchJob {

    /**
     * StartStep: syncServiceByEndStart
     *
     * @param bpc
     * @throws
     */
    public void syncServiceByEndStart(BaseProcessClass bpc){
        logAbout("Synchronize Service Status By End Date");
    }

    /**
     * StartStep: syncServiceByEndDo
     *
     * @param bpc
     * @throws
     */
    public void syncServiceByEndDo(BaseProcessClass bpc){

    }

    private void logAbout(String methodName){
        log.info(StringUtil.changeForLog("****The****" + methodName + "*****Start****"));
    }
}
