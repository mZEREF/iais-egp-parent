package com.ecquaria.cloud.moh.iais.feign;

import com.ecquaria.cloud.moh.iais.common.dto.eventbus.SubmitReq;
import com.ecquaria.cloud.moh.iais.common.dto.eventbus.SubmitResp;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * EventBusClient
 *
 * @author Jinhua
 * @date 2019/11/9 9:38
 */
@FeignClient(name="iais-event-bus")
public interface EventBusClient {
    @RequestMapping(value = "/event-bus/submit", method = RequestMethod.POST)
    ResponseEntity<SubmitResp> submit(@RequestBody SubmitReq submitReq);
}
