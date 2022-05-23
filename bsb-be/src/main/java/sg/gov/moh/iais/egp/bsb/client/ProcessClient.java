package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.ReviewAFCReportDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.process.*;


/**
 * @author : LiRan
 * @date : 2021/8/20
 */
@FeignClient(name = "bsb-api", configuration = FeignConfiguration.class)
public interface ProcessClient {
    @GetMapping(path = "/bsb-moh-officer/do-screening/moh-process-dto", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<MohProcessDto> getMohProcessDtoByAppId(@RequestParam("applicationId") String applicationId, @RequestParam("moduleName") String moduleName);

    @PostMapping(path = "/bsb-moh-officer/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateMohProcessDto(@RequestBody MohProcessDto mohProcessDto, @RequestParam("moduleName") String moduleName);

    @PostMapping(value = "/bsb-moh-officer/do-screening/moh-process-dto/screened-by-do", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveDoScreeningScreenedByDO(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/do-screening/moh-process-dto/request-for-information", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveDoScreeningRequestForInformation(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/do-screening/moh-process-dto/reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveDoScreeningReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-screening/moh-process-dto/reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAoScreeningReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-screening/moh-process-dto/route-back-to-do", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAoScreeningRouteBackToDo(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-screening/moh-process-dto/route-to-hm", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAoScreeningRouteToHm(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-screening/moh-process-dto/approve", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAoScreeningApprove(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/hm-screening/moh-process-dto/approve-or-reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveHmScreeningApproveOrReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/do-processing/moh-process-dto/recommend-approval-or-reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveDoProcessingRecommendApprovalOrReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/do-processing/moh-process-dto/request-for-information", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveDoProcessingRequestForInformation(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-processing/moh-process-dto/reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAoProcessingReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-processing/moh-process-dto/route-back-to-do", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAoProcessingRouteBackToDo(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-processing/moh-process-dto/route-to-hm", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAoProcessingRouteToHm(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-processing/moh-process-dto/approve", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAoProcessingApprove(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/hm-processing/moh-process-dto/approve-or-reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveHmProcessingApproveOrReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @GetMapping(value = "/bsb-moh-officer/do-processing/judge", produces = MediaType.APPLICATION_JSON_VALUE)
    String judgeCanSubmitDOProcessingTask(@RequestParam("appId") String appId);

    //InspectionAFCController
    @GetMapping(value = "/certification/afc/latest/inspectionAppId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ReviewAFCReportDto> getLatestCertificationReportByInsAppId(@RequestParam("appId") String appId);

    @GetMapping(value = "/certification/afc/latest/certificationAppId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ReviewAFCReportDto> getLatestCertificationReportByCertAppId(@RequestParam("appId") String appId);
}
