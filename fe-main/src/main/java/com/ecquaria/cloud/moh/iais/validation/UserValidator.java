package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * AdminValidator
 *
 * @author Jinhua
 * @date 2019/12/13 9:27
 */
public class UserValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        FeUserDto dto = (FeUserDto) ParamUtil.getRequestAttr(request, "user");
        if (dto != null) {
            String idNo = dto.getIdNo();
            String emailAddr = dto.getEmailAddr();
            String designation = dto.getDesignation();
            String idType = dto.getIdType();
            String mobileNo = dto.getMobileNo();
            String name = dto.getName();
            String officeNo = dto.getOfficeNo();
            String salutation = dto.getSalutation();
            if(StringUtil.isEmpty(name)){
                map.put("name","cannot be blank");
            }
            if(StringUtil.isEmpty(idNo)){
                map.put("idNo","cannot be blank");
            }
            if(StringUtil.isEmpty(salutation)){
                map.put("salutation","cannot be blank");
            }
            if(StringUtil.isEmpty(designation)){
                map.put("designation","cannot be blank");
            }
            if (!StringUtil.isEmpty(idNo)) {
                boolean b = SgNoValidator.validateFin(idNo);
                boolean b1 = SgNoValidator.validateNric(idNo);
                if (!(b || b1)) {
                    map.put("idNo", "Please key in a valid NRIC/FIN");
                }
            } else {
                map.put("idNo", "cannot be blank");
            }
            if(!StringUtil.isEmpty(mobileNo)){
                if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                    map.put("mobileNo", "Please key in a valid mobile number");
                }
            }else {
                map.put("mobileNo", "cannot be blank");
            }
            if(!StringUtil.isEmpty(emailAddr)) {
                if (!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                    map.put("emailAddr", "Please key in a valid email address");
                }
            }else {
                map.put("emailAddr", "cannot be blank");
            }
            if(!StringUtil.isEmpty(officeNo)) {
                if (!officeNo.matches("^[6][0-9]{7}$")) {
                    map.put("officeTelNo", "Please key in a valid phone number");
                }
            }else {
                map.put("officeTelNo", "cannot be blank");
            }
        }
        return map;
    }
}