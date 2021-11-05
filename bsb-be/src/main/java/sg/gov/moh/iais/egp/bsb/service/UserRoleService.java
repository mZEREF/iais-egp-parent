package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import sg.gov.moh.iais.egp.bsb.client.EgpUserClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The 'Role' here means working_group, user_role and USER_ROLE_ASSIGN. Or any record, column that can identify a user.
 * This class provide some functions that can not be done at the API part. Usually because EGP related data are not
 * available there.
 */
@Service
public class UserRoleService {
    private final EgpUserClient egpUserClient;

    @Autowired
    public UserRoleService(EgpUserClient egpUserClient) {
        this.egpUserClient = egpUserClient;
    }


    public List<Role> getRolesByDomain(String domain) {
        return egpUserClient.search(Collections.singletonMap("userDomains", domain));
    }


    public List<SelectOption> getRoleSelectOptions(String domain, List<String> roles) {
        Assert.hasLength(domain, "domain can not be empty");
        List<SelectOption> roleOptions;
        if (roles == null || roles.isEmpty()) {
            /* This one option is useless, and should be removed logically.
             * This is copied from phase 1, and the error handle logic may be enhanced in the future */
            roleOptions = Collections.singletonList(new SelectOption("", "Please Select"));
        } else {
            roleOptions = new ArrayList<>(roles.size());
            List<Role> domainRoles = getRolesByDomain(domain);
            for (String role : roles) {
                for (Role domainRole : domainRoles) {
                    if (role.equals(domainRole.getId())) {
                        roleOptions.add(new SelectOption(role, domainRole.getName()));
                        break;
                    }
                }
            }
        }
        return roleOptions;
    }
}
