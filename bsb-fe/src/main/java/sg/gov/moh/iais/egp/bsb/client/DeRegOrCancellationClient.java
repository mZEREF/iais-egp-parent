package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.deregorcancellation.CancellationApprovalDto;
import sg.gov.moh.iais.egp.bsb.dto.deregorcancellation.DeRegistrationAFCDto;
import sg.gov.moh.iais.egp.bsb.dto.deregorcancellation.DeRegistrationFacilityDto;

@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "deRegOrCancellation")
public interface DeRegOrCancellationClient {
    /**************************DeRegistrationFacility**********************************/

    @PostMapping(path = "/deRegOrCancellation/deRegistrationFacility/validate/deRegistrationFacilityDto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDeRegistrationFacilityDto(@RequestBody DeRegistrationFacilityDto deRegistrationFacilityDto);

    @GetMapping(path = "/deRegOrCancellation/deRegistrationFacility/getData/{approvalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DeRegistrationFacilityDto> getDeRegistrationFacilityData(@PathVariable("approvalId") String approvalId);

    @PostMapping(path = "/deRegOrCancellation/deRegistrationFacility/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveNewDeRegistrationFacilityDraft(@RequestBody DeRegistrationFacilityDto dto);

    @GetMapping(path = "/deRegOrCancellation/deRegistrationFacility/getDraftData/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DeRegistrationFacilityDto> getDraftDeRegistrationFacilityData(@PathVariable("applicationId") String applicationId);

    @PostMapping(path = "/deRegOrCancellation/deRegistrationFacility/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveDeRegistrationFacilityDto(@RequestBody DeRegistrationFacilityDto dto);

    /**************************CancellationApproval**********************************/

    @PostMapping(path = "/deRegOrCancellation/cancellationApproval/validate/cancellationApprovalDto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateCancellationApprovalDto(@RequestBody CancellationApprovalDto cancellationApprovalDto);

    @GetMapping(path = "/deRegOrCancellation/cancellationApproval/getData/{approvalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<CancellationApprovalDto> getCancellationApprovalData(@PathVariable("approvalId") String approvalId);

    @PostMapping(path = "/deRegOrCancellation/cancellationApproval/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveNewCancellationApprovalDraft(@RequestBody CancellationApprovalDto dto);

    @GetMapping(path = "/deRegOrCancellation/cancellationApproval/getDraftData/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<CancellationApprovalDto> getDraftCancellationApprovalData(@PathVariable("applicationId") String applicationId);

    @PostMapping(path = "/deRegOrCancellation/cancellationApproval/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveCancellationApprovalDto(@RequestBody CancellationApprovalDto dto);

    /**************************DeRegistrationAFC**********************************/

    @PostMapping(path = "/deRegOrCancellation/deRegistrationAFC/validate/deRegistrationAFCDto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDeRegistrationAFCDto(@RequestBody DeRegistrationAFCDto deRegistrationAFCDto);

    @GetMapping(path = "/deRegOrCancellation/deRegistrationAFC/getData/{approvalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DeRegistrationAFCDto> getDeRegistrationAFCData(@PathVariable("approvalId") String approvalId);

    @PostMapping(path = "/deRegOrCancellation/deRegistrationAFC/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveNewDeRegistrationAFCDraft(@RequestBody DeRegistrationAFCDto dto);

    @GetMapping(path = "/deRegOrCancellation/deRegistrationAFC/getDraftData/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DeRegistrationAFCDto> getDraftDeRegistrationAFCData(@PathVariable("applicationId") String applicationId);

    @PostMapping(path = "/deRegOrCancellation/deRegistrationAFC/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveDeRegistrationAFCDto(@RequestBody DeRegistrationAFCDto dto);
}
