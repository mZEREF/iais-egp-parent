package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * DrugPrescribedDispensedDelegator
 *
 * @author zhixing
 * @date 2021/11/25
 */

@Delegator("drugPrescribedDispensedDelegator")
@Slf4j
public class DrugPrescribedDispensedDelegator extends DpCommonDelegator{

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

        currentDpDataSubmission.setDrugPrescribedDispensedDto(drugPrescribedDispensed);
        String profile ="DRP";
        validatePageData(bpc.request, drugPrescribedDispensed, profile, ACTION_TYPE_CONFIRM);
        if(DpCommonDelegator.ACTION_TYPE_CONFIRM.equals(ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE))){
            valRFC(request,drugPrescribedDispensed);
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.DP_DATA_SUBMISSION, currentDpDataSubmission);
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
