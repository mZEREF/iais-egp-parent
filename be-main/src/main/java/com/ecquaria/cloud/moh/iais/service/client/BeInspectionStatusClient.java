package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Shicheng
 * @date 2019/12/10 16:52
 **/
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = BeInspectionStatusClientFallback.class)
public interface BeInspectionStatusClient {
    @RequestMapping(path = "/iais-AppInspecStatus/search-status-swo/{status}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AppInspectionStatusDto>> getAppInspectionStatusByStatus(@PathVariable("status") String status);

    @PostMapping(path = "/iais-AppInspecStatus/search-status-one/ids", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AppInspectionStatusDto>> getAppInspecStatusByIds(@RequestBody String ids);

    @RequestMapping(path = "/iais-AppInspecStatus/search-status-three/{appPremCorreId}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppInspectionStatusDto> getAppInspectionStatusByPremId(@PathVariable("appPremCorreId") String appPremCorreId);

    @PostMapping(path = "/iais-AppInspecStatus", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AppInspectionStatusDto>> create(@RequestBody List<AppInspectionStatusDto> appInspecStatusDtos);

    @PutMapping(path = "/iais-AppInspecStatus", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppInspectionStatusDto> update(@RequestBody AppInspectionStatusDto appInspecStatusDto);

    @PostMapping(path = "/iais-AppInspecStatus/create-status", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppInspectionStatusDto> createAppInspectionStatusByAppDto(@RequestBody ApplicationDto applicationDto);
}
