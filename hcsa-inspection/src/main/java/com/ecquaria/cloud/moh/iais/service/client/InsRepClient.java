package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


/**
 * @author weilu
 * @date 2019/11/29 15:44
 */
@FeignClient(name = "iais-application", configuration = {FeignConfiguration.class},
        fallback = InsRepClientFallback.class)
public interface InsRepClient {

    @RequestMapping(path = "/iais-inspection-report/saveData",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveData(@RequestBody AppPremisesRecommendationDto appPremisesRecommendationDto);

    @RequestMapping(path = "/iais-application-be/applicationview/{appNo}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationViewDto> getAppViewByNo(@PathVariable("appNo") String appNo);

    @RequestMapping(path = "/application-number-grp-premiese/{appNo}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppInsRepDto> getAppInsRepDto(@PathVariable("appNo") String appNo);

    @RequestMapping(path = "/iais-application-be",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> updateApplication(@RequestBody ApplicationDto applicationDto);
}
