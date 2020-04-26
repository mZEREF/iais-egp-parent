package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Process MohSkipInspAppt
 *
 * @author Shicheng
 * @date 2020/4/26 9:46
 **/
@Delegator("skipInspApptBatchJob")
@Slf4j
public class MohSkipInspApptBatchJob {

    /**
     * StartStep: mohSkipInspApptStart
     *
     * @param bpc
     * @throws
     */
    public void mohSkipInspApptStart(BaseProcessClass bpc){
        logAbout("Skip Online Appointment Inspection Date");
    }

    /**
     * StartStep: mohSkipInspApptStep
     *
     * @param bpc
     * @throws
     */
    public void mohSkipInspApptStep(BaseProcessClass bpc){
        logAbout("Skip Online Appointment Inspection Date");

    }

    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }
}
