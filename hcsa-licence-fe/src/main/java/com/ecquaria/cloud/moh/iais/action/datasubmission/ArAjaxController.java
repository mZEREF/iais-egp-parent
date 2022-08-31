package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsCycleRadioDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.HusbandDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsHelper;
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
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
public class ArAjaxController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    @PostMapping(value = "/retrieve-identification")
    @ResponseBody
    public Map<String, Object> retrieveIdentification(HttpServletRequest request) {
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
        Map<String, Object> situation = Maps.newHashMapWithExpectedSize(3);
        String isPatHasId = ParamUtil.getString(request, "isPatHasId");
        String identityNo = ParamUtil.getString(request, "identityNo");
        String centreSel = ParamUtil.getString(request, "centreSel");
        Map<String, PremisesDto> premisesMap = (Map<String, PremisesDto>) ParamUtil.getSessionAttr(request, DataSubmissionConstant.AR_PREMISES_MAP);
        PremisesDto premisesDto = premisesMap.get(centreSel);

        //validate indeed field is not empty
        situation.put("needShowError", false);
        if (!StringUtils.hasLength(isPatHasId) || !StringUtils.hasLength(identityNo)) {
            if (!StringUtils.hasLength(isPatHasId)) {
                situation.put("error_hasIdNumber", "");
            }
            if (!StringUtils.hasLength(identityNo)) {
                situation.put("error_identityNo", "");
            }
            situation.put("needShowError", true);
            return situation;
        }

        //do passport/FIN/NRIC validate
        boolean identityNoValidate = false;
        if("NRICORFIN".equals(isPatHasId)){
            boolean finValidation = SgNoValidator.validateFin(identityNo);
            boolean nricValidation = SgNoValidator.validateNric(identityNo);

            if(finValidation || nricValidation)identityNoValidate = true;
        }else{
            identityNoValidate = true;
        }
        if(!identityNoValidate){
            situation.put("error_identityNo","");
            situation.put("needShowError",true);
            return situation;
        }


        //by passport or NRIC NUMBER to search patient info from database
        PatientInfoDto patientInfoDto = patientService.getPatientInfoDtoByIdTypeAndIdNumber(isPatHasId,identityNo);
        ParamUtil.setSessionAttr(request,"patientInfoDto",patientInfoDto);
        if(ObjectUtils.isEmpty(patientInfoDto)){
            situation.put("registeredPT",false);

            //deal with the issue that happened when ArDataSubmission is exist and key unregistered id
            request.getSession().removeAttribute(DataSubmissionConstant.AR_DATA_SUBMISSION);
            return situation;
        }
        situation.put("registeredPT",true);
        PatientDto patientDto = patientInfoDto.getPatient();
        StringBuilder patientHtml = new StringBuilder();
        patientHtml.append("<span style=\"display:block\">").append("Details of Patient").append("</span>");
        patientHtml.append("<span style=\"display:block\">").append("Name (as per NRIC/Passport): ").append(patientDto.getName()).append("</span>");
        patientHtml.append("<span style=\"display:block\">").append("Date of Birth: ").append(patientDto.getBirthDate()).append("</span>");
        patientHtml.append("<span style=\"display:block\">").append("Nationality: ").append(patientDto.getNationality()).append("</span>");
        patientHtml.append("<span style=\"display:block\">").append("Ethnicity: ").append(patientDto.getEthnicGroup()).append("</span>");
        situation.put("arPatient", patientHtml);
        PatientDto previous = patientInfoDto.getPrevious();
        if (!ObjectUtils.isEmpty(previous)) {
            StringBuilder previousHtml = new StringBuilder();
            previousHtml.append("<span style=\"display:block\">").append("Other Identification ID Used in Previous AR Treatment").append("</span>");
            previousHtml.append("<span style=\"display:block\">").append("ID No.: ").append(previous.getIdNumber()).append("</span>");
            previousHtml.append("<span style=\"display:block\">").append("Name: ").append(previous.getName()).append("</span>");
            previousHtml.append("<span style=\"display:block\">").append("Nationality: ").append(previous.getNationality()).append("</span>");
            situation.put("preArPatient", previousHtml);
        }
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        String orgId = Optional.ofNullable(loginContext).map(LoginContext::getOrgId).orElse("");
        String hciCode = premisesDto.getHciCode();
        CycleStageSelectionDto dbDto = arDataSubmissionService.getCycleStageSelectionDtoByConds(patientDto.getIdType(), patientDto.getIdNumber(), patientDto.getNationality(), orgId,
                hciCode);
        ParamUtil.setSessionAttr(request, "selectionDto", dbDto);
        ParamUtil.setSessionAttr(request, "patientInfoDto", patientInfoDto);
        List<DsCycleRadioDto> dsCycleRadioDtos = dbDto.getDsCycleRadioDtos();
        situation.put("cycleRadio", genCycleRadioHtmls(dsCycleRadioDtos));
        situation.put("cycleNextStageMap", JSONObject.valueToString(genCycleNextStageMap(dsCycleRadioDtos)));
        HusbandDto husband = patientInfoDto.getHusband();
        if (!ObjectUtils.isEmpty(husband)) {
            StringBuilder husbandHtml = new StringBuilder();
            husbandHtml.append("<span style=\"display:block\">").append("Husband Details (If Applicable)").append("</span>");
            husbandHtml.append("<span style=\"display:block\">").append("Name (as per NRIC/Passport): ").append(husband.getIdNumber()).append("</span>");
            husbandHtml.append("<span style=\"display:block\">").append("Date of Birth: ").append(husband.getBirthDate()).append("</span>");
            husbandHtml.append("<span style=\"display:block\">").append("Nationality: ").append(husband.getNationality()).append("</span>");
            husbandHtml.append("<span style=\"display:block\">").append("Ethnicity: ").append(husband.getEthnicGroup()).append("</span>");
            situation.put("arHusband", husbandHtml);
        }
        log.debug(StringUtil.changeForLog("the AR patient info validation end ...."));
        return situation;
    }

    public static Map<String, String> genCycleNextStageMap(List<DsCycleRadioDto> dsCycleRadioDtos) {
        Map<String, String> result = IaisCommonUtils.genNewHashMap();
        for (int i = 0; i < dsCycleRadioDtos.size(); i++) {
            DsCycleRadioDto dsCycleRadioDto = dsCycleRadioDtos.get(i);
            CycleDto cycleDto = dsCycleRadioDto.getCycleDto();
            String lastCycle = cycleDto.getCycleType();
            String lastStage = dsCycleRadioDto.getLastDataSubmissionDto().getCycleStage();
            String lastStatus = cycleDto.getStatus();
            boolean undergoingCycle = dsCycleRadioDto.isUndergoingCycle();
            boolean frozenOocyte = dsCycleRadioDto.isFrozenOocyte();
            boolean frozenEmbryo = dsCycleRadioDto.isFrozenEmbryo();
            boolean freshNatural = dsCycleRadioDto.isFreshNatural();
            boolean freshStimulated = dsCycleRadioDto.isFreshStimulated();
            if (DataSubmissionConsts.DS_CYCLE_AR.equals(lastCycle) && DsHelper.isSpecialStage(lastStage)) {
                lastStage = dsCycleRadioDto.getAdditionalStage();
            }
            List<String> nextStagesForAr = DataSubmissionHelper.getNextStagesForAr(lastCycle, lastStage, lastStatus, undergoingCycle, frozenOocyte, frozenEmbryo, freshNatural, freshStimulated);
            result.put("cycleRadio" + i, DataSubmissionHelper.genOptionHtmlsWithFirst((nextStagesForAr)));
        }
        result.put("cycleRadio", DataSubmissionHelper.genOptionHtmlsWithFirst(DataSubmissionHelper.getNextStagesForAr(null, null, null, false, false, false, false, false)));
        return result;
    }

    public static String genCycleRadioHtmls(List<DsCycleRadioDto> dsCycleRadioDtos) {
        if (IaisCommonUtils.isEmpty(dsCycleRadioDtos)) {
            return "";
        }
        StringBuilder data = new StringBuilder();
        int i = 0;
        for (DsCycleRadioDto dsCycleRadioDto : dsCycleRadioDtos) {
            CycleDto cycleDto = dsCycleRadioDto.getCycleDto();
            data.append("<div class=\"form-check col-xs-12\" style=\"padding: 0;\">\n");
            data.append(String.format("<input class=\"form-check-input\" id=\"cycleRadio%d\" type=\"radio\" name=\"cycleRadio\" value=\"%s\">\n", i, cycleDto.getId()));
            data.append(String.format("<label class=\"form-check-label\" for=\"cycleRadio%d\">\n", i));
            data.append(String.format("<span class=\"check-circle\"></span>[%s] Submission ID %s\n",
                    MasterCodeUtil.getCodeDesc(cycleDto.getCycleType()),
                    dsCycleRadioDto.getStartDataSubmissionDto().getSubmissionNo()
            ));
            data.append("</label>\n");
            data.append("</div>");
            i++;
        }
        return data.toString();
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
}
