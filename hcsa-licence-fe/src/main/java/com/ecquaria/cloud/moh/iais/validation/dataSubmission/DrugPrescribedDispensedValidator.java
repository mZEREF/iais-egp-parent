package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DrugMedicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DrugPrescribedDispensedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DrugSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AppValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DpDataSubmissionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import static com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts.DRUG_PRESCRIBED;

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

    @Autowired
    private PatientService patientService;

    @Override
    public Map<String, String> validate(Object obj, String profile, HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        DpSuperDataSubmissionDto currentDpDataSubmission= DataSubmissionHelper.getCurrentDpDataSubmission(request);

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
        String doctorName=drugSubmission.getDoctorName();
        if (errorMap.isEmpty() && StringUtil.isEmpty(name) && StringUtil.isEmpty(doctorName)) {
            errorMap.put("showValidatePT", AppConsts.YES);
            ParamUtil.setRequestAttr(request, "showValidatePT", AppConsts.YES);
        }
        if(!"true".equals(drugSubmission.getDoctorInformations())){
            if (StringUtil.isNotEmpty(drugSubmission.getDoctorReignNo())) {
                if (errorMap.isEmpty() && StringUtil.isEmpty(doctorName)) {
                    errorMap.put("showValidateVD", AppConsts.YES);
                    ParamUtil.setRequestAttr(request, "showValidateVD", AppConsts.YES);
                }
            }
        }
        //validate the Submission
        result = WebValidationHelper.validateProperty(drugSubmission, profile);
        if (result != null) {
            errorMap.putAll(result.retrieveAll());
        }

        /*String doctorReignNo=drugSubmission.getDoctorReignNo();*/
        /*if(!StringUtil.isEmpty(doctorReignNo)){
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
        }*/
        String prescriptionDate = drugSubmission.getPrescriptionDate();
        String dispensingDate = drugSubmission.getDispensingDate();
        String drugType = drugSubmission.getDrugType();
      /*  String startDate = drugSubmission.getStartDate();*/
        String endDate = drugSubmission.getEndDate();
        List<DrugMedicationDto> preDrugMedicationDtos = IaisCommonUtils.genNewArrayList();

        if(DataSubmissionConsts.DRUG_DISPENSED.equals(drugType)){
            result = WebValidationHelper.validateProperty(drugSubmission, "DISPENSED");
            if (result != null) {
                errorMap.putAll(result.retrieveAll());
            }
            if (StringUtil.isNotEmpty(drugSubmission.getPrescriptionSubmissionId())){
                DrugPrescribedDispensedDto drugPrescribedDispensedDto = dpDataSubmissionService.
                        getDrugMedicationDtoBySubmissionNo(drugSubmission.getPrescriptionSubmissionId());
                if(drugPrescribedDispensedDto != null){
                    preDrugMedicationDtos =  drugPrescribedDispensedDto.getDrugMedicationDtos();
                }
                if(IaisCommonUtils.isEmpty(preDrugMedicationDtos)){
                    errorMap.put("prescriptionSubmissionId", "Please enter the correct prescription submission ID.");
                }else{
                    drugSubmission.setMedication(drugPrescribedDispensedDto.getDrugSubmission().getMedication());
                    //judge whether a prescription belongs to the patient
                    LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
                    String orgId = Optional.ofNullable(loginContext).map(LoginContext::getOrgId).orElse("");
                    PatientDto patient = patientService.getDpPatientDto(drugSubmission.getIdType(), drugSubmission.getIdNumber(), drugSubmission.getNationality(), orgId);
                    String submissionId = drugPrescribedDispensedDto.getDrugSubmission().getSubmissionId();
                    PatientDto patientDto = patientService.getPatientDtoBySubmissionId(submissionId);
                    if (patient!=null && patientDto!=null && !patient.getId().equals(patientDto.getId())){
                        errorMap.put("prescriptionSubmissionId", "The entered prescription submission ID could not be found for this patient.");
                    }
                }
            }
        }else if(DRUG_PRESCRIBED.equals(drugType)){
            result = WebValidationHelper.validateProperty(drugSubmission,"DRUG_PRESCRIBED");
            errorMap.putAll(result.retrieveAll());
        }
        //validate the medication
        result = WebValidationHelper.validateProperty(drugSubmission, "medication");
        if (result != null) {
            errorMap.putAll(result.retrieveAll());
        }
        if (DataSubmissionConsts.DRUG_METHADONE.equals(drugSubmission.getMedication())&&DataSubmissionConsts.DRUG_DISPENSED.equals(drugSubmission.getDrugType())){
            result = WebValidationHelper.validateProperty(drugSubmission, "UT");
        }else if(DataSubmissionConsts.DRUG_SOVENOR_PATCH.equals(drugSubmission.getMedication())){
            result = WebValidationHelper.validateProperty(drugSubmission, "NURSE");
        }
        if (result != null) {
            errorMap.putAll(result.retrieveAll());
        }
       /* if(!StringUtil.isEmpty(startDate) && !StringUtil.isEmpty(prescriptionDate)){
            try {
                if(Formatter.compareDateByDay(prescriptionDate,startDate)>0){
                    errorMap.put("startDate", "Must be later than or equal to Date of Prescription.");
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }*/
        if(DataSubmissionConsts.DRUG_SOVENOR_PATCH.equals(drugSubmission.getMedication())){
            if(StringUtil.isEmpty(drugSubmission.getNurseRegistrationNo())){
                errorMap.put("nurseRegistrationNo","GENERAL_ERR0006");
            }
            if(StringUtil.isEmpty(drugSubmission.getNurseName())){
                errorMap.put("nurseName","GENERAL_ERR0006");
            }
        }
        if(!StringUtil.isEmpty(dispensingDate) && !StringUtil.isEmpty(endDate)){
            try {
                if(Formatter.compareDateByDay(endDate,dispensingDate)<0){
                    errorMap.put("endDate", "Must be later than date of Start Date of Dispensing.");
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }

        int i = 0;
        int m=0;
        //validate the Medication
        List<DrugMedicationDto> drugMedicationDtos = drugPrescribedDispensed.getDrugMedicationDtos();
//        result = WebValidationHelper.validateProperty(drugMedicationDtos, profile);
//        if (result != null) {
//            errorMap.putAll(result.retrieveAll());
//        }
        //
        Map<String,Double> preDrugMedicationMap = null ;
        Map<String,Double> drugMedicationMap = null;
        if(DataSubmissionConsts.DRUG_DISPENSED.equals(drugType)){
            List<DrugMedicationDto> oldDrugMedicationDtos =  dpDataSubmissionService.
                    getDrugMedicationDtoBySubmissionNoForDispensed(drugSubmission.getPrescriptionSubmissionId(),
                            currentDpDataSubmission.getDataSubmissionDto().getSubmissionNo());
            preDrugMedicationMap = tidyDrugMedicationDto(null,preDrugMedicationDtos);
            drugMedicationMap = tidyDrugMedicationDto(drugMedicationMap,oldDrugMedicationDtos);
            drugMedicationMap = tidyDrugMedicationDto(drugMedicationMap,drugMedicationDtos);
        }
        //The amount of medication that already took
        double totalGet = 0;
        if (!CollectionUtils.isEmpty(drugMedicationMap)) {
            for (Double value : drugMedicationMap.values()) {
                totalGet = totalGet + value;
            }
        }
        List<String> quantityMatchS = new ArrayList<>(drugMedicationDtos.size());
        for (DrugMedicationDto drugMedicationDto : drugMedicationDtos){
            log.info(StringUtil.changeForLog("The DrugPrescribedDispensedValidator drugMedicationDtos i-->:"+i));
            if (!DRUG_PRESCRIBED.equals(drugType)) {
                if (StringUtil.isEmpty(drugMedicationDto.getBatchNo())) {
                    errorMap.put("batchNo" + i, "GENERAL_ERR0006");
                }
                if (!StringUtil.isEmpty(drugMedicationDto.getBatchNo()) && drugMedicationDto.getBatchNo().length() > 20) {
                    String general_err0041 = AppValidatorHelper.repLength("Batch No", "20");
                    errorMap.put("batchNo" + i, general_err0041);
                }
            }
            if(StringUtil.isEmpty(drugMedicationDto.getStrength())){
                errorMap.put("strength"+i, "GENERAL_ERR0006");
            }
            if(StringUtil.isNotEmpty(drugMedicationDto.getStrength()) && !StringUtil.isNumber(drugMedicationDto.getStrength())){
                errorMap.put("strength"+i, "GENERAL_ERR0002");
            }
            if(StringUtil.isNotEmpty(drugMedicationDto.getStrength()) && StringUtil.isNumber(drugMedicationDto.getStrength())){
                double d = Double.parseDouble(drugMedicationDto.getStrength());
                if(d<m){
                    errorMap.put("strength"+i, "Negative numbers are not allowed on this field.");
                }
            }

            if(StringUtil.isEmpty(drugMedicationDto.getQuantity())){
                errorMap.put("quantity"+i, "GENERAL_ERR0006");
            } else if(!StringUtil.isNumber(drugMedicationDto.getQuantity())){
                errorMap.put("quantity"+i, "GENERAL_ERR0002");
            }else if(Double.parseDouble(drugMedicationDto.getQuantity())<m){
                errorMap.put("quantity"+i, "Negative numbers are not allowed on this field.");
            }else if(DataSubmissionConsts.DRUG_DISPENSED.equals(drugType)){
               if(drugMedicationMap != null && preDrugMedicationMap != null){
                   ArrayList<Double> doubles = new ArrayList<>(preDrugMedicationMap.values());
                   Double preCount;
                   if (!CollectionUtils.isEmpty(doubles)) {
                       preCount = doubles.get(0);
                   } else {
                       preCount = null;
                   }
                   Double nowCount = drugMedicationMap.get(drugMedicationDto.getBatchNo());
                   log.info(StringUtil.changeForLog("The DrugPrescribedDispensedValidator drugMedicationDtos preCount-->:"+preCount));
                   log.info(StringUtil.changeForLog("The DrugPrescribedDispensedValidator drugMedicationDtos nowCount-->:"+nowCount));
                   if(preCount ==  null || nowCount > (preCount - totalGet)){
                       drugMedicationDto.setExcess("Y");
                       quantityMatchS.add("No");
                   }else {
                       drugMedicationDto.setExcess("N");
                       quantityMatchS.add("Yes");
                   }
                   totalGet = totalGet + nowCount;
               }
            }

            if(StringUtil.isEmpty(drugMedicationDto.getFrequency())){
                errorMap.put("frequency"+i, "GENERAL_ERR0006");
            }
            if(!StringUtil.isEmpty(drugMedicationDto.getFrequency())&&drugMedicationDto.getFrequency().equals("FRE009")){
                if(StringUtil.isEmpty(drugMedicationDto.getOtherFrequency())){
                    errorMap.put("otherFrequency"+i, "GENERAL_ERR0006");
                }else if(drugMedicationDto.getOtherFrequency().length()>100){
                    String general_err0041 = AppValidatorHelper.repLength("Other-Frequency", "100");
                    errorMap.put("otherFrequency"+i, general_err0041);
                }
            }
            i++;
        }
        if (!CollectionUtils.isEmpty(quantityMatchS) && quantityMatchS.contains("No")){
            ParamUtil.setRequestAttr(request, "quantityMatch", "No");
        }
        return errorMap;
    }

    private Map<String,Double> tidyDrugMedicationDto(Map<String,Double> result ,List<DrugMedicationDto> preDrugMedicationDtos){
        if(result == null){
            result = IaisCommonUtils.genNewHashMap();
        }
        if(IaisCommonUtils.isNotEmpty(preDrugMedicationDtos)){
          for(DrugMedicationDto drugMedicationDto : preDrugMedicationDtos){
            String batchNo =  drugMedicationDto.getBatchNo();
            String quantity = drugMedicationDto.getQuantity();
              Double quantityInt = result.get(batchNo);
            if(quantityInt == null){
                quantityInt = 0.00;
            }
            if(StringUtil.isNotEmpty(quantity) && StringUtil.isNumber(quantity)){
                quantityInt = quantityInt + Double.parseDouble(quantity);
            }
            result.put(batchNo,quantityInt);
          }
        }else{
            log.info(StringUtil.changeForLog("The preDrugMedicationDtos is null ..."));
        }
        return result;
    }
}
