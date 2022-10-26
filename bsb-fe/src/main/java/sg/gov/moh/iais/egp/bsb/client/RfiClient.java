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
import sg.gov.moh.iais.egp.bsb.dto.appointment.InspectionDateDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.AFCSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup.FollowUpInitDataDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalBatAndActivityDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAfcDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiDataDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiDisplayDto;

@FeignClient(name = "bsb-api", configuration = FeignConfiguration.class, contextId = "rfi")
public interface RfiClient {
    @GetMapping(value = "/rfi/specialRFIIndicator")
    String getSpecialRFIIndicatorById(@RequestParam("rfiDataId") String rfiDataId);

    @GetMapping(value = "/rfi/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<RfiDisplayDto> getProcessingRfiByApplicationId(@PathVariable("applicationId") String applicationId);

    @GetMapping(value = "/rfi/rfiData", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<RfiDataDisplayDto> getRfiDataById(@RequestParam("id") String id);

    @PostMapping(value = "/rfi", produces = MediaType.APPLICATION_JSON_VALUE)
    void saveRfi(@RequestParam("rfiId") String rfiId);

    @PostMapping(value = "/rfi/facility-registration", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppMainInfo> saveFacilityRegistration(@RequestBody FacilityRegisterDto dto, @RequestParam("rfiDataId") String rfiDataId);

    @PostMapping(value = "/rfi/inspection/self-assessment", produces = MediaType.APPLICATION_JSON_VALUE)
    void saveInspectionSelfAssessment(@RequestParam("appId") String appId, @RequestParam("rfiDataId") String rfiDataId, @RequestBody InspectionDateDto dto);

    @PostMapping(value = "/rfi/inspection/report", produces = MediaType.APPLICATION_JSON_VALUE)
    void saveInspectionReport(@RequestBody ReportDto dto, @RequestParam("rfiDataId") String rfiDataId);

    @PostMapping(value = "/rfi/inspection/nc", produces = MediaType.APPLICATION_JSON_VALUE)
    void saveInspectionNC(@RequestBody RectifyInsReportSaveDto dto, @RequestParam("rfiDataId") String rfiDataId);

    @PostMapping(value = "/rfi/inspection/follow-up", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveInspectionFollowUp(@RequestBody FollowUpInitDataDto dto, @RequestParam("rfiDataId") String rfiDataId);

    @PostMapping(value = "/rfi/bat-and-activity-approval", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppMainInfo> saveApprovalBatAndActivity(@RequestBody ApprovalBatAndActivityDto dto, @RequestParam("rfiDataId") String rfiDataId);

    @PostMapping(value = "/certification/applicant", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveApplicantCertificationComment(@RequestBody AFCSaveDto dto, @RequestParam("rfiDataId") String rfiDataId);

    @PostMapping(value = "/rfi/facility-afc/rfi", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppMainInfo> saveApplicantCertificationAfc(@RequestBody FacilityAfcDto dto, @RequestParam("rfiDataId") String rfiDataId);

    @PostMapping(path = "/rfi/inspection/appointment",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    void saveInsDate(@RequestBody InspectionDateDto dto, @RequestParam("rfiDataId") String rfiDataId);
}
