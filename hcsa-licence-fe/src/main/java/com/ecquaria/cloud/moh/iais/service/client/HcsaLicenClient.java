package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.HcsaLicenceGroupFeeDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/12/26 15:36
 */
@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class,
        fallback = HscaLicenClientFallback.class)
public interface HcsaLicenClient {

    @PostMapping(value = "/hcsa-licence/resHcsaLicenceGroupFee",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaLicenceGroupFeeDto>> retrieveHcsaLicenceGroupFee(@RequestBody List<String> licenceIds);
    @PostMapping(value = "/vehicle/vehicle-is-used",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<Boolean>> vehicleIsUsed(@RequestBody List<String>  vehicle);
}
