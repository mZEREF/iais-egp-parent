package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * LicenceViewServiceImpl
 *
 * @author suocheng
 * @date 2/18/2020
 */
@Service
@Slf4j
public class LicenceViewServiceImpl implements LicenceViewService {

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private OrganizationLienceseeClient organizationLienceseeClient;

    @Override
    public LicenseeDto getLicenseeDtoBylicenseeId(String licenseeId) {
        if (StringUtil.isEmpty(licenseeId)) {
            log.info("The licensee id is null");
            return null;
        }
        return organizationLienceseeClient.getLicenseeById(licenseeId).getEntity();
    }

    @Override
    public OrganizationDto getOrganizationDtoByLicenseeId(String licenseeId) {
        return organizationLienceseeClient.getOrganizationDtoByLicenseeId(licenseeId).getEntity();
    }

    @Override
    public List<LicenceDto> getLicenceDtoByLicenseeId(String licenseeId){
        return licenceClient.getLicenceDtoByLicenseeId(licenseeId).getEntity();
    }

    @Override
    @SearchTrack(catalog = "applicationQuery", key = "getLicenceBySerName")
    public SearchResult<MenuLicenceDto> getMenuLicence(SearchParam searchParam){
        return licenceClient.getMenuLicence(searchParam).getEntity();
    }

    @Override
    public List<SubLicenseeDto> getSubLicenseeDto(String orgId) {
        return licenceClient.getSubLicensees(orgId,null).getEntity();
    }

    @Override
    public SubLicenseeDto getSubLicenseesById(String id) {
        return licenceClient.getSubLicenseesById(id).getEntity();
    }
}
