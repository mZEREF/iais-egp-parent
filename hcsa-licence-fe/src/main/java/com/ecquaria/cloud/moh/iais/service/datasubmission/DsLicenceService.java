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

    Map<String, PremisesDto> getVssCenterPremises(String licenseeId);

    Map<String, PremisesDto> getTopCenterPremises(String licenseeId);

    List<PremisesDto> getArCenterPremiseList(String orgId);

    PremisesDto getArPremisesDto(String orgId, String hciCode);

    String getCounselling();
}
