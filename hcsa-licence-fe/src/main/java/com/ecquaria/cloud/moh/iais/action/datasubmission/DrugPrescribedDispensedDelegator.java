package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * DrugPrescribedDispensedDelegator
 *
 * @author zhixing
 * @date 2021/11/25
 */

@Delegator("drugPrescribedDispensedDelegator")
@Slf4j
public class DrugPrescribedDispensedDelegator extends DpCommonDelegator{

    @Autowired
    private PatientService patientService;

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Drug Practices</strong>");
        ParamUtil.setRequestAttr(bpc.request, "ageMsg", DataSubmissionHelper.getAgeMessage("Submission"));
        ParamUtil.setRequestAttr(bpc.request, "hbdAgeMsg", DataSubmissionHelper.getAgeMessage("Medication"));
    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        DpSuperDataSubmissionDto dpSuperDataSubmissionDto= DataSubmissionHelper.getCurrentDpDataSubmission(bpc.request);
        DrugPrescribedDispensedDto drugPrescribedDispensedDto = dpSuperDataSubmissionDto.getDrugPrescribedDispensedDto();
        List<DrugMedicationDto> drugMedicationDtos=null;
        if(drugPrescribedDispensedDto == null){
            drugPrescribedDispensedDto = new DrugPrescribedDispensedDto();
            dpSuperDataSubmissionDto.setDrugPrescribedDispensedDto(drugPrescribedDispensedDto);
        }else {
            drugMedicationDtos = drugPrescribedDispensedDto.getDrugMedicationDtos();
        }
        if(IaisCommonUtils.isEmpty(drugMedicationDtos)){
            drugMedicationDtos = IaisCommonUtils.genNewArrayList();
            drugMedicationDtos.add(new DrugMedicationDto());
        }
        drugPrescribedDispensedDto.setDrugMedicationDtos(drugMedicationDtos);
        DataSubmissionHelper.setCurrentDpDataSubmission(dpSuperDataSubmissionDto,bpc.request);
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.DP_DATA_SUBMISSION, dpSuperDataSubmissionDto);
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {

    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DpSuperDataSubmissionDto currentDpDataSubmission= DataSubmissionHelper.getCurrentDpDataSubmission(request);
        DrugPrescribedDispensedDto drugPrescribedDispensed=currentDpDataSubmission.getDrugPrescribedDispensedDto();
        if(drugPrescribedDispensed == null) {
            drugPrescribedDispensed = new DrugPrescribedDispensedDto();
        }
        DrugSubmissionDto drugSubmission = drugPrescribedDispensed.getDrugSubmission();
        if (drugSubmission == null) {
            drugSubmission = new DrugSubmissionDto();
        }
        String doctorName = ParamUtil.getString(bpc.request, "names");
        ControllerHelper.get(request, drugSubmission);
        drugSubmission.setDoctorName(doctorName);
        drugPrescribedDispensed.setDrugSubmission(drugSubmission);
        List<DrugMedicationDto> drugMedicationDtos = genDrugMedication(bpc.request);
        drugPrescribedDispensed.setDrugMedicationDtos(drugMedicationDtos);
        if(currentDpDataSubmission.getPatientDto() ==null){
            PatientDto patient = patientService.getDpPatientDto(drugSubmission.getIdType(), drugSubmission.getIdNumber(),
                    drugSubmission.getNationality(), currentDpDataSubmission.getOrgId());
            if(patient!=null){
                currentDpDataSubmission.setPatientDto(patient);
            }
        }
        currentDpDataSubmission.setDrugPrescribedDispensedDto(drugPrescribedDispensed);
        String profile ="DRP";
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String crud_action_type = ParamUtil.getRequestString(request, IntranetUserConstant.CRUD_ACTION_TYPE);
        if ("confirm".equals(crud_action_type)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(drugPrescribedDispensed, profile);
            errorMap = validationResult.retrieveAll();
            verifyRfcCommon(request, errorMap);
            if (errorMap.isEmpty()) {
                valRFC(request,drugPrescribedDispensed);
            }
        }
        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.DP_DATA_SUBMISSION, currentDpDataSubmission);
    }

    @Override
    public void pageConfirmAction(BaseProcessClass bpc) {
        String actionType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if(ACTION_TYPE_SUBMISSION.equals(actionType)){
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
            HttpServletRequest request = bpc.request;
            DpSuperDataSubmissionDto currentDpDataSubmission= DataSubmissionHelper.getCurrentDpDataSubmission(request);
            DrugPrescribedDispensedDto drugPrescribedDispensed=currentDpDataSubmission.getDrugPrescribedDispensedDto();
            if(drugPrescribedDispensed == null) {
                drugPrescribedDispensed = new DrugPrescribedDispensedDto();
            }
            DrugSubmissionDto drugSubmission = drugPrescribedDispensed.getDrugSubmission();
            if (drugSubmission == null) {
                drugSubmission = new DrugSubmissionDto();
            }
            String declaration=ParamUtil.getRequestString(request, "declaration");
            String remarks=ParamUtil.getRequestString(request, "remarks");
            if(!StringUtil.isEmpty(drugSubmission.getPrescriptionDate())){
                try {
                    if(CommonValidator.isDate(drugSubmission.getPrescriptionDate()) && Formatter.compareDateByDay(drugSubmission.getPrescriptionDate()) <-2){
                        if(declaration==null){
                            errorMap.put("declaration", "GENERAL_ERR0006");
                        }
                        if(remarks==null){
                            errorMap.put("remarks", "GENERAL_ERR0006");
                        }
                    }
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
            if(!StringUtil.isEmpty(drugSubmission.getDispensingDate())){
                try {
                    if(CommonValidator.isDate(drugSubmission.getDispensingDate()) && Formatter.compareDateByDay(drugSubmission.getDispensingDate()) <-2){
                        if(declaration==null){
                            errorMap.put("declaration", "GENERAL_ERR0006");
                        }
                        if(remarks==null){
                            errorMap.put("remarks", "GENERAL_ERR0006");
                        }
                    }
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
            DpSuperDataSubmissionDto dpSuperDataSubmissionDto= DataSubmissionHelper.getCurrentDpDataSubmission(request);
            if(dpSuperDataSubmissionDto == null) {
                dpSuperDataSubmissionDto = new DpSuperDataSubmissionDto();
            }
            DataSubmissionDto dataSubmissionDto= dpSuperDataSubmissionDto.getDataSubmissionDto();
            if(dataSubmissionDto == null) {
                dataSubmissionDto = new DataSubmissionDto();
            }

            if(remarks!=null){
                if (remarks.length() > 500) {
                    Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                    repMap.put("maxlength", "500");
                    repMap.put("field", "Reason for Late Submission");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0041", repMap);
                    errorMap.put("remarks", errMsg);
                }

            }
            dataSubmissionDto.setRemarks(remarks);
            if(declaration!=null){
                dataSubmissionDto.setDeclaration(declaration);
            }
            dpSuperDataSubmissionDto.setDataSubmissionDto(dataSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.DP_DATA_SUBMISSION, dpSuperDataSubmissionDto);
            if (!errorMap.isEmpty()){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_CONFIRM);
            }
        }

    }
    private List<DrugMedicationDto> genDrugMedication(HttpServletRequest request) {
        List<DrugMedicationDto> drugMedicationDtos=IaisCommonUtils.genNewArrayList();
        int drugMedicationLength = ParamUtil.getInt(request,"drugMedicationLength");
        DrugMedicationDto drugMedication;
        for(int i = 0; i < drugMedicationLength; i++){
            drugMedication = new DrugMedicationDto();
            String batchNo = ParamUtil.getString(request,"batchNo"+i);
            String strength = ParamUtil.getString(request,"strength"+i);
            String quantity = ParamUtil.getString(request,"quantity"+i);
            String frequency = ParamUtil.getString(request,"frequency"+i);
            drugMedication.setBatchNo(batchNo);
            drugMedication.setStrength(strength);
            drugMedication.setQuantity(quantity);
            drugMedication.setFrequency(frequency);
            drugMedicationDtos.add(drugMedication);
        }
        return drugMedicationDtos;
    }
    protected void valRFC(HttpServletRequest request, DrugPrescribedDispensedDto drugPrescribedDispensed){
        if(isRfc(request)){
            DpSuperDataSubmissionDto dpOldSuperDataSubmissionDto = DataSubmissionHelper.getOldDpSuperDataSubmissionDto(request);
            if(dpOldSuperDataSubmissionDto != null && dpOldSuperDataSubmissionDto.getDrugPrescribedDispensedDto()!= null && drugPrescribedDispensed.equals(dpOldSuperDataSubmissionDto.getDrugPrescribedDispensedDto())){
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
            }
        }
    }
}
