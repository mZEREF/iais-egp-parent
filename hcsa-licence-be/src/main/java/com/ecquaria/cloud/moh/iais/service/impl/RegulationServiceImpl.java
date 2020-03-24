package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.service.RegulationService;
import org.springframework.stereotype.Service;

/**
 * @author: yichen
 * @description:
 * @date:2020/3/23
 **/

@Service
public class RegulationServiceImpl implements RegulationService {


    @Override
    public IaisApiResult<HcsaChklSvcRegulationDto> createRegulation(HcsaChklSvcRegulationDto regulationDto) {
        return null;
    }

    @Override
    public void deleteRegulation(String id) {

    }

    @Override
    public HcsaChklSvcRegulationDto updateRegulation(HcsaChklSvcRegulationDto regulationDto) {
        return null;
    }
}
