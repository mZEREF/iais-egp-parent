package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;

import java.util.List;

public interface ArDataSubmissionService {

    List<AppGrpPremisesDto> getAppGrpPremises(String licenseeId, String serviceName);

}
