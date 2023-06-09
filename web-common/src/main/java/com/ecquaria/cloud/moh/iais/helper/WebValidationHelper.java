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

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.annotation.AuditAppNoFetch;
import com.ecquaria.cloud.moh.iais.common.annotation.AuditAppNoField;
import com.ecquaria.cloud.moh.iais.common.annotation.AuditLicNoFetch;
import com.ecquaria.cloud.moh.iais.common.annotation.AuditLicNoField;
import com.ecquaria.cloud.moh.iais.common.annotation.CustomMsg;
import com.ecquaria.cloud.moh.iais.common.annotation.CustomValidate;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.RedisNameSpaceConstant;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.helper.RedisCacheHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import lombok.extern.slf4j.Slf4j;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        return validateProperty(obj, null);
    }

    /**
     * @description: Do validation for an Object array
     *
     * @author: Jinhua on 2019/7/18 15:43
     * @param: [objs]
     * @return: ValidationResult
     */
    public static ValidationResult doValidate(Object[] objs) {
        return doValidate(objs, null);
    }

    /**
     * @description: Do validation for an Object array and profile array
     *
     * @author: Jinhua on 2019/7/18 15:44
     * @param: [objs, profile] -- These 2 arrays' length must be the same
     * @return: ValidationResult
     */
    public static ValidationResult doValidate(Object[] objs, String[] profile) {
        ValidationResult result = new ValidationResult();
        if (profile == null) {
            profile = new String[objs.length];
        }
        for (int i = 0; i < objs.length; i++) {
            ValidationResult rslt = validateProperty(objs[i], profile[i]);
            if (rslt != null && rslt.isHasErrors()) {
                result.setHasErrors(true);
                result.addMessages(rslt.retrieveAll());
            }
        }

        return result;
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
        if (obj == null) {
            return null;
        }
        ValidationResult result;
        try {
            result = validatePropertyWithoutCustom(obj, "default,"+propertyName);
            if (result != null) {
                HttpServletRequest request = MiscUtil.getCurrentRequest();
                if (request != null && result.isHasErrors()) {
                    request.setAttribute(obj.getClass().getSimpleName() + "_base_error_msgs", result.retrieveAll());
                }
                result.addMessages(customizeValidate(obj, obj.getClass(), propertyName, result.isHasErrors()));
                saveAuditTrail(obj, result);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException(e);
        }

        return result;
    }

    /**
     * only validate the entity with oval
     *
     * @param obj
     * @param propertyName
     * @param <T>
     * @return
     */
    public static <T> ValidationResult validatePropertyWithoutCustom(T obj, String propertyName) {
        if (obj == null) {
            return null;
        }
        ValidationResult result = new ValidationResult();
        try {
            Validator validator = new Validator();
            List<ConstraintViolation> violations;
            if (!StringUtil.isEmpty(propertyName)) {
                String[] profiles = propertyName.split("\\,");
                violations = validator.validate(obj, profiles);
            } else {
                violations = validator.validate(obj);
            }

            if (violations != null && !violations.isEmpty()) {
                Class cls = obj.getClass();
                result.setHasErrors(true);
                for (ConstraintViolation constraintViolation : violations) {
                    String fullName = constraintViolation.getContext().toString();
                    String name = fullName.substring(fullName.lastIndexOf('.') + 1);

                    Collection<? extends Serializable> values = constraintViolation.getMessageVariables() == null ? null
                            : constraintViolation.getMessageVariables().values();
                    String msg;
                    if (!Objects.isNull(values) && !values.isEmpty()) {
                        String i = String.valueOf(values.iterator().next());
                        //example: "Key/Number"
                        msg = formatValuesMessage(constraintViolation.getMessage(), i);
                    } else {
                        msg = constraintViolation.getMessage();
                    }
                    addMessage(result, cls, name, msg);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException(e);
        }

        return result;
    }

    private static void addMessage(ValidationResult result, Class cls, String name, String msg) throws NoSuchFieldException {
        if (StringUtil.isEmpty(msg)) {
            return;
        }
        Field field = cls.getDeclaredField(name);
        if (field != null) {
            if (msg.contains("/")) {
                msg = msg.substring(0, msg.indexOf('/'));
            }
            CustomMsg cMsg = field.getAnnotation(CustomMsg.class);
            if (cMsg != null && cMsg.placeHolders().length > 0) {
                Map<String, String> repMap = IaisCommonUtils.genNewHashMap(cMsg.placeHolders().length);
                for (int i = 0; i < cMsg.placeHolders().length; i++) {
                    repMap.put(cMsg.placeHolders()[i], cMsg.replaceVals()[i]);
                }
                msg = MessageUtil.getMessageDesc(msg, repMap);
            }
        }
        result.addMessage(name, msg);
    }

    /**
     * @description: Generate Jason String for display
     *
     * @author: Jinhua on 2019/12/13 12:11
     * @param: [errorMsg]
     * @return: java.lang.String
     */
    public static String generateJsonStr(Map<String, String> errorMsg) {
        if (errorMsg.isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (Map.Entry<String, String> ent : errorMsg.entrySet()) {
            sb.append("{\"");
            sb.append(ent.getKey()).append("\" : \"");

            String value = ent.getValue();
            String msg;
            if (value.contains("/")) {
                int indx = value.indexOf('/');
                try {
                    String num = value.substring(indx + 1);
                    if (num.contains(".")) {
                        // The value returned by annotation @Min is a float number
                        int signIndex = num.indexOf('.');
                        num = num.substring(0, signIndex);
                    }

                    Integer.parseInt(num);

                    msg = MessageUtil.getMessageDesc(value.substring(0, indx));
                    msg = msg.replace("%d", num);
                } catch (NumberFormatException e) {
                    msg = MessageUtil.getMessageDesc(value);
                    log.debug(e.getMessage());
                }
            } else {
                msg = MessageUtil.getMessageDesc(ent.getValue());
            }

            msg = msg.replace("\"", "&quot;");
            msg = msg.replace("'", "&apos;");
            sb.append(msg).append("\"},");
        }
        return sb.substring(0, sb.length() - 1) + "]";
    }

    public static String generateJsonStr(String fieldName, String errorMsg) {
        if (StringUtils.isEmpty(errorMsg)){
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        sb.append("{\"");
        sb.append(fieldName).append("\" : \"");

        String value = errorMsg;
        String msg;
        if (value.contains("/")){
            int indx = value.indexOf('/');
            try {
                String num = value.substring(indx + 1);
                Integer.valueOf(num);
                msg = MessageUtil.getMessageDesc(value.substring(0, indx));
                msg = msg.replace("%d", num);
            }catch (NumberFormatException e){
                msg  = MessageUtil.getMessageDesc(value);
                log.debug(e.getMessage());
            }
        }else {
            msg  = MessageUtil.getMessageDesc(value);
        }

        msg = msg.replace("\"", "&quot;");
        msg = msg.replace("'", "&apos;");
        sb.append(msg).append("\"},");

        return sb.substring(0, sb.length() - 1) + "]";
    }

    /**
     * @description: The method to do the customize validation
     *
     * @author: ECQ_Jinhua on 2019/7/5 9:08
     * @param: [cls]
     * @return: java.util.Map<java.lang.String,java.lang.String>
     */
    private static <T> Map<String, String> customizeValidate(T target, Class cls, String property, boolean withError){
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        CustomValidate ano = (CustomValidate) cls.getAnnotation(CustomValidate.class);
        if (ano == null || (withError && ano.skipWhenError())) {
            return errorMap;
        }
        if (isJump(property, ano)) {
            return errorMap;
        }

        try {
            Class valCls = MiscUtil.getClassFromName(ano.impClass());
            Object obj;
            try {
                obj = SpringContextHelper.getContext().getBean(valCls);
            } catch (Exception e) {
                obj = valCls.newInstance();
            }
            CustomizeValidator cv = (CustomizeValidator) obj;
            HttpServletRequest request = MiscUtil.getCurrentRequest();
            if (request != null) {
                request.setAttribute(valCls.getSimpleName() + "_profile", property);
                Map<String, String> map = cv.validate(request);
                if(map != null) {
                    errorMap.putAll(map);
                }
                map = cv.validate(target, request);
                if(map != null) {
                    errorMap.putAll(map);
                }
                map = cv.validate(target, property, request);
                if(map != null) {
                    errorMap.putAll(map);
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException(e);
        }

        return errorMap;
    }

    private static boolean isJump(String property, CustomValidate ano) {
        boolean jump = false;
        if (!org.springframework.util.StringUtils.isEmpty(property)) {
            jump = true;
            for (String prop : ano.properties()) {
                if (property.equals(prop)) {
                    jump = false;
                    break;
                }
            }
        }
        return jump;
    }

    private static void saveAuditTrail(Object obj, ValidationResult result) {
        if (!result.isHasErrors()) {
            return;
        }

        AuditTrailDto dto = IaisEGPHelper.getCurrentAuditTrailDto();

        Map<String, String> errors = result.retrieveAll();
        saveAuditTrailForNoUseResult(obj, errors);
    }

    public static void saveAuditTrailForNoUseResult(Object entity, Map<String, String> errors) {
        if (errors == null || errors.isEmpty()) {
            return;
        }
        AuditTrailDto at = MiscUtil.transferEntityDto(IaisEGPHelper.getCurrentAuditTrailDto(), AuditTrailDto.class);
        if (entity != null) {
            Field[] fields = entity.getClass().getDeclaredFields();
            RedisCacheHelper redisCacheHelper = SpringContextHelper.getContext().getBean(RedisCacheHelper.class);
            for (Field field : fields) {
                setAudit(entity, at, redisCacheHelper, field);
            }
        }
        String errorMsg = generateJsonStr(errors);
        at.setValidationFail(errorMsg);
        at.setOperation(AuditTrailConsts.OPERATION_VALIDATION_FAIL);
        AuditTrailHelper.callSaveAuditTrail(at);
        at.setValidationFail(null);
    }

    private static void setAudit(Object entity, AuditTrailDto at, RedisCacheHelper redisCacheHelper, Field field) {
        try {
            AuditAppNoField app = field.getAnnotation(AuditAppNoField.class);
            AuditAppNoFetch fetch = field.getAnnotation(AuditAppNoFetch.class);
            AuditLicNoFetch licFetch = field.getAnnotation(AuditLicNoFetch.class);
            AuditLicNoField lic = field.getAnnotation(AuditLicNoField.class);
            if (app != null) {
                Method getMed = entity.getClass().getMethod("get" + org.springframework.util.StringUtils.capitalize(field.getName()));
                String appNo = (String) getMed.invoke(entity);
                if (!StringUtil.isEmpty(appNo)) {
                    at.setApplicationNum(appNo);
                }
            } else if (fetch != null) {
                Method getMed = entity.getClass().getMethod("get" + org.springframework.util.StringUtils.capitalize(field.getName()));
                String fetchId = (String) getMed.invoke(entity);
                String appNo = redisCacheHelper.get(RedisNameSpaceConstant.REDIS_AUDIT_TRAIL_APP_NAMESPACE, fetchId);
                if (!StringUtil.isEmpty(appNo)) {
                    at.setApplicationNum(appNo);
                }
            }
            if (lic != null) {
                Method getMed = entity.getClass().getMethod("get" + org.springframework.util.StringUtils.capitalize(field.getName()));
                String licNo = (String) getMed.invoke(entity);
                if (!StringUtil.isEmpty(licNo)) {
                    at.setLicenseNum(licNo);
                }
            } else if (licFetch != null) {
                Method getMed = entity.getClass().getMethod("get" + org.springframework.util.StringUtils.capitalize(field.getName()));
                String fetchId = (String) getMed.invoke(entity);
                String licNo = redisCacheHelper.get(RedisNameSpaceConstant.REDIS_AUDIT_TRAIL_LIC_NAMESPACE, fetchId);
                if (!StringUtil.isEmpty(licNo)) {
                    at.setLicenseNum(licNo);
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException(e);
        }
    }

    public static void saveAuditTrailForNoUseResult(Map<String, String> errors){
        saveAuditTrailForNoUseResult(null, errors);
    }

    private static String formatValuesMessage(String message, String val){
        return message + "/" + val;
    }

}
