package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.dto.AuditTrailDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.*;

public class ValidationUtil {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> ValidationResult validateEntity(T obj) {
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        if (set != null && set.size() != 0) {
            result.setHasErrors(true);
            Map<String, String> errorMsg = new HashMap<String, String>();
            List<AuditTrailDto> auditList = new ArrayList<AuditTrailDto>();

            for (ConstraintViolation<T> cv : set) {
                result.addMessage(cv.getPropertyPath().toString(), cv.getMessage());
            }
        }
        return result;
    }

    public static <T> ValidationResult validateProperty(T obj, String propertyName) {
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<T>> set = validator.validateProperty(obj, propertyName, Default.class);
        if (set != null && set.size() != 0) {
            result.setHasErrors(true);
            Map<String, String> errorMsg = new HashMap<String, String>();
            for (ConstraintViolation<T> cv : set) {
                result.addMessage(propertyName, cv.getMessage());
            }
        }
        return result;
    }
}
