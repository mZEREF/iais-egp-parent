package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeAdminDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeAdminQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;

import java.util.List;
import java.util.Map;

public interface OrgUserManageService {

    SearchResult<FeUserQueryDto> getFeUserList(SearchParam searchParam);
    SearchResult<FeAdminQueryDto> getFeAdminList(SearchParam searchParam);
    OrganizationDto getOrganizationById(String id);
    FeAdminDto addAdminAccount(FeAdminDto feAdminDto);
    FeUserDto getUserAccount(String userId);
    FeUserDto editUserAccount(FeUserDto feUserDto);
    String ChangeActiveStatus(String userId, String targetStatus);
    OrgUserRoleDto addUserRole(OrgUserRoleDto orgUserRoleDto);

    Boolean validateSingpassId(String nric, String pwd);

    Boolean isKeyappointment(String uen, String nricNumber);

	IaisApiResult<List<String>> createSingpassAccount(String nric);

	IaisApiResult<String> createCropUser(String jsonStr);

    Map<String, Object>  getUserByNricAndUen(String uen, String nric);
}
