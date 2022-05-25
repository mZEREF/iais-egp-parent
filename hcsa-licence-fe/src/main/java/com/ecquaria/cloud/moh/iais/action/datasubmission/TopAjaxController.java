package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.TopDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private TopDataSubmissionService topDataSubmissionService;

    @Autowired
    private AppSubmissionService appSubmissionService;

    @PostMapping(value = "/retrieve-identification")
    public @ResponseBody
    Map<String, Object> retrieveIdentification(HttpServletRequest request) {
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        PatientInformationDto patientInformation = new PatientInformationDto();
        patientInformation.setIdType(idType);
        patientInformation.setIdNumber(idNo);
        Map<String, Object> result = IaisCommonUtils.genNewHashMap(2);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ValidationResult vr = WebValidationHelper.validateProperty(patientInformation, "ART");
        if (vr != null && vr.isHasErrors()) {
            Map<String, String> params = IaisCommonUtils.genNewHashMap();
            params.put("idType", "idType");
            params.put("idNumber", "idNumber");
            errorMap.putAll(vr.retrieveAll(params));
        }
        if (!errorMap.isEmpty()) {
            result.put(IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        } else {
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
            String orgId = Optional.ofNullable(loginContext).map(LoginContext::getOrgId).orElse("");
            PatientInformationDto top = topDataSubmissionService.getTopPatientSelect(idType, idNo, orgId);
            /*if(StringUtil.isEmpty(top.getPatientAge())){
                top.setPatientAge(0);
            }*/
            if (top != null) {
                patientInformation = top;
            }

           /* try {
                int age= -Formatter.compareDateByDay(top.getBirthData());
                patientInformation.setPatientAge(age/365);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }

            FamilyPlanDto familyPlanDto=new FamilyPlanDto();
            if(patientInformation.getPatientAge()<16 && !StringUtil.isEmpty(patientInformation.getPatientAge())){
                familyPlanDto.setNeedHpbConsult(true);
            }*/

            result.put("selection", patientInformation);
        }
        return result;
    }

    @ResponseBody
    @PostMapping(value = "/patient-age")
    public Map<String, Object> checkPatientAge(HttpServletRequest request) throws Exception {
        String birthDate = (String) ParamUtil.getSessionAttr(request, "birthDate");
        String counsellingGiven = ParamUtil.getString(request, "counsellingGiven");
        Map<String, Object> result = IaisCommonUtils.genNewHashMap(2);
        if (StringUtil.isEmpty(birthDate) || !CommonValidator.isDate(birthDate) || Formatter.compareDateByDay(birthDate) > 0) {
            return result;
        }
        int age = -Formatter.compareDateByDay(birthDate,counsellingGiven)/365;
        if(age<=16 || age>=65){
            result.put("showAge", Boolean.TRUE);
        }
        return result;
    }

    @GetMapping(value = "/prg-input-info")
    public @ResponseBody
    ProfessionalResponseDto getPrgNoInfo(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the prgNo start ...."));
        String professionRegoNo = ParamUtil.getString(request, "prgNo");
        ProfessionalResponseDto professionalResponseDto = appSubmissionService.retrievePrsInfo(professionRegoNo);
        if(StringUtil.isEmpty(professionalResponseDto.getName())){

        }
        return professionalResponseDto;
    }
}
