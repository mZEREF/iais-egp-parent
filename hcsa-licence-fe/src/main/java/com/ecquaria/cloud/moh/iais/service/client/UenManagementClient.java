package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.organization.MohUenDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * UenManagementClient
 *
 * @author junyu
 * @date 2020/1/22
 */
@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class,
        fallback = UenManagementClientFallback.class)
public interface UenManagementClient {
    @GetMapping(value = "/uen/uen-no/{uenNo}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<MohUenDto> getMohUenById(@PathVariable("uenNo")String uenNo);

    @PostMapping(value = "/uen/uen-generates",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<MohUenDto>  generatesMohIssuedUen(@RequestBody MohUenDto mohUenDto);
}
