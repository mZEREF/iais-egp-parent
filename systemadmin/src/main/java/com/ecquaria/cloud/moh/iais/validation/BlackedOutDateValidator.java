package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yichen
 * @date time:12/30/2019 9:48 AM
 * @description:
 */

public class BlackedOutDateValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String,String> map =new HashMap<>();
        //List<AppGrpPremisesDto> list  = (List<AppGrpPremisesDto>) ParamUtil.getRequestAttr(request, "valPremiseList");
        return null;
    }
}
