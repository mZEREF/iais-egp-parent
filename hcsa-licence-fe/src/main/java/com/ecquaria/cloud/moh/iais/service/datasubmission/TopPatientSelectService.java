package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInformationDto;

public interface TopPatientSelectService {
    PatientInformationDto getTopPatientSelect(String idType, String idNumber, String nationality, String orgId);
}
