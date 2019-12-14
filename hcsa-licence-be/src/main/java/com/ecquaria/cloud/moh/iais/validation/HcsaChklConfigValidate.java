package com.ecquaria.cloud.moh.iais.validation;

/*
 *author: yichen
 *date time:12/5/2019 10:12 AM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class HcsaChklConfigValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errMap = new HashMap<>();
        String common = ParamUtil.getString(httpServletRequest, HcsaChecklistConstants.PARAM_CONFIG_COMMON);
        String module = ParamUtil.getString(httpServletRequest, HcsaChecklistConstants.PARAM_CONFIG_MODULE);
        String type = ParamUtil.getString(httpServletRequest, HcsaChecklistConstants.PARAM_CONFIG_TYPE);
        String svcName = ParamUtil.getString(httpServletRequest, HcsaChecklistConstants.PARAM_CONFIG_SERVICE);
        String svcSubType = ParamUtil.getString(httpServletRequest, HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE);
        String eftStartDate = ParamUtil.getString(httpServletRequest, HcsaChecklistConstants.PARAM_CONFIG_EFFECTIVE_START_DATE);
        String eftEndDate = ParamUtil.getString(httpServletRequest, HcsaChecklistConstants.PARAM_CONFIG_EFFECTIVE_END_DATE);

        String operationType = (String) ParamUtil.getSessionAttr(httpServletRequest, "operationType");
        if (!"doEdit".equals(operationType)){
            if(StringUtils.isEmpty(common) && StringUtils.isEmpty(svcName)){
                errMap.put("configCustomValidation", "You need to select one of the configuration types!");
                return errMap;
            }
        }

        if (!StringUtils.isEmpty(common) && (!StringUtils.isEmpty(svcName) || !StringUtils.isEmpty(svcSubType))){
            errMap.put("configCustomValidation", "You can only choose between common and service.");
            return errMap;
        }

        if (StringUtils.isEmpty(module) || StringUtils.isEmpty(type)){
            errMap.put("configCustomValidation", "Module or type can not be null.");
            return errMap;
        }

        if (StringUtils.isEmpty(eftStartDate)){
            errMap.put("configCustomValidation", "Please select effective date.");
            return errMap;
        }

        if (StringUtils.isEmpty(eftEndDate)){
            errMap.put("configCustomValidation", "Please select effective end date.");
            return errMap;
        }

        //I don't want to use date.pare to have it throw an exception
        String[] startDateStr = eftStartDate.split("/");
        String[] eftEndDateStr = eftEndDate.split("/");
        String nStr = "";
        String eStr = "";

        int len = Math.min(startDateStr.length, eftEndDateStr.length);
        for (int i = len - 1; i >= 0; i--){
            nStr += startDateStr[i];
            eStr += eftEndDateStr[i];
        }

        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startDate = LocalDate.parse(nStr, formatter);
        LocalDate endDate = LocalDate.parse(eStr, formatter);

        int comparatorValue = endDate.compareTo(startDate);
        if (comparatorValue < 0){
            errMap.put("configCustomValidation", "Effective End Date must be after Effective Start Date.");
            return errMap;
        }

        return errMap;
    }
}
