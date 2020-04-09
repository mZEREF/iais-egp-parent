package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
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
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        FeUserDto dto = (FeUserDto) ParamUtil.getSessionAttr(request, "user");
            if (dto.getIdentityNo() != null && !StringUtil.isEmpty(dto.getIdentityNo())) {
                boolean b = SgNoValidator.validateFin(dto.getIdentityNo());
                boolean b1 = SgNoValidator.validateNric(dto.getIdentityNo());
                if (!(b || b1)) {
                    map.put("identityNo", "Please key in a valid NRIC/FIN");
                }
            }
            if (dto.getEmail() != null && !StringUtil.isEmpty(dto.getEmail())) {
                if (!dto.getEmail().matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                    map.put("email", "Please key in a valid email address");
                }
            }
            if(dto.getMobileNo() != null && !StringUtil.isEmpty(dto.getMobileNo())){
                if (!dto.getMobileNo().matches("^[8|9][0-9]{7}$")) {
                    map.put("mobileNo", "Please key in a valid mobile number");
                }
            }
            if(dto.getOfficeTelNo() != null && !StringUtil.isEmpty(dto.getOfficeTelNo())) {
                if (!dto.getOfficeTelNo().matches("^[6][0-9]{7}$")) {
                    map.put("officeTelNo", "Please key in a valid phone number");
                }
            }
        return map;
    }
}