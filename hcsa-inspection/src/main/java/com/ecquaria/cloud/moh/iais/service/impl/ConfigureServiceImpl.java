package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.ConfigureService;
import lombok.extern.slf4j.Slf4j;
import net.sf.oval.constraint.Max;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ConfigureServiceImpl implements ConfigureService {

    @Override
    public List<HcsaSvcRoutingStageDto> listStage(){
        return RestApiUtil.getList("hcsa-config:8878/hcsa-routing/stagelist",HcsaSvcRoutingStageDto.class);
    }

    @Override
    public List<HcsaSvcStageWorkloadDto> serviceInStage(String stageCode){
        Map<String,Object> map = new HashMap<>();
        map.put("code",stageCode);
        return RestApiUtil.getListByReqParam("hcsa-config:8878/hcsa-routing/list-workload",map,HcsaSvcStageWorkloadDto.class);
    }

    @Override
    public boolean saveStage(Map<String , List<HcsaSvcSpecificStageWorkloadDto>> map ){
        RestApiUtil.postGetObject("hcsa-config:8878/hcsa-routing/save-stage",map,HcsaSvcSpecificStageWorkloadDto.class);
        return true;
    }
}
