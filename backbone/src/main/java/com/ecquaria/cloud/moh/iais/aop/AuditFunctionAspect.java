/*
 * This file is generated by ECQ project skeleton automatically at 2019/7/3 13:36.
 *
 * Copyright 2014-2015, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 * No part of this material may be copied, reproduced, transmitted,
 * stored in a retrieval system, reverse engineered, decompiled,
 * disassembled, localised, ported, adapted, varied, modified, reused,
 * customised or translated into any language in any form or by any means,
 * electronic, mechanical, photocopying, recording or otherwise,
 * without the prior written permission of Ecquaria Technologies Pte Ltd.
 */
package com.ecquaria.cloud.moh.iais.aop;

import com.ecquaria.cloud.moh.iais.annotation.FunctionTrack;
import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import sg.gov.moh.iais.common.utils.MiscUtil;
import sg.gov.moh.iais.web.logging.dto.AuditTrailDto;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Component
public class AuditFunctionAspect {

    private RestTemplate restTemplate = new RestTemplate();

    @Pointcut("@annotation(com.ecquaria.cloud.moh.iais.annotation.FunctionTrack)")
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
            keepSearchParam(args, dto);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<AuditTrailDto> dtoList = new ArrayList<>();
        dtoList.add(dto);
        HttpEntity<Collection<AuditTrailDto>> jsonPart = new HttpEntity<>(dtoList, headers);
        restTemplate.exchange("http://localhost:8887/api/audittrail/cudTrail",
                HttpMethod.POST, jsonPart, String.class);

        return point.proceed();
    }

    private void keepSearchParam(Object[] args, AuditTrailDto dto) throws JsonProcessingException {
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
}
