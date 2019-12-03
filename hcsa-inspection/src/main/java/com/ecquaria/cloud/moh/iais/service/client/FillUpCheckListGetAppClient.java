package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2019/11/29 16:17
 */
@FeignClient(name = "iais-application", configuration = FeignConfiguration.class,
        fallback = FillUpCheckListGetAppClientFallBack.class)
public interface FillUpCheckListGetAppClient {
    @GetMapping(path = "/iais-application-be/application/{appNo}", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<ApplicationDto> getAppViewDtoByRefNo(@PathVariable(value = "appNo") String appNo);


    @GetMapping(path = "/application/correlations/{appid}", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AppPremisesCorrelationDto>> getAppPremiseseCorrDto(@PathVariable(value = "appid") String appid);
}
