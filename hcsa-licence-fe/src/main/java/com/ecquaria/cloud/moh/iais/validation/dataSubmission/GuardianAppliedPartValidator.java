package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.GuardianAppliedPartDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TreatmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssTreatmentDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class GuardianAppliedPartValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = (VssSuperDataSubmissionDto) ParamUtil.getSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION);
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto();
        TreatmentDto treatmentDto = vssTreatmentDto.getTreatmentDto();
        GuardianAppliedPartDto guardianAppliedPartDto = vssTreatmentDto.getGuardianAppliedPartDto();

        if(treatmentDto.getAge()<21 && treatmentDto.getMaritalStatus() != DataSubmissionConsts.MARITAL_STATUS_MARRIED){
            if(StringUtil.isEmpty(guardianAppliedPartDto.getGuardianName())){
                errMap.put("guardianName", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(guardianAppliedPartDto.getGuardianIdType())){
                errMap.put("guardianIdType", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(guardianAppliedPartDto.getGuardianIdNo())){
                errMap.put("guardianIdNo", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(guardianAppliedPartDto.getGuardianBirthday())){
                errMap.put("guardianBirthday", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(guardianAppliedPartDto.getGuardianRelationship())){
                errMap.put("guardianRelationship", "GENERAL_ERR0006");
            }
        }
        if(treatmentDto.getSterilizationReason() ==DataSubmissionConsts.MAIN_REASON_FOR_STERILIZATION_MENTAL_ILLNESS){
            if(StringUtil.isEmpty(guardianAppliedPartDto.getAppliedPartName())){
                errMap.put("appliedPartName", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(guardianAppliedPartDto.getAppliedPartIdType())){
                errMap.put("appliedPartIdType", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(guardianAppliedPartDto.getAppliedPartIdNo())){
                errMap.put("appliedPartIdNo", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(guardianAppliedPartDto.getAppliedPartBirthday())){
                errMap.put("appliedPartBirthday", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(guardianAppliedPartDto.getAppliedPartRelationship())){
                errMap.put("appliedPartRelationship", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(guardianAppliedPartDto.getCourtOrderIssueDate())){
                errMap.put("courtOrderIssueDate", "GENERAL_ERR0006");
            }
        }

        return errMap;
    }
}
