package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.process.*;

import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/8/20
 */
@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface ProcessClient {
    @GetMapping(path = "/bsb-moh-officer/getAOScreeningData/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AOScreeningDto> getAOScreeningDataByAppId(@PathVariable("applicationId") String applicationId);

    @GetMapping(path = "/bsb-moh-officer/getHMScreeningData/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<HMScreeningDto> getHMScreeningDataByAppId(@PathVariable("applicationId") String applicationId);

    @GetMapping(path = "/bsb-moh-officer/getDOProcessingData/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DOProcessingDto> getDOProcessingDataByAppId(@PathVariable("applicationId") String applicationId);

    @GetMapping(path = "/bsb-moh-officer/getAOProcessingData/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AOProcessingDto> getAOProcessingDataByAppId(@PathVariable("applicationId") String applicationId);

    @PostMapping(path = "/bsb-moh-officer/validate/aoScreeningDto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAOScreeningDto(@RequestBody AOScreeningDto aoScreeningDto);

    @PostMapping(path = "/bsb-moh-officer/validate/hmScreeningDto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateHMScreeningDto(@RequestBody HMScreeningDto hmScreeningDto);

    @PostMapping(path = "/bsb-moh-officer/validate/doProcessingDto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDOProcessingDto(@RequestBody DOProcessingDto doProcessingDto);

    @PostMapping(path = "/bsb-moh-officer/validate/aoProcessingDto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAOProcessingDto(@RequestBody AOProcessingDto aoProcessingDto);

    @PostMapping(path = "/bsb-moh-officer/saveAOScreening",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveAOScreening(@RequestBody AOScreeningDto aoScreeningDto);

    @PostMapping(path = "/bsb-moh-officer/saveHMScreening",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveHMScreening(@RequestBody HMScreeningDto hmScreeningDto);

    @PostMapping(path = "/bsb-moh-officer/saveDOProcessing",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveDOProcessing(@RequestBody DOProcessingDto doProcessingDto);

    @PostMapping(path = "/bsb-moh-officer/saveAOProcessing",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveAOProcessing(@RequestBody AOProcessingDto aoProcessingDto);

    @GetMapping(path = "/bsb-moh-officer/getDifferentModuleDoc/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<DocDisplayDto> getDifferentModuleDoc(@PathVariable("applicationId") String applicationId);

    @GetMapping(path = "/bsb-moh-officer/mohProcessDto/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<MohProcessDto> getMohProcessDtoByAppId(@PathVariable("applicationId") String applicationId);

    @PostMapping(path = "/bsb-moh-officer/validate/mohProcessDto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateMohProcessDto(@RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/mohProcessDto/screenedByDO", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoScreenedByDO(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/mohProcessDto/requestForInformation", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoRequestForInformation(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/mohProcessDto/doReject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoDoReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/mohProcessDto/recommendApproval", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoRecommendApproval(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/mohProcessDto/recommendRejection", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoRecommendRejection(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/mohProcessDto/approveForInspection", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoApproveForInspection(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/mohProcessDto/aoReject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoAoReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/mohProcessDto/routeBackToDo", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoRouteBackToDo(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/mohProcessDto/routeToHm", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoRouteToHm(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/mohProcessDto/aoApproved", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoAoApproved(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/mohProcessDto/hmApprove", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoHmApprove(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/mohProcessDto/hmReject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveMohProcessDtoHmReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);
}
