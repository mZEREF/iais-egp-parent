package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DoctorInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DpDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * @Description Ajax
 * @Auther fanghao on 12/07/2021.
 */
@RequestMapping(value = "/dp")
@RestController
@Slf4j
public class DpAjaxController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppSubmissionService appSubmissionService;

    @Autowired
    private DpDataSubmissionService dpDataSubmissionService;


    @PostMapping(value = "/retrieve-identification")
    public @ResponseBody
    Map<String, Object> retrieveIdentification(HttpServletRequest request) {
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        String nationality = ParamUtil.getString(request, "nationality");
        PatientDto patient = new PatientDto();
        patient.setIdType(idType);
        patient.setIdNumber(idNo);
        patient.setNationality(nationality);
        Map<String, Object> result = IaisCommonUtils.genNewHashMap(3);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ValidationResult vr = WebValidationHelper.validateProperty(patient, "ART");
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
            PatientDto db = patientService.getDpPatientDto(idType, idNo, nationality, orgId);
            if (db != null) {
                patient = db;
            }
            result.put("selection", patient);
        }
        return result;
    }

   /* @GetMapping(value = "/prg-input-info")
    public Map<String, Object> getPrgNoInfo(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the prgNo start ...."));
        String professionRegoNo = ParamUtil.getString(request, "prgNo");
        ProfessionalResponseDto professionalResponseDto = appSubmissionService.retrievePrsInfo(professionRegoNo);
        Map<String, Object> result = IaisCommonUtils.genNewHashMap(3);
        result.put("prgNo", professionalResponseDto);
        result.put("ERR0042", MessageUtil.getMessageDesc("GENERAL_ERR0042"));
        result.put("ERR0048", MessageUtil.getMessageDesc("GENERAL_ERR0048"));
        result.put("ERR0054", MessageUtil.getMessageDesc("GENERAL_ERR0054"));
        return result;
    }*/
   @GetMapping(value = "/prg-input-info")
    public @ResponseBody
    Map<String, Object> getPrgNoInfo(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the prgNo start ...."));
        String professionRegoNo = ParamUtil.getString(request, "prgNo");
        Map<String, Object> result = IaisCommonUtils.genNewHashMap(1);
        ProfessionalResponseDto professionalResponseDto=appSubmissionService.retrievePrsInfo(professionRegoNo);
        if("-1".equals(professionalResponseDto.getStatusCode()) || "-2".equals(professionalResponseDto.getStatusCode())){
            DoctorInformationDto doctorInformationDto=dpDataSubmissionService.getDoctorInformationDtoByConds(professionRegoNo);
            result.put("selection", doctorInformationDto);
            return result;
        }
        result.put("selection", professionalResponseDto);
        return result;
    }

    @ResponseBody
    @PostMapping(value = "/startdispensing-date")
    public Map<String, Object> checkPatientAge(HttpServletRequest request) throws ParseException {
        DpSuperDataSubmissionDto dpSuperDataSubmissionDto=DataSubmissionHelper.getCurrentDpDataSubmission(request);
        DataSubmissionDto dataSubmissionDto = dpSuperDataSubmissionDto.getDataSubmissionDto();
        dataSubmissionDto.setSubmitDt(new Date());
        String dispensingDate = ParamUtil.getString(request, "dispensingDate");
        String submitDt=Formatter.formatDateTime(dataSubmissionDto.getSubmitDt(), "dd/MM/yyyy HH:mm:ss");
        Map<String, Object> result = IaisCommonUtils.genNewHashMap(1);
        if(Formatter.compareDateByDay(submitDt,dispensingDate)>2){
            result.put("showDate", Boolean.TRUE);
        }
        return result;
    }
}
