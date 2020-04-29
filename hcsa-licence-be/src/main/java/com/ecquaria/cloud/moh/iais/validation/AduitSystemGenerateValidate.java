package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: jiahao
 * @Date: 2020/2/26 17:16
 */
public class AduitSystemGenerateValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String postcode = ParamUtil.getString(request,"postcode");
        String genNum = ParamUtil.getString(request,"genNum");
        if( !StringUtil.isEmpty(postcode) && !StringUtil.stringIsFewDecimal(postcode,null)){
            errMap.put("postcode","GENERAL_ERR0002");
        }

        if( !StringUtil.isEmpty(genNum) && !StringUtil.stringIsFewDecimal(genNum,null)){
            errMap.put("genNum","GENERAL_ERR0002");
        }
        return errMap;
    }
}
