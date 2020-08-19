package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.UserConstants;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * AdminValidator
 *
 * @author Jinhua
 * @date 2019/12/13 9:27
 */
@Component
public class UserValidator implements CustomizeValidator {
    @Autowired
    OrgUserManageService orgUserManageService;
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        FeUserDto dto = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);
        String isNeedValidateField = (String) ParamUtil.getRequestAttr(request, UserConstants.IS_NEED_VALIDATE_FIELD);
            if (dto.getIdentityNo() != null && !StringUtil.isEmpty(dto.getIdentityNo())) {
                boolean b = SgNoValidator.validateFin(dto.getIdentityNo());
                boolean b1 = SgNoValidator.validateNric(dto.getIdentityNo());
                if (!(b || b1)) {
                    map.put("identityNo", MessageUtil.getMessageDesc("GENERAL_ERR0008"));
                }
            }

            if (dto.getEmail() != null && !ValidationUtils.isEmail(dto.getEmail())) {
                map.put("email", MessageUtil.getMessageDesc("GENERAL_ERR0014"));
            }

            if(dto.getMobileNo() != null && !StringUtil.isEmpty(dto.getMobileNo())){
                if (!dto.getMobileNo().matches("^[8|9][0-9]{7}$")) {
                    map.put("mobileNo", MessageUtil.getMessageDesc("GENERAL_ERR0007"));
                }
            }

            if(dto.getOfficeTelNo() != null && !StringUtil.isEmpty(dto.getOfficeTelNo())) {
                if (!dto.getOfficeTelNo().matches("^[6][0-9]{7}$")) {
                    map.put("officeTelNo", MessageUtil.getMessageDesc("GENERAL_ERR0015"));
                }
            }

            if (IaisEGPConstant.YES.equals(isNeedValidateField)){
                if(dto.getId() == null && dto.getIdentityNo() != null){
                    String idType = IaisEGPHelper.checkIdentityNoType(dto.getIdentityNo());
                    FeUserDto feUserDto = orgUserManageService.getFeUserAccountByNricAndType(dto.getIdentityNo(), idType);
                    if(feUserDto != null){
                        map.put("identityNo", MessageUtil.getMessageDesc("GENERAL_ERR0035"));
                    }
                }
            }
        return map;
    }
}