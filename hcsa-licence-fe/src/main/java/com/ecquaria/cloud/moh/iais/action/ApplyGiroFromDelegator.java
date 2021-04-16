package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * ApplyGiroFromDelegateo
 *
 * @author junyu
 * @date 2021/3/8
 */
@Delegator("applyGiroFromDelegator")
@Slf4j
public class ApplyGiroFromDelegator {
    public void start(BaseProcessClass bpc){
        ParamUtil.setRequestAttr(bpc.request,"DashboardTitle","Apply for GIRO");
    }
}
