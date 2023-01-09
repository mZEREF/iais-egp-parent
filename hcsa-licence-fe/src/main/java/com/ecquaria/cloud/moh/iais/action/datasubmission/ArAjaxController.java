package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.moh.iais.action.LoginAccessCheck;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.HusbandDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AjaxResDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import com.ecquaria.cloud.usersession.UserSession;
import com.ecquaria.cloud.usersession.UserSessionUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sop.util.DateUtil;
import sop.webflow.process5.ProcessCacheHelper;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Description Ajax
 * @Auther chenlei on 10/21/2021.
 */
@RequestMapping(value = "/ar")
@RestController
@Slf4j
public class ArAjaxController implements LoginAccessCheck {

    @Autowired
    private PatientService patientService;

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    @PostMapping(value = "/retrieve-identification")
    @ResponseBody
    public Map<String, Object> retrieveIdentification(HttpServletRequest request) {
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
        patient.setIdNumber(idNo);
        patient.setNationality(nationality);
        Map<String, Object> result = IaisCommonUtils.genNewHashMap(3);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ValidationResult vr = WebValidationHelper.validateProperty(patient, "ART");
        if (vr != null && vr.isHasErrors()) {
            Map<String, String> params = IaisCommonUtils.genNewHashMap();
            params.put("idType", "preIdType");
            params.put("idNumber", "preIdNumber");
            params.put("nationality", "preNationality");
            errorMap.putAll(vr.retrieveAll(params));
        }
        if (!errorMap.isEmpty()) {
            result.put(IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        } else {
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
            String orgId = Optional.ofNullable(loginContext).map(LoginContext::getOrgId).orElse("");
            PatientDto db = patientService.getActiveArPatientByConds(idType, idNo, nationality, orgId);
            result.put("patient", db);
        }
        return result;
    }

    @PostMapping(value = "/retrieve-valid-selection")
    @ResponseBody
    public Map<String, Object> retrieveValidSelection(HttpServletRequest request) {
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        String nationality = ParamUtil.getString(request, "nationality");
        String stage = ParamUtil.getString(request, "stage");
        CycleStageSelectionDto dto = new CycleStageSelectionDto();
        dto.setPatientIdType(idType);
        dto.setPatientIdNumber(idNo);
        dto.setPatientNationality(nationality);
        Map<String, Object> result = IaisCommonUtils.genNewHashMap();
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ValidationResult vr = WebValidationHelper.validateProperty(dto, "ART");
        if (vr != null) {
            errorMap.putAll(vr.retrieveAll());
        }
        if (!errorMap.isEmpty()) {
            result.put(IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        } else {
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
            String orgId = Optional.ofNullable(loginContext).map(LoginContext::getOrgId).orElse("");
            String hciCode = DataSubmissionHelper.getCurrentArDataSubmission(request).getHciCode();
            CycleStageSelectionDto dbDto = arDataSubmissionService.getCycleStageSelectionDtoByConds(idType, idNo, nationality, orgId,
                    hciCode);
            if (dbDto != null) {
                if (StringUtil.isEmpty(dbDto.getPatientCode())) {
                    // 2: DS_MSG009 - ID Number entered belongs to a patient's previous identity, please use latest patient ID.
                    dto.setPatientStatus(dbDto.getPatientStatus());
                } else {
                    dto = dbDto;
                }
            }
            dto.setHciCode(hciCode);
            if (StringUtil.isNotEmpty(dto.getLastStage())) {
                dto.setLastStageDesc(MasterCodeUtil.getCodeDesc(dto.getLastStage()));
            } else {
                dto.setLastStageDesc("-");
            }
            // add current selection dto to super dto
            ArSuperDataSubmissionDto superDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
            superDto.setSelectionDto(dto);
            DataSubmissionHelper.setCurrentArDataSubmission(superDto, request);
            result.put("selection", dto);
        }
        result.put("cycleStartHtmls", DataSubmissionHelper.genCycleStartHtmls(dto.getCycleDtos()));
        result.put("stagHtmls",
                DataSubmissionHelper.genOptionHtmlsWithFirst(DataSubmissionHelper.getNextStageForAR(dto)));
        result.put("stage", stage);
        return result;
    }

    @ResponseBody
    @PostMapping(value = "/patient-age")
    public Map<String, Object> checkPatientAge(HttpServletRequest request) throws Exception {
        String birthDate = ParamUtil.getString(request, "birthDate");
        Map<String, Object> result = IaisCommonUtils.genNewHashMap(3);
        result.put("modalId", ParamUtil.getString(request, "modalId"));
        if (StringUtil.isEmpty(birthDate) || !CommonValidator.isDate(birthDate) || Formatter.compareDateByDay(birthDate) > 0) {
            return result;
        }
        String age1 = MasterCodeUtil.getCodeDesc("PT_AGE_001");
        String age2 = MasterCodeUtil.getCodeDesc("PT_AGE_002");
        int age = Formatter.getAge(birthDate);
        if (Integer.parseInt(age1) > age || Integer.parseInt(age2) < age) {
            result.put("showAgeMsg", Boolean.TRUE);
        }
        return result;
    }

    @ResponseBody
    @PostMapping(value = "/retrieve-cycle-selection")
    public Map<String, Object> retriveCycleStageSelection(HttpServletRequest request) {
        String cycleId = StringUtil.clarify(ParamUtil.getString(request, "cycleStart"));
        String patientCode = ParamUtil.getString(request, "patientCode");
        String hciCode = DataSubmissionHelper.getCurrentArDataSubmission(request).getHciCode();
        CycleStageSelectionDto selectionDto = arDataSubmissionService.getCycleStageSelectionDtoByConds(patientCode,
                hciCode, cycleId);
        Map<String, Object> result = IaisCommonUtils.genNewHashMap();
        selectionDto.setHciCode(hciCode);
        if (StringUtil.isNotEmpty(selectionDto.getLastStage())) {
            selectionDto.setLastStageDesc(MasterCodeUtil.getCodeDesc(selectionDto.getLastStage()));
        } else {
            selectionDto.setLastStageDesc("-");
        }
        result.put("selection", selectionDto);
        result.put("stagHtmls",
                DataSubmissionHelper.genOptionHtmlsWithFirst(DataSubmissionHelper.getNextStageForAR(selectionDto)));
        return result;
    }

    @PostMapping(value = "/pregnancy-outcome-baby-html")
    public @ResponseBody
    AjaxResDto generatePregnancyOutcomeBabyHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the generatePregnancyOutcomeBabyHtml start ...."));

        AjaxResDto ajaxResDto = new AjaxResDto();
        ajaxResDto.setResCode("200");

        int babyIndex = ParamUtil.getInt(request, "babyIndex");
        int babySize = ParamUtil.getInt(request, "babySize");
        List<SelectOption> babyWeightSelectOptions = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BABY_BIRTH_WEIGHT);

        String sqlStr = SqlMap.INSTANCE.getSql("pregnancyOutcomeStage", "generatePregnancyOutcomeBabyHtml");
        StringBuilder resultJson = new StringBuilder();

        for (int i = 0; i < babySize; i++) {
            int myBabyIndex = babyIndex + i;
            String mySqlStr = sqlStr;
            mySqlStr = mySqlStr.replace("${babyIndex}", String.valueOf(myBabyIndex));
            mySqlStr = mySqlStr.replace("${babyDisplayNum}", String.valueOf(myBabyIndex + 1));
            mySqlStr = mySqlStr.replace("${babyWeightSelectOptions}", generateDropDownHtml(babyWeightSelectOptions, null));
            resultJson.append(mySqlStr);
        }

        ajaxResDto.setResultJson(resultJson.toString());

        log.debug(StringUtil.changeForLog("the generatePregnancyOutcomeBabyHtml end ...."));
        return ajaxResDto;

    }

    @PostMapping(value = "/validate-patient-info")
    public @ResponseBody
    Map<String, Object> validatePatientInfo(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the AR patient info validation start ...."));
        Map<String, Object> result = Maps.newHashMapWithExpectedSize(3);
        String isPatHasId = ParamUtil.getString(request, "isPatHasId");
        String identityNo = ParamUtil.getString(request, "identityNo");
        String centreSel = ParamUtil.getString(request, "centreSel");
        String dateBirth = ParamUtil.getString(request, "birthDate");
        System.out.println("dateBirth ............ ...." + dateBirth);
        Map<String, PremisesDto> premisesMap = (Map<String, PremisesDto>) ParamUtil.getSessionAttr(request, DataSubmissionConstant.AR_PREMISES_MAP);
        PremisesDto premisesDto = premisesMap.get(centreSel);

        //validate indeed field is not empty
        result.put("needShowError", false);
        if (!StringUtils.hasLength(isPatHasId) || !StringUtils.hasLength(identityNo)) {
            if (!StringUtils.hasLength(isPatHasId)) {
                result.put("error_hasIdNumber", "");
            }
            if (!StringUtils.hasLength(identityNo)) {
                result.put("error_identityNo", "");
            }
            result.put("needShowError", true);
            return result;
        }

        //do passport/FIN/NRIC validate
        boolean identityNoValidate = false;
        Boolean isRangeAge = Boolean.FALSE;
        String age1 = MasterCodeUtil.getCodeDesc("PT_AGE_001");
        String age2 = MasterCodeUtil.getCodeDesc("PT_AGE_002");
        if (dateBirth != null && !Boolean.FALSE.equals(dateBirth)){
            int age = Formatter.getAge(dateBirth);
            isRangeAge = Integer.parseInt(age1) > age || Integer.parseInt(age2) < age;
        }
        if("1".equals(isPatHasId)){
            boolean finValidation = SgNoValidator.validateFin(identityNo);
            boolean nricValidation = SgNoValidator.validateNric(identityNo);

            if(finValidation || nricValidation)identityNoValidate = true;
        }else{
            identityNoValidate = true;
        }
        if(!identityNoValidate){
            result.put("error_identityNo","Please key in a valid NRIC/FIN");
            result.put("needShowError",true);
            return result;
        }

        //by passport or NRIC NUMBER to search patient info from database
        String idType = patientService.judgeIdType(isPatHasId,identityNo);
        PatientInfoDto patientInfoDto ;
        if (dateBirth != null && !Boolean.FALSE.equals(dateBirth)){
            patientInfoDto = patientService.getPatientInfoDtoByIdTypeAndIdNumberAndBirthDate(idType,identityNo,dateBirth);
        } else {
            patientInfoDto = patientService.getPatientInfoDtoByIdTypeAndIdNumber(idType,identityNo);
        }
        if (patientInfoDto == null) {
            ArSuperDataSubmissionDto currentArDataSubmission = new ArSuperDataSubmissionDto();
            String orgId = currentArDataSubmission.getOrgId();
            String userId = "";
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
            if (loginContext != null) {
                orgId = loginContext.getOrgId();
                userId = loginContext.getUserId();
            }
            ArSuperDataSubmissionDto dataSubmissionDraft = arDataSubmissionService.getArPatientSubmissionDraftByConds(orgId, DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO, idType, identityNo, userId);
            if (dataSubmissionDraft != null) {
                currentArDataSubmission.setDraftId(dataSubmissionDraft.getDraftId());
                currentArDataSubmission.setDraftNo(dataSubmissionDraft.getDraftNo());
                currentArDataSubmission.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO);
                result.put("hasDraft", Boolean.TRUE);
            } else {
                currentArDataSubmission.setDraftId(null);
                currentArDataSubmission.setDraftNo(null);
                currentArDataSubmission.setPatientInfoDto(null);
            }
            DataSubmissionHelper.setCurrentArDataSubmission(currentArDataSubmission, request);
        }
        ParamUtil.setSessionAttr(request,"patientInfoDto",patientInfoDto);


        if(ObjectUtils.isEmpty(patientInfoDto)){
            result.put("registeredPT",false);

            //deal with the issue that happened when ArDataSubmission is exist and key unregistered id
            request.getSession().removeAttribute(DataSubmissionConstant.AR_DATA_SUBMISSION);
            return result;
        }
        result.put("registeredPT",true);

        PatientDto patientDto = patientInfoDto.getPatient();
        result.put("ptName", patientDto.getName());
        result.put("ptBirth", patientDto.getBirthDate());
        result.put("ptNat", MasterCodeUtil.getCodeDesc(patientDto.getNationality()));
        String ethStr = "";
        if (DataSubmissionConsts.ETHNIC_GROUP_OTHER.equals(patientDto.getEthnicGroup())){
            ethStr = patientDto.getEthnicGroupOther();
        } else {
            ethStr = MasterCodeUtil.getCodeDesc(patientDto.getEthnicGroup());
        }
        result.put("ptEth", ethStr);

        PatientDto previous = patientInfoDto.getPrevious();
        if (previous != null) {
            result.put("ptPreId", previous.getIdNumber());
            result.put("ptPreName", previous.getName());
            result.put("ptPreNat", MasterCodeUtil.getCodeDesc(previous.getNationality()));
        }

        HusbandDto husband = patientInfoDto.getHusband();
        result.put("husName", husband.getName());
        result.put("husBirth", husband.getBirthDate());
        result.put("husNat", MasterCodeUtil.getCodeDesc(husband.getNationality()));
        String busEthStr = "";
        if (DataSubmissionConsts.ETHNIC_GROUP_OTHER.equals(husband.getEthnicGroup())){
            ethStr = husband.getEthnicGroupOther();
        } else {
            ethStr = MasterCodeUtil.getCodeDesc(husband.getEthnicGroup());
        }
        result.put("husEth", ethStr);

        LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        String orgId = Optional.ofNullable(loginContext).map(LoginContext::getOrgId).orElse("");
        String hciCode = premisesDto.getHciCode();
        CycleStageSelectionDto dbDto = arDataSubmissionService.getCycleStageSelectionDtoByConds(patientDto.getIdType(), patientDto.getIdNumber(), patientDto.getNationality(), orgId,
                hciCode);
        ParamUtil.setSessionAttr(request, "selectionDto", dbDto);
        ParamUtil.setSessionAttr(request, "patientInfoDto", patientInfoDto);

        result.put("cycles", dbDto.getDsCycleRadioDtos());
        result.put("lastStatus",dbDto.getLastStatus());
        log.debug(StringUtil.changeForLog("the AR patient info validation end ...."));
        return result;
    }

    public static String genNextStageOptions() {
        return DataSubmissionHelper.genOptionHtmlsWithFirst(DataSubmissionHelper.getNextStagesForAr(null, null, null, false, false, false, false, false));
    }

    private String generateDropDownHtml(List<SelectOption> options, String firstOption) {
        if (options == null || options.isEmpty()) {
            return "";
        }
        StringBuilder html = new StringBuilder();
        if (!StringUtil.isEmpty(firstOption)) {
            html.append("<option value=\"\">").append(StringUtil.escapeHtml(firstOption)).append("</option>");
        }
        for (SelectOption option : options) {
            String val = StringUtil.viewNonNullHtml(option.getValue());
            String txt = StringUtil.escapeHtml(option.getText());
            html.append("<option value=\"").append(val).append("\">").append(txt).append("</option>");
        }
        return html.toString();
    }

    @PostMapping(value = "/calculate-age")
    public @ResponseBody
    Map<String, Object> calculateAge(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("calculate age start ...."));
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        Date startDate = DateUtil.parseDate(arSuperDataSubmissionDto.getPatientInfoDto().getPatient().getBirthDate(), AppConsts.DEFAULT_DATE_FORMAT);
        String freezingDateStr = ParamUtil.getRequestString(request, "efoDateStarted");
        Date freezingDate = DateUtil.parseDate(freezingDateStr, AppConsts.DEFAULT_DATE_FORMAT);
        Map<String, Object> result = Maps.newHashMapWithExpectedSize(2);
        int year = 0;
        int month = 0;
        if (startDate != null && freezingDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(freezingDate);
            calendar.add(Calendar.DATE, 1);
            year = Period.between(LocalDate.parse(sdf.format(startDate)), LocalDate.parse(sdf.format(calendar.getTime()))).getYears();
            month = Period.between(LocalDate.parse(sdf.format(startDate)), LocalDate.parse(sdf.format(calendar.getTime()))).getMonths();
            if (freezingDate.before(startDate)){
                result.put("errMsg",Boolean.TRUE);
            }
        }
        result.put("freezingYear",year);
        result.put("freezingMonth",month);
        return result;
    }

    @GetMapping(value = "/donor-draft")
    public @ResponseBody
    Map<String, Object> donorDraft(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("start checking whether have donor sample draft ..."));
        Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
        String orgId = "";
        String userId = "";
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        if (loginContext != null) {
            orgId = loginContext.getOrgId();
            userId = loginContext.getUserId();
        }
        ArSuperDataSubmissionDto dataSubmissionDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftByConds(orgId,DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE,null,userId);
        if (dataSubmissionDraft != null) {
            result.put("donorHasDraft", Boolean.TRUE);
        } else {
            result.put("donorHasDraft", Boolean.FALSE);
        }
        return result;
    }
}
