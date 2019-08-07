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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.common.constant.AuditTrailConsts;
import sg.gov.moh.iais.common.exception.IaisRuntimeException;
import sg.gov.moh.iais.common.utils.MiscUtil;
import sg.gov.moh.iais.common.utils.ParamUtil;
import sg.gov.moh.iais.common.validation.ValidationUtils;
import sg.gov.moh.iais.common.validation.dto.ValidationResult;
import sg.gov.moh.iais.web.logging.dto.AuditTrailDto;
import sg.gov.moh.iais.web.logging.util.AuditLogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * WebValidationHelper
 *
 * @author Jinhua
 * @date 2019/7/17 16:52
 */
@Slf4j
public class WebValidationHelper {
    /**
     * The method to do the validation for entity class
     *
     * @param obj
     * @param <T>
     *
     * @return
     */
    public static <T> ValidationResult validateEntity(T obj) {
        ValidationResult rslt = ValidationUtils.validateEntity(obj);
        saveAuditTrail(rslt);

        return rslt;
    }

    /**
     * Method to do validate with profiles
     *
     * @param obj
     * @param propertyName
     * @param <T>
     *
     * @return
     */
    public static <T> ValidationResult validateProperty(T obj, String propertyName) {
        ValidationResult rslt = ValidationUtils.validateProperty(obj, propertyName);
        saveAuditTrail(rslt);

        return rslt;
    }

    /**
     * @description: Do validation for an Object array
     *
     * @author: Jinhua on 2019/7/18 15:43
     * @param: [objs]
     * @return: sg.gov.moh.iais.common.validation.dto.ValidationResult
     */
    public static ValidationResult doValidate(Object[] objs) {
        return doValidate(objs, null);
    }

    /**
     * @description: Do validation for an Object array and profile array
     *
     * @author: Jinhua on 2019/7/18 15:44
     * @param: [objs, profiles] -- These 2 arrays' length must be the same
     * @return: sg.gov.moh.iais.common.validation.dto.ValidationResult
     */
    public static ValidationResult doValidate(Object[] objs, String[] profiles) {
        ValidationResult rslt = ValidationUtils.doValidate(objs, profiles);
        saveAuditTrail(rslt);

        return rslt;
    }

    private static void saveAuditTrail(ValidationResult result) {
        if (!result.isHasErrors())
            return;

        AuditTrailDto dto = (AuditTrailDto) ParamUtil.getSessionAttr(MiscUtil.getCurrentRequest(),
                AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
        if (dto == null)
            dto = AuditTrailDto.getThreadDto();

        Map<String, String> errors = result.retrieveAll();
        ObjectMapper mapper = new ObjectMapper();
        try {
            dto.setValidationFail(mapper.writeValueAsString(errors));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException(e);
        }
        List<AuditTrailDto> dtoList = new ArrayList<>();
        dtoList.add(dto);
        dto.setOperation(AuditTrailConsts.OPERATION_INTERNET_VALIDATION_FAIL);
        try {
            AuditLogUtil.callAuditRestApi(dtoList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        dto.setValidationFail(null);
    }
}
