package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hcsa-licence",configuration = FeignConfiguration.class,fallback = ArFeClientFallback.class)
public interface ArFeClient {

    @GetMapping(value = "/ar-common/patient/idnumber-nationality", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PatientDto> getPatientDto(@RequestParam(name = "idNumber") String idNumber,
                                                  @RequestParam(name = "nationality") String nationality,  @RequestParam(name = "orgId") String orgId);
}
