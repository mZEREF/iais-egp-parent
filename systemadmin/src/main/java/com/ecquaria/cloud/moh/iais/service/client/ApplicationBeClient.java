package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;

/**
 * @author: yichen
 * @date time:1/6/2020 2:32 PM
 * @description:
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class, fallback = ApplicationBeClientFallback.class)
public interface ApplicationBeClient {
	@PostMapping(value = "/iais-application-be/inspection/date", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<List<Date>> getInspectionRecomInDateByCorreId(@RequestBody List<String> taskRefNum);
}
