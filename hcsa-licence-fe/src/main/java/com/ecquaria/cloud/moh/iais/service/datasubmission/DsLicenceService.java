package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;

import java.util.Map;

/**
 * {@code DsLicenceService}
 *
 * @Auther chenlei on 1/13/2022.
 */
public interface DsLicenceService {

    Map<String, PremisesDto> getArCenterPremises(String licenseeId);

    Map<String, PremisesDto> getDpCenterPremises(String licenseeId);
}
