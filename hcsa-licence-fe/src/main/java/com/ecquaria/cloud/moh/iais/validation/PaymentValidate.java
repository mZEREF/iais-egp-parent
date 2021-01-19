package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class PaymentValidate implements CustomizeValidator {

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String ,String> map= IaisCommonUtils.genNewHashMap();
        String payMethod = ParamUtil.getString(request, "payMethod");
        if(StringUtil.isEmpty(payMethod)){
            map.put("payMethod","The field is mandatory.");
        }
        return map;
    }
}
