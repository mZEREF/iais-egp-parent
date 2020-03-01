package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppPremInsDraftDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/11/29 16:17
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = FillUpCheckListGetAppClientFallBack.class)
public interface FillUpCheckListGetAppClient {
    @GetMapping(path = "/iais-application-be/application/correlations/{appid}", produces = { MediaType.APPLICATION_JSON_VALUE })
    FeignResponseEntity<List<AppPremisesCorrelationDto>> getAppPremiseseCorrDto(@PathVariable(value = "appid") String appid);

    @PostMapping(path = "/iais-apppreinschkl-be/AppPremissChkl",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremisesPreInspectChklDto> saveAppPreInspChkl(@RequestBody AppPremisesPreInspectChklDto dto);

    @PutMapping(path = "/iais-apppreinschkl-be/AppPremissChklupdate",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremisesPreInspectChklDto> updateAppPreInspChkl(@RequestBody AppPremisesPreInspectChklDto dto);


    @PostMapping(path = "/application-be/RescomDtoStorage",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremisesRecommendationDto> saveAppRecom(@RequestBody AppPremisesRecommendationDto appPremisesRecommendationDto);

    @PutMapping(path = "/application-be/RescomDtoStorageupdate",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremisesRecommendationDto> updateAppRecom(@RequestBody AppPremisesRecommendationDto appPremisesRecommendationDto);


    @PostMapping(path = "/iais-apppreinsnc-be/AppPremNcResult",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremPreInspectionNcDto> saveAppPreNc(@RequestBody AppPremPreInspectionNcDto dto);

    @PutMapping(path = "/iais-apppreinsnc-be/AppPremNcResultupdate",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremPreInspectionNcDto> updateAppPreNc(@RequestBody AppPremPreInspectionNcDto dto);


    @PostMapping(path = "/iais-apppreinsncitem-be/AppPremNcItemResult",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AppPremisesPreInspectionNcItemDto>> saveAppPreNcItem(@RequestBody List<AppPremisesPreInspectionNcItemDto> dtoList);

    @GetMapping(path = "/application-be/RescomDto/{appPremId}/{recomType}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremisesRecommendationDto> getAppPremRecordByIdAndType(@PathVariable(value ="appPremId" ) String appPremId,@PathVariable(value ="recomType" ) String recomType);

    @GetMapping(path = "/iais-apppreinschkl-be/AppPremissChklId/{appPremId}/{configId}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremisesPreInspectChklDto> getAppPremInspeChlkByAppCorrIdAndConfigId(@PathVariable(value ="appPremId" ) String appPremId,@PathVariable(value ="configId" ) String configId);

    @GetMapping(path = "/iais-apppreinschkl-be/AppPremisslistforDraftChklId/{appPremId}/{configId}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AppPremisesPreInspectChklDto>> getPremInsChklforDraftList(@PathVariable(value ="appPremId" ) String appPremId,@PathVariable(value ="configId" ) String configId);


    @GetMapping(path = "/iais-apppreinsnc-be/AppPremNcByAppCorrId{appCorrId}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremPreInspectionNcDto> getAppNcByAppCorrId(@PathVariable(value ="appCorrId" ) String appCorrId);

    @GetMapping(path = "/iais-apppreinsncitem-be/AppPremNcItemByNcId{ncId}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AppPremisesPreInspectionNcItemDto>> getAppNcItemByNcId(@PathVariable(value ="ncId" ) String ncId);

    @PostMapping(path = "/iais-application-be/apppreminsdraftresult",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremInsDraftDto> saveAppInsDraft(@RequestBody AppPremInsDraftDto dto);

    @PutMapping(path = "/iais-application-be/apppreminsdraftresultupdate",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremInsDraftDto> updateAppInsDraft(@RequestBody AppPremInsDraftDto dto);

    @PostMapping(path = "/iais-application-be/adhocdraftresult",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AdhocDraftDto>> saveAdhocDraft(@RequestBody List<AdhocDraftDto> dtoList);

    @PutMapping(path = "/iais-application-be/adhocdraftresultupdate",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AdhocDraftDto>> updateAdhocDraft(@RequestBody List<AdhocDraftDto> dtoList);


    @GetMapping(path = "/iais-apppreinsncitem-be/adhocdraftbyitemId/{itemId}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AdhocDraftDto>> getAdhocByItemId(@PathVariable(value ="itemId" ) String itemId);

    @GetMapping(path = "/iais-application-be/appindraftbychkId/{chkId}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremInsDraftDto> getAppInsDraftByChkId(@PathVariable(value ="chkId" ) String chkId);

    @PostMapping(path = "/iais-application-be/adhocdraftbyitemlsit",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AdhocDraftDto>> getAdhocDraftItems(@RequestBody List<String> itemList);

    @GetMapping(path = "/iais-apppreinsncitem-be/allrecItem",produces = { MediaType.APPLICATION_JSON_VALUE })
    FeignResponseEntity<List<AppPremisesPreInspectionNcItemDto>> getALlRecItem();

    @GetMapping(path = "/iais-apppreinsncitem-be/ncitemappdto",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<Map<String, ApplicationDto>> getApplicationDtoByNcItem();

    @GetMapping(path = "iais-apppreinschkl-be/AppPremissChklByapppremid/{appPremId}",produces = { MediaType.APPLICATION_JSON_VALUE })
    FeignResponseEntity<List<AppPremisesPreInspectChklDto>> getPremInsChklList(@PathVariable("appPremId") String appPremId);

    @GetMapping(path = "iais-apppreinschkl-be/AppPremissChklByapppremidandversion/{appPremId}/{version}",produces = { MediaType.APPLICATION_JSON_VALUE })
    FeignResponseEntity<List<AppPremisesPreInspectChklDto>> getPremInsChklListByPremIdAndVersion(@PathVariable("appPremId") String appPremId,@PathVariable("version") String version);

    @GetMapping(path = "iais-apppreinschkl-be/AppPremissChklfordraftByapppremid/{appPremId}",produces = { MediaType.APPLICATION_JSON_VALUE })
    FeignResponseEntity<List<AppPremisesPreInspectChklDto>> getPremInsChklListFOrDraft(@PathVariable("appPremId") String appPremId);

    @GetMapping(value = "/iais-apppreinsncitem-be/appncitemdto/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesPreInspectionNcItemDto> getNcItemByItemId(@PathVariable(name = "id") String id);
}
