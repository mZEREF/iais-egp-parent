package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApproveGroupDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * AppealClient
 *
 * @author suocheng
 * @date 2/6/2020
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = AppealClientFallback.class)
public interface AppealClient {
    @RequestMapping(path = "/iais-appeal/appeals",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppealApproveGroupDto>> getApproveAppeal();

    @RequestMapping(path = "/app-eic-request-tracking/{eventRefNo}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<EicRequestTrackingDto> getAppEicRequestTrackingDto(@PathVariable(name = "eventRefNo") String eventRefNo);

    @RequestMapping(path = "/app-eic-request-tracking/app-eic-request-tracking",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<EicRequestTrackingDto> updateAppEicRequestTracking(@RequestBody EicRequestTrackingDto appEicRequestTrackingDto);

}
