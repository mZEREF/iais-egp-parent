package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * EventBusCallBackDelegator
 *
 * @author suocheng
 * @date 12/3/2019
 */
@Delegator(value = "eventBusCallBackDelegator")
@Slf4j
public class EventBusCallBackDelegator {
    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void callBack(BaseProcessClass bpc) {

    }
}
