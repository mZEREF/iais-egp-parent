package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskLegislativeShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskLegislativeService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2019/12/24 19:12
 */
@Service
@Slf4j
public class HcsaRiskLegislativeServiceImpl implements HcsaRiskLegislativeService {
    @Autowired
    HcsaConfigClient hcsaConfigClient;

    @Override
    public RiskLegislativeShowDto getLegShowDto() {
        List<HcsaServiceDto> serviceDtoList =  hcsaConfigClient.getActiveServices().getEntity();
        RiskLegislativeShowDto showDto = hcsaConfigClient.getLegislativeShow(serviceDtoList).getEntity();
        return showDto;
    }
}
