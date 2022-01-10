package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.*;

/**
 *author: Zhu Tangtang
 */
@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class, contextId = "revoke")
public interface RevocationClient {
    @PostMapping(path = "/bsb-application/save",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<Void> saveRevokeApplication(@RequestBody SubmitRevokeDto dto);

    @PostMapping(value = "/bsb-application/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<Void> updateRevokeApplication(@RequestBody SubmitRevokeDto dto);

    @GetMapping(value = "/bsb-application/getApprovalById/approvalId")
    FeignResponseEntity<SubmitRevokeDto> getSubmitRevokeDtoByApprovalId(@RequestParam("approvalId") String id);

    @GetMapping(value = "/bsb-application/getApprovalById/applicationId")
    FeignResponseEntity<SubmitRevokeDto> getSubmitRevokeDtoByAppId(@RequestParam("applicationId") String id);

    @PostMapping(path = "/bsb-application/validate/revoke", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateRevoke(@RequestBody SubmitRevokeDto dto);
}
