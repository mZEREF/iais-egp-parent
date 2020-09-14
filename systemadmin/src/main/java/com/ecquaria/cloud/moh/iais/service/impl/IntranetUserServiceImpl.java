package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.*;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import com.ecquaria.cloud.moh.iais.service.client.EgpUserClient;
import com.ecquaria.cloud.moh.iais.service.client.IntranetUserClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.role.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sop.rbac.user.UserIdentifier;

import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2019/12/25 17:41
 */
@Service
@Slf4j
public class IntranetUserServiceImpl implements IntranetUserService {

    @Autowired
    private IntranetUserClient intranetUserClient;
    @Autowired
    private EgpUserClient egpUserClient;
    @Autowired
    private OrganizationClient organizationClient;

    @Override
    public void createIntranetUser(OrgUserDto orgUserDto) {
        intranetUserClient.createOrgUserDto(orgUserDto);
    }

    @Override
    public void createIntranetUsers(List<OrgUserDto> orgUserDtos) {
        intranetUserClient.createOrgUserDtos(orgUserDtos);
    }

    @Override
    public SearchResult<OrgUserQueryDto> doQuery(SearchParam param) {
        return intranetUserClient.doQuery(param).getEntity();
    }

    @Override
    public OrgUserDto updateOrgUser(OrgUserDto orgUserDto) {
        return intranetUserClient.updateOrgUserDto(orgUserDto).getEntity();
    }

    @Override
    public void delOrgUser(String id) {
        intranetUserClient.delOrgUser(id);
    }

    @Override
    public OrgUserDto findIntranetUserById(String id) {
        return intranetUserClient.findIntranetUserById(id).getEntity();
    }

    @Override
    public OrgUserDto findIntranetUserByUserId(String userId) {
        OrgUserDto orgUserDto = null;
        orgUserDto = intranetUserClient.getOrgUserAccountByUserId(userId).getEntity();
        return orgUserDto;
    }

    @Override
    public Boolean UserIsExist(String userId) {
        OrgUserDto entity = intranetUserClient.retrieveOneOrgUserAccount(userId).getEntity();
        if (entity != null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public ClientUser saveEgpUser(ClientUser clientUser) {
        return egpUserClient.createClientUser(clientUser).getEntity();
    }

    @Override
    public ClientUser updateEgpUser(ClientUser clientUser) {
        return egpUserClient.updateClientUser(clientUser).getEntity();
    }

    @Override
    public Boolean deleteEgpUser(String userDomian, String userId) {
        return egpUserClient.deleteUser(userDomian, userId).getEntity();
    }

    @Override
    public ClientUser getUserByIdentifier(String userId, String userDomain) {
        return egpUserClient.getUserByIdentifier(userId, userDomain).getEntity();
    }

    @Override
    public Boolean validatepassword(String password, UserIdentifier userIdentifier) {
        Boolean entity = egpUserClient.validatepassword(password, userIdentifier).getEntity();
        if (entity == null) {
            return Boolean.FALSE;
        }
        return entity;
    }

    @Override
    public List<OrgUserRoleDto> assignRole(List<OrgUserRoleDto> orgUserRoleDtos) {
        return intranetUserClient.assignRole(orgUserRoleDtos).getEntity();
    }

    @Override
    public void removeRole(List<String> ids) {
        intranetUserClient.removeRole(ids);
    }

    @Override
    public void removeEgpRoles(String userDomain, String userId, List<String> roleIds) {
        if (!IaisCommonUtils.isEmpty(roleIds)) {
            for (String roleId : roleIds) {
                egpUserClient.deleteUerRoleIds(userDomain, userId, roleId);
            }
        }
    }


    @Override
    public void addUserGroupId(List<UserGroupCorrelationDto> userGroupCorrelationDtos) {
        intranetUserClient.createUserGroupCorrelation(userGroupCorrelationDtos);
    }

    @Override
    public List<UserGroupCorrelationDto> getUserGroupsByUserId(String userId) {
        List<UserGroupCorrelationDto> entity = intranetUserClient.getUserGroupsByUserId(userId).getEntity();
        return entity;
    }

    @Override
    public String getWrkGrpById(String groupId) {
        WorkingGroupDto entity = intranetUserClient.getWrkGrpById(groupId).getEntity();
        String groupName = null;
        if(entity!=null){
            groupName = entity.getGroupName();
        }
        return groupName;
    }

    @Override
    public void deleteUserGroupId(List<UserGroupCorrelationDto> userGroupCorrelationDtos) {

        intranetUserClient.createUserGroupCorrelation(userGroupCorrelationDtos);
    }

    @Override
    public String createEgpRoles(List<EgpUserRoleDto> egpUserRoleDtos) {
        if (!IaisCommonUtils.isEmpty(egpUserRoleDtos)) {
            for (EgpUserRoleDto egpUserRoleDto : egpUserRoleDtos) {
                egpUserClient.createUerRoleIds(egpUserRoleDto).getEntity();
            }
        }
        return null;
    }

    @Override
    public List<OrgUserRoleDto> retrieveRolesByuserAccId(String userAccId) {
        return intranetUserClient.retrieveRolesByuserAccId(userAccId).getEntity();
    }

    @Override
    public List<OrgUserRoleDto> getOrgUserRoleDtoById(List<String> ids) {
        return intranetUserClient.getUserRoleByIds(ids).getEntity();
    }

    @Override
    public List<Role> getRolesByDomain(String domain) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        map.put("userDomain", domain);
        return egpUserClient.search(map).getEntity();
    }

    @Override
    public List<WorkingGroupDto> getWorkingGroups() {
        return organizationClient.getWorkingGroup("hcsa").getEntity();
    }

    @Override
    public SearchResult<WorkingGroupQueryDto> getWorkingGroupBySearchParam(SearchParam searchParam) {
        return intranetUserClient.getWorkingGroupBySearchParam(searchParam).getEntity();
    }
}
