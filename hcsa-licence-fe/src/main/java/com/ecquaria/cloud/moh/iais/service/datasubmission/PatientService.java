package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;

public interface PatientService {

    DataSubmissionDto getPatientDataSubmissionByConds(String idType, String idNumber, String nationality, String orgId, String patientType);

    PatientDto getActivePatientByConds(String idType, String idNumber, String nationality, String orgId, String patientType);

    PatientDto getActiveArPatientByConds(String idType, String idNumber, String nationality, String orgId);

    PatientDto getArPatientDto(String idType, String idNumber, String nationality, String orgId);

    PatientDto getDpPatientDto(String idType, String idNumber, String nationality, String orgId);

    String getPatientCode(String patientCode);

    PatientInfoDto getPatientInfoDto(String patientCode);
}
