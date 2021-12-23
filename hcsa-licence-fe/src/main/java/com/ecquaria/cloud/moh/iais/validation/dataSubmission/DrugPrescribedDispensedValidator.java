package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DrugMedicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DrugPrescribedDispensedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DrugSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DpDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * DrugPrescribedDispensedValidator
 *
 * @author zhixing
 * @date 2021/12/10
 */

@Component
@Slf4j
public class DrugPrescribedDispensedValidator implements CustomizeValidator {
    public static final String PRS_SERVICE_DOWN = "PRS_SERVICE_DOWN";
    @Autowired
    private DpDataSubmissionService dpDataSubmissionService;

    @Override
    public Map<String, String> validate(Object obj, String profile, HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();

        DrugPrescribedDispensedDto drugPrescribedDispensed=(DrugPrescribedDispensedDto) obj;
        DrugSubmissionDto drugSubmission = drugPrescribedDispensed.getDrugSubmission();
        if (drugSubmission == null) {
            drugSubmission = new DrugSubmissionDto();
        }
        //validate the Patient
        ValidationResult result = WebValidationHelper.validateProperty(drugSubmission, "ART");
        if (result != null) {
            errorMap.putAll(result.retrieveAll());
        }
        String name=drugSubmission.getName();
        if (errorMap.isEmpty() && StringUtil.isEmpty(name)) {
            errorMap.put("showValidatePT", AppConsts.YES);
            ParamUtil.setRequestAttr(request, "showValidatePT", AppConsts.YES);
        }
        //validate the Submission
        result = WebValidationHelper.validateProperty(drugSubmission, profile);
        if (result != null) {
            errorMap.putAll(result.retrieveAll());
        }

        String doctorReignNo=drugSubmission.getDoctorReignNo();
if(!StringUtil.isEmpty(doctorReignNo)){
    ProfessionalResponseDto professionalResponseDto = dpDataSubmissionService.retrievePrsInfo(doctorReignNo);
    if (professionalResponseDto.isHasException() || StringUtil.isNotEmpty(professionalResponseDto.getStatusCode())) {
        log.debug(StringUtil.changeForLog("prs svc down ..."));
        if (professionalResponseDto.isHasException()) {
            request.setAttribute(PRS_SERVICE_DOWN, PRS_SERVICE_DOWN);
            errorMap.put(PRS_SERVICE_DOWN, PRS_SERVICE_DOWN);
        } else if ("401".equals(professionalResponseDto.getStatusCode())) {
            errorMap.put("doctorReignNo", "GENERAL_ERR0054");
        } else {
            errorMap.put("doctorReignNo", "GENERAL_ERR0042");
        }
    }
}
        String prescriptionDate = drugSubmission.getPrescriptionDate();
        String dispensingDate = drugSubmission.getDispensingDate();
        String drugType = drugSubmission.getDrugType();
        String startDate = drugSubmission.getStartDate();

        if (StringUtil.isEmpty(prescriptionDate)&& "DPD001".equals(drugType)) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Date of Prescription", "field");
            errorMap.put("prescriptionDate", errMsg);
        }
        if (StringUtil.isEmpty(dispensingDate) && "DPD002".equals(drugType)) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Date of Dispensing", "field");
            errorMap.put("dispensingDate", errMsg);
        }
        if(!StringUtil.isEmpty(startDate) && !StringUtil.isEmpty(prescriptionDate)){
            int sp=startDate.compareTo(prescriptionDate);
            String errMsg="Must be later than Date of Prescription";
            if(sp<0){
                errorMap.put("startDate", errMsg);
            }
        }
        int i = 0;
        //validate the Medication
        List<DrugMedicationDto> drugMedicationDtos = drugPrescribedDispensed.getDrugMedicationDtos();
        result = WebValidationHelper.validateProperty(drugMedicationDtos, profile);
        if (result != null) {
            errorMap.putAll(result.retrieveAll());
        }
        for (DrugMedicationDto drugMedicationDto : drugMedicationDtos){
            if(StringUtil.isEmpty(drugMedicationDto.getBatchNo())){
                errorMap.put("batchNo"+i, "GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(drugMedicationDto.getStrength())){
                errorMap.put("strength"+i, "GENERAL_ERR0006");
            }
            if(StringUtil.isNotEmpty(drugMedicationDto.getStrength()) && !StringUtil.isNumber(drugMedicationDto.getStrength())){
                errorMap.put("strength"+i, "GENERAL_ERR0002");
            }
            if(StringUtil.isEmpty(drugMedicationDto.getQuantity())){
                errorMap.put("quantity"+i, "GENERAL_ERR0006");
            }
            if(StringUtil.isNotEmpty(drugMedicationDto.getQuantity()) && !StringUtil.isNumber(drugMedicationDto.getQuantity())){
                errorMap.put("quantity"+i, "GENERAL_ERR0002");
            }
            if(StringUtil.isEmpty(drugMedicationDto.getFrequency())){
                errorMap.put("frequency"+i, "GENERAL_ERR0006");
            }
            i++;
        }
        return errorMap;
    }
}
