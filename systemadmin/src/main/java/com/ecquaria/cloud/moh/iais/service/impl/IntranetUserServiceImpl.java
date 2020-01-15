package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupQueryDto;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import com.ecquaria.cloud.moh.iais.service.client.EgpUserClient;
import com.ecquaria.cloud.moh.iais.service.client.IntranetUserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sop.rbac.user.UserIdentifier;

/**
 * @author weilu
 * @date 2019/12/25 17:41
 */
@Service
@Slf4j
public class IntranetUserServiceImpl implements IntranetUserService {

    @Autowired
    private IntranetUserClient intranetUserClient ;
    @Autowired
    private EgpUserClient egpUserClient;

    @Override
    public void createIntranetUser(OrgUserDto orgUserDto) {
        intranetUserClient.createOrgUserDto(orgUserDto);
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
        return  intranetUserClient.findIntranetUserById(id).getEntity();
    }

    @Override
    public Boolean UserIsExist(String userId) {
        OrgUserDto entity = intranetUserClient.retrieveOneOrgUserAccount(userId).getEntity();
        if(entity!=null){
            return true;
        }
           return false;
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
        return egpUserClient.deleteUser(userDomian,userId).getEntity();
    }

    @Override
    public ClientUser getUserByIdentifier(String userId,String userDomain) {
        return egpUserClient.getUserByIdentifier(userId,userDomain).getEntity();
    }

    @Override
    public Boolean validatepassword(String password, UserIdentifier userIdentifier) {
        Boolean entity = egpUserClient.validatepassword(password, userIdentifier).getEntity();
        if(entity==null){
            return false;
        }
        return entity;
    }

    @Override
    public SearchResult<WorkingGroupQueryDto> getWorkingGroupBySearchParam(SearchParam searchParam) {
        return intranetUserClient.getWorkingGroupBySearchParam(searchParam).getEntity();
    }
}
