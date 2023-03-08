package com.ecquaria.cloud.moh.iais.filter;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.RedisNameSpaceConstant;
import com.ecquaria.cloud.moh.iais.common.helper.RedisCacheHelper;
import java.io.IOException;
import java.util.Date;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
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
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            // Filter session count
            RedisCacheHelper redisHelper = SpringContextHelper.getContext().getBean(RedisCacheHelper.class);
            SystemParamConfig systemParamConfig = SpringContextHelper.getContext().getBean(SystemParamConfig.class);
            Date creatDt = redisHelper.get(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET, request.getSession().getId());
            int asCount = redisHelper.keyNumbers(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET);
            if (creatDt == null && asCount > systemParamConfig.getMostActiveSessions()) {
//                IaisEGPHelper.redirectUrl((HttpServletResponse) response, homeUrl + "/403-error.jsp");
            } else {
                redisHelper.set(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET, request.getSession().getId(),
                        new Date(), RedisCacheHelper.SESSION_DEFAULT_EXPIRE);
            }
        }
        chain.doFilter(servletRequest, response);
    }

    @Override
    public void destroy() {
        log.info("destroy");
    }
}
