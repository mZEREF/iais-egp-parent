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

import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.web.logging.util.AuditLogUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

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
    private WebValidationHelper() {
        throw new IllegalStateException("Utility class");
    }

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

    /**
     * @description: Generate Jason String for display
     *
     * @author: Jinhua on 2019/12/13 12:11
     * @param: [errorMsg]
     * @return: java.lang.String
     */
    public static String generateJsonStr(Map<String, String> errorMsg) {
        if (!errorMsg.isEmpty()) {
            StringBuilder sb = new StringBuilder("{\"");
            for (Map.Entry<String, String> ent : errorMsg.entrySet()) {
                sb.append(ent.getKey()).append("\" : \"");
                String value = ent.getValue();
                value = value.replaceAll("\"", "&quot;");
                value = value.replaceAll("'", "&apos;");
                sb.append(value).append("\",");
            }
            return sb.substring(0, sb.length() - 1) + "}";
        } else {
            return "";
        }
    }

    private static void saveAuditTrail(ValidationResult result) {
        if (!result.isHasErrors()) {
            return;
        }

        AuditTrailDto dto = IaisEGPHelper.getCurrentAuditTrailDto();

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
        dto.setOperation(AuditTrailConsts.OPERATION_VALIDATION_FAIL);
        try {
            AuditLogUtil.callWithEventDriven(dtoList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        dto.setValidationFail(null);
    }

}
