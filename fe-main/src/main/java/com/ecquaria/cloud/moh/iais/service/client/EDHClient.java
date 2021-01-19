package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;

/**
 * @author yichen
 * @Date:2020/12/22
 */

@FeignClient(name = "edh-service", url = "https://test.api.edh.gov.sg")
public interface EDHClient {
    @PostMapping(value = "/gov/v1/entity/{uen}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> receiveEDHEntity(@RequestHeader(name = "Authorization") String authJson,
                                                       @PathVariable(value = "uen") String uen);
}
