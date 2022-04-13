package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MasterCodeConstants;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class MasterCodeValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        MasterCodeDto dto = (MasterCodeDto) ParamUtil.getRequestAttr(httpServletRequest,
                MasterCodeConstants.MASTERCODE_USER_DTO_ATTR);
        if (dto == null ) {
            return errMap;
        }
        if (!StringUtil.isEmpty(dto.getId())) {
            errMap.put("masterCodeId", "Duplicate MasterCode");
            return errMap;
        }
        return errMap;
    }
}
