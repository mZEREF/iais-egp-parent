package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * MasterCodeClient
 *
 * @author Jinhua
 * @date 2019/11/25 21:41
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class,
        fallback = MasterCodeClientFallback.class)
public interface MasterCodeClient {
    @PostMapping(value = "/iais-mastercode/refresh-cache")
    public ResponseEntity<Void> refreshCache();

    @RequestMapping(path = "/message-id",method = RequestMethod.GET)
    FeignResponseEntity<String> messageID();
}
