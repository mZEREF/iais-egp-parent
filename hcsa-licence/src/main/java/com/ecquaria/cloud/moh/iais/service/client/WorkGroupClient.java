package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sop.usergroup.UserGroup;

import java.util.List;

/**
 * WorkGroupClient
 *
 * @author suocheng
 * @date 11/19/2019
 */
@FeignClient(
        name = "rbac-service",
        configuration = {FeignConfiguration.class}
)
public interface WorkGroupClient {
    @RequestMapping(
            path = {"/api/v1/usergroups/{groupno}/users"},
            produces = {"application/json"},
            method = {RequestMethod.GET}
    )
    FeignResponseEntity<List<ClientUser>> getUsersByGroupNo(@PathVariable("groupNo") Long groupNo);

    @RequestMapping(
            path = {"/api/v1/usergroups/shortname/{shortname}"},
            method = {RequestMethod.GET}
    )
    FeignResponseEntity<List<UserGroup>> retrieveAgencyWorkingGroup(@PathVariable("shortname") String var1);

}
