package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Shicheng
 * @date 2020/6/10 18:11
 **/
@FeignClient(url = "http://192.168.6.52:8879/", configuration = FeignConfiguration.class,
        fallback = SendEmailClientFallback.class)
public interface SendEmailClient {
    @PostMapping(value = "/iais-task/fe-email-task", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> sendEmailByRefNo(@RequestBody ApptFeConfirmDateDto apptFeConfirmDateDto);
}
