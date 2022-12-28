package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelsDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class,
        fallback = HcsaLicenceCommonClientFallback.class)
public interface HcsaLicenceCommonClient {
    @GetMapping(value = "/hcsa-key-personnel/getPersonnelDtoByLicId/{licId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PersonnelsDto>> getPersonnelDtoByLicId(@PathVariable(name = "licId") String licId);

    @GetMapping(value = "/hcsa-key-personnel/getMobileByRole/{role}/{service}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getMobileByRole(@PathVariable(name = "role") String role,@PathVariable(name = "service") String service);

    @PostMapping(value = "/personnel-roles",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<KeyPersonnelDto>> getKeyPersonnelByRole(@RequestBody List<String> roles);

    @GetMapping(value = "/hcsa-licence/app-svc-align-licence",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppAlignLicQueryDto>> getAppAlignLicQueryDto(@RequestParam("licenseeId") String licenseeId, @RequestParam("svcNameStr") String svcNameStr);

    @RequestMapping(path = "/hcsa-licence/menuLicence",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<MenuLicenceDto>> getMenuLicence(SearchParam searchParam);

    @GetMapping(value = "/hcsa-licence/licenceById/{licId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicDtoById(@PathVariable("licId") String licenceId);

    @GetMapping(value = "/lic-common/licence-one/{licId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicDtoByIdCommon(@PathVariable("licId") String licenceId);

    @GetMapping(value = "/hcsa-key-personnel/getLicenseeIds/{service}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getLicenseeIds(@PathVariable(name = "service") String service);

    @GetMapping(value = "/hcsa-key-personnel/getEmailByRole/{role}/{service}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getEmailByRole(@PathVariable(name = "role") String role,@PathVariable(name = "service") String service);

    @GetMapping(value = "/hcsa-licence/licence-dto-svc-name",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getLicenceDtosBySvcName(@RequestParam("svcName") String svcName);

    @GetMapping(path = "/hcsa-licence/allStatusLicenceView/{licenceId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceViewDto>  getAllStatusLicenceByLicenceId(@PathVariable("licenceId") String licenceId);
}