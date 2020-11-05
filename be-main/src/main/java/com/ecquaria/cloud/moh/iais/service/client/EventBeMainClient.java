package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.QueryHelperResultDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.ecquaria.kafka.model.Submission;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author Wenkang
 * @date 2020/3/20 15:11
 */
@FeignClient(name = "iais-event-bus",configuration = FeignConfiguration.class,fallback = EventBeMainClientCallback.class)
public interface EventBeMainClient {
    @GetMapping(value = "/doQuery/{sql}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<QueryHelperResultDto> doQuery(@PathVariable("sql") String sql);
}
