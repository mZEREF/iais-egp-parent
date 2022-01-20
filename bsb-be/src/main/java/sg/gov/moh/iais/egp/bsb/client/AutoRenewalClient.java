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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface AutoRenewalClient {
    @GetMapping(value = "/autoRenewal/select/remindApproval", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, List<ApprovalDto>>> approvalRenewal(@RequestBody Map<String, LocalDate> dateMap, @RequestParam("processType") String processType);

    @GetMapping(value = "/autoRenewal/select/suspendedApproval", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApprovalDto>> getSuspendedApprovalList();

    @GetMapping(value = "/autoRenewal/select/expiredApproval", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApprovalDto>> getExpiredApprovalList();

    @PostMapping(value = "/autoRenewal/allNeedUpdate", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateAllNeedUpdateApproval(@RequestBody Map<String, List<ApprovalDto>> map);
}
