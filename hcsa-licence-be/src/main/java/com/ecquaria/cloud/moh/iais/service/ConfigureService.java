package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkloadDto;

import java.util.List;

/**
 * @author Guyin
 * @date 2019/11/26 14:09
 **/

public interface ConfigureService {

    /**
     *
     * backend method: listStage
     */
    List<HcsaSvcRoutingStageDto> listStage();

    /**
     *
     * backend method: serviceInStage
     */
    List<HcsaSvcStageWorkloadDto> serviceInStage(String stageCode);

    /**
     *
     * backend method: saveStage
     */
    void saveStage(List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtoList );
}
