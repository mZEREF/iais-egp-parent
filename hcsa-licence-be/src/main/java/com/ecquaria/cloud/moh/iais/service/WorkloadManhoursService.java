package com.ecquaria.cloud.moh.iais.service;

/*
 *author: yichen
 *date time:12/10/2019 1:00 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;

import java.util.List;

public interface WorkloadManhoursService {

    public List<HcsaSvcSpecificStageWorkloadDto> calculateWorkload(String serviceId);

    public List<HcsaSvcSpePremisesTypeDto> applicationPremisesByIds(List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtoList);
}
