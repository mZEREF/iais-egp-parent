package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationOfPregnancyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class PatientDetailsValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        PatientInformationDto patientInformationDto=terminationOfPregnancyDto.getPatientInformationDto();
        if(!"NAT0001".equals(patientInformationDto.getNationality()) && StringUtil.isEmpty(patientInformationDto.getCommResidenceInSgDate())){
            errorMap.put("commResidenceInSgDate", "GENERAL_ERR0006");
        }
        if(!"NAT0001".equals(patientInformationDto.getNationality()) && StringUtil.isEmpty(patientInformationDto.getResidenceStatus())){
            errorMap.put("residenceStatus", "GENERAL_ERR0006");
        }
        if("ETHG005".equals(patientInformationDto.getEthnicGroup()) && StringUtil.isEmpty(patientInformationDto.getOtherEthnicGroup())){
            errorMap.put("otherEthnicGroup", "GENERAL_ERR0006");
        }
        if(!StringUtil.isEmpty(patientInformationDto.getLivingChildrenNo()) && !StringUtil.isNumber(patientInformationDto.getLivingChildrenNo())){
            errorMap.put("livingChildrenNo", "GENERAL_ERR0002");
        }

        if("TOPOCC014".equals(patientInformationDto.getOccupation()) && StringUtil.isEmpty(patientInformationDto.getOtherOccupation())){
            errorMap.put("otherOccupation", "GENERAL_ERR0006");
        }
        int i = 0;
        List<String> livingChildrenGenders= patientInformationDto.getLivingChildrenGenders();
        if(!StringUtil.isEmpty(livingChildrenGenders)){
            if(livingChildrenGenders.size() !=0){
                for (String livingChildrenGender : livingChildrenGenders) {
                    if(livingChildrenGender.equals("")){
                        errorMap.put("livingChildrenGenders"+i, "GENERAL_ERR0006");
                    }
                    i++;
                }
            }
        }

        return errorMap;
    }
}
