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
    private final String RECOMMENDATION = "recommendation";
    private final String CHRONO = "chrono";
    private final String NUMBER = "number";
    private final String OTHERS = "Others";
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errorMap = new HashMap<>(34);
        String recommendation = ParamUtil.getRequestString(httpServletRequest, RECOMMENDATION);
        String remarks = ParamUtil.getRequestString(httpServletRequest, "remarks");
        String chrono = ParamUtil.getRequestString(httpServletRequest, CHRONO);
        String number = ParamUtil.getRequestString(httpServletRequest, NUMBER);
        String tcuNeeded = ParamUtil.getRequestString(httpServletRequest, "tcuNeed");
        String tcuDate = ParamUtil.getRequestString(httpServletRequest, "tcuDate");
        String enforcement = ParamUtil.getRequestString(httpServletRequest, "engageEnforcement");
        String enforcementRemarks = ParamUtil.getRequestString(httpServletRequest, "enforcementRemarks");
        String periods = ParamUtil.getRequestString(httpServletRequest, "periods");
        if (OTHERS.equals(periods)) {
            if (StringUtil.isEmpty(chrono)) {
                errorMap.put("chronoUnit", "ERR0009");
            } else if (StringUtil.isEmpty(number)) {
                errorMap.put("recomInNumber", "ERR0009");
            } else {
                try {
                    Integer.parseInt(number);
                } catch (NumberFormatException e) {
                    errorMap.put("recomInNumber", "ERR003");
                }
            }
        }
        if (!StringUtil.isEmpty(remarks)) {
            int length = remarks.length();
            if (length > 4000) {
                errorMap.put("remarks", "remarks must be less than 4000");
            }
        }
        if(!StringUtil.isEmpty(tcuNeeded)&&StringUtil.isEmpty(tcuDate)){
            errorMap.put("tcuDate", "ERR0009");
        }
        if(!StringUtil.isEmpty(enforcement)&&StringUtil.isEmpty(enforcementRemarks)){
            errorMap.put("enforcementRemarks", "ERR0009");
        }
            return errorMap;
    }
}
