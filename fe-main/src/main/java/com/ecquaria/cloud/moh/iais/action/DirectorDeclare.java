package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeAdminDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * FeAdminManageDelegate
 *
 * @author guyin
 * @date 2019/10/18 15:07
 */
@Delegator("directorDeclare")
@Slf4j
public class DirectorDeclare {
    public static final String ORG_USER_DTO_ATTR                   = "orgUserDto";

    @Autowired
    private OrgUserManageService orgUserManageService;

    FeAdminDto userDto = new FeAdminDto();

    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_USER_MANAGEMENT,
                AuditTrailConsts.FUNCTION_DIRECTOR_DECLARE);
        log.debug("****doStart Process ****");
    }

    /**
     * AutoStep: preparePage
     *
     * @param bpc
     * @throws
     */
    public void preparePage(BaseProcessClass bpc) {
        log.debug("****preparePage Process ****");

    }

    /**
     * AutoStep: InsertDatabase
     *
     * @param bpc
     * @throws
     */
    public void success(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("*******************insertDatabase end"));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        OrgUserRoleDto orgUserRoleDto = new OrgUserRoleDto();
        orgUserRoleDto.setUserAccId(userId);
        orgUserRoleDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        orgUserRoleDto.setRoleName(RoleConsts.USER_ROLE_ORG_DIRECTOR);
        orgUserManageService.addUserRole(orgUserRoleDto);
    }


}
