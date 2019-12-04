package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2019/11/29 16:17
 */
@FeignClient(name = "iais-application", configuration = FeignConfiguration.class,
        fallback = FillUpCheckListGetAppClientFallBack.class)
public interface FillUpCheckListGetAppClient {
    @GetMapping(path = "/iais-application-be/application/{appNo}", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<ApplicationDto> getAppViewDtoByRefNo(@PathVariable(value = "appNo") String appNo);

    @GetMapping(path = "/iais-application-be/application/correlations/{appid}", produces = { MediaType.APPLICATION_JSON_VALUE })
    FeignResponseEntity<List<AppPremisesCorrelationDto>> getAppPremiseseCorrDto(@PathVariable(value = "appid") String appid);

    @PostMapping(path = "/iais-apppreinschkl-be/AppPremissChkl",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremisesPreInspectChklDto> saveAppPreInspChkl(@RequestBody AppPremisesPreInspectChklDto dto);

    @PostMapping(path = "/application-be/RescomDtoStorage",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremisesRecommendationDto> saveAppRecom(@RequestBody AppPremisesRecommendationDto appPremisesRecommendationDto);

    @PostMapping(path = "/iais-apppreinsnc-be/AppPremNcResult",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremPreInspectionNcDto> saveAppPreNc(@RequestBody AppPremPreInspectionNcDto dto);

    @PostMapping(path = "/iais-apppreinsncitem-be/AppPremNcItemResult",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AppPremisesPreInspectionNcItemDto>> saveAppPreNcItem(@RequestBody List<AppPremisesPreInspectionNcItemDto> dtoList);

    @RequestMapping(path = "/iais-application-be/applicationview/{appNo}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationViewDto> getAppViewDtoByNo(@PathVariable("appNo") String appNo);
}

