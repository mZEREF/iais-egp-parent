package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author guyin
 * @date 2020/1/22 15:54
 */
@FeignClient(name = "iais-organization",configuration = FeignConfiguration.class,fallback = UserRoleClientFallback.class)
public interface UserRoleClient {
    @PostMapping(value = "/iais-orgUserRole/setAvailable", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity <Void> setAvailable(@RequestBody List<String> user);

    @PostMapping(value = "/iais-orgUserRole/getAvailable", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> getAvailable(@RequestParam("userId") String userId);

}
