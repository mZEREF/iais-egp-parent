package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;

public interface PatientService {

    DataSubmissionDto getPatientDataSubmissionByConds(String idType, String idNumber, String nationality, String orgId, String patientType);

    PatientDto getActivePatientByConds(String idType, String idNumber, String nationality, String orgId, String patientType);

    PatientDto getActivePatientByCond(String name, String idNumber, String nationality, String orgId, String patientType);

    PatientDto getActiveArPatientByConds(String name, String idNumber, String nationality, String orgId);

    PatientDto getActiveArPatientByCond(String name, String idNumber, String nationality, String orgId);

    PatientInfoDto getPatientInfoDtoByIdTypeAndIdNumber(String idType,String idNumber, String orgId);

    PatientInfoDto getPatientInfoDtoByIdTypeAndIdNumberAndBirthDate(String idType, String idNumber, String birthDate, String orgId);

    PatientDto getArPatientDto(String idType, String idNumber, String nationality, String orgId);

    PatientDto getDpPatientDto(String idType, String idNumber, String nationality, String orgId);

    String getPatientCode(String patientCode);

    PatientInfoDto getPatientInfoDto(String patientCode);

    PatientDto getPatientDtoBySubmissionId(String submissionId);

        String judgeIdType(String isNricFin, String idNo);

        String judgeIdType(String idNo);
    }
