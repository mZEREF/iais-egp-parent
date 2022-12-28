package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import java.io.IOException;
import java.util.List;

/**
 * LicenceViewService
 *
 * @author suocheng
 * @date 12/27/2022
 */

public interface LicenceViewPrintService {
    LicenceViewDto getLicenceViewDtoByLicenceId(String licenceId);
    byte[] printToPdf(List<String> licenceIds) throws IOException;
}
