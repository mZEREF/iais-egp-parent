package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

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
        ParamUtil.setRequestAttr(bpc.request, "ageMsg", DataSubmissionHelper.getPatientAgeMessage("Submission"));
        ParamUtil.setRequestAttr(bpc.request, "hbdAgeMsg", DataSubmissionHelper.getPatientAgeMessage("Medication"));
    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        DpSuperDataSubmissionDto dpSuperDataSubmissionDto= DataSubmissionHelper.getCurrentDpDataSubmission(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, dpSuperDataSubmissionDto);
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConsts.DS_PATIENT_ART);

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
        ControllerHelper.get(request, drugSubmission);
        drugPrescribedDispensed.setDrugSubmission(drugSubmission);
        DrugMedicationDto drugMedication = ControllerHelper.get(request, DrugMedicationDto.class, "Med");
        drugPrescribedDispensed.setDrugMedication(drugMedication);
        currentDpDataSubmission.setDrugPrescribedDispensedDto(drugPrescribedDispensed);
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, currentDpDataSubmission);
        validatePageData(request, drugPrescribedDispensed,"save",ACTION_TYPE_CONFIRM);

       /* HttpServletRequest request=bpc.request;
        String doctorReignNo= ParamUtil.getString(bpc.request, "doctorReignNo");
        String drugType= ParamUtil.getRequestString(bpc.request, "drugType");
        String prescriptionDates = ParamUtil.getRequestString(request, "efoDateStarted");
        Date prescriptionDate = DateUtil.parseDate(prescriptionDates, AppConsts.DEFAULT_DATE_FORMAT);
        String dispensingDates = ParamUtil.getRequestString(request, "dispensingDate");
        Date dispensingDate = DateUtil.parseDate(dispensingDates, AppConsts.DEFAULT_DATE_FORMAT);
        String medication= ParamUtil.getRequestString(bpc.request, "medication");
        String startDates = ParamUtil.getRequestString(request, "startDate");
        Date startDate = DateUtil.parseDate(startDates, AppConsts.DEFAULT_DATE_FORMAT);
        String endDates = ParamUtil.getRequestString(request, "endDate");
        Date endDate = DateUtil.parseDate(endDates, AppConsts.DEFAULT_DATE_FORMAT);
        String diagnosis= ParamUtil.getRequestString(bpc.request, "diagnosis");
        */
    }
}
