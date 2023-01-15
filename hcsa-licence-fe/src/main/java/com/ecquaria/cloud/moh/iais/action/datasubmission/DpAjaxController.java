package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.moh.iais.action.LoginAccessCheck;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import com.ecquaria.cloud.moh.iais.validation.dataSubmission.PreTerminationValidator;
import com.ecquaria.cloud.usersession.UserSession;
import com.ecquaria.cloud.usersession.UserSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sop.webflow.process5.ProcessCacheHelper;

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
public class DpAjaxController implements LoginAccessCheck {

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppSubmissionService appSubmissionService;



    @PostMapping(value = "/retrieve-identification")
    public @ResponseBody
    Map<String, Object> retrieveIdentification(HttpServletRequest request) {
        String sessionId = UserSessionUtil.getLoginSessionID(request.getSession());
        UserSession userSession = ProcessCacheHelper.getUserSessionFromCache(sessionId);
        if (userSession == null || !"Active".equals(userSession.getStatus())) {
            throw new IaisRuntimeException("User session invalid");
        }
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        String nationality = ParamUtil.getString(request, "nationality");
        PatientDto patient = new PatientDto();
        patient.setIdType(idType);
        patient.setIdNumber(idNo.toUpperCase());
        patient.setNationality(nationality);
        Map<String, Object> result = IaisCommonUtils.genNewHashMap(3);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        Map<String, String> params = IaisCommonUtils.genNewHashMap();
        if (StringUtil.isEmpty(idType)){
            params.put("idType", "idType");
        }
        if (StringUtil.isEmpty(idNo)){
            params.put("idNumber", "idNumber");
        }
        if (StringUtil.isEmpty(nationality)){
            params.put("nationality", "nationality");
        }
        errorMap.putAll(params);
        DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(request);
        if (!errorMap.isEmpty()) {
            result.put(IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            dpSuperDataSubmissionDto.setDraftPatientValid(false);
            DataSubmissionHelper.setCurrentDpDataSubmission(dpSuperDataSubmissionDto, request);
        } else {
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
            String orgId = Optional.ofNullable(loginContext).map(LoginContext::getOrgId).orElse("");
            PatientDto db = patientService.getDpPatientDto(idType, idNo, nationality, orgId);
            if (db != null) {
                patient = db;
                dpSuperDataSubmissionDto.setDraftPatientValid(true);
                DataSubmissionHelper.setCurrentDpDataSubmission(dpSuperDataSubmissionDto, request);

                ParamUtil.setRequestAttr(request,"enteredPatient",db);
            }
            result.put("selection", patient);
        }
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
        boolean b = PreTerminationValidator.validateDate(dispensingDate);
        if (b) {
            if (Formatter.compareDateByDay(submitDt, dispensingDate) > 2) {
                result.put("showDate", Boolean.TRUE);
            }
        }
        return result;
    }
}
