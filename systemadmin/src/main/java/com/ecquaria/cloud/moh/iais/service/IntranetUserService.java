package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupQueryDto;
import org.springframework.web.bind.annotation.RequestBody;
import sop.rbac.user.UserIdentifier;

import java.util.List;

/**
 * @author weilu
 * @date 2019/12/25 17:40
 */
public interface IntranetUserService {
    void createIntranetUser(OrgUserDto orgUserDto);
    void createIntranetUsers(List<OrgUserDto> orgUserDtos);
    SearchResult<OrgUserQueryDto> doQuery(SearchParam param);
    OrgUserDto updateOrgUser(OrgUserDto orgUserDto);
    void delOrgUser(String id);
    OrgUserDto findIntranetUserById(String id);
    OrgUserDto findIntranetUserByUserId(String userId);
    Boolean UserIsExist(String userId);

    ClientUser saveEgpUser(ClientUser clientUser);
    ClientUser updateEgpUser(ClientUser clientUser);
    Boolean deleteEgpUser(String userDomian,String userId);
    ClientUser getUserByIdentifier(String userId,String userDomain);
    Boolean validatepassword(String password, UserIdentifier userIdentifier);
    List<OrgUserRoleDto> assignRole(List<OrgUserRoleDto> orgUserRoleDtos);
    void removeRole(List<String> ids);
    List<OrgUserRoleDto> retrieveRolesByuserAccId (String userAccId);

    SearchResult<WorkingGroupQueryDto> getWorkingGroupBySearchParam(@RequestBody SearchParam searchParam);
}
