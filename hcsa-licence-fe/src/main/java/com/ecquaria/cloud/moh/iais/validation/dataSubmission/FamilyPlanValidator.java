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
        if("TOPRTP004".equals(familyPlanDto.getMainTopReason())){
            if("TOPSCTP003".equals(familyPlanDto.getSubRopReason()) || "TOPSCTP006".equals(familyPlanDto.getSubRopReason())){
                ValidationResult result = WebValidationHelper.validateProperty(familyPlanDto,"otherSubTopReason");
                errorMap.putAll(result.retrieveAll());
            }
        }
        int m=0;
        if(!StringUtil.isEmpty(familyPlanDto.getGestAgeBaseOnUltrWeek())){
            if(!StringUtil.isNumber(familyPlanDto.getGestAgeBaseOnUltrWeek())){
                errorMap.put("gestAgeBaseOnUltrWeek", "GENERAL_ERR0002");
            }
        }
        if(StringUtil.isNotEmpty(familyPlanDto.getGestAgeBaseOnUltrWeek()) && StringUtil.isNumber(familyPlanDto.getGestAgeBaseOnUltrWeek())){
            int f=Integer.valueOf(familyPlanDto.getGestAgeBaseOnUltrWeek());
            if(f<m){
                errorMap.put("gestAgeBaseOnUltrWeek", "Negative numbers are not allowed on this field.");
            }
        }
        if(!StringUtil.isEmpty(familyPlanDto.getGestAgeBaseOnUltrWeek())){
            if(StringUtil.isNumber(familyPlanDto.getGestAgeBaseOnUltrWeek())){
                if(Integer.valueOf(familyPlanDto.getGestAgeBaseOnUltrWeek())>=15 && StringUtil.isEmpty(familyPlanDto.getAbortChdMoreWksGender())){
                    errorMap.put("abortChdMoreWksGender", "GENERAL_ERR0006");
                }
            }
        }
        if(StringUtil.isNotEmpty(familyPlanDto.getPreviousTopNumber()) && !StringUtil.isNumber(familyPlanDto.getPreviousTopNumber())){
            errorMap.put("previousTopNumber", "GENERAL_ERR0002");
        }

        if(StringUtil.isNotEmpty(familyPlanDto.getPreviousTopNumber()) && StringUtil.isNumber(familyPlanDto.getPreviousTopNumber())){
            int f=Integer.valueOf(familyPlanDto.getPreviousTopNumber());
            if(f<m){
                errorMap.put("previousTopNumber", "Negative numbers are not allowed on this field.");
            }
        }
        if(StringUtil.isNotEmpty(familyPlanDto.getGestAgeBaseOnUltrDay()) && !StringUtil.isNumber(familyPlanDto.getGestAgeBaseOnUltrDay())){
            errorMap.put("gestAgeBaseOnUltrDay", "GENERAL_ERR0002");
        }
        if(StringUtil.isNotEmpty(familyPlanDto.getGestAgeBaseOnUltrDay()) && StringUtil.isNumber(familyPlanDto.getGestAgeBaseOnUltrDay())){
            int f=Integer.valueOf(familyPlanDto.getGestAgeBaseOnUltrDay());
            if(f<m){
                errorMap.put("gestAgeBaseOnUltrDay", "Negative numbers are not allowed on this field.");
            }
        }
        if(StringUtil.isNotEmpty(familyPlanDto.getGestAgeBaseNotOnUltrWeek()) && !StringUtil.isNumber(familyPlanDto.getGestAgeBaseNotOnUltrWeek())){
            errorMap.put("gestAgeBaseNotOnUltrWeek", "GENERAL_ERR0002");
        }
        if(StringUtil.isNotEmpty(familyPlanDto.getGestAgeBaseNotOnUltrWeek()) && StringUtil.isNumber(familyPlanDto.getGestAgeBaseNotOnUltrWeek())){
            int f=Integer.valueOf(familyPlanDto.getGestAgeBaseNotOnUltrWeek());
            if(f<m){
                errorMap.put("gestAgeBaseNotOnUltrWeek", "Negative numbers are not allowed on this field.");
            }
        }
        if(StringUtil.isNotEmpty(familyPlanDto.getGestAgeBaseNotOnUltrDay()) && !StringUtil.isNumber(familyPlanDto.getGestAgeBaseNotOnUltrDay())){
            errorMap.put("gestAgeBaseNotOnUltrDay", "GENERAL_ERR0002");
        }
        if(StringUtil.isNotEmpty(familyPlanDto.getGestAgeBaseNotOnUltrDay()) && StringUtil.isNumber(familyPlanDto.getGestAgeBaseNotOnUltrDay())){
            int f=Integer.valueOf(familyPlanDto.getGestAgeBaseNotOnUltrDay());
            if(f<m){
                errorMap.put("gestAgeBaseNotOnUltrDay", "Negative numbers are not allowed on this field.");
            }
        }
        if("TOPCH001".equals(familyPlanDto.getContraHistory())){
            if("TOPMRC007".equals(familyPlanDto.getMostRecentContraMethod())){
                ValidationResult result = WebValidationHelper.validateProperty(familyPlanDto,"otherContraMethod");
                errorMap.putAll(result.retrieveAll());
            }
        }
        return errorMap;
    }
}
