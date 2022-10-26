package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocChecklistConfigDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionChecklistDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.SelfAssessmtChklDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.AppointmentReviewDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsCertificationInitDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsFindingFormDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsNCEmailDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsNCEmailInitDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsNCRectificationDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsReportAOApprovalProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsReportDoApprovalProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsSubmitFindingDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsSubmitReportDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InspectionProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.PreInspectionDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.followup.InsFollowUpExtensionProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.followup.InsFollowUpProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.followup.InsNCProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.followup.ReviewInsFollowUpDto;
import sg.gov.moh.iais.egp.bsb.dto.process.ApprovalLetterDto;
import sg.gov.moh.iais.egp.bsb.dto.process.ApprovalLetterInitDataDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiInspectionSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskAssignDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;

import java.util.List;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class, contextId = "inspection")
public interface InspectionClient {
    @GetMapping(value = "/inspection/actual/submit-findings/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    InsSubmitFindingDataDto getInitInsFindingData(@RequestParam("appId") String appId);

    @GetMapping(path = "/checklist/inspection", produces = MediaType.APPLICATION_JSON_VALUE)
    InspectionChecklistDto getSavedInspectionChecklist(@RequestParam("appId") String appId);

    @GetMapping(value = "/checklist/config", produces = MediaType.APPLICATION_JSON_VALUE)
    ChecklistConfigDto getMaxVersionChecklistConfig(@RequestParam("insAppId") String insAppId,
                                                    @RequestParam("type") String type);

    @GetMapping(value = "/checklist/config/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ChecklistConfigDto getChecklistConfigById(@PathVariable("id") String id);

    @PostMapping(value = "/inspection/certification/do/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDoCertification(@RequestBody InsProcessDto dto);

    @PostMapping(value = "/inspection/certification/ao/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAoCertification(@RequestBody InsProcessDto dto);

    @PostMapping(value = "/inspection/actual/finding", consumes = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto saveInspectionFindings(@RequestBody InsFindingFormDto findingFormDto);

    @PostMapping(value = "/inspection/actual/validate/submit-combine-chk-list", produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateActualInspectionCombineCheckList(@RequestBody InspectionProcessDto inspectionProcessDto);

    @PostMapping(value = "/inspection/actual/process/to-report")
    void submitInspectionFindingChangeStatusToReport(@RequestBody InspectionProcessDto inspectionProcessDto);

    @PostMapping(value = "/inspection/actual/validate/submit-report", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateActualInspectionDOSubmitReportDecision(@RequestBody InsProcessDto dto, @RequestParam("appId") String appId);

    @PostMapping(value = "/inspection/actual/validate/ao-review-report", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateActualInspectionAOReviewDecision(@RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/actual/validate/do-approve-report", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateInspectionReportDOApprovalDecision(@RequestBody InsReportDoApprovalProcessDto processDto);

    @PostMapping(value = "/inspection/actual/validate/ao-approve-report", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateActualInspectionReportAOApprovalDecision(@RequestBody InsReportAOApprovalProcessDto processDto);

    @PostMapping(value = "/inspection/actual/validate/hm-approve-report", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateActualInspectionReportHMApprovalDecision(@RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/validate/approval-letter/do", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDODraftApprovalLetter(@RequestBody ApprovalLetterDto dto);

    @PostMapping(value = "/inspection/validate/approval-letter/ao", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAOReviewApprovalLetter(@RequestBody ApprovalLetterDto dto);

    @PostMapping(value = "/inspection/actual/certification/approve", consumes = MediaType.APPLICATION_JSON_VALUE)
    void inspectionCertificationApprove(@RequestParam("appId") String appId,
                                       @RequestParam("taskId") String taskId,
                                       @RequestBody InsProcessDto processDto);

    //new
    @PostMapping(value = "/inspection/actual/approval-letter/do/submit-to-ao", consumes = MediaType.APPLICATION_JSON_VALUE)
    void inspectionApprovalLetterDOSubmitToAO(@RequestParam("appId") String appId,
                                              @RequestParam("taskId") String taskId,
                                              @RequestBody ApprovalLetterDto letterDto);

    @PostMapping(value = "/inspection/actual/approval-letter/ao/approve", consumes = MediaType.APPLICATION_JSON_VALUE)
    void inspectionApprovalLetterAOApprove(@RequestParam("appId") String appId,
                                           @RequestParam("taskId") String taskId,
                                           @RequestBody ApprovalLetterDto letterDto);

    @PostMapping(value = "/inspection/actual/approval-letter/ao/route-back-to-do", consumes = MediaType.APPLICATION_JSON_VALUE)
    void inspectionApprovalLetterAORouteBackToDO(@RequestParam("appId") String appId,
                                                 @RequestParam("taskId") String taskId, @RequestBody ApprovalLetterDto letterDto);

    @GetMapping(value = "/inspection/actual/approval-letter/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    ApprovalLetterInitDataDto getInitInsApprovalLetterData(@RequestParam("appId") String appId);

    @GetMapping(value = "/inspection/certification/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    InsCertificationInitDataDto getInsCertificationInitDataDto(@RequestParam("appId") String appId);

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

    @PutMapping(value = "/inspection/chkListAnswerOfficer", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<TaskAssignDto>> getOfficerTaskList(@RequestParam("url") String url, @RequestParam("appId") String appId) ;

    @PostMapping(value = "/inspection/actual/certification/to-ao", consumes = MediaType.APPLICATION_JSON_VALUE)
    void inspectionDoCertificationToAO(@RequestParam("appId") String appId,
                                             @RequestParam("taskId") String taskId,
                                             @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/actual/certification/to-do", consumes = MediaType.APPLICATION_JSON_VALUE)
    void inspectionAoCertificationToDO(@RequestParam("appId") String appId,
                                             @RequestParam("taskId") String taskId,
                                             @RequestBody InsProcessDto processDto);

    /************************************* pre *************************************/
    @GetMapping(value = "/inspection/pre/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    PreInspectionDataDto getPreInspectionDataDto(@RequestParam("appId") String appId);

    @GetMapping(path = "/inspection/pre/self-assessment", produces = MediaType.APPLICATION_JSON_VALUE)
    SelfAssessmtChklDto getSavedSelfAssessment(@RequestParam("appId") String appId);

    @PostMapping(value = "/inspection/pre/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePreInsSubmission(@RequestBody RfiInspectionSaveDto dto);

    @PostMapping(value = "/inspection/pre/ready", consumes = MediaType.APPLICATION_JSON_VALUE)
    void changeInspectionStatusToReady(@RequestParam("appId") String appId,
                                       @RequestParam("taskId") String taskId,
                                       @RequestBody InsProcessDto processDto);

    @GetMapping(path = "/inspection/pre/appointment-review-data-dto", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppointmentReviewDataDto> getOfficerReviewData(@RequestParam("appId") String applicationId, @RequestParam("taskId") String taskId);

    @PostMapping(value = "/inspection/pre/validate/appointment-review-data-dto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAppointmentReviewDataDto(@RequestBody AppointmentReviewDataDto dto);

    @PostMapping(value = "/inspection/pre/appointment-review-data-dto/confirm-proposed-date", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAppointmentConfirmProposedDate(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody AppointmentReviewDataDto dto);

    @PostMapping(value = "/inspection/pre/appointment-review-data-dto/select-another-date", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAppointmentSelectAnotherDate(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId, @RequestBody AppointmentReviewDataDto dto);

    /************************************* report *************************************/
    @GetMapping(value = "/inspection/actual/report", produces = MediaType.APPLICATION_JSON_VALUE)
    InsSubmitReportDataDto getInitInsSubmitReportData(@RequestParam("appId") String appId, @RequestParam("roleId") String roleId);

    @PostMapping(value = "/inspection/actual/validate/report", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateActualInspectionReport(@RequestBody ReportDto reportDto);

    @PostMapping(value = "/inspection/actual/report/report-dto", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveInspectionReportDto(@RequestParam("appId") String appId, @RequestParam("roleId") String roleId, @RequestBody ReportDto reportDto);

    /** DO submit inspection report and route to AO for review */
    @PostMapping(value = "/inspection/actual/report/do/to-ao", consumes = MediaType.APPLICATION_JSON_VALUE)
    void submitInspectionReportToAO(@RequestParam("appId") String appId,
                                    @RequestParam("taskId") String taskId,
                                    @RequestBody InsProcessDto processDto);

    /** AO review inspection report and decide to route back to DO */
    @PostMapping(value = "/inspection/actual/report/review/ao/route-back", consumes = MediaType.APPLICATION_JSON_VALUE)
    void reviewInspectionReportRouteBackToDO(@RequestParam("appId") String appId,
                                             @RequestParam("taskId") String taskId,
                                             @RequestBody InsProcessDto processDto);

    /** DO approve inspection report and decide to mark as final */
    @PostMapping(value = "/inspection/actual/report/finalize/do", consumes = MediaType.APPLICATION_JSON_VALUE)
    String doFinalizeInspectionReport(@RequestParam("appId") String appId,
                                      @RequestParam("taskId") String taskId,
                                      @RequestBody InsReportDoApprovalProcessDto processDto);

    /** AO approve inspection report and decide to mark as final */
    @PostMapping(value = "/inspection/actual/report/finalize/ao", consumes = MediaType.APPLICATION_JSON_VALUE)
    String aoFinalizeInspectionReport(@RequestParam("appId") String appId,
                                      @RequestParam("taskId") String taskId,
                                      @RequestBody InsReportAOApprovalProcessDto processDto);

    /** AO approve inspection report and decide to route back to DO */
    @PostMapping(value = "/inspection/actual/report/finalize/ao/route-back", consumes = MediaType.APPLICATION_JSON_VALUE)
    void aoRouteBackFinalInspectionReport(@RequestParam("appId") String appId,
                                          @RequestParam("taskId") String taskId,
                                          @RequestBody InsReportAOApprovalProcessDto processDto);

    /** AO approve inspection report and route it to HM */
    @PostMapping(value = "/inspection/actual/report/finalize/ao/to-hm", consumes = MediaType.APPLICATION_JSON_VALUE)
    void aoRouteFinalInspectionReportToHM(@RequestParam("appId") String appId,
                                          @RequestParam("taskId") String taskId,
                                          @RequestBody InsReportAOApprovalProcessDto processDto);

    /** HM approve inspection report and approve it */
    @PostMapping(value = "/inspection/actual/report/finalize/hm/approve", consumes = MediaType.APPLICATION_JSON_VALUE)
    void hmApproveFinalInspectionReport(@RequestParam("appId") String appId,
                                        @RequestParam("taskId") String taskId,
                                        @RequestBody InsProcessDto processDto);

    /** HM approve inspection report and reject it */
    @PostMapping(value = "/inspection/actual/report/finalize/hm/reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void hmRejectFinalInspectionReport(@RequestParam("appId") String appId,
                                       @RequestParam("taskId") String taskId,
                                       @RequestBody InsProcessDto processDto);


    /************************************* non-compliance *************************************/
    @GetMapping(value = "/inspection/actual/non-compliance/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    InsNCRectificationDataDto getInitInsNCRectificationData(@RequestParam("appId") String appId);

    @PostMapping(value = "/inspection/non-compliance/validate/do", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDONCRectificationReviewDecision(@RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/non-compliance/validate/ao", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAONCRectificationReviewDecision(@RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/actual/non-compliance/review/finalization", consumes = MediaType.APPLICATION_JSON_VALUE)
    void finalizeReviewInspectionNC(@RequestParam("appId") String appId,
                                    @RequestParam("taskId") String taskId,
                                    @RequestBody InsNCProcessDto processDto);

    @PostMapping(value = "/inspection/actual/non-compliance/ao/route-back", consumes = MediaType.APPLICATION_JSON_VALUE)
    void reviewInspectionNCAORouteBackToDo(@RequestParam("appId") String appId,
                                           @RequestParam("taskId") String taskId,
                                           @RequestBody InsNCProcessDto processDto);

    @PostMapping(value = "/inspection/actual/non-compliance/do/to-ao", consumes = MediaType.APPLICATION_JSON_VALUE)
    void reviewInspectionNCToAO(@RequestParam("appId") String appId,
                                @RequestParam("taskId") String taskId,
                                @RequestBody InsNCProcessDto processDto);

    /************************************* follow-up *************************************/
    @GetMapping(value = "/inspection/actual/do-review-follow-up-items/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    ReviewInsFollowUpDto getInitInsFollowUpData(@RequestParam("appId") String appId, @RequestParam("roleId") String roleId);

    @PostMapping(value = "/inspection/post/validate/do-review-follow-up-extension", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateInspectionFollowUpExtensionReviewDecision(@RequestBody InsFollowUpExtensionProcessDto processDto);

    @PostMapping(value = "/inspection/post/validate/do-review-follow-up-items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePostInspectionDOReviewFollowUpItems(@RequestBody InsFollowUpProcessDto processDto);

    @PostMapping(value = "/inspection/post/validate/ao-review-follow-up-items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePostInspectionAOReviewFollowUpItems(@RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/post/do-review-follow-up-extension/approve", consumes = MediaType.APPLICATION_JSON_VALUE)
    void doReviewInspectionFollowUpAcceptExtension(@RequestParam("appId") String appId,
                                                   @RequestParam("taskId") String taskId,
                                                   @RequestBody InsFollowUpExtensionProcessDto processDto);

    @PostMapping(value = "/inspection/post/do-review-follow-up-extension/reject", consumes = MediaType.APPLICATION_JSON_VALUE)
    void doReviewInspectionFollowUpRejectExtension(@RequestParam("appId") String appId,
                                                   @RequestParam("taskId") String taskId,
                                                   @RequestBody InsFollowUpProcessDto processDto);

    @PostMapping(value = "/inspection/post/do-review-follow-up-extension/rfi", consumes = MediaType.APPLICATION_JSON_VALUE)
    void doReviewInspectionFollowUpExtensionRfi(@RequestParam("appId") String appId,
                                                @RequestParam("taskId") String taskId,
                                                @RequestBody InsFollowUpProcessDto processDto);




    @PostMapping(value = "/inspection/post/do-review-follow-up-items/accept-response", consumes = MediaType.APPLICATION_JSON_VALUE)
    void doReviewInspectionFollowUpItemsAcceptResponse(@RequestParam("appId") String appId,
                                                       @RequestParam("taskId") String taskId,
                                                       @RequestBody InsFollowUpProcessDto processDto);

    @PostMapping(value = "/inspection/post/ao-review-follow-up-items/route-back", consumes = MediaType.APPLICATION_JSON_VALUE)
    void aoReviewInspectionFollowUpItemsRouteBackToDO(@RequestParam("appId") String appId,
                                                      @RequestParam("taskId") String taskId,
                                                      @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/inspection/post/ao-review-follow-up-items/accept-response", consumes = MediaType.APPLICATION_JSON_VALUE)
    void aoReviewInspectionFollowUpItemsAcceptResponse(@RequestParam("appId") String appId,
                                                       @RequestParam("taskId") String taskId,
                                                       @RequestBody InsProcessDto processDto);

    @GetMapping(value = "/inspection/actual/nc-email/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    InsNCEmailInitDataDto getInitInsNCEmailData(@RequestParam("appId") String appId);

    @PostMapping(value = "/inspection/actual/nc-email/do/route-to-ao", consumes = MediaType.APPLICATION_JSON_VALUE)
    void inspectionNCEmailDORouteToAO(@RequestParam("appId") String appId,
                                      @RequestParam("taskId") String taskId,
                                      @RequestBody InsNCEmailDto ncEmailDto);

    @PostMapping(value = "/inspection/actual/nc-email/ao/route-to-applicant", consumes = MediaType.APPLICATION_JSON_VALUE)
    void inspectionNCEmailRouteToApplicant(@RequestParam("appId") String appId,
                                           @RequestParam("taskId") String taskId,
                                           @RequestParam("role") String role,
                                           @RequestBody InsNCEmailDto ncEmailDto);

    @PostMapping(value = "/inspection/actual/nc-email/ao/route-to-do", consumes = MediaType.APPLICATION_JSON_VALUE)
    void inspectionNCEmailAORouteBackToDO(@RequestParam("appId") String appId,
                                          @RequestParam("taskId") String taskId,
                                          @RequestBody InsNCEmailDto ncEmailDto);


    @PostMapping(value = "/inspection/actual/nc-email/do/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateInsDONCEmailDto(@RequestBody InsNCEmailDto dto);

    @PostMapping(value = "/inspection/actual/nc-email/ao/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateInsAONCEmailDto(@RequestBody InsNCEmailDto dto);

    @PostMapping(value = "/inspection/adhoc", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> createAdhocInspection(@RequestBody String facId);
}
