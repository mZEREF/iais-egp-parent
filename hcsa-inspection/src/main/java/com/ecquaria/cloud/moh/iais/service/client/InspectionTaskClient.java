package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Shicheng
 * @date 2019/11/26 10:33
 **/
@FeignClient(name = "application", configuration = FeignConfiguration.class,
        fallback = InspectionTaskClientFallback.class)
public interface InspectionTaskClient {
    @RequestMapping(path = "/iais-inspection/one-of-inspection/{applicationNo}",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<ApplicationDto> getApplicationDtoByAppNo(@PathVariable("applicationNo") String applicationNo);

    @RequestMapping(path = "/iais-inspection/appGroup-of-inspection/{appGroupId}",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<ApplicationGroupDto> getApplicationGroupDtoByAppGroId(@PathVariable("appGroupId") String appGroupId);

    @RequestMapping(path = "/iais-application/application-premises-by-app-id/{applicationId}",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppGrpPremisesDto> getAppGrpPremisesDtoByAppGroId(@PathVariable("applicationId") String applicationId);
}
