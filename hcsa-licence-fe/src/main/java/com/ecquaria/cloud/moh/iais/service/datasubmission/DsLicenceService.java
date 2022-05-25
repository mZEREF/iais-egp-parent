package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;

import java.util.List;
import java.util.Map;

/**
 * {@code DsLicenceService}
 *
 * @Auther chenlei on 1/13/2022.
 */
public interface DsLicenceService {

    Map<String, PremisesDto> getArCenterPremises(String licenseeId);

    Map<String, PremisesDto> getDpCenterPremises(String licenseeId);

    List<PremisesDto> getArCenterPremiseList(String orgId);

    List<PremisesDto> getLdtCenterPremiseList(String orgId);

    PremisesDto getArPremisesDto(String orgId, String hciCode);

    String getCounselling();
}
