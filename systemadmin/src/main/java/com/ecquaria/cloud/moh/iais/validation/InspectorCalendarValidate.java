package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.InspectorCalendarQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author yichen
 * @Date:2021/1/5
 */

public class InspectorCalendarValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        InspectorCalendarQueryDto icq = (InspectorCalendarQueryDto)
                ParamUtil.getRequestAttr(httpServletRequest, "inspectorCalendarQueryDto");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (icq == null){
            return errorMap;
        }

        if (icq.getUserBlockDateStart() != null && icq.getUserBlockDateEnd() != null){
            if (icq.getUserBlockDateStart().after(icq.getUserBlockDateEnd())){
                errorMap.put("userBlockDateStart", "OAPPT_ERR008");
            }
        }

        return errorMap;
    }
}
