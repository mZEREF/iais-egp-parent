package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;

import java.util.List;

public interface OrgUserManageService {

    SearchResult<FeUserQueryDto> getFeUserList(SearchParam searchParam);
    OrganizationDto getOrganizationById(String id);
    FeUserDto addAdminAccount(FeUserDto feUserDto);
    FeUserDto getUserAccount(String userId);
    FeUserDto editUserAccount(FeUserDto feUserDto);
    String ChangeActiveStatus(String userId, String targetStatus);
    OrgUserRoleDto addUserRole(OrgUserRoleDto orgUserRoleDto);

    Boolean validateSingpassId(String nric, String pwd);

    Boolean isKeyappointment(String uen, String nricNumber);

    List<String> getUenListByIdAndType(String nric, String idType);

    OrganizationDto createSingpassAccount(OrganizationDto organizationDto);

    FeUserDto createCropUser(OrganizationDto organizationDto);

    FeUserDto getUserByNricAndUen(String uen, String nric);

    FeUserDto getFeUserAccountByNricAndType(String nric, String idType);

    void createClientUser(FeUserDto userDto);
    void updateEgpUser(FeUserDto feUserDto);

    OrganizationDto findOrganizationByUen(String uen);

    void updateUserBe(OrganizationDto organizationDto);
    LicenseeDto getLicenseeById(String id);
    List<LicenseeKeyApptPersonDto> getPersonById(String id);

    IaisApiResult<Void> checkIssueUen(String idNo, String idType);

    List<LicenseeDto> getLicenseeNoUen();
}
