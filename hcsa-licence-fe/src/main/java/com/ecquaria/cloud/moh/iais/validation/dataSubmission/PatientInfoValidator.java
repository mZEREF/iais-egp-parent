package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.HusbandDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.DsRfcHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @Description PatientInfoValidator
 * @Auther chenlei on 10/25/2021.
 */
@Slf4j
public class PatientInfoValidator implements CustomizeValidator {

    private static final String ERR_MSG_INVALID_DATA = "GENERAL_ERR0051";

    @Override
    public Map<String, String> validate(Object obj, String profile, HttpServletRequest request) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        String DS_ERR001 = MessageUtil.replaceMessage("DS_ERR001", "Date of Birth", "field");
        //if (StringUtil.isIn("save", profiles)) {
        PatientInfoDto patientInfo = (PatientInfoDto) obj;
        PatientDto patient = patientInfo.getPatient();
        if (patient == null) {
            patient = new PatientDto();
        }
        PatientDto previous = patientInfo.getPrevious();
        if (previous == null) {
            previous = new PatientDto();
        }
        boolean isRfc = false;
        if ("rfc".equals(profile)) {
            isRfc = true;
            profile = "save";
        }
        ValidationResult result = WebValidationHelper.validateProperty(patient, profile);
        if (result != null) {
            map.putAll(result.retrieveAll());
        }
        String birthDate = patient.getBirthDate();
        if (!StringUtil.isEmpty(birthDate) && CommonValidator.isDate(birthDate)) {
            try {
                if (Formatter.compareDateByDay(birthDate) > 0) {
                    map.put("birthDate", DS_ERR001);
                }
            } catch (Exception e) {
                log.error(StringUtil.changeForLog(e.getMessage()), e);
            }
        }
        if (Boolean.TRUE.equals(patient.getPreviousIdentification())) {
            patientInfo.setPrevious(previous);
            result = WebValidationHelper.validateProperty(previous, "ART");
            if (result != null && result.isHasErrors()) {
                map.putAll(result.retrieveAll("pre", ""));
            }
            if (map.containsKey("preIdType") && isRfc) {
                map.remove("preIdType");
            }
            if (!"file".equals(profile) && !isRfc) {
                boolean retrievePrevious = patientInfo.isRetrievePrevious();
                if (!retrievePrevious) {
                    map.put("retrievePrevious", "DS_ERR005");
                } else if (StringUtil.isEmpty(previous.getId())) {
                    map.put("retrievePrevious", "GENERAL_ACK018");
                }
            } else if (!isRfc){
                //isRfc = true;
                if ("".equals(previous.getIdType())) {
                    map.put("preIdType", ERR_MSG_INVALID_DATA);
                }
                if ("".equals(previous.getNationality())) {
                    map.put("preNationality", ERR_MSG_INVALID_DATA);
                }
            }
            if (!isRfc && !map.containsKey("preIdNumber") && !StringUtil.isEmpty(previous.getIdNumber())
                    && previous.getIdNumber().equals(patient.getIdNumber())
                    && Objects.equals(previous.getIdType(), patient.getIdType())
                    && Objects.equals(previous.getNationality(), patient.getNationality())) {
                map.put("preIdNumber", ERR_MSG_INVALID_DATA);
            }
            //DS_MSG006 - Patient does not exist in the system, please check entered ID Type, ID No. and Nationality.
            if (!isRfc && !map.containsKey("preIdNumber") && !map.containsKey("retrievePrevious") && StringUtil.isEmpty(previous.getId())) {
                map.put("preIdNumber", "DS_MSG006");
            }
            if (!isRfc && StringUtil.isEmpty(ParamUtil.getRequestString(request,"hubHasIdNumber"))) {
                map.put("hubHasIdNumber","GENERAL_ERR0006");
            }
        }
        String jumpValidateHusband = ParamUtil.getRequestString(request,"jumpValidateHusband");
        HusbandDto husband = patientInfo.getHusband();
        if (husband == null) {
            husband = new HusbandDto();
        }
        result = null;
        if (StringUtil.isEmpty(jumpValidateHusband)){
            result = WebValidationHelper.validateProperty(husband, profile);
        }
        if (result != null) {
            map.putAll(result.retrieveAll("", "Hbd"));
        }
        if (!map.containsKey("idNumberHbd") && !StringUtil.isEmpty(husband.getIdNumber())
                && !StringUtil.isEmpty(husband.getIdType()) && !StringUtil.isEmpty(husband.getNationality())
                && husband.getIdNumber().equals(patient.getIdNumber())
                && Objects.equals(husband.getIdType(), patient.getIdType())
                && Objects.equals(husband.getNationality(), patient.getNationality())) {
            // The Husband and Patient ID Number should not be the same. (DS_ERR055)
            map.put("idNumberHbd", "DS_ERR055");
        }
        birthDate = husband.getBirthDate();
        if (!StringUtil.isEmpty(birthDate) && CommonValidator.isDate(birthDate)) {
            try {
                if (Formatter.compareDateByDay(birthDate) > 0) {
                    map.put("birthDateHbd", DS_ERR001);
                }
            } catch (Exception e) {
                log.error(StringUtil.changeForLog(e.getMessage()), e);
            }
        }
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        String orgId = Optional.ofNullable(loginContext).map(LoginContext::getOrgId).orElse("");
        PatientService patientService = SpringContextHelper.getContext().getBean(PatientService.class);
        if (isRfc) {
            if (map.isEmpty() && request.getAttribute("PatientInfoDto_base_error_msgs") == null) {
                ArSuperDataSubmissionDto oldArDataSubmission = DataSubmissionHelper.getOldArDataSubmission(request);
                PatientInfoDto oldPatientInfo;
                if (oldArDataSubmission == null || oldArDataSubmission.getPatientInfoDto() == null) {
                    oldPatientInfo = patientService.getPatientInfoDto(previous.getPatientCode());
                } else {
                    oldPatientInfo = oldArDataSubmission.getPatientInfoDto();
                }
                if (!DsRfcHelper.isChangePatientInfoDto(patientInfo, oldPatientInfo)) {
                    if ("file".equals(profile)) {
                        map.put("preIdNumber", "DS_ERR021");
                    } else {
                        map.put(DataSubmissionConstant.RFC_NO_CHANGE_ERROR, "DS_ERR021");
                        ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                    }
                }
            }
        } else {
            PatientDto patientDto = patientService.getActiveArPatientByConds(patient.getIdType(), patient.getIdNumber(),
                    patient.getNationality(), orgId);
            if (patientDto != null) {
                // nic whether exist
                String ptHasIdNumber = ParamUtil.getString(request, "ptHasIdNumber");
                String newBirthDate = getNewBirthDate(ptHasIdNumber, request);
                String oldBirthDate = patientDto.getBirthDate();
                if (StringUtil.isNotEmpty(patientDto.getIdType())){
                    Boolean isRepeatPassPortNumber = isRepeatPassportNumber(patientDto,newBirthDate,oldBirthDate) || DataSubmissionConsts.DTV_ID_TYPE_NRIC.equals(patientDto.getIdType());
                    if (isRepeatPassPortNumber){
                        map.put("idNumber", MessageUtil.getMessageDesc("DS_ERR007"));
                    }
                }
            }
        }
        if ("file".equals(profile)) {
            if (StringUtil.isEmpty(patientInfo.getIsPreviousIdentification())) {
                map.put("isPreviousIdentification", "GENERAL_ERR0006");
            }
            if (DataSubmissionConstant.DFT_ERROR_MC.equals(patient.getIdType())) {
                map.put("idType", ERR_MSG_INVALID_DATA);
            }
            if (DataSubmissionConstant.DFT_ERROR_MC.equals(patient.getNationality())) {
                map.put("nationality", ERR_MSG_INVALID_DATA);
            }
            if (DataSubmissionConstant.DFT_ERROR_MC.equals(patient.getEthnicGroup())) {
                map.put("ethnicGroup", ERR_MSG_INVALID_DATA);
            }
            if (DataSubmissionConstant.DFT_ERROR_MC.equals(husband.getIdType())) {
                map.put("idTypeHbd", ERR_MSG_INVALID_DATA);
            }
            if (DataSubmissionConstant.DFT_ERROR_MC.equals(husband.getNationality())) {
                map.put("nationalityHbd", ERR_MSG_INVALID_DATA);
            }
            if (DataSubmissionConstant.DFT_ERROR_MC.equals(husband.getEthnicGroup())) {
                map.put("ethnicGroupHbd", ERR_MSG_INVALID_DATA);
            }
        }
        return map;
    }

    /**
     *  get passport is birthDate
     * @param ptHasIdNumber
     * @param request
     * @return
     */
    private static String getNewBirthDate(String ptHasIdNumber, HttpServletRequest request){
        String result = " ";
        if (AppConsts.YES.equals(ptHasIdNumber)){
            result = ParamUtil.getString(request, "birthDate");
        }else if (AppConsts.NO.equals(ptHasIdNumber)){
            result = ParamUtil.getString(request, "dateBirth");
        }
        return result;
    }

    /**
     *  judge passport number whether repeat
     * @param patientDto
     * @param newBirthDate
     * @param oldBirthDate
     * @return
     */
    private static Boolean isRepeatPassportNumber(PatientDto patientDto, String newBirthDate, String oldBirthDate){
        Boolean result = Boolean.FALSE;
        if (StringUtil.isNotEmpty(newBirthDate) && StringUtil.isNotEmpty(oldBirthDate)){
            result =  DataSubmissionConsts.DTV_ID_TYPE_PASSPORT.equals(patientDto.getIdType())
                    && oldBirthDate.equals(newBirthDate);
        }
        return result;
    }

}
