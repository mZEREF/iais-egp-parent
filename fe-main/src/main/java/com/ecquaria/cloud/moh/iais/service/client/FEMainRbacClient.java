package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.organization.EgpUserRoleDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author yichen
 * @Date:2020/8/20
 */

@FeignClient(name = "rbac-service", configuration = FeignConfiguration.class, fallback = FEMainRbacClientFallback.class)
public interface FEMainRbacClient {
    @RequestMapping(path = {"/api/v1/userroleassignments"}, consumes = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> createUerRoleIds(@RequestBody EgpUserRoleDto var1);
}
