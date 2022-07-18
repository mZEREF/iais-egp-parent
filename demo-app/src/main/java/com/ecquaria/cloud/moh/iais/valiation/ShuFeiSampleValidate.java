package com.ecquaria.cloud.moh.iais.valiation;

import com.ecquaria.cloud.moh.iais.common.dto.sample.*;
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.*;

import javax.servlet.http.*;
import java.util.*;

public class ShuFeiSampleValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        ShuFeiCreateSampleDto shuFeiCreateSampleDto = (ShuFeiCreateSampleDto) ParamUtil.getSessionAttr(request,"ShuFeiCreateSampleDto");
        if (shuFeiCreateSampleDto == null || StringUtil.isEmpty(shuFeiCreateSampleDto.getMobileNo()))
            return errMap;
        return errMap;
    }
}
