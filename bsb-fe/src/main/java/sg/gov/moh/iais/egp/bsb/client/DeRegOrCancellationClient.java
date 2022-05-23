package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.deregorcancellation.CancellationApprovalDto;
import sg.gov.moh.iais.egp.bsb.dto.deregorcancellation.DeRegistrationAFCDto;
import sg.gov.moh.iais.egp.bsb.dto.deregorcancellation.DeRegistrationFacilityDto;

@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class, contextId = "deRegOrCancellation")
public interface DeRegOrCancellationClient {
    /**************************DeRegistrationFacility**********************************/

    @PostMapping(path = "/deregister-cancel/facility/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDeRegistrationFacilityDto(@RequestBody DeRegistrationFacilityDto deRegistrationFacilityDto);

    @GetMapping(path = "/deregister-cancel/facility/{approvalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DeRegistrationFacilityDto> getDeRegistrationFacilityData(@PathVariable("approvalId") String approvalId);

    @PostMapping(path = "/deregister-cancel/facility/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveNewDeRegistrationFacilityDraft(@RequestBody DeRegistrationFacilityDto dto);

    @GetMapping(path = "/deregister-cancel/facility/draft/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DeRegistrationFacilityDto> getDraftDeRegistrationFacilityData(@PathVariable("appId") String applicationId);

    @PostMapping(path = "/deregister-cancel/facility", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveDeRegistrationFacilityDto(@RequestBody DeRegistrationFacilityDto dto);

    /**************************CancellationApproval**********************************/

    @PostMapping(path = "/deregister-cancel/bat-approval/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateCancellationApprovalDto(@RequestBody CancellationApprovalDto cancellationApprovalDto);

    @GetMapping(path = "/deregister-cancel/bat-approval/{approvalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<CancellationApprovalDto> getCancellationApprovalData(@PathVariable("approvalId") String approvalId);

    @PostMapping(path = "/deregister-cancel/bat-approval/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveNewCancellationApprovalDraft(@RequestBody CancellationApprovalDto dto);

    @GetMapping(path = "/deregister-cancel/bat-approval/draft/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<CancellationApprovalDto> getDraftCancellationApprovalData(@PathVariable("appId") String applicationId);

    @PostMapping(path = "/deregister-cancel/bat-approval", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveCancellationApprovalDto(@RequestBody CancellationApprovalDto dto);

    /**************************DeRegistrationAFC**********************************/

    @PostMapping(path = "/deregister-cancel/afc/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDeRegistrationAFCDto(@RequestBody DeRegistrationAFCDto deRegistrationAFCDto);

    @GetMapping(path = "/deregister-cancel/afc/{approvalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DeRegistrationAFCDto> getDeRegistrationAFCData(@PathVariable("approvalId") String approvalId);

    @PostMapping(path = "/deregister-cancel/afc/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveNewDeRegistrationAFCDraft(@RequestBody DeRegistrationAFCDto dto);

    @GetMapping(path = "/deregister-cancel/afc/draft/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DeRegistrationAFCDto> getDraftDeRegistrationAFCData(@PathVariable("appId") String applicationId);

    @PostMapping(path = "/deregister-cancel/afc", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveDeRegistrationAFCDto(@RequestBody DeRegistrationAFCDto dto);
}
