package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Shicheng
 * @date 2020/2/18 11:26
 **/
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = ApplicationClientFallback.class)
public interface InspectionFeClient {

    @PostMapping(value = "/iais-appt-inspec-fe", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesInspecApptDto>> createAppPremisesInspecApptDto(@RequestBody List<AppPremisesInspecApptDto> appPremisesInspecApptDtos);

    @PutMapping(value = "/iais-appt-inspec-fe", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesInspecApptDto> updateAppPremisesInspecApptDto(@RequestBody AppPremisesInspecApptDto appPremisesInspecApptDto);

    @GetMapping(value = "/iais-appt-inspec-fe/appt-specific-dto/{appPremCorrId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesInspecApptDto> getSpecificDtoByAppPremCorrId(@PathVariable(name = "appPremCorrId")String appPremCorrId);

    @GetMapping(value = "/iais-appt-inspec-fe/appt-systemdate-dto/{appPremCorrId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesInspecApptDto>> getSystemDtosByAppPremCorrId(@PathVariable(name = "appPremCorrId")String appPremCorrId);
}
