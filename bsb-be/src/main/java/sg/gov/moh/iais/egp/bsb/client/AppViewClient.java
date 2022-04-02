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
import sg.gov.moh.iais.egp.bsb.dto.appview.facility.DeclarationItemMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.appview.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.inspection.RectifyFindingFormDto;
import sg.gov.moh.iais.egp.bsb.dto.datasubmission.DataSubmissionInfo;

import java.util.List;


@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface AppViewClient {
    @GetMapping(path = "/app-view/register/facility/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityRegisterDto> getFacRegDtoByAppId(@PathVariable("appId") String applicationId);

    @GetMapping(value = "/declaration/config/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<DeclarationItemMainInfo> getDeclarationConfigInfoById(@PathVariable("id") String id);

    @GetMapping(path = "/app-view/register/bat-approval/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalAppDto> getApprovalAppDtoByAppId(@PathVariable("appId") String applicationId);

    @GetMapping(path = "/app-view/register/facility-certifier/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityCertifierRegisterDto> getFacCerRegDtoByAppId(@PathVariable("appId") String applicationId);

    @GetMapping(path = "/app-view/deregister-cancel/facility/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DeRegistrationFacilityDto> getDeRegistrationFacilityDtoByAppId(@PathVariable("appId") String applicationId);

    @GetMapping(path = "/app-view/deregister-cancel/bat-approval/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<CancellationApprovalDto> getCancellationApprovalDtoByAppId(@PathVariable("appId") String applicationId);

    @GetMapping(path = "/app-view/deregister-cancel/facility-certifier/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DeRegistrationAFCDto> getDeRegistrationAFCDtoByAppId(@PathVariable("appId") String applicationId);

    @GetMapping(path = "/app-view/data-submission-info/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DataSubmissionInfo> getDataSubmissionInfo(@PathVariable("appId") String applicationId);

    @GetMapping(path = "/app-view/inspection/follow-up-items/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<RectifyFindingFormDto> getFollowUpItemsFindingFormDtoByAppId(@PathVariable("appId") String applicationId);
}
