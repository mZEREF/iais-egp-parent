package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.CheckCoLocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicBaseSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicSvcClinicalDirectorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoringExcel.MonitoringSheetsDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
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


@FeignClient(name = "hcsa-licence",configuration = FeignConfiguration.class,fallback = LicenceInFallback.class)
public interface LicenceClient {

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

    @PostMapping(value = "/hcsa-licence-rfc/personnel-list", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PersonnelListDto>> getPersonnelListDto(@RequestBody PersonnelTypeDto personnelTypeDto);

    @GetMapping(value = "/hcsa-licence/getPersonnelDtoByLicId",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PersonnelsDto>> getPersonnelDtoByLicId(@RequestParam("licId") String licId);

    @PostMapping(value = "/hcsa-licence/licId-spec-cessation",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getSpecLicIdsByLicIds(@RequestBody List<String> licenceIds);

    @GetMapping(value = "/hcsa-licence/premise-cessation-id", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PremisesDto> getLicPremisesDtoById(@RequestParam(value = "id")String id);

    @GetMapping(value = "/hcsa-licence/baseLicId-list-specLicId",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getActSpecIdByActBaseId(@RequestParam("licId") String licId);

//    @GetMapping(value = "/hcsa-licence/lic-premises",produces = MediaType.APPLICATION_JSON_VALUE)
//    FeignResponseEntity<List<PremisesDto>> getPremisesByLicseeIdAndSvcName(@RequestParam("licenseeId") String licenseeId, @RequestParam("svcNameStr") String svcNameStr);

    @GetMapping(value = "/hcsa-licence/app-svc-align-licence",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppAlignLicQueryDto>> getAppAlignLicQueryDto(@RequestParam("licenseeId") String licenseeId, @RequestParam("svcNameStr") String svcNameStr,@RequestParam("premTypeStr") String premTypeStr);

    @GetMapping(value = "/hcsa-licence//check-new-licensee")
    FeignResponseEntity<Boolean> checkIsNewLicsee(@RequestParam("licenseeId") String licenseeId);

    @GetMapping(value = "/hcsa-licence/licenceById/{licId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicDtoById(@PathVariable("licId") String licenceId);

    @GetMapping(value = "/hcsa-licence/licence-licenceNo",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicenceDtoByLicNo(@RequestParam("licenceNo") String licenceNo);

    @GetMapping(value = "/lic-common/existing-onsite-conv--licence",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> existingOnSiteOrConveLic(@RequestParam("svcName") String svcName, @RequestParam("licenseeId") String licenseeId);

    @PostMapping(value = "/hcsa-licence/get-premises-additional",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<MenuLicenceDto>> setPremAdditionalInfo(@RequestBody List<MenuLicenceDto> menuLicenceDtos);

    @GetMapping(value = "/hcsa-licence/get-premise-hciCodeName-fe",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PremisesDto> getPremiseDtoByHciCodeOrName(@RequestParam("hciCodeName") String hciCodeName);

    @GetMapping(value = "/hcsa-licence/listHciName",produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> listHciNames();

    @PostMapping(value = "/hcsa-licence/giro-info",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<GiroAccountInfoDto>> getGiroAccountByHciCodeAndOrgId(@RequestBody List<String> hciCodeList,@RequestParam("orgId") String orgId);

    @PostMapping(value = "/hcsa-licence/bundle-licence-by-hci-code",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> getBundleLicence(@RequestParam("hciCode")String hciCode, @RequestParam("licenseeId") String licenseeId, @RequestBody List<String> svcNameList);
    @PostMapping(value = "/get-svc-clincal-director",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicSvcClinicalDirectorDto>> getLicSvcClinicalDirectorDtoByIdNos(@RequestBody List<String> ids);

    @PostMapping(value = "/hcsa-licence/get-licence-by-prem-corre-id",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getLicenceDtoByPremCorreIds(@RequestBody List<String> premCorreIds);

    @RequestMapping(path= "/hcsa-licence-rfc/licence-bylicence-byid-include-migrated/{licenceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicBylicIdIncludeMigrated(@RequestParam(value = "licenceId") String licenceId);

    @GetMapping(path= "/hcsa-licence/sub-licensees", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<SubLicenseeDto>> getSubLicensees(@RequestParam("orgId") String orgId,
                                                                @RequestParam(value = "licenseeType", required = false) String licenseeType) ;
    @GetMapping(value = "/lic-common/get-sub-licensees-by-id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SubLicenseeDto> getSubLicenseesById(@PathVariable("id") String id);


    @PostMapping(value = "/hcsa-licence/lic-giro-acct-param", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<GiroAccountInfoQueryDto>> searchGiroInfoByParam(@RequestBody SearchParam searchParam);

    @GetMapping(value = "/hcsa-licence/monitoring-licence-sheet",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<MonitoringSheetsDto> getMonitoringLicenceSheetsDto();
}