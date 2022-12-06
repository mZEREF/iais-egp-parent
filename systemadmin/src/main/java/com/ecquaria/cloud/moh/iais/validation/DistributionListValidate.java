package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListWebDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
        Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
        if(StringUtil.isEmpty(distribution.getDisname())){
            errMap.put("disname", IaisEGPConstant.ERR_MANDATORY);
        }else if(distribution.getDisname().length()>500){
            repMap.put("number","500");
            repMap.put("fieldNo","Distribution Name");
            errMap.put("disname", MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap));
        }
        if(StringUtil.isEmpty(distribution.getService())){
            errMap.put("service", IaisEGPConstant.ERR_MANDATORY);
        }
        if(StringUtil.isEmpty(distribution.getRole())){
            errMap.put("role", IaisEGPConstant.ERR_MANDATORY);
        }
        if(StringUtil.isEmpty(distribution.getMode())){
            errMap.put("mode", IaisEGPConstant.ERR_MANDATORY);
        }else if(distribution.getMode().length()>500){
            repMap.put("number","500");
            repMap.put("fieldNo","Mode of Delivery");
            errMap.put("mode", MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap));
        }
        if(EMAIL.equals(distribution.getMode())){
            if(distribution.getEmailAddress() != null && distribution.getEmailAddress().size()>0){
                if(repeatList(distribution.getEmailAddress())){
                    errMap.put("addr", MessageUtil.replaceMessage("EMM_ERR009","email address(es)","mode"));
                }
            }
            if(distribution.getEmailAddress() != null){
                for (String item :distribution.getEmailAddress()
                ) {
                    if(!ValidationUtils.isEmail(item)){
                        errMap.put("addr", MessageUtil.getMessageDesc("GENERAL_ERR0014"));
                    }
                }
            }
        }else{
            if(distribution.getEmailAddress() != null && distribution.getEmailAddress().size()>0){
                if(repeatList(distribution.getEmailAddress())){
                    errMap.put("mobileNo", MessageUtil.replaceMessage("EMM_ERR009","mobiles","mode"));
                }
            }
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

    private boolean repeatList(List<String> list){
        Map<String,String> repeatMap = IaisCommonUtils.genNewHashMap();
        for (String item:list) {
            if(StringUtil.isEmpty(repeatMap.get(item))){
                repeatMap.put(item,item);
            }else{
                return true;
            }
        }
        return false;
    }
}
