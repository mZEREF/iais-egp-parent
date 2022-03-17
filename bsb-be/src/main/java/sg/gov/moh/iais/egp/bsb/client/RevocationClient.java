package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.*;


@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class, contextId = "revoke")
public interface RevocationClient {
    @PostMapping(path = "/revocation",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<Void> saveRevokeApplication(@RequestBody SubmitRevokeDto dto);

    @PutMapping(value = "/revocation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<Void> updateRevokeApplication(@RequestBody SubmitRevokeDto dto);

    @GetMapping(value = "/revocation/{approvalId}")
    FeignResponseEntity<SubmitRevokeDto> getSubmitRevokeDtoByApprovalId(@PathVariable("approvalId") String id);

    @GetMapping(value = "/revocation")
    FeignResponseEntity<SubmitRevokeDto> getSubmitRevokeDtoByAppId(@RequestParam("applicationId") String id);

    @PostMapping(path = "/revocation/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateRevoke(@RequestBody SubmitRevokeDto dto);
}
