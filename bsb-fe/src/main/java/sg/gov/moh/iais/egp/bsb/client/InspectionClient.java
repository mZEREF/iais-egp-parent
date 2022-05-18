package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup.FollowUpSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup.FollowUpViewDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.assessment.PreAssessmentDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.SelfAssessmtChklDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.*;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class, contextId = "inspection")
public interface InspectionClient {
    @GetMapping(path = "/inspection/self-assessment/pre/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<PreAssessmentDto> getAssessmentState(@PathVariable("appId") String appId);

    @GetMapping(path = "/inspection/self-assessment", produces = MediaType.APPLICATION_JSON_VALUE)
    SelfAssessmtChklDto getSavedSelfAssessment(@RequestParam("appId") String appId);

    @PostMapping(value = "/inspection/self-assessment", consumes = MediaType.APPLICATION_JSON_VALUE)
    void submitSelfAssessment(@RequestBody SelfAssessmtChklDto selfAssessmtChklDto);

    @GetMapping(value = "/checklist/config", produces = MediaType.APPLICATION_JSON_VALUE)
    ChecklistConfigDto getMaxVersionChecklistConfig(@RequestParam("appId") String appId,
                                                    @RequestParam("type") String type);

    @GetMapping(value = "/checklist/config/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ChecklistConfigDto getChecklistConfigById(@PathVariable("id") String id);

    @GetMapping("/inspection/report/{appId}")
    InsCommentReportDataDto retrieveInspectionReport(@PathVariable("appId") String appId);

    @PostMapping(value = "/inspection/report/comment/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateCommentReportForm(CommentInsReportDto.CommentInsReportValidateDto dto);

    @PostMapping(value = "/inspection/report/comment", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveCommentReportForm(CommentInsReportSaveDto saveDto);

    @GetMapping(path = "/inspection/non-compliance/items/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InsRectificationDisplayDto> getNonComplianceFindingFormDtoByAppId(@PathVariable("appId") String appId);

    @PostMapping(value = "/inspection/non-compliance/report", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveInsNonComplianceReport(@RequestBody RectifyInsReportSaveDto saveDto);

    @GetMapping(path = "/inspection/followUpItems/appId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowUpViewDto> getFollowUpShowDtoByAppId(@RequestParam("appId") String applicationId);

    @PostMapping(value = "/inspection/followUpItems/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFollowUpItems(@RequestBody FollowUpViewDto dto);

    @PostMapping(value = "/inspection/followUpItems", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveFollowUpData(@RequestBody FollowUpSaveDto dto);

    @GetMapping(path = "/inspection/submit-report/report", produces = MediaType.APPLICATION_JSON_VALUE)
    ReportDto getInsReportDto(@RequestParam("appId") String appId);

    @PostMapping(value = "/inspection/submit-report/report/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateReportDto(ReportDto reportDto);
}
