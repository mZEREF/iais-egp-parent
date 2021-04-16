package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.RoleService;
import com.ecquaria.cloud.moh.iais.service.client.EgpUserCommonClient;
import com.ecquaria.cloud.role.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2021/1/12 18:02
 **/
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private EgpUserCommonClient egpUserCommonClient;

    @Override
    public List<Role> getRolesByDomain(String domain) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        map.put("userDomains", domain);
        return egpUserCommonClient.search(map).getEntity();
    }

    @Override
    public Role getRoleByDomainRoleId(String domain, String roleId) {
        if(StringUtil.isEmpty(domain) || StringUtil.isEmpty(roleId)){
            return null;
        }
        List<Role> roles = getRolesByDomain(domain);
        if(!IaisCommonUtils.isEmpty(roles)) {
            for (Role roleDto : roles) {
                if(roleDto != null && roleId.equals(roleDto.getId())) {//NOSONAR
                    return roleDto;
                }
            }
        }
        return null;
    }
}
