package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;

/**
 * LicenseeService
 *
 * @author suocheng
 * @date 3/12/2020
 */

public interface LicenseeService {
    public LicenseeDto getLicenseeDtoById(String licenseeId);
}
