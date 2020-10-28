package com.ecquaria.cloud.moh.iais.filter;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.sz.commons.util.StringUtil;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class HalpLoginFilter implements Filter {
    @Value("${halp.fakelogin.flag}")
    private boolean fakeLogin;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if ((servletRequest instanceof HttpServletRequest) && !fakeLogin) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request,
                    AppConsts.SESSION_ATTR_LOGIN_USER);
            if (loginContext == null) {
                String userIdStr = request.getHeader("userid");
                if (!StringUtil.isEmpty(userIdStr)) {
                    String userId = userIdStr.substring(userIdStr.indexOf('\\'));

                }
            }
        }

    }
}
