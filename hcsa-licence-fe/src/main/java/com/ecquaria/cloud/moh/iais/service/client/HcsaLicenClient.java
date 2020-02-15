package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.HcsaLicenceGroupFeeDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/12/26 15:36
 */
@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class,
        fallback = HscaLicenClientFallback.class)
public interface HcsaLicenClient {

    @RequestMapping(value = "/hcsa-licence/resHcsaLicenceGroupFee",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE
            ,method = RequestMethod.POST)
    FeignResponseEntity<List<HcsaLicenceGroupFeeDto>> retrieveHcsaLicenceGroupFee(@RequestBody List<String> licenceIds);


}
