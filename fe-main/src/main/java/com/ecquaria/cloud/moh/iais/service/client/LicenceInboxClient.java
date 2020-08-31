package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.*;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-11-29 13:17
 **/
@FeignClient(name = "hcsa-licence",configuration = FeignConfiguration.class,fallback = LicenceInboxFallback.class)
public interface LicenceInboxClient {
    @PostMapping(path = "/hcsa-licence-transport/licence-param",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<InboxLicenceQueryDto>> searchResultFromLicence(@RequestBody SearchParam searchParam);

    @GetMapping(path= "/hcsa-licence-rfc/licence-premises", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PremisesListQueryDto>> getPremises(@RequestParam(value = "licenseeId" ) String licenseeId);


    @RequestMapping(path = "/hcsa-licence-rfc/licence-personnels",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PersonnelListQueryDto>> getPersonnel(@RequestParam(value = "licenseeId")String licenseeId);

    @RequestMapping(path = "/hcsa-licence-rfc/psn-param",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<PersonnelQueryDto>> searchPsnInfo(@RequestBody SearchParam searchParam);

    @RequestMapping(path = "/hcsa-licence-rfc/assess-psn-param",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<PersonnlAssessQueryDto>> assessPsnDoQuery(@RequestBody SearchParam searchParam);

    @GetMapping(path= "/hcsa-licence-transport/licence-active-num")
    FeignResponseEntity<Integer> getLicActiveStatusNum(@RequestParam("licenseeId")String licenseeId);

    @GetMapping(path= "/hcsa-licence-rfc/licence-bylicence-byNo/{licenceNo}")
    FeignResponseEntity<LicenceDto> getLicBylicNo();

    @RequestMapping(path= "/hcsa-licence-rfc/licence-bylicence-byid/{licenceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicBylicId(@RequestParam(value = "licenceId") String licenceId);

    @GetMapping(value = "/hcsa-licence/licence-id-premises",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PremisesDto>> getPremisesDto(@RequestParam("licenceId") String licenceId);

    @PostMapping(value = "/hcsa-licence-rfc/premises-query-param",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<PremisesListQueryDto>> searchResultPremises(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/personnel-roles",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<KeyPersonnelDto>> getKeyPersonnelByRole(@RequestBody List<String> roles);

    @GetMapping(value = "/hcsa-licence/app-svc-align-licence",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppAlignLicQueryDto>> getAppAlignLicQueryDto(@RequestParam("licenseeId") String licenseeId, @RequestParam("svcNameStr") String svcNameStr);

    @RequestMapping(path = "/hcsa-licence/menuLicence",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<MenuLicenceDto>> getMenuLicence(SearchParam searchParam);

    @GetMapping(path= "/hcsa-licence/licence-orgId/{orgId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicdtoByOrgId(@PathVariable(value = "orgId") String orgId);
    @GetMapping(value = "/hcsa-licence//check-new-licensee")
    FeignResponseEntity<Boolean> checkIsNewLicsee(@RequestParam("licenseeId") String licenseeId);

    @GetMapping(path = "/hcsa-licence/licenceView/{licenceId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceViewDto>  getLicenceViewByLicenceId(@PathVariable("licenceId") String licenceId);

    @GetMapping(path = "/hcsa-licence/assessment-personnel",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PersonnelListDto>>  getPersonnelListAssessment(@RequestParam("idNos") List<String> idNos);

    @GetMapping(value = "/hcsa-licence/lic-premises",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PremisesDto>> getPremisesByLicseeIdAndSvcName(@RequestParam("licenseeId") String licenseeId, @RequestParam("svcNameStr") String svcNameStr);
}