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


@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "cerReg")
public interface FacCertifierRegisterClient {
    @PostMapping(path = "/register/faCer/validate/orgProfile", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateOrganisationProfile(@RequestBody OrganisationProfileDto dto);

    @PostMapping(path = "/register/faCer/validate/cerTeam", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateCertifierTeam(@RequestBody CertifyingTeamDto dto);

    @PostMapping(path = "/register/faCer/validate/facAdmin", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityAdmin(@RequestBody AdministratorDto dto);

    @PostMapping(path = "/register/faCer/validate/primaryDocs", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateCerPrimaryDocs(@RequestBody PrimaryDocDto dto);

    @PostMapping(path = "/register/faCer/validate/previewSubmit", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFaCerPreviewSubmit(@RequestBody PreviewSubmitDto dto);

    @PostMapping(path = "/register/faCer", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewRegisteredFacCertifier(@RequestBody FacilityCertifierRegisterDto dto);

    @GetMapping(path = "/register/faCer/application/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FacilityCertifierRegisterDto> getCertifierRegistrationAppData(@PathVariable("appId") String appId);

}
