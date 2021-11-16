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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @Description PatientDelegator
 * @Auther chenlei on 10/22/2021.
 */
@Delegator("patientDelegator")
@Slf4j
public class PatientDelegator extends CommonDelegator {

    @Autowired
    private PatientService patientService;

    @Autowired
    private GenerateIdClient generateIdClient;

    /*@Override
    public void start(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto currentArDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DataSubmissionDto dataSubmission = currentArDataSubmission.getCurrentDataSubmissionDto();
        if (dataSubmission == null) {
            dataSubmission = new DataSubmissionDto();
            currentArDataSubmission.setCurrentDataSubmissionDto(dataSubmission);
        }
        dataSubmission.setSubmissionType(currentArDataSubmission.getSubmissionType());
        dataSubmission.setCycleStage(DataSubmissionConsts.DS_CYCLE_STAGE_PATIENT);
        dataSubmission.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        currentArDataSubmission.setCycleDto(initCycleDto(currentArDataSubmission));
        DataSubmissionHelper.setCurrentArDataSubmission(currentArDataSubmission, bpc.request);
    }*/

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Patient Information</strong>");
        ParamUtil.setRequestAttr(bpc.request, "ageMsg", DataSubmissionHelper.getPatientAgeMessage());
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        PatientInfoDto patientInfo = getPatientInfoFromPage(bpc.request);
        String actionType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if (ACTION_TYPE_DRAFT.equals(actionType)) {
            // validatePageForDraft(patientInfo.getPatient(), bpc.request);
        } else {
            validatePageData(bpc.request, patientInfo, "save", ACTION_TYPE_CONFIRM);
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
        } else {
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
        String patientCode = Optional.ofNullable(patientInfo.getPatient())
                .map(dto -> dto.getPatientCode())
                .orElseGet(() -> generateIdClient.getSeqId().getEntity());
        patient.setPatientCode(patientCode);
        patientInfo.setPatient(patient);
        // check previous
        if (patient.isPreviousIdentification()) {
            String retrievePrevious = ParamUtil.getString(request, "retrievePrevious");
            patientInfo.setRetrievePrevious(AppConsts.YES.equals(retrievePrevious));
            PatientDto previous = ControllerHelper.get(request, PatientDto.class, "pre", "");
            if (patientInfo.isRetrievePrevious()) {
                PatientDto db = patientService.getPatientDto(previous.getIdType(), previous.getIdNumber(), previous.getNationality(),
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
        if (StringUtil.isEmpty(husband.getEthnicGroup())) {
            husband.setEthnicGroup("");
        }
        patientInfo.setHusband(husband);
        currentArDataSubmission.setPatientInfoDto(patientInfo);
        // ret-set cycle dto
        CycleDto cycleDto = currentArDataSubmission.getCycleDto();
        if (cycleDto == null) {
            cycleDto = initCycleDto(currentArDataSubmission);
        }
        cycleDto.setPatientCode(patientCode);
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
        String hicCode = Optional.ofNullable(currentArDataSubmission.getPremisesDto())
                .map(PremisesDto::getHciCode)
                .orElse("");
        cycleDto.setHciCode(hicCode);
        cycleDto.setDsType(DataSubmissionConsts.DS_CYCLE_PATIENT_ART);
        cycleDto.setCycleType(DataSubmissionConsts.DS_CYCLE_STAGE_PATIENT);
        return cycleDto;
    }

}
