package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
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
import com.ecquaria.cloud.role.Role;
import org.dom4j.DocumentException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.xml.sax.SAXException;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2019/12/25 17:40
 */
public interface IntranetUserService {
    void createIntranetUser(OrgUserDto orgUserDto);
    OrgUserDto createIntrenetUser(OrgUserDto orgUserDto);
    void createIntranetUsers(List<OrgUserDto> orgUserDtos);
    List<FeUserDto> getUserListByNricAndIdType(String nric, String idType);
    SearchResult<OrgUserQueryDto> doQuery(SearchParam param);
    OrgUserDto updateOrgUser(OrgUserDto orgUserDto);
    void delOrgUser(String id);
    OrgUserDto findIntranetUserById(String id);
    List<LicenseeDto> findLicenseesFe();
    OrgUserDto findIntranetUserByUserId(String userId);
    Boolean UserIsExist(String userId);

    boolean canUpdateAccount(FeUserDto user, String prevIdNumber);

    ClientUser saveEgpUser(ClientUser clientUser);
    ClientUser updateEgpUser(ClientUser clientUser);
    Boolean deleteEgpUser(String userDomian,String userId);
    ClientUser getUserByIdentifier(String userId,String userDomain);
    List<OrgUserRoleDto> assignRole(List<OrgUserRoleDto> orgUserRoleDtos);
    void removeRole(List<OrgUserRoleDto> orgUserRoleDtos);
    void removeRoleByAccount(String accountId);
    List<UserGroupCorrelationDto> addUserGroupId(List<UserGroupCorrelationDto> userGroupCorrelationDtos);
    List<UserGroupCorrelationDto> getUserGroupsByUserId(String userId);
    String getWrkGrpById(String groupId);
    void deleteUserGroupId(List<UserGroupCorrelationDto> userGroupCorrelationDtos);
    List<UserGroupCorrelationDto> getUserGroupCorrelationDtos(String userId,List<String> grpIds,int isLeadForGroup);
    List<String> getRoleIdByUserId(String userId);

    String createEgpRoles(List<EgpUserRoleDto> egpUserRoleDtos);
    Boolean removeEgpRoles(String userDomain,String userId,List<String> roleIds);
    List<OrgUserRoleDto> retrieveRolesByuserAccId (String userAccId);
    List<OrgUserRoleDto> getOrgUserRoleDtoById(List<String> ids);
    List<Role> getRolesByDomain(String domain);
    List<WorkingGroupDto> getWorkingGroups();

    Map<String,List<String>> getRoleIdGrpIdMap();


    SearchResult<WorkingGroupQueryDto> getWorkingGroupBySearchParam(@RequestBody SearchParam searchParam);

    /**
      * @author: shicheng
      * @Date 2020/9/16
      * @Param: xmlFile
      * @return: Map<String, String>
      * @Descripation: importRoleXmlValidation
      */
    Map<String, String> importRoleXmlValidation(File xmlFile, int userFileSize, CommonsMultipartFile sessionFile, List<EgpUserRoleDto> egpUserRoleDtos) throws DocumentException;

    /**
      * @author: shicheng
      * @Date 2020/9/16
      * @Param: xmlFile
      * @return: importRoleXml
      * @Descripation: save role data
      */
    List<EgpUserRoleDto> importRoleXml(File xmlFile) throws DocumentException, SAXException;

    OrganizationDto getByUenNoAndStatus(String uen,String status);

    Boolean workloadCalculation(WorkloadCalculationDto workloadCalculationDto);

    List<SelectOption> getRoleSelection(String licenseeId);
}
