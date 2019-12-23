package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskLeaderShipShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskLeaderShipService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2019/12/23 13:53
 */
@Service
@Slf4j
public class HcsaRiskLeaderShipServiceImpl implements HcsaRiskLeaderShipService {
    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Override
    public RiskLeaderShipShowDto getLeaderShowDto() {
        List<HcsaServiceDto> serviceDtoList = hcsaConfigClient.getActiveServices().getEntity();
        RiskLeaderShipShowDto showDto  = hcsaConfigClient.getRiskLeaderShipShow(serviceDtoList).getEntity();
        return showDto;
    }
}
