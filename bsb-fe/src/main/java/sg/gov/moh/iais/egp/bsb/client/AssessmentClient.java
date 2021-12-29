package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.assessment.PreAssessmentDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.SelfAssessmtChklDto;


@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "assessment")
public interface AssessmentClient {
    @GetMapping(path = "/assessment/pre/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<PreAssessmentDto> getAssessmentState(@PathVariable("appId") String appId);

    @GetMapping(path = "/checklist/assessment", produces = MediaType.APPLICATION_JSON_VALUE)
    SelfAssessmtChklDto getSavedSelfAssessment(@RequestParam("appId") String appId);

    @PostMapping(value = "/assessment", consumes = MediaType.APPLICATION_JSON_VALUE)
    void submitSelfAssessment(@RequestBody SelfAssessmtChklDto selfAssessmtChklDto);

    @GetMapping(value = "/checklist/config", produces = MediaType.APPLICATION_JSON_VALUE)
    ChecklistConfigDto getMaxVersionChecklistConfig(@RequestParam("appId") String appId,
                                                    @RequestParam("type") String type);

    @GetMapping(value = "/checklist/config/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ChecklistConfigDto getChecklistConfigById(@PathVariable("id") String id);
}
