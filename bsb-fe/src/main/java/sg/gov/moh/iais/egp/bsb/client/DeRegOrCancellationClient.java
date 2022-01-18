package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.deregorcancellation.DeRegistrationFacilityDto;

@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "deRegOrCancellation")
public interface DeRegOrCancellationClient {
    @GetMapping(path = "/deRegOrCancellation/deRegistrationFacility/getData/{approvalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DeRegistrationFacilityDto> getDeRegistrationFacilityData(@PathVariable("approvalId") String approvalId);
}
