package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.*;



@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "facReg")
public interface FacilityRegisterClient {
    @PostMapping(path = "/register/facility/validate/facSelection", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilitySelection(@RequestBody FacilitySelectionDto dto);

    @PostMapping(path = "/register/facility/validate/facProfile", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityProfile(@RequestBody FacilityProfileDto dto);

    @PostMapping(path = "/register/facility/validate/facOperator", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityOperator(@RequestBody FacilityOperatorDto dto);

    @PostMapping(path = "/register/facility/validate/facAuthoriser", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityAuthoriser(@RequestBody FacilityAuthoriserDto dto);

    @PostMapping(path = "/register/facility/validate/facAdmin", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityAdmin(@RequestBody FacilityAdministratorDto dto);

    @PostMapping(path = "/register/facility/validate/facOfficer", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityOfficer(@RequestBody FacilityOfficerDto dto);

    @PostMapping(path = "/register/facility/validate/facCommittee", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityCommittee(@RequestBody FacilityCommitteeDto dto);

    @PostMapping(path = "/register/facility/validate/facBat", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityBiologicalAgentToxin(@RequestBody BiologicalAgentToxinDto dto);

    @PostMapping(path = "/register/facility/validate/otherAppInfo", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityOtherAppInfo(@RequestBody OtherApplicationInfoDto dto);

    @PostMapping(path = "/register/facility/validate/primaryDocs", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityPrimaryDocs(@RequestBody PrimaryDocDto dto);

    @PostMapping(path = "/register/facility/validate/previewSubmit", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFacilityPreviewSubmit(@RequestBody PreviewSubmitDto dto);

    @PostMapping(path = "/register/facility", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewRegisteredFacility(@RequestBody FacilityRegisterDto dto);
}
