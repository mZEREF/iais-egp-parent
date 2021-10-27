package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ThawingStageDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(Object obj, String[] profiles, HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        return errMap;
    }
}
