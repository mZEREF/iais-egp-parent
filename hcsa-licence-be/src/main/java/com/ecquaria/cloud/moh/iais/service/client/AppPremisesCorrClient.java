package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * AppPremisesCorrelation
 *
 * @author Jinhua
 * @date 2019/12/10 19:53
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = AppPremisesCorrClientFallback.class)
public interface AppPremisesCorrClient {
    @GetMapping(value = "/iais-apppremisescorrelation-be/AppPremisesCorrelations/{appGropId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesCorrelationDto>> getGroupAppsByNo(@PathVariable("appGropId") String appGropId);

    @GetMapping(value = "/iais-apppremisescorrelation-be/app-premises-correlations/{appId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesCorrelationDto>> getAppPremisesCorrelationsByAppId(@PathVariable("appId") String appId);

    @GetMapping(value = "/applicationDtosByPremCorreid/{appCorreId}", produces = MediaType.APPLICATION_JSON_VALUE ,
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<ApplicationDto>> getApplicationDtosByCorreId(@PathVariable("appCorreId") String appCorreId);

    @GetMapping(value = "/iais-apppremisescorrelation-be/AppPremisesCorrelationsByPremises/{appCorreId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesCorrelationDto>> getAppPremisesCorrelationsByPremises(@PathVariable("appCorreId") String appCorreId);

}
