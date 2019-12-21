package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.config.FeignMultipartConfig;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * EicGatewayClient
 *
 * @author Jinhua
 * @date 2019/12/3 17:33
 */
@FeignClient(value = "eicgate", url="${iais.intra.gateway.url}", configuration = {FeignMultipartConfig.class},
        fallback = ComSystemAdminClientFallback.class)
public interface EicGatewayClient {
    @PostMapping(value = "/file-sync-trackings/",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveFile(@RequestBody ProcessFileTrackDto processFileTrackDto,
                                         @RequestHeader("date") String date,

                                         @RequestHeader("authorization") String authorization);
    /**
    * @author: yichen
    * @description: route to BE db
    * @param:
    * @return:
    */
    @PostMapping(value = "/self-decl-bridge/",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> routeSelfDeclData(@RequestBody List<String> contentJsonList,
                                         @RequestHeader("date") String date,
                                         @RequestHeader("authorization") String authorization);
}
