package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * AUTHOR: YiMing
 * DATE:2021/9/15 10:03
 **/
@FeignClient(name = "iais-organization",configuration = FeignConfiguration.class)
public interface OrganizationClient {
    @GetMapping(value = "/iais-orguser-be/OrgUsers/{roleId}")
    FeignResponseEntity<List<OrgUserDto>> retrieveOrgUserAccountByRoleId(@PathVariable("roleId") String roleId);

    @PostMapping(value = "/iais-orgUserRole/user-by-roles", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<OrgUserDto>> retrieveOrgUserByroleId(@RequestBody List<String> roleId);
}
