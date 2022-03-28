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
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.*;
import sg.gov.moh.iais.egp.bsb.dto.renewal.FacilityRegistrationReviewDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.FileDataValidationResultDto;


@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "facReg")
public interface FacilityRegisterClient {
    @PostMapping(path = "/register/facility/form-validation/selection", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilitySelection(@RequestBody FacilitySelectionDto dto);

    @PostMapping(path = "/register/facility/form-validation/profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityProfile(@RequestBody FacilityProfileDto.FacilityProfileValidateDto dto);

    @PostMapping(path = "/register/facility/form-validation/operator", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityOperator(@RequestBody FacilityOperatorDto dto);

    @PostMapping(path = "/register/facility/form-validation/admin-officer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityAdmin(@RequestBody FacilityAdminAndOfficerDto dto);

    @PostMapping(path = "/register/facility/form-validation/approved-facility-certifier", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateApprovedFacilityCertifier(@RequestBody ApprovedFacilityCertifierDto dto);

    @PostMapping(path = "/register/facility/form-validation/committee", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FileDataValidationResultDto<FacilityCommitteeFileDto> validateFacilityCommittee(@RequestBody FacilityCommitteeDto dto);

    @PostMapping(path = "/register/facility/form-validation/authoriser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FileDataValidationResultDto<FacilityAuthoriserFileDto> validateFacilityAuthoriser(@RequestBody FacilityAuthoriserDto dto);

    @PostMapping(path = "/register/facility/validation/data-file", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateUploadedDataFileMeta(@RequestBody DocMeta meta);

    @PostMapping(path = "/register/facility/form-validation/bat", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityBiologicalAgentToxin(@RequestBody BiologicalAgentToxinDto dto);

    @PostMapping(path = "/register/facility/form-validation/other-app-info", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityOtherAppInfo(@RequestBody OtherApplicationInfoDto dto);

    @PostMapping(path = "/register/facility/form-validation/primary-docs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityPrimaryDocs(@RequestBody PrimaryDocDto.DocsMetaDto dto);

    @PostMapping(path = "/register/facility/form-validation/preview-submit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityPreviewSubmit(@RequestBody PreviewSubmitDto dto);

    @PostMapping(path = "/register/facility/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveNewFacilityDraft(@RequestBody FacilityRegisterDto dto);

    @PostMapping(path = "/register/facility", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewRegisteredFacility(@RequestBody FacilityRegisterDto dto);

    @GetMapping(path = "/register/facility/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityRegisterDto> getFacilityRegistrationAppDataByApplicationId(@PathVariable("appId") String appId);

    /*******************RFC********************/
    @GetMapping(path = "/register/facility/rfc/{approvalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityRegisterDto> getFacilityRegistrationAppDataByApprovalId(@PathVariable("approvalId") String approvalId);

    @PostMapping(path = "/register/facility/rfc", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveAmendmentFacility(@RequestBody FacilityRegisterDto dto);

    /*******************RENEWAL********************/
    @GetMapping(path = "/register/facility/renewal/{approvalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityRegisterDto> getRenewalFacRegAppDataByApprovalId(@PathVariable("approvalId") String approvalId);

    @PostMapping(path = "/register/facility/renewal/form-validation/review", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateRenewalFacilityReview(@RequestBody FacilityRegistrationReviewDto dto);

    @PostMapping(path = "/register/facility/renewal", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveRenewalRegisteredFacility(@RequestBody FacilityRegisterDto dto);
}
