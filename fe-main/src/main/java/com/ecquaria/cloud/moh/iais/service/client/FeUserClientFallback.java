package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;


import java.util.List;

public class FeUserClientFallback implements FeUserClient{
    @Override
    public FeignResponseEntity<SearchResult<FeUserQueryDto>> getFeUserList(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("getFeUserList", searchParam);
    }

    @Override
    public FeignResponseEntity<OrganizationDto> findOrganizationByUen(String uen) {
        return IaisEGPHelper.getFeignResponseEntity("findOrganizationByUen", uen);
    }

    @Override
    public FeignResponseEntity<FeUserDto> getUserAccount(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getUserAccount", id);
    }

    @Override
    public FeignResponseEntity<List<String>> getUenListByIdAndType(String nric, String idType) {
        return IaisEGPHelper.getFeignResponseEntity("getUenListByIdAndType", nric);
    }

    @Override
    public FeignResponseEntity<FeUserDto> getInternetUserByNricAndIdType(String nric, String idType, String uen) {
        return IaisEGPHelper.getFeignResponseEntity("getInternetUserByNricAndIdType", nric);
    }

    @Override
    public FeignResponseEntity<FeUserDto> editUserAccount(FeUserDto feUserDto) {
        return IaisEGPHelper.getFeignResponseEntity("editUserAccount", feUserDto);
    }

    @Override
    public FeignResponseEntity<OrgUserRoleDto> addUserRole(OrgUserRoleDto orgUserRoleDto) {
        return IaisEGPHelper.getFeignResponseEntity("addUserRole", orgUserRoleDto);
    }

    @Override
    public FeignResponseEntity<List<FeUserDto>> getAccountByOrgId(String orgId) {
        return IaisEGPHelper.getFeignResponseEntity("getAccountByOrgId", orgId);
    }

    @Override
    public FeignResponseEntity<List<FeUserDto>> getAdminAccountByOrgId(String orgId) {
        return IaisEGPHelper.getFeignResponseEntity("getAdminAccountByOrgId", orgId);
    }

    @Override
    public FeignResponseEntity<OrganizationDto> createHalpAccount(OrganizationDto organizationDto) {
        return IaisEGPHelper.getFeignResponseEntity("createHalpAccount", organizationDto);
    }


    @Override
    public FeignResponseEntity<FeUserDto> getUserByNricAndUen(String uen, String nric) {
        return IaisEGPHelper.getFeignResponseEntity("getUserByNricAndUen", nric);
    }

    @Override
    public FeignResponseEntity<InterInboxUserDto> findUserInfoByUserId(String UserId) {
        return IaisEGPHelper.getFeignResponseEntity("findUserInfoByUserId", UserId);
    }

    @Override
    public FeignResponseEntity<LicenseeDto> getLicenseeById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenseeById", id);
    }

    @Override
    public FeignResponseEntity<List<LicenseeDto>> getLicenseeNoUen() {
        return IaisEGPHelper.getFeignResponseEntity("getLicenseeNoUen");
    }

    @Override
    public FeignResponseEntity<Boolean> validatePwd(FeUserDto feUserDto) {
        return IaisEGPHelper.getFeignResponseEntity("validatePwd", feUserDto);
    }

    @Override
    public FeignResponseEntity<Boolean> isNotExistUserAccount(String orgId) {
        return IaisEGPHelper.getFeignResponseEntity("isNotExistUserAccount", orgId);
    }

    @Override
    public FeignResponseEntity<Boolean> setPermitLoginStatusInUenTrack(String uen, String nricNumber, Boolean isPermit) {
        return IaisEGPHelper.getFeignResponseEntity("setPermitLoginStatusInUenTrack", nricNumber);
    }

    @Override
    public FeignResponseEntity<String> getExpireSingPassList() {
        return IaisEGPHelper.getFeignResponseEntity("getExpireSingPassList");
    }

    @Override
    public FeignResponseEntity<List<OrgUserDto>> getUserListByNricAndIdType(String nric, String idType) {
        return IaisEGPHelper.getFeignResponseEntity("getUserListByNricAndIdType", idType);
    }

    @Override
    public FeignResponseEntity<List<OrgUserRoleDto>> retrieveRolesByUserAccId(String userAccId) {
        return IaisEGPHelper.getFeignResponseEntity("retrieveRolesByUserAccId", userAccId);
    }

    @Override
    public FeignResponseEntity<IaisApiResult<Void>> validateSingpassAccount(String idNo, String idType) {
        return IaisEGPHelper.getFeignResponseEntity("validateSingpassAccount", idNo);
    }
}