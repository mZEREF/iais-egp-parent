package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.*;
import sg.gov.moh.iais.egp.bsb.entity.*;

import java.util.List;

/**
 *author: Zhu Tangtang
 */

@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface RevocationClient {

    @GetMapping(path = "/bsb-application/{id}")
    FeignResponseEntity<ViewSelectedRevokeApplicationDto> getRevokeDetailByApplicationId(@PathVariable(name = "id") String id);

    @PostMapping(path = "/bsb-application/save",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<Void> saveRevokeApplication(@RequestBody SubmitRevokeDto dto);

    @PostMapping(path = "/bsb-application/savemisc", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApplicationMisc> saveApplicationMisc(@RequestBody ApplicationMisc misc);

    @GetMapping(value = "/bsb-application/app/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AOQueryResultDto> doQuery(@SpringQueryMap ApprovalOfficerQueryDto queryDto);

    @PostMapping(path = "/bsb-history/saveHistory", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<RoutingHistory> saveHistory(@RequestBody RoutingHistory history);

    @GetMapping(value = "/bsb-history/getAllHistory", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<RoutingHistory>> getAllHistory();

    @PutMapping(value = "/bsb-application/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateApplicationStatusById(@RequestParam(value = "id") String id, @RequestParam(value = "status") String status);

    @PutMapping(value = "/bsb-application/updateApprovalStatusById", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateApprovalStatusById(@RequestParam(value = "id") String id, @RequestParam(value = "status") String status,@RequestParam(value = "decision") String decision);

    @GetMapping(value = "/bsb-application/queryMisc", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationMisc>> getApplicationMiscByAppId(@RequestParam("applicationId") String applicationId);

    @GetMapping(value = "/bsb-application/getActiveApproval", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalQueryResultDto> queryActiveApproval(@SpringQueryMap ApprovalQueryDto queryDto);

    @GetMapping(value = "/bsb-application/getApprovalById/{id}")
    FeignResponseEntity<Approval> getApprovalById(@PathVariable(name = "id") String id);
}
