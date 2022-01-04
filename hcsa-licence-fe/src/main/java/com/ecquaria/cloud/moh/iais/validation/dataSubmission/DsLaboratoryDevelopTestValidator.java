package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
@Slf4j
public class DsLaboratoryDevelopTestValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        return map;
    }
}
