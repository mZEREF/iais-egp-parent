package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.process.SubmitDetailsDto;
import sg.gov.moh.iais.egp.bsb.entity.*;

import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/8/20
 */
@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface ProcessClient {

    @PostMapping(path = "/bsb_MohOfficer/saveMohProcess",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveMohProcess(@RequestBody MohProcessDto mohProcessDto);

    @GetMapping(path = "/bsb_MohOfficer/applicationMisc")
    FeignResponseEntity<ApplicationMisc> getAppMiscByAppIdAndReasonAndLatestDate(@RequestParam(value = "applicationId") String applicationId,@RequestParam(value = "reason") String reason);

    @GetMapping(value = "/bsb_MohOfficer/submitDetails")
    FeignResponseEntity<SubmitDetailsDto> getSubmitDetailsByAppId(@RequestParam("applicationId") String applicationId);

    @GetMapping(path = "/bsb_MohOfficer/routingHistory/{applicationNo}")
    FeignResponseEntity<List<RoutingHistory>> getRoutingHistoriesByApplicationNo(@RequestParam(name = "applicationNo") String applicationNo);

    @PostMapping(path = "/bsb_MohOfficer/validate/MohProcessDto", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateMohProcessDto(@RequestBody MohProcessDto mohProcessDto);
}
