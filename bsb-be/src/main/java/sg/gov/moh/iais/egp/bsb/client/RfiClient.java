package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sg.gov.moh.iais.egp.bsb.dto.inspection.AppointmentReviewDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.followup.InsFollowUpProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.followup.InsNCProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.process.DOVerificationDto;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiInspectionSaveDto;


@FeignClient(name = "bsb-api", configuration = FeignConfiguration.class)
public interface RfiClient {
    @PostMapping(value = "/rfi/do-screening", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveDOScreeningRfi(@RequestBody MohProcessDto dto, @RequestParam("appId") String appId, @RequestParam("taskId") String taskId);

    @PostMapping(value = "/rfi/do-recommendation", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveDORecommendationRfi(@RequestBody MohProcessDto dto, @RequestParam("appId") String appId, @RequestParam("taskId") String taskId);

    @PostMapping(value = "/rfi/do-verification-dto", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveDOVerificationRfi(@RequestBody DOVerificationDto dto, @RequestParam("appId") String appId, @RequestParam("taskId") String taskId);

    @PostMapping(value = "/rfi/inspection/pre", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveInspectionRfi(@RequestBody RfiInspectionSaveDto dto, @RequestParam("appId") String appId, @RequestParam("taskId") String taskId);

    @PostMapping(value = "/rfi/inspection/appointment", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveInspectionAppointmentRfi(@RequestBody AppointmentReviewDataDto dto, @RequestParam("appId") String appId);

    /** AO review inspection report and decide to approve */
    @PostMapping(value = "/rfi/inspection/report/review/ao/approve", consumes = MediaType.APPLICATION_JSON_VALUE)
    void reviewInspectionReportApprove(@RequestParam("appId") String appId,
                                       @RequestParam("taskId") String taskId,
                                       @RequestBody InsProcessDto processDto);

    /** DO approve inspection report and decide to route back to applicant */
    @PostMapping(value = "/rfi/inspection/report/do/to-applicant", consumes = MediaType.APPLICATION_JSON_VALUE)
    void routeInspectionReportToApplicant(@RequestParam("appId") String appId,
                                          @RequestParam("taskId") String taskId,
                                          @RequestBody InsProcessDto processDto);

    @PostMapping(value = "/rfi/inspection/do-review-follow-up-items/rfi", consumes = MediaType.APPLICATION_JSON_VALUE)
    void doReviewInspectionFollowUpItemsRFI(@RequestParam("appId") String appId,
                                            @RequestParam("taskId") String taskId,
                                            @RequestBody InsFollowUpProcessDto processDto);

    @PostMapping(value = "/rfi/inspection/certification/afc-selection-view", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveInspectionCerAfcSelectionViewRfi(@RequestBody RfiInspectionSaveDto dto, @RequestParam("appId") String appId, @RequestParam("taskId") String taskId);

    @PostMapping(value = "/rfi/inspection/non-compliance/do/rfi", consumes = MediaType.APPLICATION_JSON_VALUE)
    void reviewInspectionNCDORequestForInformation(@RequestParam("appId") String appId,
                                                   @RequestParam("taskId") String taskId,
                                                   @RequestBody InsNCProcessDto processDto);
}
