package com.ecquaria.cloud.moh.iais.filter;

import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.RedisNameSpaceConstant;
import com.ecquaria.cloud.moh.iais.common.helper.RedisCacheHelper;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ActiveSessionFilter
 *
 * @author Jinhua
 * @date 2023/3/7 17:17
 */
@Component
@WebFilter(urlPatterns = "/*", filterName = "activeSessionFilter")
@Slf4j
public class ActiveSessionFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            String currentDomain = ConfigHelper.getString("iais.current.domain");
            if (AppConsts.DOMAIN_INTERNET.equalsIgnoreCase(currentDomain)) {
                HttpServletRequest request = (HttpServletRequest) servletRequest;
                String uri = request.getRequestURI();
                String headerAgent = request.getHeader("User-Agent");
                log.info("The request user-agent => {}", headerAgent);
                // Filter session count
                RedisCacheHelper redisHelper = SpringContextHelper.getContext().getBean(RedisCacheHelper.class);
                SystemParamConfig systemParamConfig = SpringContextHelper.getContext().getBean(SystemParamConfig.class);
                if (!headerAgent.startsWith("curl") && !headerAgent.startsWith("Java")) {
                    Cookie[] cookies = request.getCookies();
                    String key = null;
                    for (Cookie cook : cookies) {
                        if ("halpActiveTick".equals(cook.getName())) {
                            key = cook.getValue();
                        }
                    }
                    int asCount = redisHelper.keyNumbers(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET);
                    boolean isBlock = redisHelper.isContainKey(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET, key);
                    if (StringUtil.isEmpty(key) && uri.contains("FE_Landing")
                            && asCount < systemParamConfig.getMostActiveSessions()) {
                        key = UUID.randomUUID().toString();
                        Cookie cookie = new Cookie("halpActiveTick", key);
                        cookie.setDomain(request.getServerName());
                        HttpServletResponse resp = (HttpServletResponse) response;
                        resp.addCookie(cookie);
                        isBlock = false;
                    }
                    if (isBlock) {
                        String homeUrl = ConfigHelper.getString("iais.system.static.waiting.page");
                        IaisEGPHelper.redirectUrl((HttpServletResponse) response, homeUrl);
                    } else {
                        redisHelper.set(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET, key, new Date());
                    }
                }
            }
        }
        chain.doFilter(servletRequest, response);
    }

    @Override
    public void destroy() {
        log.info("destroy");
    }
}
