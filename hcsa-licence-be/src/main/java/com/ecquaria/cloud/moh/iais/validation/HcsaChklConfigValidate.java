package com.ecquaria.cloud.moh.iais.validation;

/*
 *author: yichen
 *date time:12/5/2019 10:12 AM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageCodeKey;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

public class HcsaChklConfigValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String eftStartDate = ParamUtil.getString(httpServletRequest, HcsaChecklistConstants.PARAM_CONFIG_EFFECTIVE_START_DATE);
        String eftEndDate = ParamUtil.getString(httpServletRequest, HcsaChecklistConstants.PARAM_CONFIG_EFFECTIVE_END_DATE);

        Date sDate = IaisEGPHelper.parseToDate(eftStartDate);
        Date eDate = IaisEGPHelper.parseToDate(eftEndDate);

        if (IaisEGPHelper.isAfterDate(eDate, sDate)){
            errMap.put("configCustomValidation", MessageUtil.getMessageDesc(MessageCodeKey.CHKL_ERR013));
            return errMap;
        }

        return errMap;
    }
}
