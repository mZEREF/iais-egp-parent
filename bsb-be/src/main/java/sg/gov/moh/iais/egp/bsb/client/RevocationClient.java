package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.revocation.ApprovalOfficerQueryResultsDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.BsbRoutingHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.RevocationDetailsDto;

import java.util.List;

/*
 *author: Zhu Tangtang
 */

@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface RevocationClient {

    @GetMapping(path = "/bsb-application/{id}")
    FeignResponseEntity<RevocationDetailsDto> getApplicationById(@PathVariable(name = "id") String id);

    @PostMapping(path = "/bsb-application/save",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApprovalOfficerQueryResultsDto> saveApplication(@RequestBody ApprovalOfficerQueryResultsDto approvalOfficerQueryResultsDto);

    @PostMapping(path = "/bsb-application/savemisc", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<RevocationDetailsDto> saveApplicationMisc(@RequestBody RevocationDetailsDto revocationDetailsDto);

    @PostMapping(value = "/bsb-application/query", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ApprovalOfficerQueryResultsDto>> doQuery(@RequestBody SearchParam param);

    @PostMapping(path = "/bsb-application/saveHistory", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<BsbRoutingHistoryDto> saveHistory(@RequestBody BsbRoutingHistoryDto historyDto);

    @PostMapping(value = "/bsb-application/getAllHistory", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<BsbRoutingHistoryDto>> getAllHistory();

    @RequestMapping(value = "/bsb-application/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateApplicationStatusById(@RequestParam(value = "id") String id, @RequestParam(value = "status") String status);

    @RequestMapping(value = "/bsb-application/updateFacilityStatus", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateFacilityStatusById(@RequestParam(value = "id") String id, @RequestParam(value = "status") String status);

}
