package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApprovalDto;
import sg.gov.moh.iais.egp.bsb.dto.suspension.SuspensionReinstatementDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author tangtang
 **/
@FeignClient(value = "bsb-be-api", configuration = FeignClientsConfiguration.class)
public interface SuspensionClient {
    @GetMapping(path = "/bsbSuspension/getSuspensionDataByApprovalId/{appId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<SuspensionReinstatementDto> getSuspensionDataByApprovalId(@PathVariable("appId") String approvalId);

    @GetMapping(path = "/bsbSuspension/getSuspensionDataByApplicationId", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<SuspensionReinstatementDto> getSuspensionDataByApplicationId(@RequestParam("appId") String applicationId);

    @PostMapping(path = "/bsbSuspension/validate/suspension", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateSuspensionDto(@RequestBody SuspensionReinstatementDto dto);

    @PostMapping(value = "/bsbSuspension/doSuspension", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> doSuspension(@RequestBody SuspensionReinstatementDto dto);

    @PostMapping(value = "/bsbSuspension/aoSuspension", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> aoSuspension(@RequestBody SuspensionReinstatementDto dto);

    @PostMapping(value = "/bsbSuspension/hmSuspension", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> hmSuspension(@RequestBody SuspensionReinstatementDto dto);

    @PostMapping(value = "/bsbSuspension/doReinstatement", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> doReinstatement(@RequestBody SuspensionReinstatementDto dto);

    @PostMapping(value = "/bsbSuspension/aoReinstatement", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> aoReinstatement(@RequestBody SuspensionReinstatementDto dto);

    @PostMapping(value = "/bsbSuspension/hmReinstatement", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> hmReinstatement(@RequestBody SuspensionReinstatementDto dto);

    @GetMapping(value = "/bsbSuspension/job/needRemindApproval", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, List<ApprovalDto>>> getNeedRemindApproval(@RequestBody Map<String, LocalDate> dateMap);

    @GetMapping(value = "/bsbSuspension/job/needSuspendApproval", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApprovalDto>> getNeedSuspendApprovals();

    @GetMapping(value = "/bsbSuspension/job/needReinstateApproval", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApprovalDto>> getNeedReinstateApprovals();

    @PostMapping(value = "/bsbSuspension/job/allNeedUpdate", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateAllNeedUpdateApproval(@RequestBody Map<String, List<ApprovalDto>> map);
}
