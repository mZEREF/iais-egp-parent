package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationOfPregnancyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class TerminationValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        TerminationDto terminationDto=terminationOfPregnancyDto.getTerminationDto();
        if("TOPTTP001".equals(terminationDto.getTopType()) || "TOPTTP003".equals(terminationDto.getTopType()) ){
            ValidationResult result = WebValidationHelper.validateProperty(terminationDto,"drugType");
            errorMap.putAll(result.retrieveAll());
        }
        if("TOPTOD004".equals(terminationDto.getDrugType())){
            ValidationResult result = WebValidationHelper.validateProperty(terminationDto,"otherDrugType");
            errorMap.putAll(result.retrieveAll());
        }
        if(terminationDto.getComplicationForOperRslt() == true){
            ValidationResult result = WebValidationHelper.validateProperty(terminationDto,"ariseOperationComplication");
            errorMap.putAll(result.retrieveAll());
        }
        return errorMap;
    }
}
