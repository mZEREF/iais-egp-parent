package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.controller.MasterCodeDelegator;
import com.ecquaria.cloud.moh.iais.dto.MasterCodeDto;
import sg.gov.moh.iais.common.utils.ParamUtil;
import sg.gov.moh.iais.common.utils.StringUtil;
import sg.gov.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class MasterCodeValidator implements CustomizeValidator {

    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errMap = new HashMap<>();
        MasterCodeDto dto = (MasterCodeDto) ParamUtil.getSessionAttr(httpServletRequest,
                MasterCodeDelegator.MASTERCODE_USER_DTO_ATTR);
        if (dto == null || StringUtil.isEmpty(dto.getMasterCodeId()))
            return errMap;

        if (dto != null && !StringUtil.isEmpty(dto.getMasterCodeId())) {
            errMap.put("masterCodeId", "Duplicate MasterCode");
        }

        return errMap;
    }
}
