package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PreTerminationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationOfPregnancyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class PreTerminationValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        PreTerminationDto preTerminationDto=terminationOfPregnancyDto.getPreTerminationDto();
        if (preTerminationDto.getCounsellingGiven()==true) {
            if(StringUtil.isEmpty(preTerminationDto.getPatientSign())){
                errorMap.put("patientSign", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(preTerminationDto.getCounsellorIdType())){
                errorMap.put("counsellorIdType", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(preTerminationDto.getCounsellorIdNo())){
                errorMap.put("counsellorIdNo", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(preTerminationDto.getCounsellorName())){
                errorMap.put("counsellorName", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(preTerminationDto.getCounsellingDate())){
                errorMap.put("counsellingDate", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(preTerminationDto.getCounsellingPlace())){
                errorMap.put("counsellingPlace", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(preTerminationDto.getCounsellingResult())){
                errorMap.put("counsellingResult", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(preTerminationDto.getSecCounsellingDate())){
                errorMap.put("secCounsellingDate", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(preTerminationDto.getSecCounsellingResult())){
                errorMap.put("secCounsellingResult", "GENERAL_ERR0006");
            }
        }
        return errorMap;
    }
}
