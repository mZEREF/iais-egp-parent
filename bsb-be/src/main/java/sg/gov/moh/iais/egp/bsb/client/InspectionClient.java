package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocChecklistConfigDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionChecklistDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionOutcomeDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.SelfAssessmtChklDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsApprovalLetterDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsApprovalLetterInitDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsFindingFormDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsNCRectificationDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsSubmitFindingDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsSubmitReportDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.PreInspectionDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.followup.ReviewInsFollowUpDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;


@FeignClient(value = "bsb-be-api", configuration = FeignClientsConfiguration.class, contextId = "inspection")
public interface InspectionClient {
    @GetMapping(value = "/inspection/pre/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    PreInspectionDataDto getPreInspectionDataDto(@RequestParam("appId") String appId);

    @GetMapping(value = "/inspection/actual/submit-findings/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    InsSubmitFindingDataDto getInitInsFindingData(@RequestParam("appId") String appId);

    @GetMapping(value = "/inspection/actual/non-compliance/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    InsNCRectificationDataDto getInitInsNCRectificationData(@RequestParam("appId") String appId);

    @GetMapping(value = "/inspection/actual/submit-report/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    InsSubmitReportDataDto getInitInsSubmitReportData(@RequestParam("appId") String appId);

    @GetMapping(path = "/inspection/pre/self-assessment", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value = "/inspection/pre/rfi", consumes = MediaType.APPLICATION_JSON_VALUE)
    void changeInspectionStatusToRfi(@RequestParam("appId") String appId,
                                     @RequestParam("taskId") String taskId,
                                     @RequestParam("rfiFlag") int rfiFlag,
                                     @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/pre/ready", consumes = MediaType.APPLICATION_JSON_VALUE)
    void changeInspectionStatusToReady(@RequestParam("appId") String appId,
                                       @RequestParam("taskId") String taskId,
                                       @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/actual/finding", consumes = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto saveInspectionFindings(@RequestBody InsFindingFormDto findingFormDto);

    @PostMapping(value = "/inspection/actual/outcome", consumes = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto saveInspectionOutcome(@RequestBody InspectionOutcomeDto outcomeDto);

    @PostMapping(value = "/inspection/actual/validate/submit-findings", produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateActualInspectionFindings(@RequestParam("appId") String appId);

    @PostMapping(value = "/inspection/actual/process/to-report")
    void submitInspectionFindingChangeStatusToReport(@RequestParam("appId") String appId,
                                                     @RequestParam("taskId") String taskId);

    @PostMapping(value = "/inspection/actual/validate/submit-report", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateActualInspectionDOSubmitReportDecision(@RequestBody InsProcessDto dto, @RequestParam("appId") String appId);

    @PostMapping(value = "/inspection/actual/validate/ao-review-report", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateActualInspectionAOReviewDecision(@RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/actual/validate/report", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateActualInspectionReport(@RequestBody ReportDto reportDto);

    @PostMapping(value = "/inspection/actual/validate/officer-review-non-compliance", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateActualOfficerReviewNCDecision(@RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/actual/validate/approval-letter", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateInsApprovalLetter(@RequestBody InsApprovalLetterDto dto);

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

    @PostMapping(value = "/inspection/skip-flow", consumes = MediaType.APPLICATION_JSON_VALUE)
    void skipInspection(@RequestParam("appId") String appId,
                        @RequestParam("taskId") String taskId,
                        @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/actual/report/report-dto", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveInspectionReportDto(@RequestParam("appId") String appId, @RequestParam("roleId") String roleId, @RequestBody ReportDto reportDto);

    @PostMapping(value = "/inspection/actual/report/finalize", consumes = MediaType.APPLICATION_JSON_VALUE)
    void finalizeInspectionReport(@RequestParam("appId") String appId,
                                  @RequestParam("taskId") String taskId,
                                  @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/actual/report/review/approve", consumes = MediaType.APPLICATION_JSON_VALUE)
    void reviewInspectionReportApprove(@RequestParam("appId") String appId,
                                       @RequestParam("taskId") String taskId,
                                       @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/post/validate/do-review-follow-up-items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePostInspectionDOReviewFollowUpItems(@RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/post/validate/ao-review-follow-up-items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePostInspectionAOReviewFollowUpItems(@RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/post/do-review-follow-up-items/route-back", consumes = MediaType.APPLICATION_JSON_VALUE)
    void doReviewInspectionFollowUpItemsRouteBackToApplicant(@RequestParam("appId") String appId,
                                                             @RequestParam("taskId") String taskId,
                                                             @RequestParam("appStatus") String appStatus,
                                                             @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/post/do-review-follow-up-items/accept-response", consumes = MediaType.APPLICATION_JSON_VALUE)
    void doReviewInspectionFollowUpItemsAcceptResponse(@RequestParam("appId") String appId,
                                                       @RequestParam("taskId") String taskId,
                                                       @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/post/ao-review-follow-up-items/route-back", consumes = MediaType.APPLICATION_JSON_VALUE)
    void aoReviewInspectionFollowUpItemsRouteBackToDO(@RequestParam("appId") String appId,
                                                      @RequestParam("taskId") String taskId,
                                                      @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/post/ao-review-follow-up-items/accept-response", consumes = MediaType.APPLICATION_JSON_VALUE)
    void aoReviewInspectionFollowUpItemsAcceptResponse(@RequestParam("appId") String appId,
                                                       @RequestParam("taskId") String taskId,
                                                       @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/actual/non-compliance/review/to-ao", consumes = MediaType.APPLICATION_JSON_VALUE)
    void reviewInspectionNCToAO(@RequestParam("appId") String appId,
                                @RequestParam("taskId") String taskId,
                                @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/actual/non-compliance/review/finalization", consumes = MediaType.APPLICATION_JSON_VALUE)
    void finalizeReviewInspectionNC(@RequestParam("appId") String appId,
                                    @RequestParam("taskId") String taskId,
                                    @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/actual/non-compliance/ao/review/rfi", consumes = MediaType.APPLICATION_JSON_VALUE)
    void reviewInspectionNCAORequestForInformation(@RequestParam("appId") String appId,
                                                   @RequestParam("taskId") String taskId,
                                                   @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/actual/non-compliance/do/review/rfi", consumes = MediaType.APPLICATION_JSON_VALUE)
    void reviewInspectionNCDORequestForInformation(@RequestParam("appId") String appId,
                                                   @RequestParam("taskId") String taskId,
                                                   @RequestBody InsProcessDto processDto);

    //new
    @PostMapping(value = "/inspection/actual/approval-letter/do/submit-to-ao", consumes = MediaType.APPLICATION_JSON_VALUE)
    void inspectionApprovalLetterDOSubmitToAO(@RequestParam("appId") String appId,
                                              @RequestParam("taskId") String taskId,
                                              @RequestBody InsApprovalLetterDto letterDto);

    @PostMapping(value = "/inspection/actual/approval-letter/ao/approve", consumes = MediaType.APPLICATION_JSON_VALUE)
    void inspectionApprovalLetterAOApprove(@RequestParam("appId") String appId,
                                           @RequestParam("taskId") String taskId,
                                           @RequestBody InsApprovalLetterDto letterDto);

    @PostMapping(value = "/inspection/actual/approval-letter/ao/route-back-to-do", consumes = MediaType.APPLICATION_JSON_VALUE)
    void inspectionApprovalLetterAORouteBackToDO(@RequestParam("appId") String appId,
                                                 @RequestParam("taskId") String taskId,
                                                 @RequestBody InsApprovalLetterDto letterDto);


    @GetMapping(value = "/inspection/actual/approval-letter/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    InsApprovalLetterInitDataDto getInitInsApprovalLetterData(@RequestParam("appId") String appId);

    @GetMapping(value = "/inspection/actual/do-review-follow-up-items/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    ReviewInsFollowUpDto getInitInsFollowUpData(@RequestParam("appId") String appId);

    @GetMapping(value = "/inspection/chkListAnswerDraft/{userId}/{applicationid}")
    ResponseEntity<InspectionChecklistDto> getChkListDraft(@PathVariable("userId") String userId, @PathVariable("applicationid") String appId);

    @PostMapping(value = "/inspection/chkListAnswerDraft", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> saveChkListDraft(@RequestBody InspectionChecklistDto chkList);


    @PostMapping(value = "/adhoc-checklist/config", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AdhocChecklistConfigDto> saveAdhocChecklistConfig(@RequestBody AdhocChecklistConfigDto adhocChecklistConfigDto);

    @GetMapping(value = "/adhoc-checklist/config", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AdhocChecklistConfigDto> getAdhocChecklistConfigDaoByAppid(@RequestParam("appId") String appId);

    @GetMapping(value = "/inspection/comBinedchkListAnswer/{applicationid}")
    ResponseEntity<InspectionChecklistDto> getCombinedChkList(@PathVariable("applicationid") String appId);

    @PostMapping(value = "/inspection/comBinedchkListAnswer", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> saveCombinedChkList(@RequestBody InspectionChecklistDto chkList);

    @GetMapping(value = "/inspection/inspection-info/{applicationid}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<InspectionInfoDto> getInspectionInfoDto(@PathVariable("applicationid") String appId);
}
