package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;

/**
 * LicenceViewService
 *
 * @author suocheng
 * @date 2/18/2020
 */

public interface LicenceViewService {
    public LicenceViewDto getLicenceViewDtoByLicenceId(String licenceId);
    public LicenseeDto getLicenseeDtoBylicenseeId(String licenseeId);
}
