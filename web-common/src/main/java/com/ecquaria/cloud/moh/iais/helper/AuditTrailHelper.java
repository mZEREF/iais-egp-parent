/*
 * This file is generated by ECQ project skeleton automatically.
 *
 *   Copyright 2019-2049, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 *   No part of this material may be copied, reproduced, transmitted,
 *   stored in a retrieval system, reverse engineered, decompiled,
 *   disassembled, localised, ported, adapted, varied, modified, reused,
 *   customised or translated into any language in any form or by any means,
 *   electronic, mechanical, photocopying, recording or otherwise,
 *   without the prior written permission of Ecquaria Technologies Pte Ltd.
 */

package com.ecquaria.cloud.moh.iais.helper;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.audit.SessionDurationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.AuditTrailWbClient;
import com.ecquaria.cloud.moh.iais.web.logging.util.AuditLogUtil;
import com.ecquaria.cloud.submission.client.model.SubmitReq;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * AuditTrailHelper
 *
 * @author Jinhua
 * @date 2019/8/6 17:58
 */
@Slf4j
public class AuditTrailHelper {

    public static void auditFunction(String moduleName, String functionName) {
        auditFunction(moduleName, functionName, null, null);
    }

    public static void auditFunctionWithAppNo(String moduleName, String functionName, String appNo) {
        auditFunction(moduleName, functionName, appNo, null);
    }

    public static void auditFunctionWithLicNo(String moduleName, String functionName, String licNo) {
        auditFunction(moduleName, functionName, null, licNo);
    }

    public static void auditFunction(String moduleName, String functionName, String appNo, String licenceNo) {
        HttpServletRequest request = MiscUtil.getCurrentRequest();
        AuditTrailDto dto = (AuditTrailDto) ParamUtil.getSessionAttr(request,
                AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
        if (dto == null) {
            dto = new AuditTrailDto();
        }

        IaisEGPHelper.setAuditLoginUserInfo(dto);
        dto.setApplicationNum(appNo);
        dto.setLicenseNum(licenceNo);
        dto.setModule(moduleName);
        dto.setFunctionName(functionName);
        ParamUtil.setSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME, dto);
    }

    public static void setAuditAppNo(String appNo) {
        HttpServletRequest request = MiscUtil.getCurrentRequest();
        AuditTrailDto dto = (AuditTrailDto) ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
        dto.setApplicationNum(appNo);
    }

    public static void setAuditLicNo(String licNo) {
        HttpServletRequest request = MiscUtil.getCurrentRequest();
        AuditTrailDto dto = (AuditTrailDto) ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
        dto.setLicenseNum(licNo);
    }

    public static void callSaveAuditTrailByOperation(int operation){
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        auditTrailDto.setOperation(operation);
        AuditTrailHelper.callSaveAuditTrail(auditTrailDto);
    }

    public static void directInsert(AuditTrailDto trailDto){
        AuditTrailWbClient trailWbClient = SpringContextHelper.getContext().getBean(AuditTrailWbClient.class);
        List<AuditTrailDto> list = Collections.singletonList(trailDto);
        trailWbClient.insertAuditTrail(list);
    }

    /**
     * save audit trail for param
     * @param auditTrailDto
     */
    public static void callSaveAuditTrail(AuditTrailDto auditTrailDto){
        SubmissionClient submissionClient = SpringContextHelper.getContext().getBean(SubmissionClient.class);
        List<AuditTrailDto> trailDtoList = IaisCommonUtils.genNewArrayList(1);
        trailDtoList.add(auditTrailDto);
        try {
            String eventRefNo = String.valueOf(System.currentTimeMillis());
            StringBuilder console = new StringBuilder();
            console.append("call event bus for ").append(auditTrailDto.getFunctionName()).append(" that the operation is  ").append(auditTrailDto.getOperation()).append(", the event ref number is ").append(eventRefNo);
            log.info(console.toString());
            AuditLogUtil.callWithEventDriven(trailDtoList, submissionClient, eventRefNo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void callSaveSessionDuration(String sessionId, int duration){
        SubmissionClient submissionClient = SpringContextHelper.getContext().getBean(SubmissionClient.class);
        SessionDurationDto durationDto = new SessionDurationDto();
        try {
            String eventRefNo = String.valueOf(System.currentTimeMillis());
            durationDto.setSessionId(sessionId);
            durationDto.setDuration(duration);
            durationDto.setEventRefNo(eventRefNo);
            SubmitReq req = new SubmitReq();
            req.setProject("Audit Trail Function");
            req.setProcess("Save Audit Trail");
            req.setStep("Save");
            req.setService(EventBusConsts.SERVICE_NAME_AUDIT_TRAIL);
            req.setOperation(EventBusConsts.OPERATION_AUDIT_TRAIL_UPDATE_SESSION_DURATION);
            req.setSopUrl("No SOP");
            req.setData(JsonUtil.parseToJson(durationDto));
            req.setCallbackUrl((String)null);
            req.setUserId("SOP");
            req.setWait(false);
            submissionClient.submit("http://iais-event-bus", req);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static AuditTrailDto getCurrentAuditTrailDto() {
        return IaisEGPHelper.getCurrentAuditTrailDto();
    }

    private static AuditTrailDto getBatchJobAuditTrail(){
        AuditTrailDto batchJobAt = new AuditTrailDto();
        batchJobAt.setNricNumber("System");
        batchJobAt.setMohUserId("System");
        batchJobAt.setMohUserGuid(AppConsts.USER_ID_SYSTEM);
        batchJobAt.setFunctionName("Batch Job");
        batchJobAt.setOperationType(AuditTrailConsts.OPERATION_TYPE_BATCH_JOB);
        //batchJobAt.setUserDomain(domain);
        return batchJobAt;
    }

    public static void setupBatchJobAuditTrail(Object job) {
        AuditTrailDto trailDto = getBatchJobAuditTrail();
        if (job != null){
            log.info(StringUtil.changeForLog("batch job class " + job.getClass().getName()));
            JobHandler handler = job.getClass().getAnnotation(JobHandler.class);
            if (handler != null){
                log.info(StringUtil.changeForLog("handler value" + handler.value()));
                trailDto.setEntityId(handler.value());
            }else {
                Delegator delegator = job.getClass().getAnnotation(Delegator.class);
                if(delegator != null){
                    log.info(StringUtil.changeForLog("delegator value" + delegator.value()));
                    trailDto.setEntityId(delegator.value());
                }
            }
        }

        log.info(StringUtil.changeForLog("batch job function name" + trailDto.getFunctionName()));

        AuditTrailDto.setThreadDto(trailDto);
    }

    private AuditTrailHelper() {
        throw new IllegalStateException("Utility class");
    }



    public static void insertLoginFailureAuditTrail(HttpServletRequest request, String identityNo){
        insertLoginFailureAuditTrail(request, null, identityNo);
    }

    public static void insertLoginFailureAuditTrail(HttpServletRequest request, String identityNo, String reason){
        insertLoginFailureAuditTrail(request, null, identityNo, reason);
    }

    public static void insertLoginFailureAuditTrail(HttpServletRequest request, String uen, String identityNo, String reason){
        AuditTrailDto atd = new AuditTrailDto();
        SystemParamConfig config = SpringContextHelper.getContext().getBean(SystemParamConfig.class);
        String cyd = config.getCurSysDomain();
        atd.setClientIp(MiscUtil.getClientIp(request));
        atd.setUserAgent(AccessUtil.getBrowserInfo(request));
        atd.setFailReason(reason);
        if (AppConsts.USER_DOMAIN_INTRANET.equalsIgnoreCase(cyd)){
            atd.setModule("Intranet Login");
            atd.setFunctionName("Intranet Login");
            atd.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTRANET);
            atd.setMohUserId(identityNo);
            atd.setLoginType(AuditTrailConsts.LOGIN_TYPE_MOH);
        }else if (AppConsts.USER_DOMAIN_INTERNET.equalsIgnoreCase(cyd)){
            atd.setModule("Internet Login");
            atd.setFunctionName("Internet Login");
            atd.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTERNET);
            atd.setUenId(uen);
            atd.setMohUserId(identityNo);
            atd.setNricNumber(identityNo);
            atd.setLoginType(StringUtil.isEmpty(uen) ? AuditTrailConsts.LOGIN_TYPE_SING_PASS : AuditTrailConsts.LOGIN_TYPE_CORP_PASS);
            if (OrganizationConstants.ID_TYPE_FIN.equals(IaisEGPHelper.checkIdentityNoType(identityNo))){
                //for audit trail page display, issue 67866
                atd.setEntityId(identityNo);
            }
        }

        atd.setLoginTime(new Date());
        atd.setOperation(AuditTrailConsts.OPERATION_LOGIN_FAIL);
        AuditTrailHelper.callSaveAuditTrail(atd);
    }
}
