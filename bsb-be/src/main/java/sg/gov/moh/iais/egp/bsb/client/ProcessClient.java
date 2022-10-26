package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.ReviewAFCReportDto;
import sg.gov.moh.iais.egp.bsb.dto.process.DOVerificationDto;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;


@FeignClient(name = "bsb-api", configuration = FeignConfiguration.class)
public interface ProcessClient {
    @GetMapping(path = "/bsb-moh-officer/moh-process-dto", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<MohProcessDto> getMohProcessDtoByAppId(@RequestParam("applicationId") String applicationId, @RequestParam("functionName") String functionName);

    @PostMapping(path = "/bsb-moh-officer/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateMohProcessDto(@RequestBody MohProcessDto mohProcessDto, @RequestParam("functionName") String functionName);

    @PostMapping(value = "/bsb-moh-officer/do-screening/moh-process-dto/screened-by-do", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveDOScreeningScreenedByDO(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/do-screening/moh-process-dto/reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveDOScreeningReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-screening/moh-process-dto/reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAOScreeningReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-screening/moh-process-dto/route-back-to-do", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAOScreeningRouteBackToDo(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-screening/moh-process-dto/route-to-hm", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAOScreeningRouteToHm(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-screening/moh-process-dto/approve", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveAOScreeningApprove(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/hm-screening/moh-process-dto/approve-or-reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveHMScreeningApproveOrReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/do-processing/moh-process-dto/approve-or-reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveDOProcessingApproveOrReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-processing/moh-process-dto/reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAOProcessingReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-processing/moh-process-dto/route-back-to-do", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAOProcessingRouteBackToDo(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-processing/moh-process-dto/route-to-hm", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAOProcessingRouteToHm(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/ao-processing/moh-process-dto/approve", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAOProcessingApprove(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/hm-processing/moh-process-dto/approve-or-reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveHMProcessingApproveOrReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @GetMapping(value = "/bsb-moh-officer/do-processing/judge", produces = MediaType.APPLICATION_JSON_VALUE)
    String judgeCanSubmitDOProcessingTask(@RequestParam("appId") String appId);


    @GetMapping(value = "/bsb-moh-officer/ao-processing/hm-submit/judge", produces = MediaType.APPLICATION_JSON_VALUE)
    String judgeCanSubmitAOProcessingTask(@RequestParam("appId") String appId);

    // api InspectionAFCController
    @GetMapping(value = "/certification/report/inspectionAppId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ReviewAFCReportDto> getCertificationReportByMainAppId(@RequestParam("appId") String appId);

    // api Inspection Controller
    @GetMapping(value = "/inspection/final-report", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ReportDto> getFinalInsReportData(@RequestParam("appId") String appId);

    /************************************************** DO Verification **************************************************************/

    @GetMapping(path = "/bsb-moh-officer/do-verification", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DOVerificationDto> getDOVerificationByAppId(@RequestParam("applicationId") String applicationId);

    @PostMapping(path = "/bsb-moh-officer/do-verification/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDOVerificationDto(@RequestBody DOVerificationDto doVerificationDto);

    @PostMapping(value = "/bsb-moh-officer/do-verification/do-verification-dto/accept", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveDOVerificationAccept(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody DOVerificationDto doVerificationDto);

    @PostMapping(value = "/bsb-moh-officer/do-verification/do-verification-dto/reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveDOVerificationReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody DOVerificationDto doVerificationDto);

    /************************************************** Facility Renew Defer **************************************************************/

    @PostMapping(value = "/bsb-moh-officer/defer-renew/do-processing/approve-or-reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveDOProcessRenewDeferApproveOrReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/defer-renew/ao-processing/route-back-to-do", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAOProcessRenewDeferRouteBackToDo(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/defer-renew/ao-processing/route-to-hm", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAOProcessRenewDeferRouteToHm(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/defer-renew/ao-processing/reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAOProcessRenewDeferReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/defer-renew/ao-processing/approve", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAOProcessRenewDeferApprove(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);

    @PostMapping(value = "/bsb-moh-officer/defer-renew/hm-processing/approve-or-reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveHMProcessRenewDeferApproveOrReject(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody MohProcessDto mohProcessDto);
}
