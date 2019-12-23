package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Wenkang
 * @date 2019/12/23 15:40
 */
@Delegator(value = "createRoleDelegator")
@Slf4j
public class CreateRoleDelegator {

    public void prepareData(BaseProcessClass bpc){
        log.info(" the  ****   role   ***   ");

        log.info("the ** *   role   ***   end  ");
    }

    public void  createRole(BaseProcessClass bpc){
    log.info("*****   create   ***  role   start ");


    }

}
