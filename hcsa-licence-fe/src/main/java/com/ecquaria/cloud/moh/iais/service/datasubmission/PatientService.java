package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;

public interface PatientService {

    PatientDto getPatientDto(String idNumber, String nationality, String orgId);

}
