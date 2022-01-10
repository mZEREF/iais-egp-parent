package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;

import java.util.Objects;

/**
 * {@code DsRfcHelper}
 *
 * @Auther chenlei on 1/5/2022.
 */
public final class DsRfcHelper {

    public static boolean isChangePatientInfoDto(PatientInfoDto patientInfoDto, PatientInfoDto oldPatientInfoDto) {
        return !Objects.equals(patientInfoDto, oldPatientInfoDto);
    }

}
