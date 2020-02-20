package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * CessationDelegator
 *
 * @author caijing
 * @date 2020/2/20
 */

@Delegator("cessationDelegator")
@Slf4j
public class CessationDelegator {

    public void start(BaseProcessClass bpc){
        log.info("**** the  approve start ******");


        log.info("**** the  auto renwal  end ******");
    }
}
