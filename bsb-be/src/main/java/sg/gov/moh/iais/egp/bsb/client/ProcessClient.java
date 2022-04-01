package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.process.*;


/**
 * @author : LiRan
 * @date : 2021/8/20
 */
@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface ProcessClient {
    //in use
    @GetMapping(path = "/bsb-moh-officer/do-screening/moh-process-dto", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<MohProcessDto> getMohProcessDtoByAppId(@RequestParam("applicationId") String applicationId, @RequestParam("moduleName") String moduleName);

    @PostMapping(path = "/bsb-moh-officer/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateMohProcessDto(@RequestBody MohProcessDto mohProcessDto, @RequestParam("moduleName") String moduleName);

    @PostMapping(value = "/bsb-moh-officer/do-screening/moh-process-dto/screened-by-do", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoScreenedByDO(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/do-screening/moh-process-dto/request-for-information", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoRequestForInformation(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/do-screening/moh-process-dto/reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoDoReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-screening/moh-process-dto/reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoAoReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-screening/moh-process-dto/route-back-to-do", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoRouteBackToDo(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-screening/moh-process-dto/route-to-hm", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoRouteToHm(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-screening/moh-process-dto/approve", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoAoApprove(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/hm-screening/moh-process-dto/approve-or-reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoHmApproveOrReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);
}
