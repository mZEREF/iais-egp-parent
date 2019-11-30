package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * @author weilu
 * @date 2019/11/29 15:44
 */
@FeignClient(name = "iais-application", configuration = {FeignConfiguration.class},
        fallback = InsRepClientFallback.class)
public interface InsRepClient {

    @GetMapping(path = "/iais-inspection-report/{appNo}")
    FeignResponseEntity<InspectionReportDto> getInspectionReportDtoByAppNo(@PathVariable("appNo") String appNo);
}
