package com.ecquaria.cloud.moh.iais.aop;

import com.ecquaria.cloud.moh.iais.annotation.FunctionTrack;
import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.util.MiscUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Component
@Slf4j
public class AuditFunctionAspect {

    @Pointcut("@within(com.ecquaria.cloud.moh.iais.annotation.FunctionTrack)")
    public void auditFunction() {
        throw new UnsupportedOperationException();
    }

    @Around("auditFunction()")
    public Object auditAroundFunction(ProceedingJoinPoint point) throws Throwable {
        AuditTrailDto dto = new AuditTrailDto();
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        User user = SessionManager.getInstance(request).getCurrentUser();
        HttpSession session = request.getSession();
        if (user != null) {
            dto.setNricNumber(user.getId());
            dto.setMohUserId(user.getId());
            dto.setUserDomain(SessionManager.getInstance(request).getCurrentUserDomain());
        }
        dto.setSessionId(session.getId());
        dto.setClientIp(MiscUtil.getClientIp(request));
        dto.setUserAgent(request.getHeader("User-Agent"));
        Class clazz = point.getSignature().getDeclaringType();
        if (clazz.isAnnotationPresent(FunctionTrack.class)) {
            FunctionTrack logInfo = (FunctionTrack) clazz.getAnnotation(FunctionTrack.class);
            dto.setOperation(logInfo.auditType());
            dto.setFunctionName(logInfo.funcName());
            dto.setModule(logInfo.moduleName());
        }
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        if (method.isAnnotationPresent(FunctionTrack.class)) {
            FunctionTrack logInfo = method.getAnnotation(FunctionTrack.class);
            dto.setOperation(logInfo.auditType());
            dto.setFunctionName(logInfo.funcName());
            dto.setModule(logInfo.moduleName());
        }
        if (method.isAnnotationPresent(SearchTrack.class)) {
            Object[] args = point.getArgs();
            if (args != null && args.length > 0) {
                for (Object obj : args) {
                    if (obj instanceof SearchParam) {
                        SearchParam param = (SearchParam) obj;
                        Map<String, Object> filters = param.getFilters();
                        ObjectMapper mapper = new ObjectMapper();
                        Map<String, Object> json = new HashMap<>();
                        for (Map.Entry<String, Object> ent : filters.entrySet()) {
                            json.put(ent.getKey(), ent.getValue());
                        }
                        dto.setViewParams(mapper.writeValueAsString(json));
                        break;
                    }
                }
            }
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<AuditTrailDto> dtoList = new ArrayList<>();
        dtoList.add(dto);
        HttpEntity<Collection<AuditTrailDto>> jsonPart = new HttpEntity<>(dtoList, headers);
        restTemplate.exchange("http://localhost:8887/api/audittrail/cudTrail",
                HttpMethod.POST, jsonPart, String.class).getBody();

        return point.proceed();
    }

}
