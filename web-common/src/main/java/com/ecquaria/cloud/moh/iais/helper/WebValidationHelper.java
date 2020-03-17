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
import com.ecquaria.cloud.moh.iais.common.annotation.CustomValidate;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.web.logging.util.AuditLogUtil;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.apache.commons.lang.StringUtils;

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
        ValidationResult result = new ValidationResult();
        try {
            Validator validator = new Validator();
            List<ConstraintViolation> violations = null;
            if (!StringUtil.isEmpty(propertyName)) {
                String[] profiles = propertyName.split("\\,");
                violations = validator.validate(obj, profiles);
            } else {
                violations = validator.validate(obj);
            }

            if (violations != null && !violations.isEmpty()) {
                result.setHasErrors(true);
                for (ConstraintViolation constraintViolation : violations) {
                    String fullName = constraintViolation.getContext().toString();
                    String name = fullName.substring(fullName.lastIndexOf('.') + 1);

                    Collection<? extends Serializable> values = constraintViolation.getMessageVariables() == null ? null
                            : constraintViolation.getMessageVariables().values();
                    if (!Objects.isNull(values) && !values.isEmpty()){
                        String[] val = values.toArray(new String[1]);
                        if (val.length > 0){
                            String i = val[0];
                            //example: "Key/Number"
                            result.addMessage(name, formatValuesMessage(constraintViolation.getMessage(), i));
                        }
                    }else {
                        result.addMessage(name, constraintViolation.getMessage());
                    }
                }
            }
            result.addMessages(customizeValidate(obj.getClass(), propertyName, result.isHasErrors()));
            saveAuditTrail(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException(e);
        }

        return result;
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
            StringBuilder sb = new StringBuilder("[");
            for (Map.Entry<String, String> ent : errorMsg.entrySet()) {
                sb.append("{\"");
                sb.append(ent.getKey()).append("\" : \"");

                String value = ent.getValue();
                String msg;
                if (value.contains("/")){
                    int indx = value.indexOf("/");
                    try {
                        String num = value.substring(indx + 1);
                        Integer.parseInt(num);
                        msg = MessageUtil.getMessageDesc(value.substring(0, indx));
                        msg = msg.replace("%d", num);
                    }catch (NumberFormatException e){
                        msg  = MessageUtil.getMessageDesc(value);
                        log.debug(e.getMessage());
                    }
                }else {
                    msg  = MessageUtil.getMessageDesc(ent.getValue());
                }

                msg = msg.replaceAll("\"", "&quot;");
                msg = msg.replaceAll("'", "&apos;");
                sb.append(msg).append("\"},");
            }
            return sb.substring(0, sb.length() - 1) + "]";
        } else {
            return "[]";
        }
    }

    public static Boolean cpmpareDate(Date first, Date second){
        return second.compareTo(first) < 0 ? true : false;
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
            int indx = value.indexOf("/");
            try {
                String num = value.substring(indx + 1);
                Integer.parseInt(num);
                msg = MessageUtil.getMessageDesc(value.substring(0, indx));
                msg = msg.replace("%d", num);
            }catch (NumberFormatException e){
                msg  = MessageUtil.getMessageDesc(value);
                log.debug(e.getMessage());
            }
        }else {
            msg  = MessageUtil.getMessageDesc(value);
        }

        msg = msg.replaceAll("\"", "&quot;");
        msg = msg.replaceAll("'", "&apos;");
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
    private static Map<String, String> customizeValidate(Class cls, String property, boolean withError){
        CustomValidate ano = (CustomValidate) cls.getAnnotation(CustomValidate.class);
        if (ano == null || (withError && ano.skipWhenError())) {
            return null;
        }
        if (!org.springframework.util.StringUtils.isEmpty(property)) {
            boolean jump = true;
            for (String prop : ano.properties()) {
                if (property.equals(prop)) {
                    jump = false;
                    break;
                }
            }
            if (jump) {
                return null;
            }
        }

        try {
            Class valCls = Class.forName(ano.impClass());
            Object obj = null;
            try {
                obj = SpringContextHelper.getContext().getBean(valCls);
                obj.hashCode();
            } catch (Exception e) {
                obj = valCls.newInstance();
            }
            CustomizeValidator cv = (CustomizeValidator) obj;
            HttpServletRequest request = MiscUtil.getCurrentRequest();
            if (request != null) {
                return cv.validate(request);
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException(e);
        }

        return null;
    }

    private static void saveAuditTrail(ValidationResult result) {
        if (!result.isHasErrors()) {
            return;
        }

        AuditTrailDto dto = IaisEGPHelper.getCurrentAuditTrailDto();

        Map<String, String> errors = result.retrieveAll();
        String errorMsg = generateJsonStr(errors);
        dto.setValidationFail(errorMsg);
        List<AuditTrailDto> dtoList = IaisCommonUtils.genNewArrayList();
        dtoList.add(dto);
        dto.setOperation(AuditTrailConsts.OPERATION_VALIDATION_FAIL);
        SubmissionClient client = SpringContextHelper.getContext().getBean(SubmissionClient.class);
        try {
            AuditLogUtil.callWithEventDriven(dtoList, client);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        dto.setValidationFail(null);
    }

    private static String formatValuesMessage(String message, String val){
        return message + "/" + val;
    }

}
