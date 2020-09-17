package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.EgpUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import com.ecquaria.cloud.moh.iais.service.client.EgpUserClient;
import com.ecquaria.cloud.moh.iais.service.client.IntranetUserClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.role.Role;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.rbac.user.UserIdentifier;

import java.io.File;
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
    @SearchTrack(catalog = "systemAdmin", key = "IntranetUserQuery")
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
    public void removeRole(List<OrgUserRoleDto> orgUserRoleDtos) {
        intranetUserClient.removeRole(orgUserRoleDtos);
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

    @Override
    public Map<String, String> importRoleXmlValidation(File xmlFile, int userFileSize, CommonsMultipartFile sessionFile) throws DocumentException {
        Map<String, String> fileErrorMap = IaisCommonUtils.genNewHashMap();
        String errorKey = "userRoleUploadError";
        //vali size and type
        String fileName = sessionFile.getOriginalFilename();
        String substring = fileName.substring(fileName.lastIndexOf('.') + 1);
        if (sessionFile.getSize() > userFileSize * 1024 * 1024) {
            fileErrorMap.put(errorKey, MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(userFileSize),"sizeMax"));
            return fileErrorMap;
        }
        if(!("xml".equalsIgnoreCase(substring))){
            fileErrorMap.put(errorKey, MessageUtil.replaceMessage("GENERAL_ERR0018", "XML","fileType"));
            return fileErrorMap;
        }
        //validate data
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(xmlFile);
        //root
        Element root = document.getRootElement();
        //ele
        List list = root.elements();
        for (int i = 0; i < list.size(); i++) {
            try {
                Element element = (Element) list.get(i);
                String userId = element.element("userId").getText();
                String roleId = element.element("roleId").getText();
                if(StringUtil.isEmpty(userId)){
                    fileErrorMap.put(errorKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "userId","field"));
                    return fileErrorMap;
                }
                if(StringUtil.isEmpty(roleId)){
                    fileErrorMap.put(errorKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "roleId","field"));
                    return fileErrorMap;
                }
                OrgUserDto oldOrgUserDto = findIntranetUserByUserId(userId);
                if(oldOrgUserDto == null){
                    fileErrorMap.put(errorKey, "USER_ERR011");
                    return fileErrorMap;
                }
                List<Role> rolesByDomain = getRolesByDomain(AppConsts.HALP_EGP_DOMAIN);
               /* if(!rolesByDomain.contains(roleId)){
                    fileErrorMap.put(errorKey, "USER_ERR011");
                    return fileErrorMap;
                }*/
                List<OrgUserRoleDto> orgUserRoleDtos = retrieveRolesByuserAccId(userId);
                if(!IaisCommonUtils.isEmpty(orgUserRoleDtos)){
                    for(OrgUserRoleDto orgUserRoleDto : orgUserRoleDtos){
                        if(orgUserRoleDto != null){
                            if(roleId.equals(orgUserRoleDto.getRoleName())){
                                fileErrorMap.put(errorKey, "USER_ERR011");
                                return fileErrorMap;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                continue;
            }
        }
        return fileErrorMap;
    }

    @Override
    public List<EgpUserRoleDto> importRoleXml(File xmlFile) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(xmlFile);
        //root
        Element root = document.getRootElement();
        //ele
        List list = root.elements();
        List<OrgUserRoleDto> orgUserRoleDtos = IaisCommonUtils.genNewArrayList();
        List<EgpUserRoleDto> egpUserRoleDtos = IaisCommonUtils.genNewArrayList();
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        for (int i = 0; i < list.size(); i++) {
            try {
                Element element = (Element) list.get(i);
                String userId = element.element("userId").getText();
                String roleId = element.element("roleId").getText();
                OrgUserDto orgUserDto = findIntranetUserByUserId(userId);
                OrgUserRoleDto orgUserRoleDto = new OrgUserRoleDto();
                orgUserRoleDto.setUserAccId(orgUserDto.getId());
                orgUserRoleDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                orgUserRoleDto.setRoleName(roleId);
                orgUserRoleDto.setAuditTrailDto(auditTrailDto);
                orgUserRoleDtos.add(orgUserRoleDto);
                EgpUserRoleDto egpUserRoleDto = new EgpUserRoleDto();
                egpUserRoleDto.setUserId(userId);
                egpUserRoleDto.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
                egpUserRoleDto.setRoleId(roleId);
                egpUserRoleDto.setPermission("A");
                //egpUserRoleDto.isSystem()
                egpUserRoleDtos.add(egpUserRoleDto);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                continue;
            }
            assignRole(orgUserRoleDtos);
            createEgpRoles(egpUserRoleDtos);
        }
        return egpUserRoleDtos;
    }
}
