package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListWebDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: guyin
 * @Program: iais-egp
 * @Create: 2020-03-09 12:41
 **/
public class DistributionListValidate implements CustomizeValidator {

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        DistributionListWebDto distribution = (DistributionListWebDto) ParamUtil.getSessionAttr(request, "distribution");
        if(distribution.getEmailAddress() != null){
            for (String item :distribution.getEmailAddress()
            ) {
                if(!item.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
                    errMap.put("addr","Please key in a valid email address");
                }
            }
        }
        return errMap;
    }
}
