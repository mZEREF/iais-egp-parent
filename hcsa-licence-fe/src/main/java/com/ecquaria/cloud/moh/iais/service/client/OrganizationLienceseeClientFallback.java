package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeIndividualDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoring.excel.MonitoringSheetsDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;

/**
 * OrganizationLienceseeClientFallback
 *
 * @author caijing
 * @date 2020/1/15
 */
public class OrganizationLienceseeClientFallback implements OrganizationLienceseeClient{
    @Override
    public FeignResponseEntity<List<LicenseeKeyApptPersonDto>> getLicenseeKeyApptPersonDtoListByUen(String uenNo) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenseeKeyApptPersonDtoListByUen",uenNo);
    }

    @Override
    public FeignResponseEntity<List<LicenseeKeyApptPersonDto>> getLicenseeKeyApptPersonDtoListByLicenseeId(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenseeKeyApptPersonDtoListByLicenseeId",licenseeId);
    }

    @Override
    public FeignResponseEntity<LicenseeIndividualDto> getlicIndByNric(String nric) {
        return IaisEGPHelper.getFeignResponseEntity("getlicIndByNric",nric);
    }

    @Override
    public FeignResponseEntity<LicenseeDto> getLicenseeById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenseeById",id);
    }

    @Override
    public FeignResponseEntity<LicenseeDto> getLicenseeByUenNo(String uenNo) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenseeByUenNo",uenNo);
    }

    @Override
    public FeignResponseEntity<LicenseeDto> getLicenseeByOrgId(String orgId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenseeByOrgId",orgId);
    }

    @Override
    public FeignResponseEntity<List<OrgUserDto>> retrieveOrgUserAccount(List<String> ids) {
        return IaisEGPHelper.getFeignResponseEntity("retrieveOrgUserAccount",ids);
    }

    @Override
    public FeignResponseEntity<LicPremisesReqForInfoDto> acceptLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        return IaisEGPHelper.getFeignResponseEntity("acceptLicPremisesReqForInfo",licPremisesReqForInfoDto);
    }

    @Override
    public FeignResponseEntity<List<String>> getAdminEmailAdd(String orgId) {
        return IaisEGPHelper.getFeignResponseEntity("getAdminEmailAdd",orgId);
    }

    @Override
    public FeignResponseEntity<List<String>> getAdminOfficerEmailAdd(String orgId) {
        return IaisEGPHelper.getFeignResponseEntity("getAdminOfficerEmailAdd",orgId);
    }

    @Override
    public FeignResponseEntity<List<FeUserDto>> getAccountByOrgId(String orgId) {
        return IaisEGPHelper.getFeignResponseEntity("getAccountByOrgId",orgId);
    }

    @Override
    public FeignResponseEntity<List<FeUserDto>> getFeUserDtoByLicenseeId(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("getFeUserDtoByLicenseeId",licenseeId);
    }

    @Override
    public FeignResponseEntity<LicenseeDto> getLicenseeDtoById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenseeDtoById",id);
    }

    @Override
    public FeignResponseEntity<OrgUserDto> retrieveOneOrgUserAccount(String user_id) {
        return IaisEGPHelper.getFeignResponseEntity("retrieveOneOrgUserAccount",user_id);
    }

    @Override
    public FeignResponseEntity<OrganizationDto> findOrganizationByUen(String uen) {
        return IaisEGPHelper.getFeignResponseEntity("findOrganizationByUen",uen);
    }
    @Override
    public FeignResponseEntity<OrganizationDto> getOrganizationDtoByLicenseeId(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("getOrganizationDtoByLicenseeId",licenseeId);
    }

    @Override
    public FeignResponseEntity<MonitoringSheetsDto> getMonitoringUserSheetsDto() {
        return IaisEGPHelper.getFeignResponseEntity("getMonitoringUserSheetsDto");
    }
}
