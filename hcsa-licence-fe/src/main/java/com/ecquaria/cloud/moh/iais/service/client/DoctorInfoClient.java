package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DoctorInformationDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class, fallback = DpFeClientFallback.class)
public interface DoctorInfoClient {

    @GetMapping(value = "/data-submission/doctor-information/doctorReignNo", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<DoctorInformationDto> getDoctorInformationDtoByConds(@RequestParam("doctorReignNo") String doctorReignNo,
                                                                             @RequestParam("doctorSource") String doctorSource);
}
