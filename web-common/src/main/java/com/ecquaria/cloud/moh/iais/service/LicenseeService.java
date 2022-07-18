package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;

import java.util.List;

/**
 * LicenseeService
 *
 * @author suocheng
 * @date 3/12/2020
 */

public interface LicenseeService {
    LicenseeDto getLicenseeDtoById(String licenseeId);
    List<String> getLicenseeEmails(String licenseeId);
    List<OrgUserDto> getLicenseeAccountByRolesAndLicenseeId(String licenseeId, List<String> roleIds);
    List<String> getLicenseeMobiles(String licenseeId);
}
