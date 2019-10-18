package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * OrgUserManageDelegate
 *
 * @author Jinhua
 * @date 2019/10/18 15:07
 */
@Delegator("orgUserManageDelegate")
@Slf4j
public class OrgUserManageDelegate {

    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction("Front End Admin", "User Management");
    }

    /**
     * AutoStep: PrePage
     *
     * @param bpc
     * @throws
     */
    public void preparePage(BaseProcessClass bpc) {

    }
}
