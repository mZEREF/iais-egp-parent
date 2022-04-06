package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.system.EventCallbackTrackDto;
import com.ecquaria.cloud.submission.client.model.GetSubmissionStatusResp;
import com.ecquaria.cloud.submission.client.model.SubmitReq;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @GetMapping(value = "/callbackTracking/{submissionId}/{operation}")
    FeignResponseEntity<EventCallbackTrackDto> getCallbackTracking(@PathVariable("submissionId") String submissionId,
                                                                     @PathVariable("operation") String operation);

    @PutMapping(value = "/callbackTracking")
    FeignResponseEntity<Void> updateCallbackTracking(@RequestBody EventCallbackTrackDto dto);

    @GetMapping(value = "/submission/submissionstatus/{submissionId}/{operation}")
    FeignResponseEntity<GetSubmissionStatusResp> getSubmissionStatus(@PathVariable("submissionId") String submissionId, @PathVariable("operation") String operation);
}
