package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.arca.uen.IaisUENDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
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
    @PostMapping(value = "/iais-acraUen/entity", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> generateUen(@RequestBody IaisUENDto iaisUENDto);

    @PutMapping(value = "/iais-licensee-be/acra-deregister",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> acraDeregister(@RequestBody List<String> licenseeIdList);

}
