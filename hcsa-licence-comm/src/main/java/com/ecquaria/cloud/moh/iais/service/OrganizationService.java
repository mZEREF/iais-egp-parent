package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;

import java.util.List;

/**
 * @Auther chenlei on 5/4/2022.
 */
public interface OrganizationService {

    List<FeUserDto> getFeUserDtoByLicenseeId(String licenseeId);

    LicenseeDto getLicenseeById(String licenseeId);

    SubLicenseeDto getSubLicenseeByLicenseeId(String licenseeId);

    boolean isGiroAccount(String licenseeId);

    OrgUserDto retrieveOrgUserAccountById(String id);

}
