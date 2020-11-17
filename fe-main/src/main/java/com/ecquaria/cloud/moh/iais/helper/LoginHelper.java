package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Slf4j
public final class LoginHelper {
    public static final String INBOX_URL = "/main-web/eservice/INTERNET/MohInternetInbox";
    public static final String MAIN_WEB_URL = "/main-web/";
    public static final String CORPPASS_URL = "/main-web/eservice/INTERNET/FE_Landing/1/croppass";

    public static void initUserInfo(HttpServletRequest request, HttpServletResponse response, User user, int loginType){
        user.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
        SessionManager.getInstance(request).imitateLogin(user, true, true);
        SessionManager.getInstance(request).initSopLoginInfo(request);

        AccessUtil.initLoginUserInfo(request);
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        auditTrailDto.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTERNET);
        auditTrailDto.setOperation(AuditTrailConsts.OPERATION_LOGIN);
        auditTrailDto.setLoginType(loginType);
        auditTrailDto.setModule("Internet Login");
        auditTrailDto.setFunctionName("Internet Login");
        auditTrailDto.setLoginTime(new Date());
        IaisEGPHelper.setAuditLoginUserInfo(auditTrailDto);
        AuditTrailHelper.callSaveAuditTrail(auditTrailDto);
    }

    public static String getTestMode(HttpServletRequest request){
        String openTestMode = (String) ParamUtil.getSessionAttr(request, "openTestMode");
        return openTestMode;
    }

    private LoginHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static void insertLoginFailureAuditTrail(String identityNo){
        insertLoginFailureAuditTrail(null, identityNo);
    }

    public static void insertLoginFailureAuditTrail(String uen, String identityNo){
        AuditTrailDto auditTrailDto = new AuditTrailDto();

        if (OrganizationConstants.ID_TYPE_FIN.equals(IaisEGPHelper.checkIdentityNoType(identityNo))){
            //for audit trail page display
            auditTrailDto.setEntityId(identityNo);
        };

        auditTrailDto.setMohUserId(identityNo);
        auditTrailDto.setNricNumber(identityNo);
        auditTrailDto.setModule("login");
        auditTrailDto.setFunctionName("login failure");
        auditTrailDto.setUenId(uen);
        auditTrailDto.setLoginType(StringUtil.isEmpty(uen) ? AuditTrailConsts.LOGIN_TYPE_SING_PASS : AuditTrailConsts.LOGIN_TYPE_CORP_PASS);
        auditTrailDto.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTERNET);
        auditTrailDto.setOperation(AuditTrailConsts.OPERATION_LOGIN_FAIL);
        AuditTrailHelper.callSaveAuditTrail(auditTrailDto);
    }
}
