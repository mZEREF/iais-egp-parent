package com.ecquaria.cloud.moh.iais.validation.dataSubmission;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.SexualSterilizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssTreatmentDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class SexualSterilizationValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> erMap = IaisCommonUtils.genNewHashMap();
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = (VssSuperDataSubmissionDto) ParamUtil.getSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION);
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto();
        SexualSterilizationDto sexualSterilizationDto = vssTreatmentDto.getSexualSterilizationDto();
        if(sexualSterilizationDto ==null){
            sexualSterilizationDto= new SexualSterilizationDto();
        }
        if(!StringUtil.isEmpty(sexualSterilizationDto.getReviewedByHec()) && sexualSterilizationDto.getReviewedByHec()==true){
            if(sexualSterilizationDto.getHecReviewDate() == null){
                erMap.put("hecReviewDate", "GENERAL_ERR0006");
            }
        }
        if(StringUtil.isEmpty(sexualSterilizationDto.getReviewedByHec())){
            erMap.put("reviewedByHec", "GENERAL_ERR0006");
        }
        return erMap;
    }
}
