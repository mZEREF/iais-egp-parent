package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.dto.HcsaServiceDto;

import java.util.List;

/**
 * ServiceConfigService
 *
 * @author suocheng
 * @date 10/14/2019
 */
public interface ServiceConfigService {
    public List<HcsaServiceDto> getHcsaServiceDtosById(List<String> ids);
}
