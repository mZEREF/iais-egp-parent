package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(value = "iais-organization", configuration = FeignClientsConfiguration.class, contextId = "org")
public interface OrganizationClient {
    @GetMapping(value = "/iais-orgUserRole/user-by-id/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {MediaType.APPLICATION_JSON_VALUE})
    OrgUserDto retrieveOrgUserAccountById(@PathVariable(value = "id") String id);

    @PostMapping(value = "/iais-orgUserRole/users-by-id", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {MediaType.APPLICATION_JSON_VALUE})
    List<OrgUserDto> retrieveOrgUserAccount(@RequestBody List<String> ids);

    /** Attention, make sure the id type and id number is not empty before use this method */
    @GetMapping(value = "/iais-internet-user/fe-user-acc/{nric}/{idType}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<OrgUserDto> getUserListByNricAndIdType(@PathVariable(value = "nric") String nric, @PathVariable("idType") String idType);
}
