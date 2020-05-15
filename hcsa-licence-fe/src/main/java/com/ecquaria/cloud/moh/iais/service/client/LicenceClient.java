package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.*;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "hcsa-licence",configuration = FeignConfiguration.class,fallback = LicenceInFallback.class)
public interface LicenceClient {
    @RequestMapping(path= "/hcsa-licence-rfc/licence-submission", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> getAppSubmissionDto(@RequestParam(value = "licenceId") String licenceId);

    @GetMapping(path= "/hcsa-licence-rfc/lic-corrid{licenceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicAppCorrelationDto>> getLicCorrBylicId(@PathVariable(value = "licenceId") String licenceId);

    @RequestMapping(path= "/hcsa-licence-rfc/licence-bylicence-byid/{licenceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicBylicId(@RequestParam(value = "licenceId") String licenceId);

    @GetMapping(path= "/hcsa-licence-rfc/licence-premises", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PremisesListQueryDto>> getPremises(@RequestParam(value = "licenseeId" ) String licenseeId);
    @GetMapping(value = "/hcsa-licence-rfc/licnece-id-premises",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PremisesListQueryDto>> getPremisesByLicneceId(@RequestParam(value = "licenseeId")  String licenceId);
    @PutMapping(value = "/hcsa-licence-rfc/licence-status", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> doUpdate(@RequestBody LicenceDto licenceDto);

    @PutMapping(value = "/hcsa-licence-rfc/licence-save", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> doSave(@RequestBody LicenceDto licenceDto);

    @GetMapping(value= "/hcsa-licence-rfc/licence-bylicence-byNo",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicBylicNo(@RequestParam("licenceNo")String licenceNo);

    @PostMapping(path = "/hcsa-licence/licence-by-licno",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getLicDtosByLicNos(@RequestBody List<String> licenceNos);

    @GetMapping(value = "/licence-app-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto>  getLicenceByAppId(@RequestParam("appId") String appId);

    @GetMapping(path = "/hcsa-licence/licenceView/{licenceId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceViewDto>  getLicenceViewByLicenceId(@PathVariable("licenceId") String licenceId);

    @GetMapping(value = "/hcsa-licence/licence-id-premises",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PremisesDto>> getPremisesDto(@RequestParam("licenceId") String licenceId);
    @RequestMapping(path = "/hcsa-licence-rfc/licence-personnels",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PersonnelListQueryDto>> getPersonnel(@RequestParam(value = "licenseeId")String licenseeId);

    @PostMapping(path = "/hcsa-licence-rfc/licence-submission-licences", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSubmissionDto>> getAppSubmissionDtos(@RequestBody List<String> licenceIds);

    @GetMapping(path= "/hcsa-licence/application-licence-premises", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppGrpPremisesDto>> getDistinctPremisesByLicenseeId(@RequestParam(value = "licenseeId") String licenseeId);

    @GetMapping(value = "/hcsa-licence/licence/{licenceId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicenceDtoById(@PathVariable(name="licenceId") String licenceId);

    @PostMapping(value = "/hcsa-licence-rfc/psn-param", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<PersonnelQueryDto>> psnDoQuery(SearchParam searchParam);
    @PostMapping(value = "/hcsa-licence/exist-base-service-info",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> getExistBaseSvcInfo(@RequestBody List<String> licenceIds);

    @GetMapping(value = "/hcsa-licence/licenceByLicenseeId/{licenseeId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getLicenceDtoByLicenseeId(@PathVariable(name="licenseeId") String licenseeId);

    @RequestMapping(path = "/hcsa-licence/menuLicence",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<MenuLicenceDto>> getMenuLicence(SearchParam searchParam);

    @GetMapping(path= "/hcsa-licence/application-licence-correlation/{licId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getAppIdsByLicId(@PathVariable(value = "licId") String licId);
    @GetMapping(value = "/hcsa-licence/licences-by-premises-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getLicenceDtosBypremisesId(@RequestParam(value = "premisesId") String premisesId);

    @PostMapping(value = "/hcsa-licence-rfc/premises-query-param",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<PremisesListQueryDto>> getPremises(@RequestBody SearchParam searchParam);

    @GetMapping(value = "/hcsa-licence/licence-dto-by-hci-code",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getLicenceDtoByHciCode(@RequestParam("hciCode")String hciCode);

    @PostMapping(value = "/hcsa-licence-rfc/licence-by-person", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicKeyPersonnelDto>> getLicBypersonId(@RequestBody List<String> personIds);

    @GetMapping(value = "/hcsa-licence-rfc/getPersonnelDtoByLicId/{idNo}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getPersonnelDtoByIdNo(@PathVariable(name = "idNo") String idNo);
}