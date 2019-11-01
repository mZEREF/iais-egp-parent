package com.ecquaria.cloud.moh.iais.controller;

/*
 *author: yichen
 *date time:9/25/2019 2:22 PM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

@Delegator(value = "hcsaChklDelegator")
@Slf4j
public class HcsaChklDelegator {

    /**
     * StartStep: startStep
     * @param bpc
     * @throws IllegalAccessException
     */
    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>HcsaChklDelegator");
    }

    /**
     * @AutoStep: prepareSwitch
     * @param:
     * @return:
     * @author: yichen
     */
    public void prepareSwitch(BaseProcessClass bpc) throws IllegalAccessException{
        log.debug("The prepareSwitch start ...");
        String crudAction = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.debug("*******************crudAction-->:" + crudAction);
        log.debug("The prepareSwitch end ...");
    }

}
