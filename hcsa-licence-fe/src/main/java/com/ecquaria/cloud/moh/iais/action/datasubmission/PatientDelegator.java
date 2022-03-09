package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.HusbandDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.DsRfcHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * Process: MohARPatientInformationManual
 *
 * @Description PatientDelegator
 * @Auther chenlei on 10/22/2021.
 */
@Delegator("patientDelegator")
@Slf4j
public class PatientDelegator extends CommonDelegator {

    @Autowired
    private PatientService patientService;

    @Override
    public void start(BaseProcessClass bpc) {
        super.start(bpc);
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if (!DataSubmissionConsts.DS_APP_TYPE_NEW.equals(arSuperDataSubmission.getAppType())) {
            // set current as previous at the beginning
            PatientInfoDto patientInfoDto = arSuperDataSubmission.getPatientInfoDto();
            PatientDto patient = patientInfoDto.getPatient();
            patientInfoDto.setPrevious((PatientDto) CopyUtil.copyMutableObject(patient));
            patient.setPreviousIdentification(true);
            patientInfoDto.setRetrievePrevious(true);
            patientInfoDto.setPatient(patient);
            patientInfoDto.setAppType(arSuperDataSubmission.getAppType());
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission, bpc.request);
        }
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        //ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Patient Information</strong>");
        ParamUtil.setRequestAttr(bpc.request, "ageMsg", DataSubmissionHelper.getAgeMessage(DataSubmissionConstant.DS_SHOW_PATIENT));
        ParamUtil.setRequestAttr(bpc.request, "hbdAgeMsg", DataSubmissionHelper.getAgeMessage(DataSubmissionConstant.DS_SHOW_HUSBAND));
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        PatientInfoDto patientInfo = getPatientInfoFromPage(bpc.request);
        String actionType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if (ACTION_TYPE_DRAFT.equals(actionType)) {
            // validatePageForDraft(patientInfo.getPatient(), bpc.request);
        } else {
            String profile = DataSubmissionConsts.DS_APP_TYPE_RFC.equals(patientInfo.getAppType()) ? "rfc" : "save";
            validatePageData(bpc.request, patientInfo, profile, ACTION_TYPE_CONFIRM);
            // check whether user change any data
        }
    }

    private PatientInfoDto getPatientInfoFromPage(HttpServletRequest request) {
        ArSuperDataSubmissionDto currentArDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(request);
        PatientInfoDto patientInfo = currentArDataSubmission.getPatientInfoDto();
        if (patientInfo == null) {
            patientInfo = new PatientInfoDto();
        }
        PatientDto patient = patientInfo.getPatient();
        if (patient == null) {
            patient = new PatientDto();
        } else if (DataSubmissionConsts.DS_APP_TYPE_NEW.equals(currentArDataSubmission.getAppType())) {
            patient.setId(null);
        }
        ControllerHelper.get(request, patient);
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        if (loginContext != null) {
            patient.setOrgId(loginContext.getOrgId());
        }
        DsRfcHelper.prepare(patient);
        patientInfo.setPatient(patient);
        String patientCode = patient.getPatientCode();
        // check previous
        if (!DataSubmissionConsts.DS_APP_TYPE_NEW.equals(currentArDataSubmission.getAppType())) {
            patient.setPreviousIdentification(true);
            PatientDto previous = patientInfo.getPrevious();
            patientCode = previous.getPatientCode();
        } else {
            if (patient.isPreviousIdentification()) {
                String retrievePrevious = ParamUtil.getString(request, "retrievePrevious");
                patientInfo.setRetrievePrevious(AppConsts.YES.equals(retrievePrevious));
                PatientDto previous = ControllerHelper.get(request, PatientDto.class, "pre", "");
                if (patientInfo.isRetrievePrevious()) {
                    PatientDto db = patientService.getActiveArPatientByConds(previous.getIdType(), previous.getIdNumber(),
                            previous.getNationality(), patient.getOrgId());
                    if (db != null && !StringUtil.isEmpty(db.getId())) {
                        previous = db;
                    }
                }
                patientInfo.setPrevious(previous);
                patientCode = previous.getPatientCode();
            } else {
                patientInfo.setRetrievePrevious(false);
                patientInfo.setPrevious(null);
                patientCode = null;
            }
        }
        patient.setPatientCode(patientService.getPatientCode(patientCode));
        HusbandDto husband = ControllerHelper.get(request, HusbandDto.class, "Hbd");
        DsRfcHelper.prepare(husband);
        patientInfo.setHusband(husband);
        String amendReason = ParamUtil.getString(request, "amendReason");
        String amendReasonOther = ParamUtil.getString(request, "amendReasonOther");
        patientInfo.setAmendReason(StringUtil.getNonNull(amendReason));
        patientInfo.setAmendReasonOther(StringUtil.getNonNull(amendReasonOther));
        patientInfo.setAppType(currentArDataSubmission.getAppType());
        currentArDataSubmission.setPatientInfoDto(patientInfo);
        // amend reason
        DataSubmissionDto dataSubmission = currentArDataSubmission.getDataSubmissionDto();
        dataSubmission.setAmendReason(amendReason);
        dataSubmission.setAmendReasonOther(amendReasonOther);
        currentArDataSubmission.setDataSubmissionDto(dataSubmission);
        DataSubmissionHelper.setCurrentArDataSubmission(currentArDataSubmission, request);
        return patientInfo;
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConsts.DS_PATIENT_ART);
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DsRfcHelper.handle(arSuperDataSubmission.getPatientInfoDto());
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission, bpc.request);
    }

    @Override
    public void submission(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        PatientInfoDto patientInfoDto = arSuperDataSubmission.getPatientInfoDto();
        patientInfoDto.getPatient().setId(null);
        patientInfoDto.getHusband().setId(null);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission, bpc.request);
    }

}
