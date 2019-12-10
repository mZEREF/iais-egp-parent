package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
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
}
