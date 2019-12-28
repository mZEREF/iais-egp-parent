package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: yichen
 * @date time:12/28/2019 2:55 PM
 * @description:
 */


@FeignClient(name = "iais-appointment",  configuration = FeignConfiguration.class,
        fallback = OnlineApptClientFallback.class)
public interface OnlineApptClient {
    @PostMapping(value = "/iais-appointment/blacked-out-date/results", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ApptBlackoutDateQueryDto>> doQuery(@RequestBody SearchParam searchParam);
}
