package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * SystemAdminClient
 *
 * @author Jinhua
 * @date 2019/12/3 9:41
 */
@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class,
        fallback = ComSystemAdminClientFallback.class)
public interface ComSystemAdminClient {
    @GetMapping(value = "/iais-orgUserRole/users-by-loginId/{user_id}")
    FeignResponseEntity<OrgUserDto> retrieveOrgUserAccount(@PathVariable("user_id") String userId);
    @GetMapping(value = "/iais-workgroup/wrkgroups/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getWorkGrpsByUserId(@PathVariable(name = "userId") String userId);
    @GetMapping(value = "/iais-orgUserRole/user-roles/{user_id}")
    FeignResponseEntity<List<String>> retrieveUserRoles(@PathVariable("user_id") String userId);
}
