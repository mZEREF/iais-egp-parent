package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationDto;
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

public class TerminationValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        TerminationDto terminationDto=terminationOfPregnancyDto.getTerminationDto();
        if(StringUtil.isEmpty(terminationDto)){
            terminationDto=new TerminationDto();
        }
        if("TOPTTP001".equals(terminationDto.getTopType()) || "TOPTTP003".equals(terminationDto.getTopType()) ){
            ValidationResult result = WebValidationHelper.validateProperty(terminationDto,"drugType");
            errorMap.putAll(result.retrieveAll());
        }
        if("TOPTOD004".equals(terminationDto.getDrugType())){
            ValidationResult result = WebValidationHelper.validateProperty(terminationDto,"otherDrugType");
            errorMap.putAll(result.retrieveAll());
        }
        if(!StringUtil.isEmpty(terminationDto.getComplicationForOperRslt())){
            if(terminationDto.getComplicationForOperRslt() == true){
                ValidationResult result = WebValidationHelper.validateProperty(terminationDto,"ariseOperationComplication");
                errorMap.putAll(result.retrieveAll());
            }
        }
        if("TOPTTP001".equals(terminationDto.getTopType()) || "TOPTTP003".equals(terminationDto.getTopType()) ){
            ValidationResult result = WebValidationHelper.validateProperty(terminationDto,"performedOwn");
            errorMap.putAll(result.retrieveAll());
        }
        if(!StringUtil.isEmpty(terminationDto.getPerformedOwn())){
            if(terminationDto.getPerformedOwn() == false){
                if(StringUtil.isEmpty(terminationDto.getTopPlace())){
                    errorMap.put("topPlace", "GENERAL_ERR0006");
                }
            }
        }
        if("TOPTTP001".equals(terminationDto.getTopType()) || "TOPTTP003".equals(terminationDto.getTopType()) ){
            if(!StringUtil.isEmpty(terminationDto.getPregnancyOwn())){
                if(terminationDto.getPregnancyOwn() == false){
                    if(StringUtil.isEmpty(terminationDto.getPrescribeTopPlace())){
                        errorMap.put("prescribeTopPlace", "GENERAL_ERR0006");
                    }
                }
            }
        }
        if("TOPTTP001".equals(terminationDto.getTopType()) || "TOPTTP003".equals(terminationDto.getTopType()) ){
            if(!StringUtil.isEmpty(terminationDto.getTakenOwn())){
                if(terminationDto.getTakenOwn() == false){
                    if(StringUtil.isEmpty(terminationDto.getTopDrugPlace())){
                        errorMap.put("topDrugPlace", "GENERAL_ERR0006");
                    }
                }
            }
        }
        return errorMap;
    }
}
