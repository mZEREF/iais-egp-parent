package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.Date;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author weilu
 * @date 2019/11/29 15:44
 */
@FeignClient(name = "hcsa-application", configuration = {FeignConfiguration.class},
        fallback = InsRepClientFallback.class)
public interface InsRepClient {

    @RequestMapping(path = "/iais-inspection-report/saveData",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveRecommendationData(@RequestBody AppPremisesRecommendationDto appPremisesRecommendationDto);

    @RequestMapping(path = "/application-number-grp-premiese/{appPremCorreId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppInsRepDto> getAppInsRepDto(@PathVariable("appPremCorreId") String appPremCorreId);

    @GetMapping(value = "/iais-inspection-report/app-type/{appId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> getAppType (@PathVariable("appId") String appId);

    @GetMapping(value = "/iais-inspection-report/is-pre/{appGrpId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto>getApplicationGroupDto (@PathVariable("appGrpId") String appId);

    @PostMapping(value = "/iais-inspection-report/is-rectified" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<Boolean>>isRectified (@RequestBody List<String> itemId);

    @GetMapping(value = "/iais-inspection-report/recommendationDto/{appPremCorreId}/{type}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesRecommendationDto>getRecommendationDto(@PathVariable("appPremCorreId") String appPremCorreId,
                                                                          @PathVariable("type") String type);
    @PostMapping(value = "/iais-application-be/inspection/date", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<Date>> getInspectionRecomInDateByCorreId(@RequestBody List<String> taskRefNum);
}
