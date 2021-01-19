package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.QueryHelperResultDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "iais-event-bus",configuration = FeignConfiguration.class,fallback = EventBeInboxClientCallback.class)
public interface EventFeInboxClient {
    @GetMapping(value = "/doQuery",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<QueryHelperResultDto> doQuery(@RequestParam("sql") String sql);
}
