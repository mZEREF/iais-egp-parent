package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author weilu
 * @date 2019/11/29 15:44
 */
@FeignClient(name = "hcsa-application", configuration = {FeignConfiguration.class},
        fallback = InsRepClientFallback.class)
public interface InsRepClient {

    @RequestMapping(path = "/iais-inspection-report/saveData",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveData(@RequestBody AppPremisesRecommendationDto appPremisesRecommendationDto);

    @RequestMapping(path = "/application-number-grp-premiese/{appNo}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppInsRepDto> getAppInsRepDto(@PathVariable("appNo") String appNo);

    @GetMapping(value = "/iais-inspection-report/app-type/{appId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> getAppType (@PathVariable("appId") String appId);

    @GetMapping(value = "/iais-inspection-report/is-pre/{appId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto>getApplicationGroupDto (@PathVariable("appId") String appId);

    @PostMapping(value = "/iais-inspection-report/is-rectified" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<Boolean>>isRectified (@RequestBody List<String> itemId);
}
