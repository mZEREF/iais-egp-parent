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
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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
            patientInfoDto.setPatient(patient);
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission, bpc.request);
        }
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Patient Information</strong>");
        ParamUtil.setRequestAttr(bpc.request, "ageMsg", DataSubmissionHelper.getPatientAgeMessage("Patient"));
        ParamUtil.setRequestAttr(bpc.request, "hbdAgeMsg", DataSubmissionHelper.getPatientAgeMessage("Husband"));
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
        if (StringUtil.isNotEmpty(patient.getName())) {
            patient.setName(patient.getName().toUpperCase(AppConsts.DFT_LOCALE));
        }
        // for oval validation
        if (StringUtil.isEmpty(patient.getEthnicGroup())) {
            patient.setEthnicGroup("");
        }
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        if (loginContext != null) {
            patient.setOrgId(loginContext.getOrgId());
        }
        // for oval validation
        patient.setEthnicGroupOther(StringUtil.getNonNull(patient.getEthnicGroupOther()));
        patient.setPatientCode(patientService.getPatientCode(patient.getPatientCode()));
        patient.setPatientType(DataSubmissionConsts.DS_PATIENT_ART);
        patientInfo.setPatient(patient);
        // check previous
        if (patient.isPreviousIdentification() && !DataSubmissionConsts.DS_APP_TYPE_RFC.equals(patientInfo.getAppType())) {
            String retrievePrevious = ParamUtil.getString(request, "retrievePrevious");
            patientInfo.setRetrievePrevious(AppConsts.YES.equals(retrievePrevious));
            PatientDto previous = ControllerHelper.get(request, PatientDto.class, "pre", "");
            if (patientInfo.isRetrievePrevious()) {
                PatientDto db = patientService.getActiveArPatientByConds(previous.getIdType(), previous.getIdNumber(), previous.getNationality(),
                        patient.getOrgId());
                if (db != null && !StringUtil.isEmpty(db.getId())) {
                    previous = db;
                }
            }
            patientInfo.setPrevious(previous);
        }
        HusbandDto husband = ControllerHelper.get(request, HusbandDto.class, "Hbd");
        if (StringUtil.isNotEmpty(husband.getName())) {
            husband.setName(husband.getName().toUpperCase(AppConsts.DFT_LOCALE));
        }
        // for oval validation
        husband.setEthnicGroupOther(StringUtil.getNonNull(husband.getEthnicGroupOther()));
        patientInfo.setHusband(husband);
        String amendReason = ParamUtil.getString(request, "amendReason");
        String amendReasonOther = ParamUtil.getString(request, "amendReasonOther");
        patientInfo.setAmendReason(StringUtil.getNonNull(amendReason));
        patientInfo.setAmendReasonOther(StringUtil.getNonNull(amendReasonOther));
        patientInfo.setAppType(currentArDataSubmission.getAppType());
        currentArDataSubmission.setPatientInfoDto(patientInfo);

        DataSubmissionDto dataSubmission = currentArDataSubmission.getDataSubmissionDto();
        dataSubmission.setAmendReason(amendReason);
        dataSubmission.setAmendReasonOther(amendReasonOther);
        // ret-set cycle dto
        CycleDto cycleDto = currentArDataSubmission.getCycleDto();
        if (cycleDto == null) {
            cycleDto = initCycleDto(currentArDataSubmission);
        }
        // cycleDto.setPatientCode(patientCode);
        cycleDto.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        currentArDataSubmission.setCycleDto(cycleDto);
        DataSubmissionHelper.setCurrentArDataSubmission(currentArDataSubmission, request);
        return patientInfo;
    }

    private CycleDto initCycleDto(ArSuperDataSubmissionDto currentArDataSubmission) {
        CycleDto cycleDto = currentArDataSubmission.getCycleDto();
        if (cycleDto == null) {
            cycleDto = new CycleDto();
        }
        cycleDto.setHciCode(currentArDataSubmission.getHciCode());
        cycleDto.setDsType(DataSubmissionConsts.DS_CYCLE_PATIENT_ART);
        cycleDto.setCycleType(DataSubmissionConsts.DS_CYCLE_STAGE_PATIENT);
        return cycleDto;
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConsts.DS_PATIENT_ART);
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
