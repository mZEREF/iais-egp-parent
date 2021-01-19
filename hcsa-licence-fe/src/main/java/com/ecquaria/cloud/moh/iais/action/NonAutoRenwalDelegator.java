package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Wenkang
 * @date 2019/12/25 13:11
 */
@Delegator("nonAutoRenwalDelegator")
@Slf4j
public class NonAutoRenwalDelegator  {


    public void start(BaseProcessClass bpc){
    log.info("**** the non auto renwal  start ******");


    log.info("**** the non auto renwal  end ******");
    }


    public void prepare(BaseProcessClass bpc){
        log.info("**** the non auto renwal  prepare start  ******");


        log.info("**** the non auto renwal  prepare  end ******");
    }

}
