package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.QueryHelperResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.PostInsGroupDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "hcsa-licence",configuration = FeignConfiguration.class,fallback = LicenceInFallback.class)
public interface LicenceClient {

    @GetMapping(value = "/licence-app-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto>  getLicenceByAppId(@RequestParam("appId") String appId);

    @GetMapping(value = "/hcsa-licence/licenceViewDto",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceViewDto> getLicenceViewDtoByLicPremCorrId(@RequestParam(value ="licPremCorrId") String licPremCorrId);

    @GetMapping(value = "/hcsa-licence/licence/{licenceId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicBylicId(@PathVariable(name="licenceId") String licenceId);

    @GetMapping(value = "/hcsa-licence/doQuery",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<QueryHelperResultDto> doQuery(@RequestParam("sql") String sql);

    @GetMapping(value = "/hcsa-licence/doDelete",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> doDeleteBySql(@RequestParam("sql") String sql);

    @GetMapping(value = "/hcsa-licence/baseLicId-list-specLicId",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getActSpecIdByActBaseId(@RequestParam("licId") String licId);

    @GetMapping(value = "/hcsa-licence/licence-dto-licensee-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getLicenceDtosByLicenseeId(@RequestParam("licenseeId") String licenseeId);

    @GetMapping(value = "/hcsa-licence/licId-premises-hcicode-audit",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicPremisesAuditDto> getLicPremisesAuditDtoByLicIdAndHCICode(@RequestParam("licId") String licId, @RequestParam("HCICode") String HCICode);

    @GetMapping(value = "/hcsa-licence/licence-orgId-corrId/get-postInsGroupDto",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PostInsGroupDto> getPostInsGroupDto(@RequestParam(name = "licId") String licId, @RequestParam(name = "corrId") String  corrId);

    @PostMapping(value = "/hcsa-licence/licence-orgId-corrId/savePostInsGroupDto",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PostInsGroupDto> savePostInsGroupDto(@RequestBody PostInsGroupDto postInsGroupDto);

    @GetMapping(value = "/hcsa-licence/licenceById/{licId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicDtoById(@PathVariable("licId") String licenceId);

    @GetMapping(value = "/hcsa-licence/licence/{licenceId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicenceDtoById(@PathVariable(name="licenceId") String licenceId);
}