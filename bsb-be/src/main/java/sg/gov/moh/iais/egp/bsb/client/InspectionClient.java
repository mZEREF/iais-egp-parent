package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionChecklistDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionOutcomeDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.SelfAssessmtChklDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.*;


@FeignClient(value = "bsb-be-api", configuration = FeignClientsConfiguration.class, contextId = "inspection")
public interface InspectionClient {
    @GetMapping(value = "/inspection/pre/facInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    InsFacInfoDto getInsFacInfo(@RequestParam("appId") String appId);

    @GetMapping(value = "/inspection/actual/submit-findings/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    InsSubmitFindingDataDto getInitInsFindingData(@RequestParam("appId") String appId);

    @GetMapping(value = "/inspection/actual/submit-report/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    InsSubmitReportDataDto getInitInsSubmitReportData(@RequestParam("appId") String appId);

    @GetMapping(path = "/checklist/assessment", produces = MediaType.APPLICATION_JSON_VALUE)
    SelfAssessmtChklDto getSavedSelfAssessment(@RequestParam("appId") String appId);

    @GetMapping(path = "/checklist/inspection", produces = MediaType.APPLICATION_JSON_VALUE)
    InspectionChecklistDto getSavedInspectionChecklist(@RequestParam("appId") String appId);

    @GetMapping(value = "/checklist/config", produces = MediaType.APPLICATION_JSON_VALUE)
    ChecklistConfigDto getMaxVersionChecklistConfig(@RequestParam("appId") String appId,
                                                    @RequestParam("type") String type);

    @GetMapping(value = "/checklist/config/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ChecklistConfigDto getChecklistConfigById(@PathVariable("id") String id);

    @PostMapping(value = "/inspection/pre/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePreInsSubmission(@RequestBody InsProcessDto dto);

    @PostMapping(value = "/inspection/pre/ready", consumes = MediaType.APPLICATION_JSON_VALUE)
    void changeInspectionStatusToReady(@RequestParam("appId") String appId,
                                       @RequestParam("taskId") String taskId,
                                       @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/actual/finding", consumes = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto saveInspectionFindings(@RequestBody InsFindingFormDto findingFormDto);

    @PostMapping(value = "/inspection/actual/outcome", consumes = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto saveInspectionOutcome(@RequestBody InspectionOutcomeDto outcomeDto);

    @PostMapping(value = "/inspection/actual", consumes = MediaType.APPLICATION_JSON_VALUE)
    void submitInspectionChecklist(@RequestBody InspectionChecklistDto checklistDto);

    @PostMapping(value = "/inspection/actual/validate/submit-findings", produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateActualInspectionFindings(@RequestParam("appId") String appId);

    @PostMapping(value = "/inspection/actual/process/to-report")
    void submitInspectionFindingChangeStatusToReport(@RequestParam("appId") String appId,
                                                     @RequestParam("taskId") String taskId);

    @PostMapping(value = "/inspection/actual/validate/submit-report", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateActualInspectionDOSubmitReportDecision(@RequestBody InsProcessDto dto, @RequestParam("appId") String appId);

    @PostMapping(value = "/inspection/actual/validate/ao-review-report", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateActualInspectionAOReviewDecision(@RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/actual/process/to-ao", consumes = MediaType.APPLICATION_JSON_VALUE)
    void submitInspectionReportToAO(@RequestParam("appId") String appId,
                                    @RequestParam("taskId") String taskId,
                                    @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/actual/process/to-applicant", consumes = MediaType.APPLICATION_JSON_VALUE)
    void routeInspectionReportToApplicant(@RequestParam("appId") String appId,
                                          @RequestParam("taskId") String taskId,
                                          @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/actual/report/review/route-back", consumes = MediaType.APPLICATION_JSON_VALUE)
    void reviewInspectionReportRouteBackToDO(@RequestParam("appId") String appId,
                                             @RequestParam("taskId") String taskId,
                                             @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/actual/report/finalize", consumes = MediaType.APPLICATION_JSON_VALUE)
    void finalizeInspectionReport(@RequestParam("appId") String appId,
                                  @RequestParam("taskId") String taskId,
                                  @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/actual/report/review/approve", consumes = MediaType.APPLICATION_JSON_VALUE)
    void reviewInspectionReportApprove(@RequestParam("appId") String appId,
                                       @RequestParam("taskId") String taskId,
                                       @RequestBody InsProcessDto processDto);
}
