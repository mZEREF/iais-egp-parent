package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.AFCSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.ReviewAFCReportDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class)
public interface InspectionAFCClient {
    @GetMapping(value = "/certification/afc/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ReviewAFCReportDto> getReviewAFCReportDto(@RequestParam("appId") String appId);

    @PostMapping(value = "/certification/afc/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAFCReportDto(@RequestBody ReviewAFCReportDto dto);

    @PostMapping(value = "/certification/afc", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveAFCAdminInsAFCData(@RequestBody AFCSaveDto dto);

    @PostMapping(value = "/certification/applicant", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveApplicantCertificationData(@RequestBody AFCSaveDto dto);
}
