package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsCenterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.EgpUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.WorkloadCalculationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import com.ecquaria.cloud.moh.iais.service.client.EgpUserClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.IntranetUserClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.role.Role;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.xml.sax.SAXException;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    private static final Set<String> notWorkGrp = ImmutableSet.of(
            "BROADCAST",
            "REGULATORY_ANALYTICS",
            "SYSTEM_USER_ADMIN",
            "APO"
    );
    @Override
    public void createIntranetUser(OrgUserDto orgUserDto) {
        intranetUserClient.createOrgUserDto(orgUserDto);
    }

    @Override
    public OrgUserDto createIntrenetUser(OrgUserDto orgUserDto) {
        return intranetUserClient.createIntrenetUser(orgUserDto).getEntity();
    }

    @Override
    public void createIntranetUsers(List<OrgUserDto> orgUserDtos) {
        intranetUserClient.createOrgUserDtos(orgUserDtos);
    }

    @Override
    public List<FeUserDto> getUserListByNricAndIdType(String nric, String idType) {
        return intranetUserClient.getUserListByNricAndIdType(nric, idType).getEntity();
    }

    @Override
    public List<FeUserDto> getUserListByNricAndIdTypeWithDel(String nric, String idType) {
        return intranetUserClient.getUserListByNricAndIdTypeWithDel(nric, idType).getEntity();
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
    public List<LicenseeDto> findLicenseesFe() {
        return intranetUserClient.findLicenseesFe().getEntity();
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
    public boolean canUpdateAccount(FeUserDto user, String prevIdNumber) {
        String idType = user.getIdType();
        String identityNo = user.getIdentityNo();
        if (identityNo.equals(prevIdNumber)){
            return true;
        }

        String orgId = user.getOrgId();
        if (user.isCorpPass()){
            List<OrgUserDto> userList = organizationClient.getUserListByOrganId(orgId).getEntity();
            if (IaisCommonUtils.isNotEmpty(userList)){
                for (OrgUserDto i : userList){
                    if (identityNo.equals(i.getIdentityNo())){
                        return false;
                    }
                }
            }

        }else {
            List<FeUserDto> exist = getUserListByNricAndIdType(identityNo, idType);
            if (IaisCommonUtils.isNotEmpty(exist)){
                return false;
            }
        }
        return true;
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
    public List<OrgUserRoleDto> assignRole(List<OrgUserRoleDto> orgUserRoleDtos) {
        return intranetUserClient.assignRole(orgUserRoleDtos).getEntity();
    }

    @Override
    public void removeRole(List<OrgUserRoleDto> orgUserRoleDtos) {
        intranetUserClient.removeRole(orgUserRoleDtos);
    }

    @Override
    public void removeRoleByAccount(String accountId) {
        intranetUserClient.removeRoleByAccount(accountId);
    }

    @Override
    public Boolean removeEgpRoles(String userDomain, String userId, List<String> roleIds) {
        Boolean flag = Boolean.FALSE;
        if (!IaisCommonUtils.isEmpty(roleIds)) {
            for (String roleId : roleIds) {
                try {
                    flag = egpUserClient.deleteUerRoleIds(userDomain, userId, roleId).getEntity();
                    if (!flag) {
                        return Boolean.FALSE;
                    }
                } catch (Exception e) {
                    return Boolean.FALSE;
                }
            }
        }
        return flag;
    }


    @Override
    public List<UserGroupCorrelationDto> addUserGroupId(List<UserGroupCorrelationDto> userGroupCorrelationDtos) {
        return intranetUserClient.createUserGroupCorrelation(userGroupCorrelationDtos).getEntity();
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
        if (entity != null) {
            groupName = entity.getGroupName();
        }
        return groupName;
    }

    @Override
    public void deleteUserGroupId(List<UserGroupCorrelationDto> userGroupCorrelationDtos) {
        intranetUserClient.createUserGroupCorrelation(userGroupCorrelationDtos);
    }

    @Override
    public List<UserGroupCorrelationDto> getUserGroupCorrelationDtos(String userId, List<String> grpIds, int isLeadForGroup) {
        List<UserGroupCorrelationDto> userGroupCorrelationDtos = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(grpIds)) {
            UserGroupCorrelationDto userGroupCorrelationDto = new UserGroupCorrelationDto();
            userGroupCorrelationDto.setUserId(userId);
            userGroupCorrelationDto.setGroupIds(grpIds);
            userGroupCorrelationDto.setIsLeadForGroup(isLeadForGroup);
            userGroupCorrelationDtos = intranetUserClient.getUserGroupsByUserIdAndWorkGroups(userGroupCorrelationDto).getEntity();

        }
        return userGroupCorrelationDtos;
    }

    @Override
    public List<String> getRoleIdByUserId(String userId) {
        List<String> roleIds = intranetUserClient.retrieveUserRoles(userId).getEntity();
        return roleIds;
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
        map.put("userDomains", domain);
        return egpUserClient.search(map).getEntity();
    }

    @Override
    public List<WorkingGroupDto> getWorkingGroups() {
        return organizationClient.getWorkingGroup("hcsa").getEntity();
    }

    @Override
    public Map<String, List<String>> getRoleIdGrpIdMap() {
        List<WorkingGroupDto> workingGroups = getWorkingGroups();
        Map<String, List<String>> map = IaisCommonUtils.genNewHashMap();
        List<String> aso = IaisCommonUtils.genNewArrayList();
        List<String> asoLeader = IaisCommonUtils.genNewArrayList();
        List<String> pso = IaisCommonUtils.genNewArrayList();
        List<String> psoLeader = IaisCommonUtils.genNewArrayList();
        List<String> inspection = IaisCommonUtils.genNewArrayList();
        List<String> inspectionLeader = IaisCommonUtils.genNewArrayList();
        List<String> ao1 = IaisCommonUtils.genNewArrayList();
        List<String> ao1Leader = IaisCommonUtils.genNewArrayList();
        List<String> ao2 = IaisCommonUtils.genNewArrayList();
        List<String> ao2Leader = IaisCommonUtils.genNewArrayList();
        List<String> ao3 = IaisCommonUtils.genNewArrayList();
        List<String> ao3Leader = IaisCommonUtils.genNewArrayList();
        for (WorkingGroupDto workingGroupDto : workingGroups) {
            String groupId = workingGroupDto.getId();
            String groupName = workingGroupDto.getGroupName();
            if (groupName.contains("Inspection") && !groupName.contains("Approval")) {
                inspection.add(groupId);
                inspectionLeader.add(groupId);
            } else if (groupName.contains("Professional")) {
                pso.add(groupId);
                psoLeader.add(groupId);
            } else if (groupName.contains("Level 1")) {
                ao1.add(groupId);
                ao1Leader.add(groupId);
            } else if (groupName.contains("Level 2")) {
                ao2.add(groupId);
                ao2Leader.add(groupId);
            } else if (groupName.contains("Level 3")) {
                ao3.add(groupId);
                ao3Leader.add(groupId);
            } else if (groupName.contains("Admin Screening officer")) {
                aso.add(groupId);
                asoLeader.add(groupId);
            }
        }
        map.put(RoleConsts.USER_ROLE_ASO, aso);
        map.put(RoleConsts.USER_ROLE_ASO_LEAD, asoLeader);
        map.put(RoleConsts.USER_ROLE_PSO, pso);
        map.put(RoleConsts.USER_ROLE_PSO_LEAD, psoLeader);
        map.put(RoleConsts.USER_ROLE_INSPECTIOR, inspection);
        map.put(RoleConsts.USER_ROLE_INSPECTION_LEAD, inspectionLeader);
        map.put(RoleConsts.USER_ROLE_AO1, ao1);
        map.put(RoleConsts.USER_ROLE_AO1_LEAD, ao1Leader);
        map.put(RoleConsts.USER_ROLE_AO2, ao2);
        map.put(RoleConsts.USER_ROLE_AO2_LEAD, ao2Leader);
        map.put(RoleConsts.USER_ROLE_AO3, ao3);
        map.put(RoleConsts.USER_ROLE_AO3_LEAD, ao3Leader);
        return map;
    }

    @Override
    public SearchResult<WorkingGroupQueryDto> getWorkingGroupBySearchParam(SearchParam searchParam) {
        return intranetUserClient.getWorkingGroupBySearchParam(searchParam).getEntity();
    }

    @Override
    public Map<String, String> importRoleXmlValidation(File xmlFile, int userFileSize, CommonsMultipartFile sessionFile, List<EgpUserRoleDto> egpUserRoleDtos) throws DocumentException {
        Map<String, String> fileErrorMap = IaisCommonUtils.genNewHashMap();
        String errorKey = "userRoleUploadError";
        String errorUserIdKey = "userRoleUploadUserId";
        String errorRoleKey = "userRoleUploadRole";
        String errorworkGrpIdKey = "userRoleUploadWorkGroupId";
        //vali size and type
        String fileName = sessionFile.getOriginalFilename();
        String substring = fileName.substring(fileName.lastIndexOf('.') + 1);
        if (sessionFile.getSize() > userFileSize * 1024 * 1024) {
            log.debug(StringUtil.changeForLog("size"));
            fileErrorMap.put(errorKey, MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(userFileSize), "sizeMax"));
            return fileErrorMap;
        }
        if (!("xml".equalsIgnoreCase(substring))) {
            log.debug(StringUtil.changeForLog("format"));
            fileErrorMap.put(errorKey, MessageUtil.replaceMessage("GENERAL_ERR0018", "XML", "fileType"));
            return fileErrorMap;
        }
        List list = IaisCommonUtils.genNewArrayList();
        try {
            //validate data
            SAXReader saxReader = new SAXReader();
            saxReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            Document document = saxReader.read(xmlFile);
            //root
            Element root = document.getRootElement();
            list = root.elements();
            if (list != null) {
                Element elementCheck = (Element) list.get(0);
                elementCheck.element("roleId").getText();
            }
            //ele
            list = root.elements();
            log.debug(StringUtil.changeForLog("start kai shi jie xi xml------------"));
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("read error"));
            log.error(StringUtil.changeForLog(e.getMessage()), e);
            fileErrorMap.put(errorKey, MessageUtil.dateIntoMessage("USER_ERR018"));
            return fileErrorMap;
        }
        if (!IaisCommonUtils.isEmpty(list)) {
            for (int i = 1; i <= list.size(); i++) {
                log.debug(StringUtil.changeForLog("start kai shi jie xi xml for ------------" + i));
                String userId = null;
                String roleId = null;
                String workingGroupId = null;
                try {
                    boolean errorData = true;
                    Element element = (Element) list.get(i - 1);
                    userId = element.element("userId").getText();
                    roleId = element.element("roleId").getText();
                    workingGroupId = element.element("workingGroupId").getText();
                    if (StringUtil.isEmpty(userId)) {
                        errorData = false;
                        fileErrorMap.put(errorUserIdKey + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "User ID", "field"));
                    } else {
                        OrgUserDto oldOrgUserDto = findIntranetUserByUserId(userId);
                        if (oldOrgUserDto == null) {
                            errorData = false;
                            fileErrorMap.put(errorUserIdKey + i, "USER_ERR012");
                        }
                    }
                    if (StringUtil.isEmpty(roleId)) {
                        errorData = false;
                        fileErrorMap.put(errorRoleKey + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Role ID", "field"));
                    } else {
                        List<Role> rolesByDomain = getRolesByDomain(AppConsts.HALP_EGP_DOMAIN);
                        //egp contains role
                        if (!IaisCommonUtils.isEmpty(rolesByDomain)) {
                            List<String> systemRoleId = IaisCommonUtils.genNewArrayList();
                            for (Role role : rolesByDomain) {
                                String id = role.getId();
                                systemRoleId.add(id);
                            }
                            if (!systemRoleId.contains(roleId)) {
                                fileErrorMap.put(errorRoleKey + i, "USER_ERR013");
                                errorData = false;
                            }
                        }
                    }
                    if (StringUtil.isEmpty(workingGroupId)) {
                        if(!StringUtil.isEmpty(roleId)&&!notWorkGrp.contains(roleId)){
                            errorData = false;
                            fileErrorMap.put(errorworkGrpIdKey + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "WorkingGroup ID", "field"));
                        }
                    } else {
                        List<WorkingGroupDto> workingGroups = getWorkingGroups();
                        //egp contains workgroupId
                        if (!IaisCommonUtils.isEmpty(workingGroups)) {
                            List<String> groupIds = IaisCommonUtils.genNewArrayList();
                            for (WorkingGroupDto workingGroupDto : workingGroups) {
                                String id = workingGroupDto.getId();
                                groupIds.add(id);
                            }
                            if (!groupIds.contains(workingGroupId)) {
                                fileErrorMap.put(errorworkGrpIdKey + i, "USER_ERR017");
                                errorData = false;
                            }
                        }
                    }
                    if (!StringUtil.isEmpty(userId) && !StringUtil.isEmpty(roleId)) {
                        OrgUserDto oldOrgUserDto = findIntranetUserByUserId(userId);
                        if (oldOrgUserDto != null) {
                            List<OrgUserRoleDto> orgUserRoleDtos = retrieveRolesByuserAccId(oldOrgUserDto.getId());
                            if (!IaisCommonUtils.isEmpty(orgUserRoleDtos)) {
                                for (OrgUserRoleDto orgUserRoleDto : orgUserRoleDtos) {
                                    if (orgUserRoleDto != null) {
                                        if (roleId.equals(orgUserRoleDto.getRoleName())) {
                                            errorData = false;
                                            fileErrorMap.put(errorUserIdKey + i, "USER_ERR011");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (!errorData) {
                        EgpUserRoleDto egpUserRoleDto = new EgpUserRoleDto();
                        egpUserRoleDto.setUserId(userId);
                        egpUserRoleDto.setRoleId(roleId);
                        egpUserRoleDto.setWorkGroupId(workingGroupId);
                        egpUserRoleDtos.add(egpUserRoleDto);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    EgpUserRoleDto egpUserRoleDto = new EgpUserRoleDto();
                    egpUserRoleDto.setUserId(userId);
                    egpUserRoleDto.setRoleId(roleId);
                    egpUserRoleDto.setWorkGroupId(workingGroupId);
                    egpUserRoleDtos.add(egpUserRoleDto);
                    continue;
                }
            }
        }
        return fileErrorMap;
    }

    private Map<String, String> containsRoleVal(List<Role> rolesByDomain, String roleId, Map<String, String> fileErrorMap, String errorKey) {
        if (!IaisCommonUtils.isEmpty(rolesByDomain)) {
            List<String> systemRoleId = IaisCommonUtils.genNewArrayList();
            for (Role role : rolesByDomain) {
                String id = role.getId();
                systemRoleId.add(id);
            }
            if (!systemRoleId.contains(roleId)) {
                fileErrorMap.put(errorKey, "USER_ERR013");
            }
        }
        return fileErrorMap;
    }

    private Map<String, String> containsGrpIdVal(List<WorkingGroupDto> workingGroupDtos, String groupId, Map<String, String> fileErrorMap, String errorKey) {
        if (!IaisCommonUtils.isEmpty(workingGroupDtos)) {
            List<String> groupIds = IaisCommonUtils.genNewArrayList();
            for (WorkingGroupDto workingGroupDto : workingGroupDtos) {
                String id = workingGroupDto.getId();
                groupIds.add(id);
            }
            if (!groupIds.contains(groupId)) {
                fileErrorMap.put(errorKey, "USER_ERR017");
            }
        }
        return fileErrorMap;
    }

    @Override
    public List<EgpUserRoleDto> importRoleXml(File xmlFile) throws DocumentException, SAXException {
        SAXReader saxReader = new SAXReader();
        saxReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        Document document = saxReader.read(xmlFile);
        //root
        Element root = document.getRootElement();
        //ele
        List list = root.elements();
        List<OrgUserRoleDto> orgUserRoleDtos = IaisCommonUtils.genNewArrayList();
        List<EgpUserRoleDto> egpUserRoleDtos = IaisCommonUtils.genNewArrayList();
        List<UserGroupCorrelationDto> userGroupCorrelationDtos = IaisCommonUtils.genNewArrayList();
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        for (int i = 0; i < list.size(); i++) {
            try {
                Element element = (Element) list.get(i);
                String userId = element.element("userId").getText();
                String roleId = element.element("roleId").getText();
                String workingGroupId = element.element("workingGroupId").getText();
                OrgUserDto orgUserDto = findIntranetUserByUserId(userId);
                OrgUserRoleDto orgUserRoleDto = new OrgUserRoleDto();
                orgUserRoleDto.setUserAccId(orgUserDto.getId());
                orgUserRoleDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                orgUserRoleDto.setRoleName(roleId);
                orgUserRoleDto.setAuditTrailDto(auditTrailDto);
                List<OrgUserRoleDto> userRoleDtos = IaisCommonUtils.genNewArrayList();
                userRoleDtos.add(orgUserRoleDto);
                List<OrgUserRoleDto> orgUserRoleDtoList= assignRole(userRoleDtos);
                if (orgUserRoleDtoList==null||orgUserRoleDtoList.size()==0) {
                    continue;
                }
                orgUserRoleDtos.add(orgUserRoleDtoList.get(0));
                EgpUserRoleDto egpUserRoleDto = new EgpUserRoleDto();
                egpUserRoleDto.setUserId(userId);
                egpUserRoleDto.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
                egpUserRoleDto.setRoleId(roleId);
                egpUserRoleDto.setPermission("A");
                egpUserRoleDto.setWorkGroupId(workingGroupId);
                //egpUserRoleDto.isSystem()
                egpUserRoleDtos.add(egpUserRoleDto);
                UserGroupCorrelationDto userGroupCorrelationDto = new UserGroupCorrelationDto();
                userGroupCorrelationDto.setUserId(orgUserDto.getId());
                userGroupCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                userGroupCorrelationDto.setGroupId(workingGroupId);
                if (roleId.contains("LEAD")) {
                    userGroupCorrelationDto.setIsLeadForGroup(1);
                } else {
                    userGroupCorrelationDto.setIsLeadForGroup(0);
                }
                userGroupCorrelationDto.setUserRoleId(orgUserRoleDtoList.get(0).getId());
                userGroupCorrelationDto.setAuditTrailDto(auditTrailDto);
                if(!StringUtil.isEmpty(workingGroupId)){
                    userGroupCorrelationDtos.add(userGroupCorrelationDto);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                continue;
            }
        }
        if (!IaisCommonUtils.isEmpty(orgUserRoleDtos)) {
            assignRole(orgUserRoleDtos);
            createEgpRoles(egpUserRoleDtos);
            addUserGroupId(userGroupCorrelationDtos);
        }

//        if (!IaisCommonUtils.isEmpty(userGroupCorrelationDtos)) {
//            Iterator<UserGroupCorrelationDto> iterator = userGroupCorrelationDtos.iterator();
//            List<String> groupIds = IaisCommonUtils.genNewArrayList();
//            while (iterator.hasNext()) {
//                UserGroupCorrelationDto next = iterator.next();
//                String groupId = next.getGroupId();
//                Integer isLeadForGroup = next.getIsLeadForGroup();
//                if (groupIds.contains(groupId)) {
//                    iterator.remove();
//                } else {
//                    groupIds.add(groupId);
//                }
//            }
//            for (UserGroupCorrelationDto userGroupCorrelationDto : userGroupCorrelationDtos) {
//                String groupId = userGroupCorrelationDto.getGroupId();
//                if (groupIds.contains(groupId)) {
//                    userGroupCorrelationDto.setIsLeadForGroup(1);
//                }
//            }
//
//        }
        return egpUserRoleDtos;
    }

    @Override
    public OrganizationDto getByUenNoAndStatus(String uen,String status) {
        return intranetUserClient.getByUenNoAndStatus(uen, status).getEntity();
    }

    @Override
    public Boolean workloadCalculation(WorkloadCalculationDto workloadCalculationDto) {
        return intranetUserClient.workloadCalculation(workloadCalculationDto).getEntity();
    }

    @Override
    public List<SelectOption> getRoleSelection(boolean fromDsCenter,String licenseeId,String orgId) {
      if(fromDsCenter){
          List<String> strings = IaisCommonUtils.genNewArrayList();
          List<DsCenterDto> dsCenterDtos = hcsaLicenceClient.getDsCenterDtosByOrganizationId(orgId).getEntity();
          if(IaisCommonUtils.isNotEmpty(dsCenterDtos)){
              dsCenterDtos.stream().forEach(dsCenterDto -> {
                  if(!strings.contains(dsCenterDto.getCenterType())){
                      strings.add(dsCenterDto.getCenterType());
                  }
              });
          }
          return IaisEGPHelper.getRoleSelectionByDsCenter(strings);
      }else {
          return getRoleSelection(licenseeId);
      }
    }

    @Override
    public List<SelectOption> getRoleSelection(String licenseeId) {
        if(StringUtil.isEmpty(licenseeId)){
            return IaisEGPHelper.getRoleSelection(IaisCommonUtils.genNewArrayList());
        }
        List<LicenceDto> licenceDtos = hcsaLicenceClient.getActiveLicencesByLicenseeId(licenseeId).getEntity();
        List<String> strings = IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isNotEmpty(licenceDtos)){
            licenceDtos.stream().forEach(licenceDto ->{
                if(!strings.contains(licenceDto.getSvcName())){
                    strings.add(licenceDto.getSvcName());
                }
            } );
        }
        return IaisEGPHelper.getRoleSelection(strings);
    }
}
