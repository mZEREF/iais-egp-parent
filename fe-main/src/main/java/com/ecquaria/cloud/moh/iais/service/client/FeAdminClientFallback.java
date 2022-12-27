package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeAdminQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;

public class FeAdminClientFallback implements FeAdminClient{

    @Override
    public FeignResponseEntity<SearchResult<FeAdminQueryDto>> getFeAdminList(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("getFeAdminList", searchParam);
    }

    @Override
    public FeignResponseEntity<OrganizationDto> getOrganizationById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getOrganizationById", id);
    }

    @Override
    public FeignResponseEntity<List<FeUserDto>> getAccountByOrgId(String orgId) {
        return IaisEGPHelper.getFeignResponseEntity("getAccountByOrgId", orgId);
    }

    @Override
    public FeignResponseEntity<FeUserDto> addAdminAccount(FeUserDto feUserDto) {
        return IaisEGPHelper.getFeignResponseEntity("addAdminAccount", feUserDto);
    }

    @Override
    public FeignResponseEntity<String> ChangeActiveStatus(String id, String targetStatus) {
        return IaisEGPHelper.getFeignResponseEntity("ChangeActiveStatus", targetStatus);
    }

    @Override
    public FeignResponseEntity<LicenseeDto> getLicenseeById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenseeById", id);
    }

    @Override
    public FeignResponseEntity<List<LicenseeDto>> getLicenseeByOrgId(String orgId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenseeByOrgId", orgId);
    }

    @Override
    public FeignResponseEntity<List<LicenseeKeyApptPersonDto>> getLicenseeKeyApptPersonDtoListByLicenseeId(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenseeKeyApptPersonDtoListByLicenseeId", licenseeId);
    }

    @Override
    public FeignResponseEntity<List<LicenseeKeyApptPersonDto>> getPersonByid(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getPersonByid", id);
    }

    @Override
    public FeignResponseEntity<Void> updateLicence(LicenseeDto licenseeDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateLicence", licenseeDto);
    }

    @Override
    public FeignResponseEntity<LicenseeDto> getLicenseeByUserAccountInfo(String userAccountString) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenseeByUserAccountInfo", userAccountString);
    }
}
