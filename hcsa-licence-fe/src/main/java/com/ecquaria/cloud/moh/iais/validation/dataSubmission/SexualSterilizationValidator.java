package com.ecquaria.cloud.moh.iais.validation.dataSubmission;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.GuardianAppliedPartDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.SexualSterilizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssTreatmentDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;
@Slf4j
public class SexualSterilizationValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> erMap = IaisCommonUtils.genNewHashMap();
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = (VssSuperDataSubmissionDto) ParamUtil.getSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION);
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto();
        GuardianAppliedPartDto guardianAppliedPartDto = vssTreatmentDto.getGuardianAppliedPartDto() == null ? new GuardianAppliedPartDto():vssTreatmentDto.getGuardianAppliedPartDto();
        SexualSterilizationDto sexualSterilizationDto = vssTreatmentDto.getSexualSterilizationDto()== null ? new SexualSterilizationDto():vssTreatmentDto.getSexualSterilizationDto();

        if(sexualSterilizationDto ==null){
            sexualSterilizationDto= new SexualSterilizationDto();
        }
        if(!StringUtil.isEmpty(sexualSterilizationDto.getReviewedByHec()) && sexualSterilizationDto.getReviewedByHec() ==true){
            if(sexualSterilizationDto.getHecReviewDate() == null){
                erMap.put("hecReviewDate", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(sexualSterilizationDto.getHecReviewedHospital())){
                erMap.put("hecReviewedHospital", "GENERAL_ERR0006");
            }
        }
        if(StringUtil.isEmpty(sexualSterilizationDto.getReviewedByHec())){
            erMap.put("reviewedByHec", "GENERAL_ERR0006");
        }
        if(sexualSterilizationDto.getOperationDate() != null){
            try {
                if(Formatter.compareDateByDay(sexualSterilizationDto.getOperationDate(),guardianAppliedPartDto.getCourtOrderIssueDate())<0){
                    erMap.put("operationDate", "Date of Court Order Issued must be equal to or earlier than Date of Operation.");
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }

        }
        return erMap;
    }
}
