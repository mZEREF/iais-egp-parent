package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppPremInsDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditFillterDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping(path = "/iais-pre-ins-chkl-be/AppPremissChkl",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremisesPreInspectChklDto> saveAppPreInspChkl(@RequestBody AppPremisesPreInspectChklDto dto);

    @GetMapping(value = "/iais-application-be/get-last-insp-sec-insp-type", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AuditFillterDto> getAuditTaskDataDtoByAuditTaskDataDto(@RequestParam("licId") String licId);
    @PutMapping(path = "/iais-pre-ins-chkl-be/AppPremissChklupdate",produces = { MediaType.APPLICATION_JSON_VALUE },
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

    @GetMapping(path = "/application-be/RescomHistoryDtos/{appPremId}/{recomType}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AppPremisesRecommendationDto>> getAppPremisesRecommendationHistoryDtosByIdAndType(@PathVariable(value ="appPremId" ) String appPremId,@PathVariable(value ="recomType" ) String recomType);

    @GetMapping(path = "/iais-pre-ins-chkl-be/AppPremissChklId/{appPremId}/{configId}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremisesPreInspectChklDto> getAppPremInspeChlkByAppCorrIdAndConfigId(@PathVariable(value ="appPremId" ) String appPremId,@PathVariable(value ="configId" ) String configId);

    @GetMapping(path = "/iais-pre-ins-chkl-be/AppPremisslistforDraftChklId/{appPremId}/{configId}",produces = { MediaType.APPLICATION_JSON_VALUE },
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

    @GetMapping(path = "/iais-apppreinsncitem-be/allrecItem",produces = { MediaType.APPLICATION_JSON_VALUE })
    FeignResponseEntity<List<AppPremisesPreInspectionNcItemDto>> getALlRecItem();

    @GetMapping(path = "/iais-apppreinsncitem-be/ncitemappdto",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<ApplicationViewDto>> getApplicationDtoByNcItem();

    @GetMapping(path = "/iais-pre-ins-chkl-be/AppPremissChklByapppremid/{appPremId}",produces = { MediaType.APPLICATION_JSON_VALUE })
    FeignResponseEntity<List<AppPremisesPreInspectChklDto>> getPremInsChklList(@PathVariable("appPremId") String appPremId);

    @GetMapping(path = "/iais-pre-ins-chkl-be/AppPremissChklByapppremidandversion/{appPremId}/{version}",produces = { MediaType.APPLICATION_JSON_VALUE })
    FeignResponseEntity<List<AppPremisesPreInspectChklDto>> getPremInsChklListByPremIdAndVersion(@PathVariable("appPremId") String appPremId,@PathVariable("version") String version);

    @GetMapping(path = "iais-pre-ins-chkl-be/AppPremissChklfordraftByapppremid/{appPremId}",produces = { MediaType.APPLICATION_JSON_VALUE })
    FeignResponseEntity<List<AppPremisesPreInspectChklDto>> getPremInsChklListFOrDraft(@PathVariable("appPremId") String appPremId);

    @GetMapping(value = "/iais-apppreinsncitem-be/appncitemdto/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesPreInspectionNcItemDto> getNcItemByItemId(@PathVariable(name = "id") String id);

    @PostMapping(value = "/iais-pre-ins-chkl-be/premises/checklist/config-usage-status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, Boolean>> configUsageStatus(@RequestBody List<String> configId);

    @GetMapping(value = "/iais-appintranetdoc/get-appintranetdoc-by-premid-status", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppIntranetDocDto> getAppIntranetDocByPremIdAndStatus(@RequestParam(name = "premId") String premId,@RequestParam(name = "docStatus") String status );

    @GetMapping(value = "/iais-appintranetdoc/get-appintranetdocList-by-premid-status", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppIntranetDocDto>> getAppIntranetDocListByPremIdAndStatus(@RequestParam(name = "premId") String premId,@RequestParam(name = "docStatus") String status );

    @GetMapping(value = "/iais-appintranetdoc/delete-appintranetdoc-by-premid", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> deleteAppIntranetDocsByPremId(@RequestParam(name = "premId") String premId);

    @GetMapping(value = "/iais-appintranetdoc/delete-appintranetdoc-by-id", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> deleteAppIntranetDocsById(@RequestParam(name = "id") String id);
    @GetMapping(value = "/iais-appintranetdoc/get-appintranetdoc-by-premid-status-app-type", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppIntranetDocDto> getAppIntranetDocByPremIdAndStatusAndAppDocType(@RequestParam(name = "premId") String premId, @RequestParam(name = "docStatus")String status,@RequestParam(name = "appDocType")String appDocType);
    @PostMapping(value = "/iais-appintranetdoc/save-appintranetdoc",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveAppIntranetDocByAppIntranetDoc(@RequestBody AppIntranetDocDto  appIntranetDocDto);

    @GetMapping(value = "/iais-appPremisesdoc/get-appPremisesSpecialDoc-by-premid", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesSpecialDocDto>> getAppPremisesSpecialDocByPremId(@RequestParam(name = "premId") String premId);
    @GetMapping(value = "/iais-appPremisesdoc/delete-appPremisesSpecialDoc-by-premid", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> deleteAppPremisesSpecialDocByPremId(@RequestParam(name = "premId") String premId);
    @PostMapping(value = "/iais-appPremisesdoc/save-appPremisesSpecialDoc",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveAppPremisesSpecialDoc(@RequestBody AppPremisesSpecialDocDto appIntranetDocDto);
    @PostMapping(value = "/iais-pre-ins-chkl-be/app-premises/pre-inspect-chkl/draft-answers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity <List<AppPremInsDraftDto>> getInspDraftAnswer(@RequestBody List<String> preChklIds);
    @PostMapping(value = "/iais-application-be/adhoc-draft-item/all-results",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AdhocDraftDto>> getAdhocChecklistDraftsByAdhocItemIdIn(@RequestBody List<String> itemList);
    @PostMapping(value = "/iais-pre-ins-chkl-be/app-premises/pre-inspect-chkl/save-draft-answer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<CheckListDraftAllDto> saveDraftAnswerForCheckList(@RequestBody CheckListDraftAllDto checkListDraftAllDto);

    @PutMapping(path = "/iais-pre-ins-chkl-be/rollBack-inspection")
    FeignResponseEntity<Void> rollBackPreInspect(@RequestParam("appPreCorrId") String appPreCorrId);
    @GetMapping(value = "/iais-pre-ins-chkl-be/AppPremissChklId-appPremId-configId",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity< List<AppPremisesPreInspectChklDto>> getPremInsChkls(@RequestParam(name="appPremId")String appPremId,@RequestParam(name="configId") String configId);
}
