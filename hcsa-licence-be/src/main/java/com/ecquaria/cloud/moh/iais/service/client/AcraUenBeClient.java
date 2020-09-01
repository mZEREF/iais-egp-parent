package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.arcaUen.GenerateUENDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * AcraUenBeClient
 *
 * @author junyu
 * @date 2020/9/1
 */
@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class,
        fallback = AcraUenBeClientFallback.class)
public interface AcraUenBeClient {
    @GetMapping(value = "/iais-acraUen/entity/{uen}")
    FeignResponseEntity<GenerateUENDto> getUen(@PathVariable(name = "uen") String uen);
}
