package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PostTerminationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationOfPregnancyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class PostTerminationValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        PostTerminationDto postTerminationDto=terminationOfPregnancyDto.getPostTerminationDto();
        if (!StringUtil.isEmpty(postTerminationDto.getGivenPostCounselling()) && postTerminationDto.getGivenPostCounselling()==true) {
            if(StringUtil.isEmpty(postTerminationDto.getIfCounsellingNotGiven())){
                errorMap.put("ifCounsellingNotGiven", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(postTerminationDto.getCounsellorIdType())){
                errorMap.put("counsellorIdType", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(postTerminationDto.getCounsellorIdNo())){
                errorMap.put("counsellorIdNo", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(postTerminationDto.getCounsellorName())){
                errorMap.put("counsellorName", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(postTerminationDto.getCounsellingDate())){
                errorMap.put("counsellingDate", "GENERAL_ERR0006");
            }
        }
        return errorMap;
    }
}
