package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.HusbandDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Patient Information</strong>");
    }

    @Override
    public void returnStep(BaseProcessClass bpc) {

    }

    @Override
    public void preparePage(BaseProcessClass bpc) {

    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        PatientInfoDto patientInfo = getPatientInfoFromPage(bpc.request);
        ValidationResult result = WebValidationHelper.validateProperty(patientInfo, "AR");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (result != null) {
            errorMap.putAll(result.retrieveAll());
        }
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
        } else {
            ArSuperDataSubmissionDto dataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
            dataSubmission.setPatientInfoDto(patientInfo);
        }
    }

    private PatientInfoDto getPatientInfoFromPage(HttpServletRequest request) {
        PatientInfoDto patientInfo = new PatientInfoDto();
        PatientDto patient = new PatientDto();
        patient.setName(ParamUtil.getString(request, "name"));
        patient.setIdType(ParamUtil.getString(request, "idType"));
        patient.setIdNumber(ParamUtil.getString(request, "idNumber"));
        patient.setBirthDate(ParamUtil.getString(request, "birthDate"));
        patient.setNationality(ParamUtil.getString(request, "nationality"));
        patient.setEthnicGroup(ParamUtil.getString(request, "ethnicGroup"));
        patient.setEthnicGroupOther(ParamUtil.getString(request, "ethnicGroupOther"));
        boolean previousIdentification = AppConsts.YES.equals(ParamUtil.getString(request, "previousIdentification"));
        patient.setPreviousIdentification(previousIdentification);
        patientInfo.setPatient(patient);
        if (previousIdentification) {
            String preIdNumber = ParamUtil.getString(request, "preIdNumber");
            String preNationality = ParamUtil.getString(request, "preNationality");
            String orgId = null;
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
            if (loginContext != null) {
                orgId = loginContext.getOrgId();
            }
            PatientDto previous = patientService.getPatientDto(preIdNumber, preNationality, orgId);
            patientInfo.setPrevious(previous);
        }
        HusbandDto husband = new HusbandDto();
        husband.setName(ParamUtil.getString(request, "nameHbd"));
        husband.setIdType(ParamUtil.getString(request, "idTypeHbd"));
        husband.setIdNumber(ParamUtil.getString(request, "idNumberHbd"));
        husband.setBirthDate(ParamUtil.getString(request, "birthDateHbd"));
        husband.setNationality(ParamUtil.getString(request, "nationalityHbd"));
        husband.setEthnicGroup(ParamUtil.getString(request, "ethnicGroup"));
        husband.setEthnicGroupOther(ParamUtil.getString(request, "ethnicGroupOtherHbd"));
        patientInfo.setHusband(husband);
        return patientInfo;
    }

    @Override
    public void draft(BaseProcessClass bpc) {

    }

    @Override
    public void submission(BaseProcessClass bpc) {

    }

    @Override
    public void pageAction(BaseProcessClass bpc) {

    }

    @Override
    public void pageConfirmAction(BaseProcessClass bpc) {

    }

}
