package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DoctorInformationDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description DoctorInfoClientFallback
 * @Auther fanghao on 5/30/2022.
 */
@Slf4j
public class DoctorInfoClientFallback implements DoctorInfoClient {

    private FeignResponseEntity getFeignResponseEntity(Object... params) {
        return IaisEGPHelper.getFeignResponseEntity(params);
    }

    @Override
    public FeignResponseEntity<DoctorInformationDto> getAllDoctorInformationDtoByConds(String doctorReignNo, String doctorSource) {
        return getFeignResponseEntity(doctorReignNo, doctorSource);
    }

    @Override
    public FeignResponseEntity<DoctorInformationDto> getDoctorInformationDtoByConds(String doctorReignNo, String doctorSource, String hciCode) {
        return getFeignResponseEntity(doctorReignNo, doctorSource, hciCode);
    }

    @Override
    public FeignResponseEntity<DoctorInformationDto> getRfcDoctorInformationDtoByConds(String doctorInformationId) {
        return getFeignResponseEntity(doctorInformationId);
    }
}
