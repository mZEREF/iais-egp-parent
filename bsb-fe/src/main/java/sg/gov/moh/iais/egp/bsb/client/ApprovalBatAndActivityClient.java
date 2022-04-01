package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.info.facility.FacilityBasicInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.*;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;

import java.util.List;

/**
 * @author : LiRan
 * @date : 2022/3/17
 */
@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "approvalBatAndActivity")
public interface ApprovalBatAndActivityClient {
    //TODO: update url, and complete this method
    @GetMapping(value = "/facility-info/approved-facility/basic", produces = MediaType.APPLICATION_JSON_VALUE)
    List<FacilityBasicInfo> getAllMainActivityApprovedFacility();

    @GetMapping(path = "/register/bat-approval/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalBatAndActivityDto> getApprovalAppAppDataByApplicationId(@PathVariable("appId") String appId);

    @PostMapping(path = "/register/bat-and-activity-approval/form-validation/approval-selection", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateApprovalSelectionDto(@RequestBody ApprovalSelectionDto dto);

    @PostMapping(path = "/register/bat-and-activity-approval/form-validation/approval-to-activity", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateApprovalToActivityDto(@RequestBody ApprovalToActivityDto dto);

    @PostMapping(path = "/register/bat-and-activity-approval/form-validation/primary-docs/possess-bat", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateProcessBatPrimaryDocs(@RequestBody PrimaryDocDto.DocsMetaDto dto);

    @PostMapping(path = "/register/bat-and-activity-approval/form-validation/primary-docs/large-bat", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateLargeBatPrimaryDocs(@RequestBody PrimaryDocDto.DocsMetaDto dto);

    @PostMapping(path = "/register/bat-and-activity-approval/form-validation/primary-docs/special-bat", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateSpecialBatPrimaryDocs(@RequestBody PrimaryDocDto.DocsMetaDto dto);

    @PostMapping(path = "/register/bat-and-activity-approval/form-validation/primary-docs/facility-activity", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityActivityPrimaryDocs(@RequestBody PrimaryDocDto.DocsMetaDto dto);

    @GetMapping(path = "/register/bat-and-activity-approval/facProfileDto/{facilityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacProfileDto> getFacProfileDtoByFacilityId(@PathVariable("facilityId") String facilityId);

    @GetMapping(path = "/register/bat-and-activity-approval/not-approval-activities/{facilityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<String> getNotApprovalActivities(@PathVariable("facilityId") String facilityId);
}
