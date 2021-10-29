package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.HusbandDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Description PatientDelegator
 * @Auther chenlei on 10/22/2021.
 */
@Delegator("patientDelegator")
@Slf4j
public class PatientDelegator extends CommonDelegator{

    @Autowired
    private PatientService patientService;

    @Override
    public void start(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto currentArDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DataSubmissionDto dataSubmission = currentArDataSubmission.getCurrentDataSubmissionDto();
        if (dataSubmission == null) {
            dataSubmission = new DataSubmissionDto();
            currentArDataSubmission.setCurrentDataSubmissionDto(dataSubmission);
        }
        dataSubmission.setSubmissionType(DataSubmissionConsts.DATA_SUBMISSION_TYPE_AR);
        dataSubmission.setCycleStage(DataSubmissionConsts.DATA_SUBMISSION_CYCLE_STAGE_PATIENT);
        DataSubmissionHelper.setCurrentArDataSubmission(currentArDataSubmission, bpc.request);
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Patient Information</strong>");
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        PatientInfoDto patientInfo = getPatientInfoFromPage(bpc.request);
        ArSuperDataSubmissionDto dataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        dataSubmission.setPatientInfoDto(patientInfo);
        validatePageData(bpc.request, patientInfo, "AR",ACTION_TYPE_CONFIRM);
        DataSubmissionHelper.setCurrentArDataSubmission(dataSubmission, bpc.request);
    }

    private PatientInfoDto getPatientInfoFromPage(HttpServletRequest request) {
        PatientInfoDto patientInfo = new PatientInfoDto();
        PatientDto patient = ControllerHelper.get(request, PatientDto.class);
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
        patient.setPatientCode(UUID.randomUUID().toString());
        patientInfo.setPatient(patient);
        if (patient.isPreviousIdentification()) {
            String preIdType = ParamUtil.getString(request, "preIdType");
            String preIdNumber = ParamUtil.getString(request, "preIdNumber");
            String preNationality = ParamUtil.getString(request, "preNationality");
            String retrievePrevious = ParamUtil.getString(request, "retrievePrevious");
            PatientDto previous = new PatientDto();
            previous.setIdType(preIdType);
            previous.setIdNumber(preIdNumber);
            previous.setNationality(preNationality);
            if (AppConsts.YES.equals(retrievePrevious)) {
                previous = patientService.getPatientDto(preIdNumber, preNationality, patient.getOrgId());
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
        return patientInfo;
    }

}
