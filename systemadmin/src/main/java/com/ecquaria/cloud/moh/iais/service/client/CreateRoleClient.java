package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Wenkang
 * @date 2019/12/24 20:49
 */
@FeignClient(name = "egpcloud-api", configuration = FeignConfiguration.class,
        fallback = CreateRoleClientFallback.class)
public interface CreateRoleClient {
    @GetMapping(path = "/rbac-service/api/v1/roles", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<Object>> findAllRoles();
}
