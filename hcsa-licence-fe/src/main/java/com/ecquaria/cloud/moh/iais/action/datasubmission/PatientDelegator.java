package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranet.user.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.HusbandDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sop.webflow.rt.api.BaseProcessClass;

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
        ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(bpc.request);
        PatientInfoDto patientInfo = getPatientInfoFromPage(bpc.request, currentSuper.getOrgId(),true);
        if (patientInfo!=null && patientInfo.getPatient()!=null){
            patientInfo.getPatient().setPatientCode(arOldSuperDataSubmissionDto.getPatientInfoDto().getPatient().getPatientCode());
            patientInfo.getPatient().setSubmissionId(arOldSuperDataSubmissionDto.getPatientInfoDto().getPatient().getSubmissionId());
        }
        currentSuper.setPatientInfoDto(patientInfo);
        DataSubmissionHelper.setCurrentArDataSubmission(currentSuper, bpc.request);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if (CommonDelegator.ACTION_TYPE_CONFIRM.equals(actionType)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(patientInfo, "rfc");
            errorMap = validationResult.retrieveAll();
            errorMap.putAll(doValidationBirthDate(patientInfo.getPatient(),patientInfo.getHusband()));
            String crud_action_type = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE);
            if ("confirm".equals(crud_action_type)) {
                errorMap = validationResult.retrieveAll();
                verifyCommon(bpc.request, errorMap);
                if(errorMap.isEmpty()){
                    valRFC(bpc.request, patientInfo);
                }
            }
        }
        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            return;
        }
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
            oldPatient.setOrgId(orgId);
            oldPatient.setIdType(patient.getIdType());
            oldPatient.setIdNumber(patient.getIdNumber());
            oldPatient.setName(patient.getName());
            oldPatient.setBirthDate(patient.getBirthDate());
            oldPatient.setNationality(patient.getNationality());
            oldPatient.setEthnicGroup(patient.getEthnicGroup());
            oldPatient.setEthnicGroupOther(patient.getEthnicGroupOther());
            oldPatient.setPreviousIdentification(patient.getPreviousIdentification());
            patient = oldPatient;


            HusbandDto oldHusband = patientInfo.getHusband();
            oldHusband.setIdType(husband.getIdType());
            oldHusband.setIdNumber(husband.getIdNumber());
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
        PatientInfoDto patientInfo = currentSuper.getPatientInfoDto();
        String actionType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        currentSuper.setPatientInfoDto(patientInfo);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (CommonDelegator.ACTION_TYPE_CONFIRM.equals(actionType)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(patientInfo, "save");
            errorMap = validationResult.retrieveAll();

            String crud_action_type = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE);
            if ("confirm".equals(crud_action_type)) {
                errorMap = validationResult.retrieveAll();
                verifyCommon(bpc.request, errorMap);
                if(errorMap.isEmpty()){
                    valRFC(bpc.request, patientInfo);
                }
            }
        }

        DataSubmissionHelper.setCurrentArDataSubmission(currentSuper, bpc.request);
    }

    private PatientDto retrievePrePatient(PatientDto patient, PatientDto previous) {
        String idType = patientService.judgeIdType(previous.getIdNumber());
        return patientService.getActiveArPatientByConds(idType, previous.getIdNumber(),
                previous.getNationality(), patient.getOrgId());
    }

    private boolean checkRfcVal(PatientInfoDto newPatientInfo, PatientInfoDto oldPatient) {
        if (oldPatient.getPatient() != null && newPatientInfo.getPatient()!= null) {
            oldPatient.getPatient().setId(null);
            oldPatient.getPatient().setSubmissionId(null);
            oldPatient.getHusband().setId(null);
            oldPatient.getHusband().setPatientId(null);
        }
        return newPatientInfo.equals(oldPatient);
    }


    protected void valRFC(HttpServletRequest request, PatientInfoDto patientInfoDto){
        if(isRfc(request)){
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if(arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getPatientInfoDto()!= null && checkRfcVal(patientInfoDto, arOldSuperDataSubmissionDto.getPatientInfoDto())){
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
            }
        }
    }

    private Map<String, String> doValidationBirthDate(PatientDto patientDto,HusbandDto husbandDto){
        Map<String, String> errMsg = IaisCommonUtils.genNewHashMap();
        if (StringUtil.isNotEmpty(patientDto.getBirthDate())){
            String age1 = MasterCodeUtil.getCodeDesc("PT_AGE_001");
            String age2 = MasterCodeUtil.getCodeDesc("PT_AGE_002");
            int age = Formatter.getAge(patientDto.getBirthDate());
            int husAge = Formatter.getAge(husbandDto.getBirthDate());
            if (Integer.parseInt(age1) > age || Integer.parseInt(age2) < age) {
                errMsg.put("birthDate",DataSubmissionHelper.getAgeMessage(DataSubmissionConstant.DS_SHOW_PATIENT));
            }
            if (Integer.parseInt(age1) > husAge || Integer.parseInt(age2) < husAge) {
                errMsg.put("birthDateHbd",DataSubmissionHelper.getAgeMessage(DataSubmissionConstant.DS_SHOW_HUSBAND));
            }
        }
        return errMsg;
    }
}
