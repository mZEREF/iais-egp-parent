package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "eicgate", url="${iais.inter.gateway.url}", configuration = {FeignConfiguration.class},
        fallback = EicGatewayFeMainFallBack.class)
public interface EicGatewayFeMainClient {

    @PostMapping(value = "/v1/hcsa-app-recall",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> updateApplicationStatus(@RequestBody RecallApplicationDto recallApplicationDto,
                                                                                     @RequestHeader("date") String date,
                                                                                     @RequestHeader("authorization") String authorization,
                                                                                     @RequestHeader("date-Secondary") String dateSec,
                                                                                     @RequestHeader("authorization-Secondary") String authorizationSec);

    @PostMapping(value = "/v1/task-recall",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<RecallApplicationDto> recallAppChangeTask(@RequestBody RecallApplicationDto recallApplicationDto,
                                                                                     @RequestHeader("date") String date,
                                                                                     @RequestHeader("authorization") String authorization,
                                                                                     @RequestHeader("date-Secondary") String dateSec,
                                                                                     @RequestHeader("authorization-Secondary") String authorizationSec);
}
