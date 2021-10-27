package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

/**
 * @Description Ajax
 * @Auther chenlei on 10/21/2021.
 */
@RequestMapping(value = "/ar")
@Controller
@Slf4j
public class ArAjaxController {

    @Autowired
    private PatientService patientService;

    @PostMapping(value = "/retrieve-identification")
    public @ResponseBody
    PatientDto retrieveIdentification(HttpServletRequest request) {
        String idNo = ParamUtil.getString(request, "idNo");
        String nationality = ParamUtil.getString(request, "nationality");
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        String orgId = Optional.ofNullable(loginContext).map(LoginContext::getOrgId).orElse("");
        PatientDto patient = patientService.getPatientDto(idNo, nationality, orgId);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_PATIENT, patient);
        return patient;
    }

    @PostMapping(value = "/retrieve-valid-selection")
    public @ResponseBody
    Map<String, Object> retrieveValidSelection(HttpServletRequest request) {
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        String nationality = ParamUtil.getString(request, "nationality");
        CycleStageSelectionDto dto = new CycleStageSelectionDto();
        dto.setPatientIdType(idType);
        dto.setPatientIdNumber(idNo);
        dto.setPatientNationality(nationality);
        Map<String, Object> result = IaisCommonUtils.genNewHashMap(3);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ValidationResult vr = WebValidationHelper.validateProperty(dto, "AR");
        if (vr != null) {
            errorMap.putAll(vr.retrieveAll());
        }
        if (!errorMap.isEmpty()) {
            result.put(IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        } else {
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
            String orgId = Optional.ofNullable(loginContext).map(LoginContext::getOrgId).orElse("");
            PatientDto patient = patientService.getPatientDto(idNo, nationality, orgId);
            ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_PATIENT, patient);
            if (patient != null) {
                dto.setPatientName(patient.getName());
                // lastStage & undergoingCycle
                result.put("selection", dto);
            }
        }
        String currCycle = ParamUtil.getString(request, "currCycle");
        String currStage = ParamUtil.getString(request, "currStage");
        result.put("stagHtmls", DataSubmissionHelper.genOptionHtmls(DataSubmissionHelper.getNextStageForAr(currCycle, currStage)));
        return result;
    }

}
