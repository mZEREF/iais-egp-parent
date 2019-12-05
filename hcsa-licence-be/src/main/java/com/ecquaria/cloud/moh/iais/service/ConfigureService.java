package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkloadDto;

import java.util.List;
import java.util.Map;

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
     * backend method: listStage
     */
    List<HcsaSvcStageWorkloadDto> serviceInStage(String stageCode);

    /**
     *
     * backend method: listStage
     */
     boolean saveStage(Map<String , List<HcsaSvcSpecificStageWorkloadDto>> map );
}
