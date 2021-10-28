package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * FertilisationDtoValidator
 *
 * @author fanghao
 * @date 2021/10/28
 */

@Component
@Slf4j
public class FertilisationDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        return CustomizeValidator.super.validate(request);
    }
}
