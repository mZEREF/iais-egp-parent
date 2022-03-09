package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.HusbandDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;

import java.util.Map;
import java.util.Objects;

/**
 * {@code DsRfcHelper}
 *
 * @Auther chenlei on 1/5/2022.
 */
public final class DsRfcHelper {

    public static boolean isChangePatientInfoDto(PatientInfoDto patientInfoDto, PatientInfoDto oldPatientInfoDto) {
        if (patientInfoDto == null || oldPatientInfoDto == null) {
            return true;
        }

        return !Objects.equals(prepare(patientInfoDto.getPatient()), prepare(oldPatientInfoDto.getPatient()))
                || !Objects.equals(prepare(patientInfoDto.getHusband()), prepare(oldPatientInfoDto.getHusband()));
    }

    public static PatientDto prepare(PatientDto patientDto) {
        return prepare(patientDto, DataSubmissionConsts.DS_PATIENT_ART, false);
    }

    public static PatientDto prepare(PatientDto patient, String patientType, boolean force) {
        if (patient == null) {
            return patient;
        }
        if (StringUtil.isNotEmpty(patient.getName())) {
            patient.setName(patient.getName().toUpperCase(AppConsts.DFT_LOCALE));
        }
        patient.setEthnicGroup(StringUtil.getNonNull(patient.getEthnicGroup()));
        if (DataSubmissionConsts.ETHNIC_GROUP_OTHER.equals(patient.getEthnicGroup())) {
            patient.setEthnicGroupOther(StringUtil.getNonNull(patient.getEthnicGroupOther()));
        } else {
            patient.setEthnicGroupOther(null);
        }
        if (StringUtil.isEmpty(patient.getPatientType()) || force) {
            patient.setPatientType(patientType);
        }
        if (!patient.isPreviousIdentification()) {
            patient.setPreviousIdentificationId(null);
        }
        return patient;
    }

    public static HusbandDto prepare(HusbandDto husband) {
        if (husband == null) {
            return husband;
        }
        if (StringUtil.isNotEmpty(husband.getName())) {
            husband.setName(husband.getName().toUpperCase(AppConsts.DFT_LOCALE));
        }
        husband.setEthnicGroup(StringUtil.getNonNull(husband.getEthnicGroup()));
        if (DataSubmissionConsts.ETHNIC_GROUP_OTHER.equals(husband.getEthnicGroup())) {
            husband.setEthnicGroupOther(StringUtil.getNonNull(husband.getEthnicGroupOther()));
        } else {
            husband.setEthnicGroupOther(null);
        }
        return husband;
    }

    public static PatientInfoDto handle(PatientInfoDto patientInfoDto) {
        if (patientInfoDto == null) {
            return patientInfoDto;
        }
        if (patientInfoDto.getPatient() != null) {
            PatientDto patient = patientInfoDto.getPatient();
            patient.setAgeFlag(getAgeFlag(patient.getBirthDate(), DataSubmissionConstant.DS_SHOW_PATIENT));
        }
        if (patientInfoDto.getPrevious() != null) {
            PatientDto patient = patientInfoDto.getPrevious();
            patient.setAgeFlag(getAgeFlag(patient.getBirthDate(), DataSubmissionConstant.DS_SHOW_PATIENT));
        }
        if (patientInfoDto.getHusband() != null) {
            HusbandDto husband = patientInfoDto.getHusband();
            husband.setAgeFlag(getAgeFlag(husband.getBirthDate(), DataSubmissionConstant.DS_SHOW_HUSBAND));
        }
        return patientInfoDto;
    }

    public static String getAgeFlag(String birthDate, String person) {
        if (StringUtil.isEmpty(birthDate) || !CommonValidator.isDate(birthDate)) {
            return "";
        }
        String age1 = MasterCodeUtil.getCodeDesc("PT_AGE_001");
        String age2 = MasterCodeUtil.getCodeDesc("PT_AGE_002");
        int age = Formatter.getAge(birthDate);
        if (Integer.parseInt(age1) <= age && age <= Integer.parseInt(age2)) {
            return "";
        }
        Map<String, String> repMap = IaisCommonUtils.genNewHashMap(2);
        repMap.put("0", age1);
        repMap.put("1", age2);
        repMap.put("2", person);
        return MessageUtil.getMessageDesc("DS_MSG005", repMap);
       /* return new StringBuilder().append("<a class=\"btn-tooltip styleguide-tooltip\" ")
                .append("style=\"z-index: 999;position: absolute; right: 30px; top: 12px;\" ")
                .append("href=\"javascript:void(0);\" ")
                .append("data-placement=\"top\" ")
                .append("data-toggle=\"tooltip\" ")
                .append("data-html=\"true\" ")
                .append("title=\"&lt;p&gt;")
                .append(MessageUtil.getMessageDesc("DS_MSG005", repMap))
                .append("&lt;/p&gt;\">")
                .append("i</a>")
                .toString();*/
    }

}
