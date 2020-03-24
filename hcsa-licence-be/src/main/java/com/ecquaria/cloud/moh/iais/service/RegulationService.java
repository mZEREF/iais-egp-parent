package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;

/**
 * @author: yichen
 * @description:
 * @date:2020/3/23
 **/

public interface RegulationService {

    IaisApiResult<HcsaChklSvcRegulationDto> createRegulation(HcsaChklSvcRegulationDto regulationDto);

    void deleteRegulation(String id);

    HcsaChklSvcRegulationDto updateRegulation(HcsaChklSvcRegulationDto regulationDto);
}
