package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;

/**
 * LicenceViewService
 *
 * @author suocheng
 * @date 12/27/2022
 */

public interface LicenceViewPrintService {
    LicenceViewDto getLicenceViewDtoByLicenceId(String licenceId);
}
