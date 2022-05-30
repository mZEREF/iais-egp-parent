package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AjaxResDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DpDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Autowired
    private DpDataSubmissionService dpDataSubmissionService;

    @PostMapping(value = "/checkPrescriptionSubmissionId")
    public @ResponseBody
    AjaxResDto checkPrescriptionSubmissionId(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("the do checkPrescriptionSubmissionId start ...."));
        AjaxResDto ajaxResDto = new AjaxResDto();
        String prescriptionSubmissionId = ParamUtil.getString(request, "prescriptionSubmissionId");
        log.info(StringUtil.changeForLog("prescriptionSubmissionId is -->:"+prescriptionSubmissionId));
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if(StringUtil.isEmpty(prescriptionSubmissionId)){
            errorMap.put("prescriptionSubmissionId", "GENERAL_ERR0006");
        }else {
            DrugPrescribedDispensedDto drugPrescribedDispensedDto = dpDataSubmissionService.
                    getDrugMedicationDtoBySubmissionNo(prescriptionSubmissionId);
            if(drugPrescribedDispensedDto == null
                    || IaisCommonUtils.isEmpty(drugPrescribedDispensedDto.getDrugMedicationDtos())){
                errorMap.put("prescriptionSubmissionId", "Please enter the correct prescription submission ID.");
            }else{
                ajaxResDto.setResCode(AppConsts.AJAX_RES_CODE_SUCCESS);
                ajaxResDto.setResultJson(drugPrescribedDispensedDto.getDrugSubmission().getMedication());
            }
        }
        if(!errorMap.isEmpty()){
            ajaxResDto.setResCode(AppConsts.AJAX_RES_CODE_VALIDATE_ERROR);
            ajaxResDto.setResultJson(MessageUtil.getMessageDesc(errorMap.get("prescriptionSubmissionId")));
        }
        log.info(StringUtil.changeForLog("the do checkPrescriptionSubmissionId end ...."));
        return ajaxResDto;
    }

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
        String crud_action_type = ParamUtil.getRequestString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if (StringUtil.isEmpty(crud_action_type)){
            crud_action_type="";
        }
        if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(dpSuperDataSubmissionDto.getDataSubmissionDto().getAppType())) {
            if (crud_action_type.equals("rfc")) {
                DataSubmissionDto dataSubmissionDto = dpSuperDataSubmissionDto.getDataSubmissionDto();
                String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(bpc.request))
                        .map(LoginContext::getOrgId).orElse("");
                if (dpDataSubmissionService.getDpSuperDataSubmissionDtoRfcDraftByConds(
                        orgId, dpSuperDataSubmissionDto.getSubmissionType(), dpSuperDataSubmissionDto.getSvcName(), dpSuperDataSubmissionDto.getHciCode(), dataSubmissionDto.getId()) != null) {
                    ParamUtil.setRequestAttr(bpc.request, "hasDraft", Boolean.TRUE);
                }
            }
            String actionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            if ("resume".equals(actionValue)) {
                dpSuperDataSubmissionDto = dpDataSubmissionService.getDpSuperDataSubmissionDtoRfcDraftByConds(
                        dpSuperDataSubmissionDto.getOrgId(), dpSuperDataSubmissionDto.getSubmissionType(), dpSuperDataSubmissionDto.getSvcName(), dpSuperDataSubmissionDto.getHciCode(), dpSuperDataSubmissionDto.getDataSubmissionDto().getId());
                if (dpSuperDataSubmissionDto == null) {
                    log.warn("Can't resume data!");
                    dpSuperDataSubmissionDto = new DpSuperDataSubmissionDto();
                }
                DataSubmissionHelper.setCurrentDpDataSubmission(dpSuperDataSubmissionDto, bpc.request);
            } else if ("delete".equals(actionValue)) {
                dpDataSubmissionService.deleteDpSuperDataSubmissionDtoRfcDraftByConds(dpSuperDataSubmissionDto.getOrgId(), dpSuperDataSubmissionDto.getSubmissionType(), dpSuperDataSubmissionDto.getHciCode(), dpSuperDataSubmissionDto.getDataSubmissionDto().getId());
            }
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
        DoctorInformationDto doctorInformationDto=currentDpDataSubmission.getDoctorInformationDto();
        if(doctorInformationDto==null){
            doctorInformationDto=new DoctorInformationDto();
        }
        DrugPrescribedDispensedDto drugPrescribedDispensed=currentDpDataSubmission.getDrugPrescribedDispensedDto();
        if(drugPrescribedDispensed == null) {
            drugPrescribedDispensed = new DrugPrescribedDispensedDto();
        }
        DrugSubmissionDto drugSubmission = drugPrescribedDispensed.getDrugSubmission();
        if (drugSubmission == null) {
            drugSubmission = new DrugSubmissionDto();
        }
        ControllerHelper.get(request, drugSubmission);
        if("true".equals(drugSubmission.getDoctorInformations())){
            String dName = ParamUtil.getString(bpc.request, "dName");
            String dSpeciality = ParamUtil.getString(bpc.request, "dSpeciality");
            String dSubSpeciality = ParamUtil.getString(bpc.request, "dSubSpeciality");
            String dQualification = ParamUtil.getString(bpc.request, "dQualification");
            doctorInformationDto.setName(dName);
            doctorInformationDto.setDoctorReignNo(drugSubmission.getDoctorReignNo());
            doctorInformationDto.setSubSpeciality(dSubSpeciality);
            doctorInformationDto.setSpeciality(dSpeciality);
            doctorInformationDto.setQualification(dQualification);
            doctorInformationDto.setDoctorSource(DataSubmissionConsts.DS_DRP);
            currentDpDataSubmission.setDoctorInformationDto(doctorInformationDto);
        }else {
            String doctorName = ParamUtil.getString(bpc.request, "names");
            drugSubmission.setDoctorName(doctorName);
        }
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
        String crud_action_type = ParamUtil.getRequestString(request, IntranetUserConstant.CRUD_ACTION_TYPE);
        String actionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        if ("resume".equals(actionValue)||"delete".equals(actionValue)) {
            crud_action_type ="page";
            ParamUtil.setRequestAttr(request, IntranetUserConstant.CRUD_ACTION_TYPE, crud_action_type);
        }
        String profile ="DRP";
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if ("confirm".equals(crud_action_type)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(drugPrescribedDispensed, profile);
            errorMap = validationResult.retrieveAll();
            if("true".equals(drugSubmission.getDoctorInformations())){
                if(StringUtil.isEmpty(doctorInformationDto.getName())){
                    errorMap.put("dName", "GENERAL_ERR0006");
                }
                if(StringUtil.isEmpty(doctorInformationDto.getSpeciality())){
                    errorMap.put("dSpeciality", "GENERAL_ERR0006");
                }
                if(StringUtil.isEmpty(doctorInformationDto.getSubSpeciality())){
                    errorMap.put("dSubSpeciality", "GENERAL_ERR0006");
                }
                if(StringUtil.isEmpty(doctorInformationDto.getQualification())){
                    errorMap.put("dQualification", "GENERAL_ERR0006");
                }
                verifyRfcCommon(request, errorMap);
                if (errorMap.isEmpty()) {
                    valRFC(request,drugPrescribedDispensed);
                }
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
            DataSubmissionDto dataSubmissionDto= currentDpDataSubmission.getDataSubmissionDto();
            DrugSubmissionDto drugSubmission = drugPrescribedDispensed.getDrugSubmission();
            String dpLateReasonRadio=ParamUtil.getRequestString(request, "dpLateReasonRadio");
            String remarks=ParamUtil.getRequestString(request, "remarks");
            if(drugSubmission.getReasonMandatory()){
                if(StringUtil.isEmpty(dpLateReasonRadio)){
                    errorMap.put("dpLateReasonRadio", "GENERAL_ERR0006");
                }
                if(StringUtil.isEmpty(remarks)){
                    errorMap.put("remarks", "GENERAL_ERR0006");
                }
            }
            if(StringUtil.isNotEmpty(remarks)){
                if (remarks.length() > 500) {
                    Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                    repMap.put("maxlength", "500");
                    repMap.put("field", "Reason for Late Submission");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0041", repMap);
                    errorMap.put("remarks", errMsg);
                }

            }
            dataSubmissionDto.setDpLateReasonRadio(dpLateReasonRadio);
            dataSubmissionDto.setRemarks(remarks);
            // declaration
            String[] declaration = ParamUtil.getStrings(bpc.request, "declaration");
            if(declaration != null && declaration.length >0){
                dataSubmissionDto.setDeclaration(declaration[0]);
            }else{
                dataSubmissionDto.setDeclaration(null);
                errorMap.put("declaration", "GENERAL_ERR0006");
            }
            currentDpDataSubmission.setDataSubmissionDto(dataSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.DP_DATA_SUBMISSION, currentDpDataSubmission);
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
            if(dpOldSuperDataSubmissionDto != null){
                if(dpOldSuperDataSubmissionDto.getDrugPrescribedDispensedDto()!= null){
                    if(drugPrescribedDispensed.getDrugSubmission().equals(dpOldSuperDataSubmissionDto.getDrugPrescribedDispensedDto().getDrugSubmission())){
                        if(drugPrescribedDispensed.getDrugMedicationDtos().equals(dpOldSuperDataSubmissionDto.getDrugPrescribedDispensedDto().getDrugMedicationDtos())){
                            ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
                        }
                    }
                }

            }
        }
    }
}
