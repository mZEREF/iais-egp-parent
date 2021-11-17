package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.submission.client.model.SubmitReq;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * EventBusClient
 *
 * @author Jinhua
 * @date 2020/9/23 14:00
 */
@FeignClient(name = "iais-event-bus", configuration = FeignConfiguration.class, fallback = EventBusClientClientFallBack.class)
public interface EventBusClient {
    @PostMapping(value = "/trackCompersation")
    FeignResponseEntity<Void> trackCompersation();

    @GetMapping(value = "/recordsToRecover")
    FeignResponseEntity<List<SubmitReq>> getRequestToRecover();
}
