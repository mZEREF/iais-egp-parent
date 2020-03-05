package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;


/**
 * ConfigureDelegator
 *
 * @author Guyin
 * @date 2019/11/26 14:07
 */

@Delegator("defineUnavailbilityDelegate")
@Slf4j
public class defineUnavailbilityDelegate {

    @Autowired
    UserRoleService userRoleService;
    public void doStart(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction("defineUnavailbilityDelegate", "defineUnavailbilityDelegate");

    }

    /**
     * AutoStep: parpare
     *
     * @param bpc
     * @throws
     */
    public void parpare(BaseProcessClass bpc){
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        String ava = userRoleService.getAvailable(userId);
        ParamUtil.setRequestAttr(bpc.request, "ava", ava);
    }

    /**
     * AutoStep: save
     *
     * @param bpc
     * @throws
     */
    public void save(BaseProcessClass bpc){
        String[] ava =  ParamUtil.getStrings(bpc.request, "ava");
        String res = "1";
        if(ava == null){
            res = "0";
        }
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
         userRoleService.setAvailable(loginContext.getUserId(),res);
    }



}
