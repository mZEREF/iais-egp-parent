package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.HcsaLicenceGroupFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicInspectionGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremInspGrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditInspectorDto;
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

    @GetMapping(path = "/hcsa-licence/getActiveHCICode",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity< List<String>> getActiveHCICode();

    @RequestMapping(path = "/hcsa-key-personnel/latestVersion/{idNo}/{orgId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<KeyPersonnelDto> getLatestVersionKeyPersonnelByidNoAndOrgId(@PathVariable(name = "idNo") String idNo,
                                                                           @PathVariable(name = "orgId") String orgId);
    @RequestMapping(value = "/hcsa-licence-fe-date",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    FeignResponseEntity<Map<String, List<LicenceDto>>> licenceRenwal(@RequestBody List<Integer>  days);

    @GetMapping(value = "/hcsa-licence/licPremisesCorrelationsByPremises/{licCorrId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicPremisesDto>> getlicPremisesCorrelationsByPremises(@PathVariable("licCorrId") String licCorreId);

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

    @GetMapping(path= "/hcsa-licence/lic-corr-appId{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicAppCorrelationDto>> getLicCorrByappId(@PathVariable(value = "appId")String appId);

    @RequestMapping(path = "/hcsa-licence/licence-by-licno",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getLicDtosByLicNos(@RequestBody List<String> licenceNos);

    @GetMapping(path = "/hcsa-licence-transport/EventBusLicenceGroupDtos/{refNo}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<EventBusLicenceGroupDtos> getEventBusLicenceGroupDtosByRefNo(@PathVariable(name = "refNo") String refNo);

    @RequestMapping(path = "/lic-eic-request-tracking/{eventRefNo}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<EicRequestTrackingDto> getLicEicRequestTrackingDto(@PathVariable(name = "eventRefNo") String eventRefNo);

    @RequestMapping(path = "/lic-eic-request-tracking",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<EicRequestTrackingDto> updateLicEicRequestTracking(@RequestBody EicRequestTrackingDto licEicRequestTrackingDto);

    @RequestMapping(path = "/hcsa-licence/lic-systemaudit-searchparam",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<AuditTaskDataDto>> searchSysAduit(SearchParam searchParam);
    @PostMapping(value = "/hcsa-licence/cessation-licences-ids",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> updateLicences(@RequestBody List<LicenceDto> licenceDtos);


    @PostMapping(value = "/hcsa-key-personnel/professional-information/results"
            , produces = MediaType.APPLICATION_JSON_VALUE
            , consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ProfessionalInformationQueryDto>> searchProfessionalInformation
            (@RequestBody SearchParam searchParam);

    @GetMapping(path = "/LicPrembylicId{LicId}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<LicPremisesDto>> getLicPremListByLicId(@PathVariable(value ="LicId" ) String LicId);

    @GetMapping(value = "/hcsa-key-personnel/getPersonnelDtoByLicId/{licId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PersonnelsDto>> getPersonnelDtoByLicId(@PathVariable(name = "licId") String licId);

    @GetMapping(value = "hcsa-licence/cessation-licences/{status}/{endDate}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> cessationLicenceDtos(@PathVariable(name = "status") String status,@PathVariable(name = "endDate") String endDate);

    @GetMapping(value = "hcsa-licence/cessation-licences-status/{status}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> listLicencesByStatus(@PathVariable(name = "status") String status);

    @GetMapping(value = "hcsa-licence/audittculist",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AuditTaskDataFillterDto>> getAuditTcuList();

    @GetMapping(value = "/hcsa-licence/licenceViewDto/{licPremCorrId}",produces = MediaType.APPLICATION_JSON_VALUE, consumes =  MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceViewDto> getLicenceViewDtoByLicPremCorrId(@PathVariable("licPremCorrId") String licPremCorrId);

    @PostMapping(path = "/hcsa-licence/licinspectiongroup-result", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicInspectionGroupDto> createLicInspectionGroup(@RequestBody LicInspectionGroupDto licInspectionGroupDto);

    @PutMapping(path = "/hcsa-licence/licinspectiongroup-resultup", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicInspectionGroupDto> updateLicInspectionGroup(@RequestBody LicInspectionGroupDto licInspectionGroupDto);

    @PostMapping(path = "/hcsa-licence/licpreminspgrpcorre-result", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremInspGrpCorrelationDto> createLicInspectionGroupCorre(@RequestBody LicPremInspGrpCorrelationDto licPremInspGrpCorrelationDto);

    @PutMapping(path = "/hcsa-licence/licpreminspgrpcorre-resultup", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremInspGrpCorrelationDto> updateLicInspectionGroupCorre(@RequestBody LicPremInspGrpCorrelationDto licPremInspGrpCorrelationDto);

    @PostMapping(path = "/hcsa-licence/icpremaudit-result", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremisesAuditDto> createLicPremAudit(@RequestBody LicPremisesAuditDto licPremisesAuditDto);

    @PutMapping(path = "/hcsa-licence/icpremaudit-resultup", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremisesAuditDto> updateLicPremAudit(@RequestBody LicPremisesAuditDto licPremisesAuditDto);

    @PostMapping(path = "/hcsa-licence/icpremauditinsp-result", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremisesAuditInspectorDto> createLicPremisesAuditInspector(@RequestBody LicPremisesAuditInspectorDto licPremisesAuditInspectorDto);

    @PutMapping(path = "/hcsa-licence/icpremauditinsp-resultup", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremisesAuditInspectorDto> updateLicPremisesAuditInspector(@RequestBody LicPremisesAuditInspectorDto licPremisesAuditInspectorDto);
    @GetMapping(value = "/hcsa-licence/licence-dto-svc-name",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getLicenceDtosBySvcName(@RequestParam("svcName") String svcName);

    @GetMapping(value = "/hcsa-licence/postInspection-map",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String,List<String>>> getPostInspectionMap();

    @PostMapping(value = "/hcsa-licence/submission-app-licIds", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSubmissionDto>> getAppSubmissionDtos(@RequestBody List<String> licenceIds);

    @PostMapping(value = "/hcsa-licence/licId-premises",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicPremisesDto>> getPremisesByLicIds(@RequestBody List<String> licenceIds);
}
