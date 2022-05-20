package com.ecquaria.cloud.moh.iais.validation;


import com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description SubLicensee Validator
 * @Auther chenlei on 8/2/2021.
 */
public class SubLicenseeValidator implements CustomizeValidator {

    private static final String MANDATORY_MSG = MessageUtil.getMessageDesc("GENERAL_ERR0006");

    @Override
    public Map<String, String> validate(Object obj, HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (!(obj instanceof SubLicenseeDto)) {
            return errorMap;
        }
        SubLicenseeDto subLicenseeDto = (SubLicenseeDto) obj;

        String idType = subLicenseeDto.getIdType();
        String idNumber = subLicenseeDto.getIdNumber();
        String nationality = subLicenseeDto.getNationality();
        String licenseeType = subLicenseeDto.getLicenseeType();
        if (!OrganizationConstants.LICENSEE_SUB_TYPE_COMPANY.equals(licenseeType)) {
            if (StringUtil.isEmpty(idNumber)) {
                errorMap.put("idNumber", MANDATORY_MSG);
            } else if (!SgNoValidator.validateIdNo(idType, idNumber)) {
                errorMap.put("idNumber", MessageUtil.getMessageDesc("RFC_ERR0012"));
            }
        }

        if (OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL.equals(licenseeType) || StringUtil.isEmpty(licenseeType)) {
            String assignSelect = subLicenseeDto.getAssignSelect();
            if (StringUtil.isEmpty(assignSelect) || "-1".equals(assignSelect)) {
                errorMap.put("assignSelect", MANDATORY_MSG);
            }

            if (StringUtil.isEmpty(idType)) {
                errorMap.put("idType", MANDATORY_MSG);
            }
        }
        String telephoneNoErr=MessageUtil.getMessageDesc("GENERAL_ERR0015");
        if (OrganizationConstants.LICENSEE_SUB_TYPE_COMPANY.equals(licenseeType)) {
            String telephoneNo = subLicenseeDto.getTelephoneNo();
            if (telephoneNo != null && !CommonValidator.isTelephoneNo(telephoneNo)) {
                errorMap.put("telephoneNo", telephoneNoErr);
            }
        }
        if (OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL.equals(licenseeType)) {
            if (OrganizationConstants.ID_TYPE_PASSPORT.equals(idType) && StringUtil.isEmpty(nationality)) {
                errorMap.put("nationality", MANDATORY_MSG);
            }
            String mobileNo = subLicenseeDto.getTelephoneNo();
            if (mobileNo != null && !CommonValidator.isMobile(mobileNo)) {
                errorMap.put("telephoneNo", telephoneNoErr);
            }
        }
        if (OrganizationConstants.LICENSEE_SUB_TYPE_SOLO.equals(licenseeType)) {
            String telephoneNo = subLicenseeDto.getTelephoneNo();
            if (telephoneNo != null && !CommonValidator.isMobile(telephoneNo)) {
                errorMap.put("telephoneNo", telephoneNoErr);
            }
        }

        if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(subLicenseeDto.getAddrType())) {
            String blkNo = subLicenseeDto.getBlkNo();
            String floorNo = subLicenseeDto.getFloorNo();
            String unitNo = subLicenseeDto.getUnitNo();
            if (StringUtil.isEmpty(blkNo)) {
                errorMap.put("blkNo", MANDATORY_MSG);
            }
            if (StringUtil.isEmpty(floorNo)) {
                errorMap.put("floorNo", MANDATORY_MSG);
            }
            if (StringUtil.isEmpty(unitNo)) {
                errorMap.put("unitNo", MANDATORY_MSG);
            }
        }

        if (errorMap.isEmpty() && IaisEGPConstant.ASSIGN_SELECT_ADD_NEW.equals(subLicenseeDto.getAssignSelect())
                && OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL.equals(licenseeType)) {
            AppSubmissionDto appSubmissionDto = (AppSubmissionDto)ParamUtil.getSessionAttr(request, NewApplicationDelegator.APPSUBMISSIONDTO);
            boolean needVal = appSubmissionDto != null &&
                    ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
            Map<String, SubLicenseeDto> psnMap = (Map<String, SubLicenseeDto>) ParamUtil.getSessionAttr(request,
                    NewApplicationDelegator.LICENSEE_MAP);
            if (needVal && psnMap != null && psnMap.get(NewApplicationHelper.getPersonKey(nationality, idType, idNumber)) != null) {
                errorMap.put("idNumber", MessageUtil.replaceMessage("NEW_ERR0006", idNumber, "ID No."));
            }
        }

        return errorMap;
    }

    public boolean validateIdNo(String idType, String idNo) {
        boolean isValid = true;
        if (OrganizationConstants.ID_TYPE_FIN.equals(idType)) {
            isValid = SgNoValidator.validateFin(idNo);
        } else if (OrganizationConstants.ID_TYPE_NRIC.equals(idType)) {
            isValid = SgNoValidator.validateNric(idNo);
        }
        return isValid;
    }

}
