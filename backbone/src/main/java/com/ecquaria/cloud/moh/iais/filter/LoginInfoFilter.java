package com.ecquaria.cloud.moh.iais.filter;

import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.RedisNameSpaceConstant;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.helper.RedisCacheHelper;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.usersession.UserSession;
import com.ecquaria.cloud.usersession.UserSessionUtil;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
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
        log.info("init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String homeUrl = ConfigHelper.getString("egp.site.url", "https://" + request.getServerName() + "/main-web");
            // Filter session count
            Date creatDt = SpringContextHelper.getContext().getBean(RedisCacheHelper.class).get(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET, request.getSession().getId());
            if (creatDt == null) {
                IaisEGPHelper.redirectUrl((HttpServletResponse) response, homeUrl + "/403-error.jsp");
            }
            // Filter login Info
            String currentApp = ConfigHelper.getString("spring.application.name");
            String currentDomain = ConfigHelper.getString("iais.current.domain");
            boolean fakeLogin = ConfigHelper.getBoolean("halp.fakelogin.flag", false);
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            if (loginContext == null && AppConsts.DOMAIN_INTRANET.equalsIgnoreCase(currentDomain)
                    && "main-web".equalsIgnoreCase(currentApp) && !fakeLogin) {
                log.info("Come to AD login ===>");
                Class<?> cls = MiscUtil.getClassFromName("com.ecquaria.cloud.moh.iais.filter.HalpLoginFilter");
                String userIdStr = request.getHeader("userid");
                log.info(StringUtil.changeForLog("AD user id passed in ====> " + userIdStr));
                try {
                    Object obj = cls.newInstance();
                    Method med = cls.getMethod("doAdlogin", HttpServletRequest.class, String.class);
                    med.invoke(obj, request, userIdStr);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new IaisRuntimeException(e);
                }
            }
            String uri = request.getRequestURI();
            String sessionId = UserSessionUtil.getLoginSessionID(request.getSession());
            UserSession userSession = ProcessCacheHelper.getUserSessionFromCache(sessionId);
            if (userSession == null || !"Active".equals(userSession.getStatus())) {
                log.info(StringUtil.changeForLog("User session invalid ==>" + sessionId));
                loginContext = null;
                ParamUtil.setSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER, null);
            }
            if (uri.indexOf("FE_Landing") < 0 && uri.indexOf("FE_Corppass_Landing") < 0
                    && uri.indexOf("FE_Singpass_Landing") < 0 && uri.indexOf("halp-event-callback") < 0
                    && uri.indexOf("IntraLogin") < 0 && uri.indexOf("health") < 0
                    && uri.indexOf("/INTERNET/Payment") < 0  && uri.indexOf("/INTERNET/InfoDo") < 0 && uri.indexOf("/Moh_Myinfo_Transfer_Station/transmit") < 0 && loginContext == null) {
                    log.info(StringUtil.changeForLog("No Login Context ===> " + uri));
                    IaisEGPHelper.redirectUrl((HttpServletResponse) response, homeUrl);
                }
        }
        chain.doFilter(servletRequest, response);
    }

    @Override
    public void destroy() {
        log.info("destroy");
    }
}
