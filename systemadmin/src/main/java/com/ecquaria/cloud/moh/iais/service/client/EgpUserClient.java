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
 * @author weilu
 * @date 2019/12/31 9:27
 */
@FeignClient(name = "rbac-service", configuration = FeignConfiguration.class, fallback = EgpUserClientFallback.class)
public interface EgpUserClient {
    @RequestMapping(path = {"/api/v1/users"}, consumes = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ClientUser> createClientUser(@RequestBody ClientUser var1);


    @RequestMapping(path = {"/api/v1/users"}, consumes = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.PATCH}, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ClientUser> updateClientUser(ClientUser var1);

//    @RequestMapping(path = "/iais-hcsa-service/service/{code}",method = RequestMethod.GET)
//    FeignResponseEntity<String>  getServiceIdByCode(@PathVariable(name = "code")String svcCode);
}
