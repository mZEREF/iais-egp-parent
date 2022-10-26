package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.AFCSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.CertificationAfcDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.ReviewAFCReportDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiInspectionSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class)
public interface InspectionAFCClient {
    @GetMapping(value = "/certification/afc/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ReviewAFCReportDto> getReviewAFCReportDto(@RequestParam("appId") String appId);

    @PostMapping(value = "/certification/do/form-validation/report", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDOCertificationReport(@RequestBody ReviewAFCReportDto dto);

    @PostMapping(value = "/certification/ao/form-validation/report", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAOCertificationReport(@RequestBody ReviewAFCReportDto dto);

    @PostMapping(value = "/certification/report", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveInsAFCData(@RequestBody AFCSaveDto dto);

    @GetMapping(value = "/certification/facility-afc/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    CertificationAfcDataDto getInitCerFacilityAfcData(@RequestParam("appId") String appId);

    @PostMapping(value = "/certification/facility-afc/do/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAfcSelectionView(@RequestBody RfiInspectionSaveDto processDto);

    @PostMapping(value = "/certification/facility-afc/approve", consumes = MediaType.APPLICATION_JSON_VALUE)
    String doViewAfcSelectionApprove(@RequestParam("appId") String appId,
                                      @RequestParam("taskId") String taskId,
                                      @RequestBody RfiInspectionSaveDto processDto);
}
