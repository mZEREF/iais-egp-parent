package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DoctorInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationOfPregnancyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.AppValidatorHelper;
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
        DoctorInformationDto doctorInformationDto=topSuperDataSubmissionDto.getDoctorInformationDto();
        if(StringUtil.isEmpty(terminationDto)){
            terminationDto=new TerminationDto();
        }
        if("TOPTTP001".equals(terminationDto.getTopType()) || "TOPTTP003".equals(terminationDto.getTopType()) ){
            if(StringUtil.isEmpty(terminationDto.getSpType())){
                errorMap.put("spType", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(terminationDto.getAnType())){
                errorMap.put("anType", "GENERAL_ERR0006");
            }
            if("TOPTSP003".equals(terminationDto.getSpType())){
                if(StringUtil.isEmpty(terminationDto.getOtherSpType())){
                    errorMap.put("otherSpType", "GENERAL_ERR0006");
                }
            }
            if("TOPTA004".equals(terminationDto.getAnType())){
                if(StringUtil.isEmpty(terminationDto.getOtherAnType())){
                    errorMap.put("otherAnType", "GENERAL_ERR0006");
                }
            }
        }
        if("TOPTTP001".equals(terminationDto.getTopType()) || "TOPTTP002".equals(terminationDto.getTopType()) ){
            ValidationResult result = WebValidationHelper.validateProperty(terminationDto,"drugType");
            errorMap.putAll(result.retrieveAll());
        }
        if("TOPTTP001".equals(terminationDto.getTopType()) || "TOPTTP002".equals(terminationDto.getTopType()) ){
            if("TOPTOD005".equals(terminationDto.getDrugType())){
                ValidationResult result = WebValidationHelper.validateProperty(terminationDto,"otherDrugType");
                errorMap.putAll(result.retrieveAll());
            }
        }

        if(!StringUtil.isEmpty(terminationDto.getComplicationForOperRslt())){
            if(terminationDto.getComplicationForOperRslt() == true){
                ValidationResult result = WebValidationHelper.validateProperty(terminationDto,"ariseOperationComplication");
                errorMap.putAll(result.retrieveAll());
            }
        }
        if("TOPTTP001".equals(terminationDto.getTopType()) || "TOPTTP003".equals(terminationDto.getTopType()) ) {
            if (!StringUtil.isEmpty(terminationDto.getPerformedOwn())) {
                if (terminationDto.getPerformedOwn() == false) {
                    if (StringUtil.isEmpty(terminationDto.getTopPlace())) {
                        errorMap.put("topPlace", "GENERAL_ERR0006");
                    }
                }
            }
        }
        if("TOPTTP001".equals(terminationDto.getTopType()) || "TOPTTP002".equals(terminationDto.getTopType()) ){
            if(!StringUtil.isEmpty(terminationDto.getPregnancyOwn())){
                if(terminationDto.getPregnancyOwn() == false){
                    if(StringUtil.isEmpty(terminationDto.getPrescribeTopPlace())){
                        errorMap.put("prescribeTopPlace", "GENERAL_ERR0006");
                    }
                }
            }
        }
        if("TOPTTP001".equals(terminationDto.getTopType()) || "TOPTTP002".equals(terminationDto.getTopType()) ){
            if(!StringUtil.isEmpty(terminationDto.getTakenOwn())){
                if(terminationDto.getTakenOwn() == false){
                    if(StringUtil.isEmpty(terminationDto.getTopDrugPlace())){
                        errorMap.put("topDrugPlace", "GENERAL_ERR0006");
                    }
                }
            }
        }
        if("TOPTTP001".equals(terminationDto.getTopType()) || "TOPTTP003".equals(terminationDto.getTopType()) ){
            if(StringUtil.isEmpty(terminationDto.getPerformedOwn())){
                errorMap.put("performedOwn", "GENERAL_ERR0006");
            }
        }
        if("TOPTTP001".equals(terminationDto.getTopType()) || "TOPTTP002".equals(terminationDto.getTopType()) ){
            if(StringUtil.isEmpty(terminationDto.getPregnancyOwn())){
                errorMap.put("pregnancyOwn", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(terminationDto.getTakenOwn())){
                errorMap.put("takenOwn", "GENERAL_ERR0006");
            }
            if("AR_SC_001".equals(terminationDto.getTopDrugPlace())){
                if(StringUtil.isEmpty(terminationDto.getOtherTopDrugPlace())){
                    errorMap.put("otherTopDrugPlace", "GENERAL_ERR0006");
                }
            }
        }
        if(StringUtil.isEmpty(terminationDto.getComplicationForOperRslt())){
            errorMap.put("complicationForOperRslt", "GENERAL_ERR0006");
        }
        String doctorName=terminationDto.getDoctorName();
        if(!"true".equals(terminationDto.getTopDoctorInformations())){
            if (errorMap.isEmpty() && StringUtil.isEmpty(doctorName)) {
                errorMap.put("showValidatePT", AppConsts.YES);
                ParamUtil.setRequestAttr(request, "showValidatePT", AppConsts.YES);
            }
        }
        if("true".equals(terminationDto.getTopDoctorInformations())) {
            if (StringUtil.isEmpty(doctorInformationDto.getName())) {
                errorMap.put("dName", "GENERAL_ERR0006");
            }else if(StringUtil.isNotEmpty(doctorInformationDto.getName()) && doctorInformationDto.getName().length()>66){
                String general_err0041 = AppValidatorHelper.repLength("Name of Doctor", "66");
                errorMap.put("dName", general_err0041);
            }
            if (StringUtil.isEmpty(doctorInformationDto.getSpeciality())) {
                errorMap.put("dSpeciality", "GENERAL_ERR0006");
            }else if(StringUtil.isNotEmpty(doctorInformationDto.getSpeciality())&&doctorInformationDto.getSpeciality().length()>100){
                String general_err0041 = AppValidatorHelper.repLength("Specialty", "100");
                errorMap.put("dSpeciality", general_err0041);
            }
            if (StringUtil.isEmpty(doctorInformationDto.getSubSpeciality())) {
                errorMap.put("dSubSpeciality", "GENERAL_ERR0006");
            }else if(StringUtil.isNotEmpty(doctorInformationDto.getSubSpeciality())&&doctorInformationDto.getSubSpeciality().length()>100){
                String general_err0041 = AppValidatorHelper.repLength("Sub-Specialty", "100");
                errorMap.put("dSubSpeciality", general_err0041);
            }
            if (StringUtil.isEmpty(doctorInformationDto.getQualification())) {
                errorMap.put("dQualification", "GENERAL_ERR0006");
            }else if(StringUtil.isNotEmpty(doctorInformationDto.getQualification())&&doctorInformationDto.getQualification().length()>100){
                String general_err0041 = AppValidatorHelper.repLength("Qualification", "100");
                errorMap.put("dQualification", general_err0041);
            }
        }else if("false".equals(terminationDto.getTopDoctorInformations())){
            if (StringUtil.isEmpty(doctorInformationDto.getSpeciality())) {
                errorMap.put("dSpecialitys", "GENERAL_ERR0006");
            }else if(StringUtil.isNotEmpty(doctorInformationDto.getSpeciality())&&doctorInformationDto.getSpeciality().length()>100){
                String general_err0041 = AppValidatorHelper.repLength("Specialty", "100");
                errorMap.put("dSpecialitys", general_err0041);
            }
            if (StringUtil.isEmpty(doctorInformationDto.getSubSpeciality())) {
                errorMap.put("dSubSpecialitys", "GENERAL_ERR0006");
            }else if(StringUtil.isNotEmpty(doctorInformationDto.getSubSpeciality())&&doctorInformationDto.getSubSpeciality().length()>100){
                String general_err0041 = AppValidatorHelper.repLength("Sub-Specialty", "100");
                errorMap.put("dSubSpecialitys", general_err0041);
            }
            if (StringUtil.isEmpty(doctorInformationDto.getQualification())) {
                errorMap.put("dQualifications", "GENERAL_ERR0006");
            }else if(StringUtil.isNotEmpty(doctorInformationDto.getQualification())&&doctorInformationDto.getQualification().length()>100){
                String general_err0041 = AppValidatorHelper.repLength("Qualification", "100");
                errorMap.put("dQualifications", general_err0041);
            }
        }
        return errorMap;
    }
}
