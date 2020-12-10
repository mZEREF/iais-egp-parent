package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.arcaUen.GenerateUENDto;
import com.ecquaria.cloud.moh.iais.common.dto.arcaUen.IaisUENDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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

    @PostMapping(value = "/iais-acraUen/entity", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> generateUen(@RequestBody IaisUENDto iaisUENDto);

    @PutMapping(value = "/iais-licensee-be/acra-deregister")
    FeignResponseEntity<Void> acraDeregister(@RequestBody List<String> licenseeIdList);

}
