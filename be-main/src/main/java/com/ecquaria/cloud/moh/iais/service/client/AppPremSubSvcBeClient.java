package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = AppPremSubSvcBeClientFallBack.class)
public interface AppPremSubSvcBeClient {

    @GetMapping(value = "/hcsa-be-svc-subSvcRel/svc-subSvcRel/{appPremCorrId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremSubSvcRelDto>> getAppPremSubSvcRelDtoListByCorrId(@PathVariable(name = "appPremCorrId") String appPremCorrId);

    @GetMapping(value = "/hcsa-be-svc-subSvcRel/svc-subSvcRel-type/{appPremCorrId}/{type}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremSubSvcRelDto>> getAppPremSubSvcRelDtoListByCorrIdAndType(@PathVariable(name = "appPremCorrId") String appPremCorrId,
                                                                                   @PathVariable(name = "type") String type);
}
