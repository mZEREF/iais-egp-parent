package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * @author weilu
 * @date 2019/11/29 15:44
 */
@FeignClient(name = "iais-application", configuration = {FeignConfiguration.class},
        fallback = InsRepClientFallback.class)
public interface InsRepClient {

    @GetMapping(path = "/iais-inspection-report/report/{appNo}")
    FeignResponseEntity<InspectionReportDto> getInspectionReportDtoByAppNo(@PathVariable("appNo") String appNo);

    @PostMapping(path = "/iais-inspection-report/saveData")
    FeignResponseEntity<String> saveData(@RequestBody AppPremisesRecommendationDto appPremisesRecommendationDto);
}
