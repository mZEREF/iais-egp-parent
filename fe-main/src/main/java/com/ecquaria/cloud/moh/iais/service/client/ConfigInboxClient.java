package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.QueryHelperResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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

    @GetMapping(value = "/hcsa-config/doQuery",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<QueryHelperResultDto> doQuery(@RequestParam("sql") String sql);

    @GetMapping(path = "/iais-hcsa-service/one-of-hcsa-service/{serviceId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaServiceDto> getHcsaServiceDtoByServiceId(@PathVariable(value = "serviceId") String serviceId);

    @GetMapping(value = "/iais-hcsa-service/active-service-correlation",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceCorrelationDto>> getActiveSvcCorrelation();

    @RequestMapping(path = "/iais-hcsa-service/application-type-by-ids",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Set<String>> getAppGrpPremisesTypeBySvcId(@RequestBody List<String> serviceId);
}
