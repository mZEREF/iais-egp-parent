package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.FamilyPlanDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationOfPregnancyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class FamilyPlanValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        if(terminationOfPregnancyDto==null){
            terminationOfPregnancyDto = new TerminationOfPregnancyDto();
        }
        FamilyPlanDto familyPlanDto=terminationOfPregnancyDto.getFamilyPlanDto();
        if(familyPlanDto==null){
            familyPlanDto = new FamilyPlanDto();
        }

        if("TOPCH001".equals(familyPlanDto.getContraHistory())){
            ValidationResult result = WebValidationHelper.validateProperty(familyPlanDto,"mostRecentContraMethod");
            errorMap.putAll(result.retrieveAll());
        }

        if("TOPRTP008".equals(familyPlanDto.getMainTopReason())){
            ValidationResult result = WebValidationHelper.validateProperty(familyPlanDto,"otherMainTopReason");
            errorMap.putAll(result.retrieveAll());
        }
        if("TOPRTP005".equals(familyPlanDto.getMainTopReason())){
            ValidationResult result = WebValidationHelper.validateProperty(familyPlanDto,"topRiskCondition");
            errorMap.putAll(result.retrieveAll());
        }
        if("TOPRTP002".equals(familyPlanDto.getMainTopReason())){
            ValidationResult result = WebValidationHelper.validateProperty(familyPlanDto,"topMedCondition");
            errorMap.putAll(result.retrieveAll());
        }
        if("TOPRTP004".equals(familyPlanDto.getMainTopReason())){
            ValidationResult result = WebValidationHelper.validateProperty(familyPlanDto,"subRopReason");
            errorMap.putAll(result.retrieveAll());
        }
        if("TOPSCTP003".equals(familyPlanDto.getSubRopReason()) || "TOPSCTP006".equals(familyPlanDto.getSubRopReason())){
            ValidationResult result = WebValidationHelper.validateProperty(familyPlanDto,"mostRecentContraMethod");
            errorMap.putAll(result.retrieveAll());
        }
        if(!StringUtil.isEmpty(familyPlanDto.getGestAgeBaseOnUltrWeek())){
            if(Integer.valueOf(familyPlanDto.getGestAgeBaseOnUltrWeek())>=15 && StringUtil.isEmpty(familyPlanDto.getAbortChdMoreWksGender())){
                errorMap.put("abortChdMoreWksGender", "GENERAL_ERR0006");
            }
        }
        return errorMap;
    }
}
