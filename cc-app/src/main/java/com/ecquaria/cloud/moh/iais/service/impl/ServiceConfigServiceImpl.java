package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ServiceConfigServiceImpl
 *
 * @author suocheng
 * @date 10/14/2019
 */
@Service
public class ServiceConfigServiceImpl implements ServiceConfigService {
    @Override
    public List<HcsaServiceDto> getHcsaServiceDtosById(List<String> ids) {
        List<HcsaServiceDto> result = new ArrayList<>();
        HcsaServiceDto hcsaServiceDto = new HcsaServiceDto();
        hcsaServiceDto.setSvcName("Clinical Laboratory");
        hcsaServiceDto.setSvcCode("CL");
        hcsaServiceDto.setSvcType("Base");
        result.add(hcsaServiceDto);
        HcsaServiceDto hcsaServiceDto1 = new HcsaServiceDto();
        hcsaServiceDto1.setSvcName("Blood Banking");
        hcsaServiceDto1.setSvcCode("BB");
        hcsaServiceDto1.setSvcType("Base");
        result.add(hcsaServiceDto1);
        return result;
    }
}
