package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: yichen
 * @Description:
 * @Date:2020/4/8
 **/
@FeignClient(name = "rbac-service", configuration = FeignConfiguration.class, fallback = FeMainEgpUserClientFallBack.class)
public interface FeMainEgpUserClient {
    @RequestMapping(path = {"/api/v1/users"}, consumes = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ClientUser> createClientUser(@RequestBody ClientUser var1);
}
