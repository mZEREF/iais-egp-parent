package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.FamilyPlanDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationOfPregnancyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;

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

        if("TOPCH001".equals(familyPlanDto.getContraHistory()) && StringUtil.isEmpty(familyPlanDto.getMostRecentContraMethod())){
            errorMap.put("mostRecentContraMethod", "GENERAL_ERR0006");
        }

        if("TOPRTP008".equals(familyPlanDto.getMainTopReason()) && StringUtil.isEmpty(familyPlanDto.getOtherMainTopReason())){
            errorMap.put("otherMainTopReason", "GENERAL_ERR0006");
        }
        if("TOPRTP004".equals(familyPlanDto.getMainTopReason()) && StringUtil.isEmpty(familyPlanDto.getSubRopReason())){
            errorMap.put("subRopReason", "GENERAL_ERR0006");
        }
        if("TOPSCTP003".equals(familyPlanDto.getSubRopReason()) || "TOPSCTP006".equals(familyPlanDto.getSubRopReason())){
            if(StringUtil.isEmpty(familyPlanDto.getOtherSubTopReason())){
                errorMap.put("otherSubTopReason", "GENERAL_ERR0006");
            }
        }
        if(!StringUtil.isEmpty(familyPlanDto.getGestAgeBaseOnUltrWeek())){
            if(Integer.valueOf(familyPlanDto.getGestAgeBaseOnUltrWeek())>=15 && StringUtil.isEmpty(familyPlanDto.getAbortChdMoreWksGender())){
                errorMap.put("abortChdMoreWksGender", "GENERAL_ERR0006");
            }
        }

        if(StringUtil.isEmpty(familyPlanDto.getGestAgeBaseOnUltrWeek())){
            errorMap.put("gestAgeBaseOnUltrWeek", "GENERAL_ERR0006");
        }
        return errorMap;
    }
}
