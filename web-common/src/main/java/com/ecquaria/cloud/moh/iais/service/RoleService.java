package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.role.Role;

import java.util.List;

/**
 * @author Shicheng
 * @date 2021/1/12 18:01
 **/
public interface RoleService {
    List<Role> getRolesByDomain(String domain);
    Role getRoleByDomainRoleId(String domain, String roleId);
}
