package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
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
        PatientInformationDto patientInformationDto=terminationOfPregnancyDto.getPatientInformationDto();
        FamilyPlanDto familyPlanDto=terminationOfPregnancyDto.getFamilyPlanDto();
        if(StringUtil.isEmpty(preTerminationDto)){
            preTerminationDto=new PreTerminationDto();
        }
        if(StringUtil.isEmpty(patientInformationDto)){
            patientInformationDto =new PatientInformationDto();
        }
        if (!StringUtil.isEmpty(preTerminationDto.getCounsellingGiven()) && preTerminationDto.getCounsellingGiven()==true) {
           /* if(StringUtil.isEmpty(preTerminationDto.getPatientSign())){
                errorMap.put("patientSign", "GENERAL_ERR0006");
            }*/
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
        }
        if(!StringUtil.isEmpty(familyPlanDto.getGestAgeBaseOnUltrWeek())){
            if(Integer.valueOf(familyPlanDto.getGestAgeBaseOnUltrWeek())>=13 && Integer.valueOf(familyPlanDto.getGestAgeBaseOnUltrWeek())<=24){
                if(StringUtil.isEmpty(preTerminationDto.getCounsellingGivenOnMin())){
                    errorMap.put("counsellingGivenOnMin", "GENERAL_ERR0006");
                }
                if(StringUtil.isEmpty(preTerminationDto.getPatientSign())){
                    errorMap.put("patientSign", "GENERAL_ERR0006");
                }

            }
        }

        if(!"TOPPCR003".equals(preTerminationDto.getCounsellingResult())){
            if(!StringUtil.isEmpty(preTerminationDto.getCounsellingGiven())){
                if (preTerminationDto.getCounsellingGiven()==true && "TOPPCR001".equals(preTerminationDto.getCounsellingResult())) {
                    if(StringUtil.isEmpty(preTerminationDto.getPatientAppointment())){
                        errorMap.put("patientAppointment", "GENERAL_ERR0006");
                    }

                }
            }
        }
        if(!"TOPPCR003".equals(preTerminationDto.getCounsellingResult())){
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
        if (!StringUtil.isEmpty(preTerminationDto.getCounsellingGiven()) && preTerminationDto.getCounsellingGiven()==false) {
            if(StringUtil.isEmpty(preTerminationDto.getNoCounsReason())){
                errorMap.put("noCounsReason", "GENERAL_ERR0006");
            }
        }
        if(!StringUtil.isEmpty(preTerminationDto.getCounsellingGiven()) && preTerminationDto.getCounsellingGiven()==true){
            if(!"AR_SC_001".equals(preTerminationDto.getCounsellingPlace())){
                if(!"TOPMS002".equals(patientInformationDto.getMaritalStatus())){
                    if(patientInformationDto.getPatientAge()<16){
                        if(StringUtil.isEmpty(preTerminationDto.getPreCounsNoCondReason())){
                            errorMap.put("preCounsNoCondReason", "GENERAL_ERR0006");
                        }
                    }
                }
            }
        }

        return errorMap;
    }
}
