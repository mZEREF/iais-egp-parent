package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.OrgUserDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * OrgUserManageDelegate
 *
 * @author Jinhua
 * @date 2019/10/18 15:07
 */
@Delegator("orgUserManageDelegate")
@Slf4j
public class OrgUserManageDelegate {
    public static final String ORG_USER_DTO_ATTR                   = "orgUserDto";

    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("Front End Admin", "User Management");
        ParamUtil.setSessionAttr(request, ORG_USER_DTO_ATTR, new OrgUserDto());
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
