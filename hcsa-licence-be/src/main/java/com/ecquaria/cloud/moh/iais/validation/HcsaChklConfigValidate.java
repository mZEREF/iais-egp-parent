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
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class HcsaChklConfigValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String eftStartDate = ParamUtil.getString(httpServletRequest, HcsaChecklistConstants.PARAM_CONFIG_EFFECTIVE_START_DATE);
        String eftEndDate = ParamUtil.getString(httpServletRequest, HcsaChecklistConstants.PARAM_CONFIG_EFFECTIVE_END_DATE);

        if (StringUtils.isEmpty(eftStartDate)){
            errMap.put("eftStartDate", MessageUtil.getMessageDesc(MessageCodeKey.ERR0010));
            return errMap;
        }

        if (StringUtils.isEmpty(eftEndDate)){
            errMap.put("eftEndDate", MessageUtil.getMessageDesc(MessageCodeKey.ERR0010));
            return errMap;
        }

        //I don't want to use date.pare to have it throw an exception
        String[] startDateStr = eftStartDate.split("/");
        String[] eftEndDateStr = eftEndDate.split("/");
        StringBuilder nStr = new StringBuilder();
        StringBuilder eStr = new StringBuilder();

        int len = Math.min(startDateStr.length, eftEndDateStr.length);
        for (int i = len - 1; i >= 0; i--){
            nStr.append(startDateStr[i]) ;
            eStr.append(eftEndDateStr[i]);
        }

        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startDate = LocalDate.parse(nStr, formatter);
        LocalDate endDate = LocalDate.parse(eStr, formatter);

        int comparatorValue = endDate.compareTo(startDate);
        if (comparatorValue < 0 || comparatorValue == 0){
            errMap.put("configCustomValidation", MessageUtil.getMessageDesc(MessageCodeKey.CHKL_ERR013));
            return errMap;
        }

        return errMap;
    }
}
