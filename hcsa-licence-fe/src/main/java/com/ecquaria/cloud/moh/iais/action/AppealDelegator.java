package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Wenkang
 * @date 2020/2/4 12:46
 */
@Delegator("appealDelegator")
@Slf4j
public class AppealDelegator {

    public void preparetionData(BaseProcessClass bpc){
        log.info("start**************preparetionData************");


        log.info("end**************preparetionData************");
    }


    public void switchProcess(BaseProcessClass bpc ){
        log.info("start**************switchProcess************");

        log.info("end**************switchProcess************");
    }


    public void submit(BaseProcessClass bpc){
        log.info("start**************submit************");

        log.info("end**************submit************");
    }

    public void start(BaseProcessClass bpc){
        log.info("start**************start************");

        log.info("end**************start************");
    }
}
