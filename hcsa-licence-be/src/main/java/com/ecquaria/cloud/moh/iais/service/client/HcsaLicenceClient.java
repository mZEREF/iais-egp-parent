package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.HcsaLicenceGroupFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicEicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditAdhocItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremInspectiNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremPreInspNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisemPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAdhocChklConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ProfessionalInformationQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2019/12/4 16:02
 */
@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class,
        fallback = HcsaLicenceClientFallback.class)
public interface HcsaLicenceClient {
    @RequestMapping(path = "/hcsa-licence/hci-code-licence-number/{hciCode}",method = RequestMethod.GET)
    FeignResponseEntity<Integer> licenceNumber(@PathVariable("hciCode") String hciCode);
    @RequestMapping(path = "/hcsa-licence/service-group-licence-number/{serivceCode}",method = RequestMethod.GET)
    FeignResponseEntity<String > groupLicenceNumber(@PathVariable("serivceCode") String groupLicence);
    @RequestMapping(path = "/hcsa-licence-transport/licences",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceGroupDto>> createLicence(@RequestBody List<LicenceGroupDto> licenceGroupDtoList);

    @RequestMapping(path = "/hcsa-premises/latestVersion/{hciCode}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PremisesDto> getLatestVersionPremisesByHciCode(@PathVariable(name = "hciCode") String hciCode);

    @RequestMapping(path = "/hcsa-key-personnel/latestVersion/{idNo}/{orgId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<KeyPersonnelDto> getLatestVersionKeyPersonnelByidNoAndOrgId(@PathVariable(name = "idNo") String idNo,
                                                                           @PathVariable(name = "orgId") String orgId);
    @RequestMapping(value = "/hcsa-licence-fe-date",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    FeignResponseEntity<Map<String, List<LicenceDto>>> licenceRenwal(@RequestBody List<Integer>  days);


    @RequestMapping(value = "/hcsa-licence/resHcsaLicenceGroupFee",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE
    ,method = RequestMethod.POST)
    FeignResponseEntity<List<HcsaLicenceGroupFeeDto>> retrieveHcsaLicenceGroupFee(@RequestBody List<String> licenceIds);
    @RequestMapping(value = "/hcsa-licence/licences",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE
            ,method = RequestMethod.POST)
    FeignResponseEntity<List<LicenceDto>> retrieveLicenceDtos(@RequestBody List<String> licenceIds);

    @GetMapping(value = "/hcsa-licence/licence-id-premises-dto",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PremisesDto>> getPremisess(@RequestParam("licenceId") String licenceId);

    @GetMapping(value = "/hcsa-licence/licence/{licenceId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicenceDtoById(@PathVariable(name="licenceId") String licenceId);

    @GetMapping(path= "/hcsa-licence/lic-corrid{licenceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicAppCorrelationDto>> getLicCorrBylicId(@PathVariable(value = "licenceId") String licenceId);

    @RequestMapping(path = "/hcsa-licence/licence-by-licno",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getLicDtosByLicNos(@RequestBody List<String> licenceNos);

    @GetMapping(path = "/hcsa-licence-transport/EventBusLicenceGroupDtos/{refNo}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<EventBusLicenceGroupDtos> getEventBusLicenceGroupDtosByRefNo(@PathVariable(name = "refNo") String refNo);

    @RequestMapping(path = "/lic-eic-request-tracking/{eventRefNo}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicEicRequestTrackingDto> getLicEicRequestTrackingDto(@PathVariable(name = "eventRefNo") String eventRefNo);

    @RequestMapping(path = "/lic-eic-request-tracking",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicEicRequestTrackingDto> updateLicEicRequestTracking(@RequestBody LicEicRequestTrackingDto licEicRequestTrackingDto);

    @RequestMapping(path = "/hcsa-licence/lic-systemaudit-searchparam",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<AuditTaskDataDto>> searchSysAduit(SearchParam searchParam);
    @PostMapping(value = "/hcsa-licence/cessation-licences-ids",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> updateLicences(@RequestBody List<LicenceDto> licenceDtos);


    @PostMapping(value = "/hcsa-key-personnel/professional-information/results"
            , produces = MediaType.APPLICATION_JSON_VALUE
            , consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ProfessionalInformationQueryDto>> searchProfessionalInformation
            (@RequestBody SearchParam searchParam);

    @GetMapping(path = "/hcsa-licence/licPremissChklBylicpremid/{licPremId}",produces = { MediaType.APPLICATION_JSON_VALUE })
    FeignResponseEntity<List<LicPremisemPreInspectChklDto>> getPremInsChklList(@PathVariable("licPremId") String licPremId);

    @GetMapping(value = "/hcsa-licence/auditadhocchecklistbylicpremid/{licpremId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AuditAdhocItemDto>> getAdhocByPremId(@PathVariable(name = "licpremId") String licpremId);

    @GetMapping(path = "/hcsa-licence/RescomDto/{licPremId}/{recomType}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremisesRecommendationDto> getLicPremRecordByIdAndType(@PathVariable(value ="licPremId" ) String licPremId, @PathVariable(value ="recomType" ) String recomType);

    @PostMapping(path = "/hcsa-licence/LicPremissChkl",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremisemPreInspectChklDto> saveLicPreInspChkl(@RequestBody LicPremisemPreInspectChklDto dto);

    @PutMapping(path = "/hcsa-licence/LicPremissChklupdate",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremisemPreInspectChklDto> updateLicPreInspChkl(@RequestBody LicPremisemPreInspectChklDto dto);

    @GetMapping(path = "/hcsa-licence/LicPremisslistChkbylIdconfigId/{premId}/{configId}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremisemPreInspectChklDto> getLicPremInspeChlkBypremIdAndConfigId(@PathVariable(value ="premId" ) String premId, @PathVariable(value ="configId" ) String configId);

    @GetMapping(value = "/hcsa-licence/auditadhoccheckconfigbyappremid/{premId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicPremisesAdhocChklConfigDto> getAdhocConfigByPremCorrId(@PathVariable(name = "premId") String appremId);

    @PostMapping(path = "/hcsa-licence/singleauditconfig",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremisesAdhocChklConfigDto> saveAuditAdhocConfig(@RequestBody LicPremisesAdhocChklConfigDto adhocCheckListConifgDto);

    @PostMapping(path = "/hcsa-licence/audititemstorage",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AuditAdhocItemDto>> saveAdhocItems(@RequestBody List<AuditAdhocItemDto> itemDtoList);


    @PostMapping(path = "/hcsa-licence/RescomDtoStorage",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremisesRecommendationDto> saveAppRecom(@RequestBody LicPremisesRecommendationDto appPremisesRecommendationDto);

    @PutMapping(path = "/hcsa-licence/RescomDtoStorageupdate",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremisesRecommendationDto> updateAppRecom(@RequestBody LicPremisesRecommendationDto appPremisesRecommendationDto);

    @GetMapping(path = "/hcsa-licence/LicPremNcByAppCorrId{premId}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremInspectiNcDto> getAppNcByAppCorrId(@PathVariable(value ="premId" ) String premId);

    @PostMapping(path = "/hcsa-licence/LicPremNcResult",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremInspectiNcDto> saveAppPreNc(@RequestBody LicPremInspectiNcDto dto);

    @PutMapping(path = "/hcsa-licence/LicPremNcResultupdate",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremInspectiNcDto> updateAppPreNc(@RequestBody LicPremInspectiNcDto dto);

    @PostMapping(path = "/hcsa-licence/LicPremNcItemResult",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<LicPremPreInspNcItemDto>> saveAppPreNcItem(@RequestBody List<LicPremPreInspNcItemDto> dtoList);

    @GetMapping(path = "/LicPrembylicId{LicId}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<LicPremisesDto>> getLicPremListByLicId(@PathVariable(value ="LicId" ) String LicId);

    @GetMapping(value = "/hcsa-licence/hcsa-key-personnel/getPersonnelDtoByLicId/{licId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PersonnelsDto> getPersonnelDtoByLicId(@PathVariable(name = "licId") String licId);


}
