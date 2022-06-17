package com.ecquaria.cloud.moh.iais.validation.dataSubmission;


import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DoctorInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.GuardianAppliedPartDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.SexualSterilizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssTreatmentDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;
@Slf4j
public class SexualSterilizationValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> erMap = IaisCommonUtils.genNewHashMap();
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = (VssSuperDataSubmissionDto) ParamUtil.getSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION);
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto();
        GuardianAppliedPartDto guardianAppliedPartDto = vssTreatmentDto.getGuardianAppliedPartDto();
        SexualSterilizationDto sexualSterilizationDto = vssTreatmentDto.getSexualSterilizationDto();
        DoctorInformationDto doctorInformationDto=vssSuperDataSubmissionDto.getDoctorInformationDto();
        if(doctorInformationDto==null){
            doctorInformationDto=new DoctorInformationDto();
        }
        if(sexualSterilizationDto ==null){
            sexualSterilizationDto= new SexualSterilizationDto();
        }
        if(guardianAppliedPartDto ==null){
            guardianAppliedPartDto= new GuardianAppliedPartDto();
        }
        if(!StringUtil.isEmpty(sexualSterilizationDto.getReviewedByHec()) && sexualSterilizationDto.getReviewedByHec() ==true){
            if(sexualSterilizationDto.getHecReviewDate() == null){
                erMap.put("hecReviewDate", "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(sexualSterilizationDto.getHecReviewedHospital())){
                erMap.put("hecReviewedHospital", "GENERAL_ERR0006");
            }
        }
        if(StringUtil.isEmpty(sexualSterilizationDto.getReviewedByHec())){
            erMap.put("reviewedByHec", "GENERAL_ERR0006");
        }

        if("true".equals(sexualSterilizationDto.getDoctorInformations())){
            if(StringUtil.isEmpty(doctorInformationDto.getName())){
                erMap.put("dName", "GENERAL_ERR0006");
            }else if(StringUtil.isNotEmpty(doctorInformationDto.getName())&&doctorInformationDto.getName().length()>66){
                    String general_err0041 = NewApplicationHelper.repLength("Name of Doctor who performed the sterilization", "66");
                    erMap.put("dName", general_err0041);
            }
            if(StringUtil.isEmpty(doctorInformationDto.getSpeciality())){
                erMap.put("dSpeciality", "GENERAL_ERR0006");
            }else if(StringUtil.isNotEmpty(doctorInformationDto.getSpeciality())&&doctorInformationDto.getSpeciality().length()>100){
                String general_err0041 = NewApplicationHelper.repLength("Specialty", "100");
                erMap.put("dSpeciality", general_err0041);
            }

            if(StringUtil.isEmpty(doctorInformationDto.getSubSpeciality())){
                erMap.put("dSubSpeciality", "GENERAL_ERR0006");
            }else if(StringUtil.isNotEmpty(doctorInformationDto.getSubSpeciality())&&doctorInformationDto.getSubSpeciality().length()>100){
                String general_err0041 = NewApplicationHelper.repLength("Sub-Specialty", "100");
                erMap.put("dSubSpeciality", general_err0041);
            }

            if(StringUtil.isEmpty(doctorInformationDto.getQualification())){
                erMap.put("dQualification", "GENERAL_ERR0006");
            }else if(StringUtil.isNotEmpty(doctorInformationDto.getQualification())&&doctorInformationDto.getQualification().length()>100){
                String general_err0041 = NewApplicationHelper.repLength("Qualification", "100");
                erMap.put("dQualification", general_err0041);
            }

        }

        if(sexualSterilizationDto.getOperationDate() != null && guardianAppliedPartDto.getCourtOrderIssueDate() !=null){
            try {
                if(Formatter.compareDateByDay(sexualSterilizationDto.getOperationDate(),guardianAppliedPartDto.getCourtOrderIssueDate())<0){
                    erMap.put("operationDate", "Date of Court Order Issued must be equal to or earlier than Date of Operation.");
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }

        }
        String doctorName=sexualSterilizationDto.getDoctorName();
        if(!"true".equals(sexualSterilizationDto.getDoctorInformations())){
            if (erMap.isEmpty() && StringUtil.isEmpty(doctorName)) {
                erMap.put("showValidateVD", AppConsts.YES);
                ParamUtil.setRequestAttr(request, "showValidateVD", AppConsts.YES);
            }
        }
        return erMap;
    }
}
