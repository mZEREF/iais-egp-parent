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
        hcsaServiceDto.setId("AA1A7D00-2AEB-E911-BE76-000C29C8FBE4");
        hcsaServiceDto.setSvcName("Clinical Laboratory");
        hcsaServiceDto.setSvcCode("CL");
        hcsaServiceDto.setSvcType("Base");
        result.add(hcsaServiceDto);
        HcsaServiceDto hcsaServiceDto1 = new HcsaServiceDto();
        hcsaServiceDto1.setId("C3E7715A-29EB-E911-BE76-000C29C8FBE4");
        hcsaServiceDto1.setSvcName("Blood Banking");
        hcsaServiceDto1.setSvcCode("BB");
        hcsaServiceDto1.setSvcType("Base");
        result.add(hcsaServiceDto1);
        return result;
    }
}
