package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.organization.FeAdminDto;
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
 * @author gy
 * @date 2019/12/13 9:27
 */
public class AdminValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        FeAdminDto dto = (FeAdminDto) ParamUtil.getRequestAttr(request, "account");
        if (dto != null) {
            String idNo = dto.getIdNo();
            String emailAddr = dto.getEmailAddr();
            String fristName = dto.getFirstName();
            String lastName = dto.getLastName();
            String salutation = dto.getSalutation();
            if (StringUtil.isEmpty(fristName)) {
                map.put("fristName", "fristName cannot be blank");
            }
            if (StringUtil.isEmpty(lastName)) {
                map.put("lastName", "lastName cannot be blank");
            }
            if (StringUtil.isEmpty(salutation)) {
                map.put("salutation", "salutation should be chosed");
            }
            if (!StringUtil.isEmpty(idNo)) {
                boolean b = SgNoValidator.validateFin(idNo);
                boolean b1 = SgNoValidator.validateNric(idNo);
                if (!(b || b1)) {
                    map.put("NRICFIN", "Please key in a valid NRIC/FIN");
                }
            } else {
                map.put("NRICFIN", "cannot be blank");
            }
            if (!StringUtil.isEmpty(emailAddr)) {
                if (!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                    map.put("emailAddr", "Please key in a valid email address");
                }
            } else {
                map.put("emailAddr", "cannot be blank");
            }
        }
        return map;
    }
}