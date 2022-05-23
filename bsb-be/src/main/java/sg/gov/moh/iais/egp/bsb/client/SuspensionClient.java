package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApprovalDto;
import sg.gov.moh.iais.egp.bsb.dto.suspension.SuspensionReinstatementDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class)
public interface SuspensionClient {
    @GetMapping(path = "/suspension/init-data/{approvalId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<SuspensionReinstatementDto> getSuspensionDataByApprovalId(@PathVariable("approvalId") String approvalId);

    @GetMapping(path = "/suspension/init-data", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<SuspensionReinstatementDto> getSuspensionDataByApplicationId(@RequestParam("appId") String applicationId);

    @PostMapping(path = "/suspension/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateSuspensionDto(@RequestBody SuspensionReinstatementDto dto);

    @PostMapping(value = "/suspension/do-process", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> doSuspension(@RequestBody SuspensionReinstatementDto dto);

    @PostMapping(value = "/suspension/ao-process", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> aoSuspension(@RequestBody SuspensionReinstatementDto dto);

    @PostMapping(value = "/suspension/hm-process", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> hmSuspension(@RequestBody SuspensionReinstatementDto dto);

    @PostMapping(value = "/suspension/doReinstatement", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> doReinstatement(@RequestBody SuspensionReinstatementDto dto);

    @PostMapping(value = "/suspension/aoReinstatement", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> aoReinstatement(@RequestBody SuspensionReinstatementDto dto);

    @PostMapping(value = "/suspension/hmReinstatement", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> hmReinstatement(@RequestBody SuspensionReinstatementDto dto);

    @GetMapping(value = "/suspension/job/needRemindApproval", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, List<ApprovalDto>>> getNeedRemindApproval(@RequestBody Map<String, LocalDate> dateMap);

    @GetMapping(value = "/suspension/job/needSuspendApproval", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApprovalDto>> getNeedSuspendApprovals();

    @GetMapping(value = "/suspension/job/needReinstateApproval", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApprovalDto>> getNeedReinstateApprovals();

    @PostMapping(value = "/suspension/job/allNeedUpdate", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateAllNeedUpdateApproval(@RequestBody Map<String, List<ApprovalDto>> map);
}
