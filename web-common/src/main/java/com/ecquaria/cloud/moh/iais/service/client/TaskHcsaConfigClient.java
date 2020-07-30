package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * TaskHcsaConfigClient
 *
 * @author suocheng
 * @date 12/4/2019
 */
@FeignClient(name = "hcsa-config", configuration = FeignConfiguration.class,
        fallback = TaskHcsaConfigClientFallback.class)
public interface TaskHcsaConfigClient {
    @RequestMapping(path = "/hcsa-routing/work-group",method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcStageWorkingGroupDto>> getWrkGrp(@RequestBody List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDto);

    @RequestMapping(path = "/iais-hcsa-service/task-type-list-applications",method = RequestMethod.POST)
    FeignResponseEntity<String> getSendTaskType(@RequestParam("map")String stringMap);


}
