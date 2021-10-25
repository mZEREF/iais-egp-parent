package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * This interface is copied from {@code com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient} under be-main.
 * Only keep the method related to login.
 * This file will be removed when we integrate phase one and two.
 */
@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class)
public interface LoginClient {
    @GetMapping(value = "/iais-orgUserRole/users-by-loginId/{user_id}", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<OrgUserDto> retrieveOneOrgUserAccount(@PathVariable(value = "user_id") String userId);
}
