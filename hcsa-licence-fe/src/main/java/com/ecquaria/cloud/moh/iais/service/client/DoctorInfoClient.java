package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DoctorInformationDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class, fallback = DoctorInfoClientFallback.class)
public interface DoctorInfoClient {
    @GetMapping(value = "/doc-common/doctor-information/allDoctorReignNo", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<DoctorInformationDto> getAllDoctorInformationDtoByConds(@RequestParam("doctorReignNo") String doctorReignNo,
                                                                             @RequestParam("doctorSource") String doctorSource);

    @GetMapping(value = "/doc-common/doctor-information/doctorReignNo", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<DoctorInformationDto> getDoctorInformationDtoByConds(@RequestParam("doctorReignNo") String doctorReignNo,
                                                                             @RequestParam("doctorSource") String doctorSource,
                                                                             @RequestParam("hciCode") String hciCode);

    @GetMapping(value = "/doc-common/rfc-doctor-information/doctorInformationId", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<DoctorInformationDto> getRfcDoctorInformationDtoByConds(@RequestParam("doctorInformationId") String doctorInformationId);
}
