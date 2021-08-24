package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;

import java.util.List;

/**
 * LicenceViewService
 *
 * @author suocheng
 * @date 2/18/2020
 */

public interface LicenceViewService {

    LicenseeDto getLicenseeDtoBylicenseeId(String licenseeId);

    OrganizationDto getOrganizationDtoByLicenseeId(String licenseeId);

    List<LicenceDto> getLicenceDtoByLicenseeId(String licenseeId);

    SearchResult<MenuLicenceDto> getMenuLicence(SearchParam searchParam);

    List<SubLicenseeDto> getSubLicenseeDto(String orgId);

    SubLicenseeDto getSubLicenseesById(String id);

}
