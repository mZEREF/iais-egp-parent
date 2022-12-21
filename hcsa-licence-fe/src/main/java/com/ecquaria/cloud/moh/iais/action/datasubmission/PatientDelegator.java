package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.DsRfcHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Process: MohARPatientInformationManual
 *
 * @Description PatientDelegator
 * @Auther chenlei on 10/22/2021.
 */
@Delegator("patientDelegator")
@Slf4j
public class PatientDelegator extends CommonDelegator {
    private static final String CURRENT_PAGE_STAGE = "currentPageStage";
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
            patient.setPreviousIdentification(Boolean.TRUE);
            patientInfoDto.setRetrievePrevious(true);
            patientInfoDto.setPatient(patient);
            patientInfoDto.setAppType(arSuperDataSubmission.getAppType());
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission, bpc.request);
        }
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, CURRENT_PAGE_STAGE, ACTION_TYPE_PAGE);
        ParamUtil.setRequestAttr(bpc.request, "ageMsg", DataSubmissionHelper.getAgeMessage(DataSubmissionConstant.DS_SHOW_PATIENT));
        ParamUtil.setRequestAttr(bpc.request, "hbdAgeMsg", DataSubmissionHelper.getAgeMessage(DataSubmissionConstant.DS_SHOW_HUSBAND));

    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto currentSuper = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        PatientInfoDto patientInfo = getPatientInfoFromPage(bpc.request, currentSuper.getOrgId(),true);
        currentSuper.setPatientInfoDto(patientInfo);
        DataSubmissionHelper.setCurrentArDataSubmission(currentSuper, bpc.request);
    }

    private PatientInfoDto getPatientInfoFromPage(HttpServletRequest request, String orgId, boolean isAmend) {
        PatientInfoDto patientInfo = new PatientInfoDto();


        PatientDto patient = ControllerHelper.get(request, PatientDto.class);
        HusbandDto husband = ControllerHelper.get(request, HusbandDto.class, "Hbd");

        if (isAmend) {
            //amend just replace field need filled
            patientInfo.setPatient(patient);
            patientInfo.setHusband(husband);
            PatientDto oldPatient = patientInfo.getPatient();
            oldPatient.setName(patient.getName());
            oldPatient.setBirthDate(patient.getBirthDate());
            oldPatient.setNationality(patient.getNationality());
            oldPatient.setEthnicGroup(patient.getEthnicGroup());
            oldPatient.setEthnicGroupOther(patient.getEthnicGroupOther());
            oldPatient.setPreviousIdentification(patient.getPreviousIdentification());
            patient = oldPatient;


            HusbandDto oldHusband = patientInfo.getHusband();
            oldHusband.setName(husband.getName());
            oldHusband.setBirthDate(husband.getBirthDate());
            oldHusband.setNationality(husband.getNationality());
            oldHusband.setEthnicGroup(husband.getEthnicGroup());
            oldHusband.setEthnicGroupOther(husband.getEthnicGroupOther());
            husband = oldHusband;
        } else {
            String identityNo = ParamUtil.getString(request, "identityNo");
            String hasIdNumber = ParamUtil.getString(request, "ptHasIdNumber");
            String idType = patientService.judgeIdType(hasIdNumber, identityNo);

            patient.setIdType(idType);
            patient.setIdNumber(identityNo);
            patient.setOrgId(orgId);

            String hubHasIdNumber = ParamUtil.getString(request, "hubHasIdNumber");
            String hubIdType = patientService.judgeIdType(hubHasIdNumber, husband.getIdNumber());
            husband.setIdType(hubIdType);

            patientInfo.setIsPreviousIdentification(Boolean.TRUE.equals(Boolean.TRUE.equals(patient.getPreviousIdentification())) ? IaisEGPConstant.YES : IaisEGPConstant.NO);
        }

        patientInfo.setAppType(isAmend?DataSubmissionConsts.DS_APP_TYPE_NEW:DataSubmissionConsts.DS_APP_TYPE_NEW);
        DsRfcHelper.prepare(patient);
        patientInfo.setPatient(patient);

        DsRfcHelper.prepare(husband);
        patientInfo.setHusband(husband);

        String patientCode = null;
        if (Boolean.TRUE.equals(patient.getPreviousIdentification())) {
            PatientDto previous = ControllerHelper.get(request, PatientDto.class, "pre", "");

            PatientDto db = retrievePrePatient(patient, previous);

            if (db != null && !StringUtils.isEmpty(db.getId())) {
                patientInfo.setRetrievePrevious(true);
                previous = db;
            } else {
                patientInfo.setRetrievePrevious(false);
            }

            patientInfo.setPrevious(previous);

            // retrieve patient code if exist previous patient information
            patientCode = previous.getPatientCode();
        } else {
            patientInfo.setRetrievePrevious(false);
            patientInfo.setPrevious(null);
        }

        patient.setPatientCode(patientService.getPatientCode(patientCode));

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
        ArSuperDataSubmissionDto currentSuper = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        PatientInfoDto patientInfo = getPatientInfoFromPage(bpc.request, currentSuper.getOrgId(),true);
        String actionType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        currentSuper.setPatientInfoDto(patientInfo);
        if (ACTION_TYPE_SUBMISSION.equals(actionType)) {
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
            ValidationResult validationResult = WebValidationHelper.validateProperty(patientInfo, "rfc");
            errorMap.putAll(validationResult.retrieveAll());
            if (processErrorMsg(errorMap, bpc.request)) {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_PAGE);
            }
        }

        DataSubmissionHelper.setCurrentArDataSubmission(currentSuper, bpc.request);
    }

    private PatientDto retrievePrePatient(PatientDto patient, PatientDto previous) {
        String idType = patientService.judgeIdType(previous.getIdNumber());
        return patientService.getActiveArPatientByConds(idType, previous.getIdNumber(),
                previous.getNationality(), patient.getOrgId());
    }

    private boolean processErrorMsg(Map<String, String> errorMap, HttpServletRequest request) {
        if (IaisCommonUtils.isNotEmpty(errorMap)) {
            String currentStage = ParamUtil.getRequestString(request, CURRENT_PAGE_STAGE);
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, currentStage);
            return false;
        }
        return true;
    }
}
