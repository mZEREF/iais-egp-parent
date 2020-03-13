package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;


/**
 * InspEmailService
 *
 * @author junyu
 * @date 2020/02/23
 */
public interface LicInspNcEmailService {

    LicenceViewDto getLicenceDtoByLicPremCorrId(String licPremCorrId);



}
