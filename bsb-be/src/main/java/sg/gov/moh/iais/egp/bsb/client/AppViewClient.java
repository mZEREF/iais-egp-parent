package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.afc.FacilityCertifierRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.deferrenew.DeferRenewViewDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.deregorcancellation.CancellationApprovalDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.deregorcancellation.DeRegistrationAFCDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.deregorcancellation.DeRegistrationFacilityDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.inspection.RectifyFindingFormDto;
import sg.gov.moh.iais.egp.bsb.dto.datasubmission.DataSubmissionInfo;
import sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuthoriserDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalBatAndActivityDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.ViewWithdrawnDto;

import java.util.List;


@FeignClient(name = "bsb-api", configuration = FeignConfiguration.class)
public interface AppViewClient {
    //Facility
    @GetMapping(path = "/app-view/register/facility/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityRegisterDto> getFacRegDtoByAppId(@PathVariable("appId") String applicationId);

    @GetMapping(path = "/app-view/facility/{facId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityRegisterDto> getFacRegDtoByFacId(@PathVariable("facId") String facId);

    @GetMapping(path = "/app-view/rfc/facility/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityRegisterDto> getRfcOldFacRegDtoByAppId(@PathVariable("appId") String applicationId);

    @GetMapping(path = "/app-view/rfi-old-data/facility", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityRegisterDto> getRfiOldFacilityRegistrationData(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId);

    //Bat and Activity
    @GetMapping(path = "/app-view/register/bat-and-activity/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalBatAndActivityDto> getApprovalBatAndActivityDtoByAppId(@PathVariable("appId") String applicationId);

    @GetMapping(path = "/app-view/rfi-old-data/bat-and-activity", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalBatAndActivityDto> getRfiOldApprovalBatAndActivityData(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId);

    @GetMapping(path = "/app-view/rfc/bat-and-activity/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalBatAndActivityDto> retrieveRfcOldApprovalAppAppDataByApplicationId(@PathVariable("appId") String applicationId);

    // AFC
    @GetMapping(path = "/app-view/register/facility-certifier/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityCertifierRegisterDto> getFacCerRegDtoByAppId(@PathVariable("appId") String applicationId);

    // Cancellation/Deregistration
    @GetMapping(path = "/app-view/deregister-cancel/facility/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DeRegistrationFacilityDto> getDeRegistrationFacilityDtoByAppId(@PathVariable("appId") String applicationId);

    @GetMapping(path = "/app-view/deregister-cancel/bat-approval/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<CancellationApprovalDto> getCancellationApprovalDtoByAppId(@PathVariable("appId") String applicationId);

    @GetMapping(path = "/app-view/deregister-cancel/facility-certifier/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DeRegistrationAFCDto> getDeRegistrationAFCDtoByAppId(@PathVariable("appId") String applicationId);

    // DataSubmission
    @GetMapping(path = "/app-view/data-submission-info/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DataSubmissionInfo> getDataSubmissionInfo(@PathVariable("appId") String applicationId);

    @GetMapping(path = "/app-view/inspection/follow-up-items/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<RectifyFindingFormDto> getFollowUpItemsFindingFormDtoByAppId(@PathVariable("appId") String applicationId);

    @GetMapping(path = "/app-view/withdrawal/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ViewWithdrawnDto> getApplicantSubmitWithdrawDataByAppId(@PathVariable("appId") String applicationId);

    @GetMapping(path = "/app-view/has-completed-rfi")
    boolean hasCompletedRfi(@RequestParam("appId") String appId, @RequestParam("taskId") String taskId);

    @GetMapping(path = "/facility-auth-be/selection/auth-person-ids", produces = MediaType.APPLICATION_JSON_VALUE)
    List<FacilityAuthoriserDto> getAuthorisedPersonnelByAuthIds(@RequestBody List<String> authIdList);

    @GetMapping(value = "/declaration/config/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<DeclarationItemMainInfo> getDeclarationConfigInfoById(@PathVariable("id") String id);

    @GetMapping(path = "/app-view/defer-renew/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DeferRenewViewDto> getDeferRenewDataByAppId(@PathVariable("appId") String applicationId);
}
