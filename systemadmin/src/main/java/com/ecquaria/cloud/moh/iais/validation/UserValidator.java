package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
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
    IntranetUserService intranetUserService;
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        FeUserDto dto = (FeUserDto) ParamUtil.getSessionAttr(request, "inter_user_attr");
                if (dto.getIdentityNo() != null && !StringUtil.isEmpty(dto.getIdentityNo())) {
                boolean b;
                if(OrganizationConstants.ID_TYPE_FIN.equals(dto.getIdType())){
                    b = SgNoValidator.validateFin(dto.getIdentityNo());
                }else{
                    b = SgNoValidator.validateNric(dto.getIdentityNo());
                }
                if (!b) {
                    map.put("identityNo", MessageUtil.getMessageDesc("USER_ERR014"));
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

            if(dto.getId() == null && dto.getIdentityNo() != null){
                String idType = IaisEGPHelper.checkIdentityNoType(dto.getIdentityNo());
                if(!StringUtil.isEmpty(dto.getIdentityNo()) && !StringUtil.isEmpty(idType)){
                    FeUserDto feUserDto = intranetUserService.getFeUserAccountByNricAndType(dto.getIdentityNo(), idType);
                    if(feUserDto != null){
                        map.put("idNo", MessageUtil.getMessageDesc("USER_ERR015"));
                    }
                }

            }
        return map;
    }
}