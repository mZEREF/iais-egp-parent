package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * LicenceManagementDelegator
 *
 * @author junyu
 * @date 2020/5/12
 */
@Delegator(value = "licenceManagementDelegator")
@Slf4j
public class LicenceManagementDelegator {
    public void start(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request,"isCease",1);
    }
}
