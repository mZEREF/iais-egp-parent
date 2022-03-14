package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;

import sg.gov.moh.iais.egp.bsb.dto.register.afc.*;
import sg.gov.moh.iais.egp.bsb.dto.renewal.FacilityCertifierRegistrationReviewDto;


@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "cerReg")
public interface FacCertifierRegisterClient {
    @PostMapping(path = "/register/facility-certifier/form-validation/org-profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateOrganisationProfile(@RequestBody CompanyProfileDto dto);

    @PostMapping(path = "/register/facility-certifier/form-validation/certifying-team", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateCertifierTeam(@RequestBody CertifyingTeamDto dto);

    @PostMapping(path = "/register/facility-certifier/form-validation/admin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityAdmin(@RequestBody AdministratorDto dto);

    @PostMapping(path = "/register/facility-certifier/form-validation/docs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateCerPrimaryDocs(@RequestBody PrimaryDocDto.DocsMetaDto dto);

    @PostMapping(path = "/register/facility-certifier/form-validation/preview-submit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFaCerPreviewSubmit(@RequestBody PreviewSubmitDto dto);

    @PostMapping(path = "/register/facility-certifier/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveFacCertifierDraft(@RequestBody FacilityCertifierRegisterDto dto);

    @PostMapping(path = "/register/facility-certifier", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewRegisteredFacCertifier(@RequestBody FacilityCertifierRegisterDto dto);

    @GetMapping(path = "/register/facility-certifier/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityCertifierRegisterDto> getCertifierRegistrationAppData(@PathVariable("appId") String appId);

    /*******************RFC********************/
    @PostMapping(path = "/register/facility-certifier/rfc", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveAmendmentFacCertifier(@RequestBody FacilityCertifierRegisterDto dto);

    @GetMapping(path = "/register/facility-certifier/rfc/{approvalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityCertifierRegisterDto> getCertifierRegistrationAppDataByApprovalId(@PathVariable("approvalId") String approvalId);

    /*******************RENEWAL********************/
    @GetMapping(path = "/register/facility-certifier/renewal/{approvalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityCertifierRegisterDto> getRenewalFacCertifierRegisterAppByApprovalId(@PathVariable("approvalId") String approvalId);

    @PostMapping(path = "/register/facility-certifier/renewal/form-validation/review", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateRenewalFacCerRegReview(@RequestBody FacilityCertifierRegistrationReviewDto dto);

    @PostMapping(path = "/register/facility-certifier/renewal", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveRenewalRegisteredFacCertifier(@RequestBody FacilityCertifierRegisterDto dto);
}
