package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.HusbandDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;

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

        return !Objects.equals(handlePatient(patientInfoDto.getPatient()), handlePatient(oldPatientInfoDto.getPatient()))
                || !Objects.equals(handleHusband(patientInfoDto.getHusband()), handleHusband(oldPatientInfoDto.getHusband()));
    }

    public static PatientDto handlePatient(PatientDto patientDto) {
        return handlePatient(patientDto, DataSubmissionConsts.DS_PATIENT_ART, false);
    }

    public static PatientDto handlePatient(PatientDto patient, String patientType, boolean force) {
        if (patient == null) {
            return patient;
        }
        if (StringUtil.isNotEmpty(patient.getName())) {
            patient.setName(patient.getName().toUpperCase(AppConsts.DFT_LOCALE));
        }
        patient.setEthnicGroup(StringUtil.getNonNull(patient.getEthnicGroup()));
        patient.setEthnicGroupOther(StringUtil.getNonNull(patient.getEthnicGroupOther()));
        if (StringUtil.isEmpty(patient.getPatientType()) || force) {
            patient.setPatientType(DataSubmissionConsts.DS_PATIENT_ART);
        }
        return patient;
    }

    public static HusbandDto handleHusband(HusbandDto husband) {
        if (husband == null) {
            return husband;
        }
        if (StringUtil.isNotEmpty(husband.getName())) {
            husband.setName(husband.getName().toUpperCase(AppConsts.DFT_LOCALE));
        }
        husband.setEthnicGroup(StringUtil.getNonNull(husband.getEthnicGroup()));
        husband.setEthnicGroupOther(StringUtil.getNonNull(husband.getEthnicGroupOther()));
        return husband;
    }

}
