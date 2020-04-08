package com.ecquaria.cloud.moh.iais.helper;

import lombok.extern.slf4j.Slf4j;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public final class LoginHelper {
    private static final String INBOX_URL = "/main-web/eservice/INTERNET/MohInternetInbox";

    private LoginHelper(){}

    public static void login(HttpServletRequest request, HttpServletResponse response, User user){
        SessionManager.getInstance(request).imitateLogin(user, true, true);
        SessionManager.getInstance(request).initSopLoginInfo(request);

        AccessUtil.initLoginUserInfo(request);

        IaisEGPHelper.sendRedirect(request, response, INBOX_URL);
    }

}
