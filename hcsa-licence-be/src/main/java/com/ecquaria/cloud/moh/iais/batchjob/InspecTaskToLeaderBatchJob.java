package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Shicheng
 * @date 2020/2/8 11:22
 **/
@Delegator("inspecTaskToLeaderBatchJob")
@Slf4j
public class InspecTaskToLeaderBatchJob {

    /**
     * StartStep: inspTaskToLeaderStart
     *
     * @param bpc
     * @throws
     */
    public void inspTaskToLeaderStart(BaseProcessClass bpc){
        logAbout("Inspection Create Task To Leader");
    }

    /**
     * StartStep: inspTaskToLeaderJob
     *
     * @param bpc
     * @throws
     */
    public void inspTaskToLeaderJob(BaseProcessClass bpc){
        logAbout("inspTaskToLeaderJob");

    }

    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }
}
