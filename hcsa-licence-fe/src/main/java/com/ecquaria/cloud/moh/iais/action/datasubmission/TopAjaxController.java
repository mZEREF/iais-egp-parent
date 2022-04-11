package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.FamilyPlanDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInformationDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.TopPatientSelectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

/**
 * @Description Ajax
 * @Auther zhixing on 2/14/2022.
 */

@RequestMapping(value = "/top")
@RestController
@Slf4j
public class TopAjaxController {

    @Autowired
    private TopPatientSelectService topPatientSelectService;

    @PostMapping(value = "/retrieve-identification")
    public @ResponseBody
    Map<String, Object> retrieveIdentification(HttpServletRequest request) {
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        String nationality = ParamUtil.getString(request, "nationality");
        PatientInformationDto patientInformation = new PatientInformationDto();
        patientInformation.setIdType(idType);
        patientInformation.setIdNumber(idNo);
        patientInformation.setNationality(nationality);
        Map<String, Object> result = IaisCommonUtils.genNewHashMap(3);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ValidationResult vr = WebValidationHelper.validateProperty(patientInformation, "ART");
        if (vr != null && vr.isHasErrors()) {
            Map<String, String> params = IaisCommonUtils.genNewHashMap();
            params.put("idType", "idType");
            params.put("idNumber", "idNumber");
            params.put("nationality", "nationality");
            errorMap.putAll(vr.retrieveAll(params));
        }
        if (!errorMap.isEmpty()) {
            result.put(IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        } else {
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
            String orgId = Optional.ofNullable(loginContext).map(LoginContext::getOrgId).orElse("");
            PatientInformationDto top = topPatientSelectService.getTopPatientSelect(idType, idNo, nationality, orgId);
            if(top.getPatientAge() ==null){
                top.setPatientAge(0);
            }
            if (top != null) {
                patientInformation = top;
            }

            try {
                int age= -Formatter.compareDateByDay(top.getBirthData());
                patientInformation.setPatientAge(age/365);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }

            FamilyPlanDto familyPlanDto=new FamilyPlanDto();
            if(patientInformation.getPatientAge()<16 && !StringUtil.isEmpty(patientInformation.getPatientAge())){
                familyPlanDto.setNeedHpbConsult(true);
            }

            result.put("selection", patientInformation);
        }
        return result;
    }
}
