package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.batchjob.HalpActiveMcJobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

@Delegator(value = "halpActiveMcBatchJob")
@Slf4j
public class HalpActiveMcDelegator {

    @Autowired
    HalpActiveMcJobHandler halpActiveMcJobHandler;

    public void updateMsStep(BaseProcessClass bpc){
    }
}
