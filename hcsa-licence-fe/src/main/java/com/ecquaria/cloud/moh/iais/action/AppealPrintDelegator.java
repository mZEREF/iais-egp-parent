package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Wenkang
 * @date 2021/4/23 16:17
 */
@Delegator("mohAppealPrint")
@Slf4j
public class AppealPrintDelegator {

    public void prepareData(BaseProcessClass bpc){
        log.info("------>prepareData start<------");
        bpc.request.setAttribute("crud_action_type","appeal");
    }
    public void start(BaseProcessClass bpc){
        log.info("------>mohAppealPrint start<------");
    }
}
