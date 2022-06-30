package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.FamilyPlanDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PreTerminationDto;
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

public class PreTerminationValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        PreTerminationDto preTerminationDto=terminationOfPregnancyDto.getPreTerminationDto();
        PatientInformationDto patientInformationDto=terminationOfPregnancyDto.getPatientInformationDto();
        FamilyPlanDto familyPlanDto=terminationOfPregnancyDto.getFamilyPlanDto();
        if(StringUtil.isEmpty(preTerminationDto)){
            preTerminationDto=new PreTerminationDto();
        }
        if(StringUtil.isEmpty(patientInformationDto)){
            patientInformationDto =new PatientInformationDto();
        }
        if (!StringUtil.isEmpty(preTerminationDto.getCounsellingGiven()) && preTerminationDto.getCounsellingGiven()==true) {
            if(StringUtil.isEmpty(preTerminationDto.getCounsellorIdType())){
                ValidationResult result = WebValidationHelper.validateProperty(preTerminationDto,"counsellorIdType");
                errorMap.putAll(result.retrieveAll());
            }
            if(StringUtil.isEmpty(preTerminationDto.getCounsellorIdNo())){
                ValidationResult result = WebValidationHelper.validateProperty(preTerminationDto,"counsellorIdNo");
                errorMap.putAll(result.retrieveAll());
            }
            if(StringUtil.isEmpty(preTerminationDto.getCounsellorName())){
                ValidationResult result = WebValidationHelper.validateProperty(preTerminationDto,"counsellorName");
                errorMap.putAll(result.retrieveAll());
            }
            if(StringUtil.isEmpty(preTerminationDto.getCounsellingDate())){
                ValidationResult result = WebValidationHelper.validateProperty(preTerminationDto,"counsellingDate");
                errorMap.putAll(result.retrieveAll());
            }
            if(StringUtil.isEmpty(preTerminationDto.getCounsellingPlace())){
                ValidationResult result = WebValidationHelper.validateProperty(preTerminationDto,"counsellingPlace");
                errorMap.putAll(result.retrieveAll());
                errorMap.put("counsellingPlaceAge", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(preTerminationDto.getCounsellingResult())){
                ValidationResult result = WebValidationHelper.validateProperty(preTerminationDto,"counsellingResult");
                errorMap.putAll(result.retrieveAll());
            }
            if(!StringUtil.isEmpty(preTerminationDto.getCounsellingPlace())){
                if(!"AR_SC_001".equals(preTerminationDto.getCounsellingPlace())){
                    if(!"TOPMS002".equals(patientInformationDto.getMaritalStatus())){
                        if(StringUtil.isNotEmpty(preTerminationDto.getCounsellingDate())){
                            if(preTerminationDto.getCounsellingAge()<16){
                                if(StringUtil.isEmpty(preTerminationDto.getPreCounsNoCondReason())){
                                    errorMap.put("preCounsNoCondReason", "GENERAL_ERR0006");
                                }
                            }
                        }
                    }
                }
            }
            if(!"TOPPCR004".equals(preTerminationDto.getCounsellingResult())){
                if(!StringUtil.isEmpty(preTerminationDto.getCounsellingGiven())){
                    if (preTerminationDto.getCounsellingGiven()==true && "TOPPCR001".equals(preTerminationDto.getCounsellingResult())) {
                        if(StringUtil.isEmpty(preTerminationDto.getPatientAppointment())){
                            errorMap.put("patientAppointment", "GENERAL_ERR0006");
                        }

                    }
                }
            }
            if(!"TOPPCR004".equals(preTerminationDto.getCounsellingResult())){
                if(!StringUtil.isEmpty(preTerminationDto.getCounsellingGiven())){
                    if("TOPPCR001".equals(preTerminationDto.getCounsellingResult()) && preTerminationDto.getCounsellingGiven() == true){
                        if ("Yes".equals(preTerminationDto.getPatientAppointment())) {
                            if(StringUtil.isEmpty(preTerminationDto.getSecCounsellingDate())){
                                errorMap.put("secCounsellingDate", "GENERAL_ERR0006");
                            }
                            if(StringUtil.isEmpty(preTerminationDto.getSecCounsellingResult())){
                                errorMap.put("secCounsellingResult", "GENERAL_ERR0006");
                            }
                        }
                    }
                }
            }
        }else if (!StringUtil.isEmpty(preTerminationDto.getCounsellingGiven()) && preTerminationDto.getCounsellingGiven()==false) {
            if(StringUtil.isEmpty(preTerminationDto.getNoCounsReason())){
                ValidationResult result = WebValidationHelper.validateProperty(preTerminationDto,"noCounsReason");
                errorMap.putAll(result.retrieveAll());
            }
        }
        if(!StringUtil.isEmpty(familyPlanDto.getGestAgeBaseOnUltrWeek())){
            if(Integer.parseInt(familyPlanDto.getGestAgeBaseOnUltrWeek())>=13 && Integer.parseInt(familyPlanDto.getGestAgeBaseOnUltrWeek())<=24){
                if(StringUtil.isEmpty(preTerminationDto.getCounsellingGivenOnMin())){
                    errorMap.put("counsellingGivenOnMin", "GENERAL_ERR0006");
                }
                if(StringUtil.isEmpty(preTerminationDto.getPatientSign())){
                    errorMap.put("patientSign", "GENERAL_ERR0006");
                }

            }
        }
        return errorMap;
    }
}
