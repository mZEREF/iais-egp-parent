package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.service.WorkloadManhoursService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * WorkingGroupServiceImpl
 *
 * @author guyin
 * @date 12/10/2019
 */
@Service
public class WorkloadManhoursServiceImpl implements WorkloadManhoursService {

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Override
    public List<HcsaSvcSpecificStageWorkloadDto> calculateWorkload(String serviceId) {

        return  hcsaConfigClient.calculateWorkload(serviceId).getEntity();
    }

    @Override
    public List<HcsaSvcSpePremisesTypeDto> applicationPremisesByIds(List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtoList){
        return  hcsaConfigClient.applicationPremisesByIds(hcsaSvcSpecificStageWorkloadDtoList).getEntity();
    }

}
