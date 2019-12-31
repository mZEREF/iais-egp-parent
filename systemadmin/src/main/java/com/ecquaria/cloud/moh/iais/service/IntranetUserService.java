package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserQueryDto;

/**
 * @author weilu
 * @date 2019/12/25 17:40
 */
public interface IntranetUserService {
    void createIntranetUser(OrgUserDto orgUserDto);
    SearchResult<OrgUserQueryDto> doQuery(SearchParam param);
    OrgUserDto updateOrgUser(OrgUserDto orgUserDto);
    void delOrgUser(String id);
    OrgUserDto findIntranetUserById(String id);
    Boolean UserIsExist(String userId);

    ClientUser saveEgpUser(ClientUser clientUser);
    ClientUser updateEgpUser(ClientUser clientUser);

}
