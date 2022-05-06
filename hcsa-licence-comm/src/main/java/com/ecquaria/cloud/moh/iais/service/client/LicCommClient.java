package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.CheckCoLocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicBaseSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.service.callback.LicCommClientFallback;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Auther chenlei on 5/3/2022.
 */
@RequestMapping(value = "lic-common")
@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class, fallback = LicCommClientFallback.class)
public interface LicCommClient {

    @RequestMapping(path = "/active-licence/{licenceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getActiveLicenceById(@PathVariable(value = "licenceId") String licenceId);

    @GetMapping(value = "/licence-dto-by-hci-code", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getLicenceDtoByHciCode(@RequestParam("hciCode") String hciCode,
            @RequestParam("licenseeId") String licenseeId);

    @RequestMapping(path = "/licence-submission", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> getAppSubmissionDto(@RequestParam(value = "licenceId") String licenceId);

    @GetMapping(value = "/licence-view-submission", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> viewAppSubmissionDto(@RequestParam("licenceId") String licenceId);

    @GetMapping(value = "/align-licence/{licenceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSubmissionDto>> getAlginAppSubmissionDtos(@PathVariable("licenceId") String licenceId,
            @RequestParam("checkSpec") Boolean checkSpec);

    @PostMapping(value = "/sub-licensee/licence-submission", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSubmissionDto>> getAppSubmissionDtosBySubLicensee(@RequestBody SubLicenseeDto sublicenseeDto);

    @PostMapping(path = "/licence-submission-licences", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSubmissionDto>> getAppSubmissionDtosByLicenceIds(@RequestBody List<String> licenceIds);

    @PostMapping(value = "/giro-info-licId", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<GiroAccountInfoDto>> getGiroAccountsByLicIds(@RequestBody List<String> licIds);

    @GetMapping(path = "/all-related-lic-app-corrs", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicAppCorrelationDto>> getAllRelatedLicAppCorrs(@RequestParam("licenceId") String licenceId,
            @RequestParam(value = "svcName", required = false) String svcName);

    @GetMapping(value = "/premises-for-business-name", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PremisesDto> getPremisesDtoForBusinessName(@RequestParam("licenceId") String licenceId);

    @GetMapping(value = "/LicBaseSpecifiedCorrelation/{svcType}/{originLicenceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicBaseSpecifiedCorrelationDto>> getLicBaseSpecifiedCorrelationDtos(
            @PathVariable("svcType") String svcType,
            @PathVariable("originLicenceId") String originLicenceId);

    @GetMapping(path = "/application-licence-premises", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppGrpPremisesDto>> getDistinctPremisesByLicenseeId(@RequestParam(value = "licenseeId") String licenseeId,
            @RequestParam(value = "serviceName") String serviceName);

    @GetMapping(value = "/premises-by-hci-name", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PremisesDto>> getPremisesDtoByHciNameAndPremType(@RequestParam("hciName") String hciName,
            @RequestParam("premType") String premType, @RequestParam("licenseeId") String licenseeId);

    @GetMapping(value = "/other-licensee-premises")
    FeignResponseEntity<Boolean> getOtherLicseePremises(@RequestBody CheckCoLocationDto checkCoLocationDto);

    @RequestMapping(path = "/licence-personnels", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PersonnelListQueryDto>> getPersonnel(@RequestParam(value = "licenseeId") String licenseeId);

    @GetMapping(value = "/individual-sub-licensees", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<SubLicenseeDto>> getIndividualSubLicensees(@RequestParam("orgId") String orgId);

    @GetMapping(value = "/lic-premises", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PremisesDto>> getPremisesByLicseeIdAndSvcName(@RequestParam("licenseeId") String licenseeId,
            @RequestParam("svcNames") List<String> svcNames);

    @GetMapping(value = "/inactive-licence-app-correlations", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicAppCorrelationDto>> getInactiveLicAppCorrelations();

    @GetMapping(value = "/getPersonnelDtoByLicId/{idNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getPersonnelDtoByIdNo(@PathVariable(name = "idNo") String idNo);

    @PostMapping(value = "/lic-key-personnel-by-person", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicKeyPersonnelDto>> getLicKeyPersonnelDtoByPersonId(@RequestBody List<String> personIds);

    @GetMapping(value = "/licence-premises-id", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppGrpPremisesDto>> getLicPremisesById(@RequestParam("id") String id);

}
