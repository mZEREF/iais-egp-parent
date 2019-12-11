package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.service.ConfigureService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConfigureServiceImpl implements ConfigureService {
    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Override
    public List<HcsaSvcRoutingStageDto> listStage(){
        return hcsaConfigClient.stagelist().getEntity();
    }

    @Override
    public List<HcsaSvcStageWorkloadDto> serviceInStage(String stageCode){
        Map<String,Object> map = new HashMap<>();
        map.put("code",stageCode);
        return hcsaConfigClient.listHcsaSvcStageWorkloadEntity(stageCode).getEntity();
    }

    @Override
    public boolean saveStage(Map<String , List<HcsaSvcSpecificStageWorkloadDto>> map ){
        hcsaConfigClient.saveStage(map);
        return true;
    }
}
