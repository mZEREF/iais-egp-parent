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
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.ReviewAFCReportDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;

import java.util.List;
import java.util.Map;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class)
public interface InspectionAFCClient {
    @GetMapping(value = "/certification/afc/init-data", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ReviewAFCReportDto> getReviewAFCReportDto(@RequestParam("appId") String appId);

    @PostMapping(value = "/certification/afc/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAFCReportDto(@RequestBody ReviewAFCReportDto dto);

    @PostMapping(value = "/certification/afc", produces = MediaType.APPLICATION_JSON_VALUE)
    void saveAFCAdminInsAFCData(@RequestBody AFCSaveDto dto);

    @GetMapping(value = "/certification/fileNameFileDataMap/insCerFacRelId", produces = MediaType.APPLICATION_JSON_VALUE)
    List<Map<String,byte[]>> getCerFileNameFileDataMapByInsCerFacRelId(@RequestParam("insCerFacRelId") String insCerFacRelId);

    @GetMapping(value = "/certification/report/certificationAppId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ReviewAFCReportDto> getCertificationReportByCertificationAppId(@RequestParam("appId") String appId);

    @GetMapping(value = "/certification/report/mainAppId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ReviewAFCReportDto> getCertificationReportByMainAppId(@RequestParam("appId") String appId);

    @GetMapping(value = "/certification/judge/ins/cer", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String,Boolean> judgeWhetherHaveInspectionAndCertificationByInsCerFacRelId(@RequestParam("insCerFacRelId") String insCerFacRelId);
}
