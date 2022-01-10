package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.UserConstants;
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

    public static void initUserInfo(HttpServletRequest request, FeUserDto mohUser) throws FeignException, BaseException {
        User user = new User();
        user.setDisplayName(mohUser.getDisplayName());
        user.setUserDomain(mohUser.getUserDomain());
        user.setId(mohUser.getUserId());
        user.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
        user.setIdentityNo(mohUser.getIdentityNo());

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

    public static void writeUserField(HttpServletRequest request, FeUserDto userSession) {
        String name = ParamUtil.getString(request, UserConstants.NAME);
        String salutation = ParamUtil.getString(request, UserConstants.SALUTATION);
        String designation = ParamUtil.getString(request, UserConstants.DESIGNATION);
        String designationOther = ParamUtil.getString(request, UserConstants.DESIGNATION_OTHER);
        String idNo = StringUtil.isEmpty(userSession.getIdentityNo()) ? ParamUtil.getString(request, UserConstants.ID_NUMBER) : userSession.getIdentityNo();
        String mobileNo = ParamUtil.getString(request, UserConstants.MOBILE_NO);
        String officeNo = ParamUtil.getString(request, UserConstants.OFFICE_NO);
        String email = ParamUtil.getString(request, UserConstants.EMAIL);
        userSession.setDisplayName(name);
        userSession.setDesignation(designation);
        userSession.setDesignationOther(designationOther);
        userSession.setSalutation(salutation);
        userSession.setIdentityNo(idNo);
        userSession.setMobileNo(mobileNo);
        userSession.setOfficeTelNo(officeNo);
        userSession.setIdType(IaisEGPHelper.checkIdentityNoType(idNo));
        userSession.setEmail(email);
    }

    private FeLoginHelper() {
        throw new IllegalStateException("Utility class");
    }
}
