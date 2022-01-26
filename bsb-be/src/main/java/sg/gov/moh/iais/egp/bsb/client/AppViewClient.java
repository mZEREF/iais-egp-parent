package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.afc.FacilityCertifierRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.approval.ApprovalAppDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.deregorcancellation.CancellationApprovalDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.deregorcancellation.DeRegistrationAFCDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.deregorcancellation.DeRegistrationFacilityDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.facility.FacilityRegisterDto;

/**
 * @author : LiRan
 * @date : 2021/12/27
 */
@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface AppViewClient {
    @GetMapping(path = "/applicationView/facilityRegisterDto/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityRegisterDto> getFacRegDtoByAppId(@PathVariable("applicationId") String applicationId);

    @GetMapping(path = "/applicationView/approvalAppDto/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalAppDto> getApprovalAppDtoByAppId(@PathVariable("applicationId") String applicationId);

    @GetMapping(path = "/applicationView/facilityCertifierRegisterDto/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityCertifierRegisterDto> getFacCerRegDtoByAppId(@PathVariable("applicationId") String applicationId);

    @GetMapping(path = "/applicationView/deRegistrationFacilityDto/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DeRegistrationFacilityDto> getDeRegistrationFacilityDtoByAppId(@PathVariable("applicationId") String applicationId);

    @GetMapping(path = "/applicationView/cancellationApprovalDto/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<CancellationApprovalDto> getCancellationApprovalDtoByAppId(@PathVariable("applicationId") String applicationId);

    @GetMapping(path = "/applicationView/deRegistrationAFCDto/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DeRegistrationAFCDto> getDeRegistrationAFCDtoByAppId(@PathVariable("applicationId") String applicationId);
}
