package com.ecquaria.cloud.moh.iais.validation;

/*
 *author: yichen
 *date time:12/5/2019 10:12 AM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
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

        String operationType = (String) ParamUtil.getSessionAttr(httpServletRequest, "operationType");
        if (!"doEdit".equals(operationType)){
            if(StringUtils.isEmpty(common) && StringUtils.isEmpty(svcName)){
                errMap.put("error", "You need to select one of the configuration types!");
                return errMap;
            }
        }

        if (!StringUtils.isEmpty(common) && (!StringUtils.isEmpty(svcName) || !StringUtils.isEmpty(svcSubType))){
            errMap.put("error", "You can only choose between common and service!");
            return errMap;
        }

        if (StringUtils.isEmpty(module) || StringUtils.isEmpty(type)){
            errMap.put("error", "Module or type can not be null!");
            return errMap;
        }

        return errMap;
    }
}
