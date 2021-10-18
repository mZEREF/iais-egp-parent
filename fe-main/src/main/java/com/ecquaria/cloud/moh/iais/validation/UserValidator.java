package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.UserConstants;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import com.ecquaria.cloud.moh.iais.service.client.FeUserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    @Autowired
    FeUserClient feUserClient;

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        FeUserDto dto = (FeUserDto) ParamUtil.getSessionAttr(request, UserConstants.SESSION_USER_DTO);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if (Objects.isNull(dto)) {
            return map;
        }
        //don't in active myself
        if(loginContext != null) {
            String userId = loginContext.getUserId();
            String editId = dto.getId();
            if(!StringUtil.isEmpty(userId) && userId.equals(editId)) {
                if(AppConsts.COMMON_STATUS_IACTIVE.equals(dto.getStatus())) {
                    map.put("active", "You cannot set yourself to inactive");
                }
            }
        }
        // designation
        if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(dto.getDesignation())) {
            if (StringUtil.isEmpty(dto.getDesignationOther())) {
                map.put("designationOther", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            } else if (dto.getDesignationOther().length() > 100) {//WRNTYPE002
                Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                repMap.put("field", "Other Designation");
                repMap.put("maxlength", "100");
                map.put("identityNo", MessageUtil.getMessageDesc("WRNTYPE002"));
            }
        }

        String isNeedValidateField = (String) ParamUtil.getRequestAttr(request, UserConstants.IS_NEED_VALIDATE_FIELD);
        if (dto.getIdentityNo() != null && !StringUtil.isEmpty(dto.getIdentityNo())) {
            boolean b;
            if (OrganizationConstants.ID_TYPE_FIN.equals(dto.getIdType())
                    || "FIN".equalsIgnoreCase(dto.getIdType())) {
                b = SgNoValidator.validateFin(dto.getIdentityNo());
            } else {
                b = SgNoValidator.validateNric(dto.getIdentityNo());
            }
            if (!b) {
                map.put("identityNo", MessageUtil.getMessageDesc("USER_ERR014"));
            }
        }

        if (RoleConsts.USER_ROLE_ORG_USER.equals(dto.getUserRole())) {
            List<FeUserDto> feUserDtoList = feUserClient.getAdminAccountByOrgId(dto.getOrgId()).getEntity();
            if (!StringUtil.isEmpty(dto.getId()) && feUserDtoList.size() == 1 && dto.getId().equals(
                    feUserDtoList.get(0).getId())) {
                map.put("userRole", MessageUtil.getMessageDesc("USER_ERR017"));
            }
        }

        if (dto.getEmail() != null && !ValidationUtils.isEmail(dto.getEmail())) {
            map.put("email", MessageUtil.getMessageDesc("GENERAL_ERR0014"));
        }

        if (dto.getMobileNo() != null && !StringUtil.isEmpty(dto.getMobileNo())) {
            if (!dto.getMobileNo().matches("^[8|9][0-9]{7}$")) {
                map.put("mobileNo", MessageUtil.getMessageDesc("GENERAL_ERR0007"));
            }
        }

        if (dto.getOfficeTelNo() != null && !StringUtil.isEmpty(dto.getOfficeTelNo())) {
            if (!dto.getOfficeTelNo().matches(IaisEGPConstant.OFFICE_TELNO_MATCH)) {
                map.put("officeTelNo", MessageUtil.getMessageDesc("GENERAL_ERR0015"));
            }
        }

        if (IaisEGPConstant.YES.equals(isNeedValidateField)) {
            if (dto.getId() == null && dto.getIdentityNo() != null) {
                String idType = IaisEGPHelper.checkIdentityNoType(dto.getIdentityNo());
                if (!StringUtil.isEmpty(dto.getIdentityNo()) && !StringUtil.isEmpty(idType)) {
                    FeUserDto feUserDto = orgUserManageService.getFeUserAccountByNricAndType(dto.getIdentityNo(), idType, dto.getUenNo());
                    if (feUserDto != null) {
                        map.put("idNo", MessageUtil.getMessageDesc("USER_ERR015"));
                    }
                }

            }
        }
        return map;
    }

}