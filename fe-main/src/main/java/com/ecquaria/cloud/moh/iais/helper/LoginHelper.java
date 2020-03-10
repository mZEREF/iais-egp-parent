package com.ecquaria.cloud.moh.iais.helper;

import lombok.extern.slf4j.Slf4j;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public final class LoginHelper {

    private LoginHelper(){}

    public static void login(HttpServletRequest request, HttpServletResponse response, User user, String utl){
        SessionManager.getInstance(request).imitateLogin(user, true, true);
        SessionManager.getInstance(request).initSopLoginInfo(request);
        IaisEGPHelper.sendRedirect(request, response, utl);
    }

}
