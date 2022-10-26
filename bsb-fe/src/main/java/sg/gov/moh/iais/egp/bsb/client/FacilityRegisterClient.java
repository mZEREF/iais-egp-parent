package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationAnswerDto;
import sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationConfigInfo;
import sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.entity.SampleFileDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.info.bat.RichQueryScheduleParam;
import sg.gov.moh.iais.egp.bsb.dto.info.bat.RichScheduleBatInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAdminAndOfficerDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAfcDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserFileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeFileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityOperatorDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileValidateDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilitySelectionDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PreviewSubmitDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.renewal.FacilityRegistrationReviewDto;
import sg.gov.moh.iais.egp.bsb.dto.renewal.defer.DeferRenewDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.FileDataValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;

import java.util.List;
import java.util.Set;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class, contextId = "facReg")
public interface FacilityRegisterClient {
    @GetMapping(value = "/declaration/config/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<DeclarationItemMainInfo> getDeclarationConfigInfoById(@PathVariable("id") String id);

    @GetMapping(value = "/declaration/config", produces = MediaType.APPLICATION_JSON_VALUE)
    DeclarationConfigInfo getDeclarationConfigBySpecificType(@RequestParam("type") String type,
                                                             @RequestParam("sub-type") String subType);

    @GetMapping(value = "/sample-file", produces = MediaType.APPLICATION_JSON_VALUE)
    SampleFileDto retrieveSampleFileByType(@RequestParam(value = "type") String type);

    @GetMapping(value = "/sample-file", produces = MediaType.APPLICATION_JSON_VALUE)
    SampleFileDto retrieveSampleFileByTypeAndVersion(@RequestParam(value = "type") String type,
                                                     @RequestParam(value = "version") Integer version);

    @PostMapping(path = "/register/facility/form-validation/selection", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilitySelection(@RequestBody FacilitySelectionDto dto);

    @PostMapping(path = "/register/facility/form-validation/profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityProfile(@RequestBody FacilityProfileValidateDto dto);

    @PostMapping(path = "/register/facility/form-validation/operator", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityOperator(@RequestBody FacilityOperatorDto dto);

    @PostMapping(path = "/register/facility/form-validation/admin-officer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityAdmin(@RequestBody FacilityAdminAndOfficerDto dto);

    @PostMapping(path = "/register/facility/form-validation/approved-facility-certifier", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityAfc(@RequestBody FacilityAfcDto dto);

    @PostMapping(path = "/register/facility/form-validation/committee", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FileDataValidationResultDto<FacilityCommitteeFileDto> validateFacilityCommittee(@RequestBody FacilityCommitteeDto dto);

    @PostMapping(path = "/register/facility/form-validation/authoriser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FileDataValidationResultDto<FacilityAuthoriserFileDto> validateFacilityAuthoriser(@RequestBody FacilityAuthoriserDto dto);

    @PostMapping(path = "/register/facility/validation/data-file", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateUploadedDataFileMeta(@RequestBody DocMeta meta);

    @GetMapping(path = "/bat-info/dropdown/schedule-bat/activity", produces = MediaType.APPLICATION_JSON_VALUE)
    RichScheduleBatInfo queryScheduleBasedBatBasicInfo(@SpringQueryMap RichQueryScheduleParam param, @RequestParam(value = "approvalTypes") List<String> approvalTypes);

    @PostMapping(path = "/register/facility/form-validation/bat", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityBiologicalAgentToxin(@RequestBody BiologicalAgentToxinDto dto, @RequestParam("profile") String profile);

    @PostMapping(path = "/register/facility/form-validation/other-app-info", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityOtherAppInfo(@RequestBody DeclarationAnswerDto dto);

    @PostMapping(path = "/register/facility/form-validation/primary-docs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityPrimaryDocs(@RequestBody PrimaryDocDto.DocsMetaDto dto);

    @PostMapping(path = "/register/facility/form-validation/preview-submit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityPreviewSubmit(@RequestBody PreviewSubmitDto dto);

    @PostMapping(path = "/register/facility/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveNewFacilityDraft(@RequestBody FacilityRegisterDto dto);

    @PostMapping(path = "/register/facility", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppMainInfo> saveNewRegisteredFacility(@RequestBody FacilityRegisterDto dto);

    @GetMapping(path = "/register/facility/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityRegisterDto> getFacilityRegistrationAppDataByApplicationId(@PathVariable("appId") String appId);

    @GetMapping(path = "/register/facility/draft/same-classification", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityRegisterDto> getSameClassificationDraftData(@RequestParam("facClassification") String facClassification);

    /*******************RFC********************/
    @GetMapping(path = "/register/facility/rfc/{facId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityRegisterDto> getRfcFacilityRegistrationAppDataByFacilityId(@PathVariable("facId") String facId);

    @PostMapping(path = "/register/facility/rfc", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppMainInfo> saveAmendmentFacility(@RequestBody FacilityRegisterDto dto, @RequestParam("needTask") Boolean needTask, @RequestParam("needScreen") Boolean needScreen);

    @GetMapping(path = "/register/facility/draft/facId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityRegisterDto> getRfcDraftData(@RequestParam("facId") String facId);

    /*******************RENEWAL********************/
    @GetMapping(path = "/register/facility/renewal/{facId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityRegisterDto> getFacilityRegistrationAppDataByFacilityId(@PathVariable("facId") String facId);

    @PostMapping(path = "/register/facility/renewal", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppMainInfo> saveRenewalRegisteredFacility(@RequestBody FacilityRegisterDto dto);

    @GetMapping(path = "/register/facility/update-renewal", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    void updateFacilityRenewable(@RequestParam("facId") String facId, @RequestParam("renewable") Boolean renewable);

    @GetMapping(path = "/register/facility/renewal/{approvalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityRegisterDto> getRenewalFacRegAppDataByApprovalId(@PathVariable("approvalId") String approvalId);

    @PostMapping(path = "/register/facility/renewal/form-validation/review", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateRenewalFacilityReview(@RequestBody FacilityRegistrationReviewDto dto);

    @GetMapping(value = "/certification/last-two-round/afc", produces = MediaType.APPLICATION_JSON_VALUE)
    Set<String> getLast2CertificationAfc(@RequestParam("facilityId") String facilityId);

    @GetMapping(value = "/certification/last-two-round/afc/application-id", produces = MediaType.APPLICATION_JSON_VALUE)
    Set<String> getLast2CertificationAfcByApplicationId(@RequestParam("appId") String appId);


    /**************Certification select AFC*****************/
    @GetMapping(path = "/certification/afc/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityAfcDto> getFacilityAfcDtoByApplicationId(@PathVariable("appId") String appId);

    @PostMapping(path = "/certification/new/facility-afc", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppMainInfo> saveNewCertFacilityAfc(@RequestBody FacilityAfcDto dto);

    @PostMapping(path = "/certification/facility-afc/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    Void saveNewCertFacilityAfcDraft(@RequestBody FacilityAfcDto dto);

    /************** Defer Renew *****************/
    @GetMapping(path = "/register/facility/renewal/defer/facId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DeferRenewDto> getDeferViewByFacilityId(@RequestParam("facId") String facilityId);

    @GetMapping(path = "/register/facility/renewal/defer/appId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DeferRenewDto> getDeferViewByApplicationId(@RequestParam("appId") String applicationId);

    @PostMapping(path = "/register/facility/renewal/defer/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDeferRenew(@RequestBody DeferRenewDto deferRenewDto);

    @PostMapping(path = "register/facility/renewal/defer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AppMainInfo> saveDeferRenewData(@RequestBody DeferRenewDto dto);
}
