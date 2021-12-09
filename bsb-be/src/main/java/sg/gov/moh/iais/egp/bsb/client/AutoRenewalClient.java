package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApprovalDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.FacilityDto;

import java.util.List;
import java.util.Map;

@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface AutoRenewalClient {
    @PostMapping(value = "/bsb-be-approvalRenewal", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, List<ApprovalDto>>> approvalRenewal(@RequestBody List<Integer> dayList, @RequestParam("processType") String processType);

    @GetMapping(value = "/getFacility/{approvalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FacilityDto> getFacDtoByActivityApprovalId(@RequestParam("approvalId") String approvalId);

    @GetMapping(value = "/updateApproval/suspended", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApprovalDto>> updateSuspendedApproval();

    @GetMapping(value = "/updateApproval/expired", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApprovalDto>> updateExpiredApproval();

    @PostMapping(value = "/updateApproval/renewable", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateRenewableApproval(@RequestBody List<ApprovalDto> approvalDtoList);
}
