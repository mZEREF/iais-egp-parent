package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.myinfo.MyInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;

import java.util.List;

public interface OrgUserManageService {
    SearchResult<FeUserQueryDto> getFeUserList(SearchParam searchParam);

    OrganizationDto getOrganizationById(String id);

    FeUserDto getUserAccount(String userId);

    FeUserDto editUserAccount(FeUserDto feUserDto);

    String ChangeActiveStatus(String userId, String targetStatus);

    OrgUserRoleDto addUserRole(OrgUserRoleDto orgUserRoleDto);

    FeUserDto createSingpassAccount(OrganizationDto organizationDto);

    FeUserDto createCorpPassUser(OrganizationDto organizationDto);

    void refreshLicensee(String uen);

    void refreshLicensee(LicenseeDto licenseeDto);

    List<FeUserDto> getAccountByOrgId(String orgId);

    FeUserDto getUserByNricAndUen(String uen, String nric);

    FeUserDto getFeUserAccountByNricAndType(String nric, String idType);

    void createClientUser(FeUserDto userDto);

    void updateEgpUser(FeUserDto feUserDto);

    OrganizationDto findOrganizationByUen(String uen);

    void updateUserBe(OrganizationDto organizationDto);

    LicenseeDto getLicenseeById(String id);

    List<LicenseeDto> getLicenseeByOrgId(String orgId);

    List<LicenseeKeyApptPersonDto> getPersonById(String id);

    IaisApiResult<Void> checkIssueUen(String idNo, String idType);

    List<LicenseeDto> getLicenseeNoUen();

    Boolean validatePwd(FeUserDto feUserDto);

    LicenseeDto saveMyinfoDataByFeUserDtoAndLicenseeDto( LicenseeDto licenseeDto, FeUserDto feUserDto, MyInfoDto myInfoDto,boolean amendLicensee);

    Boolean isNotExistUserAccount(String orgId);
}
