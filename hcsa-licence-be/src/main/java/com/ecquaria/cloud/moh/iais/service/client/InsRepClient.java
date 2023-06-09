package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportResultDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


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

    @GetMapping(value = "/iais-inspection-report/is-pre/{appGrpId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto>getApplicationGroupDto (@PathVariable("appGrpId") String appId);

    @PostMapping(value = "/iais-inspection-report/is-rectified" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<Boolean>>isRectified (@RequestBody List<String> itemId);

    @GetMapping(value = "/iais-inspection-report/recommendationDto/{appPremCorreId}/{type}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesRecommendationDto>getRecommendationDto(@PathVariable("appPremCorreId") String appPremCorreId,
                                                                          @PathVariable("type") String type);
    @PostMapping(value = "/iais-application-be/inspection/date", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<Date>> getInspectionRecomInDateByCorreId(@RequestBody List<String> taskRefNum);

    @PostMapping(value = "/iais-inspection-report/saveReportResult" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveReportResult(@RequestBody ReportResultDto reportResultDto );
}
