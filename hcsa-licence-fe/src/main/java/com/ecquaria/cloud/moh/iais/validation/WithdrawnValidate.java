package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2020-02-17 15:27
 **/
public class WithdrawnValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errorMap = new HashMap<>(34);
        String withdrawnReason = ParamUtil.getRequestString(httpServletRequest, "withdrawnReason");
        if(StringUtil.isEmpty(withdrawnReason)){
            errorMap.put("withdrawnReason", "ERR0009");
        }
        if("Others".equals(withdrawnReason)){
            String withdrawnRemarks = ParamUtil.getRequestString(httpServletRequest, "withdrawnRemarks");
            if(StringUtil.isEmpty(withdrawnRemarks)){
                errorMap.put("withdrawnRemarks", "ERR0009");
            }
        }
        return errorMap;
    }
}
