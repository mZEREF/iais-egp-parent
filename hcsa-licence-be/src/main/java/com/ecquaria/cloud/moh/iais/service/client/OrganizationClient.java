package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/12/4 15:13
 */
@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class,
        fallback = OrganizationClientFallback.class)
public interface OrganizationClient {
    @RequestMapping(value = "/iais-orguser/users-by-ids",method = RequestMethod.POST)
    FeignResponseEntity<List<OrgUserDto>> retrieveOrgUserAccount(@RequestBody List<String> ids);

    @RequestMapping(path = "/iais-task/super/{workGroupId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<TaskDto>> getSupervisorPoolByGroupWordId(@PathVariable(value = "workGroupId") String workGroupId);

    @RequestMapping(path = "/iais-workgroup/orguseraccount/{workGroupId}/{status}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<OrgUserDto>> getUsersByWorkGroupName(@PathVariable(value = "workGroupId") String workGroupId, @PathVariable(name = "status") String status);

}
