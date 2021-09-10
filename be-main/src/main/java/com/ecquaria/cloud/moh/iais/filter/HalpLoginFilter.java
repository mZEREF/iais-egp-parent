package com.ecquaria.cloud.moh.iais.filter;

import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.action.BackendLoginDelegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloudfeign.FeignException;
import ecq.commons.exception.BaseException;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * LoginFilter
 *
 * @author Jinhua
 * @date 2020/10/28 12:06
 */
@Component
@WebFilter(urlPatterns = "/*", filterName = "adLoginFilter")
@Slf4j
@Order(2)
public class HalpLoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        log.info("Start run HalpLoginFilter");
        BackendLoginDelegator blDelegate = SpringContextHelper.getContext().getBean(BackendLoginDelegator.class);
        boolean fakeLogin = ConfigHelper.getBoolean("halp.fakelogin.flag");
        if ((servletRequest instanceof HttpServletRequest) && !fakeLogin) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request,
                    AppConsts.SESSION_ATTR_LOGIN_USER);
            if (loginContext == null) {
                String userIdStr = request.getHeader("userid");
                log.debug(StringUtil.changeForLog("AD user id passed in ====> " + userIdStr));
                if (!StringUtil.isEmpty(userIdStr)) {
                    String userId = userIdStr.substring(userIdStr.lastIndexOf('\\') + 1);
                    try {
                        blDelegate.doLogin(userId, request);
                    } catch (FeignException | BaseException e) {
                        log.error(e.getMessage(), e);
                        throw new IaisRuntimeException(e);
                    }
                }
            }
        }
        log.info("End run HalpLoginFilter");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
