package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DoctorInformationDto;

/**
 * @Description DocInfoService
 * @Auther fanghao on 5/30/2022.
 */
public interface DocInfoService {
    DoctorInformationDto getDoctorInformationDtoByConds(String doctorReignNo,String doctorSource);
}
