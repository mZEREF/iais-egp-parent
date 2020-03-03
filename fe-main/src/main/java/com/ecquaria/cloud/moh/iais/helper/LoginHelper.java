package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.RedirectUtil;
import lombok.extern.slf4j.Slf4j;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public final class LoginHelper {

    private LoginHelper(){}

    public static void login(HttpServletRequest request, HttpServletResponse response, User user, String utl){
        SessionManager.getInstance(request).imitateLogin(user, true, true);
        SessionManager.getInstance(request).initSopLoginInfo(request);
        sendRedirect(request, response, utl);
    }

    private static void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) {
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url, request);
        try {
            response.sendRedirect(tokenUrl);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

}
