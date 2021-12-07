package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;

public interface PatientService {

    PatientDto getArPatientDto(String idType, String idNumber, String nationality, String orgId);

    PatientDto getDpPatientDto(String idType, String idNumber, String nationality, String orgId);
}
