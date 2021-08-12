package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
    @Autowired
    IntranetUserService intranetUserServicee;

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        FeUserDto dto = (FeUserDto) ParamUtil.getSessionAttr(request, "inter_user_attr");
        if (Objects.isNull(dto)) {
            return map;
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
        if (dto.getIdentityNo() != null && !StringUtil.isEmpty(dto.getIdentityNo())) {
            boolean b;
            if (OrganizationConstants.ID_TYPE_FIN.equals(dto.getIdType())) {
                b = SgNoValidator.validateFin(dto.getIdentityNo());
            } else {
                b = SgNoValidator.validateNric(dto.getIdentityNo());
            }
            if (!b) {
                map.put("identityNo", MessageUtil.getMessageDesc("RFC_ERR0012"));
            }
        }


            /*if (StringUtil.isEmpty(dto.getOrgId())) {
                map.put("organizationId", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            }*/

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
        String isNeedValidateField = (String) ParamUtil.getRequestAttr(request, "isNeedValidateField");
        if (IaisEGPConstant.YES.equals(isNeedValidateField)) {
            if (dto.getIdentityNo() != null) {
                String idType = IaisEGPHelper.checkIdentityNoType(dto.getIdentityNo());
                if (!StringUtil.isEmpty(dto.getIdentityNo()) && !StringUtil.isEmpty(idType)) {
                    List<FeUserDto> userList = intranetUserService.getUserListByNricAndIdType(dto.getIdentityNo(), idType);
                    if (dto.getId() == null) { // create
                        Optional<FeUserDto> user = userList.stream()
                                .filter(feUserDto -> !AppConsts.COMMON_STATUS_DELETED.equals(feUserDto.getStatus()))
                                .findAny();
                        if (user.isPresent()) {
                            map.put("identityNo", MessageUtil.getMessageDesc("USER_ERR015"));
                        }
                    } else { // edit
                        Optional<FeUserDto> user = userList.stream()
                                .filter(feUserDto -> !AppConsts.COMMON_STATUS_DELETED.equals(feUserDto.getStatus())
                                        && !dto.getId().equals(feUserDto.getId()))
                                .findAny();
                        if (user.isPresent()) {
                            map.put("identityNo", MessageUtil.getMessageDesc("USER_ERR015"));
                        }
                    }
                }

            }
        }
        return map;
    }

}