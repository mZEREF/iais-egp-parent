package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Wenkang
 * @date 2019/12/25 13:08
 */
@Delegator("autoRenwalDelegator")
@Slf4j
public class AutoRenwalDelegator  {

    public void start(BaseProcessClass bpc){
        log.info("**** the  auto renwal  start ******");


        log.info("**** the  auto renwal  end ******");
    }


    public void prepare(BaseProcessClass bpc){
        log.info("**** the  auto renwal  prepare start  ******");



        log.info("**** the  auto renwal  prepare  end ******");
    }
}

