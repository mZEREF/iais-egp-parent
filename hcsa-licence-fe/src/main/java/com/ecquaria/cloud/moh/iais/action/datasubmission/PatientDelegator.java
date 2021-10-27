package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.HusbandDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
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
    }

    @Override
    public void draft(BaseProcessClass bpc) {

    }

    @Override
    public void submission(BaseProcessClass bpc) {

    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        PatientInfoDto patientInfo = getPatientInfoFromPage(bpc.request);
        ArSuperDataSubmissionDto dataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        dataSubmission.setPatientInfoDto(patientInfo);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if ("confirm".equals(actionType)) {
            ValidationResult result = WebValidationHelper.validateProperty(patientInfo, "AR");
            if (result != null) {
                errorMap.putAll(result.retrieveAll());
            }
        }
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
        }
    }

    private PatientInfoDto getPatientInfoFromPage(HttpServletRequest request) {
        PatientInfoDto patientInfo = new PatientInfoDto();
        PatientDto patient = ControllerHelper.get(request, PatientDto.class);
        if (StringUtil.isNotEmpty(patient.getName())) {
            patient.setName(patient.getName().toUpperCase(AppConsts.DFT_LOCALE));
        }
        if (StringUtil.isEmpty(patient.getEthnicGroup())) {
            patient.setEthnicGroup("");
        }
        patientInfo.setPatient(patient);
        if (patient.isPreviousIdentification()) {
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
        HusbandDto husband = ControllerHelper.get(request, HusbandDto.class, "Hbd");
        if (StringUtil.isNotEmpty(husband.getName())) {
            husband.setName(husband.getName().toUpperCase(AppConsts.DFT_LOCALE));
        }
        if (StringUtil.isEmpty(husband.getEthnicGroup())) {
            husband.setEthnicGroup("");
        }
        patientInfo.setHusband(husband);
        return patientInfo;
    }

    @Override
    public void pageConfirmAction(BaseProcessClass bpc) {

    }

}
