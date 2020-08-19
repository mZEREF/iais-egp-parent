package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListWebDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: guyin
 * @Program: iais-egp
 * @Create: 2020-03-09 12:41
 **/
public class DistributionListValidate implements CustomizeValidator {

    private static final String EMAIL = "Email";
    private static final String SMS = "SMS";

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        DistributionListWebDto distribution = (DistributionListWebDto) ParamUtil.getSessionAttr(request, "distribution");
        if(EMAIL.equals(distribution.getMode())){
            if(distribution.getEmailAddress() != null){
                for (String item :distribution.getEmailAddress()
                ) {
                    if(!ValidationUtils.isEmail(item)){
                        errMap.put("addr", MessageUtil.getMessageDesc("USER_ERR005"));
                    }
                }
            }
        }else{
            if(distribution.getEmailAddress() != null){
                for (String item :distribution.getEmailAddress()
                ) {
                    if (!item.matches("^[8|9][0-9]{7}$")) {
                        errMap.put("mobileNo", MessageUtil.getMessageDesc("GENERAL_ERR0007"));
                    }
                }
            }
        }

        return errMap;
    }
}
