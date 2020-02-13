package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/2/11 11:20
 */
public class CessationValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errorMap = new HashMap<>(34);
        String cessationReason = ParamUtil.getRequestString(httpServletRequest, "cessationReason");
        String otherReason = ParamUtil.getRequestString(httpServletRequest, "otherReason");
        String patRadio = ParamUtil.getRequestString(httpServletRequest, "patRadio");
        String patientSelect = ParamUtil.getRequestString(httpServletRequest, "patientSelect");
        String patNoRemarks = ParamUtil.getRequestString(httpServletRequest, "patNoRemarks");
        String patHciName = ParamUtil.getRequestString(httpServletRequest, "patHciName");
        String patRegNo = ParamUtil.getRequestString(httpServletRequest, "patRegNo");
        String patOthers = ParamUtil.getRequestString(httpServletRequest, "patOthers");
        String whichTodo = ParamUtil.getRequestString(httpServletRequest, "whichTodo");
        if("Others".equals(cessationReason)){
            if(StringUtil.isEmpty(otherReason)){
                errorMap.put("otherReason", "ERR0009");
            }
        }else if("hciName".equals(patientSelect)){
            if(StringUtil.isEmpty(patHciName)){
                errorMap.put("patHciName", "ERR0009");
            }
        }else if("regNo".equals(patientSelect)){
            if(StringUtil.isEmpty(patRegNo)){
                errorMap.put("patRegNo", "ERR0009");
            }
        }else if("Others".equals(patientSelect)){
            if(StringUtil.isEmpty(patOthers)){
                errorMap.put("patOthers", "ERR0009");
            }
        }
        return errorMap;
    }
}
