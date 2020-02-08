package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author weilu
 * @date 2020/2/7 13:12
 */
@Delegator("CessationApplication")
@Slf4j
public class CessationApplicationDelegator {

    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>CessationApplicationDelegator");
    }

    public void init(BaseProcessClass bpc){
        String appNo = "";


    }

    public void prepareData(BaseProcessClass bpc){

    }

    public void valiant(BaseProcessClass bpc){

    }

    public void action(BaseProcessClass bpc){

    }

}
