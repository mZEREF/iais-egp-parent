package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * KpiColourByWorkDaysDelegate
 *
 * @author Jinhua
 * @date 2020/6/22 9:59
 */
@Delegator("kpiColourByWorkDaysBatchJob")
@Slf4j
public class KpiColourByWorkDaysDelegate {

    @Autowired
    private KpiColourByWorkDaysJobHandler kpiColourByWorkDaysBatchJob;

    /**
     * StartStep: mohKpiColourShowStart
     *
     * @param bpc
     * @throws
     */
    public void mohKpiColourShowStart(BaseProcessClass bpc){

    }

    /**
     * StartStep: mohKpiColourShowStep
     *
     * @param bpc
     * @throws
     */
    public void mohKpiColourShowStep(BaseProcessClass bpc) throws Exception {
        kpiColourByWorkDaysBatchJob.execute("");
    }

}
