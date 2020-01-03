package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.LicenceTenShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskLicenceTenureSerice;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2020/1/3 17:00
 */
@Service
@Slf4j
public class HcsaRiskLicenceTenureSericeImpl implements HcsaRiskLicenceTenureSerice {
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Override
    public LicenceTenShowDto getTenShowDto() {
        List<HcsaServiceDto> serviceDtoList = hcsaConfigClient.getActiveServices().getEntity();
        LicenceTenShowDto showDto =  hcsaConfigClient.getLicenceTenureShow(serviceDtoList).getEntity();
        return showDto;
    }
}
