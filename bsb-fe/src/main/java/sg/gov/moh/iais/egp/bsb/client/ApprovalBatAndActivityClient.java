package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.info.bat.BatBasicInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.facility.FacilityBasicInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.*;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;

import java.util.List;
import java.util.Map;


@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "approvalBatAndActivity")
public interface ApprovalBatAndActivityClient {
    @GetMapping(value = "/facility-info/approved-facility/basic", produces = MediaType.APPLICATION_JSON_VALUE)
    List<FacilityBasicInfo> getApprovedFacility();

    @GetMapping(path = "/register/bat-approval/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalBatAndActivityDto> getApprovalAppAppDataByApplicationId(@PathVariable("appId") String appId);

    @GetMapping(path = "/bat-info/dropdown/schedule-bat/activity", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, List<BatBasicInfo>> queryScheduleBasedBatBasicInfo(@RequestParam("activity") String activity);

    @PostMapping(path = "/register/bat-and-activity-approval/form-validation/approval-selection", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateApprovalSelectionDto(@RequestBody ApprovalSelectionDto dto);

    @PostMapping(path = "/register/bat-and-activity-approval/form-validation/approval-to-activity", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateApprovalToActivityDto(@RequestBody ApprovalToActivityDto dto);

    @PostMapping(path = "/register/bat-and-activity-approval/form-validation/primary-docs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePrimaryDocs(@RequestBody PrimaryDocDto.DocsMetaDto dto, @RequestParam("processType") String processType);

    @GetMapping(path = "/register/bat-and-activity-approval/facProfileDto/{facilityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacProfileDto> getFacProfileDtoByFacilityId(@PathVariable("facilityId") String facilityId);

    @GetMapping(path = "/register/bat-and-activity-approval/not-approval-activities/{facilityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<String> getNotApprovalActivities(@PathVariable("facilityId") String facilityId);
}
