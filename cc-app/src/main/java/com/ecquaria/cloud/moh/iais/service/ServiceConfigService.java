package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.HcsaServiceDto;

import java.util.List;

/**
 * ServiceConfigService
 *
 * @author suocheng
 * @date 10/14/2019
 */
public interface ServiceConfigService {
    List<HcsaServiceDto> getHcsaServiceDtosById(List<String> ids);
}
