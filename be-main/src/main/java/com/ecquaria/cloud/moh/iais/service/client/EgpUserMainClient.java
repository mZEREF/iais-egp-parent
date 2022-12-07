package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.role.Role;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2019/12/31 9:27
 */
@FeignClient(name = "rbac-service", configuration = FeignConfiguration.class, fallback = EgpUserMainClientFallback.class)
public interface EgpUserMainClient {

    @GetMapping(path = "/api/v1/roles/{map}")
    FeignResponseEntity<List<Role>> search(@RequestParam("map") Map<String, String> map);
}
