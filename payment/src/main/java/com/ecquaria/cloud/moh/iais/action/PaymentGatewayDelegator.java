package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * PaymentGatewayDelegator
 *
 * @author suocheng
 * @date 2/3/2020
 */
@Delegator(value = "paymentGatewayDelegator")
@Slf4j
public class PaymentGatewayDelegator {
    public void  initPayment (BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("The initPayment start ...."));

        log.info(StringUtil.changeForLog("The initPayment end ...."));
    }
}
