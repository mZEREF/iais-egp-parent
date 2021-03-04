package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * FeeAndPaymentGIROPayeeDelegator
 *
 * @author junyu
 * @date 2021/3/2
 */
@Delegator(value = "feeAndPaymentGIROPayeeDelegator")
@Slf4j
public class FeeAndPaymentGIROPayeeDelegator {

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>requestForInformation");
        HttpServletRequest request=bpc.request;
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_PAYMENT,  AuditTrailConsts.FUNCTION_ONLINE_PAYMENT);
    }
    public void info(BaseProcessClass bpc) {

    }
    public void prePayeeResult(BaseProcessClass bpc) {

    }
    public void deletePayee(BaseProcessClass bpc) {

    }
    public void reSearchPayee(BaseProcessClass bpc) {

    }
    public void preOrgResult(BaseProcessClass bpc) {

    }
    public void reSearchOrg(BaseProcessClass bpc) {

    }
    public void doSelect(BaseProcessClass bpc) {

    }
    public void doBack(BaseProcessClass bpc) {

    }
    public void refill(BaseProcessClass bpc) {

    }
    public void doValidate(BaseProcessClass bpc) {

    }
    public void preView(BaseProcessClass bpc) {

    }
    public void doSubmit(BaseProcessClass bpc) {

    }
}
