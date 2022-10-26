package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsRectificationDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup.FollowUpInitDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup.FollowUpViewDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.assessment.PreAssessmentDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.SelfAssessmtChklDto;

import java.util.Map;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class, contextId = "inspection")
public interface InspectionClient {
    @GetMapping(path = "/inspection/self-assessment/pre/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<PreAssessmentDto> getAssessmentState(@PathVariable("appId") String appId);

    @GetMapping(path = "/inspection/self-assessment", produces = MediaType.APPLICATION_JSON_VALUE)
    SelfAssessmtChklDto getSavedSelfAssessment(@RequestParam("appId") String appId);

    @PostMapping(value = "/inspection/self-assessment", consumes = MediaType.APPLICATION_JSON_VALUE)
    void submitSelfAssessment(@RequestParam("appId") String appId);

    @PostMapping(value = "/inspection/self-assessment/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    void submitDraftSelfAssessment(@RequestBody SelfAssessmtChklDto selfAssessmtChklDto);

    @GetMapping(value = "/checklist/config", produces = MediaType.APPLICATION_JSON_VALUE)
    ChecklistConfigDto getMaxVersionChecklistConfig(@RequestParam("insAppId") String insAppId,
                                                    @RequestParam("type") String type);

    @GetMapping(value = "/checklist/config/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ChecklistConfigDto getChecklistConfigById(@PathVariable("id") String id);

    @GetMapping(path = "/inspection/non-compliance/items/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InsRectificationDisplayDto> getNonComplianceFindingFormDtoByAppId(@PathVariable("appId") String appId);

    @PostMapping(value = "/inspection/non-compliance/report", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveInsNonComplianceReport(@RequestBody RectifyInsReportSaveDto saveDto);

    @GetMapping(path = "/inspection/followUpItems/appId", produces = MediaType.APPLICATION_JSON_VALUE)
    FollowUpInitDataDto getFollowUpShowDtoByAppId(@RequestParam("appId") String applicationId);

    @PostMapping(value = "/inspection/followUpItems/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFollowUpItems(@RequestBody FollowUpViewDto dto);

    @PostMapping(value = "/inspection/followUpItems", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveFollowUpData(@RequestBody FollowUpInitDataDto dto);

    @PostMapping(value = "/inspection/follow-up/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveFollowUpDataDraft(@RequestBody FollowUpInitDataDto dto);

    @GetMapping(path = "/inspection/submit-report/report", produces = MediaType.APPLICATION_JSON_VALUE)
    ReportDto getInsReportDto(@RequestParam("appId") String appId);

    @PostMapping(value = "/inspection/submit-report/report/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateReportDto(ReportDto reportDto);

    @GetMapping(value = "/inspection/fileNameFileDataMap/insCerFacRelId", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String,byte[]> getInsFileNameFileDataMapByInsCerFacRelId(@RequestParam("insCerFacRelId") String insCerFacRelId);

    @GetMapping(value = "/inspection/report/inspectionAppId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ReportDto> getInspectionReportFinalDataByInspectionAppId(@RequestParam("appId") String appId);

    @GetMapping(value = "/inspection/report/mainAppId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ReportDto> getCertificationReportByMainAppId(@RequestParam("appId") String appId);
}
