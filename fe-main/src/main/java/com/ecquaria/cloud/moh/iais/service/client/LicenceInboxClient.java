package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-11-29 13:17
 **/
@FeignClient(name = "hcsa-licence",configuration = FeignConfiguration.class,fallback = LicenceInboxFallback.class)
public interface LicenceInboxClient {
    @RequestMapping(path = "/hcsa-licence-transport/licence-param",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<InboxLicenceQueryDto>> searchResultFromLicence(@RequestBody SearchParam searchParam);

    @RequestMapping(path= "/hcsa-licence-rfc/licence-premises", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PremisesListQueryDto>> getPremises(@RequestParam(value = "licenseeId" ) String licenseeId);


    @RequestMapping(path= "/hcsa-licence-rfc/licence-submission", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> getAppSubmissionDto(@RequestParam(value = "licenceId" ) String licenceId);

    @RequestMapping(path= "/licence-personnels", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PersonnelListQueryDto>> getPersonnel(@RequestParam(value = "licenceId")String licenseeId);

    @RequestMapping(path= "/hcsa-licence-transport/licence-active-num", method = RequestMethod.GET)
    FeignResponseEntity<Integer> getLicActiveStatusNum();

    @RequestMapping(path= "/hcsa-licence-rfc/licence-bylicence-byNo/{licenceNo}", method = RequestMethod.GET)
    FeignResponseEntity<LicenceDto> getLicBylicNo();
}