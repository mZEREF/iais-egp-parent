package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.ecquaria.kafka.model.Submission;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author Wenkang
 * @date 2020/3/20 15:11
 */
@FeignClient(name = "iais-event-bus",configuration = FeignConfiguration.class,fallback = EventClientCallback.class)
public interface EventClient {
    @GetMapping(value = "/submission/{submissionId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<Submission>> getSubmission(@PathVariable("submissionId") String submissionId);
}
