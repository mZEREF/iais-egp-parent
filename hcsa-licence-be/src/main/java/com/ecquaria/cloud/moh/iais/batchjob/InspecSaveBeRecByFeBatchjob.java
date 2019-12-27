package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Shicheng
 * @date 2019/12/27 9:52
 **/
@Delegator("inspecSaveBeRecByFeDelegator")
@Slf4j
public class InspecSaveBeRecByFeBatchjob {
    /**
     * StartStep: inspecSaveBeRecByFeStart
     *
     * @param bpc
     * @throws
     */
    public void inspecSaveBeRecByFeStart(BaseProcessClass bpc){
        logAbout("Be Create Rec File");
    }

    /**
     * StartStep: inspecSaveBeRecByFePre
     *
     * @param bpc
     * @throws
     */
    public void inspecSaveBeRecByFePre(BaseProcessClass bpc){
        logAbout("inspecSaveBeRecByFePre");
    }

    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }
}
