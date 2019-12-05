package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * TaskHcsaConfigClient
 *
 * @author suocheng
 * @date 12/4/2019
 */
@FeignClient(name = "hcsa-config", configuration = FeignConfiguration.class,
        fallback = TaskHcsaConfigClientFallback.class)
public interface TaskHcsaConfigClient {
    @PostMapping(path = "/hcsa-routing/work-group",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcStageWorkingGroupDto>> getWrkGrp(@RequestBody List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDto);
}
