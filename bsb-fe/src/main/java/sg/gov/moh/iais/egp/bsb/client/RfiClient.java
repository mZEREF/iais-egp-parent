package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.SelfAssessmtChklDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup.FollowUpSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalBatAndActivityDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.SaveRfiDto;

@FeignClient(name = "bsb-api", configuration = FeignConfiguration.class, contextId = "rfi")
public interface RfiClient {
    @GetMapping(value = "/rfi/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<RfiDisplayDto> getProcessingRfiByApplicationId(@PathVariable("applicationId") String applicationId);

    @PostMapping(value = "/rfi", produces = MediaType.APPLICATION_JSON_VALUE)
    void saveRfi(@RequestParam("rfiId") String rfiId);

    @PostMapping(value = "/rfi/facility-registration", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppMainInfo> saveFacilityRegistration(@RequestBody SaveRfiDto<FacilityRegisterDto> saveRfiDto);

    @PostMapping(value = "/rfi/inspection/self-assessment", produces = MediaType.APPLICATION_JSON_VALUE)
    void saveInspectionSelfAssessment(@RequestBody SaveRfiDto<SelfAssessmtChklDto> saveRfiDto);

    @PostMapping(value = "/rfi/inspection/report", produces = MediaType.APPLICATION_JSON_VALUE)
    void saveInspectionReport(@RequestBody SaveRfiDto<ReportDto> saveRfiDto);

    @PostMapping(value = "/rfi/inspection/nc", produces = MediaType.APPLICATION_JSON_VALUE)
    void saveInspectionNC(@RequestBody SaveRfiDto<RectifyInsReportSaveDto> saveRfiDto);

    @PostMapping(value = "/rfi/inspection/follow-up", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveInspectionFollowUp(@RequestBody SaveRfiDto<FollowUpSaveDto> saveRfiDto);

    @PostMapping(value = "/rfi/bat-and-activity-approval", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppMainInfo> saveNewApplicationToApproval(@RequestBody SaveRfiDto<ApprovalBatAndActivityDto> saveRfiDto);
}
