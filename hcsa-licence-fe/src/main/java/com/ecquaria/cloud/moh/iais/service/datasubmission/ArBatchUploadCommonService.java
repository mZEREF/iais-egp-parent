package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;

import javax.servlet.http.HttpServletRequest;

/**
 * ArBatchUploadCommonService
 *
 * @Author dongchi
 * @Date 2023/2/14 15:04
 **/
public interface ArBatchUploadCommonService {
    PatientInfoDto setPatientInfo(String idType, String idNo, HttpServletRequest request);
}
