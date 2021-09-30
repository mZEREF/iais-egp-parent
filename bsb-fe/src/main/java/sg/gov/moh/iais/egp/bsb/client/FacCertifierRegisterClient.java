package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.*;
import sg.gov.moh.iais.egp.bsb.dto.register.facilityCertifier.AdministratorDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facilityCertifier.CertifyingTeamDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facilityCertifier.FacilityCertifierRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facilityCertifier.OrganisationProfileDto;


@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "cerReg")
public interface FacCertifierRegisterClient {
    @PostMapping(path = "/register/faCer/validate/orgProfile", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateOrganisationProfile(@RequestBody OrganisationProfileDto dto);

    @PostMapping(path = "/register/faCer/validate/cerTeam", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateCertifierTeam(@RequestBody CertifyingTeamDto dto);

    @PostMapping(path = "/register/faCer/validate/facAdmin", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityAdmin(@RequestBody AdministratorDto dto);


    @PostMapping(path = "/register/faCer/validate/primaryDocs", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFaCerPrimaryDocs(@RequestBody PrimaryDocDto dto);

    @PostMapping(path = "/register/faCer/validate/previewSubmit", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFaCerPreviewSubmit(@RequestBody PreviewSubmitDto dto);

    @PostMapping(path = "/register/faCer", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewRegisteredFacCertifier(@RequestBody FacilityCertifierRegisterDto dto);
}
