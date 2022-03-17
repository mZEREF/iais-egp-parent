package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.approval.*;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.bat.BatBasicInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.facility.FacilityActivityBasicInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.facility.FacilityBasicInfo;
import java.util.Collection;
import java.util.List;


@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "approvalApp")
public interface ApprovalAppClient {

    @PostMapping(path = "/register/bat-approval/form-validation/activity", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateActivity(@RequestBody ActivityDto dto);

    @PostMapping(path = "/register/bat-approval/form-validation/profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateApprovalProfile(@RequestBody ApprovalProfileDto dto);

    @PostMapping(path = "/register/bat-approval/form-validation/primary-docs", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateApprovalPrimaryDocs(@RequestBody PrimaryDocDto.DocsMetaDto dto);

    @GetMapping(path = "/register/bat-approval/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalAppDto> getApprovalAppAppDataByApplicationId(@PathVariable("appId") String appId);

    @GetMapping(path = "/facility-info/doc/{facilityId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<Collection<DocRecordInfo>> getFacDocByFacId(@PathVariable("facilityId") String facilityId);

    @GetMapping(value = "/facility-info/approved-facility/basic", produces = MediaType.APPLICATION_JSON_VALUE)
    List<FacilityBasicInfo> getAllMainActivityApprovedFacility();

    @GetMapping(path = "/facility-info/approved-activities/basic/{facilityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<FacilityActivityBasicInfo> getApprovalFAByFacId(@PathVariable("facilityId") String facilityId);

    @GetMapping(path = "/bat-info/basic", produces = MediaType.APPLICATION_JSON_VALUE)
    List<BatBasicInfo> getBiologicalBySchedule(@RequestParam("schedule") String schedule);

    @PostMapping(path = "/register/bat-approval", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewApprovalApp(@RequestBody ApprovalAppDto approvalAppDto);

    @PostMapping(path = "/register/bat-approval/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveApprovalAppDraft(@RequestBody ApprovalAppDto dto);
    /*******************RFC********************/
    @GetMapping(path = "/register/bat-approval/rfc/{approvalId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalAppDto> getApprovalAppAppDataByApprovalId(@PathVariable("approvalId") String approvalId);

    @PostMapping(path = "/register/bat-approval/rfc", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveAmendmentApprovalApp(@RequestBody ApprovalAppDto approvalAppDto);
}
