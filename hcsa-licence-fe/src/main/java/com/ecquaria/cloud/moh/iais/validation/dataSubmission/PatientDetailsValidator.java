package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationOfPregnancyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
public class PatientDetailsValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        TerminationOfPregnancyDto terminationOfPregnancyDto= topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        if(terminationOfPregnancyDto==null){
            terminationOfPregnancyDto=new TerminationOfPregnancyDto();
        }
        PatientInformationDto patientInformationDto=terminationOfPregnancyDto.getPatientInformationDto();
        if(patientInformationDto==null){
            patientInformationDto = new PatientInformationDto();
        }

        /*LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        String orgId = Optional.ofNullable(loginContext).map(LoginContext::getOrgId).orElse("");
        TopPatientSelectService topPatientSelectService = SpringContextHelper.getContext().getBean(TopPatientSelectService.class);
        PatientInformationDto patientInformation=topPatientSelectService.getTopPatientSelect(patientInformationDto.getIdType(), patientInformationDto.getIdNumber(), orgId);
        if (patientInformation != null && (StringUtil.isEmpty(patientInformationDto.getId()) || !Objects.equals(patientInformationDto.getId(), patientInformationDto.getId()))) {
            errorMap.put("idNumber", MessageUtil.getMessageDesc("DS_ERR007"));
        }*/
        String birthDate = patientInformationDto.getBirthData();
        if (!StringUtil.isEmpty(birthDate) && CommonValidator.isDate(birthDate)) {
            try {
                if (Formatter.compareDateByDay(birthDate) > 0) {
                    errorMap.put("birthData", MessageUtil.replaceMessage("DS_ERR001", "Date of Birth", "field"));
                }
            } catch (Exception e) {
                log.error(StringUtil.changeForLog(e.getMessage()), e);
            }
        }
        if("AR_IT_004".equals(patientInformationDto.getIdType()) && StringUtil.isEmpty(patientInformationDto.getNationality())){
            errorMap.put("nationality", "GENERAL_ERR0006");
        }
        if(!StringUtil.isEmpty(patientInformationDto.getNationality())){
            if(!"NAT0001".equals(patientInformationDto.getNationality()) && StringUtil.isEmpty(patientInformationDto.getCommResidenceInSgDate())){
                errorMap.put("commResidenceInSgDate", "GENERAL_ERR0006");
            }
            if(!"NAT0001".equals(patientInformationDto.getNationality()) && StringUtil.isEmpty(patientInformationDto.getResidenceStatus())){
                errorMap.put("residenceStatus", "GENERAL_ERR0006");
            }
        }
        if("ECGP004".equals(patientInformationDto.getEthnicGroup())){
            if(StringUtil.isEmpty(patientInformationDto.getOtherEthnicGroup())){
                errorMap.put("otherEthnicGroup", "GENERAL_ERR0006");
            }
        }
        if(!StringUtil.isEmpty(patientInformationDto.getLivingChildrenNo()) && !StringUtil.isNumber(patientInformationDto.getLivingChildrenNo())){
            errorMap.put("livingChildrenNo", "GENERAL_ERR0002");
        }
        /*String livingChildrenNo = ParamUtil.getRequestString(request, "livingChildrenNo");*/
        if (StringUtil.isEmpty(patientInformationDto.getLivingChildrenNo())) {
            errorMap.put("livingChildrenNo","GENERAL_ERR0006");
        }
        int m=0;
        if(StringUtil.isNumber(patientInformationDto.getLivingChildrenNo()) && !StringUtil.isEmpty(patientInformationDto.getLivingChildrenNo())){
            if (Integer.parseInt(patientInformationDto.getLivingChildrenNo()) > 10) {
                errorMap.put("livingChildrenNo", "Up to the value of 10 are allowed to be entered.");
            } else if(patientInformationDto.getLivingChildrenNo().length()>2){
                Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                repMap.put("maxlength", "2");
                repMap.put("field", "No. of Living Children");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0041", repMap);
                errorMap.put("livingChildrenNo", errMsg);
            }else if(Integer.parseInt(patientInformationDto.getLivingChildrenNo())<m){
                errorMap.put("livingChildrenNo", "Negative numbers are not allowed on this field.");
            }
        }
        if("TOPOCC011".equals(patientInformationDto.getOccupation()) && StringUtil.isEmpty(patientInformationDto.getOtherOccupation())){
            errorMap.put("otherOccupation", "GENERAL_ERR0006");
        }
        if("TOPAS001".equals(patientInformationDto.getActivityStatus()) && StringUtil.isEmpty(patientInformationDto.getOccupation())){
            errorMap.put("occupation", "GENERAL_ERR0006");
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
