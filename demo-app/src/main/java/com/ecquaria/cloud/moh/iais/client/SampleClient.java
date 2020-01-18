package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.dto.sample.OrgSampleDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * SampleClient
 *
 * @author Jinhua
 * @date 2020/1/18 11:57
 */
@FeignClient(name = "iais-sample", configuration = FeignConfiguration.class,
        fallback = SampleClientFallback.class)
public interface SampleClient {
    @GetMapping(value = "/new-sequence-id")
    FeignResponseEntity<String> getSeqId();

    @GetMapping(value = "/organization/{uenNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrgSampleDto> getOrgByUen(@PathVariable("uenNo") String uenNo);
}
