package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.role.Role;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author shicheng
 * @date 2019/12/31 9:27
 */
@FeignClient(name = "rbac-service", configuration = FeignConfiguration.class, fallback = EgpUserCommonClientFallback.class)
public interface EgpUserCommonClient {
    @RequestMapping(path = "/api/v1/roles/{map}", method = RequestMethod.GET)
    FeignResponseEntity<List<Role>> search(@RequestParam("map") Map<String, String> map);
}
