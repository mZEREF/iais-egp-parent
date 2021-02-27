package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.security.AuthenticationConfig;
import com.ecquaria.cloud.usersession.UserSession;
import com.ecquaria.cloud.usersession.UserSessionUtil;
import com.ecquaria.cloud.usersession.client.UserSessionService;
import com.ecquaria.cloudfeign.FeignException;
import ecq.commons.exception.BaseException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

@Slf4j
public final class FeLoginHelper {
    public static final String INBOX_URL = "/main-web/eservice/INTERNET/MohInternetInbox";
    public static final String MAIN_WEB_URL = "/main-web/";
    public static final String CORPPASS_URL = "/main-web/eservice/INTERNET/FE_Landing/1/croppass";

    public static void initUserInfo(HttpServletRequest request, User user) throws FeignException, BaseException {
        user.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
        String conInfo = AuthenticationConfig.getConcurrentUserSession();
        if (AuthenticationConfig.VALUE_CONCURRENT_USER_SESSION_CLOSE_OLD.equals(conInfo)) {
            List<UserSession> usesses = UserSessionService.getInstance()
                    .retrieveActiveSessionByUserDomainAndUserId(user.getUserDomain(), user.getId());
            if (usesses != null && usesses.size() > 0) {
                for (UserSession us : usesses) {
                    UserSessionUtil.killUserSession(us.getSessionId());
                }
            }
        }
        SessionManager.getInstance(request).imitateLogin(user, true, true);
        SessionManager.getInstance(request).initSopLoginInfo(request);
        AccessUtil.initLoginUserInfo(request);
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        auditTrailDto.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTERNET);
        auditTrailDto.setOperation(AuditTrailConsts.OPERATION_LOGIN);

        int loginType = (int) ParamUtil.getSessionAttr(request, IaisEGPConstant.SESSION_ENTRANCE);
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

    private FeLoginHelper() {
        throw new IllegalStateException("Utility class");
    }
}
