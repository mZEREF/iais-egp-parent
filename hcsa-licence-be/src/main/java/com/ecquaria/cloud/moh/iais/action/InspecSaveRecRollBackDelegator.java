package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Shicheng
 * @date 2019/12/31 17:19
 **/
@Delegator("inspecSaveRecRollBackDelegator")
@Slf4j
public class InspecSaveRecRollBackDelegator {

    /**
     * StartStep: inspecSaveRecRollBackStart
     *
     * @param bpc
     * @throws
     */
    public void inspecSaveRecRollBackStart(BaseProcessClass bpc){
        logAbout("Be Save Rectification Data Roll Back!");
    }

    /**
     * StartStep: inspecSaveRecRollBackAction
     *
     * @param bpc
     * @throws
     */
    public void inspecSaveRecRollBackAction(BaseProcessClass bpc){
        logAbout("inspecSaveRecRollBackAction");

    }

    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }
}
