package com.ecquaria.cloud.moh.iais.filter;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.usersession.UserSession;
import com.ecquaria.cloud.usersession.UserSessionUtil;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sop.webflow.process5.ProcessCacheHelper;

/**
 * LoginInfoFilter
 *
 * @author Jinhua
 * @date 2021/1/11 14:55
 */
@Component
@WebFilter(urlPatterns = {"/eservicecontinue/*", "/eservice/*"}, filterName = "loginInfoFilter")
@Slf4j
public class LoginInfoFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String uri = request.getRequestURI();
            String sessionId = UserSessionUtil.getLoginSessionID(request.getSession());
            UserSession userSession = ProcessCacheHelper.getUserSessionFromCache(sessionId);
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            if (userSession == null || !"Active".equals(userSession.getStatus())) {
                log.info(StringUtil.changeForLog("<== User session invalid ==>"));
                loginContext = null;
                ParamUtil.setSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER, null);
            }
            if (uri.indexOf("FE_Landing") < 0 && uri.indexOf("FE_Corppass_Landing") < 0
                    && uri.indexOf("FE_Singpass_Landing") < 0 && uri.indexOf("halp-event-callback") < 0
                    && uri.indexOf("IntraLogin") < 0 && uri.indexOf("health") < 0
                    && uri.indexOf("/INTERNET/Payment") < 0 ) {
                if (loginContext == null) {
                    log.info(StringUtil.changeForLog("No Login Context ===> " + uri));
                    ((HttpServletResponse) response).sendRedirect("https://" + request.getServerName() + "/main-web");
                }
            }
        }
        chain.doFilter(servletRequest, response);
    }

    @Override
    public void destroy() {
    }
}
