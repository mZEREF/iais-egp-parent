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

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.annotation.LogInfo;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.web.logging.util.AuditLogUtil;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuditFunctionAspect {
    @Autowired
    private SubmissionClient client;

    @Pointcut("@annotation(com.ecquaria.cloud.moh.iais.common.annotation.LogInfo)")
    public void auditFunction() {
        throw new UnsupportedOperationException();
    }
    @Pointcut("@within(com.ecquaria.cloud.moh.iais.common.annotation.LogInfo)")
    public void auditClass() {
        throw new UnsupportedOperationException();
    }
    @Pointcut("@annotation(com.ecquaria.cloud.moh.iais.annotation.SearchTrack)")
    public void auditSearch() {
        throw new UnsupportedOperationException();
    }

    @Around("auditFunction()")
    public Object auditAroundFunction(ProceedingJoinPoint point) throws Throwable {
        AuditTrailDto dto = (AuditTrailDto) ParamUtil.getSessionAttr(MiscUtil.getCurrentRequest(),
                AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
        if (dto == null) {
            dto = new AuditTrailDto();
        }

        IaisEGPHelper.setAuditLoginUserInfo(dto);
        return auditFunction(point, dto);
    }

    @Around("auditClass()")
    public Object auditAroundClass(ProceedingJoinPoint point) throws Throwable {
        AuditTrailDto dto = (AuditTrailDto) ParamUtil.getSessionAttr(MiscUtil.getCurrentRequest(),
                AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
        if (dto == null) {
            dto = new AuditTrailDto();
        }

        IaisEGPHelper.setAuditLoginUserInfo(dto);
        Class clazz = point.getSignature().getDeclaringType();
        if (clazz.isAnnotationPresent(LogInfo.class)) {
            LogInfo logInfo = (LogInfo) clazz.getAnnotation(LogInfo.class);
            dto.setOperation(logInfo.auditType());
            dto.setFunctionName(logInfo.funcName());
            dto.setModule(logInfo.moduleName());
        }

        return auditFunction(point, dto);
    }

    @Around("auditSearch()")
    public Object keepSearchParam(ProceedingJoinPoint point) throws Throwable {
        AuditTrailDto dto = (AuditTrailDto) ParamUtil.getSessionAttr(MiscUtil.getCurrentRequest(),
                AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
        Object[] args = point.getArgs();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        Map<String, Object> json = IaisCommonUtils.genNewHashMap();
        SearchTrack logInfo = method.getAnnotation(SearchTrack.class);
        if (!StringUtil.isEmpty(logInfo.catalog()) && !StringUtil.isEmpty(logInfo.key())) {
            json.put("sql_catalog", logInfo.catalog());
            json.put("sql_key", logInfo.key());
        }
        dto.setOperation(AuditTrailConsts.OPERATION_VIEW_RECORD);

        if (args != null && args.length > 0) {

            for (Object obj : args) {
                if (obj instanceof SearchParam) {
                    SearchParam param = (SearchParam) obj;
                    Map<String, Object> filters = param.getFilters();
                    ObjectMapper mapper = new ObjectMapper();
                    json.put("current_page", param.getPageNo());
                    for (Map.Entry<String, Object> ent : filters.entrySet()) {
                        json.put(ent.getKey(), ent.getValue());
                    }
                    dto.setViewParams(mapper.writeValueAsString(json));
                    break;
                }
            }
        }
        callRestApi(dto);

        return point.proceed();
    }

    private Object auditFunction(ProceedingJoinPoint point, AuditTrailDto dto) throws Throwable {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        if (method.isAnnotationPresent(LogInfo.class)) {
            LogInfo logInfo = method.getAnnotation(LogInfo.class);
            dto.setOperation(logInfo.auditType());
            dto.setFunctionName(logInfo.funcName());
            dto.setModule(logInfo.moduleName());
        }
        ParamUtil.setSessionAttr(MiscUtil.getCurrentRequest(), AuditTrailConsts.SESSION_ATTR_PARAM_NAME, dto);

        return point.proceed();
    }

    private void callRestApi(AuditTrailDto dto) {
        List<AuditTrailDto> dtoList = IaisCommonUtils.genNewArrayList();
        dtoList.add(dto);
        try {
            AuditLogUtil.callWithEventDriven(dtoList, client);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        dto.setViewParams(null);
    }
}
