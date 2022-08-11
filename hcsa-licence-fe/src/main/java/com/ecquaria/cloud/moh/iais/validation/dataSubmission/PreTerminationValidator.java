package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.FamilyPlanDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PreTerminationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationOfPregnancyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
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
                if (validateDate(preTerminationDto.getCounsellingDate())){
                    errorMap.put("counsellingDate", "Invalid date");
                } else {
                    ValidationResult result = WebValidationHelper.validateProperty(preTerminationDto, "counsellingDate");
                    errorMap.putAll(result.retrieveAll());
                }
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
                            if(preTerminationDto.getCounsellingAge()==null){
                                try {
                                    String birthDate = terminationOfPregnancyDto.getPatientInformationDto().getBirthData();
                                    String counsellingGiven = preTerminationDto.getCounsellingDate();
                                    int age=-Formatter.compareDateByDay(birthDate,counsellingGiven)/365;
                                    int ageNew=-(Formatter.compareDateByDay(birthDate,counsellingGiven)+age/4+1) / 365;

                                    preTerminationDto.setCounsellingAge(ageNew);
                                }catch (Exception e){
                                    log.error(e.getMessage(),e);
                                }
                            }
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
                    if (preTerminationDto.getCounsellingGiven() && "TOPPCR001".equals(preTerminationDto.getCounsellingResult())) {
                        if(StringUtil.isEmpty(preTerminationDto.getPatientAppointment())){
                            errorMap.put("patientAppointment", "GENERAL_ERR0006");
                        }

                    }
                }
            }
            if(!"TOPPCR004".equals(preTerminationDto.getCounsellingResult())){
                if(!StringUtil.isEmpty(preTerminationDto.getCounsellingGiven())){
                    if("TOPPCR001".equals(preTerminationDto.getCounsellingResult()) && preTerminationDto.getCounsellingGiven()){
                        if ("1".equals(preTerminationDto.getPatientAppointment())) {
                            if(StringUtil.isEmpty(preTerminationDto.getSecCounsellingDate())){
                                errorMap.put("secCounsellingDate", "GENERAL_ERR0006");
                            }else if(StringUtil.isNotEmpty(preTerminationDto.getCounsellingDate())){
                                try {
                                    Date oneDate=Formatter.parseDate(preTerminationDto.getCounsellingDate());
                                    Date secDate=Formatter.parseDate(preTerminationDto.getSecCounsellingDate());
                                    if(secDate.before(oneDate)){
                                        Map<String, String> params = IaisCommonUtils.genNewHashMap();
                                        params.put("field1", "Date of Second or Final Pre-Counselling");
                                        params.put("field2", "Date of Counselling");
                                        errorMap.put("secCounsellingDate", MessageUtil.getMessageDesc("DS_ERR069", params));
                                    }
                                }catch (ParseException ignored){
                                    log.error("ParseException");
                                }



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

    public static boolean validateDate(String dateStr) {
        boolean b;
        if (dateStr != null && !"".equals(dateStr.trim())) {
            String pattern = "[0-9]{2}/[0-9]{2}/[0-9]{4}";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(dateStr);
            if (!m.find()) {
                // format error
                log.warn("Invalid Date: {}",dateStr);
                b = false;
            } else {
                // format correct
                log.warn("Valid Date: {}",dateStr);
                b = true;
            }
        } else {
            log.warn("Invalid Date: {}",dateStr);
            b = false;
        }
        return b;
    }
}
