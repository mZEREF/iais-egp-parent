package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-12-20 16:30
 **/

@FeignClient(name = "hcsa-config",configuration = FeignConfiguration.class,fallback = ConfigInboxFallBack.class)
public interface ConfigInboxClient {

    @RequestMapping(path = "/iais-hcsa-service/{serviceId}",method = RequestMethod.GET)
    FeignResponseEntity<String> getServiceNameById(@PathVariable("serviceId")String serviceId);

    @GetMapping(value = "/iais-hcsa-service/active-service",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>> getActiveServices();

    @GetMapping(value = "/iais-hcsa-service/service-correlation",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceCorrelationDto>> serviceCorrelation();


    @RequestMapping(path = "/iais-hcsa-service/hcsa-service-by-ids",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>> getHcsaService(@RequestBody List<String> serviceId);

    @GetMapping(value = "/iais-hcsa-service/active-service-correlation",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceCorrelationDto>> getActiveSvcCorrelation();

    @RequestMapping(path = "/iais-hcsa-service/application-type-by-ids",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Set<String>> getAppGrpPremisesTypeBySvcId(@RequestBody List<String> serviceId);

    @PostMapping(value = "/iais-hcsa-service/hcsa-service-code", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>> getHcsaServiceDtoByCode(@RequestBody List<String> code);

    @PostMapping(value = "/iais-hcsa-service/hcsaServiceSubTypeDtos-ids", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceSubTypeDto>> getHcsaServiceSubTypeDtosByIds(@RequestBody List<String> ids);
}
