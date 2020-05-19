package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/1/17 16:41
 */
public class InsRepRecValidate implements CustomizeValidator {
    private static final String RECOMMENDATION = "recommendation";
    private static final String CHRONO = "chrono";
    private static final String NUMBER = "number";
    private static final String OTHERS = "Others";
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errorMap = new HashMap<>(34);
        String chrono = ParamUtil.getRequestString(httpServletRequest, CHRONO);
        String number = ParamUtil.getRequestString(httpServletRequest, NUMBER);
        String enforcement = ParamUtil.getRequestString(httpServletRequest, "engageEnforcement");
        String enforcementRemarks = ParamUtil.getRequestString(httpServletRequest, "enforcementRemarks");
        String periods = ParamUtil.getRequestString(httpServletRequest, "periods");
        String recommendation = ParamUtil.getRequestString(httpServletRequest, "recommendation");
        if(!StringUtil.isEmpty(recommendation)){
            if(StringUtil.isEmpty(periods)){
                errorMap.put("period", "ERR0009");
            }
        }
        if (OTHERS.equals(periods)) {
            if (StringUtil.isEmpty(chrono)) {
                errorMap.put("chronoUnit", "ERR0009");
            } else if (StringUtil.isEmpty(number)) {
                errorMap.put("recomInNumber", "ERR0009");
            } else {
                try {
                    Integer.parseInt(number);
                } catch (NumberFormatException e) {
                    errorMap.put("recomInNumber", "GENERAL_ERR0002");
                }
            }
        }
        if(!StringUtil.isEmpty(enforcement)&&StringUtil.isEmpty(enforcementRemarks)){
            errorMap.put("enforcementRemarks", "ERR0009");
        }
            return errorMap;
    }
}
