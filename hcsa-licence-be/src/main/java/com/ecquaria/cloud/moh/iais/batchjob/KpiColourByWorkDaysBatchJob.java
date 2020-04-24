package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Process
 *
 * @author Shicheng
 * @date 2020/4/23 16:45
 **/
@Delegator("kpiColourByWorkDaysBatchJob")
@Slf4j
public class KpiColourByWorkDaysBatchJob {
    /**
     * StartStep: inspecSaveBeRecByFeStart
     *
     * @param bpc
     * @throws
     */
    public void inspecSaveBeRecByFeStart(BaseProcessClass bpc){
        logAbout("MohKpiColourShow");
    }

    /**
     * StartStep: inspecSaveBeRecByFePre
     *
     * @param bpc
     * @throws
     */
    public void inspecSaveBeRecByFePre(BaseProcessClass bpc){
        logAbout("MohKpiColourShow");

    }

    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }
}
