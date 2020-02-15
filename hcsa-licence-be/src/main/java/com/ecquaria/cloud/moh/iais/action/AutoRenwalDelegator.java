package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.service.AutoRenwalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Wenkang
 * @date 2019/12/25 13:08
 */
@Delegator("autoOrNonAutoRenwalDelegator")
@Slf4j
public class AutoRenwalDelegator {

    @Autowired
    private AutoRenwalService autoRenwalService;

    public void start(BaseProcessClass bpc){
        log.info("**** the  auto renwal  start ******");


        log.info("**** the  auto renwal  end ******");
    }


    public void prepare(BaseProcessClass bpc){
        log.info("**** the  auto renwal  prepare start  ******");

        autoRenwalService.startRenwal(bpc.request);

        log.info("**** the  auto renwal  prepare  end ******");
    }


}

