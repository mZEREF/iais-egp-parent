package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuthoriserDto;
import sg.gov.moh.iais.egp.bsb.dto.info.bat.RichScheduleBatInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.facility.FacilityBasicInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalBatAndActivityDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalSelectionDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToActivityDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToLargeDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToPossessDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToSpecialDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.FacAuthorisedDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.FacProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.PreviewDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;

import java.util.List;
import java.util.Map;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class, contextId = "approvalBatAndActivity")
public interface ApprovalBatAndActivityClient {
    @GetMapping(value = "/facility-info/approved-facility/basic", produces = MediaType.APPLICATION_JSON_VALUE)
    List<FacilityBasicInfo> getApprovedFacility();

    @GetMapping(path = "/register/bat-and-activity-approval/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalBatAndActivityDto> getApprovalAppAppDataByApplicationId(@PathVariable("appId") String appId);

    @GetMapping(path = "/register/bat-and-activity-approval/approvalId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalBatAndActivityDto> retrieveApprovalAppAppDataByApprovalId(@RequestParam("approvalId") String approvalId);

    @GetMapping(path = "/bat-info/dropdown/schedule-bat/facId", produces = MediaType.APPLICATION_JSON_VALUE)
    RichScheduleBatInfo queryScheduleBasedBatBasicInfo(@RequestParam("facId") String facId,
                                                       @RequestParam("protectedPlace") boolean protectedPlace,
                                                       @RequestParam("approvalType")String approvalType);

    @PostMapping(path = "/register/bat-and-activity-approval/form-validation/approval-selection", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateApprovalSelectionDto(@RequestBody ApprovalSelectionDto dto);

    @PostMapping(path = "/register/bat-and-activity-approval/form-validation/approval-to-activity", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateApprovalToActivityDto(@RequestBody ApprovalToActivityDto dto);

    @PostMapping(path = "/register/bat-and-activity-approval/form-validation/bat/approval-to-special", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateApprovalToSpecialBatDto(@RequestBody ApprovalToSpecialDto dto, @RequestParam("profile") String profile);

    @PostMapping(path = "/register/bat-and-activity-approval/form-validation/bat/approval-to-large", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateApprovalToLargeBatDto(@RequestBody ApprovalToLargeDto dto, @RequestParam("profile") String profile);

    @PostMapping(path = "/register/bat-and-activity-approval/form-validation/bat/approval-to-possess", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateApprovalToPossessBatDto(@RequestBody ApprovalToPossessDto dto, @RequestParam("profile") String profile);

    @PostMapping(path = "/register/bat-and-activity-approval/form-validation/facility-authoriser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityAuthoriser(@RequestBody FacAuthorisedDto facAuthorisedDto);

    @PostMapping(path = "/register/bat-and-activity-approval/form-validation/preview-dto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePreviewDto(@RequestBody PreviewDto previewDto);

    @PostMapping(path = "/register/bat-and-activity-approval/form-validation/primary-docs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePrimaryDocs(@RequestBody PrimaryDocDto.DocsMetaDto dto, @RequestParam("processType") String processType);

    @GetMapping(path = "/register/bat-and-activity-approval/facProfileDto/{facilityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacProfileDto> getFacProfileDtoByFacilityId(@PathVariable("facilityId") String facilityId);

    @GetMapping(path = "/register/bat-and-activity-approval/not-approval-activities/{facilityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String,String> getNotApprovalActivities(@PathVariable("facilityId") String facilityId);

    @PostMapping(path = "/register/bat-and-activity-approval", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppMainInfo> saveNewApplicationToApproval(@RequestBody ApprovalBatAndActivityDto dto);

    @PostMapping(path = "/register/bat-and-activity-approval/rfc", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppMainInfo> saveRfcApplicationToApproval(@RequestBody ApprovalBatAndActivityDto approvalBatAndActivityDto, @RequestParam("needTask") Boolean needTask);

    @PostMapping(path = "/register/bat-and-activity-approval/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveNewFacilityDraft(@RequestBody ApprovalBatAndActivityDto dto);

    @GetMapping(path = "/facility-auth/selection/{facilityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<FacilityAuthoriserDto> getApprovalSelectAuthorisedPersonnelByFacId(@PathVariable("facilityId") String facilityId);

    @GetMapping(path = "/register/bat-and-activity-approval/draft/same-facility-processType", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalBatAndActivityDto> getSameFacilityAndProcessTypeDraftData(@RequestParam("facId") String facId, @RequestParam("processType") String processType, @RequestParam("appType") String appType);

    @GetMapping(path = "/facility-auth/selection/auth-person-ids", produces = MediaType.APPLICATION_JSON_VALUE)
    List<FacilityAuthoriserDto> getAuthorisedPersonnelByAuthIds(@RequestBody List<String> authPersonIds);

}
