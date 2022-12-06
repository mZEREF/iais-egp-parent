package com.ecquaria.cloud.moh.iais.aop;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.usersession.UserSession;
import com.ecquaria.cloud.usersession.UserSessionUtil;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sop.webflow.process5.ProcessCacheHelper;

/**
 * AjaxAccessValAspect
 *
 * @author Jinhua
 * @date 2022/11/14 9:12
 */
@Aspect
@Component
@Slf4j
public class AjaxAccessValAspect {

    /**
     * only for point cut
     */
    @Pointcut("this(com.ecquaria.cloud.moh.iais.action.LoginAccessCheck)")
    public void accessCheck() {
    }

    @Before("accessCheck()")
    public void beforeAccessCheckFunction(JoinPoint joinPoint) throws Throwable {
        Object obj = joinPoint.getTarget();
        String methodName = joinPoint.getSignature().getName();
        Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
        Method m = obj.getClass().getMethod(methodName, parameterTypes);
        if (m != null && (m.getAnnotation(RequestMapping.class) != null
            || m.getAnnotation(PostMapping.class) != null || m.getAnnotation(GetMapping.class) != null
            || m.getAnnotation(PutMapping.class) != null || m.getAnnotation(DeleteMapping.class) != null)) {
            String sessionId = UserSessionUtil.getLoginSessionID(MiscUtil.getCurrentRequest().getSession());
            UserSession userSession = ProcessCacheHelper.getUserSessionFromCache(sessionId);
            if (userSession == null || !"Active".equals(userSession.getStatus())) {
                throw new IaisRuntimeException("User session invalid");
            }
        }
    }
}
