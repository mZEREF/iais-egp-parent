package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: yichen
 * @date time:12/30/2019 9:48 AM
 * @description:
 */

public class BlackedOutDateValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String,String> map = IaisCommonUtils.genNewHashMap();
        //List<AppGrpPremisesDto> list  = (List<AppGrpPremisesDto>) ParamUtil.getRequestAttr(request, "valPremiseList");
        return null;
    }
}
