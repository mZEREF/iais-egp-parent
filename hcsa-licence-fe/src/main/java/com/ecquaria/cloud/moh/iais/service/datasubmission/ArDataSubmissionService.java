package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;

import java.util.List;
import java.util.Map;

public interface ArDataSubmissionService {

    Map<String,AppGrpPremisesDto> getAppGrpPremises(String licenseeId, String serviceName);

}
