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
        int size = (int)ParamUtil.getSessionAttr(httpServletRequest, "size");
        for (int i = 1; i < size; i++) {
            for (int j = 1; j < size; j++) {
                String effectiveDateStr = ParamUtil.getRequestString(httpServletRequest, i+"effectiveDate"+j);
                if(StringUtil.isEmpty(effectiveDateStr)){
                    errorMap.put(i+"otherReason"+j, "ERR0009");
                }
                String reason = ParamUtil.getRequestString(httpServletRequest, i+"reason"+j);
                if(StringUtil.isEmpty(reason)){
                    errorMap.put(i+"reason"+j, "ERR0009");
                }
                String patRadio = ParamUtil.getRequestString(httpServletRequest, i+"patRadio"+j);
                String readInfo = ParamUtil.getRequestString(httpServletRequest, "readInfo");
                if(StringUtil.isEmpty(readInfo)){
                    errorMap.put(i+"readInfo"+j, "ERR0009");
                }

                String cessationReason = ParamUtil.getRequestString(httpServletRequest, i+"cessationReason"+j);
                String otherReason = ParamUtil.getRequestString(httpServletRequest, i+"otherReason"+j);
                String patientSelect = ParamUtil.getRequestString(httpServletRequest, i+"patientSelect"+j);
                String patNoRemarks = ParamUtil.getRequestString(httpServletRequest, i+"patNoRemarks"+j);
                String patHciName = ParamUtil.getRequestString(httpServletRequest, i+"patHciName"+j);
                String patRegNo = ParamUtil.getRequestString(httpServletRequest, i+"patRegNo"+j);
                String patOthers = ParamUtil.getRequestString(httpServletRequest, i+"patOthers"+j);
                if("OtherReasons".equals(cessationReason)){
                    if(StringUtil.isEmpty(otherReason)){
                        errorMap.put(i+"otherReason"+j, "ERR0009");
                    }
                }
                if("yes".equals(patRadio)){
                    if(StringUtil.isEmpty(patientSelect)){
                        errorMap.put(i+"patientSelect"+j, "ERR0009");
                    }else {
                        if("hciName".equals(patientSelect)){
                            if(StringUtil.isEmpty(patHciName)){
                                errorMap.put(i+"patHciName"+j, "ERR0009");
                            }
                        }else if("regNo".equals(patientSelect)){
                            if(StringUtil.isEmpty(patRegNo)){
                                errorMap.put(i+"patRegNo"+j, "ERR0009");
                            }
                        }else if("Others".equals(patientSelect)){
                            if(StringUtil.isEmpty(patOthers)){
                                errorMap.put(i+"patOthers"+j, "ERR0009");
                            }
                        }
                    }
                }else if("no".equals(patRadio)) {
                    if(StringUtil.isEmpty(patNoRemarks)){
                        errorMap.put(i+"patNoRemarks"+j, "ERR0009");
                    }
                }
            }
        }
        return errorMap;
    }
}
