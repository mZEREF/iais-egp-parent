package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeAdminQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;

import java.util.List;
import java.util.Map;

public interface OrgUserManageService {

    SearchResult<FeUserQueryDto> getFeUserList(SearchParam searchParam);
    SearchResult<FeAdminQueryDto> getFeAdminList(SearchParam searchParam);
    OrganizationDto getOrganizationById(String id);
    FeUserDto addAdminAccount(FeUserDto feUserDto);
    FeUserDto getUserAccount(String userId);
    FeUserDto editUserAccount(FeUserDto feUserDto);
    String ChangeActiveStatus(String userId, String targetStatus);
    OrgUserRoleDto addUserRole(OrgUserRoleDto orgUserRoleDto);

    Boolean validateSingpassId(String nric, String pwd);

    Boolean isKeyappointment(String uen, String nricNumber);

    List<String> getUenListByNric(String nric);

    OrgUserDto createSingpassAccount(String nric);

    OrgUserDto createCropUser(String jsonStr);

    Map<String, Object>  getUserByNricAndUen(String uen, String nric);

    void createClientUser(OrgUserDto orgUserDto);
    void saveEgpUser(FeUserDto feUserDto);
    void updateEgpUser(FeUserDto feUserDto);

    OrganizationDto findOrganizationByUen(String uen);
}
