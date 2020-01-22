package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sop.rbac.user.UserIdentifier;

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

    @RequestMapping(path = {"/api/v1/users/{userdomain}/{userid}"}, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.DELETE})
    FeignResponseEntity<Boolean> deleteUser(@PathVariable("userdomain") String var1, @PathVariable("userid") String var2);

    @RequestMapping(path = {"/api/v1/users/{userdomain}/{userid}"}, produces = {"application/json"}, method = {RequestMethod.GET})
    FeignResponseEntity<ClientUser> getUserByIdentifier(@PathVariable("userid") String var1, @PathVariable("userdomain") String var2);

    @RequestMapping(path = {"/api/v1/users/password_validate"}, consumes = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.PATCH}, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> validatepassword(@RequestParam("password") String var1, @RequestBody UserIdentifier var2);
}
